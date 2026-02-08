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
package org.openmbee.gearshift.kerml.exec

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.openmbee.gearshift.kerml.KerMLMetamodelLoader
import org.openmbee.gearshift.kerml.eval.KerMLExpressionEvaluator
import org.openmbee.gearshift.kerml.eval.KernelFunctionLibrary
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MetamodelRegistry

class BehaviorExecutionEngineTest : DescribeSpec({

    lateinit var engine: MDMEngine
    lateinit var executionEngine: BehaviorExecutionEngine

    beforeEach {
        val registry = MetamodelRegistry()
        registry.ensureBaseClassRegistered()
        KerMLMetamodelLoader.initialize(registry)
        engine = MDMEngine(registry)
        val library = KernelFunctionLibrary(engine)
        val evaluator = KerMLExpressionEvaluator(engine, library)
        executionEngine = BehaviorExecutionEngine(engine, evaluator)
    }

    describe("execution graph construction") {

        it("should build a graph with steps from a behavior") {
            val behavior = engine.createElement("Behavior")

            // Create steps as owned features
            val step1 = engine.createElement("Step")
            val step2 = engine.createElement("Step")

            linkOwnedFeature(engine, behavior, step1)
            linkOwnedFeature(engine, behavior, step2)

            val graph = executionEngine.buildExecutionGraph(behavior)

            graph.steps.size shouldBe 2
        }

        it("should identify initial steps with no predecessors") {
            val behavior = engine.createElement("Behavior")

            val step1 = engine.createElement("Step")
            val step2 = engine.createElement("Step")

            linkOwnedFeature(engine, behavior, step1)
            linkOwnedFeature(engine, behavior, step2)

            val graph = executionEngine.buildExecutionGraph(behavior)

            // Without successions, all steps are initial
            graph.findInitialSteps() shouldHaveSize 2
        }
    }

    describe("basic execution") {

        it("should execute a behavior with no steps") {
            val behavior = engine.createElement("Behavior")

            val result = executionEngine.execute(behavior)

            result.status shouldBe ExecutionStatus.COMPLETED
            result.stepsExecuted shouldBe 0
        }

        it("should execute a behavior with a single expression step") {
            val behavior = engine.createElement("Behavior")

            // Create a LiteralInteger expression as a step
            val exprStep = engine.createElement("LiteralInteger")
            engine.setPropertyValue(exprStep, "value", 42L)

            linkOwnedFeature(engine, behavior, exprStep)

            val result = executionEngine.execute(behavior)

            result.status shouldBe ExecutionStatus.COMPLETED
            result.stepsExecuted shouldBe 1
            result.trace shouldHaveSize 1
            result.trace.first().stepClassName shouldBe "LiteralInteger"
        }
    }

    describe("max steps termination") {

        it("should terminate with MAX_STEPS_EXCEEDED status") {
            val registry = MetamodelRegistry()
            registry.ensureBaseClassRegistered()
            KerMLMetamodelLoader.initialize(registry)
            val limitedEngine = MDMEngine(registry)
            val library = KernelFunctionLibrary(limitedEngine)
            val evaluator = KerMLExpressionEvaluator(limitedEngine, library)
            val limitedExecEngine = BehaviorExecutionEngine(limitedEngine, evaluator, maxSteps = 0)

            val behavior = limitedEngine.createElement("Behavior")
            val step = limitedEngine.createElement("Step")
            linkOwnedFeature(limitedEngine, behavior, step)

            val result = limitedExecEngine.execute(behavior)

            result.status shouldBe ExecutionStatus.MAX_STEPS_EXCEEDED
        }
    }

    describe("execution context") {

        it("should create child contexts") {
            val parent = ExecutionContext()
            parent.bind("x", 10)

            val child = parent.createChild()
            child.bind("y", 20)

            child.resolve("x") shouldBe 10
            child.resolve("y") shouldBe 20
            parent.resolve("y") shouldBe null
        }

        it("should support variable shadowing") {
            val parent = ExecutionContext()
            parent.bind("x", 10)

            val child = parent.createChild()
            child.bind("x", 20)

            child.resolve("x") shouldBe 20
            parent.resolve("x") shouldBe 10
        }

        it("should check isDefined across scopes") {
            val parent = ExecutionContext()
            parent.bind("x", 10)

            val child = parent.createChild()

            child.isDefined("x") shouldBe true
            child.isDefined("y") shouldBe false
        }
    }

    describe("execution model data structures") {

        it("should create control tokens") {
            val token = ControlToken()
            token.id.isNotEmpty() shouldBe true
        }

        it("should create object tokens with values") {
            val token = ObjectToken(value = 42)
            token.value shouldBe 42
        }

        it("should track step states") {
            val step = engine.createElement("Step")
            val exec = StepExecution(step)
            exec.state shouldBe StepState.WAITING
            exec.state = StepState.READY
            exec.state shouldBe StepState.READY
        }
    }
})

/**
 * Helper to link an owned feature to a namespace in tests.
 */
private fun linkOwnedFeature(engine: MDMEngine, owner: org.openmbee.mdm.framework.runtime.MDMObject, feature: org.openmbee.mdm.framework.runtime.MDMObject) {
    val association = engine.schema.getAssociation("featuringTypeOwnedFeature")
    if (association != null) {
        engine.link(owner.id!!, feature.id!!, association.name)
    } else {
        val ownerAssoc = engine.schema.getAssociation("namespaceOwnedMember")
            ?: engine.schema.getAssociation("elementOwnedElement")
        if (ownerAssoc != null) {
            engine.link(owner.id!!, feature.id!!, ownerAssoc.name)
        }
    }
}
