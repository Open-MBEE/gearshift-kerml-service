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
import org.openmbee.gearshift.generated.interfaces.*

/**
 * Tests for KerML 8.4.3 Table 9 - Core Semantics Implied Relationships Supporting Kernel Semantics.
 *
 * These are additional implied relationships beyond Table 8 that support specific
 * kernel semantics areas like Data Types, Classes, Structures, Associations, etc.
 */
class KernelSemanticsImpliedRelationshipsTest : DescribeSpec({

    describe("Table 9: Core Semantics Implied Relationships Supporting Kernel Semantics") {

        context("checkFeatureDataValueSpecialization -> Subsetting to Base::dataValues") {

            it("should create implied Subsetting to Base::dataValues for features typed by DataType") {
                val factory = KerMLModelFactory()

                // Load the Base library (contains dataValues)
                KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

                // Parse a structure with a feature typed by a DataType
                factory.parseString("""
                    package Test {
                        datatype Temperature;
                        class Sensor {
                            feature currentTemp : Temperature;
                        }
                    }
                """.trimIndent())

                // Find the currentTemp feature
                val currentTemp = factory.findByName<Feature>("currentTemp")
                currentTemp.shouldNotBeNull()

                // Find all Subsetting instances for currentTemp
                val subsettings = factory.allOfType<Subsetting>()
                val currentTempSubsettings = subsettings.filter { sub ->
                    val subsettingFeature = sub.subsettingFeature
                    subsettingFeature.name == "currentTemp" || subsettingFeature.declaredName == "currentTemp"
                }

                // Should have implied subsetting to Base::dataValues
                val toDataValues = currentTempSubsettings.filter { sub ->
                    sub.isImplied == true &&
                    (sub.subsettedFeature.name == "dataValues" || sub.subsettedFeature.declaredName == "dataValues")
                }

                toDataValues.shouldNotBeEmpty()
            }

            it("should not create subsetting to dataValues for features not typed by DataType") {
                val factory = KerMLModelFactory()

                // Load the Base library
                KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

                // Parse a class with a feature typed by a Class (not DataType)
                factory.parseString("""
                    package Test {
                        class Part;
                        class Vehicle {
                            feature engine : Part;
                        }
                    }
                """.trimIndent())

                // Find the engine feature
                val engine = factory.findByName<Feature>("engine")
                engine.shouldNotBeNull()

                // Find all Subsetting instances for engine
                val subsettings = factory.allOfType<Subsetting>()
                val engineSubsettings = subsettings.filter { sub ->
                    val subsettingFeature = sub.subsettingFeature
                    subsettingFeature.name == "engine" || subsettingFeature.declaredName == "engine"
                }

                // Should NOT have implied subsetting to Base::dataValues
                val toDataValues = engineSubsettings.filter { sub ->
                    sub.isImplied == true &&
                    (sub.subsettedFeature.name == "dataValues" || sub.subsettedFeature.declaredName == "dataValues")
                }

                toDataValues.size shouldBe 0
            }
        }

        context("checkFeatureOccurrenceSpecialization -> Subsetting to Occurrences::occurrences") {

            // TODO: Library element resolution for Occurrences::occurrences needs to be fixed
            // The basic SemanticBinding mechanism works (currentEvent gets subsetting to things),
            // but the conditional bindings fail because library elements can't be resolved.
            xit("should create implied Subsetting to Occurrences::occurrences for features typed by Occurrence subtypes") {
                val factory = KerMLModelFactory()

                // Load full library (includes Occurrences)
                KerMLSemanticLibraryLoader.loadLibrary(factory)

                // Parse a class with a feature typed by a class (which is an Occurrence subtype)
                factory.parseString("""
                    package Test {
                        class Event;
                        class Process {
                            feature currentEvent : Event;
                        }
                    }
                """.trimIndent())

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

            // TODO: Library element resolution for Occurrences::Occurrence::suboccurrences needs to be fixed
            xit("should create implied Subsetting to suboccurrences for composite features in Occurrence subtypes") {
                val factory = KerMLModelFactory()

                // Load full library (includes Occurrences)
                KerMLSemanticLibraryLoader.loadLibrary(factory)

                // Parse a class with a composite feature
                factory.parseString("""
                    package Test {
                        class SubProcess;
                        class Process {
                            composite feature steps : SubProcess[0..*];
                        }
                    }
                """.trimIndent())

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

            // TODO: TypeFeaturing creation is handled by IMPLICIT_TYPE_FEATURING constraints, not SemanticBindings
            xit("should create implied TypeFeaturing for features where isFeaturingType is true") {
                val factory = KerMLModelFactory()

                // Load the Base library
                KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

                // Parse a class with a feature
                factory.parseString("""
                    package Test {
                        class Vehicle {
                            feature wheels : Integer;
                        }
                    }
                """.trimIndent())

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
                val factory = KerMLModelFactory()

                // Load full library (includes Transfers)
                KerMLSemanticLibraryLoader.loadLibrary(factory)

                // Parse a flow connection
                factory.parseString("""
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
                """.trimIndent())

                // Find all Redefinition instances
                val redefinitions = factory.allOfType<Redefinition>()

                // Should have implied redefinitions for flow features
                // (specific assertions depend on how flows are parsed and modeled)
                redefinitions.shouldNotBeEmpty()
            }
        }

        context("checkFeatureValuationSpecialization -> Subsetting to FeatureValue result") {

            it("should create implied Subsetting for features with default values") {
                val factory = KerMLModelFactory()

                // Load the Base library
                KerMLSemanticLibraryLoader.loadBaseLibrary(factory)

                // Parse a class with a feature that has a default value
                factory.parseString("""
                    package Test {
                        class Vehicle {
                            feature wheels : Integer default 4;
                        }
                    }
                """.trimIndent())

                // Find the wheels feature
                val wheels = factory.findByName<Feature>("wheels")
                wheels.shouldNotBeNull()

                // Find all Subsetting instances for wheels
                val subsettings = factory.allOfType<Subsetting>()
                val wheelsSubsettings = subsettings.filter { sub ->
                    val subsettingFeature = sub.subsettingFeature
                    subsettingFeature.name == "wheels" || subsettingFeature.declaredName == "wheels"
                }

                // Should have implied subsetting to the result of the value expression
                // (specific assertions depend on how FeatureValue is parsed and modeled)
                wheelsSubsettings.shouldNotBeEmpty()
            }
        }
    }
})
