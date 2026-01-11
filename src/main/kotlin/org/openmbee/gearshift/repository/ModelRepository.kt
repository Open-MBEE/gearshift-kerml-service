package org.openmbee.gearshift.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.gearshift.engine.MofObject
import org.openmbee.gearshift.metamodel.MetaClass
import java.util.concurrent.ConcurrentHashMap

private val logger = KotlinLogging.logger {}

/**
 * Repository for managing model instances (MofObjects).
 * Provides CRUD operations, indexing, and querying capabilities.
 */
class ModelRepository {
    private val objects = ConcurrentHashMap<String, MofObject>()
    private val typeIndex = ConcurrentHashMap<String, MutableSet<String>>()
    private val propertyIndex = ConcurrentHashMap<String, MutableMap<Any?, MutableSet<String>>>()

    /**
     * Store an object in the repository.
     */
    fun store(id: String, obj: MofObject) {
        objects[id] = obj

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
    fun get(id: String): MofObject? = objects[id]

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
     * Get all objects of a specific type.
     */
    fun getByType(className: String): List<MofObject> {
        val ids = typeIndex[className] ?: return emptyList()
        return ids.mapNotNull { objects[it] }
    }

    /**
     * Get all objects where a property has a specific value.
     */
    fun getByProperty(propertyName: String, value: Any): List<MofObject> {
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
    fun getAll(): List<MofObject> = objects.values.toList()

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
