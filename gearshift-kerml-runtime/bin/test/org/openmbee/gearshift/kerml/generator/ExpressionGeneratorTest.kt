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

import org.junit.jupiter.api.Test
import org.openmbee.gearshift.kerml.KerMLModel
import kotlin.test.assertContains
import kotlin.test.assertNotNull

class ExpressionGeneratorTest {

    @Test
    fun `generate feature with multiplicity bounds`() {
        val kerml = """
            package Test {
                datatype Integer;
                class Foo {
                    feature items : Integer [0..*];
                }
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        println("Generated KerML:")
        println(generated)

        assertContains(generated, "items")
        assertContains(generated, "Integer")
    }

    @Test
    fun `generate feature with single multiplicity`() {
        val kerml = """
            package Test {
                datatype String;
                class Bar {
                    feature name : String [1];
                }
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        println("Generated KerML:")
        println(generated)

        assertContains(generated, "name")
        assertContains(generated, "String")
    }

    @Test
    fun `generate feature with default value`() {
        val kerml = """
            package Test {
                datatype Integer;
                class Config {
                    feature count : Integer default = 42;
                }
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        println("Generated KerML:")
        println(generated)

        assertContains(generated, "count")
        assertContains(generated, "Integer")
    }

    @Test
    fun `generate feature with value binding`() {
        val kerml = """
            package Test {
                datatype Boolean;
                class Flags {
                    feature enabled : Boolean = true;
                }
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        println("Generated KerML:")
        println(generated)

        assertContains(generated, "enabled")
        assertContains(generated, "Boolean")
    }

    @Test
    fun `round-trip feature with string value`() {
        val kerml = """
            package Test {
                datatype String;
                class Greeting {
                    feature message : String default = "hello";
                }
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        println("Generated KerML:")
        println(generated)

        assertContains(generated, "message")
        assertContains(generated, "String")

        // Verify round-trip parsability
        val model2 = KerMLModel().parseString(generated)
        assertNotNull(model2, "Generated KerML should be parseable")
    }
}
