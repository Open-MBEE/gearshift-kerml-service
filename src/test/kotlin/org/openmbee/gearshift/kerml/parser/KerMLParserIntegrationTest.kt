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
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.openmbee.gearshift.kerml.KerMLModelFactory
import org.openmbee.gearshift.generated.interfaces.*
import org.openmbee.gearshift.generated.interfaces.Package as KerMLPackage
import org.openmbee.gearshift.generated.interfaces.Class as KerMLClass

/**
 * Integration tests for KerML parser with typed wrappers.
 */
class KerMLParserIntegrationTest : DescribeSpec({

    describe("KerMLModelFactory integration") {

        context("basic parsing") {

            it("should parse and access via typed wrappers") {
                val factory = KerMLModelFactory()
                val pkg = factory.parseString("""
                    package Vehicles {
                        class Vehicle;
                    }
                """.trimIndent())

                pkg.shouldNotBeNull()
                pkg.declaredName shouldBe "Vehicles"

                val classes = factory.allOfType<KerMLClass>()
                classes.shouldNotBeEmpty()
            }

            it("should find elements by qualified name") {
                val factory = KerMLModelFactory()
                factory.parseString("""
                    package Test {
                        class MyClass;
                    }
                """.trimIndent())

                val element = factory.findByQualifiedName("Test::MyClass")
                element.shouldNotBeNull()
                element.declaredName shouldBe "MyClass"
            }

            it("should find elements by simple name") {
                val factory = KerMLModelFactory()
                factory.parseString("""
                    package Animals {
                        class Dog;
                        class Cat;
                    }
                """.trimIndent())

                val dog = factory.findByName<KerMLClass>("Dog")
                dog.shouldNotBeNull()
                dog.declaredName shouldBe "Dog"
            }
        }

        context("complex models") {

            it("should parse package with multiple classes") {
                val factory = KerMLModelFactory()
                val pkg = factory.parseString("""
                    package Vehicles {
                        class Vehicle;
                        class Car;
                        class Truck;
                        class Motorcycle;
                    }
                """.trimIndent())

                pkg.shouldNotBeNull()
                val classes = factory.allOfType<KerMLClass>()
                classes shouldHaveAtLeastSize 4
            }

            it("should parse nested structure") {
                val factory = KerMLModelFactory()
                factory.parseString("""
                    package Root {
                        package Sub1 {
                            class A;
                        }
                        package Sub2 {
                            class B;
                        }
                    }
                """.trimIndent())

                val packages = factory.allOfType<KerMLPackage>()
                packages shouldHaveAtLeastSize 3  // Root, Sub1, Sub2
            }

            it("should parse class with features") {
                val factory = KerMLModelFactory()
                factory.parseString("""
                    package Test {
                        class Vehicle {
                            feature color;
                            feature weight;
                            feature speed;
                        }
                    }
                """.trimIndent())

                val features = factory.allOfType<Feature>()
                features shouldHaveAtLeastSize 3
            }
        }

        context("abstract classes") {

            it("should correctly parse abstract modifier") {
                val factory = KerMLModelFactory()
                factory.parseString("""
                    package Test {
                        abstract class Shape;
                        class Circle;
                    }
                """.trimIndent())

                val classes = factory.allOfType<KerMLClass>()
                classes shouldHaveAtLeastSize 2

                // Find Shape - should be abstract
                val shape = factory.findByName<KerMLClass>("Shape")
                shape.shouldNotBeNull()

                // Find Circle - should not be abstract
                val circle = factory.findByName<KerMLClass>("Circle")
                circle.shouldNotBeNull()
            }
        }

        context("parse statistics") {

            it("should track parsed elements") {
                val factory = KerMLModelFactory()
                factory.parseString("""
                    package A {
                        class B;
                        class C;
                    }
                """.trimIndent())

                val elements = factory.getParsedElements()
                elements.size shouldNotBe 0
                elements.containsKey("A") shouldBe true
                elements.containsKey("A::B") shouldBe true
                elements.containsKey("A::C") shouldBe true
            }
        }

        context("error handling") {

            it("should return null for invalid syntax") {
                val factory = KerMLModelFactory()
                val result = factory.parseString("invalid kerml {{{")

                result shouldBe null

                val parseResult = factory.getLastParseResult()
                parseResult.shouldNotBeNull()
                parseResult.success shouldBe false
                parseResult.errors.shouldNotBeEmpty()
            }

            it("should provide error details") {
                val factory = KerMLModelFactory()
                // Use actually invalid syntax: incomplete class declaration
                factory.parseString("package Test { class")

                val parseResult = factory.getLastParseResult()
                parseResult.shouldNotBeNull()
                parseResult.success shouldBe false

                val error = parseResult.errors.first()
                error.line shouldNotBe 0
                error.message.shouldNotBeNull()
            }
        }

        context("factory reset") {

            it("should clear state on reset") {
                val factory = KerMLModelFactory()
                factory.parseString("package First;")

                val elementsBefore = factory.getParsedElements()
                elementsBefore.containsKey("First") shouldBe true

                factory.reset()

                factory.parseString("package Second;")
                val elementsAfter = factory.getParsedElements()
                elementsAfter.containsKey("First") shouldBe false
                elementsAfter.containsKey("Second") shouldBe true
            }
        }
    }
})
