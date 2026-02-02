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
 * Exception thrown when attempting to modify a mounted (read-only) element.
 */
class MountedElementReadOnlyException(
    val elementId: String,
    val mountId: String,
    operation: String
) : RuntimeException("Cannot $operation mounted element '$elementId' from mount '$mountId' - mounted content is read-only")

/**
 * An MDMEngine with mount support for sharing library content across sessions.
 *
 * MountableEngine extends MDMEngine to support read-only mounts. Libraries
 * (like KerML Kernel Semantic) can be loaded once and mounted into multiple
 * sessions without copying.
 *
 * Resolution order for element access:
 * 1. Local elements (this engine's own elements)
 * 2. Explicit mounts (user-requested, searched by priority)
 * 3. Implicit mounts (standard libraries, searched by priority)
 *
 * Immutability enforcement:
 * - Mounted elements cannot be modified (setPropertyValue throws)
 * - Links cannot be created from mounted elements
 * - Mounted elements cannot be deleted
 *
 * Example usage:
 * ```kotlin
 * val engine = MountableEngine(schema, factory)
 * engine.mountImplicit()  // Auto-mount standard libraries
 *
 * // Now can resolve library types
 * val anything = engine.getElement("base-anything-id")
 *
 * // Local operations work normally
 * val myClass = engine.createElement("Class")
 *
 * // But modifying mounted content throws
 * engine.setPropertyValue(anything, "name", "foo")  // Throws!
 * ```
 */
class MountableEngine(
    schema: MetamodelRegistry,
    factory: ElementFactory = DefaultElementFactory()
) : MDMEngine(schema, factory) {

    /**
     * Currently active mounts, sorted by priority.
     * Lower priority values are searched first.
     */
    private val activeMounts: MutableList<Mount> = mutableListOf()

    /**
     * Cache mapping element IDs to their source mount.
     * Null value means the element is local.
     */
    private val elementMountCache: MutableMap<String, Mount?> = mutableMapOf()

    // ===== Mount Management =====

    /**
     * Mount all implicit mounts from the registry.
     * Call this when creating a new session to auto-mount standard libraries.
     */
    fun mountImplicit() {
        val implicitMounts = MountRegistry.getImplicitMounts()
        for (mount in implicitMounts) {
            if (!isMounted(mount.id)) {
                mountInternal(mount)
            }
        }
        logger.info { "Mounted ${implicitMounts.size} implicit mounts" }
    }

    /**
     * Mount a library by ID.
     *
     * @param mountId The mount ID (must be registered in MountRegistry)
     * @throws IllegalArgumentException if mount is not registered
     */
    fun mount(mountId: String) {
        val mount = MountRegistry.get(mountId)
            ?: throw IllegalArgumentException("Mount '$mountId' is not registered in MountRegistry")

        if (isMounted(mountId)) {
            logger.debug { "Mount '$mountId' is already mounted" }
            return
        }

        mountInternal(mount)
    }

    /**
     * Unmount a library.
     *
     * @param mountId The mount ID to unmount
     * @return true if the mount was removed, false if not mounted
     */
    fun unmount(mountId: String): Boolean {
        val mount = activeMounts.find { it.id == mountId }
        if (mount != null) {
            activeMounts.remove(mount)
            // Clear cache entries for this mount
            elementMountCache.entries.removeIf { it.value?.id == mountId }
            logger.info { "Unmounted: ${mount.id}" }
            return true
        }
        return false
    }

    /**
     * Check if a mount is currently active.
     */
    fun isMounted(mountId: String): Boolean =
        activeMounts.any { it.id == mountId }

    /**
     * Check if an element is from a mount (read-only).
     */
    fun isMountedElement(elementId: String): Boolean =
        findMountForElement(elementId) != null

    /**
     * Get the mount that contains an element, or null if local.
     */
    fun getMountForElement(elementId: String): Mount? =
        findMountForElement(elementId)

    /**
     * Get list of active mount IDs.
     */
    fun getActiveMountIds(): List<String> =
        activeMounts.map { it.id }

    /**
     * Get list of active mounts.
     */
    fun getActiveMounts(): List<Mount> =
        activeMounts.toList()

    // ===== Overridden Element Access =====

    /**
     * Get an element by ID, checking local first then mounts.
     */
    override fun getElement(id: String): MDMObject? {
        // Check local first
        val local = super.getElement(id)
        if (local != null) {
            elementMountCache[id] = null  // Mark as local
            return local
        }

        // Check mounts in priority order
        for (mount in activeMounts) {
            val element = mount.engine.getElement(id)
            if (element != null) {
                elementMountCache[id] = mount
                return element
            }
        }

        return null
    }

    /**
     * Get all elements including mounted content.
     */
    override fun getAllElements(): List<MDMObject> {
        val all = mutableListOf<MDMObject>()
        all.addAll(super.getAllElements())
        for (mount in activeMounts) {
            all.addAll(mount.engine.getAllElements())
        }
        return all
    }

    /**
     * Get only local elements (excluding mounted content).
     */
    fun getLocalElements(): List<MDMObject> =
        super.getAllElements()

    /**
     * Get all root namespaces including mounted ones.
     */
    override fun getRootNamespaces(): List<MDMObject> {
        val roots = mutableListOf<MDMObject>()
        roots.addAll(super.getRootNamespaces())
        for (mount in activeMounts) {
            roots.addAll(mount.getRootNamespaces())
        }
        return roots
    }

    /**
     * Get elements by class including mounted content.
     */
    override fun getElementsByClass(className: String): List<MDMObject> {
        val all = mutableListOf<MDMObject>()
        all.addAll(super.getElementsByClass(className))
        for (mount in activeMounts) {
            all.addAll(mount.engine.getElementsByClass(className))
        }
        return all
    }

    // ===== Immutability Enforcement =====

    /**
     * Set a property value, with immutability check for mounted elements.
     */
    override fun setPropertyValue(element: MDMObject, propertyName: String, value: Any?) {
        val elementId = element.id ?: return
        val mount = findMountForElement(elementId)
        if (mount != null) {
            throw MountedElementReadOnlyException(elementId, mount.id, "set property '$propertyName' on")
        }
        super.setPropertyValue(element, propertyName, value)
    }

    /**
     * Create a link, with immutability check for mounted source elements.
     */
    override fun link(sourceId: String, targetId: String, associationName: String) {
        val sourceMount = findMountForElement(sourceId)
        if (sourceMount != null) {
            throw MountedElementReadOnlyException(sourceId, sourceMount.id, "create link from")
        }
        super.link(sourceId, targetId, associationName)
    }

    /**
     * Remove a link, with immutability check.
     */
    override fun unlink(sourceId: String, targetId: String, associationName: String) {
        val sourceMount = findMountForElement(sourceId)
        if (sourceMount != null) {
            throw MountedElementReadOnlyException(sourceId, sourceMount.id, "remove link from")
        }
        super.unlink(sourceId, targetId, associationName)
    }

    /**
     * Remove an element, with immutability check for mounted elements.
     */
    override fun removeElement(id: String): Boolean {
        val mount = findMountForElement(id)
        if (mount != null) {
            throw MountedElementReadOnlyException(id, mount.id, "delete")
        }
        return super.removeElement(id)
    }

    // ===== Navigation with Mounts =====

    /**
     * Navigate an association, including across mount boundaries.
     *
     * When navigating from a local element to a mounted element (or vice versa),
     * the navigation transparently crosses the boundary.
     */
    override fun navigateAssociation(element: MDMElement, propertyName: String): List<MDMObject> {
        val elementId = element.id ?: return emptyList()

        // Check if element is local
        if (super.getElement(elementId) != null) {
            return super.navigateAssociation(element, propertyName)
        }

        // Element is in a mount - delegate to that mount's engine
        val mount = findMountForElement(elementId)
        if (mount != null) {
            return mount.engine.navigateAssociation(element, propertyName)
        }

        return emptyList()
    }

    /**
     * Get linked targets, including from mounts.
     */
    override fun getLinkedTargets(associationName: String, sourceId: String): List<MDMObject> {
        // Check local first
        if (super.getElement(sourceId) != null) {
            return super.getLinkedTargets(associationName, sourceId)
        }

        // Check mounts
        val mount = findMountForElement(sourceId)
        if (mount != null) {
            return mount.engine.getLinkedTargets(associationName, sourceId)
        }

        return emptyList()
    }

    /**
     * Get linked sources, including from mounts.
     */
    override fun getLinkedSources(associationName: String, targetId: String): List<MDMObject> {
        // Check local first
        if (super.getElement(targetId) != null) {
            return super.getLinkedSources(associationName, targetId)
        }

        // Check mounts
        val mount = findMountForElement(targetId)
        if (mount != null) {
            return mount.engine.getLinkedSources(associationName, targetId)
        }

        return emptyList()
    }

    // ===== Internal Helpers =====

    private fun mountInternal(mount: Mount) {
        activeMounts.add(mount)
        activeMounts.sortBy { it.priority }
        logger.info { "Mounted: ${mount.id} (${mount.engine.elementCount()} elements, priority=${mount.priority})" }
    }

    private fun findMountForElement(elementId: String): Mount? {
        // Check cache first
        if (elementMountCache.containsKey(elementId)) {
            return elementMountCache[elementId]
        }

        // Check if local
        if (super.getElement(elementId) != null) {
            elementMountCache[elementId] = null
            return null
        }

        // Search mounts
        for (mount in activeMounts) {
            if (mount.containsElement(elementId)) {
                elementMountCache[elementId] = mount
                return mount
            }
        }

        return null
    }

    /**
     * Clear local content but keep mounts.
     */
    override fun clear() {
        super.clear()
        // Clear only local entries from cache
        elementMountCache.entries.removeIf { it.value == null }
    }

    /**
     * Clear everything including unmounting all mounts.
     */
    fun clearAll() {
        super.clear()
        activeMounts.clear()
        elementMountCache.clear()
    }
}
