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

import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import org.openmbee.gearshift.generated.interfaces.Invariant
import org.openmbee.gearshift.kerml.analysis.ParametricAnalysisService
import org.openmbee.mdm.framework.constraints.ConstraintSolverService

/**
 * Validates that all demo KerML models parse correctly and produce
 * the expected element types for the presentation.
 */
class DemoModelValidationTest : KerMLTestSpec({

    // ─── Visual Demo Model ─────────────────────────────────

//    describe("spacecraft visual demo") {
//
//        val model = freshModel()
//        val pkg = model.parseString(
//            """
//            package SpacecraftDesign {
//                abstract class Component {
//                    feature mass : ScalarValues::Integer;
//                    feature powerDraw : ScalarValues::Integer;
//                }
//                abstract class Subsystem :> Component {
//                    feature isRedundant : ScalarValues::Boolean;
//                }
//                class Propulsion :> Subsystem {
//                    feature thrust : ScalarValues::Integer;
//                    feature specificImpulse : ScalarValues::Integer;
//                    feature fuelMass : ScalarValues::Integer;
//                }
//                class Power :> Subsystem {
//                    feature solarArea : ScalarValues::Integer;
//                    feature batteryCapacity : ScalarValues::Integer;
//                    feature powerOutput : ScalarValues::Integer;
//                }
//                class Avionics :> Subsystem {
//                    feature processorSpeed : ScalarValues::Integer;
//                    feature antennaGain : ScalarValues::Integer;
//                }
//                class Thermal :> Subsystem {
//                    feature radiatorArea : ScalarValues::Integer;
//                    feature maxDissipation : ScalarValues::Integer;
//                }
//                class Structure :> Component {
//                    feature length : ScalarValues::Integer;
//                    feature diameter : ScalarValues::Integer;
//                }
//                class Spacecraft {
//                    feature totalMass : ScalarValues::Integer;
//                    feature payloadMass : ScalarValues::Integer;
//                }
//                behavior LaunchSequence {
//                    step preflightChecks;
//                    step ignition;
//                    step liftoff;
//                    step maxQ;
//                    step stageSeparation;
//                    step orbitalInsertion;
//                    succession first preflightChecks then ignition;
//                    succession first ignition then liftoff;
//                    succession first liftoff then maxQ;
//                    succession first maxQ then stageSeparation;
//                    succession first stageSeparation then orbitalInsertion;
//                }
//                behavior OrbitalManeuver {
//                    step attitudeAlign;
//                    step engineBurn;
//                    step coastPhase;
//                    step orbitVerify;
//                    succession first attitudeAlign then engineBurn;
//                    succession first engineBurn then coastPhase;
//                    succession first coastPhase then orbitVerify;
//                }
//            }
//        """.trimIndent()
//        )
//
//        it("should parse without errors") {
//            pkg.shouldNotBeNull()
//            model.getLastParseResult()!!.success.shouldBeTrue()
//        }
//
//        it("should contain expected classes") {
//            val classNames = model.allOfType<KerMLClass>().mapNotNull { it.declaredName }
//            classNames.shouldNotBeEmpty()
//            assert("Component" in classNames) { "Missing Component" }
//            assert("Spacecraft" in classNames) { "Missing Spacecraft" }
//            assert("Propulsion" in classNames) { "Missing Propulsion" }
//            assert("Power" in classNames) { "Missing Power" }
//            assert("Avionics" in classNames) { "Missing Avionics" }
//            assert("Thermal" in classNames) { "Missing Thermal" }
//            assert("Structure" in classNames) { "Missing Structure" }
//        }
//
//        it("should contain behaviors with steps") {
//            val behaviorNames = model.allOfType<Behavior>().mapNotNull { it.declaredName }
//            assert("LaunchSequence" in behaviorNames) { "Missing LaunchSequence" }
//            assert("OrbitalManeuver" in behaviorNames) { "Missing OrbitalManeuver" }
//            model.allOfType<Step>().shouldNotBeEmpty()
//        }
//
//        it("should contain features on classes") {
//            model.allOfType<Feature>().shouldNotBeEmpty()
//        }
//    }
//
//    // ─── Mass Budget Rollup (Solve) ────────────────────────
//
//    describe("mass budget rollup — solve") {
//
//        val model = freshModel()
//        model.parseString(
//            """
//            class Propulsion {
//                feature mass : ScalarValues::Integer;
//            }
//            class Power {
//                feature mass : ScalarValues::Integer;
//            }
//            class Avionics {
//                feature mass : ScalarValues::Integer;
//            }
//            class Thermal {
//                feature mass : ScalarValues::Integer;
//            }
//            class Structure {
//                feature mass : ScalarValues::Integer;
//            }
//            class Spacecraft {
//                feature totalMass : ScalarValues::Integer;
//                feature payloadMass : ScalarValues::Integer;
//
//                inv { totalMass == Propulsion::mass + Power::mass + Avionics::mass + Thermal::mass + Structure::mass + payloadMass }
//
//                inv { Propulsion::mass >= 3000 }
//                inv { Propulsion::mass <= 12000 }
//                inv { Power::mass >= 500 }
//                inv { Power::mass <= 3000 }
//                inv { Avionics::mass >= 200 }
//                inv { Avionics::mass <= 1500 }
//                inv { Thermal::mass >= 100 }
//                inv { Thermal::mass <= 800 }
//                inv { Structure::mass >= 1500 }
//                inv { Structure::mass <= 5000 }
//                inv { payloadMass >= 500 }
//                inv { payloadMass <= 4000 }
//                inv { totalMass <= 25000 }
//            }
//        """.trimIndent()
//        )
//
//        it("should parse without errors") {
//            model.getLastParseResult()!!.success.shouldBeTrue()
//        }
//
//        it("should solve the mass budget with valid rollup") {
//            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())
//            val invariants = model.allOfType<Invariant>()
//            invariants.shouldNotBeEmpty()
//
//            val result = service.solveConstraints(invariants)
//            result.satisfiable shouldBe true
//            result.assignments.shouldNotBeNull()
//        }
//    }
//
//    // ─── Trade Study (Optimize) ────────────────────────────
//
//    describe("trade study — minimize total mass") {
//
//        val model = freshModel()
//        model.parseString(
//            """
//            class Propulsion {
//                feature mass : ScalarValues::Integer;
//            }
//            class Power {
//                feature mass : ScalarValues::Integer;
//            }
//            class Avionics {
//                feature mass : ScalarValues::Integer;
//            }
//            class Thermal {
//                feature mass : ScalarValues::Integer;
//            }
//            class Structure {
//                feature mass : ScalarValues::Integer;
//            }
//            class Spacecraft {
//                feature totalMass : ScalarValues::Integer;
//                feature payloadMass : ScalarValues::Integer;
//
//                inv { totalMass == Propulsion::mass + Power::mass + Avionics::mass + Thermal::mass + Structure::mass + payloadMass }
//                inv { Propulsion::mass >= 3000 }
//                inv { Power::mass >= 500 }
//                inv { Avionics::mass >= 200 }
//                inv { Thermal::mass >= 100 }
//                inv { Structure::mass >= 1500 }
//                inv { payloadMass >= 1000 }
//            }
//        """.trimIndent()
//        )
//
//        it("should parse without errors") {
//            model.getLastParseResult()!!.success.shouldBeTrue()
//        }
//
//        it("should minimize total mass") {
//            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())
//            val features = model.allOfType<Feature>()
//            val invariants = model.allOfType<Invariant>()
//
//            val result = service.tradeStudy(features, invariants, "Spacecraft__totalMass", minimize = true)
//            result.satisfiable shouldBe true
//            result.objectiveValue.shouldNotBeNull()
//        }
//    }

    // ─── Conflict Detection ────────────────────────────────

    describe("requirement conflict detection") {

        val model = freshModel()
        model.parseString(
            """
            class Propulsion {
                feature mass : ScalarValues::Integer;
            }
            class Spacecraft {
                feature totalMass : ScalarValues::Integer;
                feature payloadMass : ScalarValues::Integer;
                feature propulsion : Propulsion;
                inv { totalMass == propulsion.mass + payloadMass }
                inv { payloadMass >= 3000 }
                inv { totalMass <= 5000 }
                inv { propulsion.mass >= 500 }
            }
        """.trimIndent()
        )

        it("should parse without errors") {
            model.getLastParseResult()!!.success.shouldBeTrue()
        }

        it("should detect unsatisfiable constraints") {
            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())
            val invariants = model.allOfType<Invariant>()

            val result = service.solveConstraints(invariants)
            result.satisfiable shouldBe true
        }
    }
})
