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
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.openmbee.gearshift.generated.interfaces.Feature
import org.openmbee.gearshift.generated.interfaces.Subclassification
import org.openmbee.gearshift.generated.interfaces.Subsetting
import org.openmbee.gearshift.generated.interfaces.Class as KerMLClass

/**
 * Tests for KerML 8.4.3 Core Semantics Implied Relationships (Table 8).
 *
 * Per the spec, Core semantics require:
 * - checkTypeSpecialization: Creates an implied Subclassification to Base::Anything
 * - checkFeatureSpecialization: Creates an implied Subsetting to Base::things
 *
 * These tests verify that when KerML textual syntax is parsed, the implied
 * relationships are correctly created by the engine.
 */
class CoreSemanticsImpliedRelationshipsTest : DescribeSpec({

    describe("Table 8: Core Semantics Implied Relationships") {

        context("checkTypeSpecialization -> Subclassification to Base::Anything") {

            it("should create implied Subclassification when parsing a class without explicit specialization") {
                val factory = KerMLModelFactory()

                // Load the real Base library
                KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

                // Now parse a class that should get an implied subclassification
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

                // Debug trace derivation chain
                println("DEBUG: myClass.id = ${myClass.id}")
                println("DEBUG: myClass.declaredName = ${myClass.declaredName}")
                println("DEBUG: myClass.owningMembership = ${myClass.owningMembership}")
                println("DEBUG: myClass.owningRelationship = ${myClass.owningRelationship}")
                println("DEBUG: myClass.owner = ${myClass.owner}")
                println("DEBUG: myClass.owningNamespace = ${myClass.owningNamespace}")
                val om = myClass.owningMembership
                if (om != null) {
                    println("DEBUG: owningMembership.id = ${om.id}")
                    println("DEBUG: owningMembership.membershipOwningNamespace = ${om.membershipOwningNamespace}")
                }
                println("DEBUG: myClass.name = ${myClass.name}")
                val ns = myClass.owningNamespace
                if (ns != null) {
                    println("DEBUG: owningNamespace.declaredName = ${ns.declaredName}")
                    println("DEBUG: owningNamespace.owner = ${ns.owner}")
                    println("DEBUG: owningNamespace.owningNamespace = ${ns.owningNamespace}")
                    val nsOwningNamespace = ns.owningNamespace
                    if (nsOwningNamespace != null) {
                        println("DEBUG: owningNamespace.owningNamespace.owner = ${nsOwningNamespace.owner}")
                        println("DEBUG: owningNamespace.owningNamespace.ownedMember = ${nsOwningNamespace.ownedMember}")
                        println("DEBUG: owningNamespace.owningNamespace.ownedMembership = ${nsOwningNamespace.ownedMembership}")
                        val ms = nsOwningNamespace.ownedMembership.firstOrNull()
                        if (ms is org.openmbee.gearshift.generated.interfaces.OwningMembership) {
                            println("DEBUG: First membership is OwningMembership")
                            println("DEBUG: OwningMembership.ownedMemberElement = ${ms.ownedMemberElement}")
                            println("DEBUG: OwningMembership.memberElement = ${ms.memberElement}")
                        }
                        // Test the derivation manually
                        val engine = factory.engine
                        val ownedMembershipCollection = nsOwningNamespace.ownedMembership
                        println("DEBUG: Manual: ownedMembership count = ${ownedMembershipCollection.size}")
                        val asOwningMemberships = ownedMembershipCollection.filterIsInstance<org.openmbee.gearshift.generated.interfaces.OwningMembership>()
                        println("DEBUG: Manual: after filterIsInstance<OwningMembership> count = ${asOwningMemberships.size}")
                        val ownedMemberElements = asOwningMemberships.mapNotNull { it.ownedMemberElement }
                        println("DEBUG: Manual: ownedMemberElements = $ownedMemberElements")
                    }
                    println("DEBUG: owningNamespace.qualifiedName = ${ns.qualifiedName}")
                }
                println("DEBUG: myClass.qualifiedName = ${myClass.qualifiedName}")

                myClass.owner.shouldNotBeNull()
                myClass.owner!!.name shouldBe "Test"
                myClass.qualifiedName shouldBe "Test::MyClass"


                // Find all Subclassification instances
                val subclassifications = factory.allOfType<Subclassification>()

                // There should be at least one implied subclassification for MyClass -> Base::Anything
                val impliedSubclassifications = subclassifications.filter { sub ->
                    sub.isImplied == true
                }

                impliedSubclassifications.shouldNotBeEmpty()

                // Verify that MyClass has an implied subclassification to Anything
                val myClassSubclassifications = impliedSubclassifications.filter { sub ->
                    val subclassifier = sub.subclassifier
                    subclassifier.name == "MyClass" || subclassifier.declaredName == "MyClass"
                }

                myClassSubclassifications.shouldNotBeEmpty()

                // Verify the superclassifier is Base::Anything
                val myClassSub = myClassSubclassifications.first()
                val superclassifier = myClassSub.superclassifier
                superclassifier.shouldNotBeNull()
                (superclassifier.name == "Anything" || superclassifier.declaredName == "Anything") shouldBe true
            }

            it("should not create duplicate implied Subclassification when class already specializes a type that specializes Base::Anything") {
                val factory = KerMLModelFactory()

                // Load the real Base library
                KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

                // Parse a hierarchy: Vehicle -> Car
                // Vehicle will get implied subclassification to Anything
                // Car :> Vehicle should NOT get a direct implied subclassification to Anything
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
                // Car should NOT have a direct implied subclassification to Anything
                // (because Vehicle already specializes Anything)
                val carToAnything = carSubclassifications.filter { sub ->
                    sub.isImplied == true &&
                            (sub.superclassifier.name == "Anything" || sub.superclassifier.declaredName == "Anything")
                }

                // Per redundancy elimination rules, there should be no direct implied
                // subclassification from Car to Anything
                carToAnything.size shouldBe 0
            }
        }

        context("checkFeatureSpecialization -> Subsetting to Base::things") {

            it("should create implied Subsetting when parsing a feature without explicit subsetting") {
                val factory = KerMLModelFactory()

                // Load the real Base library (includes things feature)
                KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

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
                val factory = KerMLModelFactory()

                // Load the real Base library
                KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

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
                val enginePartsSubsettings = subsettings.filter { sub ->
                    val subsettingFeature = sub.subsettingFeature
                    subsettingFeature.name == "engineParts" || subsettingFeature.declaredName == "engineParts"
                }

                // engineParts should have a subsetting to parts (explicit)
                // engineParts should NOT have a direct implied subsetting to Base::things
                // (because parts already subsets things)
                val enginePartsToThings = enginePartsSubsettings.filter { sub ->
                    sub.isImplied == true &&
                            (sub.subsettedFeature.name == "things" || sub.subsettedFeature.declaredName == "things")
                }

                // Per redundancy elimination rules, there should be no direct implied
                // subsetting from engineParts to things
                enginePartsToThings.size shouldBe 0
            }
        }

        context("implied relationship properties") {

            it("should mark implied relationships with isImplied = true") {
                val factory = KerMLModelFactory()

                // Load the real Base library
                KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

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
                val factory = KerMLModelFactory()

                // Load the real Base library
                KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

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
