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
package org.openmbee.gearshift.kerml.generator

import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.openmbee.gearshift.kerml.KerMLModel
import org.openmbee.gearshift.kerml.TestLogExtension
import java.io.File
import kotlin.test.assertContains
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

private val logger = KotlinLogging.logger {}

/**
 * Tests for KerMLWriter - the KerML text generator.
 */
@ExtendWith(TestLogExtension::class)
class KerMLWriterTest {

    @Test
    fun `generate simple class`() {
        val kerml = """
            package Vehicles {
                class Vehicle;
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        logger.debug { "Generated KerML:\n$generated" }

        assertContains(generated, "package Vehicles")
        assertContains(generated, "class Vehicle")
    }

    @Test
    fun `generate class with features`() {
        // Define our own types to avoid library dependencies
        val kerml = """
            package Vehicles {
                datatype Real;
                class Vehicle {
                    feature mass : Real;
                    feature speed : Real;
                }
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        logger.debug { "Generated KerML:\n$generated" }

        assertContains(generated, "package Vehicles")
        assertContains(generated, "class Vehicle")
        assertContains(generated, "mass")
        assertContains(generated, "speed")
    }

    @Test
    fun `generate abstract class with specialization`() {
        val kerml = """
            package Vehicles {
                abstract class Vehicle;
                class Car :> Vehicle;
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        // Write generated output to a file for inspection
        val outputDir = File("build/generated-kerml")
        outputDir.mkdirs()

        val writer = KerMLWriter()
        val generated = writer.write(model)

        // Write to file for inspection
        File(outputDir, "abstract-class-with-specialization.kerml").writeText(generated)
        logger.debug { "Generated KerML written to: build/generated-kerml/abstract-class-with-specialization.kerml\n$generated" }

        assertContains(generated, "abstract class Vehicle")
        assertContains(generated, "class Car")
        // Should contain specialization syntax
        assertTrue(generated.contains(":>") || generated.contains("specializes"))
    }

    @Test
    fun `round-trip parse and generate`() {
        // Define our own types to avoid library dependencies
        val originalKerml = """
            package Example {
                datatype String;
                abstract class Base {
                    feature id : String;
                }
                class Derived :> Base {
                    feature name : String;
                }
            }
        """.trimIndent()

        // Parse original
        val model1 = KerMLModel().parseString(originalKerml)
        assertNotNull(model1, "First parse should succeed")

        // Generate
        val writer = KerMLWriter()
        val generatedKerml = writer.write(model1)

        logger.debug { "Original KerML:\n$originalKerml\n\nGenerated KerML:\n$generatedKerml" }

        // Parse generated
        val model2 = KerMLModel().parseString(generatedKerml)
        assertNotNull(model2, "Second parse should succeed - generated KerML should be valid")
    }

    @Test
    fun `generate with verbose options`() {
        // Define B first so A can specialize it
        val kerml = """
            package Test {
                class B;
                class A :> B;
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        // Use verbose writer (keyword syntax)
        val writer = KerMLWriter.verbose()
        val generated = writer.write(model)

        logger.debug { "Generated KerML (verbose):\n$generated" }

        // In verbose mode, should use 'specializes' instead of ':>'
        assertContains(generated, "specializes")
    }

    @Test
    fun `generate compact output`() {
        val kerml = """
            package Test {
                class A;
                class B;
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        // Use compact writer (no blank lines)
        val writer = KerMLWriter.compact()
        val generated = writer.write(model)

        logger.debug { "Generated KerML (compact):\n$generated" }

        // Should not have consecutive blank lines
        val consecutiveBlankLines = generated.contains("\n\n\n")
        assertTrue(!consecutiveBlankLines, "Compact mode should not have excessive blank lines")
    }
}
