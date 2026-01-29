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
package org.openmbee.gearshift.kerml.parser.visitors.base

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.gearshift.generated.interfaces.Element
import org.openmbee.gearshift.generated.interfaces.ModelElement

private val logger = KotlinLogging.logger {}

/**
 * Represents a reference that needs to be resolved after parsing completes.
 *
 * @param sourceElementId The ID of the element containing the reference
 * @param targetProperty The property name on the source element to set
 * @param qualifiedName The qualified name to resolve
 * @param localNamespaceId The namespace context for resolution
 * @param isRedefinitionContext Whether this is a redefinition context
 */
data class PendingReference(
    val sourceElementId: String,
    val targetProperty: String,
    val qualifiedName: String,
    val localNamespaceId: String,
    val isRedefinitionContext: Boolean = false
)

/**
 * Collects pending references during parsing.
 * Thread-safe collection of references that will be resolved after parsing.
 */
class ReferenceCollector {
    private val references = mutableListOf<PendingReference>()

    /**
     * Add a pending reference to be resolved later.
     */
    @Synchronized
    fun addReference(reference: PendingReference) {
        references.add(reference)
    }

    /**
     * Add a pending reference with individual parameters.
     */
    @Synchronized
    fun addReference(
        sourceElementId: String,
        targetProperty: String,
        qualifiedName: String,
        localNamespaceId: String,
        isRedefinitionContext: Boolean = false
    ) {
        references.add(PendingReference(
            sourceElementId = sourceElementId,
            targetProperty = targetProperty,
            qualifiedName = qualifiedName,
            localNamespaceId = localNamespaceId,
            isRedefinitionContext = isRedefinitionContext
        ))
    }

    /**
     * Get all collected references.
     */
    @Synchronized
    fun getReferences(): List<PendingReference> = references.toList()

    /**
     * Clear all collected references.
     */
    @Synchronized
    fun clear() {
        references.clear()
    }

    /**
     * Get count of pending references.
     */
    @Synchronized
    fun size(): Int = references.size
}

/**
 * Register a pending reference to be resolved after parsing.
 *
 * @param sourceElement The element containing the reference
 * @param targetProperty The property name to set on the source element
 * @param qualifiedName The qualified name to resolve
 * @param isRedefinitionContext Whether this is a redefinition context
 */
fun ParseContext.registerReference(
    sourceElement: ModelElement,
    targetProperty: String,
    qualifiedName: String,
    isRedefinitionContext: Boolean = false
) {
    val collector = this.referenceCollector ?: return

    // Get the source element ID
    val sourceId = (sourceElement as? Element)?.elementId
        ?: return

    // Get the local namespace ID for resolution context
    val localNamespaceId = (this.parent as? Element)?.elementId
        ?: ""

    collector.addReference(
        sourceElementId = sourceId,
        targetProperty = targetProperty,
        qualifiedName = qualifiedName,
        localNamespaceId = localNamespaceId,
        isRedefinitionContext = isRedefinitionContext
    )
}
