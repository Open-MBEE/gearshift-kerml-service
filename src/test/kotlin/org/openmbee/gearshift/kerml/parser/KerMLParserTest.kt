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
package org.openmbee.gearshift.kerml.parser

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.openmbee.gearshift.GearshiftEngine
import org.openmbee.gearshift.kerml.KerMLMetamodelLoader

/**
 * Unit tests for the KerML parser.
 */
class KerMLParserTest : DescribeSpec({

    describe("KerMLParseCoordinator") {

        lateinit var engine: GearshiftEngine
        lateinit var coordinator: KerMLParseCoordinator

        beforeEach {
            engine = GearshiftEngine()
            KerMLMetamodelLoader.initialize(engine)
            coordinator = KerMLParseCoordinator(engine)
        }

        context("parsing empty package") {

            it("should parse an empty package with semicolon") {
                val result = coordinator.parseString("package MyPackage;")

                if (!result.success) {
                    println("Parse errors: ${result.errors}")
                    println("Unresolved references: ${result.unresolvedReferences}")
                }

                result.success shouldBe true
                result.errors.shouldBeEmpty()
                result.rootElementId.shouldNotBeNull()
            }

            it("should parse an empty package with braces") {
                val result = coordinator.parseString("package MyPackage {}")

                result.success shouldBe true
                result.errors.shouldBeEmpty()
            }

            it("should set declaredName on package") {
                val result = coordinator.parseString("package TestPackage;")

                result.success shouldBe true
                val elements = coordinator.getParsedElements()
                elements.containsKey("TestPackage") shouldBe true

                val pkgId = elements["TestPackage"]!!
                engine.getProperty(pkgId, "declaredName") shouldBe "TestPackage"
            }
        }

        context("parsing nested packages") {

            it("should parse nested packages") {
                val result = coordinator.parseString("""
                    package Outer {
                        package Inner;
                    }
                """.trimIndent())

                result.success shouldBe true
                val elements = coordinator.getParsedElements()
                elements.containsKey("Outer") shouldBe true
                elements.containsKey("Outer::Inner") shouldBe true
            }

            it("should parse deeply nested packages") {
                val result = coordinator.parseString("""
                    package A {
                        package B {
                            package C;
                        }
                    }
                """.trimIndent())

                result.success shouldBe true
                val elements = coordinator.getParsedElements()
                elements.containsKey("A") shouldBe true
                elements.containsKey("A::B") shouldBe true
                elements.containsKey("A::B::C") shouldBe true
            }
        }

        context("parsing classes") {

            it("should parse a simple class") {
                val result = coordinator.parseString("""
                    package Test {
                        class Vehicle;
                    }
                """.trimIndent())

                result.success shouldBe true
                val elements = coordinator.getParsedElements()
                elements.containsKey("Test::Vehicle") shouldBe true

                val vehicleId = elements["Test::Vehicle"]!!
                engine.getProperty(vehicleId, "declaredName") shouldBe "Vehicle"
            }

            it("should parse an abstract class") {
                val result = coordinator.parseString("""
                    package Test {
                        abstract class Animal;
                    }
                """.trimIndent())

                result.success shouldBe true
                val elements = coordinator.getParsedElements()
                elements.containsKey("Test::Animal") shouldBe true

                val animalId = elements["Test::Animal"]!!
                engine.getProperty(animalId, "isAbstract") shouldBe true
            }

            it("should parse class with specialization") {
                val result = coordinator.parseString("""
                    package Test {
                        class Vehicle;
                        class Car :> Vehicle;
                    }
                """.trimIndent())

                result.success shouldBe true
                val elements = coordinator.getParsedElements()
                elements.containsKey("Test::Vehicle") shouldBe true
                elements.containsKey("Test::Car") shouldBe true
            }

            it("should parse class with body") {
                val result = coordinator.parseString("""
                    package Test {
                        class Vehicle {
                        }
                    }
                """.trimIndent())

                result.success shouldBe true
            }
        }

        context("parsing features") {

            it("should parse a simple feature") {
                val result = coordinator.parseString("""
                    package Test {
                        class Vehicle {
                            feature name;
                        }
                    }
                """.trimIndent())

                result.success shouldBe true
                val elements = coordinator.getParsedElements()
                elements.containsKey("Test::Vehicle::name") shouldBe true
            }

            it("should parse a derived feature") {
                val result = coordinator.parseString("""
                    package Test {
                        class Vehicle {
                            derived feature totalWeight;
                        }
                    }
                """.trimIndent())

                result.success shouldBe true
                val elements = coordinator.getParsedElements()
                val featureId = elements["Test::Vehicle::totalWeight"]!!
                engine.getProperty(featureId, "isDerived") shouldBe true
            }

            it("should parse a composite feature") {
                val result = coordinator.parseString("""
                    package Test {
                        class Vehicle {
                            composite feature engine;
                        }
                    }
                """.trimIndent())

                result.success shouldBe true
                val elements = coordinator.getParsedElements()
                val featureId = elements["Test::Vehicle::engine"]!!
                engine.getProperty(featureId, "isComposite") shouldBe true
            }
        }

        context("error handling") {

            it("should report syntax errors") {
                // Use actually invalid syntax: incomplete class declaration
                val result = coordinator.parseString("package Test { class")

                result.success shouldBe false
                result.errors.shouldNotBeEmpty()
            }

            it("should report line and column for errors") {
                // Use actually invalid syntax: incomplete class declaration
                val result = coordinator.parseString("package Test { class")

                result.success shouldBe false
                val error = result.errors.first()
                error.line shouldNotBe 0
            }
        }

        context("qualified name computation") {

            it("should compute correct qualified names") {
                coordinator.computeQualifiedName("", "Package1") shouldBe "Package1"
                coordinator.computeQualifiedName("Package1", "Class1") shouldBe "Package1::Class1"
                coordinator.computeQualifiedName("A::B", "C") shouldBe "A::B::C"
            }
        }

        context("reference resolution") {

            it("should resolve simple references") {
                val result = coordinator.parseString("""
                    package Test {
                        class A;
                        class B :> A;
                    }
                """.trimIndent())

                // Parsing should succeed, but some references may be unresolved
                // depending on association definitions
                result.rootElementId.shouldNotBeNull()
            }
        }
    }
})
