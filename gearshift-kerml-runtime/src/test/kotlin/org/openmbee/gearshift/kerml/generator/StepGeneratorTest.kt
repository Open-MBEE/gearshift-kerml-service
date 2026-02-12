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
class StepGeneratorTest {

    @Test
    fun `generate simple step`() {
        val kerml = """
            package Test {
                behavior MyBehavior {
                    step doWork;
                }
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        logger.debug { "Generated KerML:\n$generated" }

        assertContains(generated, "behavior MyBehavior")
        assertContains(generated, "step doWork")
    }

    @Test
    fun `generate step with typing`() {
        val kerml = """
            package Test {
                behavior Action;
                behavior Process {
                    step init : Action;
                }
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        logger.debug { "Generated KerML:\n$generated" }

        assertContains(generated, "step init")
        assertContains(generated, "Action")
    }

    @Test
    fun `round-trip step in behavior`() {
        val kerml = """
            package Workflow {
                behavior Process {
                    step prepare;
                    step execute;
                    step cleanup;
                }
            }
        """.trimIndent()

        val model = KerMLModel().parseString(kerml)
        assertNotNull(model, "Model should parse successfully")

        val writer = KerMLWriter()
        val generated = writer.write(model)

        logger.debug { "Generated KerML:\n$generated" }

        // Verify round-trip parsability
        val model2 = KerMLModel().parseString(generated)
        assertNotNull(model2, "Generated KerML should be parseable")
    }
}
