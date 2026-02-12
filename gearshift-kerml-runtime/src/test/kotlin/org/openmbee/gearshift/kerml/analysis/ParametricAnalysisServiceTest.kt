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
package org.openmbee.gearshift.kerml.analysis

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.longs.shouldBeGreaterThanOrEqual
import io.kotest.matchers.longs.shouldBeLessThanOrEqual
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.openmbee.gearshift.generated.interfaces.Feature
import org.openmbee.gearshift.generated.interfaces.FeatureValue
import org.openmbee.gearshift.generated.interfaces.Invariant
import org.openmbee.gearshift.generated.interfaces.LiteralInteger
import org.openmbee.gearshift.generated.interfaces.LiteralRational
import org.openmbee.gearshift.kerml.KerMLTestSpec
import org.openmbee.mdm.framework.constraints.ConstraintSolverService
import org.openmbee.mdm.framework.constraints.Z3Sort
import org.openmbee.mdm.framework.runtime.MDMObject

class ParametricAnalysisServiceTest : KerMLTestSpec({

    val model = freshModel()

    describe("featureToVariable") {

        it("should extract variable from Feature with declaredName") {
            model.reset()
            model.parseString("package T { feature velocity; }")

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())
            val feature = model.findByName<Feature>("velocity")

            feature.shouldNotBeNull()
            val variable = service.featureToVariable(feature)

            variable.shouldNotBeNull()
            variable.name shouldBe "velocity"
            variable.sort shouldBe Z3Sort.REAL // default when no type
        }

        it("should infer INT sort from Integer-typed Feature") {
            model.reset()
            model.parseString("package T { feature count : ScalarValues::Integer; }")

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())
            val feature = model.findByName<Feature>("count")

            feature.shouldNotBeNull()
            val variable = service.featureToVariable(feature)

            variable.shouldNotBeNull()
            variable.sort shouldBe Z3Sort.INT
        }

        it("should infer BOOL sort from Boolean-typed Feature") {
            model.reset()
            model.parseString("package T { feature isActive : ScalarValues::Boolean; }")

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())
            val feature = model.findByName<Feature>("isActive")

            feature.shouldNotBeNull()
            val variable = service.featureToVariable(feature)

            variable.shouldNotBeNull()
            variable.sort shouldBe Z3Sort.BOOL
        }

        it("should infer REAL sort from Real-typed Feature") {
            model.reset()
            model.parseString("package T { feature temperature : ScalarValues::Real; }")

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())
            val feature = model.findByName<Feature>("temperature")

            feature.shouldNotBeNull()
            val variable = service.featureToVariable(feature)

            variable.shouldNotBeNull()
            variable.sort shouldBe Z3Sort.REAL
        }
    }

    describe("extractConstraintExpression") {

        it("should extract expression from parsed invariant") {
            model.reset()
            model.parseString("package T { feature x : ScalarValues::Integer; inv { x > 0 } }")

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())

            val invariants = model.allOfType<Invariant>()
            invariants.shouldNotBeNull()
            val expr = service.extractConstraintExpression(invariants.first())
            expr.shouldNotBeNull()
            expr shouldContain ">"
        }

        it("should return null for invariant without body expression") {
            model.reset()
            model.parseString("package T { inv { } }")

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())

            val invariants = model.allOfType<Invariant>()
            invariants.shouldNotBeNull()
            val expr = service.extractConstraintExpression(invariants.first())
            expr.shouldBeNull()
        }
    }

    describe("expressionTreeToOcl") {

        it("should convert LiteralInteger from parsed invariant body") {
            model.reset()
            model.parseString("package T { inv { 42 } }")

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())

            val literal = model.allOfType<LiteralInteger>()
                .firstOrNull { it.value.toLong() == 42L }

            literal.shouldNotBeNull()
            val ocl = service.expressionTreeToOcl(literal as MDMObject)
            ocl shouldBe "42"
        }

        it("should convert OperatorExpression from parsed invariant") {
            model.reset()
            model.parseString("package T { feature x : ScalarValues::Integer; inv { x > 0 } }")

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())

            val invariants = model.allOfType<Invariant>()
            invariants.shouldNotBeNull()
            val expr = service.extractConstraintExpression(invariants.first())
            expr.shouldNotBeNull()
            expr shouldContain ">"
        }
    }

    describe("solveConstraints") {

        it("should return satisfiable with no constraints") {
            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())
            val result = service.solveConstraints(emptyList())

            result.satisfiable shouldBe true
            result.assignments shouldBe emptyMap()
        }

        it("should solve simple equality constraint") {
            model.reset()
            model.parseString("package T { feature x : ScalarValues::Integer; inv { x == 42 } }")

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())
            val invariants = model.allOfType<Invariant>()

            val result = service.solveConstraints(invariants)

            result.satisfiable shouldBe true
            result.assignments["x"] shouldBe 42L
        }

        it("should solve bounded constraints") {
            model.reset()
            model.parseString(
                """
                package T {
                    feature x : ScalarValues::Integer;
                    inv { x >= 10 }
                    inv { x <= 20 }
                }
            """.trimIndent()
            )

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())
            val invariants = model.allOfType<Invariant>()

            val result = service.solveConstraints(invariants)

            result.satisfiable shouldBe true
            val xVal = result.assignments["x"] as Long
            xVal.shouldBeGreaterThanOrEqual(10L)
            xVal.shouldBeLessThanOrEqual(20L)
        }

        it("should solve multi-variable constraints") {
            model.reset()
            model.parseString(
                """
                package T {
                    feature x : ScalarValues::Integer;
                    feature y : ScalarValues::Integer;
                    inv { x + y == 10 }
                    inv { x > y }
                }
            """.trimIndent()
            )

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())
            val invariants = model.allOfType<Invariant>()

            val result = service.solveConstraints(invariants)

            result.satisfiable shouldBe true
            val xVal = result.assignments["x"] as Long
            val yVal = result.assignments["y"] as Long
            (xVal + yVal) shouldBe 10L
            xVal shouldBeGreaterThan (yVal)
        }

        it("should detect unsatisfiable constraints") {
            model.reset()
            model.parseString(
                """
                package T {
                    feature x : ScalarValues::Integer;
                    inv { x > 10 }
                    inv { x < 5 }
                }
            """.trimIndent()
            )

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())
            val invariants = model.allOfType<Invariant>()

            val result = service.solveConstraints(invariants)

            result.satisfiable shouldBe false
        }
    }

    describe("tradeStudy") {

        it("should return error when no design variables") {
            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())
            val result = service.tradeStudy(emptyList(), emptyList(), "x")

            result.satisfiable shouldBe false
            result.errorMessage shouldBe "No design variables declared"
        }

        it("should minimize objective") {
            model.reset()
            model.parseString(
                """
                package T {
                    feature x : ScalarValues::Integer;
                    inv { x >= 1 }
                    inv { x <= 100 }
                }
            """.trimIndent()
            )

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())

            val features = model.allOfType<Feature>().filter { it.declaredName == "x" }
            val invariants = model.allOfType<Invariant>()

            val result = service.tradeStudy(features, invariants, "x", minimize = true)

            result.satisfiable shouldBe true
            result.objectiveValue shouldBe 1L
        }

        it("should maximize objective") {
            model.reset()
            model.parseString(
                """
                package T {
                    feature x : ScalarValues::Integer;
                    inv { x >= 1 }
                    inv { x <= 100 }
                }
            """.trimIndent()
            )

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())

            val features = model.allOfType<Feature>().filter { it.declaredName == "x" }
            val invariants = model.allOfType<Invariant>()

            val result = service.tradeStudy(features, invariants, "x", minimize = false)

            result.satisfiable shouldBe true
            result.objectiveValue shouldBe 100L
        }
    }

    describe("checkRequirementConsistency") {

        it("should detect consistent requirements") {
            model.reset()
            model.parseString(
                """
                package T {
                    feature x : ScalarValues::Integer;
                    inv { x > 0 }
                    inv { x < 100 }
                }
            """.trimIndent()
            )

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())

            val invariants = model.allOfType<Invariant>()

            val result = service.checkRequirementConsistency(invariants)

            result.consistent shouldBe true
        }

        it("should detect conflicting requirements") {
            model.reset()
            model.parseString(
                """
                package T {
                    feature x : ScalarValues::Integer;
                    inv { x > 10 }
                    inv { x < 5 }
                }
            """.trimIndent()
            )

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())

            val invariants = model.allOfType<Invariant>()

            val result = service.checkRequirementConsistency(invariants)

            result.consistent shouldBe false
        }
    }

    describe("cross-package references") {

        it("should solve invariant referencing feature in another package") {
            model.reset()
            model.parseString(
                """
                package Physics {
                    feature x : ScalarValues::Integer;
                }
                package Rules {
                    inv { Physics::x > 0 }
                }
            """.trimIndent()
            )

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())
            val invariants = model.allOfType<Invariant>()

            val result = service.solveConstraints(invariants)

            result.satisfiable shouldBe true
            val xVal = result.assignments["x"] as Long
            xVal.shouldBeGreaterThan(0L)
        }

        it("should solve multi-level qualified name references") {
            model.reset()
            model.parseString(
                """
                package Outer {
                    package Inner {
                        feature y : ScalarValues::Integer;
                    }
                }
                package Rules {
                    inv { Outer::Inner::y == 7 }
                }
            """.trimIndent()
            )

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())
            val invariants = model.allOfType<Invariant>()

            val result = service.solveConstraints(invariants)

            result.satisfiable shouldBe true
            result.assignments["y"] shouldBe 7L
        }

        it("should solve with multiple features across packages") {
            model.reset()
            model.parseString(
                """
                package Design {
                    feature width : ScalarValues::Integer;
                    feature height : ScalarValues::Integer;
                }
                package Req {
                    inv { Design::width + Design::height == 20 }
                }
            """.trimIndent()
            )

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())
            val invariants = model.allOfType<Invariant>()

            val result = service.solveConstraints(invariants)

            result.satisfiable shouldBe true
            val w = result.assignments["width"] as Long
            val h = result.assignments["height"] as Long
            (w + h) shouldBe 20L
        }

        it("should detect unsatisfiable cross-package constraints") {
            model.reset()
            model.parseString(
                """
                package Vars {
                    feature z : ScalarValues::Integer;
                }
                package A {
                    inv { Vars::z > 100 }
                }
                package B {
                    inv { Vars::z < 50 }
                }
            """.trimIndent()
            )

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())
            val invariants = model.allOfType<Invariant>()

            val result = service.solveConstraints(invariants)

            result.satisfiable shouldBe false
        }
    }

    describe("scope filtering") {

        it("should scope solve to invariants in a specific package") {
            model.reset()
            model.parseString(
                """
                package A {
                    feature x : ScalarValues::Integer;
                    inv invA { x == 5 }
                }
                package B {
                    feature y : ScalarValues::Integer;
                    inv invB { y == 99 }
                }
            """.trimIndent()
            )

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())

            // Scope to package A only: filter by owning namespace QN
            val allInvariants = model.allOfType<Invariant>()
            val scopedInvariants = allInvariants.filter { inv ->
                inv.owningNamespace?.qualifiedName == "A"
            }

            val result = service.solveConstraints(scopedInvariants)

            result.satisfiable shouldBe true
            result.assignments["x"] shouldBe 5L
            result.assignments.containsKey("y") shouldBe false
        }

        it("should isolate unsatisfiable scope from satisfiable scope") {
            model.reset()
            model.parseString(
                """
                package Good {
                    feature a : ScalarValues::Integer;
                    inv { a >= 1 }
                    inv { a <= 10 }
                }
                package Bad {
                    feature b : ScalarValues::Integer;
                    inv { b > 100 }
                    inv { b < 50 }
                }
            """.trimIndent()
            )

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())

            val allInvariants = model.allOfType<Invariant>()

            // Scope to "Good" — should be satisfiable
            // Filter by owning namespace QN (anonymous invariants have null QN themselves)
            val goodInvariants = allInvariants.filter { inv ->
                inv.owningNamespace?.qualifiedName == "Good"
            }
            val goodResult = service.solveConstraints(goodInvariants)
            goodResult.satisfiable shouldBe true

            // Scope to "Bad" — should be unsatisfiable
            val badInvariants = allInvariants.filter { inv ->
                inv.owningNamespace?.qualifiedName == "Bad"
            }
            val badResult = service.solveConstraints(badInvariants)
            badResult.satisfiable shouldBe false
        }
    }

    describe("applySolution") {

        it("should persist integer solution as FeatureValue") {
            // Use a fresh model since applySolution creates elements that pollute shared state
            val freshModel = freshModel()
            freshModel.parseString(
                """
                package T {
                    feature x : ScalarValues::Integer;
                    inv { x == 42 }
                }
            """.trimIndent()
            )

            val service = ParametricAnalysisService(freshModel.engine, ConstraintSolverService())
            val invariants = freshModel.allOfType<Invariant>()
            val result = service.solveConstraints(invariants)

            result.satisfiable shouldBe true

            val features = invariants
                .flatMap { service.collectReferencedFeatures(it) }
                .distinctBy { (it as MDMObject).id }
            service.applySolution(result.assignments, features)

            // Verify FeatureValue was created and linked
            val featureValues = freshModel.allOfType<FeatureValue>()
            featureValues.shouldHaveSize(1)

            val fv = featureValues.first()
            val valueExpr = fv.value
            valueExpr.shouldNotBeNull()
            (valueExpr is LiteralInteger) shouldBe true
            (valueExpr as LiteralInteger).value shouldBe 42
        }

        it("should persist multi-variable solution") {
            // Use a fresh model to avoid stale state from previous applySolution
            val freshModel = freshModel()
            freshModel.parseString(
                """
                package T {
                    feature x : ScalarValues::Integer;
                    feature y : ScalarValues::Integer;
                    inv { x + y == 10 }
                    inv { x > y }
                }
            """.trimIndent()
            )

            val service = ParametricAnalysisService(freshModel.engine, ConstraintSolverService())
            val invariants = freshModel.allOfType<Invariant>()
            val result = service.solveConstraints(invariants)

            result.satisfiable shouldBe true

            val features = invariants
                .flatMap { service.collectReferencedFeatures(it) }
                .distinctBy { (it as MDMObject).id }
            service.applySolution(result.assignments, features)

            val featureValues = freshModel.allOfType<FeatureValue>()
            featureValues.shouldHaveSize(2)
        }
    }

    describe("analyzeImpact") {

        it("should find single invariant referencing a feature") {
            model.reset()
            model.parseString(
                """
                package T {
                    feature x : ScalarValues::Integer;
                    inv { x > 0 }
                }
            """.trimIndent()
            )

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())
            val invariants = model.allOfType<Invariant>()

            val impact = service.analyzeImpact("x", invariants)

            impact.featureName shouldBe "x"
            impact.affectedInvariants shouldHaveSize 1
            impact.coConstrainedFeatures.shouldBeEmpty()
        }

        it("should identify co-constrained features") {
            model.reset()
            model.parseString(
                """
                package T {
                    feature x : ScalarValues::Integer;
                    feature y : ScalarValues::Integer;
                    inv { x + y == 10 }
                }
            """.trimIndent()
            )

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())
            val invariants = model.allOfType<Invariant>()

            val impact = service.analyzeImpact("x", invariants)

            impact.featureName shouldBe "x"
            impact.affectedInvariants shouldHaveSize 1
            impact.coConstrainedFeatures shouldContain "y"
        }

        it("should detect violated invariant with proposed value") {
            model.reset()
            model.parseString(
                """
                package T {
                    feature x : ScalarValues::Integer;
                    inv { x >= 1 }
                    inv { x <= 100 }
                }
            """.trimIndent()
            )

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())
            val invariants = model.allOfType<Invariant>()

            val impact = service.analyzeImpact("x", invariants, proposedValue = 200)

            impact.featureName shouldBe "x"
            impact.affectedInvariants shouldHaveSize 2
            (impact.violatedInvariants.size > 0) shouldBe true
            impact.stillSatisfiable.shouldNotBeNull()
            impact.stillSatisfiable!!.shouldBeFalse()
        }

        it("should accept valid proposed value") {
            model.reset()
            model.parseString(
                """
                package T {
                    feature x : ScalarValues::Integer;
                    inv { x >= 1 }
                    inv { x <= 100 }
                }
            """.trimIndent()
            )

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())
            val invariants = model.allOfType<Invariant>()

            val impact = service.analyzeImpact("x", invariants, proposedValue = 50)

            impact.featureName shouldBe "x"
            impact.affectedInvariants shouldHaveSize 2
            impact.violatedInvariants.shouldBeEmpty()
            impact.stillSatisfiable.shouldNotBeNull()
            impact.stillSatisfiable!!.shouldBeTrue()
        }

        it("should find all invariants referencing a feature across multiple constraints") {
            model.reset()
            model.parseString(
                """
                package T {
                    feature x : ScalarValues::Integer;
                    feature y : ScalarValues::Integer;
                    inv { x > 0 }
                    inv { x < 100 }
                    inv { x + y == 50 }
                }
            """.trimIndent()
            )

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())
            val invariants = model.allOfType<Invariant>()

            val impact = service.analyzeImpact("x", invariants)

            impact.featureName shouldBe "x"
            impact.affectedInvariants shouldHaveSize 3
            impact.coConstrainedFeatures shouldContain "y"
        }
    }
})
