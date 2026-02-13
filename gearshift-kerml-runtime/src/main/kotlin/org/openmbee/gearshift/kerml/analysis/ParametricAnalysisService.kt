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
 * Result of an impact/dependency analysis for a specific feature.
 *
 * @param featureName The feature being analyzed
 * @param affectedInvariants IDs of invariants that reference this feature
 * @param coConstrainedFeatures Names of other features that appear in the same invariants
 * @param violatedInvariants IDs of invariants violated by a proposed value (empty if no proposedValue)
 * @param stillSatisfiable Whether the system is still satisfiable with the proposed value (null if no proposedValue)
 */
data class ImpactAnalysisResult(
    val featureName: String,
    val affectedInvariants: List<String>,
    val coConstrainedFeatures: List<String>,
    val violatedInvariants: List<String> = emptyList(),
    val stillSatisfiable: Boolean? = null
)

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

    /**
     * Analyze the impact of changing a specific feature.
     *
     * Finds all invariants that reference the feature, identifies co-constrained
     * features, and optionally checks whether a proposed value would violate any
     * constraints.
     *
     * @param featureName The name of the feature to analyze
     * @param allInvariants All invariants to scan for references
     * @param proposedValue Optional new value to check against constraints
     * @return Impact analysis result
     */
    fun analyzeImpact(
        featureName: String,
        allInvariants: List<BooleanExpression>,
        proposedValue: Any? = null
    ): ImpactAnalysisResult {
        // 1. Find all invariants that reference the target feature
        val affectedInvariants = allInvariants.filter { inv ->
            collectReferencedFeatures(inv).any { f ->
                f.declaredName == featureName || f.name == featureName
            }
        }

        // 2. Collect all co-constrained features (other features in those invariants)
        val coConstrained = affectedInvariants
            .flatMap { collectReferencedFeatures(it) }
            .filter { it.declaredName != featureName && it.name != featureName }
            .mapNotNull { it.declaredName ?: it.name }
            .distinct()

        // 3. If proposedValue provided, check which invariants become violated
        if (proposedValue != null) {
            val violated = mutableListOf<String>()
            for (inv in affectedInvariants) {
                val expr = extractConstraintExpression(inv) ?: continue
                val features = collectReferencedFeatures(inv)
                val variables = features.mapNotNull { featureToVariable(it) }
                val extraConstraint = "($featureName = $proposedValue)"
                val sat = solverService.isSatisfiable(variables, listOf(expr, extraConstraint))
                if (!sat) {
                    violated.add((inv as MDMObject).id ?: "unknown")
                }
            }
            val allFeatures = affectedInvariants
                .flatMap { collectReferencedFeatures(it) }
                .distinctBy { (it as MDMObject).id }
            val allExprs = affectedInvariants.mapNotNull { extractConstraintExpression(it) }
            val overallSat = solverService.isSatisfiable(
                allFeatures.mapNotNull { featureToVariable(it) },
                allExprs + "($featureName = $proposedValue)"
            )
            return ImpactAnalysisResult(
                featureName = featureName,
                affectedInvariants = affectedInvariants.map { (it as MDMObject).id ?: "unknown" },
                coConstrainedFeatures = coConstrained,
                violatedInvariants = violated,
                stillSatisfiable = overallSat
            )
        }

        return ImpactAnalysisResult(
            featureName = featureName,
            affectedInvariants = affectedInvariants.map { (it as MDMObject).id ?: "unknown" },
            coConstrainedFeatures = coConstrained
        )
    }

    /**
     * Persist solver result assignments back to the model as FeatureValues.
     *
     * For each assignment, creates a FeatureValue with the appropriate literal
     * expression and links it to the feature via associations.
     *
     * @param assignments Map of feature name to solved value
     * @param referencedFeatures Features that were part of the constraint system
     */
    fun applySolution(
        assignments: Map<String, Any?>,
        referencedFeatures: List<Feature>
    ) {
        for ((name, value) in assignments) {
            val feature = referencedFeatures.firstOrNull {
                it.declaredName == name || it.name == name
            } ?: continue

            val featureValue = engine.createElement("FeatureValue")
            engine.setPropertyValue(featureValue, "isDefault", false)
            engine.setPropertyValue(featureValue, "isInitial", false)

            val literal = when (value) {
                is Long, is Int -> {
                    val lit = engine.createElement("LiteralInteger")
                    engine.setPropertyValue(lit, "value", (value as Number).toInt())
                    lit
                }

                is Double, is Float -> {
                    val lit = engine.createElement("LiteralRational")
                    engine.setPropertyValue(lit, "value", (value as Number).toDouble())
                    lit
                }

                is Boolean -> {
                    val lit = engine.createElement("LiteralBoolean")
                    engine.setPropertyValue(lit, "value", value)
                    lit
                }

                else -> continue
            }

            // Link FeatureValue → Expression (value)
            engine.link(featureValue.id!!, literal.id!!, "expressedValuationValueAssociation")
            // Link FeatureValue → Feature (featureWithValue)
            engine.link(featureValue.id!!, (feature as MDMObject).id!!, "valuationFeatureWithValueAssociation")
        }
    }

    // === Model Element Extraction ===

    /**
     * Convert a KerML Feature to a Z3Variable declaration.
     * Infers the Z3 sort from the Feature's typing.
     */
    internal fun featureToVariable(feature: Feature): Z3Variable? {
        val name = solverVariableName(feature) ?: return null

        val sort = inferSort(feature)

        val lowerBound = extractLowerBound(feature)
        val upperBound = extractUpperBound(feature)

        return Z3Variable(name, sort, lowerBound, upperBound)
    }

    /**
     * Infer the Z3Sort from a Feature's type.
     */
    private fun inferSort(feature: Feature): Z3Sort {
        // Use ownedTyping → FeatureTyping.type (stored forward links) instead of
        // the derived Feature.type which triggers expensive OCL evaluation.
        for (typing in feature.ownedTyping) {
            val type = typing.type
            val typeName = (type as? Element)?.declaredName
                ?: (type as? Element)?.name
                ?: (type as? MDMObject)?.className
            if (typeName != null) return sortFromTypeName(typeName)
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
                is Element -> type.declaredName ?: type.name ?: (type as MDMObject).className
                is MDMObject -> type.className
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
            expression is Expression -> {
                // Generic Expression fallback: try its result expression
                val rawResultExpr = expression.getProperty("resultExpression") as? MDMObject
                if (rawResultExpr != null) return expressionTreeToOcl(rawResultExpr)
                try {
                    val resultExpr = getResultExpression(expression as MDMObject)
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
            val name = solverVariableName(ref) ?: ref.declaredName ?: ref.name
            if (name != null) return name
        } catch (_: Exception) { /* referent may be uninitialized */
        }

        // Strategy 2: Engine navigation (derived property computation)
        // Use engine.getPropertyValue since typed access already failed above,
        // but cast result to Feature for name access.
        try {
            val referent = engine.getPropertyValue(expression as MDMObject, "referent") as? Feature
            if (referent != null) {
                return solverVariableName(referent) ?: referent.declaredName ?: referent.name
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
            // Fallback: engine navigation, cast to typed interface for name access
            try {
                val func = engine.getPropertyValue(expression as MDMObject, "function") as? Element
                func?.let { it.declaredName ?: it.name }
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

        // Strategy 2: Typed argument property (on InstantiationExpression)
        try {
            val argList = (expression as? InstantiationExpression)?.argument?.map { it as MDMObject }
            if (argList != null && argList.isNotEmpty()) return argList
        } catch (e: Exception) {
            logger.debug { "Derived argument navigation failed: ${e.message}" }
        }

        // Strategy 3: Fall back to ownedFeature filtering (excluding result)
        try {
            val ownedFeatures = (expression as Type).ownedFeature.map { it as MDMObject }
            val resultObj = try {
                (expression as Expression).result as? MDMObject
            } catch (_: Exception) {
                null
            }
            return ownedFeatures.filter { it.id != resultObj?.id }
        } catch (e: Exception) {
            logger.debug { "Owned features fallback failed: ${e.message}" }
            return emptyList()
        }
    }

    /**
     * Navigate ResultExpressionMembership to get the ownedResultExpression.
     */
    private fun getResultExpression(element: MDMObject): MDMObject? {
        val membershipList = (element as Type).ownedFeatureMembership

        val resultMembership = membershipList.firstOrNull {
            it is ResultExpressionMembership
        } ?: return null

        // ownedResultExpression is not on the typed ResultExpressionMembership interface
        return engine.getPropertyValue(resultMembership as MDMObject, "ownedResultExpression") as? MDMObject
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
    internal fun collectReferencedFeatures(constraint: BooleanExpression): List<Feature> {
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
            val owner = (obj as Element).owner
            if (owner is Type) {
                return owner.ownedFeature.filterIsInstance<Feature>()
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

            expression is Expression -> {
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

    // === Naming Helpers ===

    /**
     * Compute a unique solver variable name for a Feature.
     *
     * When the feature is owned by a named Type (Class, Behavior, etc. — but NOT a Package),
     * the name is qualified as `OwnerName__featureName` to disambiguate features with the
     * same declaredName across different classes (e.g., `Propulsion::mass` vs `Power::mass`).
     *
     * For features owned by Packages (the common case in existing tests), the plain
     * declaredName is returned to maintain backward compatibility.
     */
    internal fun solverVariableName(feature: Feature): String? {
        val featureName = feature.declaredName ?: feature.name ?: return null
        val owner = findOwningNamespace(feature)
        // Qualify with owner name when the owner is a Type (Class, Behavior, DataType, etc.)
        // but NOT when it's merely a Package (which is a Namespace, not a Type).
        if (owner is Type) {
            val ownerName = (owner as? Element)?.let { it.declaredName ?: it.name }
            if (ownerName != null) {
                return "${ownerName}__${featureName}"
            }
        }
        return featureName
    }

    /**
     * Find the owning namespace of a Feature by navigating the containment hierarchy.
     *
     * Tries multiple strategies since derived properties like `owner` may not resolve
     * at runtime depending on the OCL engine state.
     */
    private fun findOwningNamespace(feature: Feature): Element? {
        // Strategy 1: Typed owningNamespace (derived property on Element)
        try {
            val ns = (feature as Element).owningNamespace
            if (ns != null) return ns
        } catch (_: Exception) { }

        // Strategy 2: Engine property access (triggers derivation through MDMEngine)
        try {
            val ns = engine.getPropertyValue(feature as MDMObject, "owningNamespace")
            if (ns is Element) return ns
        } catch (_: Exception) { }

        // Strategy 3: Navigate through owningRelationship → owningRelatedElement
        try {
            val owningRel = engine.getPropertyValue(feature as MDMObject, "owningRelationship") as? MDMObject
            if (owningRel != null) {
                val ownerEl = engine.getPropertyValue(owningRel, "owningRelatedElement") as? Element
                if (ownerEl != null) return ownerEl
            }
        } catch (_: Exception) { }

        // Strategy 4: Typed owner property
        try {
            val o = (feature as Element).owner
            if (o != null) return o
        } catch (_: Exception) { }

        return null
    }

    // === Navigation Helpers ===

    private fun extractLowerBound(feature: Feature): Number? {
        val multiplicity = (feature as Type).multiplicity as? MultiplicityRange ?: return null
        val lower = multiplicity.lowerBound ?: return null
        if (lower is LiteralInteger) return lower.value
        return null
    }

    private fun extractUpperBound(feature: Feature): Number? {
        val multiplicity = (feature as Type).multiplicity as? MultiplicityRange ?: return null
        return try {
            val upper = multiplicity.upperBound
            if (upper is LiteralInteger) upper.value else null
        } catch (_: Exception) {
            // upperBound is non-null typed but may be unset at runtime
            null
        }
    }
}
