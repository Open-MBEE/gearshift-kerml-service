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
package org.openmbee.gearshift.constraints

import org.openmbee.gearshift.engine.MDMObject

/**
 * Evaluator for derived properties.
 * Computes the value of a property based on other properties and associations.
 *
 * Example (from KerML spec):
 * ```
 * documentation = ownedElement->selectByKind(Documentation)
 * ```
 */
fun interface DerivedPropertyEvaluator {
    /**
     * Evaluate the derived property value.
     *
     * @param context The evaluation context with access to the instance and engine
     * @return The computed property value
     */
    fun evaluate(context: EvaluationContext): Any?
}

/**
 * Evaluator for validation constraints (invariants).
 * Checks that an instance satisfies a constraint condition.
 *
 * Example:
 * ```
 * inv: self.multiplicity.lowerBound >= 0
 * ```
 */
fun interface ValidationConstraintEvaluator {
    /**
     * Validate the constraint on the given instance.
     *
     * @param context The evaluation context
     * @return Validation result indicating pass/fail and error message
     */
    fun validate(context: EvaluationContext): ValidationResult
}

/**
 * Evaluator for derived association ends.
 * Computes the set of related objects for a non-navigable or derived association end.
 *
 * Example:
 * A derived association end that computes inverse relationships or
 * filters based on type.
 */
fun interface AssociationEndEvaluator {
    /**
     * Evaluate the derived association end.
     *
     * @param context The evaluation context
     * @return List of related objects
     */
    fun evaluate(context: EvaluationContext): List<MDMObject>
}

/**
 * Key for registering derived property evaluators.
 */
data class DerivedPropertyKey(
    val className: String,
    val propertyName: String
)

/**
 * Key for registering derived association end evaluators.
 */
data class AssociationEndKey(
    val associationName: String,
    val endName: String
)

/**
 * Builder for creating derived property evaluators using a fluent DSL.
 *
 * Example usage:
 * ```kotlin
 * DerivedPropertyBuilder.forClass("Element")
 *     .property("documentation")
 *     .derivedFrom { ctx ->
 *         ctx.getLinkedTargets("ownedElementOwnerAssociation")
 *             .filter { it.className == "Documentation" || ctx.isSubclassOf(it.className, "Documentation") }
 *     }
 *     .build()
 * ```
 */
class DerivedPropertyBuilder private constructor(
    private val className: String
) {
    private var propertyName: String? = null
    private var evaluator: DerivedPropertyEvaluator? = null

    fun property(name: String): DerivedPropertyBuilder {
        this.propertyName = name
        return this
    }

    fun derivedFrom(evaluator: DerivedPropertyEvaluator): DerivedPropertyBuilder {
        this.evaluator = evaluator
        return this
    }

    fun build(): Pair<DerivedPropertyKey, DerivedPropertyEvaluator> {
        val propName = propertyName ?: throw IllegalStateException("Property name not set")
        val eval = evaluator ?: throw IllegalStateException("Evaluator not set")
        return DerivedPropertyKey(className, propName) to eval
    }

    companion object {
        fun forClass(className: String) = DerivedPropertyBuilder(className)
    }
}

/**
 * Extension functions on EvaluationContext for common operations.
 */

/**
 * Get linked targets from the current instance via an association.
 */
fun EvaluationContext.getLinkedTargets(associationName: String): List<MDMObject> =
    engineAccessor.getLinkedTargets(associationName, instanceId)

/**
 * Get linked sources to the current instance via an association.
 */
fun EvaluationContext.getLinkedSources(associationName: String): List<MDMObject> =
    engineAccessor.getLinkedSources(associationName, instanceId)

/**
 * Get a property value from the current instance.
 */
fun EvaluationContext.getProperty(propertyName: String): Any? =
    engineAccessor.getProperty(instanceId, propertyName)

/**
 * Check if a class is of a given kind (exact match or subclass).
 */
fun EvaluationContext.isKindOf(obj: MDMObject, typeName: String): Boolean =
    obj.className == typeName || engineAccessor.isSubclassOf(obj.className, typeName)

/**
 * Filter a list of objects to only those of a specific type (selectByKind).
 * This is the OCL ->selectByKind(Type) operation.
 */
fun EvaluationContext.selectByKind(objects: List<MDMObject>, typeName: String): List<MDMObject> =
    objects.filter { isKindOf(it, typeName) }

/**
 * Filter a list of objects to only those of the exact type (selectByType).
 * This is the OCL ->selectByType(Type) operation.
 */
fun EvaluationContext.selectByType(objects: List<MDMObject>, typeName: String): List<MDMObject> =
    objects.filter { it.className == typeName }

/**
 * Collect a property value from each object in a list.
 * This is the OCL ->collect(property) operation.
 */
fun EvaluationContext.collect(objects: List<MDMObject>, propertyName: String): List<Any?> =
    objects.map { it.getProperty(propertyName) }

/**
 * Flatten a list of lists.
 * This is the OCL ->flatten() operation.
 */
fun <T> flatten(lists: List<List<T>>): List<T> = lists.flatten()

/**
 * Union of two collections.
 * This is the OCL ->union() operation.
 */
fun <T> union(a: List<T>, b: List<T>): List<T> = (a + b).distinct()

/**
 * Intersection of two collections.
 * This is the OCL ->intersection() operation.
 */
fun <T> intersection(a: List<T>, b: List<T>): List<T> = a.filter { it in b }

/**
 * Difference of two collections.
 * This is the OCL - (minus) operation.
 */
fun <T> difference(a: List<T>, b: List<T>): List<T> = a.filter { it !in b }
