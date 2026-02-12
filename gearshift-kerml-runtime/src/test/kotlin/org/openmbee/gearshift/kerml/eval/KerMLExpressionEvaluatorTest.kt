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
package org.openmbee.gearshift.kerml.eval

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.openmbee.gearshift.generated.interfaces.Feature
import org.openmbee.gearshift.generated.interfaces.Invariant
import org.openmbee.gearshift.kerml.KerMLTestSpec
import org.openmbee.mdm.framework.runtime.MDMObject

class KerMLExpressionEvaluatorTest : KerMLTestSpec({

    val model = freshModel()
    lateinit var library: KernelFunctionLibrary
    lateinit var evaluator: KerMLExpressionEvaluator

    beforeEach {
        model.reset()
        library = KernelFunctionLibrary(model.engine)
        evaluator = KerMLExpressionEvaluator(model.engine, library)
    }

    fun extractBodyExpression(): MDMObject {
        val invariant = model.allOfType<Invariant>().first() as MDMObject
        val resultExpr = invariant.getProperty("resultExpression") as? MDMObject
        if (resultExpr != null) return resultExpr
        val memberships = model.engine.getPropertyValue(invariant, "ownedFeatureMembership")
        val membershipList = when (memberships) {
            is List<*> -> memberships.filterIsInstance<MDMObject>()
            is MDMObject -> listOf(memberships)
            else -> error("Invariant has no resultExpression or ownedFeatureMembership")
        }
        val resultMembership = membershipList.firstOrNull {
            model.engine.isInstanceOf(it, "ResultExpressionMembership")
        } ?: error("No ResultExpressionMembership found in invariant")
        return model.engine.getPropertyValue(resultMembership, "ownedResultExpression") as? MDMObject
            ?: error("ResultExpressionMembership has no ownedResultExpression")
    }

    describe("LiteralExpression evaluation") {

        it("should evaluate LiteralInteger") {
            model.parseString("package T { feature target; inv { 42 } }")

            val expr = extractBodyExpression()
            val target = model.findByName<Feature>("target") as MDMObject
            val result = evaluator.evaluate(expr, target)

            result shouldHaveSize 1
            result.first().className shouldBe "LiteralInteger"
            model.engine.getPropertyValue(result.first(), "value") shouldBe 42
        }

        it("should evaluate LiteralBoolean") {
            model.parseString("package T { feature target; inv { true } }")

            val expr = extractBodyExpression()
            val target = model.findByName<Feature>("target") as MDMObject
            val result = evaluator.evaluate(expr, target)

            result shouldHaveSize 1
            result.first().className shouldBe "LiteralBoolean"
            model.engine.getPropertyValue(result.first(), "value") shouldBe true
        }

        it("should evaluate LiteralString") {
            model.parseString("""package T { feature target; inv { "hello" } }""")

            val expr = extractBodyExpression()
            val target = model.findByName<Feature>("target") as MDMObject
            val result = evaluator.evaluate(expr, target)

            result shouldHaveSize 1
            result.first().className shouldBe "LiteralString"
            model.engine.getPropertyValue(result.first(), "value") shouldBe "hello"
        }

        it("should evaluate LiteralRational") {
            model.parseString("package T { feature target; inv { 3.14 } }")

            val expr = extractBodyExpression()
            val target = model.findByName<Feature>("target") as MDMObject
            val result = evaluator.evaluate(expr, target)

            result shouldHaveSize 1
            result.first().className shouldBe "LiteralRational"
            model.engine.getPropertyValue(result.first(), "value") shouldBe 3.14
        }

        it("should evaluate LiteralInfinity") {
            model.parseString("package T { feature target; inv { * } }")

            val expr = extractBodyExpression()
            val target = model.findByName<Feature>("target") as MDMObject
            val result = evaluator.evaluate(expr, target)

            result shouldHaveSize 1
            result.first().className shouldBe "LiteralInfinity"
        }
    }

    describe("NullExpression evaluation") {

        it("should evaluate NullExpression to empty sequence") {
            model.parseString("package T { feature target; inv { null } }")

            val expr = extractBodyExpression()
            val target = model.findByName<Feature>("target") as MDMObject
            val result = evaluator.evaluate(expr, target)

            result.shouldBeEmpty()
        }
    }

    describe("OperatorExpression evaluation") {

        it("should evaluate addition") {
            model.parseString("package T { feature target; inv { 3 + 5 } }")

            val expr = extractBodyExpression()
            val target = model.findByName<Feature>("target") as MDMObject
            val result = evaluator.evaluate(expr, target)

            result shouldHaveSize 1
            result.first().className shouldBe "LiteralInteger"
            model.engine.getPropertyValue(result.first(), "value") shouldBe 8L
        }

        it("should evaluate subtraction") {
            model.parseString("package T { feature target; inv { 10 - 3 } }")

            val expr = extractBodyExpression()
            val target = model.findByName<Feature>("target") as MDMObject
            val result = evaluator.evaluate(expr, target)

            result shouldHaveSize 1
            result.first().className shouldBe "LiteralInteger"
            model.engine.getPropertyValue(result.first(), "value") shouldBe 7L
        }

        it("should evaluate multiplication") {
            model.parseString("package T { feature target; inv { 4 * 3 } }")

            val expr = extractBodyExpression()
            val target = model.findByName<Feature>("target") as MDMObject
            val result = evaluator.evaluate(expr, target)

            result shouldHaveSize 1
            result.first().className shouldBe "LiteralInteger"
            model.engine.getPropertyValue(result.first(), "value") shouldBe 12L
        }

        it("should evaluate less-than comparison") {
            model.parseString("package T { feature target; inv { 3 < 5 } }")

            val expr = extractBodyExpression()
            val target = model.findByName<Feature>("target") as MDMObject
            val result = evaluator.evaluate(expr, target)

            result shouldHaveSize 1
            result.first().className shouldBe "LiteralBoolean"
            model.engine.getPropertyValue(result.first(), "value") shouldBe true
        }

        it("should evaluate greater-than comparison") {
            model.parseString("package T { feature target; inv { 5 > 3 } }")

            val expr = extractBodyExpression()
            val target = model.findByName<Feature>("target") as MDMObject
            val result = evaluator.evaluate(expr, target)

            result shouldHaveSize 1
            result.first().className shouldBe "LiteralBoolean"
            model.engine.getPropertyValue(result.first(), "value") shouldBe true
        }

        it("should evaluate boolean 'and'") {
            model.parseString("package T { feature target; inv { true and false } }")

            val expr = extractBodyExpression()
            val target = model.findByName<Feature>("target") as MDMObject
            val result = evaluator.evaluate(expr, target)

            result shouldHaveSize 1
            result.first().className shouldBe "LiteralBoolean"
            model.engine.getPropertyValue(result.first(), "value") shouldBe false
        }

        it("should evaluate boolean 'or'") {
            model.parseString("package T { feature target; inv { true or false } }")

            val expr = extractBodyExpression()
            val target = model.findByName<Feature>("target") as MDMObject
            val result = evaluator.evaluate(expr, target)

            result shouldHaveSize 1
            result.first().className shouldBe "LiteralBoolean"
            model.engine.getPropertyValue(result.first(), "value") shouldBe true
        }

        it("should evaluate boolean 'not'") {
            model.parseString("package T { feature target; inv { not true } }")

            val expr = extractBodyExpression()
            val target = model.findByName<Feature>("target") as MDMObject
            val result = evaluator.evaluate(expr, target)

            result shouldHaveSize 1
            result.first().className shouldBe "LiteralBoolean"
            model.engine.getPropertyValue(result.first(), "value") shouldBe false
        }

        it("should evaluate nested arithmetic") {
            model.parseString("package T { feature target; inv { (2 + 3) * 4 } }")

            val expr = extractBodyExpression()
            val target = model.findByName<Feature>("target") as MDMObject
            val result = evaluator.evaluate(expr, target)

            result shouldHaveSize 1
            result.first().className shouldBe "LiteralInteger"
            model.engine.getPropertyValue(result.first(), "value") shouldBe 20L
        }
    }

    describe("FeatureReferenceExpression evaluation") {

        it("should resolve feature reference to referent") {
            model.parseString(
                """
                package T {
                    feature x;
                    inv { x }
                }
            """.trimIndent()
            )

            val expr = extractBodyExpression()
            val x = model.findByName<Feature>("x") as MDMObject
            val result = evaluator.evaluate(expr, x)

            result shouldHaveSize 1
            (result.first() as Feature).declaredName shouldBe "x"
        }
    }

    describe("modelLevelEvaluable") {

        it("should return true for LiteralExpression") {
            model.parseString("package T { inv { 42 } }")

            val expr = extractBodyExpression()

            evaluator.isModelLevelEvaluable(expr) shouldBe true
        }

        it("should return true for NullExpression") {
            model.parseString("package T { inv { null } }")

            val expr = extractBodyExpression()

            evaluator.isModelLevelEvaluable(expr) shouldBe true
        }

        it("should return true for OperatorExpression with literal args") {
            model.parseString("package T { inv { 3 + 5 } }")

            val expr = extractBodyExpression()

            evaluator.isModelLevelEvaluable(expr) shouldBe true
        }

        it("should return false for non-expression Element") {
            model.parseString("package T { feature notAnExpression; }")

            val feature = model.findByName<Feature>("notAnExpression") as MDMObject

            evaluator.isModelLevelEvaluable(feature) shouldBe false
        }
    }
})
