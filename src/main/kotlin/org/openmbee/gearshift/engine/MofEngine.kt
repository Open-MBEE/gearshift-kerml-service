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
}
