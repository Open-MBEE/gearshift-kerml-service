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

private val logger = KotlinLogging.logger {}

@ExtendWith(TestLogExtension::class)
class ExpressionContainerGeneratorTest {

    @Test
    fun `generate expression container`() {
        val kerml = """
            package Test {
                datatype Integer;
                function Sum {
                    in feature a : Integer;
                    in feature b : Integer;
                    return feature result : Integer;
                }
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        logger.debug { "Generated KerML:\n$generated" }

        assertContains(generated, "function Sum")
    }

    @Test
    fun `generate invariant`() {
        val kerml = """
            package Test {
                datatype Boolean;
                class Constrained {
                    feature valid : Boolean;
                }
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        logger.debug { "Generated KerML:\n$generated" }

        assertContains(generated, "class Constrained")
    }

    @Test
    fun `round-trip function with parameters`() {
        val kerml = """
            package Functions {
                datatype Integer;
                function Add {
                    in feature a : Integer;
                    in feature b : Integer;
                    return feature sum : Integer;
                }
                predicate IsPositive {
                    in feature value : Integer;
                }
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        logger.debug { "Generated KerML:\n$generated" }

        val model2 = KerMLModel().parseString(generated)
        assertNotNull(model2, "Generated KerML should be parseable")
    }
}
