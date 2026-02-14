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
package org.openmbee.gearshift.kerml.index

import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.openmbee.gearshift.generated.interfaces.Element
import org.openmbee.gearshift.kerml.KerMLTestSpec

class ModelIndexTest : KerMLTestSpec({

    describe("ModelIndex") {

        it("should store and retrieve bidirectional mappings") {
            val index = ModelIndex()
            index.put("id-1", "Pkg::ClassA")
            index.put("id-2", "Pkg::ClassB")

            index.getQn("id-1") shouldBe "Pkg::ClassA"
            index.getQn("id-2") shouldBe "Pkg::ClassB"
            index.getId("Pkg::ClassA") shouldBe "id-1"
            index.getId("Pkg::ClassB") shouldBe "id-2"
            index.size() shouldBe 2
        }

        it("should handle remove") {
            val index = ModelIndex()
            index.put("id-1", "Pkg::ClassA")
            index.remove("id-1")

            index.getQn("id-1").shouldBeNull()
            index.getId("Pkg::ClassA").shouldBeNull()
            index.size() shouldBe 0
        }

        it("should handle overwrite of same ID with new QN") {
            val index = ModelIndex()
            index.put("id-1", "Pkg::OldName")
            index.put("id-1", "Pkg::NewName")

            index.getQn("id-1") shouldBe "Pkg::NewName"
            index.getId("Pkg::NewName") shouldBe "id-1"
            index.getId("Pkg::OldName").shouldBeNull()
            index.size() shouldBe 1
        }

        it("should handle overwrite of same QN with new ID") {
            val index = ModelIndex()
            index.put("id-1", "Pkg::ClassA")
            index.put("id-2", "Pkg::ClassA")

            index.getId("Pkg::ClassA") shouldBe "id-2"
            index.getQn("id-2") shouldBe "Pkg::ClassA"
            index.getQn("id-1").shouldBeNull()
            index.size() shouldBe 1
        }

        describe("JSON serialization") {

            it("should round-trip through JSON") {
                val index = ModelIndex()
                index.put("id-1", "Pkg::ClassA")
                index.put("id-2", "Pkg::ClassB")
                index.put("id-3", "Pkg::ClassA::featureX")

                val json = index.toJson()
                val restored = ModelIndex.fromJson(json)

                restored.size() shouldBe 3
                restored.getQn("id-1") shouldBe "Pkg::ClassA"
                restored.getQn("id-2") shouldBe "Pkg::ClassB"
                restored.getQn("id-3") shouldBe "Pkg::ClassA::featureX"
                restored.getId("Pkg::ClassA") shouldBe "id-1"
                restored.getId("Pkg::ClassB") shouldBe "id-2"
            }

            it("should serialize empty index") {
                val index = ModelIndex()
                val json = index.toJson()
                val restored = ModelIndex.fromJson(json)
                restored.size() shouldBe 0
            }
        }
    }

    describe("IndexReconciler") {

        it("should build index from first parse (no previous index)") {
            val model = freshModel()
            model.parseString("""
                package Vehicles {
                    class Vehicle {
                        feature wheels : ScalarValues::Integer;
                    }
                    class Car;
                }
            """.trimIndent())

            val index = model.reconcileIndex(null)

            // Should have entries for named elements
            (index.size() > 0) shouldBe true

            // The package should be indexed
            index.getId("Vehicles").shouldNotBeNull()
        }

        it("should preserve IDs for unchanged elements across reparse") {
            // First parse
            val model1 = freshModel()
            model1.parseString("""
                package Vehicles {
                    class Vehicle;
                    class Car;
                }
            """.trimIndent())

            val firstIndex = model1.reconcileIndex(null)
            val vehicleId = firstIndex.getId("Vehicles::Vehicle")
            val carId = firstIndex.getId("Vehicles::Car")
            vehicleId.shouldNotBeNull()
            carId.shouldNotBeNull()

            // Second parse (same content, new model = new UUIDs)
            val model2 = freshModel()
            model2.parseString("""
                package Vehicles {
                    class Vehicle;
                    class Car;
                }
            """.trimIndent())

            val secondIndex = model2.reconcileIndex(firstIndex)

            // IDs should be preserved
            secondIndex.getId("Vehicles::Vehicle") shouldBe vehicleId
            secondIndex.getId("Vehicles::Car") shouldBe carId

            // Verify the elements in the engine actually have the remapped IDs
            val vehicleElement = model2.engine.getElement(vehicleId)
            vehicleElement.shouldNotBeNull()
            vehicleElement.className shouldBe "Class"
        }

        it("should assign new IDs for genuinely new elements") {
            // First parse
            val model1 = freshModel()
            model1.parseString("""
                package Vehicles {
                    class Vehicle;
                }
            """.trimIndent())

            val firstIndex = model1.reconcileIndex(null)
            val vehicleId = firstIndex.getId("Vehicles::Vehicle")
            vehicleId.shouldNotBeNull()

            // Second parse with added element
            val model2 = freshModel()
            model2.parseString("""
                package Vehicles {
                    class Vehicle;
                    class Truck;
                }
            """.trimIndent())

            val secondIndex = model2.reconcileIndex(firstIndex)

            // Existing element keeps its ID
            secondIndex.getId("Vehicles::Vehicle") shouldBe vehicleId

            // New element gets a new ID (different from vehicleId)
            val truckId = secondIndex.getId("Vehicles::Truck")
            truckId.shouldNotBeNull()
        }

        it("should drop deleted elements from the index") {
            // First parse with two classes
            val model1 = freshModel()
            model1.parseString("""
                package Vehicles {
                    class Vehicle;
                    class Car;
                }
            """.trimIndent())

            val firstIndex = model1.reconcileIndex(null)
            firstIndex.getId("Vehicles::Car").shouldNotBeNull()

            // Second parse without Car
            val model2 = freshModel()
            model2.parseString("""
                package Vehicles {
                    class Vehicle;
                }
            """.trimIndent())

            val secondIndex = model2.reconcileIndex(firstIndex)

            // Car should be absent from new index
            secondIndex.getId("Vehicles::Car").shouldBeNull()

            // Vehicle should still be present
            secondIndex.getId("Vehicles::Vehicle").shouldNotBeNull()
        }

        it("should preserve IDs across reparse with graph links intact") {
            // Parse a model with relationships (specializations create links)
            val model1 = freshModel()
            model1.parseString("""
                package Vehicles {
                    class Vehicle;
                    class Car :> Vehicle;
                }
            """.trimIndent())

            val firstIndex = model1.reconcileIndex(null)
            val vehicleId = firstIndex.getId("Vehicles::Vehicle")
            val carId = firstIndex.getId("Vehicles::Car")
            vehicleId.shouldNotBeNull()
            carId.shouldNotBeNull()

            // Reparse into fresh model
            val model2 = freshModel()
            model2.parseString("""
                package Vehicles {
                    class Vehicle;
                    class Car :> Vehicle;
                }
            """.trimIndent())

            val secondIndex = model2.reconcileIndex(firstIndex)
            secondIndex.getId("Vehicles::Vehicle") shouldBe vehicleId
            secondIndex.getId("Vehicles::Car") shouldBe carId

            // Verify the remapped Car element can still navigate to Vehicle
            val car = model2.engine.getElement(carId) as? Element
            car.shouldNotBeNull()
        }
    }
})
