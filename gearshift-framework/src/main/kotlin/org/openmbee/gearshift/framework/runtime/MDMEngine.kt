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
package org.openmbee.gearshift.framework.runtime

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.gearshift.framework.constraints.ConstraintEngine
import org.openmbee.gearshift.framework.constraints.ConstraintRegistry
import org.openmbee.gearshift.framework.constraints.EngineAccessor
import org.openmbee.gearshift.framework.constraints.ValidationResults
import org.openmbee.gearshift.framework.constraints.ocl.OclExecutor
import org.openmbee.gearshift.framework.constraints.ocl.OclExpression
import org.openmbee.gearshift.framework.constraints.ocl.OclParser
import org.openmbee.gearshift.framework.meta.*
import org.openmbee.gearshift.framework.storage.LinkRepository
import org.openmbee.gearshift.framework.storage.ModelRepository
import java.util.*

private val logger = KotlinLogging.logger {}

/**
 * MDM (Model Data Management) Engine for managing metamodel-driven objects and their relationships.
 * Provides runtime support for:
 * - Object (node) creation and property management
 * - Link (edge) creation and traversal
 * - Constraint validation
 * - Operation invocation
 *
 * In the graph model:
 * - MDMObject instances are nodes
 * - MDMLink instances are edges
 * - MetaClass defines node types
 * - MetaAssociation defines edge types
 */
class MDMEngine(
    val registry: MetamodelRegistry,
    val objectRepository: ModelRepository,
    val linkRepository: LinkRepository
) {
    private val instances = mutableMapOf<String, MDMObject>()

    /**
     * Constraint registry for derived properties and validation constraints.
     */
    val constraintRegistry = ConstraintRegistry()

    /**
     * Name resolver for qualified name lookups.
     */
    val nameResolver = NameResolver(objectRepository, registry).also {
        // Set the engine reference after construction to enable proper property/link resolution
        it.setEngine(this)
    }

    /**
     * Engine accessor for constraint evaluation.
     */
    private val engineAccessor = object : EngineAccessor {
        override fun getInstance(id: String): MDMObject? = objectRepository.get(id)

        override fun getLinkedTargets(navigationName: String, sourceId: String): List<MDMObject> =
            this@MDMEngine.navigateByEndName(navigationName, sourceId, forward = true)

        override fun getLinkedSources(navigationName: String, targetId: String): List<MDMObject> =
            this@MDMEngine.navigateByEndName(navigationName, targetId, forward = false)

        override fun getProperty(instanceId: String, propertyName: String): Any? {
            val instance = objectRepository.get(instanceId) ?: return null
            return this@MDMEngine.getProperty(instance, propertyName)
        }

        override fun isSubclassOf(subclass: String, superclass: String): Boolean =
            registry.isSubclassOf(subclass, superclass)

        override fun invokeOperation(instanceId: String, operationName: String, arguments: Map<String, Any?>): Any? {
            val instance = objectRepository.get(instanceId) ?: return null
            return this@MDMEngine.invokeOperation(instance, operationName, arguments)
        }

        override fun resolveGlobal(qualifiedName: String): MDMObject? {
            // Find any root namespace to start resolution from
            val rootNamespace = objectRepository.getAll().firstOrNull { obj ->
                val owner = obj.getProperty("owner")
                owner == null && (obj.className == "Namespace" || obj.className == "Package" ||
                        obj.className == "LibraryPackage" || registry.isSubclassOf(obj.className, "Namespace"))
            } ?: return null

            // Use the NameResolver to resolve the qualified name
            val result = nameResolver.resolve(qualifiedName, rootNamespace.id!!, false)
            return result?.membership
        }
    }

    /**
     * Constraint engine for evaluating derived properties and validation constraints.
     */
    val constraintEngine = ConstraintEngine(constraintRegistry, registry, engineAccessor)

    /**
     * Kotlin script executor for operations with inline Kotlin implementation.
     */
    private val kotlinScriptExecutor by lazy { KotlinScriptExecutor(engineAccessor) }

    // ===== Lifecycle Hooks =====

    /**
     * Registered lifecycle handlers, sorted by priority.
     */
    private val lifecycleHandlers = mutableListOf<LifecycleHandler>()

    /**
     * Flag to prevent recursive event firing during handler execution.
     * When true, events are still fired but handlers can check this to avoid infinite loops.
     */
    private var firingEvent = false

    /**
     * Register a lifecycle handler to receive model change events.
     *
     * Handlers are invoked in priority order (lower values first).
     * Multiple handlers can be registered and will all be called for each event.
     *
     * @param handler The handler to register
     */
    fun addLifecycleHandler(handler: LifecycleHandler) {
        lifecycleHandlers.add(handler)
        lifecycleHandlers.sortBy { it.priority }
        logger.debug { "Registered lifecycle handler: ${handler::class.simpleName} (priority=${handler.priority})" }
    }

    /**
     * Remove a previously registered lifecycle handler.
     *
     * @param handler The handler to remove
     * @return true if the handler was found and removed
     */
    fun removeLifecycleHandler(handler: LifecycleHandler): Boolean {
        val removed = lifecycleHandlers.remove(handler)
        if (removed) {
            logger.debug { "Removed lifecycle handler: ${handler::class.simpleName}" }
        }
        return removed
    }

    /**
     * Fire a lifecycle event to all registered handlers.
     *
     * Handlers are invoked synchronously in priority order.
     * Exceptions from handlers are logged but do not prevent other handlers from running.
     *
     * @param event The event to fire
     */
    private fun fireEvent(event: LifecycleEvent) {
        if (lifecycleHandlers.isEmpty()) return

        val wasAlreadyFiring = firingEvent
        firingEvent = true

        try {
            for (handler in lifecycleHandlers) {
                try {
                    handler.handle(event, this)
                } catch (e: Exception) {
                    logger.error(e) {
                        "Lifecycle handler ${handler::class.simpleName} threw exception for ${event::class.simpleName}"
                    }
                }
            }
        } finally {
            firingEvent = wasAlreadyFiring
        }
    }

    /**
     * Check if we're currently firing an event.
     * Handlers can use this to avoid infinite recursion.
     */
    fun isFiringEvent(): Boolean = firingEvent

    // ===== Instance Management =====

    /**
     * Create a new instance of a metaclass.
     *
     * @param className The name of the metaclass to instantiate
     * @param id Optional custom ID for the instance. If not provided, a UUID will be generated.
     */
    fun createInstance(
        className: String,
        id: String? = null
    ): MDMObject {
        val metaClass = registry.getClass(className)
            ?: throw IllegalArgumentException("Unknown class: $className")

        if (metaClass.isAbstract) {
            throw IllegalArgumentException("Cannot instantiate abstract class: $className")
        }

        val instance = MDMObject(className, metaClass)
        val instanceId = id ?: UUID.randomUUID().toString()
        instance.id = instanceId
        instances[instanceId] = instance
        objectRepository.store(instanceId, instance)

        logger.debug { "Created instance of $className: $instanceId" }

        // Fire lifecycle event - handlers can create implied relationships
        println("MDMEngine.createInstance: Firing InstanceCreated for $className, handlers=${lifecycleHandlers.size}")
        fireEvent(LifecycleEvent.InstanceCreated(instance, metaClass))

        return instance
    }

    /**
     * Set a property value on an instance.
     * Handles both attributes and association ends uniformly.
     *
     * For attributes, sets the value directly on the instance.
     *
     * For non-derived association ends, the value can be:
     * - MDMObject (for single-valued ends)
     * - List<MDMObject> or Set<MDMObject> (for multi-valued ends)
     * - null (to clear the association)
     *
     * Links are automatically created/removed based on the value.
     * Derived association ends cannot be set (they are computed via OCL constraints).
     */
    fun setProperty(instance: MDMObject, propertyName: String, value: Any?) {
        // First try to find as an attribute
        val property = findProperty(instance.metaClass, propertyName)

        if (property != null) {
            // It's an attribute - use existing logic
            if (property.isReadOnly) {
                throw IllegalStateException("Cannot set read-only property: $propertyName")
            }

            validatePropertyValue(property, value)

            // Capture old value for event
            val oldValue = instance.getProperty(propertyName)

            instance.setProperty(propertyName, value)

            logger.debug { "Set property $propertyName on ${instance.className} instance" }

            // Fire lifecycle event if value actually changed
            if (oldValue != value) {
                fireEvent(LifecycleEvent.PropertyChanged(instance, propertyName, oldValue, value))
            }
            return
        }

        // Not an attribute - check if it's an association end
        val associationEnd = findAssociationEnd(instance.metaClass, propertyName)
        if (associationEnd != null) {
            setAssociationEndValue(instance, associationEnd, value)
            return
        }

        throw IllegalArgumentException(
            "Property or association end '$propertyName' not found in class: ${instance.className}"
        )
    }

    /**
     * Set the value of an association end by creating/removing links.
     *
     * @param instance The instance to modify
     * @param associationEnd Triple of (association, end, isTargetEnd)
     * @param value The new value (MDMObject, Collection<MDMObject>, or null)
     */
    private fun setAssociationEndValue(
        instance: MDMObject,
        associationEnd: Triple<MetaAssociation, MetaAssociationEnd, Boolean>,
        value: Any?
    ) {
        val (association, end, isTargetEnd) = associationEnd
        val instanceId = instance.id ?: throw IllegalStateException("Instance must have an ID")

        if (end.isDerived) {
            throw IllegalStateException("Cannot set derived association end: ${end.name}")
        }

        // Normalize value to a list of MDMObjects
        val newTargets: List<MDMObject> = when (value) {
            null -> emptyList()
            is MDMObject -> listOf(value)
            is Collection<*> -> value.filterIsInstance<MDMObject>()
            else -> throw IllegalArgumentException(
                "Invalid value type for association end '${end.name}': ${value::class.simpleName}. " +
                "Expected MDMObject, Collection<MDMObject>, or null."
            )
        }

        // Get current linked objects
        val currentLinked: List<MDMObject> = if (isTargetEnd) {
            getLinkedTargets(association.name, instanceId)
        } else {
            getLinkedSources(association.name, instanceId)
        }

        // Compute what needs to be removed and added
        val currentIds = currentLinked.mapNotNull { it.id }.toSet()
        val newIds = newTargets.mapNotNull { it.id }.toSet()

        val toRemove = currentIds - newIds
        val toAdd = newIds - currentIds

        logger.debug {
            "Setting association end '${end.name}' on ${instance.className}: " +
            "removing ${toRemove.size}, adding ${toAdd.size}"
        }

        // Remove old links
        for (targetId in toRemove) {
            if (isTargetEnd) {
                removeLink(association.name, instanceId, targetId)
            } else {
                removeLink(association.name, targetId, instanceId)
            }
        }

        // Add new links
        for (target in newTargets) {
            val targetId = target.id ?: continue
            if (targetId in toAdd) {
                if (isTargetEnd) {
                    createLink(association.name, instanceId, targetId)
                } else {
                    createLink(association.name, targetId, instanceId)
                }
            }
        }
    }

    /**
     * Get a property value from an instance.
     * Checks both attributes and association ends.
     */
    fun getProperty(instance: MDMObject, propertyName: String): Any? {
        // First try to find as a property (attribute)
        val property = findProperty(instance.metaClass, propertyName)

        if (property != null) {
            var value = instance.getProperty(propertyName)

            // Handle derived properties - check if we have a registered evaluator or a derivation constraint
            if (property.isDerived && value == null) {
                val instanceId = instance.id
                if (instanceId != null) {
                    // Try registered evaluator first
                    if (constraintEngine.hasDerivedPropertyEvaluator(instance.className, propertyName)) {
                        value = constraintEngine.evaluateDerivedProperty(instance, instanceId, property)
                    } else if (property.derivationConstraint != null) {
                        // Fall back to OCL expression evaluation (placeholder)
                        value = evaluateDerivedPropertyExpression(instance, property)
                    }
                }
            }

            return value
        }

        // If not found as property, check for association end
        val associationEnd = findAssociationEnd(instance.metaClass, propertyName)
        if (associationEnd != null) {
            val (association, end, isTargetEnd) = associationEnd

            // Handle derived association ends
            if (end.isDerived && end.derivationConstraint != null) {
                return evaluateDerivedAssociationEnd(instance, end)
            }

            // Non-derived association end - traverse the links
            val instanceId = instance.id ?: return null
            return if (isTargetEnd) {
                getLinkedTargets(association.name, instanceId)
            } else {
                getLinkedSources(association.name, instanceId)
            }
        }

        throw IllegalArgumentException(
            "Property or association end '$propertyName' not found in class: ${instance.className}"
        )
    }

    /**
     * Find an association end by name for a metaclass.
     * Returns a triple of (Association, AssociationEnd, isTargetEnd) or null if not found.
     */
    private fun findAssociationEnd(
        metaClass: MetaClass,
        endName: String
    ): Triple<MetaAssociation, MetaAssociationEnd, Boolean>? {
        val className = metaClass.name
        val allSuperclasses = registry.getAllSuperclasses(className) + className

        for (association in registry.getAllAssociations()) {
            // Check if this is the target end and the source type matches our class hierarchy
            if (association.targetEnd.name == endName) {
                if (allSuperclasses.contains(association.sourceEnd.type) ||
                    registry.isSubclassOf(association.sourceEnd.type, className)
                ) {
                    return Triple(association, association.targetEnd, true)
                }
            }
            // Check if this is the source end and the target type matches our class hierarchy
            if (association.sourceEnd.name == endName && association.sourceEnd.isNavigable) {
                if (allSuperclasses.contains(association.targetEnd.type) ||
                    registry.isSubclassOf(association.targetEnd.type, className)
                ) {
                    return Triple(association, association.sourceEnd, false)
                }
            }
        }

        return null
    }

    /**
     * Evaluate a derived association end using its OCL derivation constraint expression.
     */
    private fun evaluateDerivedAssociationEnd(instance: MDMObject, end: MetaAssociationEnd): Any? {
        val constraintName = end.derivationConstraint ?: return null
        val instanceId = instance.id ?: return null

        logger.debug { "Evaluating derived association end '${end.name}' via constraint '$constraintName'" }

        // Look up the named constraint from the metaclass (with redefines support)
        val constraint = findConstraint(instance.metaClass, constraintName)
        if (constraint == null) {
            logger.warn { "Constraint '$constraintName' not found for derived association end '${end.name}'" }
            return null
        }

        // Parse and evaluate the OCL expression
        return try {
            val ast = OclParser.parse(constraint.expression)
            val executor = OclExecutor(engineAccessor, instance, instanceId)
            executor.evaluate(ast)
        } catch (e: Exception) {
            logger.error(e) { "Error evaluating constraint '$constraintName': ${constraint.expression}" }
            null
        }
    }

    /**
     * Validate an instance against its metaclass constraints.
     */
    fun validate(instance: MDMObject): List<String> {
        val errors = mutableListOf<String>()
        val metaClass = instance.metaClass

        // Validate required properties
        getAllProperties(metaClass).forEach { property ->
            if (!property.isDerived) {
                val value = instance.getProperty(property.name)
                if (value == null && property.isRequired) {
                    errors.add("Required property '${property.name}' is not set")
                }
            }
        }

        // Evaluate only VERIFICATION constraints (not DERIVATION, IMPLICIT_SPECIALIZATION, etc.)
        metaClass.constraints
            .filter { it.type == ConstraintType.VERIFICATION }
            .forEach { constraint ->
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
     * Evaluate a derived property using its OCL derivation constraint expression.
     * Looks up the named constraint and evaluates its OCL expression.
     */
    private fun evaluateDerivedPropertyExpression(instance: MDMObject, property: MetaProperty): Any? {
        val constraintName = property.derivationConstraint ?: return null
        val instanceId = instance.id ?: return null

        logger.debug { "Evaluating derived property '${property.name}' via constraint '$constraintName'" }

        // Look up the named constraint from the metaclass (including inherited constraints)
        val constraint = findConstraint(instance.metaClass, constraintName)
        if (constraint == null) {
            logger.warn { "Constraint '$constraintName' not found for derived property '${property.name}'" }
            return null
        }

        // Parse and evaluate the OCL expression
        return try {
            val ast = OclParser.parse(constraint.expression)
            val executor = OclExecutor(engineAccessor, instance, instanceId)
            executor.evaluate(ast)
        } catch (e: Exception) {
            logger.error(e) { "Error evaluating constraint '$constraintName': ${constraint.expression}" }
            null
        }
    }

    /**
     * Find a constraint by name in a metaclass or its superclasses.
     * First looks for a constraint that redefines the requested constraint (polymorphic dispatch),
     * then falls back to finding the constraint by name.
     */
    private fun findConstraint(metaClass: MetaClass, constraintName: String): MetaConstraint? {
        // First, look for a constraint that redefines the requested constraint
        // This enables polymorphic constraint dispatch (e.g., MembershipImport redefines Import's constraint)
        findRedefiningConstraint(metaClass, constraintName)?.let { return it }

        // Fall back to finding constraint by name
        return findConstraintByName(metaClass, constraintName)
    }

    /**
     * Find a constraint that redefines the given constraint name, starting from the most specific class.
     */
    private fun findRedefiningConstraint(metaClass: MetaClass, constraintName: String): MetaConstraint? {
        // Check if this class has a constraint that redefines the target
        metaClass.constraints.firstOrNull { it.redefines == constraintName }?.let { return it }

        // Check superclasses (in case of multi-level inheritance)
        metaClass.superclasses.forEach { superclassName ->
            registry.getClass(superclassName)?.let { superclass ->
                findRedefiningConstraint(superclass, constraintName)?.let { return it }
            }
        }

        return null
    }

    /**
     * Find a constraint by exact name in a metaclass or its superclasses.
     */
    private fun findConstraintByName(metaClass: MetaClass, constraintName: String): MetaConstraint? {
        // Check direct constraints
        metaClass.constraints.firstOrNull { it.name == constraintName }?.let { return it }

        // Check inherited constraints
        metaClass.superclasses.forEach { superclassName ->
            registry.getClass(superclassName)?.let { superclass ->
                findConstraintByName(superclass, constraintName)?.let { return it }
            }
        }

        return null
    }

    /**
     * Evaluate a MetaConstraint on an instance.
     * Uses registered evaluator if available, otherwise falls back to OCL parsing.
     */
    private fun evaluateConstraint(instance: MDMObject, constraint: MetaConstraint): Boolean {
        val instanceId = instance.id

        // Check for registered evaluator first
        if (instanceId != null && constraintRegistry.hasValidationConstraint(instance.className, constraint.name)) {
            val result = constraintEngine.validateConstraint(instance, instanceId, constraint.name)
            return result.isValid
        }

        // Fall back to OCL expression evaluation
        val expression = constraint.expression ?: return true
        logger.debug { "Evaluating constraint expression: ${constraint.name} = $expression" }

        return try {
            val ast = OclParser.parse(expression)
            val executor = OclExecutor(engineAccessor, instance, instanceId ?: "")
            val result = executor.evaluate(ast)
            result == true
        } catch (e: Exception) {
            logger.error(e) { "OCL constraint evaluation failed: ${constraint.name}" }
            false
        }
    }

    /**
     * Evaluate an OCL expression string on an instance.
     * Returns the result of the evaluation, or null if evaluation fails.
     */
    fun evaluateOclExpression(instance: MDMObject, expression: String): Any? {
        return try {
            val ast = OclParser.parse(expression)
            val executor = OclExecutor(engineAccessor, instance, instance.id ?: "")
            executor.evaluate(ast)
        } catch (e: Exception) {
            logger.error(e) { "OCL expression evaluation failed: $expression" }
            null
        }
    }

    /**
     * Evaluate an OCL AST on an instance.
     * Returns the result of the evaluation, or null if evaluation fails.
     */
    fun evaluateOclAst(instance: MDMObject, ast: OclExpression): Any? {
        return try {
            val executor = OclExecutor(engineAccessor, instance, instance.id ?: "")
            executor.evaluate(ast)
        } catch (e: Exception) {
            logger.error(e) { "OCL AST evaluation failed" }
            null
        }
    }

    /**
     * Validate an instance using both MetaConstraints and registered validation constraints.
     *
     * @param instanceId The instance ID
     * @return Combined validation results from MetaConstraints and registered constraints
     */
    fun validateWithConstraints(instanceId: String): ValidationResults {
        val instance = objectRepository.get(instanceId)
            ?: return ValidationResults.fromResults(
                listOf(
                    org.openmbee.gearshift.framework.constraints.ValidationResult.invalid("Instance not found: $instanceId")
                )
            )

        return constraintEngine.validateInstance(instance, instanceId)
    }

    /**
     * Get all instances.
     */
    fun getAllInstances(): Collection<MDMObject> = instances.values

    /**
     * Invoke an operation on an instance.
     *
     * @param instance The instance to invoke the operation on
     * @param operationName The name of the operation to invoke
     * @param arguments Map of parameter names to values
     * @return The result of the operation invocation
     */
    fun invokeOperation(
        instance: MDMObject,
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
        instance: MDMObject,
        operation: MetaOperation,
        arguments: Map<String, Any?>
    ): Any? {
        val body = operation.body ?: return null

        return when (operation.bodyLanguage) {
            BodyLanguage.PROPERTY_REF -> {
                // Simple property reference - just return the property value
                instance.getProperty(body)
            }

            BodyLanguage.OCL -> {
                // Parse and evaluate OCL expression using ANTLR parser
                try {
                    val ast = OclParser.parse(body)
                    val instanceId = instance.id
                        ?: throw IllegalStateException("Instance must have an ID for OCL evaluation")
                    val executor = OclExecutor(engineAccessor, instance, instanceId)

                    // Add operation arguments to the executor's variable scope
                    executor.evaluateWith(ast, arguments)
                } catch (e: Exception) {
                    logger.error(e) { "OCL evaluation failed for operation: ${operation.name}" }
                    null
                }
            }

            BodyLanguage.GQL -> {
                // TODO: Integrate GQL evaluator
                logger.warn { "GQL evaluation not yet implemented for operation: ${operation.name}" }
                null
            }

            BodyLanguage.KOTLIN_DSL -> {
                // Execute Kotlin DSL body using the script executor
                try {
                    kotlinScriptExecutor.execute(body, instance, arguments)
                } catch (e: Exception) {
                    logger.error(e) { "Kotlin DSL execution failed for operation: ${operation.name}" }
                    throw e
                }
            }
        }
    }

    // ===== Link (Edge) Management =====

    /**
     * Create a link between two instances via an association.
     *
     * @param associationName The name of the MetaAssociation defining this relationship
     * @param sourceId ID of the source instance
     * @param targetId ID of the target instance
     * @return The created MDMLink
     * @throws IllegalArgumentException if association not found, instances not found, or type mismatch
     * @throws IllegalStateException if multiplicity constraints would be violated
     */
    fun createLink(associationName: String, sourceId: String, targetId: String): MDMLink {
        val association = registry.getAssociation(associationName)
            ?: throw IllegalArgumentException("Unknown association: $associationName")

        val source = objectRepository.get(sourceId)
            ?: throw IllegalArgumentException("Source instance not found: $sourceId")

        val target = objectRepository.get(targetId)
            ?: throw IllegalArgumentException("Target instance not found: $targetId")

        // Validate types match association ends
        validateLinkTypes(association, source, target)

        // Check for duplicate link first (before multiplicity to give better error)
        // Enforce uniqueness if EITHER end has isUnique=true:
        // - targetEnd.isUnique: prevents duplicate targets when navigating source→target
        // - sourceEnd.isUnique: prevents duplicate sources when navigating target→source
        // When both ends have isUnique=false (Bag semantics), duplicate links are allowed
        val requiresUniqueness = association.targetEnd.isUnique || association.sourceEnd.isUnique
        if (requiresUniqueness && linkRepository.linkExists(associationName, sourceId, targetId)) {
            throw IllegalStateException(
                "Link already exists: $sourceId --[$associationName]--> $targetId " +
                "(sourceEnd.isUnique=${association.sourceEnd.isUnique}, targetEnd.isUnique=${association.targetEnd.isUnique})"
            )
        }

        // Validate multiplicity constraints
        validateLinkMultiplicity(association, sourceId, targetId)

        val link = MDMLink(
            id = UUID.randomUUID().toString(),
            association = association,
            sourceId = sourceId,
            targetId = targetId
        )

        linkRepository.store(link)
        logger.debug { "Created link: $sourceId --[$associationName]--> $targetId" }

        // Fire lifecycle event - handlers can react to new relationships
        fireEvent(LifecycleEvent.LinkCreated(link, source, target, association))

        return link
    }

    /**
     * Navigate to related instances by association end name.
     * This is the OCL-style navigation where property names refer to association ends.
     *
     * @param endName The name of the association end to navigate
     * @param instanceId The instance to navigate from
     * @param forward If true, navigate source->target; if false, navigate target->source
     * @return List of related MDMObjects
     */
    fun navigateByEndName(endName: String, instanceId: String, forward: Boolean): List<MDMObject> {
        val instance = objectRepository.get(instanceId) ?: return emptyList()
        val metaClass = instance.metaClass

        // Find the association by end name
        val associationInfo = findAssociationEnd(metaClass, endName) ?: return emptyList()
        val (association, end, isTargetEnd) = associationInfo

        // Determine if we're navigating in the direction specified
        val navigatingToTarget = if (forward) isTargetEnd else !isTargetEnd

        // Check if the end is derived
        if (end.isDerived && end.derivationConstraint != null) {
            // Evaluate the derivation constraint
            val result = evaluateDerivedAssociationEnd(instance, end)
            return when (result) {
                is Collection<*> -> result.filterIsInstance<MDMObject>()
                is MDMObject -> listOf(result)
                else -> emptyList()
            }
        }

        // Non-derived: traverse actual links
        // Also collect links from associations that subset this one (supporting subset chains)
        val allAssociations = mutableListOf(association)
        findSubsettingAssociations(endName, allAssociations, mutableSetOf())

        val results = mutableListOf<MDMObject>()
        for (assoc in allAssociations) {
            val links = if (navigatingToTarget) {
                linkRepository.getByAssociationAndSource(assoc.name, instanceId)
            } else {
                linkRepository.getByAssociationAndTarget(assoc.name, instanceId)
            }
            for (link in links) {
                val targetObj = if (navigatingToTarget) {
                    objectRepository.get(link.targetId)
                } else {
                    objectRepository.get(link.sourceId)
                }
                targetObj?.let { results.add(it) }
            }
        }

        return results
    }

    /**
     * Find all associations whose ends subset the given end name (recursively).
     */
    private fun findSubsettingAssociations(
        endName: String,
        result: MutableList<MetaAssociation>,
        visited: MutableSet<String>
    ) {
        if (endName in visited) return
        visited.add(endName)

        logger.trace { "findSubsettingAssociations: looking for associations that subset '$endName'" }

        for (assoc in registry.getAllAssociations()) {
            // Check if target end subsets this end name
            if (assoc.targetEnd.subsets.contains(endName) && assoc !in result) {
                logger.trace { "  Found: ${assoc.name} (targetEnd '${assoc.targetEnd.name}' subsets '$endName')" }
                result.add(assoc)
                // Recursively find associations that subset this one
                findSubsettingAssociations(assoc.targetEnd.name, result, visited)
            }
            // Check if source end subsets this end name
            if (assoc.sourceEnd.subsets.contains(endName) && assoc !in result) {
                logger.trace { "  Found: ${assoc.name} (sourceEnd '${assoc.sourceEnd.name}' subsets '$endName')" }
                result.add(assoc)
                findSubsettingAssociations(assoc.sourceEnd.name, result, visited)
            }
        }
    }

    /**
     * Get all target instances linked from a source via an association.
     * Traverses in the forward direction (source -> target).
     *
     * @param associationName The association to traverse
     * @param sourceId The source instance ID
     * @return List of target MDMObjects
     */
    fun getLinkedTargets(associationName: String, sourceId: String): List<MDMObject> {
        val association = registry.getAssociation(associationName)
            ?: throw IllegalArgumentException("Unknown association: $associationName")

        if (!association.targetEnd.isNavigable) {
            throw IllegalStateException(
                "Association '$associationName' is not navigable from source to target"
            )
        }

        return linkRepository.getByAssociationAndSource(associationName, sourceId)
            .mapNotNull { objectRepository.get(it.targetId) }
    }

    /**
     * Get all source instances linked to a target via an association.
     * Traverses in the reverse direction (target -> source).
     *
     * @param associationName The association to traverse
     * @param targetId The target instance ID
     * @return List of source MDMObjects
     */
    fun getLinkedSources(associationName: String, targetId: String): List<MDMObject> {
        val association = registry.getAssociation(associationName)
            ?: throw IllegalArgumentException("Unknown association: $associationName")

        if (!association.sourceEnd.isNavigable) {
            throw IllegalStateException(
                "Association '$associationName' is not navigable from target to source"
            )
        }

        return linkRepository.getByAssociationAndTarget(associationName, targetId)
            .mapNotNull { objectRepository.get(it.sourceId) }
    }

    /**
     * Remove a link between two instances.
     *
     * @param associationName The association name
     * @param sourceId Source instance ID
     * @param targetId Target instance ID
     * @return true if link was removed, false if not found
     */
    fun removeLink(associationName: String, sourceId: String, targetId: String): Boolean {
        val link = linkRepository.findLink(associationName, sourceId, targetId)
            ?: return false

        val association = registry.getAssociation(associationName)
        val source = objectRepository.get(sourceId)
        val target = objectRepository.get(targetId)

        // Fire lifecycle event before deletion
        if (association != null && source != null && target != null) {
            fireEvent(LifecycleEvent.LinkDeleting(link, source, target, association))
        }

        linkRepository.delete(link.id)
        logger.debug { "Removed link: $sourceId --[$associationName]--> $targetId" }

        return true
    }

    /**
     * Remove all links involving an instance.
     * Used for cascade delete when removing an instance.
     *
     * @param instanceId The instance ID
     * @return List of removed links
     */
    fun removeAllLinks(instanceId: String): List<MDMLink> {
        val removed = linkRepository.deleteByInstance(instanceId)
        if (removed.isNotEmpty()) {
            logger.debug { "Removed ${removed.size} links involving instance $instanceId" }
        }
        return removed
    }

    /**
     * Get all links from/to an instance.
     *
     * @param instanceId The instance ID
     * @return All links where instance is source or target
     */
    fun getLinks(instanceId: String): List<MDMLink> {
        return linkRepository.getByInstance(instanceId)
    }

    /**
     * Get all outgoing links from an instance.
     */
    fun getOutgoingLinks(instanceId: String): List<MDMLink> {
        return linkRepository.getBySource(instanceId)
    }

    /**
     * Get all incoming links to an instance.
     */
    fun getIncomingLinks(instanceId: String): List<MDMLink> {
        return linkRepository.getByTarget(instanceId)
    }

    /**
     * Delete an instance and handle composite aggregation cascading.
     * If the instance owns composite parts, those are also deleted.
     *
     * @param instanceId The instance to delete
     * @return List of all deleted instance IDs (including cascaded deletes)
     */
    fun deleteInstanceWithCascade(instanceId: String): List<String> {
        val deleted = mutableListOf<String>()
        deleteInstanceRecursive(instanceId, deleted)
        return deleted
    }

    private fun deleteInstanceRecursive(instanceId: String, deleted: MutableList<String>) {
        if (instanceId in deleted) return // Already deleted

        val instance = objectRepository.get(instanceId)

        // Fire lifecycle event before deletion
        if (instance != null) {
            fireEvent(LifecycleEvent.InstanceDeleting(instance))
        }

        // Find composite parts (where this instance is the composite owner)
        val outgoingLinks = linkRepository.getBySource(instanceId)
        val compositeParts = outgoingLinks.filter { it.isTargetComposite }

        // Recursively delete composite parts first
        compositeParts.forEach { link ->
            deleteInstanceRecursive(link.targetId, deleted)
        }

        // Remove all links involving this instance
        removeAllLinks(instanceId)

        // Delete the instance itself
        objectRepository.delete(instanceId)
        instances.remove(instanceId)
        deleted.add(instanceId)

        logger.debug { "Deleted instance $instanceId (cascade)" }
    }

    /**
     * Validate that source and target types match the association's expected types.
     */
    private fun validateLinkTypes(
        association: MetaAssociation,
        source: MDMObject,
        target: MDMObject
    ) {
        val sourceTypeValid = isInstanceOfType(source, association.sourceEnd.type)
        if (!sourceTypeValid) {
            throw IllegalArgumentException(
                "Source instance type '${source.className}' is not compatible with " +
                        "association source end type '${association.sourceEnd.type}'"
            )
        }

        val targetTypeValid = isInstanceOfType(target, association.targetEnd.type)
        if (!targetTypeValid) {
            throw IllegalArgumentException(
                "Target instance type '${target.className}' is not compatible with " +
                        "association target end type '${association.targetEnd.type}'"
            )
        }
    }

    /**
     * Check if an instance is of a given type (including subclasses).
     */
    private fun isInstanceOfType(instance: MDMObject, typeName: String): Boolean {
        return instance.className == typeName ||
                registry.isSubclassOf(instance.className, typeName)
    }

    /**
     * Validate that creating this link won't violate multiplicity constraints.
     */
    private fun validateLinkMultiplicity(
        association: MetaAssociation,
        sourceId: String,
        targetId: String
    ) {
        // Check source end multiplicity (how many sources can link to this target)
        val sourceEnd = association.sourceEnd
        if (sourceEnd.upperBound != -1) { // -1 means unbounded
            val currentSourceCount = linkRepository.countByAssociationAndTarget(
                association.name, targetId
            )
            if (currentSourceCount >= sourceEnd.upperBound) {
                throw IllegalStateException(
                    "Cannot create link: target '$targetId' already has maximum " +
                            "${sourceEnd.upperBound} sources via '${association.name}'"
                )
            }
        }

        // Check target end multiplicity (how many targets can this source link to)
        val targetEnd = association.targetEnd
        if (targetEnd.upperBound != -1) {
            val currentTargetCount = linkRepository.countByAssociationAndSource(
                association.name, sourceId
            )
            if (currentTargetCount >= targetEnd.upperBound) {
                throw IllegalStateException(
                    "Cannot create link: source '$sourceId' already has maximum " +
                            "${targetEnd.upperBound} targets via '${association.name}'"
                )
            }
        }
    }

    /**
     * Validate all links for an instance against association constraints.
     * Returns list of validation errors.
     */
    fun validateLinks(instanceId: String): List<String> {
        val errors = mutableListOf<String>()
        val instance = objectRepository.get(instanceId)
            ?: return listOf("Instance not found: $instanceId")

        // Check required associations (lowerBound > 0)
        registry.getAllAssociations().forEach { association ->
            // Check if this instance type participates in this association
            if (isInstanceOfType(instance, association.sourceEnd.type)) {
                val targetCount = linkRepository.countByAssociationAndSource(
                    association.name, instanceId
                )
                if (targetCount < association.targetEnd.lowerBound) {
                    errors.add(
                        "Instance '$instanceId' requires at least ${association.targetEnd.lowerBound} " +
                                "target(s) via '${association.name}', but has $targetCount"
                    )
                }
            }

            if (isInstanceOfType(instance, association.targetEnd.type)) {
                val sourceCount = linkRepository.countByAssociationAndTarget(
                    association.name, instanceId
                )
                if (sourceCount < association.sourceEnd.lowerBound) {
                    errors.add(
                        "Instance '$instanceId' requires at least ${association.sourceEnd.lowerBound} " +
                                "source(s) via '${association.name}', but has $sourceCount"
                    )
                }
            }
        }

        return errors
    }
}
