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
package org.openmbee.gearshift

import org.openmbee.gearshift.engine.MDMEngine
import org.openmbee.gearshift.engine.MDMLink
import org.openmbee.gearshift.engine.MDMObject
import org.openmbee.gearshift.engine.MetamodelRegistry
import org.openmbee.gearshift.engine.NameResolver
import org.openmbee.gearshift.metamodel.MetaAssociation
import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.query.QueryEngine
import org.openmbee.gearshift.repository.LinkRepository
import org.openmbee.gearshift.repository.ModelRepository
import java.util.*

/**
 * Main Gearshift engine combining MOF and Model Data Management.
 * Provides a unified API for metamodel management, instance creation,
 * link (association) management, and model repository operations.
 *
 * This is the "next generation MOF" - combining traditional MOF capabilities
 * with modern model data management features including full graph support
 * where classes are nodes and associations are edges.
 */
class GearshiftEngine {
    val metamodelRegistry = MetamodelRegistry()
    val objectRepository = ModelRepository()
    val linkRepository = LinkRepository()
    val queryEngine = QueryEngine(objectRepository)
    val nameResolver = NameResolver(objectRepository, metamodelRegistry)
    private val mdmEngine = MDMEngine(metamodelRegistry, objectRepository, linkRepository)

    // ===== Metamodel Management =====

    /**
     * Register a metaclass in the metamodel.
     */
    fun registerMetaClass(metaClass: MetaClass) {
        metamodelRegistry.registerClass(metaClass)
    }

    /**
     * Register a meta-association in the metamodel.
     */
    fun registerMetaAssociation(association: MetaAssociation) {
        metamodelRegistry.registerAssociation(association)
    }

    /**
     * Get a metaclass by name.
     */
    fun getMetaClass(name: String): MetaClass? {
        return metamodelRegistry.getClass(name)
    }

    /**
     * Get a meta-association by name.
     */
    fun getMetaAssociation(name: String): MetaAssociation? {
        return metamodelRegistry.getAssociation(name)
    }

    /**
     * Validate the entire metamodel for consistency.
     */
    fun validateMetamodel(): List<String> {
        return metamodelRegistry.validate()
    }

    // ===== Instance (Node) Management =====

    /**
     * Create a new instance of a metaclass and store it in the repository.
     * Returns the instance ID and the created object.
     */
    fun createInstance(className: String, id: String? = null): Pair<String, MDMObject> {
        val instance = mdmEngine.createInstance(className)
        val instanceId = id ?: UUID.randomUUID().toString()
        objectRepository.store(instanceId, instance)
        return instanceId to instance
    }

    /**
     * Get an instance by ID.
     */
    fun getInstance(id: String): MDMObject? {
        return objectRepository.get(id)
    }

    /**
     * Delete an instance by ID.
     * Note: This does not handle cascade delete. Use deleteInstanceWithCascade for that.
     */
    fun deleteInstance(id: String): Boolean {
        // Remove all links first
        mdmEngine.removeAllLinks(id)
        return objectRepository.delete(id)
    }

    /**
     * Delete an instance and cascade delete any composite parts.
     * Returns list of all deleted instance IDs.
     */
    fun deleteInstanceWithCascade(id: String): List<String> {
        return mdmEngine.deleteInstanceWithCascade(id)
    }

    /**
     * Set a property on an instance.
     */
    fun setProperty(instanceId: String, propertyName: String, value: Any?) {
        val instance = objectRepository.get(instanceId)
            ?: throw IllegalArgumentException("Instance not found: $instanceId")
        val oldValue = instance.getProperty(propertyName)
        mdmEngine.setProperty(instance, propertyName, value)
        objectRepository.updatePropertyIndex(instanceId, propertyName, oldValue, value)
    }

    /**
     * Get a property from an instance.
     */
    fun getProperty(instanceId: String, propertyName: String): Any? {
        val instance = objectRepository.get(instanceId)
            ?: throw IllegalArgumentException("Instance not found: $instanceId")
        return mdmEngine.getProperty(instance, propertyName)
    }

    /**
     * Validate an instance against its metaclass constraints.
     */
    fun validateInstance(instanceId: String): List<String> {
        val instance = objectRepository.get(instanceId)
            ?: throw IllegalArgumentException("Instance not found: $instanceId")
        return mdmEngine.validate(instance)
    }

    /**
     * Invoke an operation on an instance.
     *
     * @param instanceId The ID of the instance to invoke the operation on
     * @param operationName The name of the operation to invoke
     * @param arguments Map of parameter names to values (default: empty)
     * @return The result of the operation invocation
     */
    fun invokeOperation(
        instanceId: String,
        operationName: String,
        arguments: Map<String, Any?> = emptyMap()
    ): Any? {
        val instance = objectRepository.get(instanceId)
            ?: throw IllegalArgumentException("Instance not found: $instanceId")
        return mdmEngine.invokeOperation(instance, operationName, arguments)
    }

    // ===== Link (Edge) Management =====

    /**
     * Create a link between two instances via an association.
     *
     * @param associationName The name of the MetaAssociation
     * @param sourceId ID of the source instance
     * @param targetId ID of the target instance
     * @return The created MDMLink
     */
    fun createLink(associationName: String, sourceId: String, targetId: String): MDMLink {
        return mdmEngine.createLink(associationName, sourceId, targetId)
    }

    /**
     * Get all target instances linked from a source via an association.
     * Traverses in the forward direction (source -> target).
     */
    fun getLinkedTargets(associationName: String, sourceId: String): List<MDMObject> {
        return mdmEngine.getLinkedTargets(associationName, sourceId)
    }

    /**
     * Get all source instances linked to a target via an association.
     * Traverses in the reverse direction (target -> source).
     */
    fun getLinkedSources(associationName: String, targetId: String): List<MDMObject> {
        return mdmEngine.getLinkedSources(associationName, targetId)
    }

    /**
     * Remove a link between two instances.
     *
     * @return true if link was removed, false if not found
     */
    fun removeLink(associationName: String, sourceId: String, targetId: String): Boolean {
        return mdmEngine.removeLink(associationName, sourceId, targetId)
    }

    /**
     * Get all links from/to an instance.
     */
    fun getLinks(instanceId: String): List<MDMLink> {
        return mdmEngine.getLinks(instanceId)
    }

    /**
     * Get all outgoing links from an instance.
     */
    fun getOutgoingLinks(instanceId: String): List<MDMLink> {
        return mdmEngine.getOutgoingLinks(instanceId)
    }

    /**
     * Get all incoming links to an instance.
     */
    fun getIncomingLinks(instanceId: String): List<MDMLink> {
        return mdmEngine.getIncomingLinks(instanceId)
    }

    /**
     * Validate all links for an instance against association constraints.
     */
    fun validateLinks(instanceId: String): List<String> {
        return mdmEngine.validateLinks(instanceId)
    }

    // ===== Query and Search =====

    /**
     * Get all instances of a specific type.
     */
    fun getInstancesByType(className: String): List<MDMObject> {
        return objectRepository.getByType(className)
    }

    /**
     * Get all instances where a property has a specific value.
     */
    fun getInstancesByProperty(propertyName: String, value: Any): List<MDMObject> {
        return objectRepository.getByProperty(propertyName, value)
    }

    /**
     * Get all links of a specific association type.
     */
    fun getLinksByAssociation(associationName: String): List<MDMLink> {
        return linkRepository.getByAssociation(associationName)
    }

    /**
     * Get repository statistics.
     */
    fun getStatistics() = GearshiftStatistics(
        objects = objectRepository.getStatistics(),
        links = linkRepository.getStatistics()
    )

    /**
     * Clear all instances and links (keeps metamodel).
     */
    fun clearInstances() {
        linkRepository.clear()
        objectRepository.clear()
    }

    /**
     * Clear everything (metamodel, instances, and links).
     */
    fun clearAll() {
        linkRepository.clear()
        objectRepository.clear()
        metamodelRegistry.clear()
    }

    // ===== Name Resolution =====

    /**
     * Resolve a qualified name to a Membership and its member Element.
     * Implements KerML 8.2.3.5 Name Resolution.
     *
     * @param qualifiedName The name to resolve (e.g., "A::B::C" or "$::Root::Element")
     * @param localNamespaceId The ID of the Namespace to resolve relative to
     * @param isRedefinitionContext If true, use special redefinition resolution rules
     * @return Resolution result or null if name cannot be resolved
     */
    fun resolveName(
        qualifiedName: String,
        localNamespaceId: String,
        isRedefinitionContext: Boolean = false
    ): NameResolver.ResolutionResult? {
        return nameResolver.resolve(qualifiedName, localNamespaceId, isRedefinitionContext)
    }

    /**
     * Parse a qualified name into segments.
     */
    fun parseQualifiedName(name: String): NameResolver.QualifiedName {
        return NameResolver.QualifiedName.parse(name)
    }
}

/**
 * Combined statistics for objects and links.
 */
data class GearshiftStatistics(
    val objects: org.openmbee.gearshift.repository.RepositoryStatistics,
    val links: org.openmbee.gearshift.repository.LinkRepositoryStatistics
)
