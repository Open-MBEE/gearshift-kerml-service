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
import org.openmbee.gearshift.GearshiftEngine
import org.openmbee.gearshift.engine.NameResolver
import org.openmbee.gearshift.generated.Wrappers
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
 * Resolves all pending references after parsing completes.
 *
 * Uses the NameResolver to resolve qualified names and sets the resolved
 * elements on the source elements' properties.
 */
class ReferenceResolver(
    private val engine: GearshiftEngine
) {
    /**
     * Result of reference resolution.
     */
    data class ResolutionReport(
        val totalReferences: Int,
        val resolvedCount: Int,
        val failedCount: Int,
        val failures: List<ResolutionFailure>
    )

    data class ResolutionFailure(
        val reference: PendingReference,
        val reason: String
    )

    /**
     * Resolve all pending references collected during parsing.
     *
     * @param collector The ReferenceCollector containing pending references
     * @return ResolutionReport with results
     */
    fun resolveAll(collector: ReferenceCollector): ResolutionReport {
        val references = collector.getReferences()
        val failures = mutableListOf<ResolutionFailure>()
        var resolvedCount = 0

        for (ref in references) {
            try {
                if (resolveReference(ref)) {
                    resolvedCount++
                } else {
                    failures.add(ResolutionFailure(ref, "Name not found: ${ref.qualifiedName}"))
                }
            } catch (e: Exception) {
                failures.add(ResolutionFailure(ref, "Error: ${e.message}"))
                logger.warn(e) { "Failed to resolve reference: ${ref.qualifiedName}" }
            }
        }

        val report = ResolutionReport(
            totalReferences = references.size,
            resolvedCount = resolvedCount,
            failedCount = failures.size,
            failures = failures
        )

        logger.info { "Reference resolution complete: ${report.resolvedCount}/${report.totalReferences} resolved" }
        if (failures.isNotEmpty()) {
            logger.warn { "Failed to resolve ${failures.size} references" }
        }

        return report
    }

    /**
     * Resolve a single pending reference.
     *
     * @return true if resolved successfully, false otherwise
     */
    private fun resolveReference(ref: PendingReference): Boolean {
        // Get the source element
        val sourceObj = engine.getInstance(ref.sourceElementId)
        if (sourceObj == null) {
            logger.warn { "Source element not found: ${ref.sourceElementId}" }
            return false
        }

        // Resolve the qualified name
        val result = engine.resolveName(
            ref.qualifiedName,
            ref.localNamespaceId,
            ref.isRedefinitionContext
        )

        if (result == null) {
            logger.debug { "Could not resolve: ${ref.qualifiedName} in namespace ${ref.localNamespaceId}" }
            return false
        }

        // Set the property on the source element
        setProperty(sourceObj, ref.targetProperty, result.memberElement)

        logger.debug { "Resolved ${ref.qualifiedName} -> ${result.memberElement.id}" }
        return true
    }

    /**
     * Set a property on a source element to reference the resolved element.
     */
    private fun setProperty(
        sourceObj: org.openmbee.gearshift.engine.MDMObject,
        propertyName: String,
        resolvedElement: org.openmbee.gearshift.engine.MDMObject
    ) {
        // Use the engine to set the property/link
        engine.mdmEngine.setProperty(sourceObj, propertyName, resolvedElement.id)
    }
}

/**
 * Extension functions for ParseContext to support reference collection.
 */

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
        ?: return

    collector.addReference(
        sourceElementId = sourceId,
        targetProperty = targetProperty,
        qualifiedName = qualifiedName,
        localNamespaceId = localNamespaceId,
        isRedefinitionContext = isRedefinitionContext
    )
}
