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
import org.openmbee.mdm.framework.meta.MetaOperation
import org.openmbee.mdm.framework.meta.MetaParameter
import org.openmbee.mdm.framework.meta.OperationBody
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MDMObject
import org.openmbee.mdm.framework.runtime.MetamodelRegistry

private val logger = KotlinLogging.logger {}

/**
 * Registers native `evaluate(target)` and `modelLevelEvaluable(visited)` implementations
 * for KerML Expression metaclasses, replacing the OCL operation bodies with efficient
 * native dispatch via [KerMLExpressionEvaluator].
 *
 * This handler should be installed after the metamodel registry is populated but before
 * any expression evaluation occurs. It overrides the OCL-based operation bodies defined
 * in the Expression metaclass hierarchy with native Kotlin implementations that dispatch
 * through the KerML expression evaluator.
 *
 * Usage:
 * ```kotlin
 * val engine = MDMEngine(registry)
 * val functionLibrary = KernelFunctionLibrary(engine)
 * val evaluator = KerMLExpressionEvaluator(engine, functionLibrary)
 * KerMLExpressionOperationHandler.install(engine, evaluator)
 * ```
 */
object KerMLExpressionOperationHandler {

    /**
     * The set of Expression metaclass names whose operations should be overridden
     * with native implementations.
     */
    private val EXPRESSION_CLASSES = setOf(
        "Expression",
        "LiteralExpression",
        "LiteralBoolean",
        "LiteralInteger",
        "LiteralRational",
        "LiteralString",
        "LiteralInfinity",
        "NullExpression",
        "OperatorExpression",
        "InvocationExpression",
        "FeatureReferenceExpression",
        "FeatureChainExpression",
        "SelectExpression",
        "CollectExpression",
        "IndexExpression",
        "BooleanExpression",
        "MetadataAccessExpression",
        "ConstructorExpression",
        "InstantiationExpression"
    )

    /**
     * Install native operation handlers for Expression metaclasses.
     *
     * This replaces the OCL-based `evaluate(target)` and `modelLevelEvaluable(visited)`
     * operation bodies with native implementations that use [KerMLExpressionEvaluator].
     *
     * @param engine The MDMEngine with the populated metamodel registry
     * @param evaluator The KerML expression evaluator to delegate to
     */
    fun install(engine: MDMEngine, evaluator: KerMLExpressionEvaluator) {
        val registry = engine.schema

        for (className in EXPRESSION_CLASSES) {
            val metaClass = registry.getClass(className) ?: continue

            val updatedOperations = metaClass.operations.map { operation ->
                when (operation.name) {
                    "evaluate" -> createNativeEvaluateOperation(operation, evaluator, engine)
                    "modelLevelEvaluable" -> createNativeModelLevelEvaluableOperation(operation, evaluator)
                    "checkCondition" -> createNativeCheckConditionOperation(operation, evaluator, engine)
                    else -> operation
                }
            }

            // Re-register the metaclass with updated operations
            if (updatedOperations != metaClass.operations) {
                val updatedMetaClass = metaClass.copy(operations = updatedOperations)
                registry.registerClass(updatedMetaClass)
                logger.debug { "Installed native expression operations for $className" }
            }
        }

        logger.info { "KerML expression operation handlers installed" }
    }

    /**
     * Create a native `evaluate(target: Element): Sequence(Element)` operation.
     */
    private fun createNativeEvaluateOperation(
        original: MetaOperation,
        evaluator: KerMLExpressionEvaluator,
        engine: MDMEngine
    ): MetaOperation {
        return original.copy(
            body = MetaOperation.native { element, args, eng ->
                val target = args["target"] as? MDMObject ?: element
                evaluator.evaluate(element, target)
            }
        )
    }

    /**
     * Create a native `modelLevelEvaluable(visited: Set(Feature)): Boolean` operation.
     */
    private fun createNativeModelLevelEvaluableOperation(
        original: MetaOperation,
        evaluator: KerMLExpressionEvaluator
    ): MetaOperation {
        return original.copy(
            body = MetaOperation.native { element, args, _ ->
                @Suppress("UNCHECKED_CAST")
                val visited = when (val v = args["visited"]) {
                    is Set<*> -> v as Set<MDMObject>
                    is Collection<*> -> v.filterIsInstance<MDMObject>().toSet()
                    else -> emptySet()
                }
                evaluator.isModelLevelEvaluable(element, visited)
            }
        )
    }

    /**
     * Create a native `checkCondition(target: Element): Boolean` operation.
     */
    private fun createNativeCheckConditionOperation(
        original: MetaOperation,
        evaluator: KerMLExpressionEvaluator,
        engine: MDMEngine
    ): MetaOperation {
        return original.copy(
            body = MetaOperation.native { element, args, eng ->
                val target = args["target"] as? MDMObject ?: element
                val results = evaluator.evaluate(element, target)
                if (results.size == 1) {
                    val result = results.first()
                    if (eng.isInstanceOf(result, "LiteralBoolean")) {
                        eng.getPropertyValue(result, "value") as? Boolean ?: false
                    } else {
                        false
                    }
                } else {
                    false
                }
            }
        )
    }
}
