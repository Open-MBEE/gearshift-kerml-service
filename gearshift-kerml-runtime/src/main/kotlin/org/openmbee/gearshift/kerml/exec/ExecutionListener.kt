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

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

// === Helper DTOs (no MDMObject references — safe for serialization) ===

data class StepInfo(
    val id: String,
    val name: String?
)

data class SuccessionInfo(
    val sourceStepId: String,
    val targetStepId: String,
    val hasGuard: Boolean
)

data class FlowInfo(
    val sourceStepId: String,
    val targetStepId: String,
    val sourceOutputFeature: String?,
    val targetInputFeature: String?
)

// === Event Model ===

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(ExecutionEvent.GraphInitialized::class, name = "GraphInitialized"),
    JsonSubTypes.Type(ExecutionEvent.StepStateChanged::class, name = "StepStateChanged"),
    JsonSubTypes.Type(ExecutionEvent.TokenPlaced::class, name = "TokenPlaced"),
    JsonSubTypes.Type(ExecutionEvent.TokenPropagated::class, name = "TokenPropagated"),
    JsonSubTypes.Type(ExecutionEvent.DataTransferred::class, name = "DataTransferred"),
    JsonSubTypes.Type(ExecutionEvent.GuardEvaluated::class, name = "GuardEvaluated"),
    JsonSubTypes.Type(ExecutionEvent.ExecutionCompleted::class, name = "ExecutionCompleted"),
    JsonSubTypes.Type(ExecutionEvent.ExecutionError::class, name = "ExecutionError")
)
sealed class ExecutionEvent {
    abstract val sequenceNumber: Int
    abstract val timestamp: Long

    data class GraphInitialized(
        override val sequenceNumber: Int,
        override val timestamp: Long = System.currentTimeMillis(),
        val steps: List<StepInfo>,
        val successions: List<SuccessionInfo>,
        val flows: List<FlowInfo>
    ) : ExecutionEvent()

    data class StepStateChanged(
        override val sequenceNumber: Int,
        override val timestamp: Long = System.currentTimeMillis(),
        val stepId: String,
        val stepName: String?,
        val oldState: StepState,
        val newState: StepState
    ) : ExecutionEvent()

    data class TokenPlaced(
        override val sequenceNumber: Int,
        override val timestamp: Long = System.currentTimeMillis(),
        val stepId: String,
        val stepName: String?
    ) : ExecutionEvent()

    data class TokenPropagated(
        override val sequenceNumber: Int,
        override val timestamp: Long = System.currentTimeMillis(),
        val sourceStepId: String,
        val targetStepId: String
    ) : ExecutionEvent()

    data class DataTransferred(
        override val sequenceNumber: Int,
        override val timestamp: Long = System.currentTimeMillis(),
        val sourceStepId: String,
        val targetStepId: String,
        val featureName: String?,
        val value: Any?
    ) : ExecutionEvent()

    data class GuardEvaluated(
        override val sequenceNumber: Int,
        override val timestamp: Long = System.currentTimeMillis(),
        val sourceStepId: String,
        val targetStepId: String,
        val passed: Boolean
    ) : ExecutionEvent()

    data class ExecutionCompleted(
        override val sequenceNumber: Int,
        override val timestamp: Long = System.currentTimeMillis(),
        val status: ExecutionStatus,
        val stepsExecuted: Int,
        val outputs: Map<String, Any?> = emptyMap()
    ) : ExecutionEvent()

    data class ExecutionError(
        override val sequenceNumber: Int,
        override val timestamp: Long = System.currentTimeMillis(),
        val message: String
    ) : ExecutionEvent()
}

// === Listener Interface ===

interface ExecutionListener {
    suspend fun onEvent(event: ExecutionEvent)
}

object NoOpExecutionListener : ExecutionListener {
    override suspend fun onEvent(event: ExecutionEvent) { /* no-op */ }
}
