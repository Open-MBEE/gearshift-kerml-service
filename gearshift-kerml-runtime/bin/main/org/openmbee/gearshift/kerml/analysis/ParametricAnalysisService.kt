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
package org.openmbee.gearshift.kerml.analysis

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.mdm.framework.constraints.ConflictResult
import org.openmbee.mdm.framework.constraints.ConstraintSolverService
import org.openmbee.mdm.framework.constraints.OptimizationResult
import org.openmbee.mdm.framework.constraints.SolverResult
import org.openmbee.mdm.framework.constraints.Z3Sort
import org.openmbee.mdm.framework.constraints.Z3Variable
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MDMObject

private val logger = KotlinLogging.logger {}

/**
 * KerML-specific parametric analysis service.
 *
 * Extracts constraints from KerML model elements (Invariants, Features),
 * infers variable sorts from Feature types, and submits to the
 * [ConstraintSolverService] for solving.
 *
 * Supports:
 * - Solving constraint systems over KerML Features
 * - Checking requirement consistency (are all invariants satisfiable together?)
 * - Trade studies (optimize an objective subject to constraints)
 *
 * @param engine The MDMEngine for model access
 * @param solverService The underlying Z3-based constraint solver
 */
class ParametricAnalysisService(
    private val engine: MDMEngine,
    private val solverService: ConstraintSolverService
) {
    /**
     * Solve a constraint system defined by Features and Invariants.
     *
     * Extracts variable declarations from Features (name and type) and
     * constraint expressions from Invariants, then solves for satisfying
     * assignments.
     *
     * @param features List of Feature MDMObjects representing variables
     * @param invariants List of constraint MDMObjects (Invariant or ConstraintUsage)
     * @return Solver result with variable assignments
     */
    fun solveConstraints(
        features: List<MDMObject>,
        invariants: List<MDMObject>
    ): SolverResult {
        logger.info { "Solving constraints: ${features.size} features, ${invariants.size} invariants" }

        val variables = features.mapNotNull { featureToVariable(it) }
        val constraints = invariants.mapNotNull { extractConstraintExpression(it) }

        if (variables.isEmpty()) {
            return SolverResult(satisfiable = true, assignments = emptyMap())
        }

        if (constraints.isEmpty()) {
            return SolverResult(satisfiable = true, assignments = emptyMap())
        }

        return solverService.solve(variables, constraints)
    }

    /**
     * Check if a set of requirements (invariants) are mutually consistent.
     *
     * This is useful for requirement conflict detection: given a set of
     * requirements expressed as invariants, are they all satisfiable together?
     *
     * @param invariants List of constraint MDMObjects
     * @return Conflict result indicating consistency and any conflicts
     */
    fun checkRequirementConsistency(invariants: List<MDMObject>): ConflictResult {
        logger.info { "Checking consistency of ${invariants.size} invariants" }

        val constraints = invariants.mapNotNull { extractConstraintExpression(it) }
        if (constraints.isEmpty()) {
            return ConflictResult(consistent = true)
        }

        // Collect all variables referenced in the constraints
        val allFeatures = mutableSetOf<MDMObject>()
        for (invariant in invariants) {
            val referencedFeatures = collectReferencedFeatures(invariant)
            allFeatures.addAll(referencedFeatures)
        }

        val variables = allFeatures.mapNotNull { featureToVariable(it) }

        return solverService.findConflicts(variables, constraints)
    }

    /**
     * Perform a trade study: optimize an objective function subject to constraints.
     *
     * @param designVariables Features representing design variables
     * @param constraints Invariants defining the feasible region
     * @param objective OCL expression for the objective function
     * @param minimize true to minimize, false to maximize
     * @return Optimization result with optimal assignments
     */
    fun tradeStudy(
        designVariables: List<MDMObject>,
        constraints: List<MDMObject>,
        objective: String,
        minimize: Boolean = true
    ): OptimizationResult {
        logger.info { "Running trade study: ${designVariables.size} variables, ${constraints.size} constraints" }

        val variables = designVariables.mapNotNull { featureToVariable(it) }
        val constraintExprs = constraints.mapNotNull { extractConstraintExpression(it) }

        if (variables.isEmpty()) {
            return OptimizationResult(
                satisfiable = false,
                errorMessage = "No design variables declared"
            )
        }

        return solverService.optimize(variables, constraintExprs, objective, minimize)
    }

    // === Model Element Extraction ===

    /**
     * Convert a KerML Feature to a Z3Variable declaration.
     * Infers the Z3 sort from the Feature's typing.
     */
    internal fun featureToVariable(feature: MDMObject): Z3Variable? {
        val name = engine.getPropertyValue(feature, "declaredName") as? String
            ?: engine.getPropertyValue(feature, "name") as? String
            ?: return null

        val sort = inferSort(feature)

        // Extract bounds from multiplicity or attribute annotations
        val lowerBound = extractLowerBound(feature)
        val upperBound = extractUpperBound(feature)

        return Z3Variable(name, sort, lowerBound, upperBound)
    }

    /**
     * Infer the Z3Sort from a Feature's type.
     */
    private fun inferSort(feature: MDMObject): Z3Sort {
        // Check direct typing
        val types = getTypes(feature)
        for (type in types) {
            val typeName = engine.getPropertyValue(type, "declaredName") as? String
                ?: engine.getPropertyValue(type, "name") as? String
                ?: type.className

            return when (typeName.lowercase()) {
                "integer", "natural", "positive" -> Z3Sort.INT
                "real", "rational" -> Z3Sort.REAL
                "boolean" -> Z3Sort.BOOL
                else -> Z3Sort.REAL // Default to Real for unknown types
            }
        }

        // Default to Real if no type information
        return Z3Sort.REAL
    }

    /**
     * Extract a constraint expression from an Invariant or ConstraintUsage element.
     */
    internal fun extractConstraintExpression(invariant: MDMObject): String? {
        // Check for an expression body
        val ownedConstraints = getOwnedConstraints(invariant)
        if (ownedConstraints.isNotEmpty()) {
            // Use the first constraint's expression
            val constraint = ownedConstraints.first()
            val expression = engine.getPropertyValue(constraint, "expression") as? String
            if (expression != null) return expression
        }

        // Check for the constraint expression on the element itself
        val expression = engine.getPropertyValue(invariant, "expression") as? String
        if (expression != null) return expression

        // Check for owned expressions
        val ownedExpressions = getOwnedExpressions(invariant)
        if (ownedExpressions.isNotEmpty()) {
            // Try to get the text representation of the first expression
            val expr = ownedExpressions.first()
            return engine.getPropertyValue(expr, "body") as? String
        }

        return null
    }

    /**
     * Collect all Features referenced in a constraint/invariant.
     */
    private fun collectReferencedFeatures(invariant: MDMObject): List<MDMObject> {
        // Get features from the invariant's context (owning namespace)
        val owner = engine.getPropertyValue(invariant, "owner") as? MDMObject
        if (owner != null) {
            val features = engine.getPropertyValue(owner, "ownedFeature")
            return when (features) {
                is List<*> -> features.filterIsInstance<MDMObject>()
                    .filter { engine.isInstanceOf(it, "Feature") }
                is MDMObject -> if (engine.isInstanceOf(features, "Feature")) listOf(features) else emptyList()
                else -> emptyList()
            }
        }
        return emptyList()
    }

    // === Navigation Helpers ===

    private fun getTypes(feature: MDMObject): List<MDMObject> {
        val types = engine.getPropertyValue(feature, "type")
        return when (types) {
            is List<*> -> types.filterIsInstance<MDMObject>()
            is MDMObject -> listOf(types)
            else -> emptyList()
        }
    }

    private fun getOwnedConstraints(element: MDMObject): List<MDMObject> {
        val constraints = engine.getPropertyValue(element, "ownedConstraint")
        return when (constraints) {
            is List<*> -> constraints.filterIsInstance<MDMObject>()
            is MDMObject -> listOf(constraints)
            else -> emptyList()
        }
    }

    private fun getOwnedExpressions(element: MDMObject): List<MDMObject> {
        val expressions = engine.getPropertyValue(element, "ownedExpression")
        return when (expressions) {
            is List<*> -> expressions.filterIsInstance<MDMObject>()
            is MDMObject -> listOf(expressions)
            else -> emptyList()
        }
    }

    private fun extractLowerBound(feature: MDMObject): Number? {
        // Look for multiplicity lower bound
        val multiplicity = engine.getPropertyValue(feature, "multiplicity") as? MDMObject
        if (multiplicity != null) {
            val lower = engine.getPropertyValue(multiplicity, "lowerBound")
            if (lower is MDMObject && engine.isInstanceOf(lower, "LiteralInteger")) {
                return engine.getPropertyValue(lower, "value") as? Number
            }
        }
        return null
    }

    private fun extractUpperBound(feature: MDMObject): Number? {
        // Look for multiplicity upper bound
        val multiplicity = engine.getPropertyValue(feature, "multiplicity") as? MDMObject
        if (multiplicity != null) {
            val upper = engine.getPropertyValue(multiplicity, "upperBound")
            if (upper is MDMObject && engine.isInstanceOf(upper, "LiteralInteger")) {
                return engine.getPropertyValue(upper, "value") as? Number
            }
        }
        return null
    }
}
