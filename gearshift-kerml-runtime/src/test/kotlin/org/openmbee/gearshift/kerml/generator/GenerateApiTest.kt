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
import kotlin.test.assertContains
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

private val logger = KotlinLogging.logger {}

@ExtendWith(TestLogExtension::class)
class GenerateApiTest {

    @Test
    fun `round-trip comprehensive model`() {
        val kerml = """
            package ComprehensiveTest {
                datatype Integer;
                datatype String;
                datatype Boolean;

                abstract class Base {
                    feature id : String;
                }

                class Derived :> Base {
                    feature name : String;
                    feature count : Integer;
                }

                struct Point {
                    feature x : Integer;
                    feature y : Integer;
                }

                assoc Connection {
                    end feature source;
                    end feature target;
                }

                behavior Process;
                function Compute;
                predicate IsValid;
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        logger.debug { "Generated KerML:\n$generated" }

        // Verify key elements are present
        assertContains(generated, "package ComprehensiveTest")
        assertContains(generated, "abstract class Base")
        assertContains(generated, "class Derived")
        assertContains(generated, "struct Point")
        assertContains(generated, "assoc Connection")
        assertContains(generated, "behavior Process")
        assertContains(generated, "function Compute")
        assertContains(generated, "predicate IsValid")

        // Verify round-trip
        val model2 = KerMLModel().parseString(generated)
        assertNotNull(model2, "Generated KerML should parse back successfully")

        // Generate again and verify stability
        val generated2 = writer.write(model2)
        val model3 = KerMLModel().parseString(generated2)
        assertNotNull(model3, "Double round-trip should succeed")
    }

    @Test
    fun `generate verbose comprehensive model`() {
        val kerml = """
            package VerboseTest {
                datatype Real;
                abstract class Vehicle {
                    feature mass : Real;
                }
                class Car :> Vehicle;
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter.verbose()
        val generated = writer.write(model)

        logger.debug { "Generated KerML (verbose):\n$generated" }

        assertContains(generated, "specializes")
        assertContains(generated, "abstract class Vehicle")
        assertContains(generated, "class Car")
    }

    @Test
    fun `generate all classifier types`() {
        val kerml = """
            package AllTypes {
                class MyClass;
                datatype MyDataType;
                struct MyStruct;
                assoc MyAssoc;
                assoc struct MyAssocStruct;
                behavior MyBehavior;
                function MyFunction;
                predicate MyPredicate;
                interaction MyInteraction;
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        logger.debug { "Generated KerML (all types):\n$generated" }

        assertContains(generated, "class MyClass")
        assertContains(generated, "datatype MyDataType")
        assertContains(generated, "struct MyStruct")
        assertContains(generated, "assoc MyAssoc")
        assertContains(generated, "assoc struct MyAssocStruct")
        assertContains(generated, "behavior MyBehavior")
        assertContains(generated, "function MyFunction")
        assertContains(generated, "predicate MyPredicate")
        assertContains(generated, "interaction MyInteraction")

        // Round-trip
        val model2 = KerMLModel().parseString(generated)
        assertNotNull(model2, "All-types model should round-trip")
    }
}
