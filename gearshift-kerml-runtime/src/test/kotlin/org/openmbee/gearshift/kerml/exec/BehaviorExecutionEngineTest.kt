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

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.openmbee.gearshift.generated.interfaces.Behavior
import org.openmbee.gearshift.generated.interfaces.Step
import org.openmbee.gearshift.kerml.KerMLTestSpec
import org.openmbee.gearshift.kerml.eval.KerMLExpressionEvaluator
import org.openmbee.gearshift.kerml.eval.KernelFunctionLibrary
import org.openmbee.mdm.framework.runtime.MDMObject

class BehaviorExecutionEngineTest : KerMLTestSpec({

    val model = freshModel()

    fun createExecutionEngine(maxSteps: Int = 1000): BehaviorExecutionEngine {
        val library = KernelFunctionLibrary(model.engine)
        val evaluator = KerMLExpressionEvaluator(model.engine, library)
        return BehaviorExecutionEngine(model.engine, evaluator, maxSteps)
    }

    describe("execution graph construction") {

        it("should build a graph with steps from a parsed behavior") {
            model.reset()
            model.parseString(
                """
                package T {
                    behavior ComputeFlow {
                        step s1;
                        step s2;
                    }
                }
            """.trimIndent()
            )

            val behavior = model.findByName<Behavior>("ComputeFlow")
            behavior.shouldNotBeNull()

            val executionEngine = createExecutionEngine()
            val graph = executionEngine.buildExecutionGraph(behavior as MDMObject)

            graph.steps.size shouldBe 2
        }

        it("should identify all steps as initial when no successions") {
            model.reset()
            model.parseString(
                """
                package T {
                    behavior Parallel {
                        step s1;
                        step s2;
                    }
                }
            """.trimIndent()
            )

            val behavior = model.findByName<Behavior>("Parallel")
            behavior.shouldNotBeNull()

            val executionEngine = createExecutionEngine()
            val graph = executionEngine.buildExecutionGraph(behavior as MDMObject)

            graph.findInitialSteps() shouldHaveSize 2
        }

        it("should build a graph with successions from parsed behavior") {
            model.reset()
            model.parseString(
                """
                package T {
                    behavior Sequential {
                        step s1;
                        step s2;
                        succession first s1 then s2;
                    }
                }
            """.trimIndent()
            )

            val behavior = model.findByName<Behavior>("Sequential")
            behavior.shouldNotBeNull()

            val executionEngine = createExecutionEngine()
            val graph = executionEngine.buildExecutionGraph(behavior as MDMObject)

            graph.steps.size shouldBe 2
            graph.successions.size shouldBe 1
        }
    }

    describe("basic execution") {

        it("should execute a behavior with no steps") {
            model.reset()
            model.parseString(
                """
                package T {
                    behavior EmptyBehavior { }
                }
            """.trimIndent()
            )

            val behavior = model.findByName<Behavior>("EmptyBehavior")
            behavior.shouldNotBeNull()

            val executionEngine = createExecutionEngine()
            val result = executionEngine.execute(behavior as MDMObject)

            result.status shouldBe ExecutionStatus.COMPLETED
            result.stepsExecuted shouldBe 0
        }

        it("should execute a behavior with parallel steps") {
            model.reset()
            model.parseString(
                """
                package T {
                    behavior TwoSteps {
                        step s1;
                        step s2;
                    }
                }
            """.trimIndent()
            )

            val behavior = model.findByName<Behavior>("TwoSteps")
            behavior.shouldNotBeNull()

            val executionEngine = createExecutionEngine()
            val result = executionEngine.execute(behavior as MDMObject)

            result.status shouldBe ExecutionStatus.COMPLETED
            result.stepsExecuted shouldBe 2
        }

        it("should execute a behavior with sequenced steps") {
            model.reset()
            model.parseString(
                """
                package T {
                    behavior Pipeline {
                        step s1;
                        step s2;
                        succession first s1 then s2;
                    }
                }
            """.trimIndent()
            )

            val behavior = model.findByName<Behavior>("Pipeline")
            behavior.shouldNotBeNull()

            val executionEngine = createExecutionEngine()
            val result = executionEngine.execute(behavior as MDMObject)

            result.status shouldBe ExecutionStatus.COMPLETED
            result.stepsExecuted shouldBe 2
            result.trace shouldHaveSize 2
        }

        it("should execute a three-step pipeline") {
            model.reset()
            model.parseString(
                """
                package T {
                    behavior ThreeStage {
                        step first_step;
                        step middle_step;
                        step last_step;
                        succession first first_step then middle_step;
                        succession first middle_step then last_step;
                    }
                }
            """.trimIndent()
            )

            val behavior = model.findByName<Behavior>("ThreeStage")
            behavior.shouldNotBeNull()

            val executionEngine = createExecutionEngine()
            val result = executionEngine.execute(behavior as MDMObject)

            result.status shouldBe ExecutionStatus.COMPLETED
            result.stepsExecuted shouldBe 3
            result.trace shouldHaveSize 3
        }
    }

    describe("max steps termination") {

        it("should terminate with MAX_STEPS_EXCEEDED status") {
            model.reset()
            model.parseString(
                """
                package T {
                    behavior BoundedBehavior {
                        step s1;
                    }
                }
            """.trimIndent()
            )

            val behavior = model.findByName<Behavior>("BoundedBehavior")
            behavior.shouldNotBeNull()

            val executionEngine = createExecutionEngine(maxSteps = 0)
            val result = executionEngine.execute(behavior as MDMObject)

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

        it("should track step states on parsed step") {
            model.reset()
            model.parseString(
                """
                package T {
                    behavior B {
                        step s;
                    }
                }
            """.trimIndent()
            )

            val step = model.allOfType<Step>().firstOrNull()
            step.shouldNotBeNull()

            val exec = StepExecution(step as MDMObject)
            exec.state shouldBe StepState.WAITING
            exec.state = StepState.READY
            exec.state shouldBe StepState.READY
        }
    }
})