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

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.openmbee.gearshift.kerml.KerMLMetamodelLoader
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MDMObject
import org.openmbee.mdm.framework.runtime.MetamodelRegistry

class KerMLExpressionEvaluatorTest : DescribeSpec({

    lateinit var engine: MDMEngine
    lateinit var library: KernelFunctionLibrary
    lateinit var evaluator: KerMLExpressionEvaluator

    beforeEach {
        val registry = MetamodelRegistry()
        registry.ensureBaseClassRegistered()
        KerMLMetamodelLoader.initialize(registry)
        engine = MDMEngine(registry)
        library = KernelFunctionLibrary(engine)
        evaluator = KerMLExpressionEvaluator(engine, library)
    }

    describe("LiteralExpression evaluation") {

        it("should evaluate LiteralBoolean to itself") {
            val literal = engine.createElement("LiteralBoolean")
            engine.setPropertyValue(literal, "value", true)

            val target = engine.createElement("Element")
            val result = evaluator.evaluate(literal, target)

            result shouldHaveSize 1
            result.first().id shouldBe literal.id
        }

        it("should evaluate LiteralInteger to itself") {
            val literal = engine.createElement("LiteralInteger")
            engine.setPropertyValue(literal, "value", 42L)

            val target = engine.createElement("Element")
            val result = evaluator.evaluate(literal, target)

            result shouldHaveSize 1
            result.first().id shouldBe literal.id
            engine.getPropertyValue(result.first(), "value") shouldBe 42L
        }

        it("should evaluate LiteralString to itself") {
            val literal = engine.createElement("LiteralString")
            engine.setPropertyValue(literal, "value", "hello")

            val target = engine.createElement("Element")
            val result = evaluator.evaluate(literal, target)

            result shouldHaveSize 1
            engine.getPropertyValue(result.first(), "value") shouldBe "hello"
        }

        it("should evaluate LiteralRational to itself") {
            val literal = engine.createElement("LiteralRational")
            engine.setPropertyValue(literal, "value", 3.14)

            val target = engine.createElement("Element")
            val result = evaluator.evaluate(literal, target)

            result shouldHaveSize 1
            engine.getPropertyValue(result.first(), "value") shouldBe 3.14
        }

        it("should evaluate LiteralInfinity to itself") {
            val literal = engine.createElement("LiteralInfinity")

            val target = engine.createElement("Element")
            val result = evaluator.evaluate(literal, target)

            result shouldHaveSize 1
            result.first().className shouldBe "LiteralInfinity"
        }
    }

    describe("NullExpression evaluation") {

        it("should evaluate NullExpression to empty sequence") {
            val nullExpr = engine.createElement("NullExpression")

            val target = engine.createElement("Element")
            val result = evaluator.evaluate(nullExpr, target)

            result.shouldBeEmpty()
        }
    }

    describe("OperatorExpression evaluation") {

        it("should evaluate addition of two literal integers") {
            // Build: OperatorExpression(+, [LiteralInteger(3), LiteralInteger(5)])
            val opExpr = engine.createElement("OperatorExpression")
            engine.setPropertyValue(opExpr, "operator", "+")

            val arg1 = engine.createElement("LiteralInteger")
            engine.setPropertyValue(arg1, "value", 3L)

            val arg2 = engine.createElement("LiteralInteger")
            engine.setPropertyValue(arg2, "value", 5L)

            // Link arguments via ownedFeature
            linkOwnedFeature(engine, opExpr, arg1)
            linkOwnedFeature(engine, opExpr, arg2)

            val target = engine.createElement("Element")
            val result = evaluator.evaluate(opExpr, target)

            result shouldHaveSize 1
            result.first().className shouldBe "LiteralInteger"
            engine.getPropertyValue(result.first(), "value") shouldBe 8L
        }

        it("should evaluate boolean comparison") {
            val opExpr = engine.createElement("OperatorExpression")
            engine.setPropertyValue(opExpr, "operator", "<")

            val arg1 = engine.createElement("LiteralInteger")
            engine.setPropertyValue(arg1, "value", 3L)

            val arg2 = engine.createElement("LiteralInteger")
            engine.setPropertyValue(arg2, "value", 5L)

            linkOwnedFeature(engine, opExpr, arg1)
            linkOwnedFeature(engine, opExpr, arg2)

            val target = engine.createElement("Element")
            val result = evaluator.evaluate(opExpr, target)

            result shouldHaveSize 1
            result.first().className shouldBe "LiteralBoolean"
            engine.getPropertyValue(result.first(), "value") shouldBe true
        }
    }

    describe("modelLevelEvaluable") {

        it("should return true for LiteralExpression") {
            val literal = engine.createElement("LiteralBoolean")
            engine.setPropertyValue(literal, "value", true)

            evaluator.isModelLevelEvaluable(literal) shouldBe true
        }

        it("should return true for NullExpression") {
            val nullExpr = engine.createElement("NullExpression")

            evaluator.isModelLevelEvaluable(nullExpr) shouldBe true
        }

        it("should return true for MetadataAccessExpression") {
            val metaExpr = engine.createElement("MetadataAccessExpression")

            evaluator.isModelLevelEvaluable(metaExpr) shouldBe true
        }

        it("should return false for unknown element types") {
            val element = engine.createElement("Element")

            evaluator.isModelLevelEvaluable(element) shouldBe false
        }
    }
})

/**
 * Helper to link an owned feature to a namespace.
 * Uses the ownedFeature association if available.
 */
private fun linkOwnedFeature(engine: MDMEngine, owner: MDMObject, feature: MDMObject) {
    // Try to use the standard ownedFeature association
    val association = engine.schema.getAssociation("featuringTypeOwnedFeature")
    if (association != null) {
        engine.link(owner.id!!, feature.id!!, association.name)
    } else {
        // Fall back to a generic ownership association
        val ownerAssoc = engine.schema.getAssociation("namespaceOwnedMember")
            ?: engine.schema.getAssociation("elementOwnedElement")
        if (ownerAssoc != null) {
            engine.link(owner.id!!, feature.id!!, ownerAssoc.name)
        }
    }
}
