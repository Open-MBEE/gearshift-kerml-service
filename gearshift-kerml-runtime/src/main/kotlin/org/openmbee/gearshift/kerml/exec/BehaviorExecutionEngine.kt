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
import org.openmbee.gearshift.generated.interfaces.*
import org.openmbee.gearshift.kerml.GearshiftSettings
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
 * @param listener Listener for streaming execution events (default: no-op)
 */
class BehaviorExecutionEngine(
    private val engine: MDMEngine,
    private val expressionEvaluator: KerMLExpressionEvaluator,
    private val maxSteps: Int = 1000,
    private val listener: ExecutionListener = NoOpExecutionListener
) {
    private var eventSequence = 0

    private fun nextSeq(): Int = ++eventSequence

    private fun MDMObject.stepName(): String? {
        val f = this as? Feature
        return f?.declaredName ?: f?.name
    }

    /**
     * Execute a Behavior with the given input parameter values.
     *
     * @param behavior The Behavior MDMObject to execute
     * @param inputs Input parameter values (parameter name → value)
     * @return The execution result containing outputs, trace, and status
     */
    suspend fun execute(behavior: MDMObject, inputs: Map<String, Any?> = emptyMap()): ExecutionResult {
        logger.info { "Executing behavior: ${behavior.className} (id=${behavior.id})" }

        return try {
            val context = ExecutionContext()
            inputs.forEach { (name, value) -> context.bind(name, value) }

            val graph = buildExecutionGraph(behavior)
            executeGraph(graph, context, behavior)
        } catch (e: Exception) {
            logger.error(e) { "Error executing behavior ${behavior.className}" }
            listener.onEvent(ExecutionEvent.ExecutionError(
                sequenceNumber = nextSeq(),
                message = e.message ?: "Unknown error"
            ))
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
        val typed = behavior as Behavior
        val steps = typed.step.map { it as MDMObject }
        val stepExecutions = steps.associate { step ->
            step.id!! to StepExecution(step)
        }

        val successions = getSuccessions(behavior, stepExecutions)
        val flows = getFlows(behavior, stepExecutions)

        return ExecutionGraph(stepExecutions, successions, flows)
    }

    /**
     * Execute an execution graph in a context.
     */
    private suspend fun executeGraph(
        graph: ExecutionGraph,
        context: ExecutionContext,
        behavior: MDMObject
    ): ExecutionResult {
        val trace = mutableListOf<TraceEntry>()
        var stepCount = 0

        // Emit graph structure
        listener.onEvent(ExecutionEvent.GraphInitialized(
            sequenceNumber = nextSeq(),
            steps = graph.steps.map { (id, exec) -> StepInfo(id, exec.step.stepName()) },
            successions = graph.successions.map { edge ->
                SuccessionInfo(edge.sourceStepId, edge.targetStepId, edge.guardExpression != null)
            },
            flows = graph.flows.map { edge ->
                FlowInfo(edge.sourceStepId, edge.targetStepId, edge.sourceOutputFeature, edge.targetInputFeature)
            }
        ))

        // Place initial control tokens on steps with no predecessors
        val initialSteps = graph.findInitialSteps()
        for (step in initialSteps) {
            step.inputTokens.add(ControlToken())
            val oldState = step.state
            step.state = StepState.READY

            listener.onEvent(ExecutionEvent.TokenPlaced(
                sequenceNumber = nextSeq(),
                stepId = step.step.id!!,
                stepName = step.step.stepName()
            ))
            listener.onEvent(ExecutionEvent.StepStateChanged(
                sequenceNumber = nextSeq(),
                stepId = step.step.id!!,
                stepName = step.step.stepName(),
                oldState = oldState,
                newState = StepState.READY
            ))
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
                val result = ExecutionResult(
                    status = ExecutionStatus.MAX_STEPS_EXCEEDED,
                    outputs = collectOutputs(graph, behavior, context),
                    trace = trace,
                    stepsExecuted = stepCount
                )
                listener.onEvent(ExecutionEvent.ExecutionCompleted(
                    sequenceNumber = nextSeq(),
                    status = result.status,
                    stepsExecuted = stepCount,
                    outputs = result.outputs
                ))
                return result
            }

            // Execute each ready step
            for (stepExec in readySteps) {
                stepExec.state = StepState.EXECUTING
                listener.onEvent(ExecutionEvent.StepStateChanged(
                    sequenceNumber = nextSeq(),
                    stepId = stepExec.step.id!!,
                    stepName = stepExec.step.stepName(),
                    oldState = StepState.READY,
                    newState = StepState.EXECUTING
                ))

                // Transfer incoming data from Flows
                transferInputData(graph, stepExec, context)

                // Execute the step
                val stepInputs = stepExec.inputTokens
                    .filterIsInstance<ObjectToken>()
                    .associate { "token_${it.id}" to it.value }

                executeStep(stepExec, context)

                stepExec.state = StepState.COMPLETED
                stepCount++

                listener.onEvent(ExecutionEvent.StepStateChanged(
                    sequenceNumber = nextSeq(),
                    stepId = stepExec.step.id!!,
                    stepName = stepExec.step.stepName(),
                    oldState = StepState.EXECUTING,
                    newState = StepState.COMPLETED
                ))

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

        val result = ExecutionResult(
            status = ExecutionStatus.COMPLETED,
            outputs = collectOutputs(graph, behavior, context),
            trace = trace,
            stepsExecuted = stepCount
        )
        listener.onEvent(ExecutionEvent.ExecutionCompleted(
            sequenceNumber = nextSeq(),
            status = result.status,
            stepsExecuted = stepCount,
            outputs = result.outputs
        ))
        return result
    }

    /**
     * Execute a single Step.
     */
    private suspend fun executeStep(stepExec: StepExecution, context: ExecutionContext) {
        val step = stepExec.step
        logger.debug { "Executing step: ${step.className} (id=${step.id})" }

        when {
            // If the step is an Expression, evaluate it
            step is Expression -> {
                val target = engine.createElement("Element")
                val results = expressionEvaluator.evaluate(step, target)
                if (results.isNotEmpty()) {
                    stepExec.outputValues["result"] = if (results.size == 1) results.first() else results
                    stepExec.outputTokens.add(ObjectToken(value = results))
                }
                stepExec.outputTokens.add(ControlToken())
            }

            // If the step is typed by a Behavior, recursively execute it
            step is Step -> {
                val typedStep = step as Step
                val behaviors = typedStep.behavior.map { it as MDMObject }
                if (behaviors.isNotEmpty()) {
                    val childBehavior = behaviors.first()
                    val childContext = context.createChild()

                    // Bind input parameters from context
                    for (param in typedStep.parameter) {
                        val typedParam = param as Feature
                        val paramName = typedParam.declaredName ?: typedParam.name ?: continue
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
    private suspend fun propagateTokens(
        graph: ExecutionGraph,
        completedStep: StepExecution,
        context: ExecutionContext
    ) {
        val outgoing = graph.getOutgoingSuccessions(completedStep.step.id!!)

        for (succession in outgoing) {
            // Evaluate guard condition if present
            if (succession.guardExpression != null) {
                val guardResult = evaluateGuard(succession.guardExpression, context)
                listener.onEvent(ExecutionEvent.GuardEvaluated(
                    sequenceNumber = nextSeq(),
                    sourceStepId = succession.sourceStepId,
                    targetStepId = succession.targetStepId,
                    passed = guardResult
                ))
                if (!guardResult) {
                    logger.debug { "Guard condition failed for succession to ${succession.targetStepId}" }
                    continue
                }
            }

            // Propagate control token to target step
            val targetStep = graph.steps[succession.targetStepId]
            if (targetStep != null) {
                targetStep.inputTokens.add(ControlToken())

                listener.onEvent(ExecutionEvent.TokenPropagated(
                    sequenceNumber = nextSeq(),
                    sourceStepId = completedStep.step.id!!,
                    targetStepId = succession.targetStepId
                ))

                // Check if all incoming successions have been satisfied
                val incomingSuccessions = graph.getIncomingSuccessions(succession.targetStepId)
                val allSatisfied = incomingSuccessions.all { incoming ->
                    val sourceStep = graph.steps[incoming.sourceStepId]
                    sourceStep?.state == StepState.COMPLETED
                }

                if (allSatisfied) {
                    targetStep.state = StepState.READY
                    listener.onEvent(ExecutionEvent.StepStateChanged(
                        sequenceNumber = nextSeq(),
                        stepId = succession.targetStepId,
                        stepName = targetStep.step.stepName(),
                        oldState = StepState.WAITING,
                        newState = StepState.READY
                    ))
                }
            }
        }
    }

    /**
     * Transfer input data via incoming Flows to a Step.
     */
    private suspend fun transferInputData(
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

                    listener.onEvent(ExecutionEvent.DataTransferred(
                        sequenceNumber = nextSeq(),
                        sourceStepId = flow.sourceStepId,
                        targetStepId = stepExec.step.id!!,
                        featureName = inputName,
                        value = value
                    ))
                }
            }
        }
    }

    /**
     * Transfer output data via outgoing Flows from a completed Step.
     */
    private suspend fun transferOutputData(
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

                listener.onEvent(ExecutionEvent.DataTransferred(
                    sequenceNumber = nextSeq(),
                    sourceStepId = completedStep.step.id!!,
                    targetStepId = flow.targetStepId,
                    featureName = inputName,
                    value = value
                ))
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
                if (result is LiteralBoolean) {
                    result.value
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
        for (param in (behavior as Behavior).parameter) {
            val typedParam = param as Feature
            val direction = typedParam.direction ?: "in"
            if (direction == "out" || direction == "inout" || direction == "return") {
                val paramName = typedParam.declaredName ?: typedParam.name ?: continue
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

    private fun getSuccessions(
        behavior: MDMObject,
        stepMap: Map<String, StepExecution>
    ): List<SuccessionEdge> {
        return (behavior as Type).ownedFeature
            .filterIsInstance<Succession>()
            .mapNotNull { succession ->
                val connectorEnds = getConnectorEnds(succession as MDMObject)
                if (connectorEnds.size >= 2) {
                    val sourceId = resolveConnectorEndStepId(connectorEnds[0], stepMap)
                    val targetId = resolveConnectorEndStepId(connectorEnds[1], stepMap)
                    if (sourceId != null && targetId != null) {
                        val guard = getGuardExpression(succession as MDMObject)
                        SuccessionEdge(succession as MDMObject, sourceId, targetId, guard)
                    } else null
                } else null
            }
    }

    private fun getFlows(
        behavior: MDMObject,
        stepMap: Map<String, StepExecution>
    ): List<FlowEdge> {
        return (behavior as Type).ownedFeature
            .filterIsInstance<Flow>()
            .mapNotNull { flow ->
                val sourceFeatureName = flow.sourceOutputFeature?.let { it.declaredName ?: it.name }
                val targetFeatureName = flow.targetInputFeature?.let { it.declaredName ?: it.name }
                val connectorEnds = getConnectorEnds(flow as MDMObject)

                if (connectorEnds.size >= 2) {
                    val sourceId = resolveConnectorEndStepId(connectorEnds[0], stepMap)
                    val targetId = resolveConnectorEndStepId(connectorEnds[1], stepMap)
                    if (sourceId != null && targetId != null) {
                        FlowEdge(flow as MDMObject, sourceId, targetId, sourceFeatureName, targetFeatureName)
                    } else null
                } else null
            }
    }

    private fun getConnectorEnds(connector: MDMObject): List<MDMObject> {
        // Try derived connectorEnd first (works when derivation is fully implemented)
        val ends = (connector as Connector).connectorEnd.map { it as MDMObject }
        if (ends.isNotEmpty()) return ends

        // Fallback: walk ownedRelationship to find EndFeatureMembership → memberElement
        // Use engine.getPropertyValue for membership navigation since non-null typed
        // properties may actually be unset in intermediate model objects.
        val result = mutableListOf<MDMObject>()
        for (rel in (connector as Element).ownedRelationship) {
            val member = engine.getPropertyValue(rel as MDMObject, "memberElement") as? MDMObject ?: continue
            if (member is EndFeatureMembership) {
                val endFeature = engine.getPropertyValue(member, "memberElement") as? MDMObject
                if (endFeature != null) result.add(endFeature)
            }
        }
        return result
    }

    /**
     * Resolve a connector EndFeature to a step ID in the step map.
     *
     * Connector ends are EndFeatures whose declaredName references the target step.
     * If the end's ID is directly in the step map (manual construction), use that.
     * Otherwise, match by declaredName against the steps.
     */
    private fun resolveConnectorEndStepId(
        endFeature: MDMObject,
        stepMap: Map<String, StepExecution>
    ): String? {
        // Direct ID match (manually constructed models)
        val endId = endFeature.id
        if (endId != null && endId in stepMap) return endId

        // Name-based match (parsed models): EndFeature.declaredName == Step.declaredName
        val endName = (endFeature as Feature).declaredName ?: return null
        return stepMap.entries.firstOrNull { (_, exec) ->
            val step = exec.step as Feature
            (step.declaredName ?: step.name) == endName
        }?.key
    }

    private fun getGuardExpression(succession: MDMObject): MDMObject? {
        // guardExpression is not yet in the typed Succession interface
        val guard = engine.getPropertyValue(succession, "guardExpression")
        return guard as? MDMObject
    }

    companion object {
        /**
         * Create a BehaviorExecutionEngine configured from [GearshiftSettings].
         */
        fun fromSettings(
            engine: MDMEngine,
            expressionEvaluator: KerMLExpressionEvaluator,
            settings: GearshiftSettings,
            listener: ExecutionListener = NoOpExecutionListener
        ): BehaviorExecutionEngine = BehaviorExecutionEngine(
            engine = engine,
            expressionEvaluator = expressionEvaluator,
            maxSteps = settings.maxExecutionSteps,
            listener = listener
        )
    }
}
