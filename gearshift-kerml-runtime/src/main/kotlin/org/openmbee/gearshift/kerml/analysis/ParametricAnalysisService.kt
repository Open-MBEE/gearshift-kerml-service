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
     *
     * Uses a three-strategy fallthrough:
     * 1. Walk the expression tree via ResultExpressionMembership (parsed KerML)
     * 2. Check owned features for Expression elements (alternative tree shapes)
     * 3. Legacy: string `expression` property (backward compat with hand-built tests)
     */
    internal fun extractConstraintExpression(invariant: MDMObject): String? {
        // Strategy 1: Walk expression tree via ResultExpressionMembership
        val resultExpr = getResultExpression(invariant)
        if (resultExpr != null) {
            val ocl = expressionTreeToOcl(resultExpr)
            if (ocl != null) return ocl
        }

        // Strategy 2: Check owned features for Expression elements
        val ownedFeatures = getOwnedFeatures(invariant)
        for (feature in ownedFeatures) {
            if (engine.isInstanceOf(feature, "Expression")) {
                val ocl = expressionTreeToOcl(feature)
                if (ocl != null) return ocl
            }
        }

        // Strategy 3: Legacy string property (backward compat with hand-built tests)
        // Uses MDMObject.getProperty() directly since "expression" is not a metamodel
        // attribute — it's an ad-hoc property set directly on the object.
        val expression = invariant.getProperty("expression") as? String
        if (expression != null) return expression

        return null
    }

    // === Expression Tree → OCL Bridge ===

    /**
     * Convert a KerML Expression MDMObject tree to an OCL string for the solver.
     *
     * Walks the MDMObject expression tree recursively, dispatching on className,
     * following the same pattern as [KerMLExpressionEvaluator.evaluate].
     */
    internal fun expressionTreeToOcl(expression: MDMObject): String? {
        return when (expression.className) {
            "LiteralInteger" -> {
                val value = engine.getPropertyValue(expression, "value")
                value?.toString()
            }
            "LiteralRational" -> {
                val value = engine.getPropertyValue(expression, "value")
                value?.toString()
            }
            "LiteralBoolean" -> {
                val value = engine.getPropertyValue(expression, "value")
                if (value is Boolean) value.toString() else null
            }
            "LiteralString" -> {
                val value = engine.getPropertyValue(expression, "value")
                if (value is String) "'$value'" else null
            }
            "LiteralInfinity" -> "*"
            "NullExpression" -> "null"
            "OperatorExpression" -> operatorExpressionToOcl(expression)
            "FeatureReferenceExpression" -> featureReferenceToOcl(expression)
            "InvocationExpression" -> invocationExpressionToOcl(expression)
            else -> {
                // For generic Expression, try its result expression
                if (engine.isInstanceOf(expression, "Expression")) {
                    val resultExpr = getResultExpression(expression)
                    if (resultExpr != null) expressionTreeToOcl(resultExpr) else null
                } else {
                    null
                }
            }
        }
    }

    private fun operatorExpressionToOcl(expression: MDMObject): String? {
        val operator = engine.getPropertyValue(expression, "operator") as? String ?: return null
        val args = getExpressionArguments(expression)

        val oclOp = mapOperatorToOcl(operator)

        return when {
            // Conditional: if cond then a else b endif
            oclOp == "if" && args.size == 3 -> {
                val cond = expressionTreeToOcl(args[0]) ?: return null
                val thenExpr = expressionTreeToOcl(args[1]) ?: return null
                val elseExpr = expressionTreeToOcl(args[2]) ?: return null
                "if $cond then $thenExpr else $elseExpr endif"
            }
            // Unary prefix: not x, -x
            args.size == 1 -> {
                val operand = expressionTreeToOcl(args[0]) ?: return null
                "$oclOp $operand"
            }
            // Binary infix: (a op b)
            args.size == 2 -> {
                val left = expressionTreeToOcl(args[0]) ?: return null
                val right = expressionTreeToOcl(args[1]) ?: return null
                "($left $oclOp $right)"
            }
            else -> null
        }
    }

    private fun featureReferenceToOcl(expression: MDMObject): String? {
        val referent = engine.getPropertyValue(expression, "referent") as? MDMObject ?: return null
        return engine.getPropertyValue(referent, "declaredName") as? String
            ?: engine.getPropertyValue(referent, "name") as? String
    }

    private fun invocationExpressionToOcl(expression: MDMObject): String? {
        val function = engine.getPropertyValue(expression, "function") as? MDMObject
        val funcName = if (function != null) {
            engine.getPropertyValue(function, "declaredName") as? String
                ?: engine.getPropertyValue(function, "name") as? String
                ?: return null
        } else {
            return null
        }

        val args = getExpressionArguments(expression)
        val argStrings = args.mapNotNull { expressionTreeToOcl(it) }
        if (argStrings.size != args.size) return null

        return "$funcName(${argStrings.joinToString(", ")})"
    }

    // === Expression Navigation Helpers ===

    /**
     * Get expression arguments from an Expression MDMObject.
     *
     * First tries the derived `argument` association end. If that returns null
     * (derivation may not be available), falls back to `ownedFeature` filtering,
     * matching the pattern in [KerMLExpressionEvaluator.getArguments].
     */
    private fun getExpressionArguments(expression: MDMObject): List<MDMObject> {
        val argument = engine.getPropertyValue(expression, "argument")
        return when (argument) {
            is List<*> -> argument.filterIsInstance<MDMObject>()
            is MDMObject -> listOf(argument)
            null -> {
                // Fall back to ownedFeature filtering (excluding result)
                val ownedFeatures = getOwnedFeatures(expression)
                val result = engine.getPropertyValue(expression, "result") as? MDMObject
                ownedFeatures.filter { it.id != result?.id }
            }
            else -> emptyList()
        }
    }

    /**
     * Navigate ResultExpressionMembership to get the ownedResultExpression.
     */
    private fun getResultExpression(element: MDMObject): MDMObject? {
        val memberships = engine.getPropertyValue(element, "ownedFeatureMembership")
        val membershipList = when (memberships) {
            is List<*> -> memberships.filterIsInstance<MDMObject>()
            is MDMObject -> listOf(memberships)
            else -> return null
        }

        val resultMembership = membershipList.firstOrNull {
            engine.isInstanceOf(it, "ResultExpressionMembership")
        } ?: return null

        return engine.getPropertyValue(resultMembership, "ownedResultExpression") as? MDMObject
    }

    /**
     * Map KerML operators to OCL equivalents.
     */
    private fun mapOperatorToOcl(operator: String): String {
        return when (operator) {
            "==" -> "="
            "!=" -> "<>"
            else -> operator
        }
    }

    /**
     * Collect all Features referenced in a constraint/invariant.
     *
     * First tries to walk the expression tree to find FeatureReferenceExpressions,
     * then falls back to collecting owned features from the parent namespace.
     */
    private fun collectReferencedFeatures(invariant: MDMObject): List<MDMObject> {
        // Try expression tree walking first
        val resultExpr = getResultExpression(invariant)
        if (resultExpr != null) {
            val referenced = mutableListOf<MDMObject>()
            collectReferencedFeaturesFromExpression(resultExpr, referenced)
            if (referenced.isNotEmpty()) return referenced
        }

        // Fall back to collecting features from the owning namespace
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

    /**
     * Walk an expression tree to collect FeatureReferenceExpression referents.
     */
    private fun collectReferencedFeaturesFromExpression(expression: MDMObject, result: MutableList<MDMObject>) {
        when (expression.className) {
            "FeatureReferenceExpression" -> {
                val referent = engine.getPropertyValue(expression, "referent") as? MDMObject
                if (referent != null && engine.isInstanceOf(referent, "Feature")) {
                    result.add(referent)
                }
            }
            "OperatorExpression", "InvocationExpression" -> {
                for (arg in getExpressionArguments(expression)) {
                    collectReferencedFeaturesFromExpression(arg, result)
                }
            }
            else -> {
                if (engine.isInstanceOf(expression, "Expression")) {
                    val resultExpr = getResultExpression(expression)
                    if (resultExpr != null) {
                        collectReferencedFeaturesFromExpression(resultExpr, result)
                    }
                }
            }
        }
    }

    // === Navigation Helpers ===

    private fun getTypes(feature: MDMObject): List<MDMObject> {
        // Try engine first (navigates association ends), then fall back to raw property
        val types = engine.getPropertyValue(feature, "type")
            ?: feature.getProperty("type")
        return when (types) {
            is List<*> -> types.filterIsInstance<MDMObject>()
            is MDMObject -> listOf(types)
            else -> emptyList()
        }
    }

    private fun getOwnedFeatures(element: MDMObject): List<MDMObject> {
        val features = engine.getPropertyValue(element, "ownedFeature")
        return when (features) {
            is List<*> -> features.filterIsInstance<MDMObject>()
            is MDMObject -> listOf(features)
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
