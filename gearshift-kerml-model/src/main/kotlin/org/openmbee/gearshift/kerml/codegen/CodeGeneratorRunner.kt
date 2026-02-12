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
package org.openmbee.gearshift.kerml.codegen

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.gearshift.kerml.KerMLMetamodelLoader
import org.openmbee.gearshift.kerml.ViewsExtensionLoader
import org.openmbee.mdm.framework.codegen.CodeGenConfig
import org.openmbee.mdm.framework.codegen.MetamodelCodeGenerator
import org.openmbee.mdm.framework.codegen.TypeScriptCodeGenConfig
import org.openmbee.mdm.framework.codegen.TypeScriptCodeGenerator
import org.openmbee.mdm.framework.runtime.DefaultElementFactory
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MetamodelRegistry
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

private val logger = KotlinLogging.logger {}

/**
 * Main entry point for running the metamodel code generator.
 *
 * This can be invoked by Gradle to generate typed Kotlin interfaces
 * and implementations from the KerML metamodel.
 *
 * This runner is in the metamodel module (not runtime) to avoid circular
 * dependencies with the generated code.
 *
 * Usage:
 * ```
 * ./gradlew generateMetamodelCode
 * ```
 */
object CodeGeneratorRunner {

    @JvmStatic
    fun main(args: Array<String>) {
        val mode = System.getProperty("gearshift.codegen.mode", "kotlin")

        if (mode == "typescript") {
            generateTypeScript(args)
            return
        }

        // Default: Kotlin code generation
        val outputDir = if (args.isNotEmpty()) {
            Paths.get(args[0])
        } else {
            // Default to kerml-generated module
            Paths.get("kerml-generated/src/main/kotlin")
        }

        logger.info { "Generating metamodel code to: $outputDir" }
        generate(outputDir)
        logger.info { "Code generation complete!" }
    }

    /**
     * Generate all metamodel code to the specified output directory.
     */
    fun generate(outputDir: Path) {
        // Initialize engine with KerML metamodel using DefaultElementFactory
        // (we can't use KerMLElementFactory because it hasn't been generated yet)
        val schema = MetamodelRegistry()
        KerMLMetamodelLoader.initialize(schema)
        ViewsExtensionLoader.initialize(schema)
        val engine = MDMEngine(schema, DefaultElementFactory())

        // Configure the generator
        val config = CodeGenConfig(
            outputDir = outputDir,
            metamodelName = "KerML",
            interfacePackage = "org.openmbee.gearshift.generated.interfaces",
            implPackage = "org.openmbee.gearshift.generated.impl",
            utilPackage = "org.openmbee.gearshift.generated",
            generateDocs = true
        )

        val generator = MetamodelCodeGenerator(config)

        // Clean output directories first
        generator.cleanOutputDirectories()

        // Generate base classes first
        generateBaseClasses(generator, config)

        // Generate all metamodel code
        generator.generateAll(engine.schema)

        // Print statistics
        val stats = KerMLMetamodelLoader.getStatistics(engine.schema)
        logger.info { "Generated code for ${stats["total"]} metamodel classes: root=${stats["root"]}, core=${stats["core"]}, kernel=${stats["kernel"]}" }
    }

    private fun generateBaseClasses(generator: MetamodelCodeGenerator, config: CodeGenConfig) {
        // Generate the base ModelElement interface
        val interfaceCode = generator.generateBaseClasses()

        // Write base interface
        Files.createDirectories(config.interfaceOutputDir)
        Files.writeString(config.interfaceOutputDir.resolve("ModelElement.kt"), interfaceCode)
    }

    /**
     * Generate TypeScript interfaces from the KerML metamodel.
     *
     * Usage:
     * ```
     * ./gradlew generateTypeScriptTypes
     * ```
     */
    @JvmStatic
    fun generateTypeScript(args: Array<String>) {
        val outputDir = if (args.isNotEmpty()) {
            Paths.get(args[0])
        } else {
            Paths.get("build/generated-ts")
        }

        logger.info { "Generating TypeScript types to: $outputDir" }

        val schema = MetamodelRegistry()
        KerMLMetamodelLoader.initialize(schema)
        ViewsExtensionLoader.initialize(schema)

        val config = TypeScriptCodeGenConfig(
            outputDir = outputDir,
            metamodelName = "KerML",
            generateDocs = true
        )

        val generator = TypeScriptCodeGenerator(config)
        generator.generateAll(schema)

        // Print statistics
        val stats = KerMLMetamodelLoader.getStatistics(schema)
        val allClasses = schema.getAllClasses().filter { config.shouldGenerate(it.name) }
        val concreteCount = allClasses.count { !it.isAbstract }
        val abstractCount = allClasses.count { it.isAbstract }

        logger.info { "TypeScript generation complete! Total interfaces: ${allClasses.size}, abstract: $abstractCount, concrete (in union type): $concreteCount, output: ${config.modelFileName}, ${config.metaclassTypeFileName}" }
    }
}
