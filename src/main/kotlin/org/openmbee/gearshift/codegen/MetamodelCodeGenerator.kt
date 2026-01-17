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
package org.openmbee.gearshift.codegen

import org.openmbee.gearshift.engine.MetamodelRegistry
import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaOperation
import org.openmbee.gearshift.metamodel.MetaProperty
import java.nio.file.Files
import java.nio.file.Path

/**
 * Generates typed Kotlin interfaces and implementation classes from MetaClass definitions.
 *
 * Generated implementations wrap MDMObject and delegate operations to the engine:
 * - Derived properties delegate to engine.getProperty() for constraint evaluation
 * - Regular properties use wrapped.getProperty() directly
 * - Operations delegate to engine.invokeOperation()
 */
class MetamodelCodeGenerator(
    private val config: CodeGenConfig
) {

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

        // Generate Wrappers factory
        val wrappersCode = generateWrappers(classes)
        val wrappersFile = config.utilOutputDir.resolve("Wrappers.kt")
        Files.writeString(wrappersFile, wrappersCode)
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

        // Properties
        val ownProperties = metaClass.attributes.sortedBy { it.name }
        for (property in ownProperties) {
            sb.appendLine()
            sb.append(generateInterfaceProperty(property))
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
        sb.appendLine("import org.openmbee.gearshift.GearshiftEngine")
        sb.appendLine("import org.openmbee.gearshift.engine.MDMObject")
        sb.appendLine("import ${config.interfacePackage}.*")
        sb.appendLine("import ${config.utilPackage}.Wrappers")
        // Add explicit import for classes that conflict with Kotlin stdlib
        if (metaClass.name == "Function" || metaClass.superclasses.contains("Function")) {
            sb.appendLine("import ${config.interfacePackage}.Function as KerMLFunction")
        }
        sb.appendLine()

        // KDoc
        if (config.generateDocs && metaClass.description != null) {
            sb.appendLine("/**")
            sb.appendLine(" * Implementation of ${metaClass.name}.")
            sb.appendLine(" * ${metaClass.description}")
            sb.appendLine(" */")
        }

        // Class declaration
        val openModifier = if (metaClass.isAbstract) "abstract" else "open"
        val superclass = if (metaClass.superclasses.isNotEmpty()) {
            "${metaClass.superclasses.first()}Impl(wrapped, engine)"
        } else {
            "BaseModelElementImpl(wrapped, engine)"
        }

        // Use alias for classes that conflict with Kotlin stdlib
        val interfaceName = if (metaClass.name == "Function") "KerMLFunction" else metaClass.name

        sb.appendLine("$openModifier class ${metaClass.name}Impl(")
        sb.appendLine("    wrapped: MDMObject,")
        sb.appendLine("    engine: GearshiftEngine")
        sb.appendLine(") : $superclass, $interfaceName {")

        // Collect own properties
        val ownProperties = metaClass.attributes.sortedBy { it.name }

        // Collect inherited properties from secondary superclasses (not covered by primary impl chain)
        val primaryChainProperties = collectPrimaryChainPropertyNames(metaClass, registry)
        val secondaryInheritedProperties = collectSecondaryInheritedProperties(metaClass, registry, primaryChainProperties)

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

        // Collect own operations
        val ownOperations = metaClass.operations.sortedBy { it.name }

        // Collect inherited operations from secondary superclasses
        val primaryChainOperations = collectPrimaryChainOperationNames(metaClass, registry)
        val secondaryInheritedOperations = collectSecondaryInheritedOperations(metaClass, registry, primaryChainOperations)

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
     * Generate implementation property.
     *
     * Key distinction:
     * - Derived properties delegate to engine.getProperty() for constraint evaluation
     * - Regular properties use wrapped.getProperty() directly for efficiency
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
            // Regular properties can access wrapped directly
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

        sb.appendLine("        get() {")
        sb.appendLine("            val rawValue = engine.getProperty(wrapped.id!!, \"${property.name}\")")

        if (property.isMultiValued) {
            val emptyCollection = if (isSet) "emptySet()" else "emptyList()"
            val toCollection = if (isSet) "?.toSet()" else ""
            if (isReference) {
                sb.appendLine("            return (rawValue as? List<*>)")
                sb.appendLine("                ?.filterIsInstance<MDMObject>()")
                sb.appendLine("                ?.map { Wrappers.wrap(it, engine) as ${property.type} }$toCollection")
                sb.appendLine("                ?: $emptyCollection")
            } else {
                sb.appendLine("            @Suppress(\"UNCHECKED_CAST\")")
                sb.appendLine("            return (rawValue as? $kotlinType) ?: $emptyCollection")
            }
        } else {
            if (isReference) {
                if (isNullable) {
                    sb.appendLine("            return (rawValue as? MDMObject)?.let { Wrappers.wrap(it, engine) as ${property.type} }")
                } else {
                    sb.appendLine("            return (rawValue as MDMObject).let { Wrappers.wrap(it, engine) as ${property.type} }")
                }
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
     * Generate getter for regular properties that accesses wrapped directly.
     */
    private fun generateRegularPropertyGetter(property: MetaProperty, isReference: Boolean): String {
        val sb = StringBuilder()
        val kotlinType = TypeMapper.mapPropertyType(property)
        val isNullable = !property.isRequired || property.isMultiValued
        val baseType = kotlinType.removeSuffix("?")
        val isSet = property.isUnique && !property.isOrdered

        sb.appendLine("        get() {")
        sb.appendLine("            val rawValue = wrapped.getProperty(\"${property.name}\")")

        if (property.isMultiValued) {
            val emptyCollection = if (isSet) "emptySet()" else "emptyList()"
            val toCollection = if (isSet) "?.toSet()" else ""
            if (isReference) {
                sb.appendLine("            return (rawValue as? List<*>)")
                sb.appendLine("                ?.filterIsInstance<MDMObject>()")
                sb.appendLine("                ?.map { Wrappers.wrap(it, engine) as ${property.type} }$toCollection")
                sb.appendLine("                ?: $emptyCollection")
            } else {
                sb.appendLine("            @Suppress(\"UNCHECKED_CAST\")")
                sb.appendLine("            return (rawValue as? $kotlinType) ?: $emptyCollection")
            }
        } else {
            if (isReference) {
                if (isNullable) {
                    sb.appendLine("            return (rawValue as? MDMObject)?.let { Wrappers.wrap(it, engine) as ${property.type} }")
                } else {
                    sb.appendLine("            return (rawValue as MDMObject).let { Wrappers.wrap(it, engine) as ${property.type} }")
                }
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

        if (property.isMultiValued) {
            if (isReference) {
                sb.appendLine("            val rawValue = value.map { (it as BaseModelElementImpl).wrapped }")
                sb.appendLine("            engine.setProperty(wrapped.id!!, \"${property.name}\", rawValue)")
            } else {
                sb.appendLine("            engine.setProperty(wrapped.id!!, \"${property.name}\", value)")
            }
        } else {
            if (isReference) {
                sb.appendLine("            val rawValue = (value as? BaseModelElementImpl)?.wrapped")
                sb.appendLine("            engine.setProperty(wrapped.id!!, \"${property.name}\", rawValue)")
            } else {
                sb.appendLine("            engine.setProperty(wrapped.id!!, \"${property.name}\", value)")
            }
        }

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

        // Abstract operations have no body
        if (operation.isAbstract) {
            sb.appendLine("    abstract override fun ${operation.name}($params): $returnType")
            return sb.toString()
        }

        sb.appendLine("    override fun ${operation.name}($params): $returnType {")

        // Build arguments map
        if (operation.parameters.isEmpty()) {
            sb.appendLine("        val result = engine.invokeOperation(wrapped.id!!, \"${operation.name}\")")
        } else {
            val argsMap = operation.parameters.joinToString(", ") { "\"${it.name}\" to ${it.name}" }
            sb.appendLine("        val result = engine.invokeOperation(wrapped.id!!, \"${operation.name}\", mapOf($argsMap))")
        }

        // Return conversion
        if (returnType == "Unit") {
            // No return value
        } else if (isMultiValued) {
            // Multi-valued return type
            val elementType = operation.returnType!!
            if (isReference) {
                sb.appendLine("        return (result as? List<*>)")
                sb.appendLine("            ?.filterIsInstance<MDMObject>()")
                sb.appendLine("            ?.map { Wrappers.wrap(it, engine) as $elementType }")
                sb.appendLine("            ?: emptyList()")
            } else {
                sb.appendLine("        @Suppress(\"UNCHECKED_CAST\")")
                sb.appendLine("        return (result as? $returnType) ?: emptyList()")
            }
        } else if (isReference) {
            // Single reference type
            if (isNullable) {
                sb.appendLine("        return (result as? MDMObject)?.let { Wrappers.wrap(it, engine) as ${operation.returnType} }")
            } else {
                sb.appendLine("        return (result as MDMObject).let { Wrappers.wrap(it, engine) as ${operation.returnType} }")
            }
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
     * Generate the Wrappers factory object.
     */
    fun generateWrappers(classes: Collection<MetaClass>): String {
        val sb = StringBuilder()

        // File header
        sb.appendLine(config.fileHeader)
        sb.appendLine()

        // Package
        sb.appendLine("package ${config.utilPackage}")
        sb.appendLine()

        // Imports
        sb.appendLine("import org.openmbee.gearshift.GearshiftEngine")
        sb.appendLine("import org.openmbee.gearshift.engine.MDMObject")
        sb.appendLine("import ${config.interfacePackage}.ModelElement")
        sb.appendLine("import ${config.implPackage}.*")
        sb.appendLine()

        sb.appendLine("/**")
        sb.appendLine(" * Factory for wrapping MDMObject instances in typed wrappers.")
        sb.appendLine(" */")
        sb.appendLine("object Wrappers {")
        sb.appendLine()
        sb.appendLine("    /**")
        sb.appendLine("     * Wrap an MDMObject in its corresponding typed wrapper.")
        sb.appendLine("     */")
        sb.appendLine("    fun wrap(obj: MDMObject, engine: GearshiftEngine): ModelElement {")
        sb.appendLine("        return when (obj.className) {")

        // Generate cases for all non-abstract classes
        val concreteClasses = classes.filter { !it.isAbstract }.sortedBy { it.name }
        for (metaClass in concreteClasses) {
            sb.appendLine("            \"${metaClass.name}\" -> ${metaClass.name}Impl(obj, engine)")
        }

        sb.appendLine("            else -> BaseModelElementImpl(obj, engine)")
        sb.appendLine("        }")
        sb.appendLine("    }")
        sb.appendLine()

        sb.appendLine("    /**")
        sb.appendLine("     * Wrap an MDMObject with explicit type parameter.")
        sb.appendLine("     */")
        sb.appendLine("    inline fun <reified T : ModelElement> wrapAs(obj: MDMObject, engine: GearshiftEngine): T {")
        sb.appendLine("        return wrap(obj, engine) as T")
        sb.appendLine("    }")
        sb.appendLine("}")
        sb.appendLine()

        return sb.toString()
    }

    /**
     * Generate the base ModelElement interface and implementation.
     */
    fun generateBaseClasses(): Pair<String, String> {
        val interfaceCode = buildString {
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

        val implCode = buildString {
            appendLine(config.fileHeader)
            appendLine()
            appendLine("package ${config.implPackage}")
            appendLine()
            appendLine("import org.openmbee.gearshift.GearshiftEngine")
            appendLine("import org.openmbee.gearshift.engine.MDMObject")
            appendLine("import ${config.interfacePackage}.ModelElement")
            appendLine()
            appendLine("/**")
            appendLine(" * Base implementation for all generated model element wrappers.")
            appendLine(" */")
            appendLine("open class BaseModelElementImpl(")
            appendLine("    protected val wrapped: MDMObject,")
            appendLine("    protected val engine: GearshiftEngine")
            appendLine(") : ModelElement {")
            appendLine()
            appendLine("    override val id: String?")
            appendLine("        get() = wrapped.id")
            appendLine()
            appendLine("    override val className: String")
            appendLine("        get() = wrapped.className")
            appendLine()
            appendLine("    override fun equals(other: Any?): Boolean {")
            appendLine("        if (this === other) return true")
            appendLine("        if (other !is BaseModelElementImpl) return false")
            appendLine("        return wrapped.id == other.wrapped.id")
            appendLine("    }")
            appendLine()
            appendLine("    override fun hashCode(): Int = wrapped.id?.hashCode() ?: 0")
            appendLine()
            appendLine("    override fun toString(): String = \"\${className}(\${id})\"")
            appendLine("}")
            appendLine()
        }

        return interfaceCode to implCode
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

    companion object {
        /**
         * Create a generator with default configuration.
         */
        fun withDefaults(outputDir: Path): MetamodelCodeGenerator {
            return MetamodelCodeGenerator(CodeGenConfig.default(outputDir))
        }
    }
}
