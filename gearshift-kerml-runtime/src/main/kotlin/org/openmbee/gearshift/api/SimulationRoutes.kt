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

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.openmbee.gearshift.generated.interfaces.Behavior
import org.openmbee.gearshift.kerml.KerMLModel
import org.openmbee.gearshift.kerml.eval.KerMLExpressionEvaluator
import org.openmbee.gearshift.kerml.eval.KernelFunctionLibrary
import org.openmbee.gearshift.kerml.exec.BehaviorExecutionEngine
import org.openmbee.gearshift.kerml.exec.ExecutionEvent
import org.openmbee.gearshift.kerml.exec.SimulationState
import org.openmbee.mdm.framework.runtime.MDMObject

// === DTOs ===

data class SimulationStartRequest(
    val kerml: String,
    val behaviorName: String,
    val inputs: Map<String, Any?>? = null,
    val delayMs: Long? = null,
    val startPaused: Boolean? = null
)

data class SimulationStartResponse(
    val sessionId: String,
    val state: SimulationState
)

data class SpeedRequest(
    val delayMs: Long
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SimulationStatusResponse(
    val sessionId: String,
    val state: SimulationState,
    val speedMs: Long
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SimulationSessionSummary(
    val sessionId: String,
    val state: SimulationState,
    val speedMs: Long
)

private val sseObjectMapper = ObjectMapper().registerKotlinModule().apply {
    setSerializationInclusion(JsonInclude.Include.NON_NULL)
}

/**
 * Install simulation streaming routes.
 *
 * Endpoints:
 * - `POST /simulation/start` — start a new simulation session
 * - `GET /simulation/{id}/events` — SSE event stream
 * - `POST /simulation/{id}/pause` — pause
 * - `POST /simulation/{id}/resume` — resume
 * - `POST /simulation/{id}/step` — step forward one event
 * - `POST /simulation/{id}/speed` — set playback speed
 * - `GET /simulation/{id}/status` — get session state
 * - `GET /simulation/sessions` — list all sessions
 */
fun Route.simulationRoutes(sessionManager: SimulationSessionManager) {

    post("/simulation/start") {
        try {
            val request = call.receive<SimulationStartRequest>()

            // Parse KerML into a temporary model
            val model = KerMLModel.createWithMounts()
            model.parseString(request.kerml)

            val parseResult = model.getLastParseResult()
            if (parseResult == null || !parseResult.success) {
                val errors = parseResult?.errors?.map { it.message } ?: listOf("Parse error")
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("success" to false, "errors" to errors)
                )
                return@post
            }

            // Find the behavior by name
            val behavior = model.findByName<Behavior>(request.behaviorName)
            if (behavior == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("success" to false, "errors" to listOf("Behavior '${request.behaviorName}' not found"))
                )
                return@post
            }

            // Create session
            val session = sessionManager.create(
                delayMs = request.delayMs ?: 0L,
                startPaused = request.startPaused ?: false
            )

            // Build engine with session as listener
            val library = KernelFunctionLibrary(model.engine)
            val evaluator = KerMLExpressionEvaluator(model.engine, library)
            val engine = BehaviorExecutionEngine(
                engine = model.engine,
                expressionEvaluator = evaluator,
                listener = session
            )

            // Launch execution in background
            CoroutineScope(Dispatchers.Default).launch {
                engine.execute(behavior as MDMObject, request.inputs ?: emptyMap())
            }

            call.respond(
                SimulationStartResponse(
                    sessionId = session.id,
                    state = session.state
                )
            )
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("success" to false, "errors" to listOf(e.message ?: "Unknown error"))
            )
        }
    }

    get("/simulation/{id}/events") {
        val id = call.parameters["id"]!!
        val session = sessionManager.get(id)
        if (session == null) {
            call.respond(HttpStatusCode.NotFound, mapOf("error" to "Session not found: $id"))
            return@get
        }

        call.respondTextWriter(contentType = ContentType.Text.EventStream) {
            session.events.collect { event ->
                val json = sseObjectMapper.writeValueAsString(event)
                write("data: $json\n\n")
                flush()

                // Close stream on terminal events
                if (event is ExecutionEvent.ExecutionCompleted || event is ExecutionEvent.ExecutionError) {
                    return@collect
                }
            }
        }
    }

    post("/simulation/{id}/pause") {
        val id = call.parameters["id"]!!
        val session = sessionManager.get(id)
        if (session == null) {
            call.respond(HttpStatusCode.NotFound, mapOf("error" to "Session not found: $id"))
            return@post
        }
        session.pause()
        call.respond(mapOf("sessionId" to id, "state" to session.state))
    }

    post("/simulation/{id}/resume") {
        val id = call.parameters["id"]!!
        val session = sessionManager.get(id)
        if (session == null) {
            call.respond(HttpStatusCode.NotFound, mapOf("error" to "Session not found: $id"))
            return@post
        }
        session.resume()
        call.respond(mapOf("sessionId" to id, "state" to session.state))
    }

    post("/simulation/{id}/step") {
        val id = call.parameters["id"]!!
        val session = sessionManager.get(id)
        if (session == null) {
            call.respond(HttpStatusCode.NotFound, mapOf("error" to "Session not found: $id"))
            return@post
        }
        session.stepForward()
        call.respond(mapOf("sessionId" to id, "state" to session.state))
    }

    post("/simulation/{id}/speed") {
        val id = call.parameters["id"]!!
        val session = sessionManager.get(id)
        if (session == null) {
            call.respond(HttpStatusCode.NotFound, mapOf("error" to "Session not found: $id"))
            return@post
        }
        val request = call.receive<SpeedRequest>()
        session.setSpeed(request.delayMs)
        call.respond(mapOf("sessionId" to id, "speedMs" to session.getSpeed()))
    }

    get("/simulation/{id}/status") {
        val id = call.parameters["id"]!!
        val session = sessionManager.get(id)
        if (session == null) {
            call.respond(HttpStatusCode.NotFound, mapOf("error" to "Session not found: $id"))
            return@get
        }
        call.respond(
            SimulationStatusResponse(
                sessionId = session.id,
                state = session.state,
                speedMs = session.getSpeed()
            )
        )
    }

    get("/simulation/sessions") {
        val sessions = sessionManager.listAll().map { session ->
            SimulationSessionSummary(
                sessionId = session.id,
                state = session.state,
                speedMs = session.getSpeed()
            )
        }
        call.respond(sessions)
    }
}
