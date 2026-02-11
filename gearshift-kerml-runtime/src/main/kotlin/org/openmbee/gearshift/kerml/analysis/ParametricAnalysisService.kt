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
import org.openmbee.gearshift.generated.interfaces.*
import org.openmbee.mdm.framework.constraints.*
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
     * Solve a constraint system defined by BooleanExpressions.
     *
     * Derives variable declarations from Features referenced in the constraints
     * and constraint expressions from the BooleanExpressions, then solves for
     * satisfying assignments.
     *
     * @param constraints List of BooleanExpressions (Invariant, ConstraintUsage, etc.)
     * @return Solver result with variable assignments
     */
    fun solveConstraints(
        constraints: List<BooleanExpression>
    ): SolverResult {
        val constraintExprs = constraints.mapNotNull { extractConstraintExpression(it) }

        if (constraintExprs.isEmpty()) {
            return SolverResult(satisfiable = true, assignments = emptyMap())
        }

        val allFeatures = mutableSetOf<Feature>()
        for (constraint in constraints) {
            allFeatures.addAll(collectReferencedFeatures(constraint))
        }
        val variables = allFeatures.mapNotNull { featureToVariable(it) }

        if (variables.isEmpty()) {
            return SolverResult(satisfiable = true, assignments = emptyMap())
        }

        logger.info { "Solving constraints: ${variables.size} variables, ${constraintExprs.size} constraints" }
        return solverService.solve(variables, constraintExprs)
    }

    /**
     * Check if a set of requirements (constraints) are mutually consistent.
     *
     * This is useful for requirement conflict detection: given a set of
     * requirements expressed as BooleanExpressions, are they all satisfiable together?
     *
     * @param constraints List of BooleanExpressions
     * @return Conflict result indicating consistency and any conflicts
     */
    fun checkRequirementConsistency(constraints: List<BooleanExpression>): ConflictResult {
        logger.info { "Checking consistency of ${constraints.size} constraints" }

        val constraintExprs = constraints.mapNotNull { extractConstraintExpression(it) }
        if (constraintExprs.isEmpty()) {
            return ConflictResult(consistent = true)
        }

        // Collect all variables referenced in the constraints
        val allFeatures = mutableSetOf<Feature>()
        for (constraint in constraints) {
            allFeatures.addAll(collectReferencedFeatures(constraint))
        }

        val variables = allFeatures.mapNotNull { featureToVariable(it) }

        return solverService.findConflicts(variables, constraintExprs)
    }

    /**
     * Perform a trade study: optimize an objective function subject to constraints.
     *
     * @param designVariables Features representing design variables
     * @param constraints BooleanExpressions defining the feasible region
     * @param objective OCL expression for the objective function
     * @param minimize true to minimize, false to maximize
     * @return Optimization result with optimal assignments
     */
    fun tradeStudy(
        designVariables: List<Feature>,
        constraints: List<BooleanExpression>,
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
    internal fun featureToVariable(feature: Feature): Z3Variable? {
        val name = feature.declaredName ?: feature.name ?: return null

        val sort = inferSort(feature)

        val obj = feature as MDMObject
        val lowerBound = extractLowerBound(obj)
        val upperBound = extractUpperBound(obj)

        return Z3Variable(name, sort, lowerBound, upperBound)
    }

    /**
     * Infer the Z3Sort from a Feature's type.
     */
    private fun inferSort(feature: Feature): Z3Sort {
        // Try typed access first (navigates association graph)
        for (type in feature.type) {
            val typeName = type.declaredName ?: type.name ?: (type as MDMObject).className
            return sortFromTypeName(typeName)
        }

        // Fallback: raw property access (for hand-built tests where type is set via setProperty)
        val rawTypes = (feature as MDMObject).getProperty("type")
        val typeList = when (rawTypes) {
            is List<*> -> rawTypes.filterNotNull()
            is MDMObject -> listOf(rawTypes)
            else -> emptyList()
        }
        for (type in typeList) {
            val typeName = when (type) {
                is Type -> type.declaredName ?: type.name ?: (type as MDMObject).className
                is MDMObject -> engine.getPropertyValue(type, "declaredName") as? String
                    ?: engine.getPropertyValue(type, "name") as? String
                    ?: type.className

                else -> continue
            }
            return sortFromTypeName(typeName)
        }

        // Default to Real if no type information
        return Z3Sort.REAL
    }

    private fun sortFromTypeName(typeName: String): Z3Sort {
        return when (typeName.lowercase()) {
            "integer", "natural", "positive" -> Z3Sort.INT
            "real", "rational" -> Z3Sort.REAL
            "boolean" -> Z3Sort.BOOL
            else -> Z3Sort.REAL
        }
    }

    /**
     * Extract a constraint expression from a BooleanExpression element.
     *
     * Uses a multi-strategy fallthrough:
     * 1. Raw `resultExpression` property (stored during parsing for direct access)
     * 2. Walk the expression tree via ResultExpressionMembership (derived navigation)
     * 3. Legacy: string `expression` property (backward compat with hand-built tests)
     */
    internal fun extractConstraintExpression(constraint: BooleanExpression): String? {
        val obj = constraint as MDMObject

        // Strategy 1: Raw resultExpression property (set during parsing, bypasses derived navigation)
        val rawResultExpr = obj.getProperty("resultExpression") as? MDMObject
        if (rawResultExpr != null) {
            val ocl = expressionTreeToOcl(rawResultExpr)
            if (ocl != null) return ocl
        }

        // Strategy 2: Walk expression tree via derived ResultExpressionMembership
        try {
            val resultExpr = getResultExpression(obj)
            if (resultExpr != null) {
                val ocl = expressionTreeToOcl(resultExpr)
                if (ocl != null) return ocl
            }
        } catch (e: Exception) {
            logger.debug { "Derived navigation failed for ${obj.id}: ${e.message}" }
        }

        // Strategy 3: Legacy string property (backward compat with hand-built tests)
        // Uses MDMObject.getProperty() directly since "expression" is not a metamodel
        // attribute — it's an ad-hoc property set directly on the object.
        val expression = obj.getProperty("expression") as? String
        if (expression != null) return expression

        return null
    }

    // === Expression Tree → OCL Bridge ===

    /**
     * Convert a KerML Expression MDMObject tree to an OCL string for the solver.
     *
     * Walks the MDMObject expression tree recursively, dispatching on typed
     * interface checks, following the same pattern as [KerMLExpressionEvaluator.evaluate].
     */
    internal fun expressionTreeToOcl(expression: MDMObject): String? {
        return when {
            expression is LiteralInteger -> expression.value.toString()
            expression is LiteralRational -> expression.value.toString()
            expression is LiteralBoolean -> expression.value.toString()
            expression is LiteralString -> "'${expression.value}'"
            expression is LiteralInfinity -> "*"
            expression is NullExpression -> "null"
            expression is OperatorExpression -> operatorExpressionToOcl(expression)
            expression is FeatureReferenceExpression -> featureReferenceToOcl(expression)
            expression is InvocationExpression -> invocationExpressionToOcl(expression)
            engine.isInstanceOf(expression, "Expression") -> {
                // Generic Expression fallback: try its result expression
                val rawResultExpr = expression.getProperty("resultExpression") as? MDMObject
                if (rawResultExpr != null) return expressionTreeToOcl(rawResultExpr)
                try {
                    val resultExpr = getResultExpression(expression)
                    if (resultExpr != null) expressionTreeToOcl(resultExpr) else null
                } catch (_: Exception) {
                    null
                }
            }

            else -> null
        }
    }

    private fun operatorExpressionToOcl(expression: OperatorExpression): String? {
        val operator = expression.operator
        val args = getExpressionArguments(expression as MDMObject)

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

    private fun featureReferenceToOcl(expression: FeatureReferenceExpression): String? {
        // Strategy 1: Typed property access (set by ReferenceResolver via reflection)
        try {
            val ref = expression.referent
            val name = ref.declaredName ?: ref.name
            if (name != null) return name
        } catch (_: Exception) { /* referent may be uninitialized */
        }

        // Strategy 2: Engine navigation (derived property computation)
        try {
            val referent = engine.getPropertyValue(expression as MDMObject, "referent") as? MDMObject
            if (referent != null) {
                return engine.getPropertyValue(referent, "declaredName") as? String
                    ?: engine.getPropertyValue(referent, "name") as? String
            }
        } catch (e: Exception) {
            logger.debug { "Derived referent navigation failed: ${e.message}" }
        }

        return null
    }

    private fun invocationExpressionToOcl(expression: InvocationExpression): String? {
        val funcName = try {
            val func = expression.function
            func?.declaredName ?: func?.name
        } catch (_: Exception) {
            // Fallback to engine navigation
            try {
                val function = engine.getPropertyValue(expression as MDMObject, "function") as? MDMObject
                function?.let {
                    engine.getPropertyValue(it, "declaredName") as? String
                        ?: engine.getPropertyValue(it, "name") as? String
                }
            } catch (e: Exception) {
                logger.debug { "Derived function navigation failed: ${e.message}" }
                null
            }
        } ?: return null

        val args = getExpressionArguments(expression as MDMObject)
        val argStrings = args.mapNotNull { expressionTreeToOcl(it) }
        if (argStrings.size != args.size) return null

        return "$funcName(${argStrings.joinToString(", ")})"
    }

    // === Expression Navigation Helpers ===

    /**
     * Get expression arguments from an Expression MDMObject.
     *
     * First tries the raw `_arguments` property. If that returns null,
     * falls back to derived `argument` association end, then to `ownedFeature` filtering,
     * matching the pattern in [KerMLExpressionEvaluator.getArguments].
     */
    private fun getExpressionArguments(expression: MDMObject): List<MDMObject> {
        // Strategy 1: Raw _arguments property (stored during parsing by OwnedExpressionVisitor)
        val rawArgs = expression.getProperty("_arguments")
        if (rawArgs is List<*>) {
            val args = rawArgs.filterIsInstance<MDMObject>()
            if (args.isNotEmpty()) return args
        }

        // Strategy 2: Try derived `argument` association end
        try {
            val argument = engine.getPropertyValue(expression, "argument")
            val argList = when (argument) {
                is List<*> -> argument.filterIsInstance<MDMObject>()
                is MDMObject -> listOf(argument)
                else -> emptyList()
            }
            if (argList.isNotEmpty()) return argList
        } catch (e: Exception) {
            logger.debug { "Derived argument navigation failed: ${e.message}" }
        }

        // Strategy 3: Fall back to ownedFeature filtering (excluding result)
        try {
            val features = engine.getPropertyValue(expression, "ownedFeature")
            val ownedFeatures = when (features) {
                is List<*> -> features.filterIsInstance<MDMObject>()
                is MDMObject -> listOf(features)
                else -> emptyList()
            }
            val result = engine.getPropertyValue(expression, "result") as? MDMObject
            return ownedFeatures.filter { it.id != result?.id }
        } catch (e: Exception) {
            logger.debug { "Owned features fallback failed: ${e.message}" }
            return emptyList()
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
     * Collect all Features referenced in a constraint/BooleanExpression.
     *
     * First tries to walk the expression tree to find FeatureReferenceExpressions,
     * then falls back to collecting owned features from the parent namespace.
     */
    private fun collectReferencedFeatures(constraint: BooleanExpression): List<Feature> {
        val obj = constraint as MDMObject

        // Try raw resultExpression property first (set during parsing)
        val rawResultExpr = obj.getProperty("resultExpression") as? MDMObject
        if (rawResultExpr != null) {
            val referenced = mutableListOf<Feature>()
            collectReferencedFeaturesFromExpression(rawResultExpr, referenced)
            if (referenced.isNotEmpty()) return referenced
        }

        // Try derived expression tree walking
        try {
            val resultExpr = getResultExpression(obj)
            if (resultExpr != null) {
                val referenced = mutableListOf<Feature>()
                collectReferencedFeaturesFromExpression(resultExpr, referenced)
                if (referenced.isNotEmpty()) return referenced
            }
        } catch (e: Exception) {
            logger.debug { "Derived expression navigation failed: ${e.message}" }
        }

        // Fall back to collecting features from the owning namespace
        try {
            val owner = engine.getPropertyValue(obj, "owner") as? MDMObject
            if (owner != null) {
                val features = engine.getPropertyValue(owner, "ownedFeature")
                return when (features) {
                    is List<*> -> features.filterIsInstance<Feature>()
                    is Feature -> listOf(features)
                    else -> emptyList()
                }
            }
        } catch (e: Exception) {
            logger.debug { "Owner/feature navigation failed: ${e.message}" }
        }
        return emptyList()
    }

    /**
     * Walk an expression tree to collect FeatureReferenceExpression referents.
     */
    private fun collectReferencedFeaturesFromExpression(expression: MDMObject, result: MutableList<Feature>) {
        when {
            expression is FeatureReferenceExpression -> {
                try {
                    result.add(expression.referent)
                } catch (_: Exception) {
                    // Fallback to engine navigation
                    try {
                        val referent = engine.getPropertyValue(expression, "referent")
                        if (referent is Feature) result.add(referent)
                    } catch (_: Exception) {
                    }
                }
            }

            expression is OperatorExpression || expression is InvocationExpression -> {
                for (arg in getExpressionArguments(expression)) {
                    collectReferencedFeaturesFromExpression(arg, result)
                }
            }

            engine.isInstanceOf(expression, "Expression") -> {
                val rawResultExpr = expression.getProperty("resultExpression") as? MDMObject
                if (rawResultExpr != null) {
                    collectReferencedFeaturesFromExpression(rawResultExpr, result)
                } else {
                    try {
                        val resultExpr = getResultExpression(expression)
                        if (resultExpr != null) {
                            collectReferencedFeaturesFromExpression(resultExpr, result)
                        }
                    } catch (_: Exception) {
                    }
                }
            }
        }
    }

    // === Navigation Helpers ===

    private fun extractLowerBound(feature: MDMObject): Number? {
        val multiplicity = engine.getPropertyValue(feature, "multiplicity") as? MDMObject
        if (multiplicity != null) {
            val lower = engine.getPropertyValue(multiplicity, "lowerBound")
            if (lower is LiteralInteger) {
                return lower.value
            }
            if (lower is MDMObject && engine.isInstanceOf(lower, "LiteralInteger")) {
                return engine.getPropertyValue(lower, "value") as? Number
            }
        }
        return null
    }

    private fun extractUpperBound(feature: MDMObject): Number? {
        val multiplicity = engine.getPropertyValue(feature, "multiplicity") as? MDMObject
        if (multiplicity != null) {
            val upper = engine.getPropertyValue(multiplicity, "upperBound")
            if (upper is LiteralInteger) {
                return upper.value
            }
            if (upper is MDMObject && engine.isInstanceOf(upper, "LiteralInteger")) {
                return engine.getPropertyValue(upper, "value") as? Number
            }
        }
        return null
    }
}
