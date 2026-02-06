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
package org.openmbee.mdm.framework.runtime

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.mdm.framework.meta.*
import org.openmbee.mdm.framework.query.ocl.OclAsTypeView
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
 *
 * This class is open to allow subclasses like MountableEngine to extend functionality.
 */
open class MDMEngine(
    /** The schema (metamodel registry) this model uses for metaclass definitions */
    val schema: MetamodelRegistry,
    /** The factory used to create element instances (allows typed implementations) */
    elementFactory: ElementFactory = DefaultElementFactory()
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
        // Wire the factory to this engine
        factory.engine = this
        // Register default expression evaluators
        registerEvaluator("OCL", OclExpressionEvaluator())
    }

    /**
     * Set a new element factory.
     */
    fun setElementFactory(factory: ElementFactory) {
        factory.engine = this
        this.factory = factory
    }

    // ===== Public API - Element Access =====

    /**
     * Get an element by ID.
     */
    open fun getElement(id: String): MDMObject? = elements[id]

    /**
     * Get all elements in the model.
     */
    open fun getAllElements(): List<MDMObject> = elements.values.toList()

    /**
     * Get all elements of a specific class (including subclasses).
     */
    open fun getElementsByClass(className: String): List<MDMObject> =
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

        val element = factory.createInstance(className, metaClass)
        val id = UUID.randomUUID().toString()
        element.id = id
        elements[id] = element

        fireEvent(LifecycleEvent.InstanceCreated(element, metaClass))

        return element
    }

    /**
     * Register an element that was created via constructor (not factory).
     * Assigns an ID and adds it to the element store.
     * This is idempotent - if the element is already registered, nothing happens.
     */
    fun registerElement(element: MDMObject): MDMObject {
        if (element.id == null) {
            element.id = UUID.randomUUID().toString()
        }
        // Check if already registered to avoid duplicate events
        if (elements.containsKey(element.id!!)) {
            return element
        }
        elements[element.id!!] = element
        fireEvent(LifecycleEvent.InstanceCreated(element, element.metaClass))
        return element
    }

    /**
     * Remove an element and all its links.
     */
    open fun removeElement(id: String): Boolean {
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
    open fun getRootNamespaces(): List<MDMObject> =
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
    open fun clear() {
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

        val element = factory.createInstance(className, metaClass)
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
     *
     * Supports both forward and reverse navigation:
     * - Forward: Uses associationName as the edge label to find outgoing links
     * - Reverse: If forward returns empty and associationName matches a source end name,
     *   finds associations where the current element is the target and returns sources
     *
     * This enables OCL navigation like `.typing` on a Feature to find FeatureTyping elements,
     * where the association is defined as FeatureTyping(typing) -> Feature(typedFeature).
     */
    open fun getLinkedTargets(associationName: String, sourceId: String): List<MDMObject> {
        // First try direct/forward navigation
        // Use getElement() to support cross-mount resolution in MountableEngine
        val directResults = graph.getTargets(sourceId, associationName).mapNotNull { getElement(it) }
        if (directResults.isNotEmpty()) {
            return directResults
        }

        // If the associationName is a known association, don't try reverse navigation
        if (schema.getAssociation(associationName) != null) {
            return emptyList()
        }

        // Try reverse navigation: find associations where associationName matches source end name
        // and the current element's class matches the target end type
        val element = getElement(sourceId) ?: return emptyList()
        val assocInfo = schema.findAssociationBySourceEndName(associationName, element.className)

        if (assocInfo != null) {
            val (association, _) = assocInfo
            // Reverse navigation: get sources (elements that link TO this element)
            // Use getElement() to support cross-mount resolution in MountableEngine
            val reverseResults = graph.getSources(sourceId, association.name).mapNotNull { getElement(it) }
            return reverseResults
        }

        return emptyList()
    }

    /**
     * Get linked sources via association (GearshiftEngine compatibility).
     */
    open fun getLinkedSources(associationName: String, targetId: String): List<MDMObject> {
        // Use getElement() to support cross-mount resolution in MountableEngine
        return graph.getSources(targetId, associationName).mapNotNull { getElement(it) }
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
            return normalizeForMultiplicity(results, end, element, propertyName)
        }

        // Not found
        return null
    }

    /**
     * Set a stored property value.
     */
    open fun setPropertyValue(element: MDMObject, propertyName: String, value: Any?) {
        val metaClass = element.metaClass
        val property = findProperty(metaClass, propertyName)

        if (property != null) {
            if (property.isDerived) {
                throw IllegalStateException("Cannot set derived property: $propertyName")
            }
            if (property.isReadOnly) {
                throw IllegalStateException("Cannot set read-only property: $propertyName")
            }

            // Validate unique constraint for multi-valued properties
            if (property.isUnique && property.isMultiValued && value is Collection<*>) {
                val distinctCount = value.toSet().size
                if (distinctCount != value.size) {
                    throw IllegalArgumentException(
                        "Property '$propertyName' must contain unique values (found duplicates)"
                    )
                }
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
     * Uses getElement() to support mounted elements in MountableEngine.
     */
    fun setProperty(instanceId: String, propertyName: String, value: Any?) {
        val element = getElement(instanceId)
            ?: throw IllegalArgumentException("Instance not found: $instanceId")
        setPropertyValue(element, propertyName, value)
    }

    /**
     * Get a property by instance ID (for generated code compatibility).
     * Uses getElement() to support mounted elements in MountableEngine.
     */
    fun getProperty(instanceId: String, propertyName: String): Any? {
        val element = getElement(instanceId)
            ?: throw IllegalArgumentException("Instance not found: $instanceId")
        return getProperty(element, propertyName)
    }

    // ===== Public API - Links & Navigation =====

    /**
     * Create a link between two elements.
     */
    open fun link(sourceId: String, targetId: String, associationName: String) {
        val association = schema.getAssociation(associationName)
        if (association == null) {
            throw IllegalArgumentException("Unknown association: $associationName")
        }

        val linkId = UUID.randomUUID().toString()
        val link = MDMLink(linkId, association, sourceId, targetId)
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
    open fun unlink(sourceId: String, targetId: String, associationName: String) {
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
    open fun navigateAssociation(element: MDMElement, propertyName: String): List<MDMObject> {
        val elementId = element.id ?: return emptyList()
        val metaClass = element.metaClass

        val associationEnd = findAssociationEnd(metaClass, propertyName)
        if (associationEnd == null) {
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

        // Also collect from redefining associations (e.g., subclassifier redefines specific)
        val redefiningEnds = schema.findRedefiningEnds(propertyName)
        for ((redefiningAssoc, redefiningEnd) in redefiningEnds) {
            // Check if the redefining association applies to this element's class hierarchy
            val redefiningIsTargetEnd = redefiningEnd == redefiningAssoc.targetEnd
            val applicableType =
                if (redefiningIsTargetEnd) redefiningAssoc.sourceEnd.type else redefiningAssoc.targetEnd.type
            if (schema.isSubclassOf(metaClass.name, applicableType) || metaClass.name == applicableType) {
                val redefiningResults = if (redefiningIsTargetEnd) {
                    graph.getTargets(elementId, redefiningAssoc.name)
                } else {
                    graph.getSources(elementId, redefiningAssoc.name)
                }
                allTargetIds.addAll(redefiningResults)
            }
        }

        // Also collect from subsetting associations (e.g., ownedMembership subsets ownedRelationship)
        val subsettingEnds = schema.findSubsettingEnds(propertyName)
        for ((subsettingAssoc, subsettingEnd) in subsettingEnds) {
            // Check if the subsetting association applies to this element's class hierarchy
            val subsettingIsTargetEnd = subsettingEnd == subsettingAssoc.targetEnd
            val applicableType =
                if (subsettingIsTargetEnd) subsettingAssoc.sourceEnd.type else subsettingAssoc.targetEnd.type
            if (schema.isSubclassOf(metaClass.name, applicableType) || metaClass.name == applicableType) {
                val subsettingResults = if (subsettingIsTargetEnd) {
                    graph.getTargets(elementId, subsettingAssoc.name)
                } else {
                    graph.getSources(elementId, subsettingAssoc.name)
                }
                allTargetIds.addAll(subsettingResults)
            }
        }

        // Use getElement() to support cross-mount resolution in MountableEngine
        // Track broken references (IDs that exist but elements that don't)
        val results = mutableListOf<MDMObject>()
        val brokenIds = mutableListOf<String>()
        for (id in allTargetIds) {
            val element = getElement(id)
            if (element != null) {
                results.add(element)
            } else {
                brokenIds.add(id)
            }
        }
        if (brokenIds.isNotEmpty()) {
            throw BrokenReferenceException(
                "Association '$propertyName' on ${metaClass.name}[$elementId] references non-existent element(s): ${brokenIds.joinToString()}"
            )
        }
        return results
    }

    // ===== Public API - Operations & Validation =====

    /**
     * Invoke an operation on an element.
     */
    fun invokeOperation(element: MDMObject, operationName: String, args: Map<String, Any?> = emptyMap()): Any? {
        val metaClass = element.metaClass
        val operation = findOperation(metaClass, operationName)
            ?: throw IllegalArgumentException("Operation '$operationName' not found on class: ${element.className}")

        return when (val body = operation.body) {
            null -> {
                logger.warn { "Operation $operationName has no body" }
                null
            }

            is OperationBody.Native -> {
                logger.debug { "Invoking native operation $operationName on ${element.className}" }
                body.impl(element, args, this)
            }

            is OperationBody.Expression -> {
                when (body.language) {
                    BodyLanguage.PROPERTY_REF -> {
                        // Simple property reference - just get the property
                        getPropertyValue(element, body.code)
                    }

                    else -> {
                        val evaluator = evaluators[body.language.name]
                        if (evaluator == null) {
                            logger.warn { "No evaluator registered for language: ${body.language}" }
                            null
                        } else {
                            logger.debug { "Invoking operation $operationName on ${element.className} using ${body.language}" }
                            evaluator.evaluate(body.code, element, this, args)
                        }
                    }
                }
            }
        }
    }

    /**
     * Invoke an operation by instance ID (for generated code compatibility).
     * Uses getElement() to support mounted elements in MountableEngine.
     */
    fun invokeOperation(instanceId: String, operationName: String, args: Map<String, Any?> = emptyMap()): Any? {
        val element = getElement(instanceId)
            ?: throw IllegalArgumentException("Instance not found: $instanceId")
        return invokeOperation(element, operationName, args)
    }

    /**
     * Invoke an operation on an instance, dispatching based on a specific class.
     * This is used by oclAsType() to call parent class implementations.
     */
    fun invokeOperationAs(
        instanceId: String,
        operationName: String,
        dispatchClass: String,
        args: Map<String, Any?> = emptyMap()
    ): Any? {
        val element = getElement(instanceId)
            ?: throw IllegalArgumentException("Instance not found: $instanceId")

        // Look up the metaclass to dispatch on
        val viewMetaClass = schema.getClass(dispatchClass)
            ?: throw IllegalArgumentException("Unknown class for dispatch: $dispatchClass")

        // Find the operation on the specified class (not the element's actual class)
        val operation = findOperation(viewMetaClass, operationName)
            ?: throw IllegalArgumentException("Operation '$operationName' not found on class: $dispatchClass")

        return when (val body = operation.body) {
            null -> {
                logger.warn { "Operation $operationName has no body" }
                null
            }
            is OperationBody.Native -> {
                logger.debug { "Invoking native operation $operationName as $dispatchClass" }
                body.impl(element, args, this)
            }
            is OperationBody.Expression -> {
                when (body.language) {
                    BodyLanguage.PROPERTY_REF -> getPropertyValue(element, body.code)
                    else -> {
                        val evaluator = evaluators[body.language.name]
                        if (evaluator == null) {
                            logger.warn { "No evaluator registered for language: ${body.language}" }
                            null
                        } else {
                            logger.debug { "Invoking operation $operationName as $dispatchClass using ${body.language}" }
                            evaluator.evaluate(body.code, element, this, args)
                        }
                    }
                }
            }
        }
    }

    /**
     * Get a property value, looking up the property definition on a specific class.
     * This is used by oclAsType() to access properties as defined on a parent class.
     *
     * When viewing an object as a parent type via oclAsType(), we need to use the
     * parent class's property definitions, including derivation constraints.
     * This allows `self.oclAsType(Parent).derivedProp` to use Parent's derivation
     * constraint rather than the subclass's potentially-overridden version.
     */
    fun getPropertyAs(instanceId: String, propertyName: String, viewAsClass: String): Any? {
        val element = getElement(instanceId)
            ?: throw IllegalArgumentException("Instance not found: $instanceId")

        // Look up the metaclass to view as
        val viewMetaClass = schema.getClass(viewAsClass)
            ?: throw IllegalArgumentException("Unknown class: $viewAsClass")

        // Check if it's a stored property (on the view class)
        val property = findPropertyOnClass(viewMetaClass, propertyName)
        if (property != null) {
            return if (property.isDerived) {
                computeDerivedPropertyAs(element, property, viewMetaClass)
            } else {
                element.getProperty(propertyName)
            }
        }

        // Check if it's an association end (on the view class)
        val associationEnd = findAssociationEndOnClass(viewMetaClass, propertyName)
        if (associationEnd != null) {
            val (_, end, _) = associationEnd
            // Check if it's a derived association end
            if (end.isDerived && end.derivationConstraint != null) {
                return computeDerivedAssociationEndAs(element, end, viewMetaClass)
            }
            val results = navigateAssociation(element, propertyName)
            return normalizeForMultiplicity(results, end, element, propertyName)
        }

        // Not found on view class
        return null
    }

    /**
     * Find a property on a specific metaclass (not the element's actual class).
     */
    private fun findPropertyOnClass(metaClass: MetaClass, propertyName: String): MetaProperty? {
        metaClass.attributes.firstOrNull { it.name == propertyName }?.let { return it }
        for (superclassName in metaClass.superclasses) {
            schema.getClass(superclassName)?.let { superclass ->
                findPropertyOnClass(superclass, propertyName)?.let { return it }
            }
        }
        return null
    }

    /**
     * Find an association end on a specific metaclass (not the element's actual class).
     */
    private fun findAssociationEndOnClass(
        metaClass: MetaClass,
        propertyName: String
    ): Triple<MetaAssociation, MetaAssociationEnd, Boolean>? {
        // Check all associations for matching end name on the view class
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

    /**
     * Compute a derived property using a specific metaclass's constraint definition.
     */
    private fun computeDerivedPropertyAs(element: MDMObject, property: MetaProperty, viewMetaClass: MetaClass): Any? {
        // Check if it's a union property (computed from subsetters)
        if (property.isUnion) {
            return computeFromSubsetters(element, property)
        }

        // Check for derivation constraint - look it up on the view class
        val derivationConstraintName = property.derivationConstraint
        if (derivationConstraintName != null) {
            // Look up the constraint on the VIEW class, not the element's actual class
            val constraint = findConstraintOnClass(viewMetaClass, derivationConstraintName)
            if (constraint != null) {
                val evaluator = evaluators[constraint.language.uppercase()]
                if (evaluator != null) {
                    logger.debug { "Computing derived property ${property.name} via ${constraint.language} as $viewMetaClass" }
                    return evaluator.evaluate(constraint.expression, element, this)
                } else {
                    logger.warn { "No evaluator for ${constraint.language} to compute ${property.name}" }
                }
            } else {
                logger.warn { "Derivation constraint '$derivationConstraintName' not found on ${viewMetaClass.name} for ${property.name}" }
            }
        }

        return null
    }

    /**
     * Find a constraint on a specific metaclass.
     */
    private fun findConstraintOnClass(metaClass: MetaClass, constraintName: String): MetaConstraint? {
        metaClass.constraints.firstOrNull { it.name == constraintName }?.let { return it }
        for (superclassName in metaClass.superclasses) {
            schema.getClass(superclassName)?.let { superclass ->
                findConstraintOnClass(superclass, constraintName)?.let { return it }
            }
        }
        return null
    }

    /**
     * Compute a derived association end using a specific metaclass's constraint.
     */
    private fun computeDerivedAssociationEndAs(
        element: MDMObject,
        end: MetaAssociationEnd,
        viewMetaClass: MetaClass
    ): Any? {
        val derivationConstraintName = end.derivationConstraint ?: return null

        // Look up the constraint on the VIEW class
        val constraint = findConstraintOnClass(viewMetaClass, derivationConstraintName)
        if (constraint != null) {
            val evaluator = evaluators[constraint.language.uppercase()]
            if (evaluator != null) {
                val result = evaluator.evaluate(constraint.expression, element, this)
                return normalizeForMultiplicity(
                    when (result) {
                        null -> emptyList()
                        is Collection<*> -> result.filterNotNull()
                        else -> listOf(result)
                    },
                    end,
                    element,
                    end.name
                )
            } else {
                logger.warn { "No evaluator for ${constraint.language} to compute ${end.name}" }
            }
        } else {
            logger.warn { "Derivation constraint '$derivationConstraintName' not found on ${viewMetaClass.name} for association end ${end.name}" }
        }

        return null
    }

    /**
     * Compute a derived association end value.
     */
    private fun computeDerivedAssociationEnd(
        element: MDMObject,
        end: MetaAssociationEnd
    ): Any? {
        val derivationConstraintName = end.derivationConstraint

        // Try explicit derivation constraint first
        if (derivationConstraintName != null) {
            val constraint = findConstraint(element.metaClass, derivationConstraintName)
            if (constraint != null) {
                val evaluator = evaluators[constraint.language.uppercase()]
                if (evaluator != null) {
                    val result = evaluator.evaluate(constraint.expression, element, this)
                    // Unwrap OclAsTypeView to get the underlying MDMObject
                    val unwrappedResult = unwrapOclResult(result)
                    return normalizeForMultiplicity(
                        when (unwrappedResult) {
                            null -> emptyList()
                            is Collection<*> -> unwrappedResult.map { unwrapOclResult(it) }.filterNotNull()
                            else -> listOf(unwrappedResult)
                        },
                        end,
                        element,
                        end.name
                    )
                } else {
                    logger.warn { "No evaluator for ${constraint.language} to compute ${end.name}" }
                }
            } else {
                logger.warn { "Derivation constraint '$derivationConstraintName' not found for association end ${end.name}" }
            }
        }

        return null
    }

    /**
     * Unwrap OCL wrapper types to get the underlying value.
     * OclAsTypeView wraps an MDMObject with a view type for dispatching.
     */
    private fun unwrapOclResult(value: Any?): Any? {
        return when (value) {
            is OclAsTypeView -> value.obj
            else -> value
        }
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
     * Runs all VERIFICATION constraints including inherited ones from MDMBaseClass.
     */
    fun validate(element: MDMObject): List<ValidationError> {
        val errors = mutableListOf<ValidationError>()

        // Run all constraints (including native constraints from MDMBaseClass)
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
    ): Triple<MetaAssociation,
            MetaAssociationEnd,
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
    ): MetaOperation? {
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

    private fun normalizeForMultiplicity(
        values: List<Any>,
        end: MetaAssociationEnd,
        element: MDMObject,
        propertyName: String
    ): Any? {
        return if (end.upperBound == 1) {
            // Single-valued: return the first element or null
            // Note: Required associations with no value are allowed during construction.
            // Model validation should catch missing required associations.
            values.firstOrNull()
        } else {
            // Multi-valued: return the list
            values
        }
    }

    private fun setAssociationEndValue(
        element: MDMObject,
        associationEnd: Triple<MetaAssociation,
                MetaAssociationEnd,
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
            ConstraintType.VERIFICATION -> {
                // Check for body-based constraint (new style)
                val body = constraint.body
                if (body != null) {
                    return runConstraintBody(element, constraint, body)
                }

                // Fall back to legacy expression-based constraint
                if (constraint.expression.isEmpty()) {
                    return ValidationResult(true, null) // No expression, skip
                }

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

    /**
     * Run a constraint body (expression or native).
     */
    private fun runConstraintBody(
        element: MDMObject,
        constraint: MetaConstraint,
        body: ConstraintBody
    ): ValidationResult {
        return when (body) {
            is ConstraintBody.Native -> {
                try {
                    val result = body.impl(element, this)
                    ValidationResult(result.isValid, result.message)
                } catch (e: Exception) {
                    logger.error(e) { "Error in native constraint ${constraint.name}" }
                    ValidationResult(false, "Native constraint error: ${e.message}")
                }
            }

            is ConstraintBody.Expression -> {
                val evaluator = evaluators[body.language.uppercase()]
                if (evaluator == null) {
                    logger.warn { "No evaluator registered for language: ${body.language}" }
                    return ValidationResult(true, null)
                }

                try {
                    val result = evaluator.evaluate(body.code, element, this)
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
