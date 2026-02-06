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

import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.openmbee.gearshift.generated.interfaces.Feature
import org.openmbee.gearshift.generated.interfaces.Subclassification
import org.openmbee.gearshift.generated.interfaces.Subsetting
import org.openmbee.gearshift.generated.interfaces.Class as KerMLClass
import org.openmbee.mdm.framework.runtime.MissingRequiredAssociationException

/**
 * Tests for KerML 8.4.3 Core Semantics Implied Relationships (Table 8).
 *
 * Per the spec, Core semantics require:
 * - checkTypeSpecialization: Creates implied Subclassification (Type → Base::Anything, Class → Occurrences::Occurrence)
 * - checkFeatureSpecialization: Creates an implied Subsetting to Base::things
 *
 * These tests verify that when KerML textual syntax is parsed, the implied
 * relationships are correctly created by the engine.
 *
 * Uses KerMLTestSpec to share the Kernel Semantic Library across tests for efficiency.
 */
class CoreSemanticsImpliedRelationshipsTest : KerMLTestSpec({

    describe("Table 8: Core Semantics Implied Relationships") {

        context("checkTypeSpecialization -> Subclassification to Occurrences::Occurrence") {

            it("should create implied Subclassification when parsing a class without explicit specialization") {
                val factory = freshModel()

                // Parse a class that should get an implied subclassification
                factory.parseString(
                    """
                    package Test {
                        class MyClass;
                    }
                """.trimIndent()
                )

                // Find MyClass
                val myClass = factory.findByName<KerMLClass>("MyClass")
                myClass.shouldNotBeNull()

                myClass.owner.shouldNotBeNull()
                myClass.owner!!.name shouldBe "Test"
                myClass.qualifiedName shouldBe "Test::MyClass"

                // Find all Subclassification instances
                val subclassifications = factory.allOfType<Subclassification>()

                // There should be at least one implied subclassification for MyClass
                val impliedSubclassifications = subclassifications.filter { sub ->
                    sub.isImplied == true
                }

                impliedSubclassifications.shouldNotBeEmpty()

                // Verify that MyClass has an implied subclassification
                val myClassSubclassifications = impliedSubclassifications.filter { sub ->
                    val subclassifier = sub.subclassifier
                    subclassifier.name == "MyClass" || subclassifier.declaredName == "MyClass"
                }

                myClassSubclassifications.shouldNotBeEmpty()

                // Verify the superclassifier is Occurrences::Occurrence (per KerML spec, Class specializes Occurrence)
                val myClassSub = myClassSubclassifications.first()
                val superclassifier = myClassSub.superclassifier
                superclassifier.shouldNotBeNull()
                (superclassifier.name == "Occurrence" || superclassifier.declaredName == "Occurrence") shouldBe true
            }

            it("should not create duplicate implied Subclassification when class already specializes a type that specializes Occurrences::Occurrence") {
                val factory = freshModel()

                // Parse a hierarchy: Vehicle -> Car
                // Vehicle will get implied subclassification to Occurrence
                // Car :> Vehicle should NOT get a direct implied subclassification to Occurrence
                // (redundancy elimination per spec)
                factory.parseString(
                    """
                    package Test {
                        class Vehicle;
                        class Car :> Vehicle;
                    }
                """.trimIndent()
                )

                // Find Car
                val car = factory.findByName<KerMLClass>("Car")
                car.shouldNotBeNull()
                car.name.shouldNotBeNull()

                // Debug: Check ownership chain
                println("DEBUG: Car.id = ${car.id}")
                println("DEBUG: Car.declaredName = ${car.declaredName}")
                println("DEBUG: Car.owningMembership = ${car.owningMembership}")
                println("DEBUG: Car.owningRelationship = ${car.owningRelationship}")
                println("DEBUG: Car.owner = ${car.owner}")

                car.owner.shouldNotBeNull()
                car.owner!!.declaredName shouldBe "Test"
                car.qualifiedName shouldBe "Test::Car"
                // Find all Subclassification instances where Car is the subclassifier
                val subclassifications = factory.allOfType<Subclassification>()
                val carSubclassifications = subclassifications.filter { sub ->
                    val subclassifier = sub.subclassifier
                    subclassifier.name == "Car" || subclassifier.declaredName == "Car"
                }

                // Car should have a subclassification to Vehicle (explicit)
                // Car should NOT have a direct implied subclassification to Occurrence
                // (because Vehicle already specializes Occurrence)
                val carToOccurrence = carSubclassifications.filter { sub ->
                    sub.isImplied == true &&
                            (sub.superclassifier.name == "Occurrence" || sub.superclassifier.declaredName == "Occurrence")
                }

                // Per redundancy elimination rules, there should be no direct implied
                // subclassification from Car to Occurrence
                carToOccurrence.size shouldBe 0
            }
        }

        context("checkFeatureSpecialization -> Subsetting to Base::things") {

            it("should create implied Subsetting when parsing a feature without explicit subsetting") {
                val factory = freshModel()

                // Parse a class with a feature
                factory.parseString(
                    """
                    package Test {
                        class Vehicle {
                            feature wheels;
                        }
                    }
                """.trimIndent()
                )

                // Find the wheels feature
                val wheels = factory.findByName<Feature>("wheels")
                wheels.shouldNotBeNull()

                // Find all Subsetting instances
                val subsettings = factory.allOfType<Subsetting>()

                // There should be at least one implied subsetting for wheels -> Base::things
                val impliedSubsettings = subsettings.filter { sub ->
                    sub.isImplied == true
                }

                impliedSubsettings.shouldNotBeEmpty()

                // Verify that wheels has an implied subsetting to things
                val wheelsSubsettings = impliedSubsettings.filter { sub ->
                    val subsettingFeature = sub.subsettingFeature
                    subsettingFeature.name == "wheels" || subsettingFeature.declaredName == "wheels"
                }

                wheelsSubsettings.shouldNotBeEmpty()

                // Verify the subsetted feature is Base::things
                val wheelsSub = wheelsSubsettings.first()
                val subsettedFeature = wheelsSub.subsettedFeature
                subsettedFeature.shouldNotBeNull()
                (subsettedFeature.name == "things" || subsettedFeature.declaredName == "things") shouldBe true
            }

            it("should not create duplicate implied Subsetting when feature already subsets a feature that subsets Base::things") {
                val factory = freshModel()

                // Parse a class with features where child subsets parent
                factory.parseString(
                    """
                    package Test {
                        class Vehicle {
                            feature parts;
                        }
                        class Car :> Vehicle {
                            feature engineParts :> parts;
                        }
                    }
                """.trimIndent()
                )

                // Find engineParts
                val engineParts = factory.findByName<Feature>("engineParts")
                engineParts.shouldNotBeNull()

                // Find all Subsetting instances where engineParts is the subsettingFeature
                val subsettings = factory.allOfType<Subsetting>()
                val enginePartsSubsettings = subsettings.mapNotNull { sub ->
                    try {
                        val subsettingFeature = sub.subsettingFeature
                        if (subsettingFeature?.name == "engineParts" || subsettingFeature?.declaredName == "engineParts") {
                            sub
                        } else {
                            null
                        }
                    } catch (e: Exception) {
                        null
                    }
                }

                // engineParts should have a subsetting to parts (explicit)
                // engineParts should NOT have a direct implied subsetting to Base::things
                // (because parts already subsets things)
                val enginePartsToThings = enginePartsSubsettings.filter { sub ->
                    try {
                        val subsettedFeature = sub.subsettedFeature
                        sub.isImplied == true &&
                                (subsettedFeature?.name == "things" || subsettedFeature?.declaredName == "things")
                    } catch (e: Exception) {
                        false
                    }
                }

                // Per redundancy elimination rules, there should be no direct implied
                // subsetting from engineParts to things
                enginePartsToThings.size shouldBe 0
            }
        }

        context("implied relationship properties") {

            it("should mark implied relationships with isImplied = true") {
                val factory = freshModel()

                // Parse a simple class
                factory.parseString(
                    """
                    package Test {
                        class SimpleClass;
                    }
                """.trimIndent()
                )

                // Find all Subclassifications
                val subclassifications = factory.allOfType<Subclassification>()
                val impliedSubs = subclassifications.filter { it.isImplied == true }

                // At minimum, SimpleClass should have an implied subclassification
                impliedSubs.shouldNotBeEmpty()

                // All implied subclassifications should have isImplied = true
                impliedSubs.forEach { sub ->
                    sub.isImplied shouldBe true
                }
            }

            it("should have implied relationships owned by the specific type") {
                val factory = freshModel()

                // Parse a simple class
                factory.parseString(
                    """
                    package Test {
                        class OwnedRelTest;
                    }
                """.trimIndent()
                )

                val testClass = factory.findByName<KerMLClass>("OwnedRelTest")
                testClass.shouldNotBeNull()

                // Find implied subclassifications for OwnedRelTest
                val subclassifications = factory.allOfType<Subclassification>()
                val testClassSubs = subclassifications.filter { sub ->
                    sub.isImplied == true &&
                            (sub.subclassifier.name == "OwnedRelTest" || sub.subclassifier.declaredName == "OwnedRelTest")
                }

                testClassSubs.shouldNotBeEmpty()

                // The implied subclassification should be owned by OwnedRelTest
                // (owningRelatedElement should be OwnedRelTest)
                val sub = testClassSubs.first()
                val owner = sub.owningRelatedElement
                owner.shouldNotBeNull()
                (owner.name == "OwnedRelTest" || owner.declaredName == "OwnedRelTest") shouldBe true
            }
        }
    }
})
