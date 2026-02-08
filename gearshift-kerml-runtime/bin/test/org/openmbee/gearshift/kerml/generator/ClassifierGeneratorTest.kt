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
import kotlin.test.assertTrue

class ClassifierGeneratorTest {

    @Test
    fun `generate behavior`() {
        val kerml = """
            package Test {
                behavior DoSomething;
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        println("Generated KerML:")
        println(generated)

        assertContains(generated, "behavior DoSomething")
    }

    @Test
    fun `generate function`() {
        val kerml = """
            package Test {
                function Add;
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        println("Generated KerML:")
        println(generated)

        assertContains(generated, "function Add")
    }

    @Test
    fun `generate predicate`() {
        val kerml = """
            package Test {
                predicate IsValid;
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        println("Generated KerML:")
        println(generated)

        assertContains(generated, "predicate IsValid")
    }

    @Test
    fun `generate interaction`() {
        val kerml = """
            package Test {
                interaction Handshake;
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        println("Generated KerML:")
        println(generated)

        assertContains(generated, "interaction Handshake")
    }

    @Test
    fun `generate assoc struct`() {
        val kerml = """
            package Test {
                assoc struct Link;
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        println("Generated KerML:")
        println(generated)

        assertContains(generated, "assoc struct Link")
    }

    @Test
    fun `generate abstract behavior with specialization`() {
        val kerml = """
            package Test {
                abstract behavior Action;
                behavior SpecialAction :> Action;
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        println("Generated KerML:")
        println(generated)

        assertContains(generated, "abstract behavior Action")
        assertContains(generated, "behavior SpecialAction")
        assertTrue(generated.contains(":>") || generated.contains("specializes"))
    }

    @Test
    fun `generate doc comment`() {
        val kerml = """
            package Test {
                class Foo {
                    doc /* This is documentation */
                }
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        println("Generated KerML:")
        println(generated)

        assertContains(generated, "class Foo")
        assertContains(generated, "doc")
        assertContains(generated, "This is documentation")
    }

    @Test
    fun `round-trip behavior and function`() {
        val kerml = """
            package Behaviors {
                abstract behavior Action;
                behavior SpecialAction :> Action;
                function Compute;
                predicate IsReady;
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        println("Generated KerML:")
        println(generated)

        // Verify round-trip parsability
        val model2 = KerMLModel().parseString(generated)
        assertNotNull(model2, "Generated KerML should be parseable")
    }
}
