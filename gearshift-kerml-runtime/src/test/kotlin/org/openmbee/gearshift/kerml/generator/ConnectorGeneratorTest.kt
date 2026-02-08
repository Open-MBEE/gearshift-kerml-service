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

class ConnectorGeneratorTest {

    @Test
    fun `generate connector`() {
        val kerml = """
            package Test {
                class A {
                    feature x;
                }
                class B {
                    feature y;
                }
                class System {
                    feature a : A;
                    feature b : B;
                    connector link from a.x to b.y;
                }
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        println("Generated KerML:")
        println(generated)

        assertContains(generated, "connector")
    }

    @Test
    fun `generate binding connector`() {
        val kerml = """
            package Test {
                class Config {
                    feature source;
                    feature target;
                    binding source = target;
                }
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        println("Generated KerML:")
        println(generated)

        assertContains(generated, "binding")
    }

    @Test
    fun `generate succession`() {
        val kerml = """
            package Test {
                behavior Process {
                    step s1;
                    step s2;
                    succession first s1 then s2;
                }
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        println("Generated KerML:")
        println(generated)

        assertContains(generated, "succession")
    }

    @Test
    fun `round-trip connectors with dot paths`() {
        val kerml = """
            package Connectors {
                class A {
                    feature p;
                    feature q;
                }
                class System {
                    feature a1 : A;
                    feature a2 : A;
                    connector c from a1.p to a2.q;
                }
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        println("Generated KerML:")
        println(generated)

        assertContains(generated, "connector c")
        assertContains(generated, "from")
        assertContains(generated, "to")

        val model2 = KerMLModel().parseString(generated)
        assertNotNull(model2, "Generated KerML should be parseable")
    }
}
