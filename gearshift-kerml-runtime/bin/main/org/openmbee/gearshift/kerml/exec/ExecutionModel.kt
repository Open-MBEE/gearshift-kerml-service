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

import org.openmbee.mdm.framework.runtime.MDMObject

/**
 * Token-passing execution model for KerML Behaviors.
 *
 * Tokens flow through an execution graph composed of Steps connected by
 * Successions (control flow) and Flows (data transfer). This model is
 * inspired by fUML but dramatically simplified.
 */

// === Tokens ===

/**
 * A token in the execution system. Tokens carry either control flow
 * or data through the execution graph.
 */
sealed class Token {
    /** Unique ID for this token. */
    abstract val id: String
}

/**
 * Control token — represents execution flow (no data payload).
 * When a ControlToken arrives at a Step, the Step becomes READY.
 */
data class ControlToken(
    override val id: String = java.util.UUID.randomUUID().toString()
) : Token()

/**
 * Object token — carries a data value through the graph.
 * Used for data transfer via Flows between Steps.
 */
data class ObjectToken(
    override val id: String = java.util.UUID.randomUUID().toString(),
    val value: Any?
) : Token()

// === Step State ===

/**
 * The execution state of a Step node.
 */
enum class StepState {
    /** Waiting for input tokens from predecessor Steps. */
    WAITING,
    /** All required input tokens have arrived; ready to execute. */
    READY,
    /** Currently executing. */
    EXECUTING,
    /** Execution complete; output tokens produced. */
    COMPLETED
}

// === Step Execution ===

/**
 * Runtime execution state for a single Step node.
 */
data class StepExecution(
    /** The Step model element being executed. */
    val step: MDMObject,

    /** Current execution state. */
    var state: StepState = StepState.WAITING,

    /** Input tokens received from predecessor Steps. */
    val inputTokens: MutableList<Token> = mutableListOf(),

    /** Output tokens produced by execution. */
    val outputTokens: MutableList<Token> = mutableListOf(),

    /** Output values produced by expression evaluation. */
    val outputValues: MutableMap<String, Any?> = mutableMapOf()
)

// === Execution Graph ===

/**
 * An edge in the execution graph representing a Succession (control flow).
 */
data class SuccessionEdge(
    /** The Succession model element. */
    val succession: MDMObject,

    /** Source Step ID. */
    val sourceStepId: String,

    /** Target Step ID. */
    val targetStepId: String,

    /** Guard condition expression (if any). */
    val guardExpression: MDMObject? = null
)

/**
 * An edge in the execution graph representing a Flow (data transfer).
 */
data class FlowEdge(
    /** The Flow model element. */
    val flow: MDMObject,

    /** Source Step ID (data producer). */
    val sourceStepId: String,

    /** Target Step ID (data consumer). */
    val targetStepId: String,

    /** Name of the source output feature. */
    val sourceOutputFeature: String? = null,

    /** Name of the target input feature. */
    val targetInputFeature: String? = null
)

/**
 * The complete execution graph for a Behavior.
 * Contains Steps as nodes and Successions/Flows as edges.
 */
data class ExecutionGraph(
    /** All Step nodes in the graph (keyed by Step element ID). */
    val steps: Map<String, StepExecution>,

    /** Succession edges (control flow). */
    val successions: List<SuccessionEdge>,

    /** Flow edges (data transfer). */
    val flows: List<FlowEdge>
) {
    /** Find initial Steps (those with no incoming Successions). */
    fun findInitialSteps(): List<StepExecution> {
        val stepsWithPredecessors = successions.map { it.targetStepId }.toSet()
        return steps.values.filter { it.step.id !in stepsWithPredecessors }
    }

    /** Find outgoing Successions from a given Step. */
    fun getOutgoingSuccessions(stepId: String): List<SuccessionEdge> {
        return successions.filter { it.sourceStepId == stepId }
    }

    /** Find incoming Successions to a given Step. */
    fun getIncomingSuccessions(stepId: String): List<SuccessionEdge> {
        return successions.filter { it.targetStepId == stepId }
    }

    /** Find outgoing Flows from a given Step. */
    fun getOutgoingFlows(stepId: String): List<FlowEdge> {
        return flows.filter { it.sourceStepId == stepId }
    }

    /** Find incoming Flows to a given Step. */
    fun getIncomingFlows(stepId: String): List<FlowEdge> {
        return flows.filter { it.targetStepId == stepId }
    }
}

// === Execution Result ===

/**
 * Status of a behavior execution.
 */
enum class ExecutionStatus {
    /** Execution completed normally (all Steps reached COMPLETED). */
    COMPLETED,

    /** Execution terminated because maxSteps was exceeded. */
    MAX_STEPS_EXCEEDED,

    /** Execution failed due to an error. */
    ERROR
}

/**
 * An entry in the execution trace, recording what happened during execution.
 */
data class TraceEntry(
    /** The Step that was executed. */
    val stepId: String,

    /** The Step's className. */
    val stepClassName: String,

    /** Step number in the execution sequence. */
    val sequenceNumber: Int,

    /** Input values for this Step. */
    val inputs: Map<String, Any?> = emptyMap(),

    /** Output values produced by this Step. */
    val outputs: Map<String, Any?> = emptyMap()
)

/**
 * The result of executing a Behavior.
 */
data class ExecutionResult(
    /** Overall execution status. */
    val status: ExecutionStatus,

    /** Output values from the Behavior's output parameters. */
    val outputs: Map<String, Any?> = emptyMap(),

    /** Ordered trace of Step executions. */
    val trace: List<TraceEntry> = emptyList(),

    /** Error message if status is ERROR. */
    val errorMessage: String? = null,

    /** Number of execution steps taken. */
    val stepsExecuted: Int = 0
)
