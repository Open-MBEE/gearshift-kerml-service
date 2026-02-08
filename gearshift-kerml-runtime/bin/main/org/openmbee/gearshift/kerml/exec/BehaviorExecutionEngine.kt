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

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.gearshift.kerml.eval.KerMLExpressionEvaluator
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MDMObject

private val logger = KotlinLogging.logger {}

/**
 * Lightweight token-passing execution engine for KerML Behaviors.
 *
 * Executes Behaviors composed of Steps connected by Successions (control flow)
 * and Flows (data transfer). Inspired by fUML but dramatically simplified.
 *
 * Execution loop:
 * 1. Build execution graph from Behavior's Steps and Successions
 * 2. Place initial control tokens on Steps with no predecessors
 * 3. Find READY steps (all input tokens available)
 * 4. Execute step: if typed by Function/Expression → use expression evaluator;
 *    if typed by Behavior → recurse
 * 5. Evaluate guard conditions on outgoing Successions
 * 6. Propagate tokens along satisfied Successions, transfer data via Flows
 * 7. Repeat until no READY steps or maxSteps exceeded
 *
 * @param engine The MDMEngine for model access
 * @param expressionEvaluator The expression evaluator for Step bodies
 * @param maxSteps Maximum execution steps before termination (infinite loop protection)
 */
class BehaviorExecutionEngine(
    private val engine: MDMEngine,
    private val expressionEvaluator: KerMLExpressionEvaluator,
    private val maxSteps: Int = 1000
) {
    /**
     * Execute a Behavior with the given input parameter values.
     *
     * @param behavior The Behavior MDMObject to execute
     * @param inputs Input parameter values (parameter name → value)
     * @return The execution result containing outputs, trace, and status
     */
    fun execute(behavior: MDMObject, inputs: Map<String, Any?> = emptyMap()): ExecutionResult {
        logger.info { "Executing behavior: ${behavior.className} (id=${behavior.id})" }

        return try {
            val context = ExecutionContext()
            inputs.forEach { (name, value) -> context.bind(name, value) }

            val graph = buildExecutionGraph(behavior)
            executeGraph(graph, context, behavior)
        } catch (e: Exception) {
            logger.error(e) { "Error executing behavior ${behavior.className}" }
            ExecutionResult(
                status = ExecutionStatus.ERROR,
                errorMessage = e.message
            )
        }
    }

    /**
     * Build an execution graph from a Behavior's Steps, Successions, and Flows.
     */
    internal fun buildExecutionGraph(behavior: MDMObject): ExecutionGraph {
        // Get Steps
        val steps = getSteps(behavior)
        val stepExecutions = steps.associate { step ->
            step.id!! to StepExecution(step)
        }

        // Get Successions (Features that are Successions, connecting Steps)
        val successions = getSuccessions(behavior, stepExecutions)

        // Get Flows (Features that are Flows, carrying data between Steps)
        val flows = getFlows(behavior, stepExecutions)

        return ExecutionGraph(stepExecutions, successions, flows)
    }

    /**
     * Execute an execution graph in a context.
     */
    private fun executeGraph(
        graph: ExecutionGraph,
        context: ExecutionContext,
        behavior: MDMObject
    ): ExecutionResult {
        val trace = mutableListOf<TraceEntry>()
        var stepCount = 0

        // Place initial control tokens on steps with no predecessors
        val initialSteps = graph.findInitialSteps()
        for (step in initialSteps) {
            step.inputTokens.add(ControlToken())
            step.state = StepState.READY
        }

        // Main execution loop
        while (true) {
            // Find all READY steps
            val readySteps = graph.steps.values.filter { it.state == StepState.READY }

            if (readySteps.isEmpty()) {
                // No more steps to execute — done
                break
            }

            if (stepCount >= maxSteps) {
                logger.warn { "Max steps ($maxSteps) exceeded for behavior ${behavior.className}" }
                return ExecutionResult(
                    status = ExecutionStatus.MAX_STEPS_EXCEEDED,
                    outputs = collectOutputs(graph, behavior, context),
                    trace = trace,
                    stepsExecuted = stepCount
                )
            }

            // Execute each ready step
            for (stepExec in readySteps) {
                stepExec.state = StepState.EXECUTING

                // Transfer incoming data from Flows
                transferInputData(graph, stepExec, context)

                // Execute the step
                val stepInputs = stepExec.inputTokens
                    .filterIsInstance<ObjectToken>()
                    .associate { "token_${it.id}" to it.value }

                executeStep(stepExec, context)

                stepExec.state = StepState.COMPLETED
                stepCount++

                // Record trace entry
                trace.add(
                    TraceEntry(
                        stepId = stepExec.step.id!!,
                        stepClassName = stepExec.step.className,
                        sequenceNumber = stepCount,
                        inputs = stepInputs,
                        outputs = stepExec.outputValues.toMap()
                    )
                )

                // Propagate tokens along outgoing Successions
                propagateTokens(graph, stepExec, context)

                // Transfer output data via Flows
                transferOutputData(graph, stepExec, context)
            }
        }

        return ExecutionResult(
            status = ExecutionStatus.COMPLETED,
            outputs = collectOutputs(graph, behavior, context),
            trace = trace,
            stepsExecuted = stepCount
        )
    }

    /**
     * Execute a single Step.
     */
    private fun executeStep(stepExec: StepExecution, context: ExecutionContext) {
        val step = stepExec.step
        logger.debug { "Executing step: ${step.className} (id=${step.id})" }

        when {
            // If the step is an Expression, evaluate it
            engine.isInstanceOf(step, "Expression") -> {
                val target = engine.createElement("Element")  // Dummy target for now
                val results = expressionEvaluator.evaluate(step, target)
                if (results.isNotEmpty()) {
                    stepExec.outputValues["result"] = if (results.size == 1) results.first() else results
                    stepExec.outputTokens.add(ObjectToken(value = results))
                }
                stepExec.outputTokens.add(ControlToken())
            }

            // If the step is typed by a Behavior, recursively execute it
            engine.isInstanceOf(step, "Step") -> {
                val behaviors = getStepBehaviors(step)
                if (behaviors.isNotEmpty()) {
                    val childBehavior = behaviors.first()
                    val childContext = context.createChild()

                    // Bind input parameters from context
                    val params = getParameters(step)
                    for (param in params) {
                        val paramName = engine.getPropertyValue(param, "declaredName") as? String
                            ?: engine.getPropertyValue(param, "name") as? String
                            ?: continue
                        val value = context.resolve(paramName)
                        if (value != null) {
                            childContext.bind(paramName, value)
                        }
                    }

                    val result = execute(childBehavior, childContext.getLocalBindings())
                    stepExec.outputValues.putAll(result.outputs)

                    // Produce output tokens
                    for ((name, value) in result.outputs) {
                        stepExec.outputTokens.add(ObjectToken(value = value))
                        context.bind(name, value)
                    }
                }
                stepExec.outputTokens.add(ControlToken())
            }

            else -> {
                // No-op step; just pass through control
                stepExec.outputTokens.add(ControlToken())
            }
        }
    }

    /**
     * Propagate tokens along outgoing Successions from a completed Step.
     */
    private fun propagateTokens(
        graph: ExecutionGraph,
        completedStep: StepExecution,
        context: ExecutionContext
    ) {
        val outgoing = graph.getOutgoingSuccessions(completedStep.step.id!!)

        for (succession in outgoing) {
            // Evaluate guard condition if present
            if (succession.guardExpression != null) {
                val guardResult = evaluateGuard(succession.guardExpression, context)
                if (!guardResult) {
                    logger.debug { "Guard condition failed for succession to ${succession.targetStepId}" }
                    continue
                }
            }

            // Propagate control token to target step
            val targetStep = graph.steps[succession.targetStepId]
            if (targetStep != null) {
                targetStep.inputTokens.add(ControlToken())

                // Check if all incoming successions have been satisfied
                val incomingSuccessions = graph.getIncomingSuccessions(succession.targetStepId)
                val allSatisfied = incomingSuccessions.all { incoming ->
                    val sourceStep = graph.steps[incoming.sourceStepId]
                    sourceStep?.state == StepState.COMPLETED
                }

                if (allSatisfied) {
                    targetStep.state = StepState.READY
                }
            }
        }
    }

    /**
     * Transfer input data via incoming Flows to a Step.
     */
    private fun transferInputData(
        graph: ExecutionGraph,
        stepExec: StepExecution,
        context: ExecutionContext
    ) {
        val incomingFlows = graph.getIncomingFlows(stepExec.step.id!!)
        for (flow in incomingFlows) {
            val sourceStep = graph.steps[flow.sourceStepId]
            if (sourceStep != null && sourceStep.state == StepState.COMPLETED) {
                // Transfer data from source's output to target's context
                val outputName = flow.sourceOutputFeature ?: "result"
                val value = sourceStep.outputValues[outputName]
                if (value != null) {
                    val inputName = flow.targetInputFeature ?: outputName
                    context.bind(inputName, value)
                    stepExec.inputTokens.add(ObjectToken(value = value))
                }
            }
        }
    }

    /**
     * Transfer output data via outgoing Flows from a completed Step.
     */
    private fun transferOutputData(
        graph: ExecutionGraph,
        completedStep: StepExecution,
        context: ExecutionContext
    ) {
        val outgoingFlows = graph.getOutgoingFlows(completedStep.step.id!!)
        for (flow in outgoingFlows) {
            val outputName = flow.sourceOutputFeature ?: "result"
            val value = completedStep.outputValues[outputName]
            if (value != null) {
                val inputName = flow.targetInputFeature ?: outputName
                context.bind(inputName, value)
            }
        }
    }

    /**
     * Evaluate a guard condition expression.
     */
    private fun evaluateGuard(guardExpression: MDMObject, context: ExecutionContext): Boolean {
        return try {
            val target = engine.createElement("Element")
            val results = expressionEvaluator.evaluate(guardExpression, target)
            if (results.size == 1) {
                val result = results.first()
                if (engine.isInstanceOf(result, "LiteralBoolean")) {
                    engine.getPropertyValue(result, "value") as? Boolean ?: false
                } else {
                    false
                }
            } else {
                false
            }
        } catch (e: Exception) {
            logger.warn(e) { "Error evaluating guard condition" }
            false
        }
    }

    /**
     * Collect output parameter values from the execution.
     */
    private fun collectOutputs(
        graph: ExecutionGraph,
        behavior: MDMObject,
        context: ExecutionContext
    ): Map<String, Any?> {
        val outputs = mutableMapOf<String, Any?>()

        // Get output parameters from the behavior
        val params = getParameters(behavior)
        for (param in params) {
            val direction = engine.getPropertyValue(param, "direction") as? String
                ?: getDirection(param)
            if (direction == "out" || direction == "inout" || direction == "return") {
                val paramName = engine.getPropertyValue(param, "declaredName") as? String
                    ?: engine.getPropertyValue(param, "name") as? String
                    ?: continue
                val value = context.resolve(paramName)
                if (value != null) {
                    outputs[paramName] = value
                }
            }
        }

        // Also include outputs from terminal steps
        val terminalSteps = graph.steps.values.filter { exec ->
            exec.state == StepState.COMPLETED &&
                graph.getOutgoingSuccessions(exec.step.id!!).isEmpty()
        }
        for (stepExec in terminalSteps) {
            outputs.putAll(stepExec.outputValues)
        }

        return outputs
    }

    // === Model Navigation Helpers ===

    private fun getSteps(behavior: MDMObject): List<MDMObject> {
        val steps = engine.getPropertyValue(behavior, "step")
        return when (steps) {
            is List<*> -> steps.filterIsInstance<MDMObject>()
            is MDMObject -> listOf(steps)
            else -> {
                // Fall back to feature->selectByKind(Step)
                val features = engine.getPropertyValue(behavior, "feature")
                when (features) {
                    is List<*> -> features.filterIsInstance<MDMObject>()
                        .filter { engine.isInstanceOf(it, "Step") }
                    is MDMObject -> if (engine.isInstanceOf(features, "Step")) listOf(features) else emptyList()
                    else -> emptyList()
                }
            }
        }
    }

    private fun getSuccessions(
        behavior: MDMObject,
        stepMap: Map<String, StepExecution>
    ): List<SuccessionEdge> {
        val features = engine.getPropertyValue(behavior, "ownedFeature")
        val featureList = when (features) {
            is List<*> -> features.filterIsInstance<MDMObject>()
            is MDMObject -> listOf(features)
            else -> emptyList()
        }

        return featureList
            .filter { engine.isInstanceOf(it, "Succession") }
            .mapNotNull { succession ->
                val connectorEnds = getConnectorEnds(succession)
                if (connectorEnds.size >= 2) {
                    val sourceId = connectorEnds[0].id
                    val targetId = connectorEnds[1].id
                    if (sourceId != null && targetId != null &&
                        sourceId in stepMap && targetId in stepMap
                    ) {
                        val guard = getGuardExpression(succession)
                        SuccessionEdge(succession, sourceId, targetId, guard)
                    } else null
                } else null
            }
    }

    private fun getFlows(
        behavior: MDMObject,
        stepMap: Map<String, StepExecution>
    ): List<FlowEdge> {
        val features = engine.getPropertyValue(behavior, "ownedFeature")
        val featureList = when (features) {
            is List<*> -> features.filterIsInstance<MDMObject>()
            is MDMObject -> listOf(features)
            else -> emptyList()
        }

        return featureList
            .filter { engine.isInstanceOf(it, "Flow") }
            .mapNotNull { flow ->
                val sourceOutput = engine.getPropertyValue(flow, "sourceOutputFeature") as? MDMObject
                val targetInput = engine.getPropertyValue(flow, "targetInputFeature") as? MDMObject
                val connectorEnds = getConnectorEnds(flow)

                if (connectorEnds.size >= 2) {
                    val sourceId = connectorEnds[0].id
                    val targetId = connectorEnds[1].id
                    if (sourceId != null && targetId != null &&
                        sourceId in stepMap && targetId in stepMap
                    ) {
                        val sourceFeatureName = sourceOutput?.let {
                            engine.getPropertyValue(it, "declaredName") as? String
                                ?: engine.getPropertyValue(it, "name") as? String
                        }
                        val targetFeatureName = targetInput?.let {
                            engine.getPropertyValue(it, "declaredName") as? String
                                ?: engine.getPropertyValue(it, "name") as? String
                        }
                        FlowEdge(flow, sourceId, targetId, sourceFeatureName, targetFeatureName)
                    } else null
                } else null
            }
    }

    private fun getConnectorEnds(connector: MDMObject): List<MDMObject> {
        val ends = engine.getPropertyValue(connector, "connectorEnd")
            ?: engine.getPropertyValue(connector, "relatedFeature")
        return when (ends) {
            is List<*> -> ends.filterIsInstance<MDMObject>()
            is MDMObject -> listOf(ends)
            else -> emptyList()
        }
    }

    private fun getGuardExpression(succession: MDMObject): MDMObject? {
        val guard = engine.getPropertyValue(succession, "guardExpression")
        return guard as? MDMObject
    }

    private fun getStepBehaviors(step: MDMObject): List<MDMObject> {
        val behaviors = engine.getPropertyValue(step, "behavior")
        return when (behaviors) {
            is List<*> -> behaviors.filterIsInstance<MDMObject>()
            is MDMObject -> listOf(behaviors)
            else -> emptyList()
        }
    }

    private fun getParameters(element: MDMObject): List<MDMObject> {
        val params = engine.getPropertyValue(element, "parameter")
        return when (params) {
            is List<*> -> params.filterIsInstance<MDMObject>()
            is MDMObject -> listOf(params)
            else -> emptyList()
        }
    }

    private fun getDirection(param: MDMObject): String {
        // Check the direction via the directionOf operation or stored property
        return engine.getPropertyValue(param, "direction") as? String ?: "in"
    }
}
