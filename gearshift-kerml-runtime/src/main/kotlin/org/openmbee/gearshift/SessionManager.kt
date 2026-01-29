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
package org.openmbee.gearshift

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.gearshift.framework.runtime.MetamodelRegistry

private val logger = KotlinLogging.logger {}

/**
 * Manages modeling sessions for the application.
 *
 * SessionManager is a singleton that:
 * - Creates and tracks sessions
 * - Provides access to sessions by ID
 * - Manages session lifecycle (create, close, persist)
 *
 * This is the entry point for the API layer.
 */
class SessionManager(
    /** The schema (metamodel registry) to use for creating sessions */
    private val schema: MetamodelRegistry,
) {
    /** Active sessions by ID */
    private val sessions: MutableMap<String, Session> = mutableMapOf()

    /**
     * Create a new session with the given name and language.
     */
    fun createSession(name: String, language: ModelLanguage = ModelLanguage.KERML): Session {
        val session = Session.create(name, language, schema)
        sessions[session.id] = session
        logger.info { "Created session '${session.name}' with ID ${session.id}" }
        return session
    }

    /**
     * Get a session by ID.
     */
    fun getSession(id: String): Session? = sessions[id]

    /**
     * Get all active sessions.
     */
    fun getAllSessions(): List<Session> = sessions.values.toList()

    /**
     * Close and remove a session.
     */
    fun closeSession(id: String): Boolean {
        val session = sessions.remove(id)
        if (session != null) {
            logger.info { "Closed session '${session.name}' with ID ${session.id}" }
            return true
        }
        return false
    }

    /**
     * Get the count of active sessions.
     */
    fun sessionCount(): Int = sessions.size

    /**
     * Close all sessions.
     */
    fun closeAll() {
        val count = sessions.size
        sessions.clear()
        logger.info { "Closed $count session(s)" }
    }
}
