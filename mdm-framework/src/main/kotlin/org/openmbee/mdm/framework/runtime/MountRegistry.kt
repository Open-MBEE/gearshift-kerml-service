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
 * Global registry of available mounts.
 *
 * The MountRegistry is a singleton that stores pre-loaded engines that can be
 * mounted into user sessions. Standard libraries (like KerML Kernel Semantic)
 * are registered once at application startup and then mounted read-only into
 * each session.
 *
 * Library ID Convention:
 * - IDs are derived from qualified names by replacing `::` with `_`
 * - Examples:
 *   - `Base` -> `Base`
 *   - `KerML::Base` -> `KerML_Base`
 *   - `Kernel Semantic Library::Base::Anything` -> `Kernel_Semantic_Library_Base_Anything`
 *
 * Thread Safety:
 * - Registration operations are synchronized
 * - Read operations return copies to avoid concurrent modification
 *
 * Example usage:
 * ```kotlin
 * // At application startup
 * val kernelEngine = loadKernelLibrary()
 * MountRegistry.register(
 *     id = "kerml-kernel-semantic-library",
 *     name = "KerML Kernel Semantic Library",
 *     engine = kernelEngine,
 *     priority = 1000,
 *     isImplicit = true
 * )
 *
 * // In session creation
 * val implicitMounts = MountRegistry.getImplicitMounts()
 * for (mount in implicitMounts) {
 *     session.mount(mount.id)
 * }
 * ```
 */
object MountRegistry {

    private val mounts: MutableMap<String, Mount> = mutableMapOf()

    /**
     * Register a mount in the global registry.
     *
     * @param id Unique identifier (use qualifiedNameToId for consistency)
     * @param name Human-readable name
     * @param engine The engine containing the mount content
     * @param priority Lower = searched first (default 100 for explicit, 1000+ for implicit)
     * @param isImplicit True for auto-mounted standard libraries
     * @return The created Mount
     * @throws IllegalArgumentException if a mount with this ID is already registered
     */
    @Synchronized
    fun register(
        id: String,
        name: String,
        engine: MDMEngine,
        priority: Int = StandardMount.DEFAULT_PRIORITY,
        isImplicit: Boolean = false
    ): Mount {
        if (mounts.containsKey(id)) {
            throw IllegalArgumentException("Mount with ID '$id' is already registered")
        }

        val mount = StandardMount(id, name, engine, priority, isImplicit)
        mounts[id] = mount
        logger.info { "Registered mount: $mount" }
        return mount
    }

    /**
     * Register a pre-built mount.
     *
     * @param mount The mount to register
     * @return The registered mount
     * @throws IllegalArgumentException if a mount with this ID is already registered
     */
    @Synchronized
    fun register(mount: Mount): Mount {
        if (mounts.containsKey(mount.id)) {
            throw IllegalArgumentException("Mount with ID '${mount.id}' is already registered")
        }

        mounts[mount.id] = mount
        logger.info { "Registered mount: $mount" }
        return mount
    }

    /**
     * Unregister a mount from the registry.
     *
     * Note: This does not affect sessions that have already mounted this.
     *
     * @param id The mount ID to unregister
     * @return The unregistered mount, or null if not found
     */
    @Synchronized
    fun unregister(id: String): Mount? {
        val mount = mounts.remove(id)
        if (mount != null) {
            logger.info { "Unregistered mount: $mount" }
        }
        return mount
    }

    /**
     * Get a mount by ID.
     *
     * @param id The mount ID
     * @return The mount, or null if not found
     */
    fun get(id: String): Mount? = mounts[id]

    /**
     * Check if a mount is registered.
     *
     * @param id The mount ID
     * @return True if registered
     */
    fun isRegistered(id: String): Boolean = mounts.containsKey(id)

    /**
     * Get all implicit mounts (standard libraries that auto-mount).
     * Sorted by priority (lower first).
     *
     * @return List of implicit mounts
     */
    fun getImplicitMounts(): List<Mount> =
        mounts.values
            .filter { it.isImplicit }
            .sortedBy { it.priority }
            .toList()

    /**
     * Get all explicit mounts (user-registered project libraries).
     * Sorted by priority (lower first).
     *
     * @return List of explicit mounts
     */
    fun getExplicitMounts(): List<Mount> =
        mounts.values
            .filter { !it.isImplicit }
            .sortedBy { it.priority }
            .toList()

    /**
     * Get all registered mounts.
     * Sorted by priority (lower first).
     *
     * @return List of all mounts
     */
    fun getAllMounts(): List<Mount> =
        mounts.values.sortedBy { it.priority }.toList()

    /**
     * Convert a qualified name to a mount ID.
     * Replaces `::` with `_` and spaces with `_`.
     *
     * Examples:
     * - `Base` -> `Base`
     * - `KerML::Base` -> `KerML_Base`
     * - `Kernel Semantic Library::Base` -> `Kernel_Semantic_Library_Base`
     *
     * @param qualifiedName The qualified name
     * @return The derived ID
     */
    fun qualifiedNameToId(qualifiedName: String): String =
        qualifiedName
            .replace("::", "_")
            .replace(" ", "_")

    /**
     * Clear all registered mounts.
     * Primarily for testing purposes.
     */
    @Synchronized
    fun clear() {
        mounts.clear()
        logger.info { "Cleared all mounts from registry" }
    }

    /**
     * Get the count of registered mounts.
     */
    fun count(): Int = mounts.size
}
