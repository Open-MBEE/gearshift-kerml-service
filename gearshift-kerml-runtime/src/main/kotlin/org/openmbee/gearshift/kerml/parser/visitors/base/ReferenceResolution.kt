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
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MDMObject
import org.openmbee.gearshift.generated.interfaces.Element
import org.openmbee.gearshift.generated.interfaces.ModelElement
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties

private val logger = KotlinLogging.logger {}

/**
 * Thrown when a reference cannot be resolved during parsing.
 */
class UnresolvedReferenceException(message: String) : RuntimeException(message)

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
        references.add(
            PendingReference(
                sourceElementId = sourceElementId,
                targetProperty = targetProperty,
                qualifiedName = qualifiedName,
                localNamespaceId = localNamespaceId,
                isRedefinitionContext = isRedefinitionContext
            )
        )
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
fun KermlParseContext.registerReference(
    sourceElement: ModelElement,
    targetProperty: String,
    qualifiedName: String,
    isRedefinitionContext: Boolean = false
) {
    val collector = this.referenceCollector ?: return

    // Get the source element ID - use MDM's id (UUID) not KerML's elementId
    // MDM's getInstance() uses the MDM id for lookups
    val sourceId = (sourceElement as? MDMObject)?.id
        ?: return

    // Get the local namespace ID for resolution context
    val localNamespaceId = (this.parent as? MDMObject)?.id
        ?: ""

    collector.addReference(
        sourceElementId = sourceId,
        targetProperty = targetProperty,
        qualifiedName = qualifiedName,
        localNamespaceId = localNamespaceId,
        isRedefinitionContext = isRedefinitionContext
    )
}

/**
 * Resolves collected references after parsing completes.
 * Links source elements to their resolved targets via property setters.
 */
class ReferenceResolver(private val engine: MDMEngine) {

    /**
     * Resolve all pending references in the collector.
     *
     * @param collector The reference collector containing pending references
     * @return Number of successfully resolved references
     */
    fun resolveAll(collector: ReferenceCollector): Int {
        val references = collector.getReferences()
        var resolved = 0

        for (ref in references) {
            if (resolveReference(ref)) {
                resolved++
            }
        }

        logger.debug { "Resolved $resolved of ${references.size} references" }
        collector.clear()
        return resolved
    }

    /**
     * Resolve a single pending reference.
     *
     * @param ref The pending reference to resolve
     * @return true if successfully resolved
     */
    private fun resolveReference(ref: PendingReference): Boolean {
        // Get the source element
        val sourceElement = engine.getInstance(ref.sourceElementId) as? ModelElement
        if (sourceElement == null) {
            logger.warn { "Source element ${ref.sourceElementId} not found for reference to ${ref.qualifiedName}" }
            return false
        }

        // Resolve the qualified name to find the target element
        val targetElement = resolveQualifiedName(ref.qualifiedName, ref.localNamespaceId)
        if (targetElement == null) {
            throw UnresolvedReferenceException(
                "Could not resolve qualified name '${ref.qualifiedName}' for ${ref.targetProperty} on ${sourceElement::class.simpleName}[${ref.sourceElementId}]"
            )
        }

        // Set the property using reflection
        return setProperty(sourceElement, ref.targetProperty, targetElement)
    }

    /**
     * Resolve a qualified name to an element.
     *
     * @param qualifiedName The qualified name (e.g., "Base::Anything" or just "Anything")
     * @param localNamespaceId The namespace context for resolution (for relative names)
     * @return The resolved element or null
     */
    private fun resolveQualifiedName(qualifiedName: String, localNamespaceId: String): ModelElement? {
        val parts = qualifiedName.split("::")
        val simpleName = parts.last()

        // First, try to find by declared name or effective name
        val allElements = engine.getAllElements()

        // Try exact qualified name match first
        if (parts.size > 1) {
            // For qualified names like "Base::Anything", try to match the full path
            for (element in allElements) {
                if (element is Element) {
                    val elementQualifiedName = element.qualifiedName
                    if (elementQualifiedName == qualifiedName) {
                        return element as? ModelElement
                    }
                }
            }
        }

        // Fall back to simple name match
        for (element in allElements) {
            if (element is Element) {
                if (element.declaredName == simpleName || element.name == simpleName) {
                    return element as? ModelElement
                }
            }
        }

        // If localNamespaceId is provided, try to resolve relative to that namespace
        if (localNamespaceId.isNotEmpty()) {
            val localNamespace = engine.getInstance(localNamespaceId) as? Element
            if (localNamespace != null) {
                // Try to find within the local namespace's owned members
                // This is a simplified implementation - full KerML name resolution is more complex
                for (element in allElements) {
                    if (element is Element && element.declaredName == simpleName) {
                        return element as? ModelElement
                    }
                }
            }
        }

        return null
    }

    /**
     * Set a property on an element using reflection.
     *
     * @param element The element to modify
     * @param propertyName The property name to set
     * @param value The value to set
     * @return true if successfully set
     */
    @Suppress("UNCHECKED_CAST")
    private fun setProperty(element: ModelElement, propertyName: String, value: ModelElement): Boolean {
        try {
            // Try Kotlin reflection first (works for var properties)
            val property = element::class.memberProperties
                .filterIsInstance<KMutableProperty1<ModelElement, Any?>>()
                .firstOrNull { it.name == propertyName }

            if (property != null) {
                property.setter.call(element, value)
                logger.debug { "Set ${element::class.simpleName}.$propertyName = ${(value as? Element)?.declaredName ?: value}" }
                return true
            }

            // If reflection found no mutable property, the property is likely derived (val).
            // Derived properties must not be set directly — fix the visitor to set the
            // structural backing instead (e.g., create a Membership for derived navigation).
            if (element is MDMObject) {
                throw IllegalStateException(
                    "Cannot directly set '${propertyName}' on ${element.className}[${element.id}]: " +
                        "property is read-only (likely derived). Fix the visitor to build the " +
                        "structural model so the derivation constraint can compute this value."
                )
            }

            logger.warn { "Property '$propertyName' not found on ${element::class.simpleName}" }
            return false
        } catch (e: Exception) {
            logger.warn { "Failed to set property '$propertyName' on ${element::class.simpleName}: ${e.message}" }
            return false
        }
    }
}
