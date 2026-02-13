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

import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.openmbee.gearshift.generated.interfaces.Behavior
import org.openmbee.gearshift.kerml.KerMLTestSpec
import org.openmbee.gearshift.kerml.eval.KerMLExpressionEvaluator
import org.openmbee.gearshift.kerml.eval.KernelFunctionLibrary
import org.openmbee.mdm.framework.runtime.MDMObject

class SimulationSessionTest : KerMLTestSpec({

    val model = freshModel()

    fun createEngine(listener: ExecutionListener): BehaviorExecutionEngine {
        val library = KernelFunctionLibrary(model.engine)
        val evaluator = KerMLExpressionEvaluator(model.engine, library)
        return BehaviorExecutionEngine(model.engine, evaluator, listener = listener)
    }

    /**
     * Collect all events from the session's replay buffer.
     * Since the replay buffer stores all events, we can subscribe after execution
     * and collect everything that was emitted.
     */
    suspend fun collectReplayedEvents(session: SimulationSession): List<ExecutionEvent> = coroutineScope {
        val events = mutableListOf<ExecutionEvent>()
        val job = launch {
            session.events.collect { event ->
                events.add(event)
            }
        }
        // Give collector time to receive replayed events
        delay(100)
        job.cancel()
        events
    }

    describe("event emission") {

        it("should emit GraphInitialized first and ExecutionCompleted last for a sequential pipeline") {
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

            val behavior = model.findByName<Behavior>("Pipeline") as MDMObject
            val session = SimulationSession("test-1")
            val engine = createEngine(session)

            // Execute (events stored in replay buffer)
            engine.execute(behavior)

            // Collect from replay buffer
            val events = collectReplayedEvents(session)

            events shouldHaveAtLeastSize 2
            events.first().shouldBeInstanceOf<ExecutionEvent.GraphInitialized>()
            events.last().shouldBeInstanceOf<ExecutionEvent.ExecutionCompleted>()

            // Verify sequence numbers are monotonically increasing
            events.zipWithNext { a, b -> a.sequenceNumber < b.sequenceNumber }.all { it } shouldBe true
        }

        it("should emit correct StepStateChanged count for two-step pipeline") {
            model.reset()
            model.parseString(
                """
                package T {
                    behavior TwoStep {
                        step s1;
                        step s2;
                        succession first s1 then s2;
                    }
                }
                """.trimIndent()
            )

            val behavior = model.findByName<Behavior>("TwoStep") as MDMObject
            val session = SimulationSession("test-2")
            val engine = createEngine(session)

            engine.execute(behavior)
            val events = collectReplayedEvents(session)

            val stateChanges = events.filterIsInstance<ExecutionEvent.StepStateChanged>()
            // Each step: WAITING->READY, READY->EXECUTING, EXECUTING->COMPLETED = 3 per step
            // s1: WAITING->READY (initial), READY->EXECUTING, EXECUTING->COMPLETED = 3
            // s2: WAITING->READY (propagation), READY->EXECUTING, EXECUTING->COMPLETED = 3
            stateChanges.size shouldBe 6
        }
    }

    describe("pause and resume") {

        it("should start in PAUSED state when startPaused is true") {
            val session = SimulationSession("test-paused", startPaused = true)
            session.state shouldBe SimulationState.PAUSED
        }

        it("should resume and complete execution after resume") {
            model.reset()
            model.parseString(
                """
                package T {
                    behavior Simple {
                        step s1;
                    }
                }
                """.trimIndent()
            )

            val behavior = model.findByName<Behavior>("Simple") as MDMObject
            val session = SimulationSession("test-resume", startPaused = true)
            val engine = createEngine(session)

            // Launch execution — it will suspend because session is paused
            val execJob = launch {
                engine.execute(behavior)
            }

            // Give execution time to start and hit the pause gate
            delay(200)
            session.state shouldBe SimulationState.PAUSED

            // Resume
            session.resume()
            execJob.join()

            // Collect events from replay buffer
            val events = collectReplayedEvents(session)

            events shouldHaveAtLeastSize 2
            events.last().shouldBeInstanceOf<ExecutionEvent.ExecutionCompleted>()
            session.state shouldBe SimulationState.FINISHED
        }
    }

    describe("step forward") {

        it("should emit exactly one event per step-forward call") {
            model.reset()
            model.parseString(
                """
                package T {
                    behavior StepBehavior {
                        step s1;
                        step s2;
                        succession first s1 then s2;
                    }
                }
                """.trimIndent()
            )

            val behavior = model.findByName<Behavior>("StepBehavior") as MDMObject
            val session = SimulationSession("test-step", startPaused = true)
            val engine = createEngine(session)

            // Launch execution — will pause immediately
            val execJob = launch {
                engine.execute(behavior)
            }

            delay(200)
            session.state shouldBe SimulationState.PAUSED

            // Step forward once — should emit exactly one event
            session.stepForward()
            delay(200)
            var events = collectReplayedEvents(session)
            events.size shouldBe 1

            // Step again
            session.stepForward()
            delay(200)
            events = collectReplayedEvents(session)
            events.size shouldBe 2

            // Resume to finish
            session.resume()
            execJob.join()

            events = collectReplayedEvents(session)
            events shouldHaveAtLeastSize 3
        }
    }

    describe("speed control") {

        it("should get and set speed") {
            val session = SimulationSession("test-speed", delayMs = 500)
            session.getSpeed() shouldBe 500

            session.setSpeed(100)
            session.getSpeed() shouldBe 100
        }

        it("should clamp negative speed to zero") {
            val session = SimulationSession("test-speed-clamp", delayMs = 100)
            session.setSpeed(-50)
            session.getSpeed() shouldBe 0
        }
    }
})
