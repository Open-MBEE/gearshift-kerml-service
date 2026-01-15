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
package org.openmbee.gearshift.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.gearshift.engine.MDMObject
import java.util.concurrent.ConcurrentHashMap

private val logger = KotlinLogging.logger {}

/**
 * Repository for managing model instances (MDMObjects).
 * Provides CRUD operations, indexing, and querying capabilities.
 */
class ModelRepository {
    private val objects = ConcurrentHashMap<String, MDMObject>()
    private val typeIndex = ConcurrentHashMap<String, MutableSet<String>>()
    private val propertyIndex = ConcurrentHashMap<String, MutableMap<Any?, MutableSet<String>>>()

    /**
     * Store an object in the repository.
     */
    fun store(id: String, obj: MDMObject) {
        objects[id] = obj
        obj.id = id  // Set the ID on the object for reverse lookup

        // Update type index
        typeIndex.getOrPut(obj.className) { ConcurrentHashMap.newKeySet() }.add(id)

        // Update property indices
        obj.getAllProperties().forEach { (propName, value) ->
            if (value != null) {
                propertyIndex
                    .getOrPut(propName) { ConcurrentHashMap() }
                    .getOrPut(value) { ConcurrentHashMap.newKeySet() }
                    .add(id)
            }
        }

        logger.debug { "Stored object $id of type ${obj.className}" }
    }

    /**
     * Retrieve an object by ID.
     */
    fun get(id: String): MDMObject? = objects[id]

    /**
     * Delete an object by ID.
     */
    fun delete(id: String): Boolean {
        val obj = objects.remove(id) ?: return false

        // Update type index
        typeIndex[obj.className]?.remove(id)

        // Update property indices
        obj.getAllProperties().forEach { (propName, value) ->
            if (value != null) {
                propertyIndex[propName]?.get(value)?.remove(id)
            }
        }

        logger.debug { "Deleted object $id" }
        return true
    }

    /**
     * Update the property index when a property value changes.
     */
    fun updatePropertyIndex(id: String, propertyName: String, oldValue: Any?, newValue: Any?) {
        // Remove old value from index
        if (oldValue != null) {
            propertyIndex[propertyName]?.get(oldValue)?.remove(id)
        }

        // Add new value to index
        if (newValue != null) {
            propertyIndex
                .getOrPut(propertyName) { ConcurrentHashMap() }
                .getOrPut(newValue) { ConcurrentHashMap.newKeySet() }
                .add(id)
        }
    }

    /**
     * Get all objects of a specific type.
     */
    fun getByType(className: String): List<MDMObject> {
        val ids = typeIndex[className] ?: return emptyList()
        return ids.mapNotNull { objects[it] }
    }

    /**
     * Get all objects where a property has a specific value.
     */
    fun getByProperty(propertyName: String, value: Any): List<MDMObject> {
        val ids = propertyIndex[propertyName]?.get(value) ?: return emptyList()
        return ids.mapNotNull { objects[it] }
    }

    /**
     * Get all object IDs.
     */
    fun getAllIds(): Set<String> = objects.keys.toSet()

    /**
     * Get all objects.
     */
    fun getAll(): List<MDMObject> = objects.values.toList()

    /**
     * Count objects of a specific type.
     */
    fun countByType(className: String): Int = typeIndex[className]?.size ?: 0

    /**
     * Check if an object exists.
     */
    fun exists(id: String): Boolean = objects.containsKey(id)

    /**
     * Clear all objects.
     */
    fun clear() {
        objects.clear()
        typeIndex.clear()
        propertyIndex.clear()
        logger.debug { "Repository cleared" }
    }

    /**
     * Get repository statistics.
     */
    fun getStatistics(): RepositoryStatistics {
        return RepositoryStatistics(
            totalObjects = objects.size,
            typeDistribution = typeIndex.mapValues { it.value.size }
        )
    }
}

data class RepositoryStatistics(
    val totalObjects: Int,
    val typeDistribution: Map<String, Int>
)
