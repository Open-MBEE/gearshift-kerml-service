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
package org.openmbee.mdm.framework.constraints

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.mdm.framework.runtime.MDMObject
import org.openmbee.mdm.framework.runtime.MetamodelRegistry
import org.openmbee.mdm.framework.meta.MetaProperty

private val logger = KotlinLogging.logger {}

/**
 * Engine for evaluating constraints on model instances.
 *
 * Provides methods to:
 * - Evaluate derived properties
 * - Validate instances against constraints
 * - Compute derived association ends
 *
 * The engine uses a ConstraintRegistry to look up evaluators and an
 * EngineAccessor to traverse the model graph.
 */
class ConstraintEngine(
    val registry: ConstraintRegistry,
    val metamodelRegistry: MetamodelRegistry,
    private val engineAccessor: EngineAccessor
) {
    init {
        // Set up superclass resolver for inheritance-based evaluator lookup
        registry.setSuperclassResolver { className ->
            metamodelRegistry.getClass(className)?.superclasses ?: emptyList()
        }
    }

    /**
     * Evaluate a derived property on an instance.
     *
     * @param instance The instance to evaluate
     * @param instanceId The instance ID
     * @param property The derived property metadata
     * @return The computed property value, or null if no evaluator found
     */
    fun evaluateDerivedProperty(
        instance: MDMObject,
        instanceId: String,
        property: MetaProperty
    ): Any? {
        if (!property.isDerived) {
            logger.warn { "Property ${property.name} is not marked as derived" }
            return null
        }

        val evaluator = registry.getDerivedPropertyEvaluator(instance.className, property.name)
        if (evaluator == null) {
            logger.debug { "No evaluator registered for derived property ${instance.className}::${property.name}" }
            return null
        }

        val context = createContext(instance, instanceId)
        return try {
            val result = evaluator.evaluate(context)
            logger.debug { "Evaluated derived property ${property.name} = $result" }
            result
        } catch (e: Exception) {
            logger.error(e) { "Error evaluating derived property ${instance.className}::${property.name}" }
            null
        }
    }

    /**
     * Evaluate a derived property by name.
     */
    fun evaluateDerivedProperty(
        instance: MDMObject,
        instanceId: String,
        propertyName: String
    ): Any? {
        val evaluator = registry.getDerivedPropertyEvaluator(instance.className, propertyName)
            ?: return null

        val context = createContext(instance, instanceId)
        return evaluator.evaluate(context)
    }

    /**
     * Validate an instance against all registered constraints for its class.
     *
     * @param instance The instance to validate
     * @param instanceId The instance ID
     * @return Validation results
     */
    fun validateInstance(instance: MDMObject, instanceId: String): ValidationResults {
        val context = createContext(instance, instanceId)
        val results = mutableListOf<ValidationResult>()

        // Get all constraints for this class (including inherited)
        val constraints = registry.getValidationConstraints(instance.className)

        constraints.forEach { (constraintName, evaluator) ->
            try {
                val result = evaluator.validate(context)
                results.add(result.copy(constraintName = constraintName))
            } catch (e: Exception) {
                logger.error(e) { "Error validating constraint $constraintName on ${instance.className}" }
                results.add(
                    ValidationResult.invalid(
                        "Error evaluating constraint: ${e.message}",
                        constraintName
                    )
                )
            }
        }

        // If no constraints, return valid
        if (results.isEmpty()) {
            return ValidationResults.valid()
        }

        return ValidationResults.fromResults(results)
    }

    /**
     * Validate an instance against a specific constraint.
     *
     * @param instance The instance to validate
     * @param instanceId The instance ID
     * @param constraintName The name of the constraint to check
     * @return Validation result
     */
    fun validateConstraint(
        instance: MDMObject,
        instanceId: String,
        constraintName: String
    ): ValidationResult {
        val evaluator = registry.getValidationConstraint(instance.className, constraintName)
            ?: return ValidationResult.invalid(
                "Constraint not found: $constraintName",
                constraintName
            )

        val context = createContext(instance, instanceId)
        return try {
            evaluator.validate(context).copy(constraintName = constraintName)
        } catch (e: Exception) {
            logger.error(e) { "Error validating constraint $constraintName" }
            ValidationResult.invalid("Error: ${e.message}", constraintName)
        }
    }

    /**
     * Evaluate a derived association end.
     *
     * @param instance The instance to evaluate from
     * @param instanceId The instance ID
     * @param associationName The association name
     * @param endName The end name to evaluate
     * @return List of related objects
     */
    fun evaluateAssociationEnd(
        instance: MDMObject,
        instanceId: String,
        associationName: String,
        endName: String
    ): List<MDMObject> {
        val evaluator = registry.getAssociationEndEvaluator(associationName, endName)
            ?: return emptyList()

        val context = createContext(instance, instanceId)
        return try {
            evaluator.evaluate(context)
        } catch (e: Exception) {
            logger.error(e) { "Error evaluating association end $associationName::$endName" }
            emptyList()
        }
    }

    /**
     * Check if a derived property evaluator is registered.
     */
    fun hasDerivedPropertyEvaluator(className: String, propertyName: String): Boolean =
        registry.hasDerivedPropertyEvaluator(className, propertyName)

    /**
     * Create an evaluation context for an instance.
     */
    private fun createContext(
        instance: MDMObject,
        instanceId: String,
        parameters: Map<String, Any?> = emptyMap()
    ): EvaluationContext {
        return EvaluationContext(
            instance = instance,
            instanceId = instanceId,
            engineAccessor = engineAccessor,
            parameters = parameters
        )
    }
}

/**
 * Builder DSL for registering KerML-specific constraints.
 *
 * Example usage:
 * ```kotlin
 * constraintEngine.registerKerMLConstraints {
 *     // Derived property: documentation = ownedElement->selectByKind(Documentation)
 *     derivedProperty("Element", "documentation") { ctx ->
 *         ctx.getLinkedTargets("ownedElementOwnerAssociation")
 *             .filter { ctx.isKindOf(it, "Documentation") }
 *     }
 *
 *     // Validation: XOR constraint
 *     validation("Annotation", "validateAnnotationAnnotatingElement") { ctx ->
 *         val owned = ctx.getProperty("ownedAnnotatingElement") != null
 *         val owning = ctx.getProperty("owningAnnotatingElement") != null
 *         if (owned xor owning) {
 *             ValidationResult.valid()
 *         } else {
 *             ValidationResult.invalid("Exactly one of ownedAnnotatingElement or owningAnnotatingElement must be set")
 *         }
 *     }
 * }
 * ```
 */
class ConstraintRegistrationDsl(private val registry: ConstraintRegistry) {

    /**
     * Register a derived property evaluator.
     */
    fun derivedProperty(
        className: String,
        propertyName: String,
        evaluator: (EvaluationContext) -> Any?
    ) {
        registry.registerDerivedProperty(className, propertyName, evaluator)
    }

    /**
     * Register a validation constraint.
     */
    fun validation(
        className: String,
        constraintName: String,
        evaluator: (EvaluationContext) -> ValidationResult
    ) {
        registry.registerValidationConstraint(className, constraintName, evaluator)
    }

    /**
     * Register a global validation constraint (applies to all classes).
     */
    fun globalValidation(
        constraintName: String,
        evaluator: (EvaluationContext) -> ValidationResult
    ) {
        registry.registerGlobalConstraint(constraintName, evaluator)
    }

    /**
     * Register a derived association end evaluator.
     */
    fun associationEnd(
        associationName: String,
        endName: String,
        evaluator: (EvaluationContext) -> List<MDMObject>
    ) {
        registry.registerAssociationEnd(associationName, endName, evaluator)
    }
}

/**
 * Extension function for fluent constraint registration.
 */
fun ConstraintRegistry.register(block: ConstraintRegistrationDsl.() -> Unit) {
    ConstraintRegistrationDsl(this).block()
}
