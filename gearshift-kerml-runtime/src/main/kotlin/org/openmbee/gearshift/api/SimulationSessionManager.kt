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
package org.openmbee.gearshift.api

import org.openmbee.gearshift.kerml.exec.SimulationSession
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * Manages the lifecycle of [SimulationSession] instances.
 */
class SimulationSessionManager {

    private val sessions = ConcurrentHashMap<String, SimulationSession>()

    fun create(delayMs: Long = 0L, startPaused: Boolean = false): SimulationSession {
        val id = UUID.randomUUID().toString()
        val session = SimulationSession(id, delayMs, startPaused)
        sessions[id] = session
        return session
    }

    fun get(id: String): SimulationSession? = sessions[id]

    fun remove(id: String): SimulationSession? = sessions.remove(id)

    fun listAll(): List<SimulationSession> = sessions.values.toList()
}
