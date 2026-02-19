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

private val logger = KotlinLogging.logger {}

/**
 * Pluggable strategy so different metamodels can define how ownership and naming work
 * for qualified name computation.
 */
interface QualifiedNameConfig {
    /** Separator between QN segments (e.g., "::" for KerML) */
    val separator: String

    /**
     * Get the local name for an element (reads stored properties directly, no OCL).
     * Returns null if the element is unnamed.
     */
    fun getLocalName(element: MDMObject): String?

    /**
     * Escape a local name if needed (e.g., wrap non-basic names in single quotes).
     * Default: return name as-is.
     */
    fun escapeName(name: String): String = name

    /** The derived property name this index provides values for (e.g., "qualifiedName") */
    val derivedPropertyName: String get() = "qualifiedName"

    /**
     * Get the child elements owned by this element for BFS traversal.
     * Implementations should use direct graph traversal (not OCL) for efficiency.
     */
    fun getOwnedElements(engine: MDMEngine, element: MDMObject): List<MDMObject>
}

/**
 * BFS-based index that computes qualified names top-down from root namespaces.
 *
 * Instead of each element independently recursing UP the ownership chain via OCL
 * (O(n × depth) total), a single top-down BFS from root namespaces propagates the
 * QN prefix downward (O(n) total). The index provides O(1) bidirectional lookup:
 * - ID → qualified name
 * - qualified name → ID
 *
 * Incremental maintenance via [LifecycleHandler]: ownership changes and renames
 * trigger targeted subtree rebuilds rather than full reindexing.
 */
class QualifiedNameIndex(
    val config: QualifiedNameConfig
) : LifecycleHandler {

    // Bidirectional maps
    private val idToQn = HashMap<String, String>()
    private val qnToId = HashMap<String, String>()

    // Reverse ownership: childId → parentId (for incremental subtree rebuilds)
    private val childToParent = HashMap<String, String>()

    // Forward ownership: parentId → ordered list of childIds
    private val parentToChildren = HashMap<String, MutableList<String>>()

    override val priority: Int = 50 // Run before default handlers at 100

    /**
     * When true, lifecycle events are ignored. Set during bulk operations
     * (e.g., parsing) where a full rebuild follows. This avoids wasted work
     * from incremental updates that would be immediately overwritten.
     */
    var suspended = false

    /**
     * Full BFS build from root namespaces. Clears and rebuilds all maps.
     */
    fun build(engine: MDMEngine) {
        val start = System.currentTimeMillis()
        clear()

        val roots = engine.getRootNamespaces()
        val queue = ArrayDeque<Triple<MDMObject, String?, MDMObject?>>() // (element, parentQN, parentElement)

        for (root in roots) {
            queue.add(Triple(root, null, null))
        }

        var elementCount = 0
        var namedCount = 0

        while (queue.isNotEmpty()) {
            val (element, parentQN, parent) = queue.removeFirst()
            val elementId = element.id ?: continue
            elementCount++

            // Track ownership tree
            if (parent != null) {
                val parentId = parent.id
                if (parentId != null) {
                    childToParent[elementId] = parentId
                    parentToChildren.getOrPut(parentId) { mutableListOf() }.add(elementId)
                }
            }

            // Compute local name
            val localName = config.getLocalName(element)
            val escapedName = if (localName != null) config.escapeName(localName) else null

            // Compute QN
            var qn: String? = when {
                escapedName == null -> null // unnamed element
                parentQN == null -> escapedName // top-level
                else -> parentQN + config.separator + escapedName
            }

            // Disambiguate: skip if sibling already has this name
            if (qn != null) {
                if (qnToId.containsKey(qn)) {
                    qn = null // duplicate name among siblings → no QN
                } else {
                    idToQn[elementId] = qn
                    qnToId[qn] = elementId
                    namedCount++
                }
            }

            // Enqueue children
            try {
                val children = config.getOwnedElements(engine, element)
                for (child in children) {
                    queue.add(Triple(child, qn, element))
                }
            } catch (e: Exception) {
                logger.debug { "Error getting children of ${element.className}[$elementId]: ${e.message}" }
            }
        }

        logger.debug { "QN index built: $namedCount named / $elementCount total elements in ${System.currentTimeMillis() - start}ms" }
    }

    /**
     * O(1) lookup: element ID → qualified name.
     */
    fun getQualifiedName(elementId: String): String? = idToQn[elementId]

    /**
     * O(1) reverse lookup: qualified name → element ID.
     */
    fun resolveQualifiedName(qualifiedName: String): String? = qnToId[qualifiedName]

    /**
     * Number of named elements in the index.
     */
    fun size(): Int = idToQn.size

    /**
     * Clear all index data.
     */
    fun clear() {
        idToQn.clear()
        qnToId.clear()
        childToParent.clear()
        parentToChildren.clear()
    }

    /**
     * Export to a [ModelIndex]-compatible format for reconciliation.
     * Returns a map of elementId → qualifiedName.
     */
    fun toIdQnMap(): Map<String, String> = idToQn.toMap()

    // ===== Incremental Maintenance via LifecycleHandler =====

    override fun handle(event: LifecycleEvent, model: MDMEngine) {
        if (suspended) return
        when (event) {
            is LifecycleEvent.PropertyChanged -> {
                if (event.propertyName == "declaredName" || event.propertyName == "declaredShortName") {
                    rebuildSubtree(model, event.instance)
                }
            }
            is LifecycleEvent.OwnershipEstablished -> {
                handleOwnershipEstablished(model, event.parent, event.child)
            }
            is LifecycleEvent.OwnershipRemoved -> {
                handleOwnershipRemoved(event.child)
            }
            is LifecycleEvent.InstanceDeleting -> {
                removeElement(event.instance.id ?: return)
            }
            else -> { /* no-op for InstanceCreated, LinkCreated, LinkDeleting */ }
        }
    }

    /**
     * Handle a new child being placed under a parent in the ownership tree.
     * Adds the child and its entire subtree to the index.
     */
    private fun handleOwnershipEstablished(engine: MDMEngine, parent: MDMObject, child: MDMObject) {
        val parentId = parent.id ?: return
        val childId = child.id ?: return

        // Update ownership tree
        childToParent[childId] = parentId
        parentToChildren.getOrPut(parentId) { mutableListOf() }.add(childId)

        // Compute QN for the child from parent's QN
        val parentQN = idToQn[parentId]
        val localName = config.getLocalName(child)
        val escapedName = if (localName != null) config.escapeName(localName) else null

        var qn: String? = when {
            escapedName == null -> null
            parentQN == null -> escapedName
            else -> parentQN + config.separator + escapedName
        }

        if (qn != null) {
            if (qnToId.containsKey(qn)) {
                qn = null
            } else {
                idToQn[childId] = qn
                qnToId[qn] = childId
            }
        }

        child.derivedCache.clear()

        // Also index the child's descendants (they may already have been parsed)
        try {
            val grandchildren = config.getOwnedElements(engine, child)
            for (grandchild in grandchildren) {
                handleOwnershipEstablished(engine, child, grandchild)
            }
        } catch (e: Exception) {
            logger.debug { "Error indexing children of ${child.className}[$childId]: ${e.message}" }
        }

        logger.trace { "Ownership established: ${idToQn[parentId]} → $qn" }
    }

    /**
     * Handle a child being removed from the ownership tree.
     * Removes the child and its entire subtree from the index.
     */
    private fun handleOwnershipRemoved(child: MDMObject) {
        val childId = child.id ?: return

        // Remove from ownership tree
        val oldParentId = childToParent.remove(childId)
        if (oldParentId != null) {
            parentToChildren[oldParentId]?.remove(childId)
        }

        // Remove QN entries for child and all descendants
        removeSubtree(childId)
        child.derivedCache.clear()

        logger.trace { "Ownership removed: child $childId" }
    }

    /**
     * Remove a single element from all index maps.
     * Used when an element is deleted.
     */
    private fun removeElement(elementId: String) {
        // Remove QN entries
        val qn = idToQn.remove(elementId)
        if (qn != null) {
            qnToId.remove(qn)
        }

        // Remove from ownership tree
        val parentId = childToParent.remove(elementId)
        if (parentId != null) {
            parentToChildren[parentId]?.remove(elementId)
        }

        // Re-parent children (they become orphans in the index, will be cleaned up or re-indexed)
        val children = parentToChildren.remove(elementId)
        if (children != null) {
            for (childId in children) {
                childToParent.remove(childId)
                removeSubtree(childId)
            }
        }
    }

    /**
     * Rebuild QN for an element and all its descendants.
     * Used when an element is renamed — QNs cascade to all children.
     */
    private fun rebuildSubtree(engine: MDMEngine, element: MDMObject) {
        val elementId = element.id ?: return

        // Remove old QN for this element and all descendants
        removeSubtree(elementId)

        // Recompute QN for this element
        val parentId = childToParent[elementId]
        val parentQN = if (parentId != null) idToQn[parentId] else null

        // Rebuild from this element downward
        val queue = ArrayDeque<Triple<MDMObject, String?, MDMObject?>>()
        val parentElement = if (parentId != null) engine.getElement(parentId) else null
        queue.add(Triple(element, parentQN, parentElement))

        while (queue.isNotEmpty()) {
            val (current, pQN, _) = queue.removeFirst()
            val currentId = current.id ?: continue

            val localName = config.getLocalName(current)
            val escapedName = if (localName != null) config.escapeName(localName) else null

            var qn: String? = when {
                escapedName == null -> null
                pQN == null -> escapedName
                else -> pQN + config.separator + escapedName
            }

            if (qn != null) {
                if (qnToId.containsKey(qn)) {
                    qn = null
                } else {
                    idToQn[currentId] = qn
                    qnToId[qn] = currentId
                }
            }

            // Also invalidate the derived cache for this element so the engine picks up the new QN
            current.derivedCache.clear()

            // Continue to children
            val childIds = parentToChildren[currentId] ?: continue
            for (childId in childIds) {
                val child = engine.getElement(childId) ?: continue
                queue.add(Triple(child, qn, current))
            }
        }
    }

    /**
     * Remove QN entries for an element and all its descendants.
     */
    private fun removeSubtree(elementId: String) {
        // Remove this element's QN
        val qn = idToQn.remove(elementId)
        if (qn != null) {
            qnToId.remove(qn)
        }

        // Recurse into children
        val children = parentToChildren[elementId] ?: return
        for (childId in children) {
            removeSubtree(childId)
        }
    }
}
