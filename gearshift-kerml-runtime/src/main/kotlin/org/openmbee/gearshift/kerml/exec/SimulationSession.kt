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
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.concurrent.atomic.AtomicLong

private val logger = KotlinLogging.logger {}

enum class SimulationState {
    PENDING,
    RUNNING,
    PAUSED,
    FINISHED
}

/**
 * A simulation session that implements [ExecutionListener] for streaming
 * execution events with playback controls (pause, resume, step-forward, speed).
 *
 * Events are emitted to a [SharedFlow] for SSE consumers. Pause is implemented
 * via a [CompletableDeferred] gate that suspends event emission until resumed.
 */
class SimulationSession(
    val id: String,
    delayMs: Long = 0L,
    startPaused: Boolean = false
) : ExecutionListener {

    private val _events = MutableSharedFlow<ExecutionEvent>(replay = 256, extraBufferCapacity = 64)

    /** Flow of execution events for SSE consumers. Replay buffer supports late-connecting SSE clients. */
    val events: SharedFlow<ExecutionEvent> = _events.asSharedFlow()

    @Volatile
    var state: SimulationState = if (startPaused) SimulationState.PAUSED else SimulationState.PENDING
        private set

    private val delayMsAtomic = AtomicLong(delayMs.coerceAtLeast(0))

    @Volatile
    private var pauseGate: CompletableDeferred<Unit>? = if (startPaused) CompletableDeferred() else null

    @Volatile
    private var stepOnce: Boolean = false

    override suspend fun onEvent(event: ExecutionEvent) {
        // If we haven't started yet, mark as running on first event
        if (state == SimulationState.PENDING) {
            state = SimulationState.RUNNING
        }

        // Suspend if paused (wait for gate to be completed)
        pauseGate?.await()

        // Apply speed delay
        val ms = delayMsAtomic.get()
        if (ms > 0) {
            delay(ms)
        }

        // Emit the event
        _events.emit(event)

        logger.debug { "Session $id: emitted ${event::class.simpleName} seq=${event.sequenceNumber}" }

        // If stepping, re-pause after emitting one event
        if (stepOnce) {
            stepOnce = false
            doPause()
        }

        // Mark as finished on terminal events
        if (event is ExecutionEvent.ExecutionCompleted || event is ExecutionEvent.ExecutionError) {
            state = SimulationState.FINISHED
        }
    }

    fun pause() {
        if (state == SimulationState.RUNNING) {
            doPause()
        }
    }

    fun resume() {
        if (state == SimulationState.PAUSED) {
            doResume()
        }
    }

    /**
     * Step forward one event then re-pause.
     * If not currently paused, this is a no-op.
     */
    fun stepForward() {
        if (state == SimulationState.PAUSED) {
            stepOnce = true
            doResume()
        }
    }

    fun setSpeed(delayMs: Long) {
        delayMsAtomic.set(delayMs.coerceAtLeast(0))
    }

    fun getSpeed(): Long = delayMsAtomic.get()

    private fun doPause() {
        state = SimulationState.PAUSED
        pauseGate = CompletableDeferred()
        logger.debug { "Session $id: paused" }
    }

    private fun doResume() {
        state = SimulationState.RUNNING
        val gate = pauseGate
        pauseGate = null
        gate?.complete(Unit)
        logger.debug { "Session $id: resumed" }
    }
}
