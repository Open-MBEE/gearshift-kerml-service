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
import org.openmbee.gearshift.generated.interfaces.Element
import org.openmbee.gearshift.generated.interfaces.Feature
import org.openmbee.gearshift.generated.interfaces.Invariant
import org.openmbee.gearshift.kerml.KerMLModel
import org.openmbee.gearshift.kerml.analysis.ImpactAnalysisResult
import org.openmbee.gearshift.kerml.analysis.ParametricAnalysisService
import org.openmbee.mdm.framework.constraints.ConstraintSolverService
import org.openmbee.mdm.framework.runtime.MDMObject

// === DTOs ===

data class ParametricAnalysisRequest(
    val operation: String,              // "solve", "optimize", "check-consistency", "impact-analysis"
    val kerml: String? = null,          // KerML text for inline analysis
    val scope: String? = null,          // QN prefix OR element ID — filters invariants to a namespace
    val constraints: List<String>? = null,    // Specific invariant QNs or element IDs
    val designVariables: List<String>? = null, // Feature QNs or element IDs for optimize
    val objective: String? = null,      // Feature QN or name for optimization
    val minimize: Boolean = true,
    val apply: Boolean = false,         // Persist solver results as FeatureValues
    val feature: String? = null,        // For impact-analysis: feature QN or element ID
    val proposedValue: Any? = null      // For impact-analysis: optional new value to check
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ParametricAnalysisResponse(
    val success: Boolean,
    val satisfiable: Boolean? = null,
    val assignments: Map<String, Any?>? = null,
    val objectiveValue: Any? = null,
    val consistent: Boolean? = null,
    val conflictingConstraints: List<String>? = null,
    val impact: ImpactAnalysisResult? = null,
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

// === Resolution Helpers ===

private val UUID_PATTERN = Regex(
    "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
    RegexOption.IGNORE_CASE
)

private fun isElementId(s: String): Boolean = UUID_PATTERN.matches(s)

/**
 * Resolve a QN-or-ID string to a typed element.
 * UUIDs are treated as element IDs; anything else as a qualified name.
 */
private inline fun <reified T : Element> resolveElement(model: KerMLModel, ref: String): T? {
    if (isElementId(ref)) {
        return model.getTypedElement(ref) as? T
    }
    return model.allOfType<T>().firstOrNull { it.qualifiedName == ref }
        ?: model.findByName<T>(ref)
}

/**
 * Collect invariants, optionally filtered by specific constraint refs or scope.
 */
private fun collectInvariants(
    model: KerMLModel,
    scope: String?,
    constraints: List<String>?
): List<Invariant> {
    // If specific QNs/IDs provided, resolve each one
    if (!constraints.isNullOrEmpty()) {
        return constraints.mapNotNull { resolveElement<Invariant>(model, it) }
    }
    // Otherwise get all, optionally filtered by scope
    var invariants = model.allOfType<Invariant>()
    if (scope != null) {
        if (isElementId(scope)) {
            invariants = invariants.filter { inv ->
                (inv.owningNamespace as? MDMObject)?.id == scope
            }
        } else {
            invariants = invariants.filter { inv ->
                val nsQn = inv.owningNamespace?.qualifiedName
                nsQn != null && (nsQn.startsWith("$scope::") || nsQn == scope)
            }
        }
    }
    return invariants
}

/**
 * Collect design variable features, optionally from specific refs.
 */
private fun collectDesignVariables(
    model: KerMLModel,
    designVariables: List<String>?
): List<Feature> {
    if (!designVariables.isNullOrEmpty()) {
        return designVariables.mapNotNull { resolveElement<Feature>(model, it) }
    }
    return model.allOfType<Feature>().filter { it.declaredName != null }
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

    val invariants = collectInvariants(model, request.scope, request.constraints)

    return when (request.operation) {
        "solve" -> {
            val result = service.solveConstraints(invariants)
            if (request.apply && result.satisfiable) {
                val features = invariants
                    .flatMap { service.collectReferencedFeatures(it) }
                    .distinctBy { (it as MDMObject).id }
                service.applySolution(result.assignments, features)
            }
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
            val features = collectDesignVariables(model, request.designVariables)
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

        "impact-analysis" -> {
            val featureRef = request.feature ?: return ParametricAnalysisResponse(
                success = false,
                errors = listOf("'feature' required for impact-analysis")
            )
            val feature = resolveElement<Feature>(model, featureRef)
            val featureName = feature?.declaredName ?: feature?.name ?: featureRef
            // Scan all invariants for impact (scope doesn't limit the search)
            val allInvariants = model.allOfType<Invariant>()
            val result = service.analyzeImpact(featureName, allInvariants, request.proposedValue)
            ParametricAnalysisResponse(success = true, impact = result)
        }

        else -> ParametricAnalysisResponse(
            success = false,
            errors = listOf("Unknown operation: '${request.operation}'. Valid operations: solve, optimize, check-consistency, impact-analysis")
        )
    }
}
