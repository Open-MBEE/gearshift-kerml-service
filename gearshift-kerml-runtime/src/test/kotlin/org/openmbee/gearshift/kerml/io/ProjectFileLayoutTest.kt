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
package org.openmbee.gearshift.kerml.io

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.openmbee.gearshift.kerml.KerMLTestSpec
import java.nio.file.Files
import org.openmbee.gearshift.generated.interfaces.Class as KerMLClass

class ProjectFileLayoutTest : KerMLTestSpec({

    val layout = ProjectFileLayout()

    describe("writeToFiles") {

        it("should split single-package model into one file") {
            val model = freshModel()
            model.parseString("""
                package Vehicles {
                    class Vehicle;
                }
            """.trimIndent())

            val files = layout.writeToFiles(model)

            files shouldHaveSize 1
            files shouldContainKey "Vehicles.kerml"
            files["Vehicles.kerml"]!! shouldContain "package Vehicles"
            files["Vehicles.kerml"]!! shouldContain "class Vehicle"
        }

        it("should split multi-package model into one file per package") {
            val model = freshModel()
            model.parseString("""
                package Vehicles {
                    class Vehicle;
                    class Car :> Vehicle;
                }
                package Requirements {
                    class Requirement;
                }
            """.trimIndent())

            val files = layout.writeToFiles(model)

            files shouldHaveSize 2
            files shouldContainKey "Vehicles.kerml"
            files shouldContainKey "Requirements.kerml"

            files["Vehicles.kerml"]!! shouldContain "class Vehicle"
            files["Vehicles.kerml"]!! shouldContain "class Car"
            files["Requirements.kerml"]!! shouldContain "class Requirement"
        }
    }

    describe("readFromDirectory") {

        it("should read directory of kerml files and produce model") {
            // Write files to temp dir
            val tmpDir = Files.createTempDirectory("kerml-layout-test")
            try {
                Files.writeString(tmpDir.resolve("Vehicles.kerml"), """
                    package Vehicles {
                        class Vehicle;
                    }
                """.trimIndent())
                Files.writeString(tmpDir.resolve("Requirements.kerml"), """
                    package Requirements {
                        class Requirement;
                    }
                """.trimIndent())

                val model = freshModel()
                val result = layout.readFromDirectory(tmpDir, model)

                result.shouldNotBeNull()
                result.success shouldBe true

                // Both packages should be parsed into the model
                val classes = model.allOfType<KerMLClass>()
                val classNames = classes.map { it.declaredName }
                classNames shouldContain "Vehicle"
                classNames shouldContain "Requirement"
            } finally {
                // Clean up temp dir
                Files.walk(tmpDir)
                    .sorted(Comparator.reverseOrder())
                    .forEach { Files.deleteIfExists(it) }
            }
        }
    }

    describe("round-trip") {

        it("should produce identical files on write-read-write") {
            val model1 = freshModel()
            model1.parseString("""
                package Vehicles {
                    class Vehicle;
                    class Car :> Vehicle;
                }
                package Requirements {
                    class Requirement;
                }
            """.trimIndent())

            // Write to files
            val files1 = layout.writeToFiles(model1)

            // Write to temp dir, read back
            val tmpDir = Files.createTempDirectory("kerml-roundtrip-test")
            try {
                for ((name, content) in files1) {
                    Files.writeString(tmpDir.resolve(name), content)
                }

                val model2 = freshModel()
                layout.readFromDirectory(tmpDir, model2)

                // Write again
                val files2 = layout.writeToFiles(model2)

                // Files should be identical
                files2.keys shouldBe files1.keys
                for (name in files1.keys) {
                    files2[name] shouldBe files1[name]
                }
            } finally {
                Files.walk(tmpDir)
                    .sorted(Comparator.reverseOrder())
                    .forEach { Files.deleteIfExists(it) }
            }
        }
    }
})
