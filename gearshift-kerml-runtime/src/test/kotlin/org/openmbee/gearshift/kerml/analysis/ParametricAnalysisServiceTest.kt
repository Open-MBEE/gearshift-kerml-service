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

import io.kotest.matchers.longs.shouldBeGreaterThanOrEqual
import io.kotest.matchers.longs.shouldBeLessThanOrEqual
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.string.shouldContain
import org.openmbee.gearshift.kerml.KerMLMetamodelLoader
import org.openmbee.gearshift.kerml.KerMLTestSpec
import org.openmbee.mdm.framework.constraints.ConstraintSolverService
import org.openmbee.mdm.framework.constraints.Z3Sort
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MetamodelRegistry

class ParametricAnalysisServiceTest : KerMLTestSpec({

    // Helper to create a fresh engine/service pair for legacy hand-built tests
    fun legacySetup(): Pair<MDMEngine, ParametricAnalysisService> {
        val registry = MetamodelRegistry()
        registry.ensureBaseClassRegistered()
        KerMLMetamodelLoader.initialize(registry)
        val engine = MDMEngine(registry)
        val service = ParametricAnalysisService(engine, ConstraintSolverService())
        return engine to service
    }

    // === Legacy hand-built tests ===
    // These tests validate low-level featureToVariable/extractConstraintExpression behavior
    // using hand-constructed MDMObjects (no parser involved).

    describe("featureToVariable") {

        it("should extract variable from Feature with declaredName") {
            val (engine, service) = legacySetup()
            val feature = engine.createElement("Feature")
            engine.setPropertyValue(feature, "declaredName", "velocity")

            val variable = service.featureToVariable(feature)

            variable.shouldNotBeNull()
            variable.name shouldBe "velocity"
            variable.sort shouldBe Z3Sort.REAL // default when no type
        }

        it("should return null for Feature without name") {
            val (engine, service) = legacySetup()
            val feature = engine.createElement("Feature")

            val variable = service.featureToVariable(feature)

            variable.shouldBeNull()
        }

        it("should infer INT sort from Integer-typed Feature") {
            val (engine, service) = legacySetup()
            val feature = engine.createElement("Feature")
            engine.setPropertyValue(feature, "declaredName", "count")

            // Set type directly on the MDMObject to bypass association validation
            val intType = engine.createElement("DataType")
            engine.setPropertyValue(intType, "declaredName", "Integer")
            feature.setProperty("type", listOf(intType))

            val variable = service.featureToVariable(feature)

            variable.shouldNotBeNull()
            variable.sort shouldBe Z3Sort.INT
        }

        it("should infer BOOL sort from Boolean-typed Feature") {
            val (engine, service) = legacySetup()
            val feature = engine.createElement("Feature")
            engine.setPropertyValue(feature, "declaredName", "isActive")

            val boolType = engine.createElement("DataType")
            engine.setPropertyValue(boolType, "declaredName", "Boolean")
            feature.setProperty("type", listOf(boolType))

            val variable = service.featureToVariable(feature)

            variable.shouldNotBeNull()
            variable.sort shouldBe Z3Sort.BOOL
        }

        it("should infer REAL sort from Real-typed Feature") {
            val (engine, service) = legacySetup()
            val feature = engine.createElement("Feature")
            engine.setPropertyValue(feature, "declaredName", "temperature")

            val realType = engine.createElement("DataType")
            engine.setPropertyValue(realType, "declaredName", "Real")
            feature.setProperty("type", listOf(realType))

            val variable = service.featureToVariable(feature)

            variable.shouldNotBeNull()
            variable.sort shouldBe Z3Sort.REAL
        }
    }

    describe("extractConstraintExpression - legacy") {

        it("should extract expression from element with expression property") {
            val (engine, service) = legacySetup()
            val invariant = engine.createElement("Invariant")
            // Use MDMObject.setProperty() directly — "expression" is not a metamodel attribute
            invariant.setProperty("expression", "x > 0")

            val expr = service.extractConstraintExpression(invariant)

            expr shouldBe "x > 0"
        }

        it("should return null for element without expression") {
            val (engine, service) = legacySetup()
            val invariant = engine.createElement("Invariant")

            val expr = service.extractConstraintExpression(invariant)

            expr.shouldBeNull()
        }
    }

    describe("solveConstraints - legacy hand-built") {

        it("should return satisfiable with empty features") {
            val (_, service) = legacySetup()
            val result = service.solveConstraints(emptyList(), emptyList())

            result.satisfiable shouldBe true
            result.assignments shouldBe emptyMap()
        }

        it("should return satisfiable with features but no constraints") {
            val (engine, service) = legacySetup()
            val feature = engine.createElement("Feature")
            engine.setPropertyValue(feature, "declaredName", "x")

            val result = service.solveConstraints(listOf(feature), emptyList())

            result.satisfiable shouldBe true
            result.assignments shouldBe emptyMap()
        }

        it("should solve constraints with named features and invariants") {
            val (engine, service) = legacySetup()
            val feature = engine.createElement("Feature")
            engine.setPropertyValue(feature, "declaredName", "x")

            val intType = engine.createElement("DataType")
            engine.setPropertyValue(intType, "declaredName", "Integer")
            feature.setProperty("type", listOf(intType))

            val invariant = engine.createElement("Invariant")
            // Use MDMObject.setProperty() for non-metamodel "expression" property
            invariant.setProperty("expression", "x = 42")

            val result = service.solveConstraints(listOf(feature), listOf(invariant))

            result.satisfiable shouldBe true
            result.assignments["x"] shouldBe 42L
        }
    }

    describe("tradeStudy - legacy hand-built") {

        it("should return error when no design variables") {
            val (_, service) = legacySetup()
            val result = service.tradeStudy(emptyList(), emptyList(), "x")

            result.satisfiable shouldBe false
            result.errorMessage shouldBe "No design variables declared"
        }

        it("should optimize with bounded design variables") {
            val (engine, service) = legacySetup()
            val feature = engine.createElement("Feature")
            engine.setPropertyValue(feature, "declaredName", "x")

            val intType = engine.createElement("DataType")
            engine.setPropertyValue(intType, "declaredName", "Integer")
            feature.setProperty("type", listOf(intType))

            val constraint = engine.createElement("Invariant")
            constraint.setProperty("expression", "x >= 1")

            val constraint2 = engine.createElement("Invariant")
            constraint2.setProperty("expression", "x <= 100")

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

    // === Parsed KerML integration tests ===

    describe("DEBUG: property storage diagnostics") {

        it("should verify elements are registered after parsing") {
            val model = freshModel()
            val debugFile = java.io.File("/tmp/parametric_debug.txt")
            val sb = StringBuilder()

            val countBefore = model.engine.getAllElements().size
            sb.appendLine("Elements BEFORE parse: $countBefore")

            val result = model.parseString("package T { feature x : ScalarValues::Integer; inv { x == 42 } }")
            sb.appendLine("parseString returned: ${result?.javaClass?.simpleName ?: "null"}")

            val parseResult = model.getLastParseResult()
            sb.appendLine("Parse success: ${parseResult?.success}")
            sb.appendLine("Parse errors: ${parseResult?.errors}")
            sb.appendLine("Parse rootNamespace: ${parseResult?.rootNamespace?.javaClass?.simpleName}")

            val countAfter = model.engine.getAllElements().size
            sb.appendLine("Elements AFTER parse: $countAfter")
            sb.appendLine("New elements: ${countAfter - countBefore}")

            // List all non-library elements
            val mountableEngine = model.engine as org.openmbee.mdm.framework.runtime.MountableEngine
            val localElements = mountableEngine.getLocalElements()
            sb.appendLine("\n=== LOCAL ELEMENTS (${localElements.size}) ===")
            localElements.forEach { elem ->
                sb.appendLine("  ${elem.className} id=${elem.id} props=${elem.getAllProperties().keys}")
            }

            // Try getElementsByClass
            val packages = model.engine.getElementsByClass("Package")
            val invariants = model.engine.getElementsByClass("Invariant")
            val features = model.engine.getElementsByClass("Feature")
            sb.appendLine("\n=== getElementsByClass ===")
            sb.appendLine("Package count: ${packages.size}")
            sb.appendLine("Invariant count: ${invariants.size}")
            sb.appendLine("Feature count: ${features.size}")

            // Check user packages (not library)
            val userPackages = packages.filter { it.getProperty("declaredName") == "T" }
            sb.appendLine("User Package 'T': ${userPackages.size}")

            // Check features named 'x'
            val userFeatures = features.filter { model.engine.getPropertyValue(it, "declaredName") as? String == "x" }
            sb.appendLine("Features named 'x': ${userFeatures.size}")

            // Check the model root's owned members
            val root = model.root
            sb.appendLine("\n=== MODEL ROOT ===")
            sb.appendLine("Root class: ${(root as org.openmbee.mdm.framework.runtime.MDMObject).className}")
            sb.appendLine("Root id: ${(root as org.openmbee.mdm.framework.runtime.MDMObject).id}")
            sb.appendLine("Root props: ${(root as org.openmbee.mdm.framework.runtime.MDMObject).getAllProperties().keys}")

            debugFile.writeText(sb.toString())

            countAfter shouldBeGreaterThan countBefore
        }
    }

    describe("expressionTreeToOcl") {

        it("should convert LiteralInteger from parsed invariant body") {
            val model = freshModel()
            model.parseString("package T { inv { 42 } }")

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())

            // Find the LiteralInteger with value 42
            val literal = model.engine.getAllElements()
                .filter { it.className == "LiteralInteger" }
                .firstOrNull { model.engine.getPropertyValue(it, "value") == 42L }

            literal.shouldNotBeNull()
            val ocl = service.expressionTreeToOcl(literal)
            ocl shouldBe "42"
        }

        it("should convert OperatorExpression from parsed invariant") {
            val model = freshModel()
            model.parseString("package T { feature x : ScalarValues::Integer; inv { x > 0 } }")

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())

            // Find the invariant and extract its expression
            val invariants = model.engine.getAllElements()
                .filter { model.engine.isInstanceOf(it, "Invariant") }
            invariants.shouldNotBeNull()

            val expr = service.extractConstraintExpression(invariants.first())
            expr.shouldNotBeNull()
            expr shouldContain ">"
        }
    }

    describe("solveConstraints with parsed KerML") {

        it("should solve simple equality constraint") {
            val model = freshModel()
            model.parseString("package T { feature x : ScalarValues::Integer; inv { x == 42 } }")

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())

            val features = model.engine.getAllElements()
                .filter { it.className == "Feature" }
                .filter { model.engine.getPropertyValue(it, "declaredName") as? String == "x" }
            val invariants = model.engine.getAllElements()
                .filter { model.engine.isInstanceOf(it, "Invariant") }

            val result = service.solveConstraints(features, invariants)

            result.satisfiable shouldBe true
            result.assignments["x"] shouldBe 42L
        }

        it("should solve bounded constraints") {
            val model = freshModel()
            model.parseString("""
                package T {
                    feature x : ScalarValues::Integer;
                    inv { x >= 10 }
                    inv { x <= 20 }
                }
            """.trimIndent())

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())

            val features = model.engine.getAllElements()
                .filter { it.className == "Feature" }
                .filter { model.engine.getPropertyValue(it, "declaredName") as? String == "x" }
            val invariants = model.engine.getAllElements()
                .filter { model.engine.isInstanceOf(it, "Invariant") }

            val result = service.solveConstraints(features, invariants)

            result.satisfiable shouldBe true
            val xVal = result.assignments["x"] as Long
            xVal.shouldBeGreaterThanOrEqual(10L)
            xVal.shouldBeLessThanOrEqual(20L)
        }

        it("should solve multi-variable constraints") {
            val model = freshModel()
            model.parseString("""
                package T {
                    feature x : ScalarValues::Integer;
                    feature y : ScalarValues::Integer;
                    inv { x + y == 10 }
                }
            """.trimIndent())

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())

            val features = model.engine.getAllElements()
                .filter { it.className == "Feature" }
                .filter {
                    val name = model.engine.getPropertyValue(it, "declaredName") as? String
                    name == "x" || name == "y"
                }
            val invariants = model.engine.getAllElements()
                .filter { model.engine.isInstanceOf(it, "Invariant") }

            val result = service.solveConstraints(features, invariants)

            result.satisfiable shouldBe true
            val xVal = result.assignments["x"] as Long
            val yVal = result.assignments["y"] as Long
            (xVal + yVal) shouldBe 10L
        }

        it("should detect unsatisfiable constraints") {
            val model = freshModel()
            model.parseString("""
                package T {
                    feature x : ScalarValues::Integer;
                    inv { x > 10 }
                    inv { x < 5 }
                }
            """.trimIndent())

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())

            val features = model.engine.getAllElements()
                .filter { it.className == "Feature" }
                .filter { model.engine.getPropertyValue(it, "declaredName") as? String == "x" }
            val invariants = model.engine.getAllElements()
                .filter { model.engine.isInstanceOf(it, "Invariant") }

            val result = service.solveConstraints(features, invariants)

            result.satisfiable shouldBe false
        }
    }

    describe("tradeStudy with parsed KerML") {

        it("should minimize objective") {
            val model = freshModel()
            model.parseString("""
                package T {
                    feature x : ScalarValues::Integer;
                    inv { x >= 1 }
                    inv { x <= 100 }
                }
            """.trimIndent())

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())

            val features = model.engine.getAllElements()
                .filter { it.className == "Feature" }
                .filter { model.engine.getPropertyValue(it, "declaredName") as? String == "x" }
            val invariants = model.engine.getAllElements()
                .filter { model.engine.isInstanceOf(it, "Invariant") }

            val result = service.tradeStudy(features, invariants, "x", minimize = true)

            result.satisfiable shouldBe true
            result.objectiveValue shouldBe 1L
        }

        it("should maximize objective") {
            val model = freshModel()
            model.parseString("""
                package T {
                    feature x : ScalarValues::Integer;
                    inv { x >= 1 }
                    inv { x <= 100 }
                }
            """.trimIndent())

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())

            val features = model.engine.getAllElements()
                .filter { it.className == "Feature" }
                .filter { model.engine.getPropertyValue(it, "declaredName") as? String == "x" }
            val invariants = model.engine.getAllElements()
                .filter { model.engine.isInstanceOf(it, "Invariant") }

            val result = service.tradeStudy(features, invariants, "x", minimize = false)

            result.satisfiable shouldBe true
            result.objectiveValue shouldBe 100L
        }
    }

    describe("checkRequirementConsistency with parsed KerML") {

        it("should detect consistent requirements") {
            val model = freshModel()
            model.parseString("""
                package T {
                    feature x : ScalarValues::Integer;
                    inv { x > 0 }
                    inv { x < 100 }
                }
            """.trimIndent())

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())

            val invariants = model.engine.getAllElements()
                .filter { model.engine.isInstanceOf(it, "Invariant") }

            val result = service.checkRequirementConsistency(invariants)

            result.consistent shouldBe true
        }

        it("should detect conflicting requirements") {
            val model = freshModel()
            model.parseString("""
                package T {
                    feature x : ScalarValues::Integer;
                    inv { x > 10 }
                    inv { x < 5 }
                }
            """.trimIndent())

            val service = ParametricAnalysisService(model.engine, ConstraintSolverService())

            val invariants = model.engine.getAllElements()
                .filter { model.engine.isInstanceOf(it, "Invariant") }

            val result = service.checkRequirementConsistency(invariants)

            result.consistent shouldBe false
        }
    }
})
