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
package org.openmbee.gearshift.kerml.eval

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.gearshift.generated.interfaces.Behavior
import org.openmbee.gearshift.generated.interfaces.Element
import org.openmbee.gearshift.generated.interfaces.Expression
import org.openmbee.gearshift.generated.interfaces.FeatureChainExpression
import org.openmbee.gearshift.generated.interfaces.FeatureReferenceExpression
import org.openmbee.gearshift.generated.interfaces.FeatureValue
import org.openmbee.gearshift.generated.interfaces.InvocationExpression
import org.openmbee.gearshift.generated.interfaces.MetadataAccessExpression
import org.openmbee.gearshift.generated.interfaces.OperatorExpression
import org.openmbee.gearshift.generated.interfaces.ResultExpressionMembership
import org.openmbee.gearshift.generated.interfaces.Type
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MDMObject

private val logger = KotlinLogging.logger {}

/**
 * Evaluates KerML Expression model elements — walks a tree of MDMObject instances
 * (LiteralExpression, OperatorExpression, InvocationExpression, etc.) and computes values.
 *
 * This is distinct from OCL evaluation: OCL evaluates text-based constraint expressions,
 * while this evaluator walks the KerML model element tree itself.
 *
 * Dispatches on MDMObject.className to determine evaluation strategy:
 * - LiteralExpression → returns Sequence{self}
 * - OperatorExpression → evaluate arguments, apply operator via KernelFunctionLibrary
 * - InvocationExpression → evaluate arguments, bind to parameters, apply function body
 * - FeatureReferenceExpression → resolve referent Feature, return its value
 * - FeatureChainExpression → evaluate source, navigate target feature on result
 * - SelectExpression / CollectExpression / IndexExpression → collection operations
 * - NullExpression → returns empty Sequence
 */
class KerMLExpressionEvaluator(
    private val engine: MDMEngine,
    private val functionLibrary: KernelFunctionLibrary
) {
    /**
     * Evaluate a KerML Expression model element in the context of a target element.
     *
     * @param expression The Expression MDMObject to evaluate
     * @param target The context element for resolving Feature names and testing classification
     * @return A list of MDMObject values (the KerML "Sequence" result)
     */
    fun evaluate(expression: MDMObject, target: MDMObject): List<MDMObject> {
        logger.debug { "Evaluating ${expression.className} (id=${expression.id}) with target ${target.className}" }

        return when {
            // Literal expressions (concrete literals and base class)
            isLiteralExpression(expression) -> evaluateLiteral(expression)

            // Null expression
            expression.className == "NullExpression" -> emptyList()

            // Feature chain expression (operator='.')
            expression.className == "FeatureChainExpression" -> evaluateFeatureChain(expression, target)

            // Select expression (operator='select')
            expression.className == "SelectExpression" -> evaluateSelect(expression, target)

            // Collect expression (operator='collect')
            expression.className == "CollectExpression" -> evaluateCollect(expression, target)

            // Index expression (operator='#')
            expression.className == "IndexExpression" -> evaluateIndex(expression, target)

            // Operator expression (general case - must be after specific operator subclasses)
            expression.className == "OperatorExpression" -> evaluateOperator(expression, target)

            // Invocation expression
            expression.className == "InvocationExpression" -> evaluateInvocation(expression, target)

            // Feature reference expression
            expression.className == "FeatureReferenceExpression" -> evaluateFeatureReference(expression, target)

            // Metadata access expression
            expression.className == "MetadataAccessExpression" -> evaluateMetadataAccess(expression, target)

            // Boolean expression (treated like a general expression)
            expression.className == "BooleanExpression" -> evaluateGenericExpression(expression, target)

            // Constructor expression
            expression.className == "ConstructorExpression" -> evaluateConstructor(expression, target)

            // Generic Expression base case — delegate to result expression
            expression is Expression -> evaluateGenericExpression(expression, target)

            else -> {
                logger.warn { "Unknown expression type: ${expression.className}" }
                emptyList()
            }
        }
    }

    /**
     * Check whether this Expression is model-level evaluable.
     *
     * An expression is model-level evaluable if it can be fully evaluated at the model
     * level without requiring runtime execution context. This mirrors the KerML
     * modelLevelEvaluable() operation.
     *
     * @param expression The Expression to check
     * @param visited Set of already-visited elements (for circular reference detection)
     * @return true if the expression can be evaluated at model level
     */
    fun isModelLevelEvaluable(expression: MDMObject, visited: Set<MDMObject> = emptySet()): Boolean {
        if (expression in visited) return false

        return when {
            isLiteralExpression(expression) -> true
            expression.className == "NullExpression" -> true
            expression.className == "MetadataAccessExpression" -> true

            expression is FeatureReferenceExpression -> {
                val referent = expression.referent as? MDMObject
                if (referent == null || referent in visited) return false
                (referent as? Type)?.ownedFeature?.isEmpty() ?: true
            }

            expression is InvocationExpression -> {
                val newVisited = visited + expression
                val arguments = getArguments(expression)
                arguments.all { arg ->
                    if (arg is Expression) {
                        isModelLevelEvaluable(arg, newVisited)
                    } else {
                        true
                    }
                }
            }

            expression is Expression -> {
                val resultExpression = getResultExpression(expression)
                if (resultExpression != null) {
                    isModelLevelEvaluable(resultExpression, visited + expression)
                } else {
                    true
                }
            }

            else -> false
        }
    }

    // === Evaluation Dispatch Methods ===

    private fun evaluateLiteral(expression: MDMObject): List<MDMObject> {
        // A LiteralExpression evaluates to itself (Sequence{self})
        return listOf(expression)
    }

    private fun evaluateOperator(expression: MDMObject, target: MDMObject): List<MDMObject> {
        val operator = (expression as? OperatorExpression)?.operator
        if (operator.isNullOrEmpty()) {
            logger.warn { "OperatorExpression has no operator" }
            return emptyList()
        }

        val arguments = getArguments(expression)
        val evaluatedArgs = arguments.map { arg ->
            if (arg is Expression) {
                val result = evaluate(arg, target)
                if (result.size == 1) result.first() else result
            } else {
                arg
            }
        }

        val funcResult = functionLibrary.apply(operator, evaluatedArgs)
        return when (funcResult) {
            is FunctionResult.LiteralElement -> listOf(funcResult.element)
            is FunctionResult.Value -> {
                when (val v = funcResult.value) {
                    null -> emptyList()
                    is MDMObject -> listOf(v)
                    is List<*> -> v.filterIsInstance<MDMObject>()
                    else -> emptyList()
                }
            }
            null -> {
                logger.warn { "Unknown operator: $operator" }
                emptyList()
            }
        }
    }

    private fun evaluateInvocation(expression: MDMObject, target: MDMObject): List<MDMObject> {
        // Check for arrow-operation function name (set by parser for source->funcName(...))
        val functionName = expression.getProperty("_functionName") as? String

        // If the function library has a native implementation, dispatch directly
        if (functionName != null && functionLibrary.hasOperator(functionName)) {
            val arguments = getArguments(expression)
            val evaluatedArgs = arguments.map { arg ->
                if (arg is Expression) {
                    val result = evaluate(arg, target)
                    if (result.size == 1) result.first() else result
                } else {
                    arg
                }
            }
            val funcResult = functionLibrary.apply(functionName, evaluatedArgs)
            return when (funcResult) {
                is FunctionResult.LiteralElement -> listOf(funcResult.element)
                is FunctionResult.Value -> {
                    when (val v = funcResult.value) {
                        null -> emptyList()
                        is MDMObject -> listOf(v)
                        is List<*> -> v.filterIsInstance<MDMObject>()
                        else -> emptyList()
                    }
                }
                null -> {
                    logger.warn { "Function library returned null for: $functionName" }
                    emptyList()
                }
            }
        }

        val function = (expression as? Expression)?.function as? MDMObject
        if (function == null) {
            logger.warn { "InvocationExpression has no function${if (functionName != null) " (name=$functionName)" else ""}" }
            return emptyList()
        }

        val arguments = getArguments(expression)
        val evaluatedArgs = arguments.map { arg ->
            if (arg is Expression) {
                evaluate(arg, target)
            } else {
                listOf(arg)
            }
        }

        // Bind parameters to evaluated arguments
        val parameters = (function as? Behavior)?.parameter ?: emptyList()
        val bindings = mutableMapOf<String, Any?>()
        parameters.forEachIndexed { index, param ->
            val paramName = (param as? Element)?.declaredName ?: (param as? Element)?.name
            if (paramName != null && index < evaluatedArgs.size) {
                bindings[paramName] = if (evaluatedArgs[index].size == 1) {
                    evaluatedArgs[index].first()
                } else {
                    evaluatedArgs[index]
                }
            }
        }

        val resultExpr = getResultExpression(function)
        return if (resultExpr != null) {
            evaluate(resultExpr, target)
        } else {
            emptyList()
        }
    }

    private fun evaluateFeatureReference(expression: MDMObject, target: MDMObject): List<MDMObject> {
        val referent = (expression as? FeatureReferenceExpression)?.referent as? MDMObject
        if (referent == null) {
            logger.warn { "FeatureReferenceExpression has no referent" }
            return emptyList()
        }

        // If referent has a value (e.g., is a Feature with a FeatureValue), evaluate it
        val featureValue = (referent as? Element)?.ownedRelationship
            ?.filterIsInstance<FeatureValue>()?.firstOrNull()
        if (featureValue != null) {
            val valueExpr = featureValue.value as? MDMObject
            if (valueExpr != null && valueExpr is Expression) {
                return evaluate(valueExpr, target)
            }
        }

        // FeatureReferenceExpression resolves to its referent
        return listOf(referent)
    }

    private fun evaluateFeatureChain(expression: MDMObject, target: MDMObject): List<MDMObject> {
        val arguments = getArguments(expression)
        if (arguments.isEmpty()) {
            logger.warn { "FeatureChainExpression requires at least 1 argument (source)" }
            return emptyList()
        }

        val sourceArg = arguments[0]
        val sourceResults = if (sourceArg is Expression) {
            evaluate(sourceArg, target)
        } else {
            listOf(sourceArg)
        }

        // Resolve target feature name: try typed interface, then raw property fallback
        val featureName = run {
            val targetFeature = (expression as? FeatureChainExpression)?.targetFeature as? MDMObject
            if (targetFeature != null) {
                (targetFeature as? Element)?.declaredName ?: (targetFeature as? Element)?.name
            } else {
                expression.getProperty("_targetFeatureName") as? String
            }
        }
        if (featureName == null) {
            logger.warn { "FeatureChainExpression has no target feature name" }
            return emptyList()
        }

        return sourceResults.flatMap { source ->
            val value = engine.getPropertyValue(source, featureName)
            when (value) {
                null -> emptyList()
                is MDMObject -> listOf(value)
                is List<*> -> value.filterIsInstance<MDMObject>()
                else -> emptyList()
            }
        }
    }

    private fun evaluateSelect(expression: MDMObject, target: MDMObject): List<MDMObject> {
        val arguments = getArguments(expression)
        if (arguments.size < 2) return emptyList()

        val sourceResults = if (arguments[0] is Expression) {
            evaluate(arguments[0], target)
        } else {
            listOf(arguments[0])
        }

        val predicate = arguments[1]
        if (predicate !is Expression) return sourceResults

        return sourceResults.filter { element ->
            val predicateResult = evaluate(predicate, element)
            predicateResult.any { functionLibrary.extractBoolean(it) == true }
        }
    }

    private fun evaluateCollect(expression: MDMObject, target: MDMObject): List<MDMObject> {
        val arguments = getArguments(expression)
        if (arguments.size < 2) return emptyList()

        val sourceResults = if (arguments[0] is Expression) {
            evaluate(arguments[0], target)
        } else {
            listOf(arguments[0])
        }

        val body = arguments[1]
        if (body !is Expression) return sourceResults

        return sourceResults.flatMap { element ->
            evaluate(body, element)
        }
    }

    private fun evaluateIndex(expression: MDMObject, target: MDMObject): List<MDMObject> {
        val arguments = getArguments(expression)
        if (arguments.size < 2) return emptyList()

        val sourceResults = if (arguments[0] is Expression) {
            evaluate(arguments[0], target)
        } else {
            listOf(arguments[0])
        }

        val indexResult = if (arguments[1] is Expression) {
            evaluate(arguments[1], target)
        } else {
            listOf(arguments[1])
        }

        val index = indexResult.firstOrNull()?.let { functionLibrary.extractNumber(it)?.toInt() }
        if (index == null) return emptyList()

        // KerML indexing is 1-based
        val zeroBasedIndex = index - 1
        return if (zeroBasedIndex in sourceResults.indices) {
            listOf(sourceResults[zeroBasedIndex])
        } else {
            emptyList()
        }
    }

    private fun evaluateMetadataAccess(expression: MDMObject, target: MDMObject): List<MDMObject> {
        val referencedElement = (expression as? MetadataAccessExpression)?.referencedElement
        if (referencedElement == null) {
            logger.warn { "MetadataAccessExpression has no referenced element" }
            return emptyList()
        }

        return referencedElement.ownedAnnotation.filterIsInstance<MDMObject>()
    }

    private fun evaluateConstructor(expression: MDMObject, target: MDMObject): List<MDMObject> {
        val instantiatedType = (expression as? org.openmbee.gearshift.generated.interfaces.InstantiationExpression)
            ?.instantiatedType as? MDMObject
        if (instantiatedType == null) {
            logger.warn { "ConstructorExpression has no instantiated type" }
            return emptyList()
        }

        val typeName = (instantiatedType as? Element)?.declaredName
            ?: (instantiatedType as? Element)?.name
            ?: instantiatedType.className

        val instance = engine.createElement(typeName)

        val arguments = getArguments(expression)
        for (arg in arguments) {
            if (arg is Expression) {
                val result = evaluate(arg, target)
                val featureName = (arg as? Element)?.declaredName
                if (featureName != null && result.isNotEmpty()) {
                    engine.setPropertyValue(instance, featureName, result.first())
                }
            }
        }

        return listOf(instance)
    }

    private fun evaluateGenericExpression(expression: MDMObject, target: MDMObject): List<MDMObject> {
        val resultExpr = getResultExpression(expression)
        return if (resultExpr != null) {
            evaluate(resultExpr, target)
        } else {
            emptyList()
        }
    }

    // === Helper Methods ===

    private fun isLiteralExpression(expression: MDMObject): Boolean {
        return expression.className in LITERAL_EXPRESSION_CLASSES
    }

    /**
     * Get the arguments of an InvocationExpression or OperatorExpression.
     *
     * Uses the `_arguments` parser shortcut first, then falls back to typed navigation.
     */
    private fun getArguments(expression: MDMObject): List<MDMObject> {
        // Parser shortcut: _arguments stored directly by OwnedExpressionVisitor
        val rawArgs = expression.getProperty("_arguments")
        if (rawArgs is List<*>) {
            val args = rawArgs.filterIsInstance<MDMObject>()
            if (args.isNotEmpty()) return args
        }

        // Typed navigation: ownedFeature minus result
        val typed = expression as? Type ?: return emptyList()
        val result = (expression as? Expression)?.result as? MDMObject
        return typed.ownedFeature.filterIsInstance<MDMObject>().filter { it.id != result?.id }
    }

    /**
     * Get the result expression (via ResultExpressionMembership).
     *
     * Uses the `resultExpression` parser shortcut first, then falls back to typed navigation.
     */
    private fun getResultExpression(element: MDMObject): MDMObject? {
        // Parser shortcut: resultExpression stored directly by ExpressionVisitor
        val rawResultExpr = element.getProperty("resultExpression") as? MDMObject
        if (rawResultExpr != null) return rawResultExpr

        // Typed navigation via ownedFeatureMembership → ResultExpressionMembership
        val resultMembership = (element as? Type)?.ownedFeatureMembership
            ?.filterIsInstance<ResultExpressionMembership>()
            ?.firstOrNull() ?: return null

        return resultMembership.ownedMemberFeature as? MDMObject
    }

    companion object {
        private val LITERAL_EXPRESSION_CLASSES = setOf(
            "LiteralExpression",
            "LiteralBoolean",
            "LiteralInteger",
            "LiteralRational",
            "LiteralString",
            "LiteralInfinity"
        )
    }
}
