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

/**
 * A mount represents a read-only engine that can be attached to a session.
 *
 * Mounts are used to share library content across sessions without copying.
 * Libraries (like the KerML Kernel Semantic Library) are loaded once into
 * a dedicated engine, then mounted read-only into user sessions.
 *
 * Resolution order: Local -> Explicit Mounts -> Implicit Mounts
 *
 * Example usage:
 * ```kotlin
 * // Register kernel library as implicit mount (done once at startup)
 * val kernelEngine = loadKernelLibrary()
 * val kernelMount = MountRegistry.register(
 *     id = "kerml-kernel-semantic-library",
 *     name = "KerML Kernel Semantic Library",
 *     engine = kernelEngine,
 *     priority = 1000,
 *     isImplicit = true
 * )
 *
 * // User session auto-mounts implicit libraries
 * val session = Session.createWithMounts(...)
 * // Now session can resolve Base::Anything from the mounted library
 * ```
 */
interface Mount {
    /**
     * Unique identifier for this mount.
     * Convention: derived from qualified names by replacing `::` with `_`
     * Examples: "kerml-kernel-semantic-library", "Base", "Kernel_Semantic_Library_Base_Anything"
     */
    val id: String

    /**
     * Human-readable name for display purposes.
     */
    val name: String

    /**
     * The engine containing the mounted content.
     * This engine is read-only when accessed through the mount.
     */
    val engine: MDMEngine

    /**
     * Priority for resolution ordering. Lower values are searched first.
     * Suggested ranges:
     * - 0-99: Reserved
     * - 100-499: User explicit mounts
     * - 500-999: Reserved
     * - 1000+: Implicit standard libraries
     */
    val priority: Int

    /**
     * Whether this mount is automatically attached to new sessions.
     * Implicit mounts are standard libraries (KerML Kernel Semantic, SysML Standard).
     * Explicit mounts are user-requested project dependencies.
     */
    val isImplicit: Boolean

    /**
     * Check if this mount contains an element with the given ID.
     */
    fun containsElement(elementId: String): Boolean

    /**
     * Get all root namespaces in this mount.
     * These are top-level elements with no owner that are of type Namespace.
     */
    fun getRootNamespaces(): List<MDMObject>
}

/**
 * Standard implementation of the Mount interface.
 */
class StandardMount(
    override val id: String,
    override val name: String,
    override val engine: MDMEngine,
    override val priority: Int = DEFAULT_PRIORITY,
    override val isImplicit: Boolean = false
) : Mount {

    companion object {
        const val DEFAULT_PRIORITY = 100
        const val IMPLICIT_LIBRARY_PRIORITY = 1000
    }

    override fun containsElement(elementId: String): Boolean =
        engine.getElement(elementId) != null

    override fun getRootNamespaces(): List<MDMObject> =
        engine.getRootNamespaces()

    override fun toString(): String =
        "Mount(id='$id', name='$name', implicit=$isImplicit, priority=$priority, elements=${engine.elementCount()})"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Mount) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}
