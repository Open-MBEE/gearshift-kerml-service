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
import org.openmbee.gearshift.framework.meta.AggregationKind
import org.openmbee.gearshift.framework.meta.MetaClass
import org.openmbee.gearshift.framework.meta.MetaConstraint
import org.openmbee.gearshift.framework.meta.MetaProperty
import java.util.UUID
import kotlin.collections.ArrayDeque

private val logger = KotlinLogging.logger {}

/**
 * The core runtime model container.
 *
 * MDMEngine is the single source of truth for:
 * - Element storage and access
 * - Association graph (links between elements)
 * - Property access (stored, derived, and association-based)
 * - Operation invocation
 * - Validation
 * - Lifecycle events
 *
 * This consolidates what was previously split across MDMEngine, GearshiftEngine,
 * and various repositories.
 *
 * Expression evaluation is pluggable - register evaluators for different languages
 * (OCL, GQL, Kotlin DSL) to support dynamic language extension.
 */
class MDMEngine(
    /** The schema (metamodel registry) this model uses for metaclass definitions */
    val schema: MetamodelRegistry,
    /** The factory used to create element instances (allows typed implementations) */
    elementFactory: ElementFactory = DefaultElementFactory
) {
    /** Element instances by ID */
    private val elements: MutableMap<String, MDMObject> = mutableMapOf()

    /** The association graph */
    private val graph: MDMGraph = MDMGraph()

    /** Registered lifecycle handlers */
    private val lifecycleHandlers: MutableList<LifecycleHandler> = mutableListOf()

    /** Registered expression evaluators by language */
    private val evaluators: MutableMap<String, ExpressionEvaluator> = mutableMapOf()

    /** The element factory - can be changed at runtime */
    var factory: ElementFactory = elementFactory
        private set

    init {
        // Register default expression evaluators
        registerEvaluator("OCL", OclExpressionEvaluator())
    }

    /**
     * Set a new element factory.
     */
    fun setElementFactory(factory: ElementFactory) {
        this.factory = factory
    }

    // ===== Public API - Element Access =====

    /**
     * Get an element by ID.
     */
    fun getElement(id: String): MDMObject? = elements[id]

    /**
     * Get all elements in the model.
     */
    fun getAllElements(): List<MDMObject> = elements.values.toList()

    /**
     * Get all elements of a specific class (including subclasses).
     */
    fun getElementsByClass(className: String): List<MDMObject> =
        elements.values.filter { isInstanceOf(it, className) }

    /**
     * Get all element IDs.
     */
    fun getElementIds(): Set<String> = elements.keys.toSet()

    /**
     * Get the count of elements.
     */
    fun elementCount(): Int = elements.size

    /**
     * Create a new element of the specified class.
     * Uses the registered ElementFactory to create the appropriate typed instance.
     */
    fun createElement(className: String): MDMObject {
        val metaClass = schema.getClass(className)
            ?: throw IllegalArgumentException("Unknown class: $className")

        val element = factory.createInstance(className, metaClass, this)
        val id = UUID.randomUUID().toString()
        element.id = id
        elements[id] = element

        fireEvent(LifecycleEvent.InstanceCreated(element, metaClass))

        return element
    }

    /**
     * Remove an element and all its links.
     */
    fun removeElement(id: String): Boolean {
        val element = elements.remove(id) ?: return false
        fireEvent(LifecycleEvent.InstanceDeleting(element))
        graph.removeEdgesForElement(id)
        return true
    }

    /**
     * Delete an instance and cascade delete all owned/composite elements.
     * Follows composite associations to find elements that should be deleted.
     *
     * @param id The ID of the element to delete
     * @return List of IDs that were deleted (including the original)
     */
    fun deleteInstanceWithCascade(id: String): List<String> {
        val deleted = mutableListOf<String>()
        val toDelete = ArrayDeque<String>()
        toDelete.add(id)

        while (toDelete.isNotEmpty()) {
            val currentId = toDelete.removeFirst()
            if (currentId in deleted) continue

            val element = elements[currentId] ?: continue

            // Find all outgoing composite links (owned elements)
            val outgoingLinks = graph.getLinksForElement(currentId).filter { it.sourceId == currentId }
            for (link in outgoingLinks) {
                // Check if this is a composite/owning association
                val association = schema.getAssociation(link.associationName)
                if (association != null && association.targetEnd.aggregation == AggregationKind.COMPOSITE) {
                    toDelete.add(link.targetId)
                }
            }

            // Delete the element
            if (removeElement(currentId)) {
                deleted.add(currentId)
            }
        }

        return deleted
    }

    /**
     * Get all root namespaces (elements with no owner).
     * Note: This is a convenience for KerML but implemented generically.
     */
    fun getRootNamespaces(): List<MDMObject> =
        elements.values.filter { element ->
            val owner = getPropertyValue(element, "owner")
            owner == null && schema.isSubclassOf(element.className, "Namespace")
        }

    /**
     * Check if an element is an instance of a class (including inheritance).
     */
    fun isInstanceOf(element: MDMObject, className: String): Boolean =
        element.className == className || schema.isSubclassOf(element.className, className)

    /**
     * Clear all elements and links.
     */
    fun clear() {
        elements.clear()
        graph.clear()
    }

    // ===== Compatibility API (for generated code and migration) =====

    /** Alias for schema */
    val metamodelRegistry: MetamodelRegistry get() = schema

    /**
     * Create instance with optional custom ID (GearshiftEngine compatibility).
     * Returns Pair of (id, object) for compatibility with old API.
     * Uses the registered ElementFactory to create the appropriate typed instance.
     */
    fun createInstance(className: String, id: String? = null): Pair<String, MDMObject> {
        val metaClass = schema.getClass(className)
            ?: throw IllegalArgumentException("Unknown class: $className")

        val element = factory.createInstance(className, metaClass, this)
        val elementId = id ?: UUID.randomUUID().toString()
        element.id = elementId
        elements[elementId] = element

        fireEvent(LifecycleEvent.InstanceCreated(element, metaClass))

        return elementId to element
    }

    /** Alias for getElement (GearshiftEngine compatibility) */
    fun getInstance(id: String): MDMObject? = getElement(id)

    /**
     * Create a link (GearshiftEngine compatibility - takes associationName first).
     */
    fun createLink(associationName: String, sourceId: String, targetId: String): MDMLink {
        val association = schema.getAssociation(associationName)
            ?: throw IllegalArgumentException("Unknown association: $associationName")

        val linkId = UUID.randomUUID().toString()
        val link = MDMLink(linkId, association, sourceId, targetId)
        graph.addEdge(link)

        val source = elements[sourceId]
        val target = elements[targetId]
        if (source != null && target != null) {
            fireEvent(LifecycleEvent.LinkCreated(link, source, target, association))
        }

        return link
    }

    /**
     * Get linked targets via association (GearshiftEngine compatibility).
     */
    fun getLinkedTargets(associationName: String, sourceId: String): List<MDMObject> {
        return graph.getTargets(sourceId, associationName).mapNotNull { elements[it] }
    }

    /**
     * Get linked sources via association (GearshiftEngine compatibility).
     */
    fun getLinkedSources(associationName: String, targetId: String): List<MDMObject> {
        return graph.getSources(targetId, associationName).mapNotNull { elements[it] }
    }

    /**
     * Remove a link (GearshiftEngine compatibility).
     */
    fun removeLink(associationName: String, sourceId: String, targetId: String): Boolean {
        val link = graph.findEdge(sourceId, targetId, associationName) ?: return false
        graph.removeEdge(link.id)
        return true
    }

    /**
     * Get all links for an element.
     */
    fun getLinks(instanceId: String): List<MDMLink> {
        return graph.getLinksForElement(instanceId)
    }

    /**
     * Remove all links for an element.
     */
    fun removeAllLinks(instanceId: String) {
        graph.removeEdgesForElement(instanceId)
    }

    /**
     * Get instances by exact type (GearshiftEngine compatibility).
     */
    fun getInstancesByType(className: String): List<MDMObject> =
        elements.values.filter { it.className == className }

    // ===== Public API - Property Access =====

    /**
     * Get a property value (handles stored, derived, and association properties).
     */
    fun getPropertyValue(element: MDMObject, propertyName: String): Any? =
        getProperty(element, propertyName)

    /**
     * Get a property value (handles stored, derived, and association properties).
     * Alias for getPropertyValue for compatibility.
     */
    fun getProperty(element: MDMObject, propertyName: String): Any? {
        val metaClass = element.metaClass

        // Check if it's a stored property
        val property = findProperty(metaClass, propertyName)
        if (property != null) {
            return if (property.isDerived) {
                computeDerivedProperty(element, property)
            } else {
                element.getProperty(propertyName)
            }
        }

        // Check if it's an association end
        val associationEnd = findAssociationEnd(metaClass, propertyName)
        if (associationEnd != null) {
            val (_, end, _) = associationEnd
            // Check if it's a derived association end
            if (end.isDerived && end.derivationConstraint != null) {
                return computeDerivedAssociationEnd(element, end)
            }
            val results = navigateAssociation(element, propertyName)
            return normalizeForMultiplicity(results, end.upperBound)
        }

        // Not found
        return null
    }

    /**
     * Set a stored property value.
     */
    fun setPropertyValue(element: MDMObject, propertyName: String, value: Any?) {
        val metaClass = element.metaClass
        val property = findProperty(metaClass, propertyName)

        if (property != null) {
            if (property.isDerived) {
                throw IllegalStateException("Cannot set derived property: $propertyName")
            }
            val oldValue = element.getProperty(propertyName)
            element.setProperty(propertyName, value)
            fireEvent(LifecycleEvent.PropertyChanged(element, propertyName, oldValue, value))
            return
        }

        // Check if it's an association end - setting association means linking
        val associationEnd = findAssociationEnd(metaClass, propertyName)
        if (associationEnd != null) {
            setAssociationEndValue(element, associationEnd, value)
            return
        }

        throw IllegalArgumentException("Property '$propertyName' not found on class: ${element.className}")
    }

    /**
     * Set a property by instance ID (for generated code compatibility).
     */
    fun setProperty(instanceId: String, propertyName: String, value: Any?) {
        val element = elements[instanceId]
            ?: throw IllegalArgumentException("Instance not found: $instanceId")
        setPropertyValue(element, propertyName, value)
    }

    /**
     * Get a property by instance ID (for generated code compatibility).
     */
    fun getProperty(instanceId: String, propertyName: String): Any? {
        val element = elements[instanceId]
            ?: throw IllegalArgumentException("Instance not found: $instanceId")
        return getProperty(element, propertyName)
    }

    // ===== Public API - Links & Navigation =====

    /**
     * Create a link between two elements.
     */
    fun link(sourceId: String, targetId: String, associationName: String) {
        val association = schema.getAssociation(associationName)
        if (association == null) {
            println("DEBUG MDMEngine.link: Unknown association '$associationName'")
            throw IllegalArgumentException("Unknown association: $associationName")
        }

        val linkId = UUID.randomUUID().toString()
        val link = MDMLink(linkId, association, sourceId, targetId)
        System.err.println("DEBUG MDMEngine.link: $sourceId --[$associationName]--> $targetId")
        graph.addEdge(link)

        val source = elements[sourceId]
        val target = elements[targetId]
        if (source != null && target != null) {
            fireEvent(LifecycleEvent.LinkCreated(link, source, target, association))
        }

        // Invalidate derived property caches
        elements[sourceId]?.let { invalidateAssociationDependents(it, association.targetEnd.name) }
        elements[targetId]?.let { invalidateAssociationDependents(it, association.sourceEnd.name) }
    }

    /**
     * Remove a link between two elements.
     */
    fun unlink(sourceId: String, targetId: String, associationName: String) {
        val link = graph.findEdge(sourceId, targetId, associationName)
        if (link != null) {
            graph.removeEdge(link.id)

            val source = elements[sourceId]
            val target = elements[targetId]
            if (source != null && target != null) {
                fireEvent(LifecycleEvent.LinkDeleting(link, source, target, link.association))
            }

            // Invalidate derived property caches
            val association = link.association
            elements[sourceId]?.let { invalidateAssociationDependents(it, association.targetEnd.name) }
            elements[targetId]?.let { invalidateAssociationDependents(it, association.sourceEnd.name) }
        }
    }

    /**
     * Navigate an association from an element.
     *
     * This handles both `redefines` and `subsets` relationships:
     * - If we navigate `specific` but the actual links are stored via a redefining property
     *   (e.g., `subclassifier` redefines `specific`), we collect those links.
     * - If we navigate `ownedRelationship` and `ownedMembership` subsets it, we collect
     *   links from `ownedMembership` as well (since subsets implies inclusion in the superset).
     */
    fun navigateAssociation(element: MDMElement, propertyName: String): List<MDMObject> {
        val elementId = element.id ?: return emptyList()
        val metaClass = element.metaClass

        val associationEnd = findAssociationEnd(metaClass, propertyName)
        if (associationEnd == null) {
            System.err.println("DEBUG navigateAssociation: No association found for ${metaClass.name}.$propertyName")
            return emptyList()
        }

        val (association, end, isTargetEnd) = associationEnd

        // Collect target IDs from the primary association
        val allTargetIds = mutableSetOf<String>()
        val directResults = if (isTargetEnd) {
            graph.getTargets(elementId, association.name)
        } else {
            graph.getSources(elementId, association.name)
        }
        allTargetIds.addAll(directResults)
        System.err.println("DEBUG navigateAssociation: ${metaClass.name}.$propertyName via ${association.name} -> ${directResults.size} direct results")

        // Also collect from redefining associations (e.g., subclassifier redefines specific)
        val redefiningEnds = schema.findRedefiningEnds(propertyName)
        for ((redefiningAssoc, redefiningEnd) in redefiningEnds) {
            // Check if the redefining association applies to this element's class hierarchy
            val redefiningIsTargetEnd = redefiningEnd == redefiningAssoc.targetEnd
            val applicableType = if (redefiningIsTargetEnd) redefiningAssoc.sourceEnd.type else redefiningAssoc.targetEnd.type
            if (schema.isSubclassOf(metaClass.name, applicableType) || metaClass.name == applicableType) {
                val redefiningResults = if (redefiningIsTargetEnd) {
                    graph.getTargets(elementId, redefiningAssoc.name)
                } else {
                    graph.getSources(elementId, redefiningAssoc.name)
                }
                System.err.println("DEBUG navigateAssociation: + redefining ${redefiningEnd.name} via ${redefiningAssoc.name} -> ${redefiningResults.size} results")
                allTargetIds.addAll(redefiningResults)
            }
        }

        // Also collect from subsetting associations (e.g., ownedMembership subsets ownedRelationship)
        val subsettingEnds = schema.findSubsettingEnds(propertyName)
        for ((subsettingAssoc, subsettingEnd) in subsettingEnds) {
            // Check if the subsetting association applies to this element's class hierarchy
            val subsettingIsTargetEnd = subsettingEnd == subsettingAssoc.targetEnd
            val applicableType = if (subsettingIsTargetEnd) subsettingAssoc.sourceEnd.type else subsettingAssoc.targetEnd.type
            if (schema.isSubclassOf(metaClass.name, applicableType) || metaClass.name == applicableType) {
                val subsettingResults = if (subsettingIsTargetEnd) {
                    graph.getTargets(elementId, subsettingAssoc.name)
                } else {
                    graph.getSources(elementId, subsettingAssoc.name)
                }
                System.err.println("DEBUG navigateAssociation: + subsetting ${subsettingEnd.name} via ${subsettingAssoc.name} -> ${subsettingResults.size} results")
                allTargetIds.addAll(subsettingResults)
            }
        }

        System.err.println("DEBUG navigateAssociation: Total results: ${allTargetIds.size}")
        return allTargetIds.mapNotNull { elements[it] }
    }

    // ===== Public API - Operations & Validation =====

    /**
     * Invoke an operation on an element.
     */
    fun invokeOperation(element: MDMObject, operationName: String, args: Map<String, Any?> = emptyMap()): Any? {
        val metaClass = element.metaClass
        val operation = findOperation(metaClass, operationName)
            ?: throw IllegalArgumentException("Operation '$operationName' not found on class: ${element.className}")

        val body = operation.body
        if (body == null) {
            logger.warn { "Operation $operationName has no body" }
            return null
        }

        // Map BodyLanguage enum to evaluator key
        val languageKey = when (operation.bodyLanguage) {
            org.openmbee.gearshift.framework.meta.BodyLanguage.OCL -> "OCL"
            org.openmbee.gearshift.framework.meta.BodyLanguage.GQL -> "GQL"
            org.openmbee.gearshift.framework.meta.BodyLanguage.KOTLIN_DSL -> "KOTLIN_DSL"
            org.openmbee.gearshift.framework.meta.BodyLanguage.PROPERTY_REF -> {
                // Simple property reference - just get the property
                return getPropertyValue(element, body)
            }
        }

        val evaluator = evaluators[languageKey]
        if (evaluator == null) {
            logger.warn { "No evaluator registered for language: $languageKey" }
            return null
        }

        logger.debug { "Invoking operation $operationName on ${element.className} using $languageKey" }
        return evaluator.evaluate(body, element, this, args)
    }

    /**
     * Invoke an operation by instance ID (for generated code compatibility).
     */
    fun invokeOperation(instanceId: String, operationName: String, args: Map<String, Any?> = emptyMap()): Any? {
        val element = elements[instanceId]
            ?: throw IllegalArgumentException("Instance not found: $instanceId")
        return invokeOperation(element, operationName, args)
    }

    /**
     * Compute a derived association end value.
     */
    private fun computeDerivedAssociationEnd(
        element: MDMObject,
        end: org.openmbee.gearshift.framework.meta.MetaAssociationEnd
    ): Any? {
        val derivationConstraintName = end.derivationConstraint ?: return null

        // Look up the constraint by name to get the expression and language
        val constraint = findConstraint(element.metaClass, derivationConstraintName)
        if (constraint != null) {
            val evaluator = evaluators[constraint.language.uppercase()]
            if (evaluator != null) {
                logger.debug { "Computing derived association end ${end.name} via ${constraint.language}" }
                System.err.println("DEBUG MDMEngine: Computing ${end.name} with OCL: ${constraint.expression}")
                val result = evaluator.evaluate(constraint.expression, element, this)
                return normalizeForMultiplicity(
                    when (result) {
                        null -> emptyList()
                        is Collection<*> -> result.filterNotNull()
                        else -> listOf(result)
                    },
                    end.upperBound
                )
            } else {
                logger.warn { "No evaluator for ${constraint.language} to compute ${end.name}" }
            }
        } else {
            logger.warn { "Derivation constraint '$derivationConstraintName' not found for association end ${end.name}" }
        }

        return null
    }

    /**
     * Compute a derived property value.
     */
    fun computeDerivedProperty(element: MDMObject, property: MetaProperty): Any? {
        // Check if it's a union property (computed from subsetters)
        if (property.isUnion) {
            return computeFromSubsetters(element, property)
        }

        // Check for derivation constraint
        val derivationConstraintName = property.derivationConstraint
        if (derivationConstraintName != null) {
            // Look up the constraint by name to get the expression and language
            val constraint = findConstraint(element.metaClass, derivationConstraintName)
            if (constraint != null) {
                val evaluator = evaluators[constraint.language.uppercase()]
                if (evaluator != null) {
                    logger.debug { "Computing derived property ${property.name} via ${constraint.language}" }
                    return evaluator.evaluate(constraint.expression, element, this)
                } else {
                    logger.warn { "No evaluator for ${constraint.language} to compute ${property.name}" }
                }
            } else {
                logger.warn { "Derivation constraint '$derivationConstraintName' not found for ${property.name}" }
            }
        }

        return null
    }

    /**
     * Validate all elements (or filtered subset).
     */
    fun validateAll(
        classFilter: String? = null,
        constraintNames: List<String> = emptyList()
    ): List<ValidationError> {
        val errors = mutableListOf<ValidationError>()

        val elementsToValidate = if (classFilter != null) {
            getElementsByClass(classFilter)
        } else {
            getAllElements()
        }

        for (element in elementsToValidate) {
            val constraints = getConstraintsForClass(element.metaClass, constraintNames)
            for (constraint in constraints) {
                val result = runValidationConstraint(element, constraint)
                if (!result.isValid) {
                    errors.add(ValidationError(element, constraint, result.message))
                }
            }
        }

        return errors
    }

    /**
     * Validate a single element.
     * Compatibility method - validates all constraints on the given element.
     */
    fun validate(element: MDMObject): List<ValidationError> {
        val errors = mutableListOf<ValidationError>()
        val constraints = getConstraintsForClass(element.metaClass, emptyList())
        for (constraint in constraints) {
            val result = runValidationConstraint(element, constraint)
            if (!result.isValid) {
                errors.add(ValidationError(element, constraint, result.message))
            }
        }
        return errors
    }

    /**
     * Validate links for a given element.
     * Compatibility method - validates association cardinality constraints.
     */
    fun validateLinks(elementId: String): List<ValidationError> {
        val element = getElement(elementId) ?: return emptyList()
        // For now, return empty - full link validation can be added later
        return emptyList()
    }

    /**
     * Get all instances. Alias for getAllElements().
     */
    fun getAllInstances(): List<MDMObject> = getAllElements()

    // ===== Public API - Lifecycle Handlers =====

    /**
     * Register a lifecycle handler.
     */
    fun registerLifecycleHandler(handler: LifecycleHandler) {
        lifecycleHandlers.add(handler)
        lifecycleHandlers.sortByDescending { it.priority }
    }

    /**
     * Unregister a lifecycle handler.
     */
    fun unregisterLifecycleHandler(handler: LifecycleHandler) {
        lifecycleHandlers.remove(handler)
    }

    // ===== Public API - Expression Evaluators =====

    /**
     * Register an expression evaluator for a language.
     */
    fun registerEvaluator(language: String, evaluator: ExpressionEvaluator) {
        evaluators[language.uppercase()] = evaluator
    }

    /**
     * Unregister an expression evaluator.
     */
    fun unregisterEvaluator(language: String) {
        evaluators.remove(language.uppercase())
    }

    /**
     * Get an evaluator for a language.
     */
    fun getEvaluator(language: String): ExpressionEvaluator? = evaluators[language.uppercase()]

    // ===== Public API - Serialization =====

    /**
     * Serialize the model to JSON.
     */
    fun toJson(): String {
        // TODO: Implement JSON serialization
        return "{}"
    }

    /**
     * Load model from JSON.
     */
    fun fromJson(json: String) {
        // TODO: Implement JSON deserialization
    }

    // ===== Internal - Property Access Helpers =====

    private fun findProperty(metaClass: MetaClass, propertyName: String): MetaProperty? {
        metaClass.attributes.firstOrNull { it.name == propertyName }?.let { return it }
        for (superclassName in metaClass.superclasses) {
            schema.getClass(superclassName)?.let { superclass ->
                findProperty(superclass, propertyName)?.let { return it }
            }
        }
        return null
    }

    private fun findAssociationEnd(
        metaClass: MetaClass,
        propertyName: String
    ): Triple<org.openmbee.gearshift.framework.meta.MetaAssociation,
            org.openmbee.gearshift.framework.meta.MetaAssociationEnd,
            Boolean>? {
        // Check all associations for matching end name
        for (association in schema.getAllAssociations()) {
            if (association.targetEnd.name == propertyName &&
                (association.sourceEnd.type == metaClass.name ||
                        schema.isSubclassOf(metaClass.name, association.sourceEnd.type))
            ) {
                return Triple(association, association.targetEnd, true)
            }
            if (association.sourceEnd.name == propertyName &&
                (association.targetEnd.type == metaClass.name ||
                        schema.isSubclassOf(metaClass.name, association.targetEnd.type))
            ) {
                return Triple(association, association.sourceEnd, false)
            }
        }
        return null
    }

    private fun findOperation(
        metaClass: MetaClass,
        operationName: String
    ): org.openmbee.gearshift.framework.meta.MetaOperation? {
        metaClass.operations.firstOrNull { it.name == operationName }?.let { return it }
        for (superclassName in metaClass.superclasses) {
            schema.getClass(superclassName)?.let { superclass ->
                findOperation(superclass, operationName)?.let { return it }
            }
        }
        return null
    }

    private fun findConstraint(metaClass: MetaClass, constraintName: String): MetaConstraint? {
        metaClass.constraints.firstOrNull { it.name == constraintName }?.let { return it }
        for (superclassName in metaClass.superclasses) {
            schema.getClass(superclassName)?.let { superclass ->
                findConstraint(superclass, constraintName)?.let { return it }
            }
        }
        return null
    }

    private fun computeFromSubsetters(element: MDMObject, property: MetaProperty): List<Any> {
        // TODO: Aggregate values from all subsetting properties
        return emptyList()
    }

    private fun normalizeForMultiplicity(values: List<Any>, upperBound: Int): Any? {
        // Always return a list - the generated Impl code expects to cast to List
        // and extract the first element for single-valued associations
        return values
    }

    private fun setAssociationEndValue(
        element: MDMObject,
        associationEnd: Triple<org.openmbee.gearshift.framework.meta.MetaAssociation,
                org.openmbee.gearshift.framework.meta.MetaAssociationEnd,
                Boolean>,
        value: Any?
    ) {
        val (association, end, isTargetEnd) = associationEnd
        val elementId = element.id ?: return

        // Normalize value to list of target IDs
        val newTargetIds: List<String> = when (value) {
            null -> emptyList()
            is MDMObject -> listOfNotNull(value.id)
            is Collection<*> -> value.filterIsInstance<MDMObject>().mapNotNull { it.id }
            else -> throw IllegalArgumentException("Invalid value for association end")
        }

        // Get current targets
        val currentTargetIds = if (isTargetEnd) {
            graph.getTargets(elementId, association.name)
        } else {
            graph.getSources(elementId, association.name)
        }

        // Compute diff
        val toRemove = currentTargetIds - newTargetIds.toSet()
        val toAdd = newTargetIds.toSet() - currentTargetIds.toSet()

        // Apply changes to this association
        // Navigation will handle redefines/subsets collection automatically
        for (targetId in toRemove) {
            if (isTargetEnd) {
                unlink(elementId, targetId, association.name)
            } else {
                unlink(targetId, elementId, association.name)
            }
        }
        for (targetId in toAdd) {
            if (isTargetEnd) {
                link(elementId, targetId, association.name)
            } else {
                link(targetId, elementId, association.name)
            }
        }
    }

    // ===== Internal - Validation =====

    private fun getConstraintsForClass(metaClass: MetaClass, filterNames: List<String>): List<MetaConstraint> {
        val constraints = mutableListOf<MetaConstraint>()
        constraints.addAll(metaClass.constraints)
        for (superclassName in metaClass.superclasses) {
            schema.getClass(superclassName)?.let { superclass ->
                constraints.addAll(getConstraintsForClass(superclass, filterNames))
            }
        }
        return if (filterNames.isEmpty()) {
            constraints
        } else {
            constraints.filter { it.name in filterNames }
        }
    }

    private fun runValidationConstraint(element: MDMObject, constraint: MetaConstraint): ValidationResult {
        return when (constraint.type) {
            org.openmbee.gearshift.framework.meta.ConstraintType.VERIFICATION -> {
                // Dispatch to evaluator based on constraint.language
                val evaluator = evaluators[constraint.language.uppercase()]
                if (evaluator == null) {
                    logger.warn { "No evaluator registered for language: ${constraint.language}" }
                    return ValidationResult(true, null) // Skip if no evaluator
                }

                try {
                    val result = evaluator.evaluate(constraint.expression, element, this)
                    when (result) {
                        is Boolean -> ValidationResult(
                            result,
                            if (!result) "Constraint ${constraint.name} failed" else null
                        )

                        is ValidationResult -> result
                        else -> ValidationResult(true, null)
                    }
                } catch (e: Exception) {
                    logger.error(e) { "Error evaluating constraint ${constraint.name}" }
                    ValidationResult(false, "Evaluation error: ${e.message}")
                }
            }

            else -> ValidationResult(true, null)
        }
    }

    // ===== Internal - Events =====

    private fun fireEvent(event: LifecycleEvent) {
        for (handler in lifecycleHandlers) {
            try {
                handler.handle(event, this)
            } catch (e: Exception) {
                logger.error(e) { "Lifecycle handler ${handler::class.simpleName} failed" }
            }
        }
    }

    // ===== Internal - Cache =====

    private fun invalidateAssociationDependents(element: MDMObject, associationEndName: String) {
        // TODO: Implement cache invalidation for derived properties
    }
}

/**
 * Graph structure for storing links between elements.
 */
class MDMGraph {
    private val edges: MutableMap<String, MDMLink> = mutableMapOf()
    private val sourceIndex: MutableMap<String, MutableList<MDMLink>> = mutableMapOf()
    private val targetIndex: MutableMap<String, MutableList<MDMLink>> = mutableMapOf()

    fun addEdge(link: MDMLink) {
        edges[link.id] = link
        sourceIndex.getOrPut(link.sourceId) { mutableListOf() }.add(link)
        targetIndex.getOrPut(link.targetId) { mutableListOf() }.add(link)
    }

    fun removeEdge(linkId: String) {
        val link = edges.remove(linkId) ?: return
        sourceIndex[link.sourceId]?.remove(link)
        targetIndex[link.targetId]?.remove(link)
    }

    fun removeEdgesForElement(elementId: String) {
        val linksToRemove = (sourceIndex[elementId].orEmpty() + targetIndex[elementId].orEmpty())
            .map { it.id }
            .toSet()
        for (linkId in linksToRemove) {
            removeEdge(linkId)
        }
    }

    fun findEdge(sourceId: String, targetId: String, associationName: String): MDMLink? =
        sourceIndex[sourceId]?.find { it.targetId == targetId && it.associationName == associationName }

    fun getTargets(sourceId: String, associationName: String): List<String> =
        sourceIndex[sourceId]?.filter { it.associationName == associationName }?.map { it.targetId } ?: emptyList()

    fun getSources(targetId: String, associationName: String): List<String> =
        targetIndex[targetId]?.filter { it.associationName == associationName }?.map { it.sourceId } ?: emptyList()

    fun getLinksForElement(elementId: String): List<MDMLink> =
        (sourceIndex[elementId].orEmpty() + targetIndex[elementId].orEmpty()).distinct()

    fun clear() {
        edges.clear()
        sourceIndex.clear()
        targetIndex.clear()
    }
}

/**
 * Result of running a validation constraint.
 */
data class ValidationResult(
    val isValid: Boolean,
    val message: String?
)

/**
 * A validation error.
 */
data class ValidationError(
    val element: MDMObject,
    val constraint: MetaConstraint,
    val message: String?
)

/**
 * Type alias for element (using MDMObject for now, may change to MDMElement later)
 */
typealias MDMElement = MDMObject
