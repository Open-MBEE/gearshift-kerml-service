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
package org.openmbee.gearshift.engine

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.gearshift.metamodel.*
import java.util.*

private val logger = KotlinLogging.logger {}

/**
 * MOF (Meta-Object Facility) Engine for managing and executing metamodel operations.
 * Provides runtime support for metamodel-driven object creation, validation, and queries.
 */
class MofEngine(val registry: MetamodelRegistry) {
    private val instances = mutableMapOf<String, MofObject>()

    /**
     * Create a new instance of a metaclass.
     */
    fun createInstance(className: String): MofObject {
        val metaClass = registry.getClass(className)
            ?: throw IllegalArgumentException("Unknown class: $className")

        if (metaClass.isAbstract) {
            throw IllegalArgumentException("Cannot instantiate abstract class: $className")
        }

        val instance = MofObject(className, metaClass)
        val instanceId = UUID.randomUUID().toString()
        instances[instanceId] = instance

        logger.debug { "Created instance of $className: $instanceId" }
        return instance
    }

    /**
     * Set a property value on an instance.
     */
    fun setProperty(instance: MofObject, propertyName: String, value: Any?) {
        val property = findProperty(instance.metaClass, propertyName)
            ?: throw IllegalArgumentException(
                "Property '$propertyName' not found in class: ${instance.className}"
            )

        if (property.isReadOnly) {
            throw IllegalStateException("Cannot set read-only property: $propertyName")
        }

        validatePropertyValue(property, value)
        instance.setProperty(propertyName, value)

        logger.debug { "Set property $propertyName on ${instance.className} instance" }
    }

    /**
     * Get a property value from an instance.
     */
    fun getProperty(instance: MofObject, propertyName: String): Any? {
        val property = findProperty(instance.metaClass, propertyName)
            ?: throw IllegalArgumentException(
                "Property '$propertyName' not found in class: ${instance.className}"
            )

        var value = instance.getProperty(propertyName)

        // Handle derived properties
        if (property.isDerived && value == null && property.derivationConstraint != null) {
            value = evaluateDerivedProperty(instance, property)
        }

        return value
    }

    /**
     * Validate an instance against its metaclass constraints.
     */
    fun validate(instance: MofObject): List<String> {
        val errors = mutableListOf<String>()
        val metaClass = instance.metaClass

        // Validate required properties
        getAllProperties(metaClass).forEach { property ->
            if (!property.isDerived) {
                val value = instance.getProperty(property.name)
                if (value == null && property.multiplicity.startsWith("1")) {
                    errors.add("Required property '${property.name}' is not set")
                }
            }
        }

        // Evaluate constraints
        metaClass.constraints.forEach { constraint ->
            if (!evaluateConstraint(instance, constraint)) {
                errors.add(
                    "Constraint '${constraint.name}' violated: ${constraint.description}"
                )
            }
        }

        return errors
    }

    /**
     * Find a property in a metaclass or its superclasses.
     */
    private fun findProperty(metaClass: MetaClass, propertyName: String): MetaProperty? {
        // Check direct properties
        metaClass.attributes.firstOrNull { it.name == propertyName }?.let { return it }

        // Check inherited properties
        metaClass.superclasses.forEach { superclassName ->
            registry.getClass(superclassName)?.let { superclass ->
                findProperty(superclass, propertyName)?.let { return it }
            }
        }

        return null
    }

    /**
     * Get all properties including inherited ones.
     */
    private fun getAllProperties(metaClass: MetaClass): List<MetaProperty> {
        val allProps = metaClass.attributes.toMutableList()

        metaClass.superclasses.forEach { superclassName ->
            registry.getClass(superclassName)?.let { superclass ->
                allProps.addAll(getAllProperties(superclass))
            }
        }

        return allProps
    }

    /**
     * Validate a property value against its metadata.
     */
    private fun validatePropertyValue(property: MetaProperty, value: Any?) {
        if (value == null) return // Null checking handled elsewhere

        // Additional validation for collections
        if (property.isUnique && value is Collection<*>) {
            if (value.size != value.toSet().size) {
                throw IllegalArgumentException(
                    "Property '${property.name}' must contain unique values"
                )
            }
        }
    }

    /**
     * Evaluate a derived property using its derivation constraint.
     * Placeholder - would integrate with OCL/constraint engine.
     */
    private fun evaluateDerivedProperty(instance: MofObject, property: MetaProperty): Any? {
        logger.warn { "Derived property evaluation not yet implemented: ${property.name}" }
        // TODO: Integrate with OCL evaluator
        return null
    }

    /**
     * Evaluate a constraint on an instance.
     * Placeholder - would integrate with OCL/constraint engine.
     */
    private fun evaluateConstraint(instance: MofObject, constraint: MetaConstraint): Boolean {
        logger.warn { "Constraint evaluation not yet implemented: ${constraint.name}" }
        // TODO: Integrate with OCL evaluator
        return true // Assume valid for now
    }

    /**
     * Get all instances.
     */
    fun getAllInstances(): Collection<MofObject> = instances.values

    /**
     * Invoke an operation on an instance.
     *
     * @param instance The instance to invoke the operation on
     * @param operationName The name of the operation to invoke
     * @param arguments Map of parameter names to values
     * @return The result of the operation invocation
     */
    fun invokeOperation(
        instance: MofObject,
        operationName: String,
        arguments: Map<String, Any?> = emptyMap()
    ): Any? {
        val operation = findOperation(instance.metaClass, operationName)
            ?: throw IllegalArgumentException(
                "Operation '$operationName' not found in class: ${instance.className}"
            )

        // Validate arguments match parameters
        validateArguments(operation, arguments)

        // Execute the operation body
        val result = evaluateOperation(instance, operation, arguments)

        logger.debug { "Invoked operation $operationName on ${instance.className} instance" }
        return result
    }

    /**
     * Find an operation in a metaclass or its superclasses.
     */
    private fun findOperation(metaClass: MetaClass, operationName: String): MetaOperation? {
        // Check direct operations
        metaClass.operations.firstOrNull { it.name == operationName }?.let { return it }

        // Check inherited operations
        metaClass.superclasses.forEach { superclassName ->
            registry.getClass(superclassName)?.let { superclass ->
                findOperation(superclass, operationName)?.let { return it }
            }
        }

        return null
    }

    /**
     * Validate that provided arguments match the operation's parameters.
     */
    private fun validateArguments(operation: MetaOperation, arguments: Map<String, Any?>) {
        // Check for required parameters
        operation.parameters.forEach { param ->
            if (param.defaultValue == null && !arguments.containsKey(param.name)) {
                throw IllegalArgumentException(
                    "Missing required parameter '${param.name}' for operation '${operation.name}'"
                )
            }
        }

        // Check for unknown arguments
        arguments.keys.forEach { argName ->
            if (operation.parameters.none { it.name == argName }) {
                throw IllegalArgumentException(
                    "Unknown parameter '$argName' for operation '${operation.name}'"
                )
            }
        }
    }

    /**
     * Evaluate an operation body.
     * Supports simple property access expressions and will be extended for more complex bodies.
     */
    private fun evaluateOperation(
        instance: MofObject,
        operation: MetaOperation,
        arguments: Map<String, Any?>
    ): Any? {
        val body = operation.body ?: return null

        // For simple cases where body is just a property name, return that property's value
        if (operation.parameters.isEmpty() && body.matches(Regex("^[a-zA-Z_][a-zA-Z0-9_]*$"))) {
            return instance.getProperty(body)
        }

        // TODO: Integrate with OCL or expression evaluator for complex bodies
        logger.warn {
            "Complex operation body evaluation not yet implemented: ${operation.name}. " +
            "Body: $body"
        }
        return null
    }
}
