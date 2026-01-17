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

import org.openmbee.gearshift.GearshiftEngine
import org.openmbee.gearshift.kerml.KerMLMetamodelLoader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Main entry point for running the metamodel code generator.
 *
 * This can be invoked by Gradle to generate typed Kotlin interfaces
 * and implementations from the KerML metamodel.
 *
 * Usage:
 * ```
 * ./gradlew generateMetamodelCode
 * ```
 */
object CodeGeneratorRunner {

    @JvmStatic
    fun main(args: Array<String>) {
        // Output directory should be the base src directory, not including package path
        val outputDir = if (args.isNotEmpty()) {
            Paths.get(args[0])
        } else {
            Paths.get("src/main/kotlin")
        }

        println("Generating metamodel code to: $outputDir")
        generate(outputDir)
        println("Code generation complete!")
    }

    /**
     * Generate all metamodel code to the specified output directory.
     */
    fun generate(outputDir: Path) {
        // Initialize engine with KerML metamodel
        val engine = GearshiftEngine()
        KerMLMetamodelLoader.initialize(engine)

        // Configure the generator
        val config = CodeGenConfig(
            outputDir = outputDir,
            interfacePackage = "org.openmbee.gearshift.generated.interfaces",
            implPackage = "org.openmbee.gearshift.generated.impl",
            utilPackage = "org.openmbee.gearshift.generated",
            generateDocs = true
        )

        val generator = MetamodelCodeGenerator(config)

        // Generate base classes first
        generateBaseClasses(generator, config)

        // Generate all metamodel code
        generator.generateAll(engine.metamodelRegistry)

        // Print statistics
        val stats = KerMLMetamodelLoader.getStatistics(engine)
        println("Generated code for ${stats["total"]} metamodel classes:")
        println("  - Root package: ${stats["root"]} classes")
        println("  - Core package: ${stats["core"]} classes")
        println("  - Kernel package: ${stats["kernel"]} classes")
    }

    private fun generateBaseClasses(generator: MetamodelCodeGenerator, config: CodeGenConfig) {
        // Generate the base ModelElement interface and implementation
        val (interfaceCode, implCode) = generator.generateBaseClasses()

        // Write base interface
        Files.createDirectories(config.interfaceOutputDir)
        Files.writeString(config.interfaceOutputDir.resolve("ModelElement.kt"), interfaceCode)

        // Write base implementation
        Files.createDirectories(config.implOutputDir)
        Files.writeString(config.implOutputDir.resolve("BaseModelElementImpl.kt"), implCode)
    }
}
