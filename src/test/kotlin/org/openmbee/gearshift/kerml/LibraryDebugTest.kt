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
package org.openmbee.gearshift.kerml

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class LibraryDebugTest : DescribeSpec({
    describe("Debug library parsing") {
        it("should parse Base.kerml and show results") {
            val factory = KerMLModelFactory()
            val result = KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

            println("=== Library Load Result ===")
            println("Success: ${result.success}")
            println("Error: ${result.error}")
            result.parseResult?.let { pr ->
                println("Parse success: ${pr.success}")
                println("Errors: ${pr.errors}")
                println("Unresolved refs: ${pr.unresolvedReferences.size}")
                pr.unresolvedReferences.take(5).forEach { ref ->
                    println("  - ${ref.targetQualifiedName} from ${ref.sourceInstanceId}")
                }
            }

            val engine = factory.engine
            println("\n=== Statistics ===")
            val stats = engine.getStatistics()
            println("Total instances: ${stats.objects.totalObjects}")
            println("By type:")
            stats.objects.typeDistribution.entries.sortedByDescending { it.value }.forEach { (type, count) ->
                println("  $type: $count")
            }

            println("\n=== Parsed Elements ===")
            val parsed = factory.getCoordinator().getParsedElements()
            println("Total registered: ${parsed.size}")
            parsed.entries.take(20).forEach { (name, id) ->
                println("  $name -> $id")
            }

            // Print result BEFORE assertion
            println("\n=== Assertion Info ===")
            println("Result success: ${result.success}")
            println("Result error: ${result.error}")

            // Parsing should have no syntax errors (only cross-library refs are allowed unresolved)
            result.parseResult?.errors shouldBe emptyList()
            result.error shouldBe null

            // Verify key elements were parsed
            (stats.objects.totalObjects > 0) shouldBe true
            (stats.objects.typeDistribution["LibraryPackage"] ?: 0) shouldBe 1
            (stats.objects.typeDistribution["Classifier"] ?: 0) shouldBe 1  // Anything
            (stats.objects.typeDistribution["DataType"] ?: 0) shouldBe 1    // DataValue
            (stats.objects.typeDistribution["Feature"] ?: 0) shouldBe 5      // self, things, dataValues, naturals, that
            (stats.objects.typeDistribution["MultiplicityRange"] ?: 0) shouldBe 4  // exactlyOne, zeroOrOne, etc.

            // Verify qualified names were registered
            parsed.keys.any { it.contains("Base::Anything") } shouldBe true
            parsed.keys.any { it.contains("Base::DataValue") } shouldBe true
        }
    }
})
