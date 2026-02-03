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

import org.openmbee.mdm.framework.runtime.MDMObject

/**
 * Context provided to constraint evaluators for evaluating expressions.
 * Provides access to the instance being evaluated and the engine for
 * traversing associations and accessing related objects.
 */
data class EvaluationContext(
    /**
     * The instance being evaluated (the "self" in OCL terms).
     */
    val instance: MDMObject,

    /**
     * The instance ID in the repository.
     */
    val instanceId: String,

    /**
     * Access to the engine for traversing links and getting related objects.
     * This is provided as a function to avoid circular dependencies.
     */
    val engineAccessor: EngineAccessor,

    /**
     * Additional parameters that may be passed to the evaluator.
     */
    val parameters: Map<String, Any?> = emptyMap()
)

/**
 * Interface for accessing engine functionality during constraint evaluation.
 * Abstracts the engine to avoid circular dependencies.
 */
interface EngineAccessor {
    /**
     * Get an instance by ID.
     */
    fun getInstance(id: String): MDMObject?

    /**
     * Get targets linked from source via an association.
     */
    fun getLinkedTargets(associationName: String, sourceId: String): List<MDMObject>

    /**
     * Get sources linked to target via an association.
     */
    fun getLinkedSources(associationName: String, targetId: String): List<MDMObject>

    /**
     * Get a property value from an instance.
     */
    fun getProperty(instanceId: String, propertyName: String): Any?

    /**
     * Check if a class is a subclass of another.
     */
    fun isSubclassOf(subclass: String, superclass: String): Boolean

    /**
     * Invoke an operation on an instance.
     */
    fun invokeOperation(instanceId: String, operationName: String, arguments: Map<String, Any?> = emptyMap()): Any?
}

/**
 * Result of a validation constraint evaluation.
 */
data class ValidationResult(
    /**
     * Whether the constraint passed.
     */
    val isValid: Boolean,

    /**
     * Error message if validation failed.
     */
    val message: String? = null,

    /**
     * The constraint name that was evaluated.
     */
    val constraintName: String? = null
) {
    companion object {
        fun valid() = ValidationResult(isValid = true)

        fun invalid(message: String, constraintName: String? = null) =
            ValidationResult(isValid = false, message = message, constraintName = constraintName)
    }
}

/**
 * Aggregated validation results for an instance.
 */
data class ValidationResults(
    val results: List<ValidationResult>
) {
    val isValid: Boolean get() = results.all { it.isValid }

    val errors: List<ValidationResult> get() = results.filter { !it.isValid }

    val errorMessages: List<String> get() = errors.mapNotNull { it.message }

    companion object {
        fun valid() = ValidationResults(listOf(ValidationResult.valid()))

        fun fromResults(results: List<ValidationResult>) = ValidationResults(results)
    }
}
