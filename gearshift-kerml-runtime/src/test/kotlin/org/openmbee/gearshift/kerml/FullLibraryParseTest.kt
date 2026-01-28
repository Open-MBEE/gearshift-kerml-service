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
import java.io.File

class FullLibraryParseTest : DescribeSpec({
    describe("Full KerML Library Parsing") {
        it("should parse all standard library files") {
            val factory = KerMLModelFactory()
            val results = KerMLSemanticLibraryLoader.loadLibrary(factory)

            val sb = StringBuilder()
            sb.appendLine("=== Library Parsing Results ===")
            var successCount = 0
            var failCount = 0

            for (result in results) {
                val status = if (result.success) "PASS" else "FAIL"
                sb.appendLine("$status ${result.fileName}")
                if (!result.success) {
                    sb.appendLine("   Error: ${result.error}")
                    result.parseResult?.errors?.take(3)?.forEach { err ->
                        sb.appendLine("   Parse error: $err")
                    }
                    failCount++
                } else {
                    successCount++
                }
            }

            sb.appendLine("\n=== Summary ===")
            sb.appendLine("Success: $successCount / ${results.size}")

            val stats = factory.engine.getStatistics()
            sb.appendLine("\n=== Total Objects Created ===")
            sb.appendLine("Total instances: ${stats.objects.totalObjects}")
            sb.appendLine("\nBy type (top 25):")
            stats.objects.typeDistribution.entries
                .sortedByDescending { it.value }
                .take(25)
                .forEach { (type, count) ->
                    sb.appendLine("  $type: $count")
                }

            // Write to file for easy reading
            val outputFile = File("/tmp/kerml_library_parse_results.txt")
            outputFile.writeText(sb.toString())

            // All files should parse successfully
            results.all { it.success } shouldBe true
        }
    }
})
