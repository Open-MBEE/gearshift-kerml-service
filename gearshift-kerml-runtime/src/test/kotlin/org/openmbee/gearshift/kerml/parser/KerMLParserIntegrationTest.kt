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

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
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

        context("association end setters") {

            it("should set non-derived single-valued association end") {
                val factory = KerMLModelFactory()

                // Create a namespace and a membership
                val ns = factory.create<Namespace>()
                ns.declaredName = "TestNamespace"

                val membership = factory.create<OwningMembership>()

                // Set the association end using the typed wrapper
                membership.membershipOwningNamespace = ns

                // Verify bidirectional navigation - namespace should see the membership
                ns.ownedMembership shouldContain membership
            }

            it("should set non-derived multi-valued association end") {
                val factory = KerMLModelFactory()

                // Create a namespace and memberships
                val ns = factory.create<Namespace>()
                ns.declaredName = "TestNamespace"

                val membership1 = factory.create<OwningMembership>()
                val membership2 = factory.create<OwningMembership>()

                // Set the multi-valued association end
                ns.ownedMembership = listOf(membership1, membership2)

                // Verify the association was set
                ns.ownedMembership shouldHaveSize 2
                ns.ownedMembership shouldContain membership1
                ns.ownedMembership shouldContain membership2

                // Verify bidirectional navigation
                membership1.membershipOwningNamespace shouldBe ns
                membership2.membershipOwningNamespace shouldBe ns
            }

            it("should throw when setting derived association end") {
                val factory = KerMLModelFactory()

                val ns = factory.create<Namespace>()
                val element = factory.create<KerMLClass>()

                // member is a derived association end - should throw
                // Note: The interface defines 'member' as val (no setter),
                // but if we try via engine directly it should throw
                val exception = shouldThrow<IllegalStateException> {
                    factory.engine.setProperty(ns.id!!, "member", listOf(element))
                }

                exception.message shouldContain "derived"
            }

            it("should replace existing links when setting association end") {
                val factory = KerMLModelFactory()

                val ns = factory.create<Namespace>()
                val membership1 = factory.create<OwningMembership>()
                val membership2 = factory.create<OwningMembership>()
                val membership3 = factory.create<OwningMembership>()

                // Set initial memberships
                ns.ownedMembership = listOf(membership1, membership2)
                ns.ownedMembership shouldHaveSize 2

                // Replace with different memberships
                ns.ownedMembership = listOf(membership3)

                // Verify old links are removed and new one is added
                ns.ownedMembership shouldHaveSize 1
                ns.ownedMembership shouldContain membership3
            }

            it("should clear association when setting to empty list") {
                val factory = KerMLModelFactory()

                val ns = factory.create<Namespace>()
                val membership = factory.create<OwningMembership>()

                // Set membership
                ns.ownedMembership = listOf(membership)
                ns.ownedMembership shouldHaveSize 1

                // Clear by setting empty list
                ns.ownedMembership = emptyList()
                ns.ownedMembership shouldHaveSize 0
            }

            it("should update opposite end when reassigning single-valued association") {
                val factory = KerMLModelFactory()

                // Create two namespaces and a membership
                val ns1 = factory.create<Namespace>()
                ns1.declaredName = "Namespace1"

                val ns2 = factory.create<Namespace>()
                ns2.declaredName = "Namespace2"

                val membership = factory.create<OwningMembership>()

                // Initially assign membership to ns1
                membership.membershipOwningNamespace = ns1

                // Verify ns1 sees the membership
                ns1.ownedMembership shouldContain membership
                ns2.ownedMembership shouldHaveSize 0

                // Reassign membership to ns2
                membership.membershipOwningNamespace = ns2

                // ns1 should no longer see the membership
                ns1.ownedMembership shouldHaveSize 0

                // ns2 should now see the membership
                ns2.ownedMembership shouldContain membership
            }

            it("should update opposite end when modifying multi-valued association") {
                val factory = KerMLModelFactory()

                val ns = factory.create<Namespace>()
                val membership1 = factory.create<OwningMembership>()
                val membership2 = factory.create<OwningMembership>()
                val membership3 = factory.create<OwningMembership>()

                // Set initial memberships on namespace
                ns.ownedMembership = listOf(membership1, membership2)

                // Verify bidirectional from membership's perspective
                membership1.membershipOwningNamespace shouldBe ns
                membership2.membershipOwningNamespace shouldBe ns

                // Modify the namespace's memberships (remove membership1, add membership3)
                ns.ownedMembership = listOf(membership2, membership3)

                // membership1 should no longer point to ns (link removed)
                // Note: Accessing membershipOwningNamespace when no link exists will throw
                val links1 = factory.engine.getLinkedTargets(
                    "membershipOwningNamespaceOwnedMembershipAssociation",
                    membership1.id!!
                )
                links1 shouldHaveSize 0

                // membership2 and membership3 should still point to ns
                membership2.membershipOwningNamespace shouldBe ns
                membership3.membershipOwningNamespace shouldBe ns
            }
        }
    }
})
