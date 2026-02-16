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
import org.openmbee.mdm.framework.meta.MetaProperty
import org.openmbee.mdm.framework.runtime.MetamodelRegistry
import org.openmbee.mdm.framework.runtime.MetamodelRegistry.Companion.DEFAULT_BASE_CLASS
import java.nio.file.Files
import java.nio.file.Path

/**
 * Generates TypeScript interfaces from MetaClass definitions.
 *
 * Produces two files:
 * 1. `kerml.model.ts` — TypeScript interface declarations for all metaclasses
 * 2. `kerml-metaclass.type.ts` — Discriminated union type of all concrete metaclass names
 *
 * Association ends are mapped to `ElementRef` (ID-based references) since the
 * frontend communicates with the server via the SysML v2 API.
 */
class TypeScriptCodeGenerator(
    private val config: TypeScriptCodeGenConfig
) {

    /**
     * Generate all TypeScript files for a metamodel registry.
     */
    fun generateAll(registry: MetamodelRegistry) {
        Files.createDirectories(config.outputDir)

        val classes = registry.getAllClasses().filter { config.shouldGenerate(it.name) }

        // Generate the main model file with all interfaces
        val modelCode = generateModelFile(classes, registry)
        Files.writeString(config.outputDir.resolve(config.modelFileName), modelCode)

        // Generate the metaclass discriminated union type
        val metaclassCode = generateMetaclassTypeFile(classes)
        Files.writeString(config.outputDir.resolve(config.metaclassTypeFileName), metaclassCode)
    }

    /**
     * Generate the main model file containing all interface declarations.
     */
    fun generateModelFile(classes: Collection<MetaClass>, registry: MetamodelRegistry): String {
        val sb = StringBuilder()

        // File header
        sb.appendLine(config.fileHeader)
        sb.appendLine()

        // Import the metaclass type and ElementRef
        val metaclassTypeName = "${config.metamodelName}Metaclass"
        sb.appendLine("import { $metaclassTypeName } from './${config.metaclassTypeFileName.removeSuffix(".ts")}';")
        sb.appendLine()

        // ElementRef type for association ends
        sb.appendLine("/**")
        sb.appendLine(" * Reference to another element by ID.")
        sb.appendLine(" * Used for association ends in the SysML v2 API.")
        sb.appendLine(" */")
        sb.appendLine("export interface ElementRef {")
        sb.appendLine("  '@id': string;")
        sb.appendLine("}")
        sb.appendLine()

        // Sort classes in topological order so extends references are valid
        val sorted = topologicalSort(classes, registry)

        // Generate each interface
        for (metaClass in sorted) {
            sb.append(generateInterface(metaClass, registry))
            sb.appendLine()
        }

        return sb.toString()
    }

    /**
     * Generate a single TypeScript interface declaration.
     */
    fun generateInterface(metaClass: MetaClass, registry: MetamodelRegistry): String {
        val sb = StringBuilder()

        // JSDoc
        if (config.generateDocs && metaClass.description != null) {
            sb.appendLine("/**")
            sb.appendLine(" * ${metaClass.description}")
            sb.appendLine(" */")
        }

        // Interface declaration with extends
        val generatedSuperclasses = metaClass.superclasses.filter { it != DEFAULT_BASE_CLASS }
        sb.append("export interface ${metaClass.name}")
        if (generatedSuperclasses.isNotEmpty()) {
            sb.append(" extends ${generatedSuperclasses.joinToString(", ")}")
        }
        sb.appendLine(" {")

        // For the root element (no superclasses), add @id and @type fields
        if (generatedSuperclasses.isEmpty()) {
            sb.appendLine("  '@id': string;")
            sb.appendLine("  '@type': ${config.metamodelName}Metaclass;")
        }

        // Properties (attributes) - alphabetical order per CLAUDE.md
        val ownProperties = metaClass.attributes.sortedBy { it.name }
        for (property in ownProperties) {
            sb.append(generateProperty(property))
        }

        // Association ends - alphabetical order per CLAUDE.md
        val ownAssociationEnds = registry.getNavigableEndsForClass(metaClass.name)
            .sortedBy { it.name }
        for (end in ownAssociationEnds) {
            sb.append(generateAssociationEnd(end))
        }

        sb.appendLine("}")

        return sb.toString()
    }

    /**
     * Generate a TypeScript property declaration from a MetaProperty.
     */
    private fun generateProperty(property: MetaProperty): String {
        val sb = StringBuilder()

        // JSDoc
        if (config.generateDocs && property.description != null) {
            sb.appendLine("  /** ${property.description} */")
        }

        val readonly = if (property.isDerived || property.isReadOnly) "readonly " else ""
        val optional = if (!property.isRequired && !property.isMultiValued) "?" else ""
        val tsType = TypeScriptTypeMapper.mapPropertyType(property)

        sb.appendLine("  ${readonly}${property.name}${optional}: ${tsType};")

        return sb.toString()
    }

    /**
     * Generate a TypeScript property declaration from a MetaAssociationEnd.
     */
    private fun generateAssociationEnd(end: MetaAssociationEnd): String {
        val sb = StringBuilder()

        val readonly = if (end.isDerived) "readonly " else ""
        val isMultiValued = end.upperBound == -1 || end.upperBound > 1
        val optional = if (end.lowerBound == 0 && !isMultiValued) "?" else ""
        val tsType = TypeScriptTypeMapper.mapAssociationEndType(end)

        sb.appendLine("  ${readonly}${end.name}${optional}: ${tsType};")

        return sb.toString()
    }

    /**
     * Generate the metaclass discriminated union type file.
     *
     * Only includes non-abstract classes since abstract classes cannot be instantiated.
     */
    fun generateMetaclassTypeFile(classes: Collection<MetaClass>): String {
        val sb = StringBuilder()

        // File header
        sb.appendLine(config.fileHeader)
        sb.appendLine()

        val metaclassTypeName = "${config.metamodelName}Metaclass"

        sb.appendLine("/**")
        sb.appendLine(" * Discriminated union of all concrete ${config.metamodelName} metaclass names.")
        sb.appendLine(" * Used as the '@type' field on element objects from the SysML v2 API.")
        sb.appendLine(" */")

        val concreteClasses = classes
            .filter { !it.isAbstract }
            .map { it.name }
            .sorted()

        if (concreteClasses.isEmpty()) {
            sb.appendLine("export type $metaclassTypeName = string;")
        } else {
            sb.appendLine("export type $metaclassTypeName =")
            for ((index, name) in concreteClasses.withIndex()) {
                val terminator = if (index < concreteClasses.size - 1) "" else ";"
                sb.appendLine("  | '$name'$terminator")
            }
        }

        sb.appendLine()

        return sb.toString()
    }

    /**
     * Sort MetaClasses in topological order so that superclass interfaces
     * are declared before subclass interfaces.
     */
    private fun topologicalSort(
        classes: Collection<MetaClass>,
        registry: MetamodelRegistry
    ): List<MetaClass> {
        val classMap = classes.associateBy { it.name }
        val sorted = mutableListOf<MetaClass>()
        val visited = mutableSetOf<String>()
        val visiting = mutableSetOf<String>() // cycle detection

        fun visit(metaClass: MetaClass) {
            if (metaClass.name in visited) return
            if (metaClass.name in visiting) {
                // Cycle detected — break it by just adding
                visited.add(metaClass.name)
                sorted.add(metaClass)
                return
            }
            visiting.add(metaClass.name)

            // Visit superclasses first
            for (superName in metaClass.superclasses) {
                if (superName == DEFAULT_BASE_CLASS) continue
                val superClass = classMap[superName]
                if (superClass != null) {
                    visit(superClass)
                }
            }

            visiting.remove(metaClass.name)
            visited.add(metaClass.name)
            sorted.add(metaClass)
        }

        for (metaClass in classes) {
            visit(metaClass)
        }

        return sorted
    }

    companion object {
        /**
         * Create a generator with default configuration.
         */
        fun withDefaults(outputDir: Path): TypeScriptCodeGenerator {
            return TypeScriptCodeGenerator(TypeScriptCodeGenConfig(outputDir = outputDir))
        }
    }
}
