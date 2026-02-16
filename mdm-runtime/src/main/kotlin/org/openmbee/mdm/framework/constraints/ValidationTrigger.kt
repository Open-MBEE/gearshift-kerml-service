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

private val logger = KotlinLogging.logger {}

/**
 * Mode for automatic constraint validation.
 */
enum class ValidationMode {
    /**
     * Validate when an instance is created.
     */
    ON_CREATE,

    /**
     * Validate when an instance is created or updated.
     */
    ON_UPDATE,

    /**
     * Only validate when explicitly requested.
     */
    ON_DEMAND,

    /**
     * Collect changes and validate in batch when flushed.
     */
    DEFERRED
}

/**
 * Listener for validation failure events.
 */
fun interface ValidationFailureListener {
    /**
     * Called when validation fails for an instance.
     *
     * @param instanceId The ID of the instance that failed validation
     * @param results The validation results
     */
    fun onValidationFailed(instanceId: String, results: ValidationResults)
}

/**
 * Triggers constraint validation in response to instance lifecycle events.
 *
 * This class implements [InstanceLifecycleListener] to automatically validate
 * instances when they are created or updated, based on the configured [ValidationMode].
 *
 * @param verificationService The service to use for validation
 * @param mode The validation mode determining when validation occurs
 */
class ValidationTrigger(
    private val verificationService: ConstraintVerificationService,
    private val mode: ValidationMode = ValidationMode.ON_UPDATE
) : InstanceLifecycleListener {

    /**
     * Set of instance IDs pending validation (used in DEFERRED mode).
     */
    private val pendingValidation = mutableSetOf<String>()

    /**
     * Listeners notified when validation fails.
     */
    private val failureListeners = mutableListOf<ValidationFailureListener>()

    /**
     * Add a listener to be notified when validation fails.
     */
    fun addFailureListener(listener: ValidationFailureListener) {
        failureListeners.add(listener)
    }

    /**
     * Remove a failure listener.
     */
    fun removeFailureListener(listener: ValidationFailureListener) {
        failureListeners.remove(listener)
    }

    override fun onInstanceCreated(instanceId: String, instance: MDMObject) {
        when (mode) {
            ValidationMode.ON_CREATE, ValidationMode.ON_UPDATE -> {
                validateAndReport(instanceId)
            }

            ValidationMode.DEFERRED -> {
                pendingValidation.add(instanceId)
            }

            ValidationMode.ON_DEMAND -> {
                // Do nothing
            }
        }
    }

    override fun onInstanceUpdated(instanceId: String, instance: MDMObject, propertyName: String, newValue: Any?) {
        when (mode) {
            ValidationMode.ON_UPDATE -> {
                validateAndReport(instanceId)
            }

            ValidationMode.DEFERRED -> {
                pendingValidation.add(instanceId)
            }

            ValidationMode.ON_CREATE, ValidationMode.ON_DEMAND -> {
                // Do nothing
            }
        }
    }

    override fun onInstanceDeleted(instanceId: String) {
        // Remove from pending if present
        pendingValidation.remove(instanceId)
    }

    /**
     * Flush pending validations (used in DEFERRED mode).
     *
     * @return Bulk validation results for all pending instances
     */
    fun flushPending(): BulkValidationResults {
        val ids = pendingValidation.toList()
        pendingValidation.clear()

        if (ids.isEmpty()) {
            return BulkValidationResults.empty()
        }

        logger.info { "Flushing ${ids.size} pending validations" }
        return verificationService.validateInstances(ids)
    }

    /**
     * Get the number of instances pending validation.
     */
    fun getPendingCount(): Int = pendingValidation.size

    /**
     * Check if there are any instances pending validation.
     */
    fun hasPending(): Boolean = pendingValidation.isNotEmpty()

    /**
     * Clear all pending validations without executing them.
     */
    fun clearPending() {
        pendingValidation.clear()
    }

    /**
     * Validate an instance and notify listeners if validation fails.
     */
    private fun validateAndReport(instanceId: String) {
        val results = verificationService.validateInstance(instanceId)

        if (!results.isValid) {
            logger.warn { "Validation failed for instance $instanceId: ${results.errorMessages}" }
            notifyFailureListeners(instanceId, results)
        }
    }

    /**
     * Notify all registered failure listeners.
     */
    private fun notifyFailureListeners(instanceId: String, results: ValidationResults) {
        failureListeners.forEach { listener ->
            try {
                listener.onValidationFailed(instanceId, results)
            } catch (e: Exception) {
                logger.error(e) { "Error in validation failure listener" }
            }
        }
    }
}
