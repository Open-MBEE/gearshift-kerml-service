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

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.openmbee.gearshift.kerml.KerMLMetamodelLoader
import org.openmbee.mdm.framework.constraints.ConstraintSolverService
import org.openmbee.mdm.framework.constraints.Z3Sort
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MetamodelRegistry

class ParametricAnalysisServiceTest : DescribeSpec({

    lateinit var engine: MDMEngine
    lateinit var service: ParametricAnalysisService

    beforeEach {
        val registry = MetamodelRegistry()
        registry.ensureBaseClassRegistered()
        KerMLMetamodelLoader.initialize(registry)
        engine = MDMEngine(registry)
        service = ParametricAnalysisService(engine, ConstraintSolverService())
    }

    describe("featureToVariable") {

        it("should extract variable from Feature with declaredName") {
            val feature = engine.createElement("Feature")
            engine.setPropertyValue(feature, "declaredName", "velocity")

            val variable = service.featureToVariable(feature)

            variable.shouldNotBeNull()
            variable.name shouldBe "velocity"
            variable.sort shouldBe Z3Sort.REAL // default when no type
        }

        it("should return null for Feature without name") {
            val feature = engine.createElement("Feature")

            val variable = service.featureToVariable(feature)

            variable.shouldBeNull()
        }

        it("should infer INT sort from Integer-typed Feature") {
            val feature = engine.createElement("Feature")
            engine.setPropertyValue(feature, "declaredName", "count")

            // Create a type element named "Integer"
            val intType = engine.createElement("DataType")
            engine.setPropertyValue(intType, "declaredName", "Integer")
            engine.setPropertyValue(feature, "type", listOf(intType))

            val variable = service.featureToVariable(feature)

            variable.shouldNotBeNull()
            variable.sort shouldBe Z3Sort.INT
        }

        it("should infer BOOL sort from Boolean-typed Feature") {
            val feature = engine.createElement("Feature")
            engine.setPropertyValue(feature, "declaredName", "isActive")

            val boolType = engine.createElement("DataType")
            engine.setPropertyValue(boolType, "declaredName", "Boolean")
            engine.setPropertyValue(feature, "type", listOf(boolType))

            val variable = service.featureToVariable(feature)

            variable.shouldNotBeNull()
            variable.sort shouldBe Z3Sort.BOOL
        }

        it("should infer REAL sort from Real-typed Feature") {
            val feature = engine.createElement("Feature")
            engine.setPropertyValue(feature, "declaredName", "temperature")

            val realType = engine.createElement("DataType")
            engine.setPropertyValue(realType, "declaredName", "Real")
            engine.setPropertyValue(feature, "type", listOf(realType))

            val variable = service.featureToVariable(feature)

            variable.shouldNotBeNull()
            variable.sort shouldBe Z3Sort.REAL
        }
    }

    describe("extractConstraintExpression") {

        it("should extract expression from element with expression property") {
            val invariant = engine.createElement("Invariant")
            engine.setPropertyValue(invariant, "expression", "x > 0")

            val expr = service.extractConstraintExpression(invariant)

            expr shouldBe "x > 0"
        }

        it("should return null for element without expression") {
            val invariant = engine.createElement("Invariant")

            val expr = service.extractConstraintExpression(invariant)

            expr.shouldBeNull()
        }
    }

    describe("solveConstraints") {

        it("should return satisfiable with empty features") {
            val result = service.solveConstraints(emptyList(), emptyList())

            result.satisfiable shouldBe true
            result.assignments shouldBe emptyMap()
        }

        it("should return satisfiable with features but no constraints") {
            val feature = engine.createElement("Feature")
            engine.setPropertyValue(feature, "declaredName", "x")

            val result = service.solveConstraints(listOf(feature), emptyList())

            result.satisfiable shouldBe true
            result.assignments shouldBe emptyMap()
        }

        it("should solve constraints with named features and invariants") {
            val feature = engine.createElement("Feature")
            engine.setPropertyValue(feature, "declaredName", "x")

            val intType = engine.createElement("DataType")
            engine.setPropertyValue(intType, "declaredName", "Integer")
            engine.setPropertyValue(feature, "type", listOf(intType))

            val invariant = engine.createElement("Invariant")
            engine.setPropertyValue(invariant, "expression", "x = 42")

            val result = service.solveConstraints(listOf(feature), listOf(invariant))

            result.satisfiable shouldBe true
            result.assignments["x"] shouldBe 42L
        }
    }

    describe("tradeStudy") {

        it("should return error when no design variables") {
            val result = service.tradeStudy(emptyList(), emptyList(), "x")

            result.satisfiable shouldBe false
            result.errorMessage shouldBe "No design variables declared"
        }

        it("should optimize with bounded design variables") {
            val feature = engine.createElement("Feature")
            engine.setPropertyValue(feature, "declaredName", "x")

            val intType = engine.createElement("DataType")
            engine.setPropertyValue(intType, "declaredName", "Integer")
            engine.setPropertyValue(feature, "type", listOf(intType))

            val constraint = engine.createElement("Invariant")
            engine.setPropertyValue(constraint, "expression", "x >= 1")

            val constraint2 = engine.createElement("Invariant")
            engine.setPropertyValue(constraint2, "expression", "x <= 100")

            val result = service.tradeStudy(
                listOf(feature),
                listOf(constraint, constraint2),
                "x",
                minimize = true
            )

            result.satisfiable shouldBe true
            result.objectiveValue shouldBe 1L
        }
    }
})
