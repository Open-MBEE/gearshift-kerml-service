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
package org.openmbee.mdm.framework.storage

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.mdm.framework.runtime.MDMLink
import org.openmbee.mdm.framework.runtime.MDMObject
import org.openmbee.mdm.framework.runtime.MetamodelRegistry
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

private val logger = KotlinLogging.logger {}

/**
 * In-memory implementation of GraphStorage using MDMObject and MDMLink directly.
 *
 * Provides efficient indexing for:
 * - Node lookup by ID and type
 * - Edge lookup by ID, type, source, and target
 * - Bidirectional traversal (outgoing/incoming edges)
 *
 * Thread-safe using ConcurrentHashMap and synchronized collections.
 * Uses LinkedHashSet to maintain insertion order for ordered associations.
 */
class InMemoryGraphStorage : GraphStorage {

    // Primary storage
    private val nodes = ConcurrentHashMap<String, MDMObject>()
    private val edges = ConcurrentHashMap<String, MDMLink>()

    // Node indexes
    private val nodesByType = ConcurrentHashMap<String, MutableSet<String>>()

    // Edge indexes
    private val edgesByType = ConcurrentHashMap<String, MutableSet<String>>()
    private val outgoingEdges = ConcurrentHashMap<String, MutableSet<String>>()  // nodeId -> edgeIds
    private val incomingEdges = ConcurrentHashMap<String, MutableSet<String>>()  // nodeId -> edgeIds

    // Compound indexes for efficient filtered traversal
    private val outgoingByType = ConcurrentHashMap<String, MutableSet<String>>()  // "nodeId:type" -> edgeIds
    private val incomingByType = ConcurrentHashMap<String, MutableSet<String>>()  // "nodeId:type" -> edgeIds

    // === Node Operations ===

    override fun getNode(id: String): MDMObject? = nodes[id]

    override fun setNode(node: MDMObject) {
        val nodeId = node.id ?: throw IllegalArgumentException("MDMObject must have an ID")
        val existing = nodes[nodeId]

        // Update type index if type changed or new node
        if (existing != null && existing.className != node.className) {
            nodesByType[existing.className]?.remove(nodeId)
        }

        nodes[nodeId] = node
        nodesByType.getOrPut(node.className) { createOrderedSet() }.add(nodeId)

        logger.debug { "Set node $nodeId of type ${node.className}" }
    }

    override fun deleteNode(id: String): Boolean {
        val node = nodes.remove(id) ?: return false

        // Update type index
        nodesByType[node.className]?.remove(id)

        logger.debug { "Deleted node $id" }
        return true
    }

    override fun deleteNodeCascade(id: String): List<String> {
        val deletedEdgeIds = mutableListOf<String>()

        // Delete all connected edges first
        val connectedEdges = getAllEdges(id)
        connectedEdges.forEach { edge ->
            deleteEdge(edge.id)
            deletedEdgeIds.add(edge.id)
        }

        // Delete the node
        deleteNode(id)

        return deletedEdgeIds
    }

    override fun nodeExists(id: String): Boolean = nodes.containsKey(id)

    // === Edge Operations ===

    override fun getEdge(id: String): MDMLink? = edges[id]

    override fun setEdge(edge: MDMLink) {
        // Remove old edge if updating (to clean up indexes)
        if (edges.containsKey(edge.id)) {
            deleteEdge(edge.id)
        }

        edges[edge.id] = edge

        // Update indexes
        edgesByType.getOrPut(edge.associationName) { createOrderedSet() }.add(edge.id)
        outgoingEdges.getOrPut(edge.sourceId) { createOrderedSet() }.add(edge.id)
        incomingEdges.getOrPut(edge.targetId) { createOrderedSet() }.add(edge.id)

        // Compound indexes
        outgoingByType.getOrPut(outgoingKey(edge.sourceId, edge.associationName)) { createOrderedSet() }.add(edge.id)
        incomingByType.getOrPut(incomingKey(edge.targetId, edge.associationName)) { createOrderedSet() }.add(edge.id)

        logger.debug { "Set edge ${edge.id}: ${edge.sourceId} --[${edge.associationName}]--> ${edge.targetId}" }
    }

    override fun deleteEdge(id: String): Boolean {
        val edge = edges.remove(id) ?: return false

        // Update all indexes
        edgesByType[edge.associationName]?.remove(id)
        outgoingEdges[edge.sourceId]?.remove(id)
        incomingEdges[edge.targetId]?.remove(id)
        outgoingByType[outgoingKey(edge.sourceId, edge.associationName)]?.remove(id)
        incomingByType[incomingKey(edge.targetId, edge.associationName)]?.remove(id)

        logger.debug { "Deleted edge $id" }
        return true
    }

    override fun edgeExists(id: String): Boolean = edges.containsKey(id)

    // === Traversal Operations ===

    override fun getOutgoingEdges(nodeId: String, edgeType: String?): List<MDMLink> {
        val edgeIds = if (edgeType != null) {
            outgoingByType[outgoingKey(nodeId, edgeType)] ?: emptySet()
        } else {
            outgoingEdges[nodeId] ?: emptySet()
        }

        return edgeIds.mapNotNull { edges[it] }
    }

    override fun getIncomingEdges(nodeId: String, edgeType: String?): List<MDMLink> {
        val edgeIds = if (edgeType != null) {
            incomingByType[incomingKey(nodeId, edgeType)] ?: emptySet()
        } else {
            incomingEdges[nodeId] ?: emptySet()
        }

        return edgeIds.mapNotNull { edges[it] }
    }

    override fun getAllEdges(nodeId: String): List<MDMLink> {
        val outgoing = outgoingEdges[nodeId]?.mapNotNull { edges[it] } ?: emptyList()
        val incoming = incomingEdges[nodeId]?.mapNotNull { edges[it] } ?: emptyList()
        return (outgoing + incoming).distinctBy { it.id }
    }

    // === Query Operations ===

    override fun getNodesByType(type: String): List<MDMObject> {
        val ids = nodesByType[type] ?: return emptyList()
        return ids.mapNotNull { nodes[it] }
    }

    override fun getEdgesByType(associationName: String): List<MDMLink> {
        val ids = edgesByType[associationName] ?: return emptyList()
        return ids.mapNotNull { edges[it] }
    }

    override fun findEdge(sourceId: String, targetId: String, associationName: String): MDMLink? {
        return getOutgoingEdges(sourceId, associationName).find { it.targetId == targetId }
    }

    // === Bulk Operations ===

    override fun getNodes(ids: List<String>): List<MDMObject> {
        return ids.mapNotNull { nodes[it] }
    }

    override fun setNodes(nodes: List<MDMObject>) {
        nodes.forEach { setNode(it) }
    }

    override fun setEdges(edges: List<MDMLink>) {
        edges.forEach { setEdge(it) }
    }

    override fun deleteNodes(ids: List<String>): Int {
        return ids.count { deleteNode(it) }
    }

    override fun deleteEdges(ids: List<String>): Int {
        return ids.count { deleteEdge(it) }
    }

    // === Utility Operations ===

    override fun clear() {
        nodes.clear()
        edges.clear()
        nodesByType.clear()
        edgesByType.clear()
        outgoingEdges.clear()
        incomingEdges.clear()
        outgoingByType.clear()
        incomingByType.clear()
        logger.debug { "InMemoryGraphStorage cleared" }
    }

    override fun getStats(): GraphStats {
        return GraphStats(
            nodeCount = nodes.size,
            edgeCount = edges.size,
            nodeTypeDistribution = nodesByType.mapValues { it.value.size },
            edgeTypeDistribution = edgesByType.mapValues { it.value.size }
        )
    }

    override fun export(): GraphData {
        return GraphData(
            nodes = nodes.values.map { it.toNodeData() },
            edges = edges.values.map { it.toEdgeData() }
        )
    }

    override fun import(data: GraphData, registry: MetamodelRegistry) {
        // Convert NodeData to MDMObject using metamodel
        data.nodes.forEach { nodeData ->
            val metaClass = registry.getClass(nodeData.type)
                ?: throw IllegalArgumentException("Unknown type: ${nodeData.type}")
            val obj = nodeData.toMDMObject(metaClass)
            setNode(obj)
        }

        // Convert EdgeData to MDMLink using metamodel
        data.edges.forEach { edgeData ->
            val association = registry.getAssociation(edgeData.associationName)
                ?: throw IllegalArgumentException("Unknown association: ${edgeData.associationName}")
            val link = edgeData.toMDMLink(association)
            setEdge(link)
        }
    }

    // === Helper Methods ===

    /**
     * Creates a thread-safe, ordered set for storing IDs.
     * LinkedHashSet maintains insertion order while Set semantics ensure uniqueness.
     */
    private fun createOrderedSet(): MutableSet<String> =
        Collections.synchronizedSet(LinkedHashSet())

    private fun outgoingKey(nodeId: String, edgeType: String) = "$nodeId:out:$edgeType"
    private fun incomingKey(nodeId: String, edgeType: String) = "$nodeId:in:$edgeType"

    // === Debug/Inspection Methods ===

    /**
     * Get all nodes (for debugging/export).
     */
    fun getAllNodes(): List<MDMObject> = nodes.values.toList()

    /**
     * Get all edges (for debugging/export).
     */
    fun getAllEdgesRaw(): List<MDMLink> = edges.values.toList()
}
