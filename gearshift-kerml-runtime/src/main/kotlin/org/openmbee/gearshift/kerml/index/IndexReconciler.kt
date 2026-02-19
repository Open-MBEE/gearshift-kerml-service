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
package org.openmbee.gearshift.kerml.index

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.gearshift.generated.interfaces.Element
import org.openmbee.gearshift.kerml.LibraryElementIdAssigner
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MDMObject
import org.openmbee.mdm.framework.runtime.MountableEngine

private val logger = KotlinLogging.logger {}

/**
 * Reconciles element IDs after a reparse using a previous [ModelIndex].
 *
 * After parsing produces fresh UUIDs for every element, this reconciler:
 * 1. Walks all elements and builds a qualified-name map for the new parse
 * 2. Looks up each QN in the previous index to find the stable ID
 * 3. Remaps any element whose QN existed before to its original ID
 * 4. Returns a new [ModelIndex] reflecting the reconciled state
 *
 * Elements whose QN did not exist in the previous index keep their new IDs
 * (they are genuinely new elements). Elements whose QN is absent from the
 * new parse are dropped (they were deleted).
 */
object IndexReconciler {

    /**
     * Reconcile element IDs in [engine] against [previousIndex].
     *
     * @param engine The engine containing freshly-parsed elements
     * @param previousIndex The index from the prior commit (null for first commit)
     * @return A new ModelIndex reflecting the reconciled state
     */
    fun reconcile(engine: MDMEngine, previousIndex: ModelIndex?): ModelIndex {
        val newIndex = ModelIndex()

        // Get local elements only (exclude mounted library elements)
        val elements = if (engine is MountableEngine) {
            engine.getLocalElements()
        } else {
            engine.getAllElements()
        }

        // Build QN → new ID map for all named elements
        val currentQnMap = buildQualifiedNameMap(engine, elements)
        logger.debug { "Reconciling ${currentQnMap.size} named elements against previous index (${previousIndex?.size() ?: 0} entries)" }

        if (previousIndex == null) {
            // First commit — just build the index from current state
            for ((qn, element) in currentQnMap) {
                val id = element.id ?: continue
                newIndex.put(id, qn)
            }
            return newIndex
        }

        // Reconcile: remap IDs for elements whose QN existed before
        var remapped = 0
        var newElements = 0
        for ((qn, element) in currentQnMap) {
            val currentId = element.id ?: continue
            val previousId = previousIndex.getId(qn)

            if (previousId != null && previousId != currentId) {
                // This element existed before — remap to the stable ID
                engine.reassignElementId(currentId, previousId)
                newIndex.put(previousId, qn)
                remapped++
            } else {
                // New element or ID already matches — keep current ID
                newIndex.put(currentId, qn)
                if (previousId == null) newElements++
            }
        }

        logger.debug { "Reconciliation complete: $remapped remapped, $newElements new" }
        return newIndex
    }

    /**
     * Build a map of qualified name to element for all elements that have
     * a computable qualified name (i.e., named elements in the containment tree).
     *
     * Uses the pre-built QN index when available for O(1) lookups,
     * falling back to [LibraryElementIdAssigner.computePath] otherwise.
     */
    private fun buildQualifiedNameMap(engine: MDMEngine, elements: List<MDMObject>): Map<String, MDMObject> {
        val qnIndex = engine.qualifiedNameIndex
        val qnMap = mutableMapOf<String, MDMObject>()
        for (element in elements) {
            if (element !is Element) continue
            // Fast path: use pre-built QN index
            val qn = qnIndex?.getQualifiedName(element.id ?: "")
                ?.let { it } // QN index returns null for unnamed elements
                ?: computeStructuralIdentity(element) // Fallback
            if (qn.isNotEmpty()) {
                qnMap[qn] = element
            }
        }
        return qnMap
    }

    /**
     * Compute a stable structural identity for an element.
     *
     * Uses the path() computation from [LibraryElementIdAssigner] which
     * handles named elements (qualified name), relationships (positional),
     * and owning memberships.
     */
    private fun computeStructuralIdentity(element: Element): String {
        return LibraryElementIdAssigner.computePath(element)
    }
}
