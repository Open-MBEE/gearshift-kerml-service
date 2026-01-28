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
package org.openmbee.gearshift.framework.storage

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.gearshift.framework.runtime.MDMLink
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

private val logger = KotlinLogging.logger {}

/**
 * Repository for managing association links (edges in the model graph).
 * Provides efficient indexing for traversing relationships in both directions.
 *
 * Indexes maintained:
 * - By link ID (primary key)
 * - By association name (all links of a type)
 * - By source instance ID (outgoing links from a node)
 * - By target instance ID (incoming links to a node)
 * - By (association, source) pair (outgoing links of specific type from a node)
 * - By (association, target) pair (incoming links of specific type to a node)
 */
class LinkRepository {
    // Primary storage: linkId -> MDMLink
    private val links = ConcurrentHashMap<String, MDMLink>()

    // Index by association name: associationName -> Set<linkId>
    private val byAssociation = ConcurrentHashMap<String, MutableSet<String>>()

    // Index by source instance: sourceId -> Set<linkId>
    private val bySource = ConcurrentHashMap<String, MutableSet<String>>()

    // Index by target instance: targetId -> Set<linkId>
    private val byTarget = ConcurrentHashMap<String, MutableSet<String>>()

    // Index by (association, source): "assocName:sourceId" -> Set<linkId>
    private val byAssociationSource = ConcurrentHashMap<String, MutableSet<String>>()

    // Index by (association, target): "assocName:targetId" -> Set<linkId>
    private val byAssociationTarget = ConcurrentHashMap<String, MutableSet<String>>()

    /**
     * Store a link in the repository.
     * Uses LinkedHashSet to maintain insertion order for isOrdered associations.
     */
    fun store(link: MDMLink) {
        links[link.id] = link

        // Update all indexes using ordered sets (LinkedHashSet) to preserve insertion order
        // This is important for isOrdered=true association ends
        byAssociation.getOrPut(link.associationName) { createOrderedSet() }.add(link.id)
        bySource.getOrPut(link.sourceId) { createOrderedSet() }.add(link.id)
        byTarget.getOrPut(link.targetId) { createOrderedSet() }.add(link.id)
        byAssociationSource.getOrPut(associationSourceKey(link)) { createOrderedSet() }.add(link.id)
        byAssociationTarget.getOrPut(associationTargetKey(link)) { createOrderedSet() }.add(link.id)

        logger.debug { "Stored link ${link.id}: ${link.sourceId} --[${link.associationName}]--> ${link.targetId}" }
    }

    /**
     * Creates a thread-safe, ordered set for storing link IDs.
     * LinkedHashSet maintains insertion order while Set semantics ensure link ID uniqueness.
     */
    private fun createOrderedSet(): MutableSet<String> =
        Collections.synchronizedSet(LinkedHashSet())

    /**
     * Retrieve a link by ID.
     */
    fun get(id: String): MDMLink? = links[id]

    /**
     * Delete a link by ID.
     */
    fun delete(id: String): Boolean {
        val link = links.remove(id) ?: return false

        // Remove from all indexes
        byAssociation[link.associationName]?.remove(id)
        bySource[link.sourceId]?.remove(id)
        byTarget[link.targetId]?.remove(id)
        byAssociationSource[associationSourceKey(link)]?.remove(id)
        byAssociationTarget[associationTargetKey(link)]?.remove(id)

        logger.debug { "Deleted link $id" }
        return true
    }

    /**
     * Get all links of a specific association type.
     */
    fun getByAssociation(associationName: String): List<MDMLink> {
        val ids = byAssociation[associationName] ?: return emptyList()
        return ids.mapNotNull { links[it] }
    }

    /**
     * Get all outgoing links from a source instance.
     */
    fun getBySource(sourceId: String): List<MDMLink> {
        val ids = bySource[sourceId] ?: return emptyList()
        return ids.mapNotNull { links[it] }
    }

    /**
     * Get all incoming links to a target instance.
     */
    fun getByTarget(targetId: String): List<MDMLink> {
        val ids = byTarget[targetId] ?: return emptyList()
        return ids.mapNotNull { links[it] }
    }

    /**
     * Get outgoing links of a specific association type from a source instance.
     * This is the primary traversal method: "from source, follow association to get targets"
     */
    fun getByAssociationAndSource(associationName: String, sourceId: String): List<MDMLink> {
        val key = "$associationName:$sourceId"
        val ids = byAssociationSource[key] ?: return emptyList()
        return ids.mapNotNull { links[it] }
    }

    /**
     * Get incoming links of a specific association type to a target instance.
     * This is the reverse traversal: "from target, reverse follow association to get sources"
     */
    fun getByAssociationAndTarget(associationName: String, targetId: String): List<MDMLink> {
        val key = "$associationName:$targetId"
        val ids = byAssociationTarget[key] ?: return emptyList()
        return ids.mapNotNull { links[it] }
    }

    /**
     * Get all links involving an instance (either as source or target).
     */
    fun getByInstance(instanceId: String): List<MDMLink> {
        val sourceLinks = bySource[instanceId]?.mapNotNull { links[it] } ?: emptyList()
        val targetLinks = byTarget[instanceId]?.mapNotNull { links[it] } ?: emptyList()
        return (sourceLinks + targetLinks).distinctBy { it.id }
    }

    /**
     * Check if a specific link exists between two instances via an association.
     */
    fun linkExists(associationName: String, sourceId: String, targetId: String): Boolean {
        return getByAssociationAndSource(associationName, sourceId).any { it.targetId == targetId }
    }

    /**
     * Find the link between two specific instances via an association.
     */
    fun findLink(associationName: String, sourceId: String, targetId: String): MDMLink? {
        return getByAssociationAndSource(associationName, sourceId).find { it.targetId == targetId }
    }

    /**
     * Delete all links involving an instance (for cascade delete).
     * Returns the list of deleted links.
     */
    fun deleteByInstance(instanceId: String): List<MDMLink> {
        val linksToDelete = getByInstance(instanceId)
        linksToDelete.forEach { delete(it.id) }
        return linksToDelete
    }

    /**
     * Delete all links of a specific association type from a source.
     */
    fun deleteByAssociationAndSource(associationName: String, sourceId: String): List<MDMLink> {
        val linksToDelete = getByAssociationAndSource(associationName, sourceId)
        linksToDelete.forEach { delete(it.id) }
        return linksToDelete
    }

    /**
     * Count links of a specific association type from a source.
     * Used for multiplicity validation.
     */
    fun countByAssociationAndSource(associationName: String, sourceId: String): Int {
        val key = "$associationName:$sourceId"
        return byAssociationSource[key]?.size ?: 0
    }

    /**
     * Count links of a specific association type to a target.
     * Used for multiplicity validation.
     */
    fun countByAssociationAndTarget(associationName: String, targetId: String): Int {
        val key = "$associationName:$targetId"
        return byAssociationTarget[key]?.size ?: 0
    }

    /**
     * Get all link IDs.
     */
    fun getAllIds(): Set<String> = links.keys.toSet()

    /**
     * Get all links.
     */
    fun getAll(): List<MDMLink> = links.values.toList()

    /**
     * Check if a link exists by ID.
     */
    fun exists(id: String): Boolean = links.containsKey(id)

    /**
     * Clear all links.
     */
    fun clear() {
        links.clear()
        byAssociation.clear()
        bySource.clear()
        byTarget.clear()
        byAssociationSource.clear()
        byAssociationTarget.clear()
        logger.debug { "LinkRepository cleared" }
    }

    /**
     * Get repository statistics.
     */
    fun getStatistics(): LinkRepositoryStatistics {
        return LinkRepositoryStatistics(
            totalLinks = links.size,
            associationDistribution = byAssociation.mapValues { it.value.size }
        )
    }

    private fun associationSourceKey(link: MDMLink) = "${link.associationName}:${link.sourceId}"
    private fun associationTargetKey(link: MDMLink) = "${link.associationName}:${link.targetId}"
}

data class LinkRepositoryStatistics(
    val totalLinks: Int,
    val associationDistribution: Map<String, Int>
)
