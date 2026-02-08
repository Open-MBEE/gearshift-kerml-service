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
            engine.isInstanceOf(expression, "Expression") -> evaluateGenericExpression(expression, target)

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

            expression.className == "FeatureReferenceExpression" -> {
                val referent = getReferent(expression)
                if (referent == null || referent in visited) return false
                // The referent must not have its own complex owned features
                val ownedFeatures = getOwnedFeatures(referent)
                ownedFeatures.isEmpty()
            }

            engine.isInstanceOf(expression, "InvocationExpression") -> {
                val newVisited = visited + expression
                val arguments = getArguments(expression)
                arguments.all { arg ->
                    if (engine.isInstanceOf(arg, "Expression")) {
                        isModelLevelEvaluable(arg, newVisited)
                    } else {
                        true
                    }
                }
            }

            engine.isInstanceOf(expression, "Expression") -> {
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
        val operator = engine.getPropertyValue(expression, "operator") as? String
        if (operator == null) {
            logger.warn { "OperatorExpression has no operator" }
            return emptyList()
        }

        val arguments = getArguments(expression)
        val evaluatedArgs = arguments.map { arg ->
            if (engine.isInstanceOf(arg, "Expression")) {
                val result = evaluate(arg, target)
                if (result.size == 1) result.first() else result
            } else {
                arg
            }
        }

        val funcResult = functionLibrary.apply(operator, evaluatedArgs)
        return when (funcResult) {
            is KernelFunctionLibrary.FunctionResult.LiteralElement -> listOf(funcResult.element)
            is KernelFunctionLibrary.FunctionResult.Value -> {
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
        // Get the function being invoked
        val function = getFunction(expression)
        if (function == null) {
            logger.warn { "InvocationExpression has no function" }
            return emptyList()
        }

        // Evaluate arguments
        val arguments = getArguments(expression)
        val evaluatedArgs = arguments.map { arg ->
            if (engine.isInstanceOf(arg, "Expression")) {
                evaluate(arg, target)
            } else {
                listOf(arg)
            }
        }

        // Bind parameters to evaluated arguments
        val parameters = getParameters(function)
        val bindings = mutableMapOf<String, Any?>()
        parameters.forEachIndexed { index, param ->
            val paramName = engine.getPropertyValue(param, "declaredName") as? String
                ?: engine.getPropertyValue(param, "name") as? String
            if (paramName != null && index < evaluatedArgs.size) {
                bindings[paramName] = if (evaluatedArgs[index].size == 1) {
                    evaluatedArgs[index].first()
                } else {
                    evaluatedArgs[index]
                }
            }
        }

        // Get the result expression of the function
        val resultExpr = getResultExpression(function)
        return if (resultExpr != null) {
            evaluate(resultExpr, target)
        } else {
            emptyList()
        }
    }

    private fun evaluateFeatureReference(expression: MDMObject, target: MDMObject): List<MDMObject> {
        val referent = getReferent(expression)
        if (referent == null) {
            logger.warn { "FeatureReferenceExpression has no referent" }
            return emptyList()
        }

        // If referent has a value (e.g., is a Feature with a FeatureValue), evaluate it
        val featureValue = getFeatureValue(referent)
        if (featureValue != null) {
            val valueExpr = getValueExpression(featureValue)
            if (valueExpr != null && engine.isInstanceOf(valueExpr, "Expression")) {
                return evaluate(valueExpr, target)
            }
        }

        // Navigate the referent feature on the target
        val referentName = engine.getPropertyValue(referent, "declaredName") as? String
            ?: engine.getPropertyValue(referent, "name") as? String
        if (referentName != null) {
            val value = engine.getPropertyValue(target, referentName)
            return when (value) {
                null -> emptyList()
                is MDMObject -> listOf(value)
                is List<*> -> value.filterIsInstance<MDMObject>()
                else -> emptyList()
            }
        }

        // Fall back to returning the referent itself
        return listOf(referent)
    }

    private fun evaluateFeatureChain(expression: MDMObject, target: MDMObject): List<MDMObject> {
        // FeatureChainExpression has two arguments: source expression and target feature
        val arguments = getArguments(expression)
        if (arguments.size < 2) {
            logger.warn { "FeatureChainExpression requires at least 2 arguments" }
            return emptyList()
        }

        // Evaluate the source expression
        val sourceArg = arguments[0]
        val sourceResults = if (engine.isInstanceOf(sourceArg, "Expression")) {
            evaluate(sourceArg, target)
        } else {
            listOf(sourceArg)
        }

        // Navigate target feature on each source result
        val targetFeature = getTargetFeature(expression)
        if (targetFeature == null) {
            logger.warn { "FeatureChainExpression has no target feature" }
            return emptyList()
        }

        val featureName = engine.getPropertyValue(targetFeature, "declaredName") as? String
            ?: engine.getPropertyValue(targetFeature, "name") as? String
        if (featureName == null) {
            logger.warn { "Target feature has no name" }
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

        // First argument is the source collection
        val sourceResults = if (engine.isInstanceOf(arguments[0], "Expression")) {
            evaluate(arguments[0], target)
        } else {
            listOf(arguments[0])
        }

        // Second argument is the predicate expression
        val predicate = arguments[1]
        if (!engine.isInstanceOf(predicate, "Expression")) return sourceResults

        return sourceResults.filter { element ->
            val predicateResult = evaluate(predicate, element)
            predicateResult.any { functionLibrary.extractBoolean(it) == true }
        }
    }

    private fun evaluateCollect(expression: MDMObject, target: MDMObject): List<MDMObject> {
        val arguments = getArguments(expression)
        if (arguments.size < 2) return emptyList()

        // First argument is the source collection
        val sourceResults = if (engine.isInstanceOf(arguments[0], "Expression")) {
            evaluate(arguments[0], target)
        } else {
            listOf(arguments[0])
        }

        // Second argument is the body expression
        val body = arguments[1]
        if (!engine.isInstanceOf(body, "Expression")) return sourceResults

        return sourceResults.flatMap { element ->
            evaluate(body, element)
        }
    }

    private fun evaluateIndex(expression: MDMObject, target: MDMObject): List<MDMObject> {
        val arguments = getArguments(expression)
        if (arguments.size < 2) return emptyList()

        // First argument is the source collection
        val sourceResults = if (engine.isInstanceOf(arguments[0], "Expression")) {
            evaluate(arguments[0], target)
        } else {
            listOf(arguments[0])
        }

        // Second argument is the index expression
        val indexResult = if (engine.isInstanceOf(arguments[1], "Expression")) {
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
        val referencedElement = getReferencedElement(expression)
        if (referencedElement == null) {
            logger.warn { "MetadataAccessExpression has no referenced element" }
            return emptyList()
        }

        // Return the metadata annotations on the referenced element
        val metadata = engine.getPropertyValue(referencedElement, "ownedAnnotation")
        return when (metadata) {
            null -> emptyList()
            is MDMObject -> listOf(metadata)
            is List<*> -> metadata.filterIsInstance<MDMObject>()
            else -> emptyList()
        }
    }

    private fun evaluateConstructor(expression: MDMObject, target: MDMObject): List<MDMObject> {
        // Get the type being instantiated
        val instantiatedType = getInstantiatedType(expression)
        if (instantiatedType == null) {
            logger.warn { "ConstructorExpression has no instantiated type" }
            return emptyList()
        }

        val typeName = engine.getPropertyValue(instantiatedType, "declaredName") as? String
            ?: engine.getPropertyValue(instantiatedType, "name") as? String
            ?: instantiatedType.className

        // Create a new instance of the type
        val instance = engine.createElement(typeName)

        // Evaluate and bind arguments to the instance's features
        val arguments = getArguments(expression)
        for (arg in arguments) {
            if (engine.isInstanceOf(arg, "Expression")) {
                val result = evaluate(arg, target)
                val featureName = engine.getPropertyValue(arg, "declaredName") as? String
                if (featureName != null && result.isNotEmpty()) {
                    engine.setPropertyValue(instance, featureName, result.first())
                }
            }
        }

        return listOf(instance)
    }

    private fun evaluateGenericExpression(expression: MDMObject, target: MDMObject): List<MDMObject> {
        // Generic Expression — delegate to its result expression if it has one
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
     * Arguments are the owned Features with direction=in (excluding result).
     */
    private fun getArguments(expression: MDMObject): List<MDMObject> {
        val argument = engine.getPropertyValue(expression, "argument")
        return when (argument) {
            null -> {
                // Fall back to ownedFeature filtering
                val ownedFeatures = getOwnedFeatures(expression)
                val result = getResult(expression)
                ownedFeatures.filter { it.id != result?.id }
            }
            is List<*> -> argument.filterIsInstance<MDMObject>()
            is MDMObject -> listOf(argument)
            else -> emptyList()
        }
    }

    /**
     * Get the function that types this Expression.
     */
    private fun getFunction(expression: MDMObject): MDMObject? {
        val fn = engine.getPropertyValue(expression, "function")
        return fn as? MDMObject
    }

    /**
     * Get the result parameter of an Expression or Function.
     */
    private fun getResult(expression: MDMObject): MDMObject? {
        val result = engine.getPropertyValue(expression, "result")
        return result as? MDMObject
    }

    /**
     * Get the parameters of a Behavior or Function.
     */
    private fun getParameters(behavior: MDMObject): List<MDMObject> {
        val params = engine.getPropertyValue(behavior, "parameter")
        return when (params) {
            is List<*> -> params.filterIsInstance<MDMObject>()
            is MDMObject -> listOf(params)
            else -> emptyList()
        }
    }

    /**
     * Get the result expression (via ResultExpressionMembership).
     */
    private fun getResultExpression(element: MDMObject): MDMObject? {
        val memberships = engine.getPropertyValue(element, "ownedFeatureMembership")
        val membershipList = when (memberships) {
            is List<*> -> memberships.filterIsInstance<MDMObject>()
            is MDMObject -> listOf(memberships)
            else -> return null
        }

        // Find ResultExpressionMembership
        val resultMembership = membershipList.firstOrNull {
            engine.isInstanceOf(it, "ResultExpressionMembership")
        } ?: return null

        // Get the owned result expression
        return engine.getPropertyValue(resultMembership, "ownedResultExpression") as? MDMObject
    }

    /**
     * Get the referent of a FeatureReferenceExpression.
     */
    private fun getReferent(expression: MDMObject): MDMObject? {
        return engine.getPropertyValue(expression, "referent") as? MDMObject
    }

    /**
     * Get the target feature of a FeatureChainExpression.
     */
    private fun getTargetFeature(expression: MDMObject): MDMObject? {
        return engine.getPropertyValue(expression, "targetFeature") as? MDMObject
    }

    /**
     * Get the referenced element of a MetadataAccessExpression.
     */
    private fun getReferencedElement(expression: MDMObject): MDMObject? {
        return engine.getPropertyValue(expression, "referencedElement") as? MDMObject
    }

    /**
     * Get the instantiated type of an InstantiationExpression.
     */
    private fun getInstantiatedType(expression: MDMObject): MDMObject? {
        return engine.getPropertyValue(expression, "instantiatedType") as? MDMObject
    }

    /**
     * Get the FeatureValue of a Feature (if any).
     */
    private fun getFeatureValue(feature: MDMObject): MDMObject? {
        val valuation = engine.getPropertyValue(feature, "valuation")
        return valuation as? MDMObject
    }

    /**
     * Get the value expression from a FeatureValue.
     */
    private fun getValueExpression(featureValue: MDMObject): MDMObject? {
        return engine.getPropertyValue(featureValue, "value") as? MDMObject
    }

    /**
     * Get owned features of an element.
     */
    private fun getOwnedFeatures(element: MDMObject): List<MDMObject> {
        val features = engine.getPropertyValue(element, "ownedFeature")
        return when (features) {
            is List<*> -> features.filterIsInstance<MDMObject>()
            is MDMObject -> listOf(features)
            else -> emptyList()
        }
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
