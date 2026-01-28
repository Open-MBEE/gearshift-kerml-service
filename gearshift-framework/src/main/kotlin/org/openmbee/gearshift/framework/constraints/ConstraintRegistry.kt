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
package org.openmbee.gearshift.framework.constraints

import io.github.oshai.kotlinlogging.KotlinLogging
import java.util.concurrent.ConcurrentHashMap

private val logger = KotlinLogging.logger {}

/**
 * Global registry for constraint evaluators.
 *
 * Manages registration and lookup of:
 * - Derived property evaluators (compute derived property values)
 * - Validation constraint evaluators (check invariants)
 * - Association end evaluators (compute derived association ends)
 *
 * Supports inheritance: if no evaluator is found for a specific class,
 * it will check superclasses.
 */
class ConstraintRegistry {

    // Derived property evaluators: (className, propertyName) -> evaluator
    private val derivedPropertyEvaluators = ConcurrentHashMap<DerivedPropertyKey, DerivedPropertyEvaluator>()

    // Validation constraints: constraintKey -> evaluator
    // constraintKey format: "ClassName::constraintName" or just "constraintName" for global
    private val validationConstraints = ConcurrentHashMap<String, ValidationConstraintEvaluator>()

    // Class-specific validation constraints: className -> list of evaluators
    private val classConstraints = ConcurrentHashMap<String, MutableList<Pair<String, ValidationConstraintEvaluator>>>()

    // Association end evaluators: (associationName, endName) -> evaluator
    private val associationEndEvaluators = ConcurrentHashMap<AssociationEndKey, AssociationEndEvaluator>()

    // Superclass resolver for inheritance lookup
    private var superclassResolver: ((String) -> List<String>)? = null

    /**
     * Set the superclass resolver function.
     * This is used to look up evaluators in superclasses if not found in the direct class.
     */
    fun setSuperclassResolver(resolver: (String) -> List<String>) {
        this.superclassResolver = resolver
    }

    // ===== Derived Property Evaluators =====

    /**
     * Register a derived property evaluator.
     *
     * @param className The class that has the derived property
     * @param propertyName The name of the derived property
     * @param evaluator The evaluator function
     */
    fun registerDerivedProperty(
        className: String,
        propertyName: String,
        evaluator: DerivedPropertyEvaluator
    ) {
        val key = DerivedPropertyKey(className, propertyName)
        derivedPropertyEvaluators[key] = evaluator
        logger.debug { "Registered derived property evaluator: $className::$propertyName" }
    }

    /**
     * Register a derived property evaluator using a builder.
     */
    fun registerDerivedProperty(registration: Pair<DerivedPropertyKey, DerivedPropertyEvaluator>) {
        derivedPropertyEvaluators[registration.first] = registration.second
        logger.debug { "Registered derived property evaluator: ${registration.first}" }
    }

    /**
     * Get a derived property evaluator, checking superclasses if not found directly.
     */
    fun getDerivedPropertyEvaluator(className: String, propertyName: String): DerivedPropertyEvaluator? {
        // Check direct class
        val key = DerivedPropertyKey(className, propertyName)
        derivedPropertyEvaluators[key]?.let { return it }

        // Check superclasses
        superclassResolver?.invoke(className)?.forEach { superclass ->
            getDerivedPropertyEvaluator(superclass, propertyName)?.let { return it }
        }

        return null
    }

    /**
     * Check if a derived property evaluator exists.
     */
    fun hasDerivedPropertyEvaluator(className: String, propertyName: String): Boolean =
        getDerivedPropertyEvaluator(className, propertyName) != null

    // ===== Validation Constraint Evaluators =====

    /**
     * Register a validation constraint for a specific class.
     *
     * @param className The class this constraint applies to
     * @param constraintName The name of the constraint
     * @param evaluator The validation evaluator
     */
    fun registerValidationConstraint(
        className: String,
        constraintName: String,
        evaluator: ValidationConstraintEvaluator
    ) {
        val fullKey = "$className::$constraintName"
        validationConstraints[fullKey] = evaluator

        classConstraints.getOrPut(className) { mutableListOf() }
            .add(constraintName to evaluator)

        logger.debug { "Registered validation constraint: $fullKey" }
    }

    /**
     * Register a global validation constraint (applies to all instances).
     */
    fun registerGlobalConstraint(
        constraintName: String,
        evaluator: ValidationConstraintEvaluator
    ) {
        validationConstraints[constraintName] = evaluator
        logger.debug { "Registered global validation constraint: $constraintName" }
    }

    /**
     * Get a specific validation constraint evaluator.
     */
    fun getValidationConstraint(className: String, constraintName: String): ValidationConstraintEvaluator? {
        // Check class-specific first
        val fullKey = "$className::$constraintName"
        validationConstraints[fullKey]?.let { return it }

        // Check superclasses
        superclassResolver?.invoke(className)?.forEach { superclass ->
            getValidationConstraint(superclass, constraintName)?.let { return it }
        }

        // Check global
        return validationConstraints[constraintName]
    }

    /**
     * Check if a validation constraint evaluator exists for a class.
     */
    fun hasValidationConstraint(className: String, constraintName: String): Boolean =
        getValidationConstraint(className, constraintName) != null

    /**
     * Get all validation constraints for a class (including inherited).
     */
    fun getValidationConstraints(className: String): List<Pair<String, ValidationConstraintEvaluator>> {
        val result = mutableListOf<Pair<String, ValidationConstraintEvaluator>>()

        // Add class-specific constraints
        classConstraints[className]?.let { result.addAll(it) }

        // Add inherited constraints
        superclassResolver?.invoke(className)?.forEach { superclass ->
            result.addAll(getValidationConstraints(superclass))
        }

        return result.distinctBy { it.first }
    }

    // ===== Association End Evaluators =====

    /**
     * Register an evaluator for a derived or non-navigable association end.
     *
     * @param associationName The association name
     * @param endName The end name (role name)
     * @param evaluator The evaluator that computes the related objects
     */
    fun registerAssociationEnd(
        associationName: String,
        endName: String,
        evaluator: AssociationEndEvaluator
    ) {
        val key = AssociationEndKey(associationName, endName)
        associationEndEvaluators[key] = evaluator
        logger.debug { "Registered association end evaluator: $associationName::$endName" }
    }

    /**
     * Get an association end evaluator.
     */
    fun getAssociationEndEvaluator(associationName: String, endName: String): AssociationEndEvaluator? {
        val key = AssociationEndKey(associationName, endName)
        return associationEndEvaluators[key]
    }

    /**
     * Check if an association end evaluator exists.
     */
    fun hasAssociationEndEvaluator(associationName: String, endName: String): Boolean {
        val key = AssociationEndKey(associationName, endName)
        return associationEndEvaluators.containsKey(key)
    }

    // ===== Bulk Registration =====

    /**
     * Register multiple derived property evaluators at once.
     */
    fun registerDerivedProperties(vararg registrations: Pair<DerivedPropertyKey, DerivedPropertyEvaluator>) {
        registrations.forEach { registerDerivedProperty(it) }
    }

    // ===== Statistics =====

    /**
     * Get registry statistics.
     */
    fun getStatistics(): ConstraintRegistryStatistics {
        return ConstraintRegistryStatistics(
            derivedPropertyCount = derivedPropertyEvaluators.size,
            validationConstraintCount = validationConstraints.size,
            associationEndCount = associationEndEvaluators.size
        )
    }

    /**
     * Clear all registered evaluators.
     */
    fun clear() {
        derivedPropertyEvaluators.clear()
        validationConstraints.clear()
        classConstraints.clear()
        associationEndEvaluators.clear()
        logger.debug { "Constraint registry cleared" }
    }
}

data class ConstraintRegistryStatistics(
    val derivedPropertyCount: Int,
    val validationConstraintCount: Int,
    val associationEndCount: Int
)
