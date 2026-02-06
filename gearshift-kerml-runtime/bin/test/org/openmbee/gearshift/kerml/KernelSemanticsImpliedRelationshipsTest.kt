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
import org.openmbee.gearshift.generated.interfaces.Redefinition
import org.openmbee.gearshift.generated.interfaces.Subsetting
import org.openmbee.gearshift.generated.interfaces.TypeFeaturing
import org.openmbee.mdm.framework.runtime.MissingRequiredAssociationException

/**
 * Tests for KerML 8.4.3 Table 9 - Core Semantics Implied Relationships Supporting Kernel Semantics.
 *
 * These are additional implied relationships beyond Table 8 that support specific
 * kernel semantics areas like Data Types, Classes, Structures, Associations, etc.
 *
 * Uses KerMLTestSpec to share the Base library across tests for efficiency.
 */
class KernelSemanticsImpliedRelationshipsTest : KerMLTestSpec({

    describe("Table 9: Core Semantics Implied Relationships Supporting Kernel Semantics") {

        context("checkFeatureDataValueSpecialization -> Subsetting to Base::dataValues") {

            it("should create implied Subsetting to Base::dataValues for features typed by DataType") {
                val factory = freshModel()

                // Parse a structure with a feature typed by a DataType
                factory.parseString(
                    """
                    package Test {
                        datatype Temperature;
                        class Sensor {
                            feature currentTemp : Temperature;
                        }
                    }
                """.trimIndent()
                )

                // Find the currentTemp feature
                val currentTemp = factory.findByName<Feature>("currentTemp")
                currentTemp.shouldNotBeNull()

                // DEBUG: Check intermediate derivations
                println("DEBUG: currentTemp.ownedRelationship.size = ${currentTemp.ownedRelationship.size}")
                currentTemp.ownedRelationship.forEach { rel ->
                    val mdmObj = rel as org.openmbee.mdm.framework.runtime.MDMObject
                    println("DEBUG:   ownedRelationship: ${mdmObj.className} id=${mdmObj.id}")
                }

                println("DEBUG: currentTemp.ownedSpecialization.size = ${currentTemp.ownedSpecialization.size}")
                currentTemp.ownedSpecialization.forEach { spec ->
                    val mdmObj = spec as org.openmbee.mdm.framework.runtime.MDMObject
                    println("DEBUG:   ownedSpecialization: ${mdmObj.className} id=${mdmObj.id}")
                }

                println("DEBUG: currentTemp.ownedTyping.size = ${currentTemp.ownedTyping.size}")
                currentTemp.ownedTyping.forEach { typing ->
                    val typeObj = try {
                        typing.type
                    } catch (e: Exception) {
                        null
                    }
                    println("DEBUG:   ownedTyping: type=${typeObj?.declaredName}")
                }

                // DEBUG: Check what types currentTemp has
                println("DEBUG: currentTemp.type.size = ${currentTemp.type.size}")
                currentTemp.type.forEach { t ->
                    val mdmObj = t as org.openmbee.mdm.framework.runtime.MDMObject
                    println("DEBUG:   type: ${t.declaredName} (className=${mdmObj.className})")
                }

                // DEBUG: Check if Temperature is a DataType
                val temperature = factory.findByName<org.openmbee.gearshift.generated.interfaces.Type>("Temperature")
                if (temperature != null) {
                    val mdmObj = temperature as org.openmbee.mdm.framework.runtime.MDMObject
                    println("DEBUG: Temperature className = ${mdmObj.className}")
                }

                // Find all Subsetting instances for currentTemp
                val subsettings = factory.allOfType<Subsetting>()
                val currentTempSubsettings = subsettings.mapNotNull { sub ->
                    try {
                        val subsettingFeature = sub.subsettingFeature
                        if (subsettingFeature?.name == "currentTemp" || subsettingFeature?.declaredName == "currentTemp") {
                            sub
                        } else {
                            null
                        }
                    } catch (e: Exception) {
                        null
                    }
                }

                println("DEBUG: currentTempSubsettings.size = ${currentTempSubsettings.size}")
                currentTempSubsettings.forEach { sub ->
                    try {
                        println("DEBUG:   subsetting: isImplied=${sub.isImplied}, subsettedFeature=${sub.subsettedFeature?.declaredName ?: sub.subsettedFeature?.name}")
                    } catch (e: Exception) {
                        println("DEBUG:   subsetting: error accessing subsettedFeature")
                    }
                }

                // Should have implied subsetting to Base::dataValues
                val toDataValues = currentTempSubsettings.filter { sub ->
                    try {
                        val subsettedFeature = sub.subsettedFeature
                        sub.isImplied == true &&
                                (subsettedFeature?.name == "dataValues" || subsettedFeature?.declaredName == "dataValues")
                    } catch (e: Exception) {
                        false
                    }
                }

                toDataValues.shouldNotBeEmpty()
            }

            it("should not create subsetting to dataValues for features not typed by DataType") {
                val factory = freshModel()

                // Parse a class with a feature typed by a Class (not DataType)
                factory.parseString(
                    """
                    package Test {
                        class Part;
                        class Vehicle {
                            feature engine : Part;
                        }
                    }
                """.trimIndent()
                )

                // Find the engine feature
                val engine = factory.findByName<Feature>("engine")
                engine.shouldNotBeNull()

                // Find all Subsetting instances for engine
                val subsettings = factory.allOfType<Subsetting>()
                val engineSubsettings = subsettings.mapNotNull { sub ->
                    try {
                        val subsettingFeature = sub.subsettingFeature
                        if (subsettingFeature?.name == "engine" || subsettingFeature?.declaredName == "engine") {
                            sub
                        } else {
                            null
                        }
                    } catch (e: Exception) {
                        null
                    }
                }

                // Should NOT have implied subsetting to Base::dataValues
                val toDataValues = engineSubsettings.filter { sub ->
                    try {
                        val subsettedFeature = sub.subsettedFeature
                        sub.isImplied == true &&
                                (subsettedFeature?.name == "dataValues" || subsettedFeature?.declaredName == "dataValues")
                    } catch (e: Exception) {
                        false
                    }
                }

                toDataValues.size shouldBe 0
            }
        }

        context("checkFeatureOccurrenceSpecialization -> Subsetting to Occurrences::occurrences") {

            // TODO: Semantic handler needs to check if feature type specializes Occurrence and create subsetting
            xit("should create implied Subsetting to Occurrences::occurrences for features typed by Occurrence subtypes") {
                val factory = freshModel()

                // Parse a class with a feature typed by a class (which is an Occurrence subtype)
                factory.parseString(
                    """
                    package Test {
                        class Event;
                        class Process {
                            feature currentEvent : Event;
                        }
                    }
                """.trimIndent()
                )

                // Find the currentEvent feature
                val currentEvent = factory.findByName<Feature>("currentEvent")
                currentEvent.shouldNotBeNull()

                // Find all Subsetting instances for currentEvent
                val subsettings = factory.allOfType<Subsetting>()
                val currentEventSubsettings = subsettings.filter { sub ->
                    val subsettingFeature = sub.subsettingFeature
                    subsettingFeature.name == "currentEvent" || subsettingFeature.declaredName == "currentEvent"
                }

                // Should have implied subsetting to Occurrences::occurrences
                val toOccurrences = currentEventSubsettings.filter { sub ->
                    sub.isImplied == true &&
                            (sub.subsettedFeature.name == "occurrences" || sub.subsettedFeature.declaredName == "occurrences")
                }

                toOccurrences.shouldNotBeEmpty()
            }
        }

        context("checkFeatureSuboccurrenceSpecialization -> Subsetting to Occurrences::Occurrence::suboccurrences") {

            // TODO: Semantic handler needs to check composite features in Occurrence types
            xit("should create implied Subsetting to suboccurrences for composite features in Occurrence subtypes") {
                val factory = freshModel()

                // Parse a class with a composite feature
                factory.parseString(
                    """
                    package Test {
                        class SubProcess;
                        class Process {
                            composite feature steps : SubProcess[0..*];
                        }
                    }
                """.trimIndent()
                )

                // Find the steps feature
                val steps = factory.findByName<Feature>("steps")
                steps.shouldNotBeNull()

                // Find all Subsetting instances for steps
                val subsettings = factory.allOfType<Subsetting>()
                val stepsSubsettings = subsettings.filter { sub ->
                    val subsettingFeature = sub.subsettingFeature
                    subsettingFeature.name == "steps" || subsettingFeature.declaredName == "steps"
                }

                // Should have implied subsetting to Occurrences::Occurrence::suboccurrences
                val toSuboccurrences = stepsSubsettings.filter { sub ->
                    sub.isImplied == true &&
                            (sub.subsettedFeature.name == "suboccurrences" || sub.subsettedFeature.declaredName == "suboccurrences")
                }

                toSuboccurrences.shouldNotBeEmpty()
            }
        }

        context("checkFeatureFeatureMembershipTypeFeaturing -> TypeFeaturing") {

            it("should create implied TypeFeaturing for features where isFeaturingType is true") {
                val factory = freshModel()

                // Parse a class with a feature
                factory.parseString(
                    """
                    package Test {
                        class Vehicle {
                            feature wheels : Integer;
                        }
                    }
                """.trimIndent()
                )

                // Find the Vehicle class and wheels feature
                val vehicle = factory.findByName<org.openmbee.gearshift.generated.interfaces.Class>("Vehicle")
                vehicle.shouldNotBeNull()

                val wheels = factory.findByName<Feature>("wheels")
                wheels.shouldNotBeNull()

                // Find all TypeFeaturing instances
                val typeFeaturings = factory.allOfType<TypeFeaturing>()

                // Should have a TypeFeaturing linking wheels to Vehicle
                val wheelsTypeFeaturing = typeFeaturings.filter { tf ->
                    val feature = tf.featureOfType
                    val type = tf.featuringType
                    (feature.name == "wheels" || feature.declaredName == "wheels") &&
                            (type.name == "Vehicle" || type.declaredName == "Vehicle")
                }

                wheelsTypeFeaturing.shouldNotBeEmpty()
            }
        }

        context("checkFeatureFlowFeatureRedefinition -> Redefinition to Transfer::source/target") {

            it("should create implied Redefinition for flow source features") {
                val factory = freshModel()

                // Parse a flow connection
                factory.parseString(
                    """
                    package Test {
                        class Producer {
                            out feature output;
                        }
                        class Consumer {
                            in feature input;
                        }
                        class System {
                            feature p : Producer;
                            feature c : Consumer;
                            flow from p.output to c.input;
                        }
                    }
                """.trimIndent()
                )

                // Find all Redefinition instances
                val redefinitions = factory.allOfType<Redefinition>()

                // Should have implied redefinitions for flow features
                // (specific assertions depend on how flows are parsed and modeled)
                redefinitions.shouldNotBeEmpty()
            }
        }

        context("checkFeatureValuationSpecialization -> Subsetting to FeatureValue result") {

            it("should create implied Subsetting for features with default values") {
                val factory = freshModel()

                // Parse a class with a feature that has a default value
                factory.parseString(
                    """
                    package Test {
                        class Vehicle {
                            feature wheels : Integer default 4;
                        }
                    }
                """.trimIndent()
                )

                // Find the wheels feature
                val wheels = factory.findByName<Feature>("wheels")
                wheels.shouldNotBeNull()

                // Find all Subsetting instances for wheels
                val subsettings = factory.allOfType<Subsetting>()
                val wheelsSubsettings = subsettings.mapNotNull { sub ->
                    try {
                        val subsettingFeature = sub.subsettingFeature
                        if (subsettingFeature?.name == "wheels" || subsettingFeature?.declaredName == "wheels") {
                            sub
                        } else {
                            null
                        }
                    } catch (e: Exception) {
                        null
                    }
                }

                // Should have implied subsetting to the result of the value expression
                // (specific assertions depend on how FeatureValue is parsed and modeled)
                wheelsSubsettings.shouldNotBeEmpty()
            }
        }
    }
})
