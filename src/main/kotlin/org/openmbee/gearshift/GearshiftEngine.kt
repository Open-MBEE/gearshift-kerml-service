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

import org.openmbee.gearshift.engine.MetamodelRegistry
import org.openmbee.gearshift.engine.MofEngine
import org.openmbee.gearshift.engine.MofObject
import org.openmbee.gearshift.engine.NameResolver
import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaAssociation
import org.openmbee.gearshift.query.QueryEngine
import org.openmbee.gearshift.repository.ModelRepository
import java.util.UUID

/**
 * Main Gearshift engine combining MOF and Model Data Management.
 * Provides a unified API for metamodel management, instance creation,
 * and model repository operations.
 *
 * This is the "next generation MOF" - combining traditional MOF capabilities
 * with modern model data management features.
 */
class GearshiftEngine {
    val metamodelRegistry = MetamodelRegistry()
    val repository = ModelRepository()
    val queryEngine = QueryEngine(repository)
    val nameResolver = NameResolver(repository, metamodelRegistry)
    private val mofEngine = MofEngine(metamodelRegistry)

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
     * Validate the entire metamodel for consistency.
     */
    fun validateMetamodel(): List<String> {
        return metamodelRegistry.validate()
    }

    // ===== Instance Management =====

    /**
     * Create a new instance of a metaclass and store it in the repository.
     * Returns the instance ID and the created object.
     */
    fun createInstance(className: String, id: String? = null): Pair<String, MofObject> {
        val instance = mofEngine.createInstance(className)
        val instanceId = id ?: UUID.randomUUID().toString()
        repository.store(instanceId, instance)
        return instanceId to instance
    }

    /**
     * Get an instance by ID.
     */
    fun getInstance(id: String): MofObject? {
        return repository.get(id)
    }

    /**
     * Delete an instance by ID.
     */
    fun deleteInstance(id: String): Boolean {
        return repository.delete(id)
    }

    /**
     * Set a property on an instance.
     */
    fun setProperty(instanceId: String, propertyName: String, value: Any?) {
        val instance = repository.get(instanceId)
            ?: throw IllegalArgumentException("Instance not found: $instanceId")
        mofEngine.setProperty(instance, propertyName, value)
    }

    /**
     * Get a property from an instance.
     */
    fun getProperty(instanceId: String, propertyName: String): Any? {
        val instance = repository.get(instanceId)
            ?: throw IllegalArgumentException("Instance not found: $instanceId")
        return mofEngine.getProperty(instance, propertyName)
    }

    /**
     * Validate an instance against its metaclass constraints.
     */
    fun validateInstance(instanceId: String): List<String> {
        val instance = repository.get(instanceId)
            ?: throw IllegalArgumentException("Instance not found: $instanceId")
        return mofEngine.validate(instance)
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
        val instance = repository.get(instanceId)
            ?: throw IllegalArgumentException("Instance not found: $instanceId")
        return mofEngine.invokeOperation(instance, operationName, arguments)
    }

    // ===== Query and Search =====

    /**
     * Get all instances of a specific type.
     */
    fun getInstancesByType(className: String): List<MofObject> {
        return repository.getByType(className)
    }

    /**
     * Get all instances where a property has a specific value.
     */
    fun getInstancesByProperty(propertyName: String, value: Any): List<MofObject> {
        return repository.getByProperty(propertyName, value)
    }

    /**
     * Get repository statistics.
     */
    fun getStatistics() = repository.getStatistics()

    /**
     * Clear all instances (keeps metamodel).
     */
    fun clearInstances() {
        repository.clear()
    }

    /**
     * Clear everything (metamodel and instances).
     */
    fun clearAll() {
        repository.clear()
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
