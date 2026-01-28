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

import org.openmbee.gearshift.framework.runtime.MDMLink
import org.openmbee.gearshift.framework.runtime.MDMObject
import org.openmbee.gearshift.framework.runtime.MetamodelRegistry
import org.openmbee.gearshift.framework.meta.MetaAssociation
import org.openmbee.gearshift.framework.meta.MetaClass

/**
 * MDM Graph-Native Storage System
 *
 * Designed for easy integration with graph databases like Neo4j, ArangoDB, etc.
 * Treats MDMObjects as nodes and MDMLinks as directed edges in a property graph.
 *
 * Implementations:
 * - InMemoryGraphStorage: Development/testing (wraps existing repositories)
 * - Neo4jGraphStorage: Production graph database
 * - PostgresGraphStorage: JSON-based storage with SQL
 * - ArangoDBGraphStorage: Multi-model database
 */
interface GraphStorage {

    // === Node (MDMObject) Operations ===

    /**
     * Get a node by ID.
     * @return The MDMObject or null if not found
     */
    fun getNode(id: String): MDMObject?

    /**
     * Store or update a node.
     */
    fun setNode(node: MDMObject)

    /**
     * Delete a node by ID.
     * Note: Does NOT automatically delete connected edges.
     * Use deleteNodeCascade for that behavior.
     * @return true if node existed and was deleted
     */
    fun deleteNode(id: String): Boolean

    /**
     * Delete a node and all its connected edges.
     * @return List of deleted edge IDs
     */
    fun deleteNodeCascade(id: String): List<String>

    /**
     * Check if a node exists.
     */
    fun nodeExists(id: String): Boolean

    // === Edge (MDMLink) Operations ===

    /**
     * Get an edge by ID.
     * @return The MDMLink or null if not found
     */
    fun getEdge(id: String): MDMLink?

    /**
     * Store or update an edge.
     */
    fun setEdge(edge: MDMLink)

    /**
     * Delete an edge by ID.
     * @return true if edge existed and was deleted
     */
    fun deleteEdge(id: String): Boolean

    /**
     * Check if an edge exists.
     */
    fun edgeExists(id: String): Boolean

    // === Traversal Operations ===

    /**
     * Get all outgoing edges from a node.
     * @param nodeId Source node ID
     * @param edgeType Optional filter by association name
     */
    fun getOutgoingEdges(nodeId: String, edgeType: String? = null): List<MDMLink>

    /**
     * Get all incoming edges to a node.
     * @param nodeId Target node ID
     * @param edgeType Optional filter by association name
     */
    fun getIncomingEdges(nodeId: String, edgeType: String? = null): List<MDMLink>

    /**
     * Get all edges connected to a node (both directions).
     */
    fun getAllEdges(nodeId: String): List<MDMLink>

    /**
     * Get target nodes from a source via an association.
     * Convenience method combining getOutgoingEdges + getNode.
     */
    fun getTargets(sourceId: String, associationName: String): List<MDMObject> {
        return getOutgoingEdges(sourceId, associationName)
            .mapNotNull { getNode(it.targetId) }
    }

    /**
     * Get source nodes to a target via an association.
     * Convenience method combining getIncomingEdges + getNode.
     */
    fun getSources(targetId: String, associationName: String): List<MDMObject> {
        return getIncomingEdges(targetId, associationName)
            .mapNotNull { getNode(it.sourceId) }
    }

    // === Query Operations ===

    /**
     * Get all nodes of a specific type.
     */
    fun getNodesByType(type: String): List<MDMObject>

    /**
     * Get all edges of a specific association type.
     */
    fun getEdgesByType(associationName: String): List<MDMLink>

    /**
     * Find a specific edge between two nodes.
     */
    fun findEdge(sourceId: String, targetId: String, associationName: String): MDMLink?

    /**
     * Check if a link exists between two nodes.
     */
    fun linkExists(sourceId: String, targetId: String, associationName: String): Boolean {
        return findEdge(sourceId, targetId, associationName) != null
    }

    // === Bulk Operations ===

    /**
     * Get multiple nodes by ID.
     */
    fun getNodes(ids: List<String>): List<MDMObject>

    /**
     * Store multiple nodes.
     */
    fun setNodes(nodes: List<MDMObject>)

    /**
     * Store multiple edges.
     */
    fun setEdges(edges: List<MDMLink>)

    /**
     * Delete multiple nodes.
     * @return count of deleted nodes
     */
    fun deleteNodes(ids: List<String>): Int

    /**
     * Delete multiple edges.
     * @return count of deleted edges
     */
    fun deleteEdges(ids: List<String>): Int

    // === Utility Operations ===

    /**
     * Clear all data.
     */
    fun clear()

    /**
     * Get storage statistics.
     */
    fun getStats(): GraphStats

    /**
     * Export all data for serialization/backup.
     */
    fun export(): GraphData

    /**
     * Import data from serialized form.
     * Requires metamodel registry to reconstruct typed objects.
     */
    fun import(data: GraphData, registry: MetamodelRegistry)
}

/**
 * Graph storage statistics.
 */
data class GraphStats(
    val nodeCount: Int,
    val edgeCount: Int,
    val nodeTypeDistribution: Map<String, Int> = emptyMap(),
    val edgeTypeDistribution: Map<String, Int> = emptyMap()
)

/**
 * Container for graph data export/import.
 * Uses simple serializable types for database/JSON compatibility.
 */
data class GraphData(
    val nodes: List<NodeData>,
    val edges: List<EdgeData>
)

/**
 * Serializable node representation.
 *
 * Structure:
 * - id: Unique identifier (UUID)
 * - type: KerML/metamodel type (used as graph label)
 * - properties: All properties (schema + dynamic treated the same)
 * - versionInfo: Optional versioning metadata (branch, commits)
 *
 * In a graph database like Neo4j:
 * ```
 * Labels: [Node, {type}]
 * Properties: id, ...properties
 * Relationships: -[:CREATED_IN]-> Commit, -[:MODIFIED_IN]-> Commit, -[:ON_BRANCH]-> Branch
 * ```
 */
data class NodeData(
    val id: String,
    val type: String,
    val properties: Map<String, Any?>,
    val versionInfo: VersionInfo? = null
)

/**
 * Versioning metadata for a node.
 * Tracks the commit history for change tracking.
 */
data class VersionInfo(
    /**
     * ID of the branch this node belongs to.
     */
    val branchId: String,

    /**
     * ID of the commit that created this node.
     */
    val createdCommitId: String,

    /**
     * ID of the commit that last modified this node.
     * Same as createdCommitId if never modified.
     */
    val lastCommitId: String
)

/**
 * Serializable edge representation.
 *
 * Edges have:
 * - Required fields: id, sourceId, targetId, associationName
 * - isDynamic: true if this relationship is not in the metamodel
 */
data class EdgeData(
    val id: String,
    val sourceId: String,
    val targetId: String,
    val associationName: String,
    val isDynamic: Boolean = false
)

// === Conversion Extensions ===

/**
 * Convert MDMObject to serializable NodeData.
 */
fun MDMObject.toNodeData(): NodeData {
    return NodeData(
        id = this.id ?: throw IllegalStateException("MDMObject has no ID"),
        type = this.className,
        properties = this.getAllProperties()
    )
}

/**
 * Convert NodeData to MDMObject.
 * Requires MetaClass from registry to create properly typed object.
 */
fun NodeData.toMDMObject(metaClass: MetaClass): MDMObject {
    val obj = MDMObject(this.type, metaClass)
    obj.id = this.id
    this.properties.forEach { (key, value) ->
        obj.setProperty(key, value)
    }
    return obj
}

/**
 * Convert MDMLink to serializable EdgeData.
 */
fun MDMLink.toEdgeData(): EdgeData {
    return EdgeData(
        id = this.id,
        sourceId = this.sourceId,
        targetId = this.targetId,
        associationName = this.associationName,
        isDynamic = false
    )
}

/**
 * Convert EdgeData to MDMLink.
 * Requires MetaAssociation from registry to create properly typed link.
 */
fun EdgeData.toMDMLink(association: MetaAssociation): MDMLink {
    return MDMLink(
        id = this.id,
        association = association,
        sourceId = this.sourceId,
        targetId = this.targetId
    )
}
