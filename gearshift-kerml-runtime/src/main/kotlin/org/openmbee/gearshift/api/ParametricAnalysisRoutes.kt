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
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.openmbee.gearshift.generated.interfaces.Feature
import org.openmbee.gearshift.generated.interfaces.Invariant
import org.openmbee.gearshift.kerml.KerMLModel
import org.openmbee.gearshift.kerml.analysis.ParametricAnalysisService
import org.openmbee.mdm.framework.constraints.ConstraintSolverService

// === DTOs ===

data class ParametricAnalysisRequest(
    val operation: String,
    val kerml: String? = null,
    val scope: String? = null,
    val objective: String? = null,
    val minimize: Boolean = true
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ParametricAnalysisResponse(
    val success: Boolean,
    val satisfiable: Boolean? = null,
    val assignments: Map<String, Any?>? = null,
    val objectiveValue: Any? = null,
    val consistent: Boolean? = null,
    val conflictingConstraints: List<String>? = null,
    val errors: List<String> = emptyList()
)

// === Route Installation ===

/**
 * Install parametric analysis routes.
 *
 * Endpoints:
 * - `POST /analysis/parametric` — inline analysis (accepts KerML text, parses temporarily)
 * - `POST /projects/{projectId}/commits/{commitId}/analysis/parametric` — project-scoped
 */
fun Route.parametricAnalysisRoutes(store: ProjectStore) {

    post("/analysis/parametric") {
        try {
            val request = call.receive<ParametricAnalysisRequest>()

            if (request.kerml == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ParametricAnalysisResponse(
                        success = false,
                        errors = listOf("'kerml' field is required for inline analysis")
                    )
                )
                return@post
            }

            // Parse KerML into a temporary model
            val model = KerMLModel.createWithMounts()
            model.parseString(request.kerml)

            val result = model.getLastParseResult()
            if (result == null || !result.success) {
                val errors = result?.errors?.map { it.message } ?: listOf("Parse error")
                call.respond(
                    HttpStatusCode.BadRequest,
                    ParametricAnalysisResponse(success = false, errors = errors)
                )
                return@post
            }

            val response = executeAnalysis(request, model)
            call.respond(response)
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                ParametricAnalysisResponse(
                    success = false,
                    errors = listOf(e.message ?: "Unknown error")
                )
            )
        }
    }

    post("/projects/{projectId}/commits/{commitId}/analysis/parametric") {
        try {
            val projectId = call.parameters["projectId"]!!
            val commitId = call.parameters["commitId"]!!
            val request = call.receive<ParametricAnalysisRequest>()

            val model = store.getModel(projectId)
            if (model == null) {
                call.respond(
                    HttpStatusCode.NotFound,
                    ParametricAnalysisResponse(
                        success = false,
                        errors = listOf("Project not found: $projectId")
                    )
                )
                return@post
            }

            // Verify commit exists
            val commit = store.getCommit(commitId)
            if (commit == null || commit.owningProject != projectId) {
                call.respond(
                    HttpStatusCode.NotFound,
                    ParametricAnalysisResponse(
                        success = false,
                        errors = listOf("Commit not found: $commitId")
                    )
                )
                return@post
            }

            val response = executeAnalysis(request, model)
            call.respond(response)
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                ParametricAnalysisResponse(
                    success = false,
                    errors = listOf(e.message ?: "Unknown error")
                )
            )
        }
    }
}

/**
 * Execute the parametric analysis operation against the given model.
 */
private fun executeAnalysis(
    request: ParametricAnalysisRequest,
    model: KerMLModel
): ParametricAnalysisResponse {
    val solverService = ConstraintSolverService()
    val service = ParametricAnalysisService(model.engine, solverService)

    // Collect invariants from the model using typed interfaces
    val invariants = model.allOfType<Invariant>()

    return when (request.operation) {
        "solve" -> {
            val result = service.solveConstraints(invariants)
            ParametricAnalysisResponse(
                success = true,
                satisfiable = result.satisfiable,
                assignments = result.assignments,
                errors = listOfNotNull(result.errorMessage)
            )
        }

        "optimize" -> {
            val objective = request.objective
            if (objective == null) {
                return ParametricAnalysisResponse(
                    success = false,
                    errors = listOf("'objective' field is required for optimize operation")
                )
            }
            val features = model.allOfType<Feature>().filter { it.declaredName != null }
            val result = service.tradeStudy(features, invariants, objective, request.minimize)
            ParametricAnalysisResponse(
                success = true,
                satisfiable = result.satisfiable,
                assignments = result.assignments,
                objectiveValue = result.objectiveValue,
                errors = listOfNotNull(result.errorMessage)
            )
        }

        "check-consistency" -> {
            val result = service.checkRequirementConsistency(invariants)
            ParametricAnalysisResponse(
                success = true,
                consistent = result.consistent,
                conflictingConstraints = result.conflictingConstraints,
                errors = listOfNotNull(result.errorMessage)
            )
        }

        else -> ParametricAnalysisResponse(
            success = false,
            errors = listOf("Unknown operation: '${request.operation}'. Valid operations: solve, optimize, check-consistency")
        )
    }
}
