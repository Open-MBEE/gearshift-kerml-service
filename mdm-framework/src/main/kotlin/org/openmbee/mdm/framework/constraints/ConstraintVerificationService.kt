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
import org.openmbee.mdm.framework.constraints.ocl.OclExecutor
import org.openmbee.mdm.framework.constraints.ocl.OclExpression
import org.openmbee.mdm.framework.constraints.ocl.OclParseException
import org.openmbee.mdm.framework.constraints.ocl.OclParser
import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.runtime.MDMObject
import org.openmbee.mdm.framework.runtime.MetamodelRegistry
import org.openmbee.mdm.framework.storage.ModelRepository

private val logger = KotlinLogging.logger {}

/**
 * A compiled constraint with pre-parsed AST for efficient repeated evaluation.
 */
data class CompiledConstraint(
    val name: String,
    val description: String?,
    val ast: OclExpression,
    val originalExpression: String
)

/**
 * Service for discovering and executing VERIFICATION constraints from metamodel definitions.
 *
 * This service:
 * - Discovers all VERIFICATION constraints from MetaClass definitions
 * - Pre-compiles OCL expressions for efficient evaluation
 * - Provides bulk validation across all instances or by type
 * - Supports single-instance validation
 */
class ConstraintVerificationService(
    private val metamodelRegistry: MetamodelRegistry,
    private val modelRepository: ModelRepository,
    private val engineAccessor: EngineAccessor
) {
    /**
     * Cache of compiled constraints per metaclass name.
     * Key is the metaclass name, value is the list of compiled VERIFICATION constraints.
     */
    private val constraintCache = mutableMapOf<String, List<CompiledConstraint>>()

    /**
     * Parse errors encountered during constraint compilation.
     */
    private val parseErrors = mutableListOf<String>()

    /**
     * Initialize the service by discovering and compiling all VERIFICATION constraints.
     * Call this after the metamodel is loaded.
     *
     * @return List of parse errors encountered during compilation (empty if all succeeded)
     */
    fun initializeConstraints(): List<String> {
        constraintCache.clear()
        parseErrors.clear()

        metamodelRegistry.getAllClasses().forEach { metaClass ->
            val verificationConstraints = collectVerificationConstraints(metaClass)
            if (verificationConstraints.isNotEmpty()) {
                val compiled = verificationConstraints.mapNotNull { constraint ->
                    try {
                        CompiledConstraint(
                            name = constraint.name,
                            description = constraint.description,
                            ast = OclParser.parse(constraint.expression),
                            originalExpression = constraint.expression
                        )
                    } catch (e: OclParseException) {
                        val error = "Failed to parse constraint '${constraint.name}' on ${metaClass.name}: ${e.message}"
                        parseErrors.add(error)
                        logger.error { error }
                        null
                    }
                }
                if (compiled.isNotEmpty()) {
                    constraintCache[metaClass.name] = compiled
                }
            }
        }

        logger.info { "Initialized ${constraintCache.values.sumOf { it.size }} verification constraints across ${constraintCache.size} classes" }
        return parseErrors.toList()
    }

    /**
     * Collect all VERIFICATION constraints for a metaclass, including inherited constraints.
     */
    private fun collectVerificationConstraints(metaClass: MetaClass): List<MetaConstraint> {
        val constraints = metaClass.constraints
            .filter { it.type == ConstraintType.VERIFICATION }
            .toMutableList()

        // Include inherited constraints from superclasses
        metaClass.superclasses.forEach { superName ->
            metamodelRegistry.getClass(superName)?.let { superclass ->
                constraints.addAll(collectVerificationConstraints(superclass))
            }
        }

        return constraints.distinctBy { it.name }
    }

    /**
     * Get all compiled constraints applicable to an instance of the given class.
     * Includes constraints from the class itself and all superclasses.
     */
    private fun getConstraintsForClass(className: String): List<CompiledConstraint> {
        val result = mutableListOf<CompiledConstraint>()

        // Add constraints from this class
        constraintCache[className]?.let { result.addAll(it) }

        // Add constraints from superclasses
        metamodelRegistry.getClass(className)?.superclasses?.forEach { superName ->
            result.addAll(getConstraintsForClass(superName))
        }

        return result.distinctBy { it.name }
    }

    /**
     * Validate all instances in the repository against their VERIFICATION constraints.
     *
     * @return Aggregated validation results
     */
    fun validateAll(): BulkValidationResults {
        val startTime = System.currentTimeMillis()
        val allInstances = modelRepository.getAll()

        val instanceResults = mutableMapOf<String, ValidationResults>()
        var validCount = 0
        var invalidCount = 0
        var violationCount = 0

        allInstances.forEach { instance ->
            val instanceId = instance.id ?: return@forEach
            val results = validateInstanceInternal(instanceId, instance)
            instanceResults[instanceId] = results

            if (results.isValid) {
                validCount++
            } else {
                invalidCount++
                violationCount += results.errors.size
            }
        }

        val executionTime = System.currentTimeMillis() - startTime
        logger.info { "Validated ${allInstances.size} instances in ${executionTime}ms: $validCount valid, $invalidCount invalid" }

        return BulkValidationResults(
            instanceResults = instanceResults,
            totalInstances = allInstances.size,
            validInstances = validCount,
            invalidInstances = invalidCount,
            constraintViolations = violationCount,
            executionTimeMs = executionTime
        )
    }

    /**
     * Validate all instances of a specific type against their VERIFICATION constraints.
     *
     * @param className The metaclass name to validate
     * @param includeSubtypes Whether to include instances of subclasses
     * @return Aggregated validation results
     */
    fun validateByType(className: String, includeSubtypes: Boolean = true): BulkValidationResults {
        val startTime = System.currentTimeMillis()

        val instances = if (includeSubtypes) {
            getInstancesIncludingSubtypes(className)
        } else {
            modelRepository.getByType(className)
        }

        val instanceResults = mutableMapOf<String, ValidationResults>()
        var validCount = 0
        var invalidCount = 0
        var violationCount = 0

        instances.forEach { instance ->
            val instanceId = instance.id ?: return@forEach
            val results = validateInstanceInternal(instanceId, instance)
            instanceResults[instanceId] = results

            if (results.isValid) {
                validCount++
            } else {
                invalidCount++
                violationCount += results.errors.size
            }
        }

        val executionTime = System.currentTimeMillis() - startTime
        logger.info { "Validated ${instances.size} instances of $className in ${executionTime}ms: $validCount valid, $invalidCount invalid" }

        return BulkValidationResults(
            instanceResults = instanceResults,
            totalInstances = instances.size,
            validInstances = validCount,
            invalidInstances = invalidCount,
            constraintViolations = violationCount,
            executionTimeMs = executionTime
        )
    }

    /**
     * Get all instances of a class including instances of all subclasses.
     */
    private fun getInstancesIncludingSubtypes(className: String): List<MDMObject> {
        val result = mutableListOf<MDMObject>()
        result.addAll(modelRepository.getByType(className))

        // Recursively add instances of subclasses
        metamodelRegistry.getDirectSubclasses(className).forEach { subclassName ->
            result.addAll(getInstancesIncludingSubtypes(subclassName))
        }

        return result
    }

    /**
     * Validate a single instance by ID.
     *
     * @param instanceId The ID of the instance to validate
     * @return Validation results for this instance
     */
    fun validateInstance(instanceId: String): ValidationResults {
        val instance = modelRepository.get(instanceId)
            ?: return ValidationResults.fromResults(
                listOf(ValidationResult.invalid("Instance not found: $instanceId"))
            )

        return validateInstanceInternal(instanceId, instance)
    }

    /**
     * Validate multiple instances by their IDs.
     *
     * @param instanceIds The IDs of instances to validate
     * @return Aggregated validation results
     */
    fun validateInstances(instanceIds: List<String>): BulkValidationResults {
        val startTime = System.currentTimeMillis()

        val instanceResults = mutableMapOf<String, ValidationResults>()
        var validCount = 0
        var invalidCount = 0
        var violationCount = 0

        instanceIds.forEach { instanceId ->
            val instance = modelRepository.get(instanceId) ?: return@forEach
            val results = validateInstanceInternal(instanceId, instance)
            instanceResults[instanceId] = results

            if (results.isValid) {
                validCount++
            } else {
                invalidCount++
                violationCount += results.errors.size
            }
        }

        val executionTime = System.currentTimeMillis() - startTime

        return BulkValidationResults(
            instanceResults = instanceResults,
            totalInstances = instanceIds.size,
            validInstances = validCount,
            invalidInstances = invalidCount,
            constraintViolations = violationCount,
            executionTimeMs = executionTime
        )
    }

    /**
     * Internal method to validate an instance against all applicable constraints.
     */
    private fun validateInstanceInternal(instanceId: String, instance: MDMObject): ValidationResults {
        val constraints = getConstraintsForClass(instance.className)

        if (constraints.isEmpty()) {
            return ValidationResults.valid()
        }

        val results = constraints.map { constraint ->
            evaluateConstraint(constraint, instance, instanceId)
        }

        return ValidationResults.fromResults(results)
    }

    /**
     * Evaluate a single constraint against an instance.
     */
    private fun evaluateConstraint(
        constraint: CompiledConstraint,
        instance: MDMObject,
        instanceId: String
    ): ValidationResult {
        return try {
            val executor = OclExecutor(engineAccessor, instance, instanceId)
            val result = executor.evaluate(constraint.ast)

            when (result) {
                true -> ValidationResult.valid()
                false -> ValidationResult.invalid(
                    message = "Constraint '${constraint.name}' failed${constraint.description?.let { ": $it" } ?: ""}",
                    constraintName = constraint.name
                )

                else -> ValidationResult.invalid(
                    message = "Constraint '${constraint.name}' returned non-boolean result: $result",
                    constraintName = constraint.name
                )
            }
        } catch (e: Exception) {
            logger.error(e) { "Error evaluating constraint '${constraint.name}' on instance $instanceId" }
            ValidationResult.invalid(
                message = "Error evaluating constraint '${constraint.name}': ${e.message}",
                constraintName = constraint.name
            )
        }
    }

    /**
     * Get all registered verification constraints.
     *
     * @return Map of class name to list of constraint names
     */
    fun getRegisteredConstraints(): Map<String, List<String>> =
        constraintCache.mapValues { entry -> entry.value.map { it.name } }

    /**
     * Check if there are any verification constraints registered.
     */
    fun hasConstraints(): Boolean = constraintCache.isNotEmpty()

    /**
     * Get the total number of registered verification constraints.
     */
    fun getConstraintCount(): Int = constraintCache.values.sumOf { it.size }
}
