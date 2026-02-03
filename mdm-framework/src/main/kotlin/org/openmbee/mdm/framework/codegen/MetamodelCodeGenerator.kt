/*
 * Copyright 2026 Charles Galey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openmbee.mdm.framework.codegen

import org.openmbee.mdm.framework.meta.MetaAssociationEnd
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaOperation
import org.openmbee.mdm.framework.meta.MetaProperty
import org.openmbee.mdm.framework.runtime.MetamodelRegistry
import org.openmbee.mdm.framework.runtime.MetamodelRegistry.Companion.DEFAULT_BASE_CLASS
import java.nio.file.Files
import java.nio.file.Path

/**
 * Generates typed Kotlin interfaces and implementation classes from MetaClass definitions.
 *
 * Generated implementations wrap MDMObject and delegate operations to the engine:
 * - Derived properties delegate to engine.getProperty() for constraint evaluation
 * - Regular properties use getProperty() directly
 * - Operations delegate to engine.invokeOperation()
 */
class MetamodelCodeGenerator(
    private val config: CodeGenConfig
) {

    /**
     * Clean all output directories to remove stale files.
     * Call this before generating to ensure clean output.
     */
    fun cleanOutputDirectories() {
        cleanDirectory(config.interfaceOutputDir)
        cleanDirectory(config.implOutputDir)
        cleanDirectory(config.utilOutputDir)
    }

    /**
     * Generate all code for a metamodel registry.
     */
    fun generateAll(registry: MetamodelRegistry) {
        // Ensure output directories exist
        Files.createDirectories(config.interfaceOutputDir)
        Files.createDirectories(config.implOutputDir)
        Files.createDirectories(config.utilOutputDir)

        // Collect all classes to generate
        val classes = registry.getAllClasses().filter { config.shouldGenerate(it.name) }

        // Generate interfaces and implementations
        for (metaClass in classes) {
            val interfaceCode = generateInterface(metaClass, registry)
            val interfaceFile = config.interfaceOutputDir.resolve("${metaClass.name}.kt")
            Files.writeString(interfaceFile, interfaceCode)

            val implCode = generateImplementation(metaClass, registry)
            val implFile = config.implOutputDir.resolve("${metaClass.name}Impl.kt")
            Files.writeString(implFile, implCode)
        }

        // Generate ElementFactory implementation
        val factoryCode = generateElementFactory(classes, registry)
        val factoryFile = config.utilOutputDir.resolve("${config.metamodelName}ElementFactory.kt")
        Files.writeString(factoryFile, factoryCode)
    }

    /**
     * Generate an interface for a metaclass.
     */
    fun generateInterface(metaClass: MetaClass, registry: MetamodelRegistry): String {
        val sb = StringBuilder()

        // File header
        sb.appendLine(config.fileHeader)
        sb.appendLine()

        // Package
        sb.appendLine("package ${config.interfacePackage}")
        sb.appendLine()

        // KDoc
        if (config.generateDocs && metaClass.description != null) {
            sb.appendLine("/**")
            sb.appendLine(" * ${metaClass.description}")
            sb.appendLine(" */")
        }

        // Interface declaration
        val superInterfaces = metaClass.superclasses.ifEmpty { listOf("ModelElement") }
        sb.append("interface ${metaClass.name}")
        sb.appendLine(" : ${superInterfaces.joinToString(", ")} {")

        // Properties (attributes)
        val ownProperties = metaClass.attributes.sortedBy { it.name }
        for (property in ownProperties) {
            sb.appendLine()
            sb.append(generateInterfaceProperty(property))
        }

        // Association ends (own only, not inherited)
        val ownAssociationEnds = registry.getNavigableEndsForClass(metaClass.name)
            .sortedBy { it.name }
        for (end in ownAssociationEnds) {
            sb.appendLine()
            sb.append(generateInterfaceAssociationEnd(end))
        }

        // Operations
        val ownOperations = metaClass.operations.sortedBy { it.name }
        for (operation in ownOperations) {
            sb.appendLine()
            sb.append(generateInterfaceOperation(operation))
        }

        sb.appendLine("}")
        sb.appendLine()

        return sb.toString()
    }

    /**
     * Generate interface property declaration.
     */
    private fun generateInterfaceProperty(property: MetaProperty): String {
        val sb = StringBuilder()

        // KDoc
        if (config.generateDocs && property.description != null) {
            sb.appendLine("    /**")
            sb.appendLine("     * ${property.description}")
            sb.appendLine("     */")
        }

        val modifier = if (property.isDerived || property.isReadOnly) "val" else "var"
        val kotlinType = TypeMapper.mapPropertyType(property)

        // Add override modifier if this redefines a parent property
        val overrideModifier = if (property.redefines.isNotEmpty()) "override " else ""

        sb.appendLine("    $overrideModifier$modifier ${property.name}: $kotlinType")

        return sb.toString()
    }

    /**
     * Generate interface association end declaration.
     */
    private fun generateInterfaceAssociationEnd(end: MetaAssociationEnd): String {
        val sb = StringBuilder()

        val modifier = if (end.isDerived) "val" else "var"
        val kotlinType = TypeMapper.mapAssociationEndType(end)

        // Add 'override' if this redefines or subsets a property with the same name from a parent interface
        // This handles cases like:
        // - EndFeatureMembership.ownedMemberFeature redefining FeatureMembership.ownedMemberFeature
        // - Redefinition.owningFeature subsetting Subsetting.owningFeature
        val needsOverride = end.redefines.contains(end.name) || end.subsets.contains(end.name)
        val overrideModifier = if (needsOverride) "override " else ""
        sb.appendLine("    $overrideModifier$modifier ${end.name}: $kotlinType")

        return sb.toString()
    }

    /**
     * Generate interface operation declaration.
     */
    private fun generateInterfaceOperation(operation: MetaOperation): String {
        val sb = StringBuilder()

        // KDoc
        if (config.generateDocs && operation.description != null) {
            sb.appendLine("    /**")
            sb.appendLine("     * ${operation.description}")
            sb.appendLine("     */")
        }

        // Use the new type mapping that respects lowerBound/upperBound for parameters
        val params = operation.parameters.joinToString(", ") { param ->
            "${param.name}: ${TypeMapper.mapParameterType(param)}"
        }
        // Use the new type mapping that respects returnLowerBound/returnUpperBound
        val returnType = TypeMapper.mapOperationReturnType(operation)

        // Add override modifier if this redefines a parent operation
        val overrideModifier = if (operation.redefines != null) "override " else ""

        sb.appendLine("    ${overrideModifier}fun ${operation.name}($params): $returnType")

        return sb.toString()
    }

    /**
     * Generate implementation class for a metaclass.
     */
    fun generateImplementation(metaClass: MetaClass, registry: MetamodelRegistry): String {
        val sb = StringBuilder()

        // File header
        sb.appendLine(config.fileHeader)
        sb.appendLine()

        // Package
        sb.appendLine("package ${config.implPackage}")
        sb.appendLine()

        // Imports
        sb.appendLine("import org.openmbee.mdm.framework.runtime.MDMEngine")
        sb.appendLine("import org.openmbee.mdm.framework.meta.MetaClass as FrameworkMetaClass")
        sb.appendLine("import org.openmbee.mdm.framework.runtime.MDMObject")
        sb.appendLine("import org.openmbee.mdm.framework.runtime.OwnershipResolver")
        sb.appendLine("import ${config.interfacePackage}.*")
        // Add explicit imports for classes that conflict with Kotlin stdlib
        sb.appendLine("import ${config.interfacePackage}.Annotation as KerMLAnnotation")
        sb.appendLine("import ${config.interfacePackage}.Function as KerMLFunction")
        sb.appendLine()

        // KDoc
        if (config.generateDocs && metaClass.description != null) {
            sb.appendLine("/**")
            sb.appendLine(" * Implementation of ${metaClass.name}.")
            sb.appendLine(" * ${metaClass.description}")
            sb.appendLine(" */")
        }

        // Use alias for classes that conflict with Kotlin stdlib
        val interfaceName = TypeMapper.getAliasedTypeName(metaClass.name)
        val openModifier = if (metaClass.isAbstract) "abstract" else "open"
        // Only the true root class (MDMBaseClass, with no superclasses) extends MDMObject directly
        val isRootClass = metaClass.superclasses.isEmpty()

        if (isRootClass) {
            // Root class (MDMBaseClass) extends MDMObject directly and defines engine
            sb.appendLine("$openModifier class ${metaClass.name}Impl(")
            sb.appendLine("    className: String,")
            sb.appendLine("    metaClass: FrameworkMetaClass,")
            sb.appendLine("    internal val engine: MDMEngine")
            sb.appendLine(") : MDMObject(className, metaClass), $interfaceName {")
        } else {
            // Non-root class extends parent impl (Element extends MDMBaseClassImpl, etc.)
            val parentImpl = "${metaClass.superclasses.first()}Impl"
            sb.appendLine("$openModifier class ${metaClass.name}Impl(")
            sb.appendLine("    className: String,")
            sb.appendLine("    metaClass: FrameworkMetaClass,")
            sb.appendLine("    engine: MDMEngine")
            sb.appendLine(") : $parentImpl(className, metaClass, engine), $interfaceName {"
            )
        }
        sb.appendLine()

        // Creation constructor - creates new instance with optional parent and properties
        if (!metaClass.isAbstract) {
            sb.append(generateCreationConstructor(metaClass, registry))
        }

        // Collect own properties
        val ownProperties = metaClass.attributes.sortedBy { it.name }

        // Collect inherited properties from secondary superclasses (not covered by primary impl chain)
        val primaryChainProperties = collectPrimaryChainPropertyNames(metaClass, registry)
        val secondaryInheritedProperties =
            collectSecondaryInheritedProperties(metaClass, registry, primaryChainProperties)

        // Generate own properties
        for (property in ownProperties) {
            sb.appendLine()
            sb.append(generateImplProperty(property))
        }

        // Generate inherited properties from secondary superclasses
        for (property in secondaryInheritedProperties.sortedBy { it.name }) {
            sb.appendLine()
            sb.append(generateImplProperty(property))
        }

        // Generate own association ends
        val ownAssociationEnds = registry.getNavigableEndsForClass(metaClass.name)
            .sortedBy { it.name }
        for (end in ownAssociationEnds) {
            sb.appendLine()
            sb.append(generateImplAssociationEnd(end))
        }

        // Generate inherited association ends from secondary superclasses
        val primaryChainEndNames = collectPrimaryChainAssociationEndNames(metaClass, registry)
        val secondaryInheritedEnds = collectSecondaryInheritedAssociationEnds(metaClass, registry, primaryChainEndNames)
        for (end in secondaryInheritedEnds.sortedBy { it.name }) {
            sb.appendLine()
            sb.append(generateImplAssociationEnd(end))
        }

        // Collect own operations
        val ownOperations = metaClass.operations.sortedBy { it.name }

        // Collect inherited operations from secondary superclasses
        val primaryChainOperations = collectPrimaryChainOperationNames(metaClass, registry)
        val secondaryInheritedOperations =
            collectSecondaryInheritedOperations(metaClass, registry, primaryChainOperations)

        // Generate own operations
        for (operation in ownOperations) {
            sb.appendLine()
            sb.append(generateImplOperation(operation))
        }

        // Generate inherited operations from secondary superclasses
        for (operation in secondaryInheritedOperations.sortedBy { it.name }) {
            sb.appendLine()
            sb.append(generateImplOperation(operation))
        }

        sb.appendLine("}")
        sb.appendLine()

        return sb.toString()
    }

    /**
     * Generate a creation constructor for a concrete class.
     *
     * The constructor uses a FIXED signature based on Element's properties to reduce churn.
     * All concrete classes have the same constructor parameters:
     * - engine: MDMEngine (required)
     * - parent: Element? (optional, for establishing ownership)
     * - Element's settable properties (declaredName, declaredShortName, aliasIds, elementId, isImpliedIncluded)
     */
    /**
     * Generate a creation constructor for a concrete class.
     *
     * The constructor uses a FIXED signature based on the root element's properties to reduce churn.
     * All concrete classes have the same constructor parameters derived from the metamodel's
     * root element (the abstract class with no superclasses).
     */
    private fun generateCreationConstructor(metaClass: MetaClass, registry: MetamodelRegistry): String {
        val sb = StringBuilder()

        // Find the root element class (abstract with no superclasses)
        val rootClass = findRootElementClass(registry)
        val rootClassName = rootClass?.name ?: "Element"
        val rootTypeName = TypeMapper.getAliasedTypeName(rootClassName)

        // Get settable properties from root class
        val rootProperties = rootClass?.attributes
            ?.filter { !it.isDerived && !it.isReadOnly }
            ?.sortedBy { it.name }
            ?: emptyList()

        sb.appendLine("    /**")
        sb.appendLine("     * Create a new ${metaClass.name} instance.")
        sb.appendLine("     * @param parent The owning $rootClassName (optional)")
        sb.appendLine("     */")

        // Constructor signature based on root element's settable properties
        sb.appendLine("    constructor(")
        sb.appendLine("        engine: MDMEngine,")
        sb.appendLine("        parent: $rootTypeName? = null,")

        for ((index, prop) in rootProperties.withIndex()) {
            val kotlinType = TypeMapper.mapPropertyType(prop)
            val defaultValue = getPropertyDefaultValue(prop)
            val comma = if (index < rootProperties.size - 1) "," else ""
            sb.appendLine("        ${prop.name}: $kotlinType = $defaultValue$comma")
        }

        sb.appendLine("    ) : this(\"${metaClass.name}\", engine.schema.getClass(\"${metaClass.name}\")!!, engine) {")

        // Generate ID and register with engine immediately
        // Property setters use engine.setProperty(id!!, ...) which requires element to be registered
        sb.appendLine("        this.id = java.util.UUID.randomUUID().toString()")
        sb.appendLine("        engine.registerElement(this)")
        sb.appendLine()

        // Set root element properties (after registration)
        for (prop in rootProperties) {
            if (prop.isRequired && !prop.isMultiValued) {
                sb.appendLine("        this.${prop.name} = ${prop.name}")
            } else if (prop.isMultiValued) {
                sb.appendLine("        if (${prop.name}.isNotEmpty()) this.${prop.name} = ${prop.name}")
            } else {
                sb.appendLine("        ${prop.name}?.let { this.${prop.name} = it }")
            }
        }
        sb.appendLine()
        sb.appendLine("        // Establish ownership via appropriate intermediate")
        sb.appendLine("        parent?.let { owner ->")
        sb.appendLine("            val resolver = OwnershipResolver(engine.schema)")
        sb.appendLine("            val resolved = resolver.resolve(owner.className, \"${metaClass.name}\")")
        sb.appendLine("            if (resolved != null) {")
        sb.appendLine("                val membership = engine.createElement(resolved.intermediateType)")
        sb.appendLine("                engine.setProperty(membership.id!!, resolved.binding.ownedElementEnd, this)")
        sb.appendLine("                engine.setProperty(membership.id!!, resolved.binding.ownerEnd, owner)")
        sb.appendLine("                // Set member names on membership for navigation")
        sb.appendLine("                declaredName?.let { engine.setProperty(membership.id!!, \"memberName\", it) }")
        sb.appendLine("                declaredShortName?.let { engine.setProperty(membership.id!!, \"memberShortName\", it) }")
        sb.appendLine("            }")
        sb.appendLine("        }")

        sb.appendLine("    }")
        sb.appendLine()

        return sb.toString()
    }

    /**
     * Find the root element class in the metamodel.
     * The root is the abstract class with no generated superclasses that other classes inherit from.
     * (MDMBaseClass is filtered out since it's a framework class, not a generated one)
     */
    private fun findRootElementClass(registry: MetamodelRegistry): MetaClass? {
        return registry.getAllClasses()
            .filter { metaClass ->
                metaClass.isAbstract &&
                metaClass.name != DEFAULT_BASE_CLASS &&
                metaClass.superclasses.filter { it != DEFAULT_BASE_CLASS }.isEmpty()
            }
            .firstOrNull()
    }

    /**
     * Get the default value for a property parameter.
     */
    private fun getPropertyDefaultValue(property: MetaProperty): String {
        return when {
            property.isMultiValued -> if (property.isUnique && !property.isOrdered) "emptySet()" else "emptyList()"
            !property.isRequired -> "null"
            property.type == "Boolean" -> "false"
            property.type == "Integer" || property.type == "Int" -> "0"
            property.type == "String" -> "\"\""
            else -> "null"
        }
    }

    /**
     * Generate implementation property.
     *
     * Key distinction:
     * - Derived properties delegate to engine.getProperty() for constraint evaluation
     * - Regular properties use getProperty() directly for efficiency
     */
    private fun generateImplProperty(property: MetaProperty): String {
        val sb = StringBuilder()

        val modifier = if (property.isDerived || property.isReadOnly) "val" else "var"
        val kotlinType = TypeMapper.mapPropertyType(property)
        val isReference = TypeMapper.isModelElementType(property.type)

        sb.appendLine("    override $modifier ${property.name}: $kotlinType")

        if (property.isDerived) {
            // Derived properties MUST go through engine.getProperty() to trigger constraint evaluation
            sb.append(generateDerivedPropertyGetter(property, isReference))
        } else {
            // Regular properties can access getProperty directly
            sb.append(generateRegularPropertyGetter(property, isReference))

            if (!property.isReadOnly) {
                sb.append(generatePropertySetter(property, isReference))
            }
        }

        return sb.toString()
    }

    /**
     * Generate getter for derived properties that delegates to engine.getProperty().
     */
    private fun generateDerivedPropertyGetter(property: MetaProperty, isReference: Boolean): String {
        val sb = StringBuilder()
        val kotlinType = TypeMapper.mapPropertyType(property)
        val isNullable = !property.isRequired || property.isMultiValued
        val baseType = kotlinType.removeSuffix("?")
        val isSet = property.isUnique && !property.isOrdered
        val castType = TypeMapper.getAliasedTypeName(property.type)

        sb.appendLine("        get() {")
        sb.appendLine("            val rawValue = engine.getProperty(id!!, \"${property.name}\")")

        if (property.isMultiValued) {
            val emptyCollection = if (isSet) "emptySet()" else "emptyList()"
            val toCollection = if (isSet) "?.toSet()" else ""
            if (isReference) {
                // Direct cast - impl classes ARE the model objects
                sb.appendLine("            return (rawValue as? List<*>)?.filterIsInstance<$castType>()$toCollection ?: $emptyCollection")
            } else {
                sb.appendLine("            @Suppress(\"UNCHECKED_CAST\")")
                sb.appendLine("            return (rawValue as? $kotlinType) ?: $emptyCollection")
            }
        } else {
            if (isReference) {
                // Direct cast - impl classes ARE the model objects
                sb.appendLine("            return rawValue as? $castType")
            } else {
                if (isNullable) {
                    sb.appendLine("            return rawValue as? $baseType")
                } else {
                    // Non-nullable primitive - provide default value
                    val defaultValue = getRequiredDefaultValue(property.type)
                    sb.appendLine("            return (rawValue as? $baseType) ?: $defaultValue")
                }
            }
        }

        sb.appendLine("        }")

        return sb.toString()
    }

    /**
     * Generate getter for regular properties that accesses getProperty directly.
     */
    private fun generateRegularPropertyGetter(property: MetaProperty, isReference: Boolean): String {
        val sb = StringBuilder()
        val kotlinType = TypeMapper.mapPropertyType(property)
        val isNullable = !property.isRequired || property.isMultiValued
        val baseType = kotlinType.removeSuffix("?")
        val isSet = property.isUnique && !property.isOrdered
        val castType = TypeMapper.getAliasedTypeName(property.type)

        sb.appendLine("        get() {")
        sb.appendLine("            val rawValue = getProperty(\"${property.name}\")")

        if (property.isMultiValued) {
            val emptyCollection = if (isSet) "emptySet()" else "emptyList()"
            val toCollection = if (isSet) "?.toSet()" else ""
            if (isReference) {
                // Direct cast - impl classes ARE the model objects
                sb.appendLine("            return (rawValue as? List<*>)?.filterIsInstance<$castType>()$toCollection ?: $emptyCollection")
            } else {
                sb.appendLine("            @Suppress(\"UNCHECKED_CAST\")")
                sb.appendLine("            return (rawValue as? $kotlinType) ?: $emptyCollection")
            }
        } else {
            if (isReference) {
                // Direct cast - impl classes ARE the model objects
                sb.appendLine("            return rawValue as? $castType")
            } else {
                if (isNullable) {
                    sb.appendLine("            return rawValue as? $baseType")
                } else {
                    // Non-nullable primitive - provide default value
                    val defaultValue = getRequiredDefaultValue(property.type)
                    sb.appendLine("            return (rawValue as? $baseType) ?: $defaultValue")
                }
            }
        }

        sb.appendLine("        }")

        return sb.toString()
    }

    /**
     * Generate setter for writable properties.
     */
    private fun generatePropertySetter(property: MetaProperty, isReference: Boolean): String {
        val sb = StringBuilder()

        sb.appendLine("        set(value) {")

        // Impl classes extend MDMObject, so we can pass them directly to engine.setProperty
        sb.appendLine("            engine.setProperty(id!!, \"${property.name}\", value)")

        sb.appendLine("        }")

        return sb.toString()
    }

    /**
     * Generate implementation for an association end.
     *
     * Key distinction:
     * - Derived association ends delegate to engine.getProperty() for constraint evaluation
     * - Regular association ends use getProperty() directly for efficiency
     */
    private fun generateImplAssociationEnd(end: MetaAssociationEnd): String {
        val sb = StringBuilder()

        val modifier = if (end.isDerived) "val" else "var"
        val kotlinType = TypeMapper.mapAssociationEndType(end, useAliases = true)

        sb.appendLine("    override $modifier ${end.name}: $kotlinType")

        if (end.isDerived) {
            sb.append(generateDerivedAssociationEndGetter(end))
        } else {
            sb.append(generateRegularAssociationEndGetter(end))

            // Association ends are always references (not primitives)
            sb.append(generateAssociationEndSetter(end))
        }

        return sb.toString()
    }

    /**
     * Generate getter for derived association ends that delegates to engine.getProperty().
     */
    private fun generateDerivedAssociationEndGetter(end: MetaAssociationEnd): String {
        val sb = StringBuilder()
        val kotlinType = TypeMapper.mapAssociationEndType(end)
        val isMultiValued = end.upperBound == -1 || end.upperBound > 1
        val isNullable = end.lowerBound == 0 && !isMultiValued
        val isSet = end.isUnique && !end.isOrdered
        val castType = TypeMapper.getAliasedTypeName(end.type)

        sb.appendLine("        get() {")
        sb.appendLine("            val rawValue = engine.getProperty(id!!, \"${end.name}\")")

        if (isMultiValued) {
            val emptyCollection = if (isSet) "emptySet()" else "emptyList()"
            val toCollection = if (isSet) "?.toSet()" else ""
            // Direct cast - impl classes ARE the model objects
            sb.appendLine("            return (rawValue as? List<*>)?.filterIsInstance<$castType>()$toCollection ?: $emptyCollection")
        } else if (isNullable) {
            // Direct cast - impl classes ARE the model objects
            sb.appendLine("            return rawValue as? $castType")
        } else {
            // Required (non-nullable) - use !! to match interface type
            sb.appendLine("            return rawValue as $castType")
        }

        sb.appendLine("        }")

        return sb.toString()
    }

    /**
     * Generate getter for regular association ends.
     * Uses engine.getProperty() which knows how to traverse links for association ends.
     */
    private fun generateRegularAssociationEndGetter(end: MetaAssociationEnd): String {
        val sb = StringBuilder()
        val kotlinType = TypeMapper.mapAssociationEndType(end)
        val isMultiValued = end.upperBound == -1 || end.upperBound > 1
        val isNullable = end.lowerBound == 0 && !isMultiValued
        val isSet = end.isUnique && !end.isOrdered
        val castType = TypeMapper.getAliasedTypeName(end.type)

        sb.appendLine("        get() {")
        // Use engine.getProperty() which handles link traversal for association ends
        sb.appendLine("            val rawValue = engine.getProperty(id!!, \"${end.name}\")")

        if (isMultiValued) {
            val emptyCollection = if (isSet) "emptySet()" else "emptyList()"
            val toCollection = if (isSet) "?.toSet()" else ""
            // Direct cast - impl classes ARE the model objects
            sb.appendLine("            return (rawValue as? List<*>)?.filterIsInstance<$castType>()$toCollection ?: $emptyCollection")
        } else if (isNullable) {
            // Single-valued association end - engine.getProperty returns the element directly
            sb.appendLine("            return rawValue as? $castType")
        } else {
            // Required (non-nullable) single-valued association end
            sb.appendLine("            return rawValue as $castType")
        }

        sb.appendLine("        }")

        return sb.toString()
    }

    /**
     * Generate setter for writable association ends.
     */
    private fun generateAssociationEndSetter(end: MetaAssociationEnd): String {
        val sb = StringBuilder()

        sb.appendLine("        set(value) {")
        // Impl classes extend MDMObject, so we can pass them directly to engine.setProperty
        sb.appendLine("            engine.setProperty(id!!, \"${end.name}\", value)")
        sb.appendLine("        }")

        return sb.toString()
    }

    /**
     * Generate implementation operation that delegates to engine.invokeOperation().
     *
     * Handles:
     * - Abstract operations (no body, marked abstract)
     * - Multi-valued return types (List<T>)
     * - Nullable vs non-nullable return types
     */
    private fun generateImplOperation(operation: MetaOperation): String {
        val sb = StringBuilder()

        // Use the new type mapping that respects lowerBound/upperBound for parameters
        val params = operation.parameters.joinToString(", ") { param ->
            "${param.name}: ${TypeMapper.mapParameterType(param)}"
        }
        // Use the new type mapping that respects returnLowerBound/returnUpperBound
        val returnType = TypeMapper.mapOperationReturnType(operation)
        val isReference = operation.returnType?.let { TypeMapper.isModelElementType(it) } ?: false
        val isMultiValued = operation.returnUpperBound == -1 || operation.returnUpperBound > 1
        val isNullable = operation.returnLowerBound == 0 && !isMultiValued
        val castType = operation.returnType?.let { TypeMapper.getAliasedTypeName(it) }

        // Abstract operations have no body
        if (operation.isAbstract) {
            sb.appendLine("    abstract override fun ${operation.name}($params): $returnType")
            return sb.toString()
        }

        sb.appendLine("    override fun ${operation.name}($params): $returnType {")

        // Build arguments map
        if (operation.parameters.isEmpty()) {
            sb.appendLine("        val result = engine.invokeOperation(id!!, \"${operation.name}\")")
        } else {
            val argsMap = operation.parameters.joinToString(", ") { "\"${it.name}\" to ${it.name}" }
            sb.appendLine("        val result = engine.invokeOperation(id!!, \"${operation.name}\", mapOf($argsMap))")
        }

        // Return conversion
        if (returnType == "Unit") {
            // No return value
        } else if (isMultiValued) {
            // Multi-valued return type
            if (isReference && castType != null) {
                // Direct cast - impl classes ARE the model objects
                sb.appendLine("        return (result as? List<*>)?.filterIsInstance<$castType>() ?: emptyList()")
            } else {
                sb.appendLine("        @Suppress(\"UNCHECKED_CAST\")")
                sb.appendLine("        return (result as? $returnType) ?: emptyList()")
            }
        } else if (isReference && castType != null) {
            // Single reference type - direct cast
            sb.appendLine("        return result as? $castType")
        } else {
            // Single primitive type
            if (isNullable) {
                val baseType = returnType.removeSuffix("?")
                sb.appendLine("        return result as? $baseType")
            } else {
                val defaultValue = getRequiredDefaultValue(operation.returnType ?: "Any")
                sb.appendLine("        return (result as? $returnType) ?: $defaultValue")
            }
        }

        sb.appendLine("    }")

        return sb.toString()
    }


    /**
     * Generate an ElementFactory implementation for the metamodel.
     *
     * The generated factory creates typed implementation instances (e.g., PackageImpl)
     * instead of raw MDMObject instances. Only concrete (non-abstract) classes are included.
     *
     * The factory is named using the metamodelName from config (e.g., "KerMLElementFactory")
     * to allow multiple metamodel libraries to coexist.
     */
    fun generateElementFactory(classes: Collection<MetaClass>, registry: MetamodelRegistry): String {
        val sb = StringBuilder()

        // File header
        sb.appendLine(config.fileHeader)
        sb.appendLine()

        // Package
        sb.appendLine("package ${config.utilPackage}")
        sb.appendLine()

        // Imports
        sb.appendLine("import org.openmbee.mdm.framework.meta.MetaClass")
        sb.appendLine("import org.openmbee.mdm.framework.runtime.ElementFactory")
        sb.appendLine("import org.openmbee.mdm.framework.runtime.MDMEngine")
        sb.appendLine("import org.openmbee.mdm.framework.runtime.MDMObject")
        sb.appendLine("import ${config.implPackage}.*")
        sb.appendLine()

        // Factory name from metamodel name (e.g., "KerML" -> "KerMLElementFactory")
        val factoryName = "${config.metamodelName}ElementFactory"

        sb.appendLine("/**")
        sb.appendLine(" * Factory for creating typed ${config.metamodelName} element instances.")
        sb.appendLine(" *")
        sb.appendLine(" * When MDMEngine creates an element, this factory returns the appropriate")
        sb.appendLine(" * typed implementation (e.g., PackageImpl, NamespaceImpl) instead of raw MDMObject.")
        sb.appendLine(" *")
        sb.appendLine(" * This enables:")
        sb.appendLine(" * 1. Type-safe access to element properties via the generated interfaces")
        sb.appendLine(" * 2. Correct typed instances when deserializing from storage")
        sb.appendLine(" * 3. Proper polymorphic behavior through the generated implementation hierarchy")
        sb.appendLine(" */")
        sb.appendLine("class $factoryName : ElementFactory {")
        sb.appendLine()
        sb.appendLine("    override var engine: MDMEngine? = null")
        sb.appendLine()

        sb.appendLine("    override fun createInstance(className: String, metaClass: MetaClass): MDMObject {")
        sb.appendLine("        if (metaClass.isAbstract) {")
        sb.appendLine("            throw IllegalArgumentException(\"Cannot instantiate abstract class: \$className\")")
        sb.appendLine("        }")
        sb.appendLine()
        sb.appendLine("        val eng = engine ?: throw IllegalStateException(\"Engine not set on factory\")")
        sb.appendLine()
        sb.appendLine("        return when (className) {")

        // Generate cases for all non-abstract classes only, sorted by name
        val concreteClasses = classes.filter { !it.isAbstract }.sortedBy { it.name }
        for (metaClass in concreteClasses) {
            sb.appendLine("            \"${metaClass.name}\" -> ${metaClass.name}Impl(className, metaClass, eng)")
        }

        sb.appendLine()
        sb.appendLine("            // Fallback for any unknown concrete class")
        sb.appendLine("            else -> MDMObject(className, metaClass)")
        sb.appendLine("        }")
        sb.appendLine("    }")
        sb.appendLine()

        sb.appendLine("    override fun supportsClass(className: String): Boolean = true")
        sb.appendLine("}")
        sb.appendLine()

        return sb.toString()
    }

    /**
     * Generate the base ModelElement interface.
     * No BaseModelElementImpl is generated - root classes extend MDMObject directly.
     */
    fun generateBaseClasses(): String {
        return buildString {
            appendLine(config.fileHeader)
            appendLine()
            appendLine("package ${config.interfacePackage}")
            appendLine()
            appendLine("/**")
            appendLine(" * Base interface for all generated model element types.")
            appendLine(" */")
            appendLine("interface ModelElement {")
            appendLine("    /**")
            appendLine("     * The unique identifier of this element.")
            appendLine("     */")
            appendLine("    val id: String?")
            appendLine()
            appendLine("    /**")
            appendLine("     * The metaclass name of this element.")
            appendLine("     */")
            appendLine("    val className: String")
            appendLine("}")
            appendLine()
        }
    }

    /**
     * Get default value for required (non-nullable) primitive types.
     */
    private fun getRequiredDefaultValue(type: String): String {
        return when (type) {
            "String" -> "\"\""
            "Boolean" -> "false"
            "Int", "Integer" -> "0"
            "Long" -> "0L"
            "Double", "Real" -> "0.0"
            "Float" -> "0.0f"
            // Enum types mapped to String
            "VisibilityKind", "FeatureDirectionKind", "PortionKind",
            "RequirementConstraintKind", "StateSubactionKind",
            "TransitionFeatureKind", "TriggerKind" -> "\"\""

            else -> "null" // Fallback for unknown types
        }
    }

    // ===== Inheritance Helper Methods =====

    /**
     * Collect all property names from the primary superclass chain.
     * The primary chain is followed by taking the first superclass at each level.
     */
    private fun collectPrimaryChainPropertyNames(metaClass: MetaClass, registry: MetamodelRegistry): Set<String> {
        val names = mutableSetOf<String>()
        // Add own properties
        names.addAll(metaClass.attributes.map { it.name })

        // Walk up the primary superclass chain
        var current: MetaClass? = metaClass
        while (current != null && current.superclasses.isNotEmpty()) {
            val primarySuperclassName = current.superclasses.first()
            val primarySuperclass = registry.getClass(primarySuperclassName)
            if (primarySuperclass != null) {
                names.addAll(primarySuperclass.attributes.map { it.name })
                current = primarySuperclass
            } else {
                break
            }
        }
        return names
    }

    /**
     * Collect all operation names from the primary superclass chain.
     */
    private fun collectPrimaryChainOperationNames(metaClass: MetaClass, registry: MetamodelRegistry): Set<String> {
        val names = mutableSetOf<String>()
        // Add own operations
        names.addAll(metaClass.operations.map { it.name })

        // Walk up the primary superclass chain
        var current: MetaClass? = metaClass
        while (current != null && current.superclasses.isNotEmpty()) {
            val primarySuperclassName = current.superclasses.first()
            val primarySuperclass = registry.getClass(primarySuperclassName)
            if (primarySuperclass != null) {
                names.addAll(primarySuperclass.operations.map { it.name })
                current = primarySuperclass
            } else {
                break
            }
        }
        return names
    }

    /**
     * Collect properties from secondary superclasses (all except the first) that aren't
     * already in the primary chain.
     */
    private fun collectSecondaryInheritedProperties(
        metaClass: MetaClass,
        registry: MetamodelRegistry,
        primaryChainNames: Set<String>
    ): List<MetaProperty> {
        val result = mutableListOf<MetaProperty>()
        val seen = mutableSetOf<String>()
        seen.addAll(primaryChainNames)

        // BFS through all superclasses to find secondary chain properties
        val queue = ArrayDeque<String>()

        // Start with secondary superclasses (skip the first)
        if (metaClass.superclasses.size > 1) {
            queue.addAll(metaClass.superclasses.drop(1))
        }

        // Also check secondary superclasses of ancestors in the primary chain
        var current: MetaClass? = metaClass
        while (current != null && current.superclasses.isNotEmpty()) {
            val primarySuper = registry.getClass(current.superclasses.first())
            if (primarySuper != null && primarySuper.superclasses.size > 1) {
                queue.addAll(primarySuper.superclasses.drop(1))
            }
            current = primarySuper
        }

        // Process all secondary superclasses
        val visitedClasses = mutableSetOf<String>()
        while (queue.isNotEmpty()) {
            val className = queue.removeFirst()
            if (className in visitedClasses) continue
            visitedClasses.add(className)

            val clazz = registry.getClass(className) ?: continue

            // Add properties not yet seen
            for (prop in clazz.attributes) {
                if (prop.name !in seen) {
                    result.add(prop)
                    seen.add(prop.name)
                }
            }

            // Queue this class's superclasses
            queue.addAll(clazz.superclasses)
        }

        return result
    }

    /**
     * Collect operations from secondary superclasses that aren't already in the primary chain.
     */
    private fun collectSecondaryInheritedOperations(
        metaClass: MetaClass,
        registry: MetamodelRegistry,
        primaryChainNames: Set<String>
    ): List<MetaOperation> {
        val result = mutableListOf<MetaOperation>()
        val seen = mutableSetOf<String>()
        seen.addAll(primaryChainNames)

        // BFS through all superclasses to find secondary chain operations
        val queue = ArrayDeque<String>()

        // Start with secondary superclasses (skip the first)
        if (metaClass.superclasses.size > 1) {
            queue.addAll(metaClass.superclasses.drop(1))
        }

        // Also check secondary superclasses of ancestors in the primary chain
        var current: MetaClass? = metaClass
        while (current != null && current.superclasses.isNotEmpty()) {
            val primarySuper = registry.getClass(current.superclasses.first())
            if (primarySuper != null && primarySuper.superclasses.size > 1) {
                queue.addAll(primarySuper.superclasses.drop(1))
            }
            current = primarySuper
        }

        // Process all secondary superclasses
        val visitedClasses = mutableSetOf<String>()
        while (queue.isNotEmpty()) {
            val className = queue.removeFirst()
            if (className in visitedClasses) continue
            visitedClasses.add(className)

            val clazz = registry.getClass(className) ?: continue

            // Add operations not yet seen
            for (op in clazz.operations) {
                if (op.name !in seen) {
                    result.add(op)
                    seen.add(op.name)
                }
            }

            // Queue this class's superclasses
            queue.addAll(clazz.superclasses)
        }

        return result
    }

    /**
     * Collect all association end names from the primary superclass chain.
     * The primary chain is followed by taking the first superclass at each level.
     */
    private fun collectPrimaryChainAssociationEndNames(metaClass: MetaClass, registry: MetamodelRegistry): Set<String> {
        val names = mutableSetOf<String>()

        // Add own association ends
        names.addAll(registry.getNavigableEndsForClass(metaClass.name).map { it.name })

        // Walk up the primary superclass chain
        var current: MetaClass? = metaClass
        while (current != null && current.superclasses.isNotEmpty()) {
            val primarySuperclassName = current.superclasses.first()
            val primarySuperclass = registry.getClass(primarySuperclassName)
            if (primarySuperclass != null) {
                names.addAll(registry.getNavigableEndsForClass(primarySuperclass.name).map { it.name })
                current = primarySuperclass
            } else {
                break
            }
        }
        return names
    }

    /**
     * Collect association ends from secondary superclasses that aren't already in the primary chain.
     */
    private fun collectSecondaryInheritedAssociationEnds(
        metaClass: MetaClass,
        registry: MetamodelRegistry,
        primaryChainNames: Set<String>
    ): List<MetaAssociationEnd> {
        val result = mutableListOf<MetaAssociationEnd>()
        val seen = mutableSetOf<String>()
        seen.addAll(primaryChainNames)

        // BFS through all superclasses to find secondary chain association ends
        val queue = ArrayDeque<String>()

        // Start with secondary superclasses (skip the first)
        if (metaClass.superclasses.size > 1) {
            queue.addAll(metaClass.superclasses.drop(1))
        }

        // Also check secondary superclasses of ancestors in the primary chain
        var current: MetaClass? = metaClass
        while (current != null && current.superclasses.isNotEmpty()) {
            val primarySuper = registry.getClass(current.superclasses.first())
            if (primarySuper != null && primarySuper.superclasses.size > 1) {
                queue.addAll(primarySuper.superclasses.drop(1))
            }
            current = primarySuper
        }

        // Process all secondary superclasses
        val visitedClasses = mutableSetOf<String>()
        while (queue.isNotEmpty()) {
            val className = queue.removeFirst()
            if (className in visitedClasses) continue
            visitedClasses.add(className)

            val clazz = registry.getClass(className) ?: continue

            // Add association ends not yet seen
            for (end in registry.getNavigableEndsForClass(clazz.name)) {
                if (end.name !in seen) {
                    result.add(end)
                    seen.add(end.name)
                }
            }

            // Queue this class's superclasses
            queue.addAll(clazz.superclasses)
        }

        return result
    }

    /**
     * Clean a directory by deleting all .kt files in it.
     */
    private fun cleanDirectory(dir: Path) {
        if (Files.exists(dir)) {
            Files.walk(dir)
                .filter { Files.isRegularFile(it) && it.toString().endsWith(".kt") }
                .forEach { Files.delete(it) }
        }
    }

    companion object {
        /**
         * Create a generator with default configuration.
         */
        fun withDefaults(outputDir: Path): MetamodelCodeGenerator {
            return MetamodelCodeGenerator(CodeGenConfig.default(outputDir))
        }
    }
}
