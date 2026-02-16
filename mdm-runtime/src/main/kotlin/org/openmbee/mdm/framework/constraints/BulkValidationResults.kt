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

/**
 * Results from bulk validation of multiple instances.
 */
data class BulkValidationResults(
    /**
     * Validation results keyed by instance ID.
     */
    val instanceResults: Map<String, ValidationResults>,

    /**
     * Total number of instances validated.
     */
    val totalInstances: Int,

    /**
     * Number of instances that passed all constraints.
     */
    val validInstances: Int,

    /**
     * Number of instances with at least one constraint violation.
     */
    val invalidInstances: Int,

    /**
     * Total number of constraint violations across all instances.
     */
    val constraintViolations: Int,

    /**
     * Time taken for validation in milliseconds.
     */
    val executionTimeMs: Long
) {
    /**
     * Whether all instances passed validation.
     */
    val isValid: Boolean get() = invalidInstances == 0

    /**
     * Percentage of instances that passed validation.
     */
    val validationRate: Double get() = if (totalInstances > 0) validInstances.toDouble() / totalInstances else 1.0

    /**
     * Get only the instances with errors.
     */
    fun getErrors(): Map<String, List<ValidationResult>> =
        instanceResults.mapValues { it.value.errors }.filterValues { it.isNotEmpty() }

    /**
     * Get all error messages grouped by instance ID.
     */
    fun getErrorMessages(): Map<String, List<String>> =
        instanceResults.mapValues { it.value.errorMessages }.filterValues { it.isNotEmpty() }

    /**
     * Generate a summary string of the validation results.
     */
    fun summarize(): String = buildString {
        appendLine("Validation Summary:")
        appendLine("  Total instances: $totalInstances")
        appendLine("  Valid: $validInstances (${String.format("%.1f", validationRate * 100)}%)")
        appendLine("  Invalid: $invalidInstances")
        appendLine("  Total violations: $constraintViolations")
        appendLine("  Execution time: ${executionTimeMs}ms")
    }

    companion object {
        /**
         * Create an empty result for when there are no instances to validate.
         */
        fun empty() = BulkValidationResults(
            instanceResults = emptyMap(),
            totalInstances = 0,
            validInstances = 0,
            invalidInstances = 0,
            constraintViolations = 0,
            executionTimeMs = 0
        )
    }
}

/**
 * Validation results grouped by type.
 */
data class TypeValidationResults(
    /**
     * Results grouped by metaclass name.
     */
    val byType: Map<String, BulkValidationResults>,

    /**
     * Overall aggregated results across all types.
     */
    val overall: BulkValidationResults
)
