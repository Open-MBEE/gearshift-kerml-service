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
import java.time.Instant
import java.util.UUID

private val logger = KotlinLogging.logger {}

/**
 * A modeling session containing an engine and optional model.
 *
 * Session is a container for working with models. It:
 * - Owns an MDMEngine instance
 * - Optionally owns an MDMModel for project-level operations
 * - Provides convenience delegates to the engine
 * - Tracks session metadata (id, name, created time)
 * - Supports mounting read-only libraries (when using MountableEngine)
 *
 * Sessions can be created directly or via a SessionManager.
 *
 * For sessions with mount support, use [createWithMounts] which creates
 * a MountableEngine and auto-mounts implicit libraries.
 */
open class Session(
    /** Unique identifier for this session */
    val id: String = UUID.randomUUID().toString(),

    /** Human-readable name for the session */
    val name: String = "Untitled Session",

    /** The engine (model container) */
    val engine: MDMEngine,

    /** When this session was created */
    val createdAt: Instant = Instant.now()
) {
    /**
     * Optional model associated with this session.
     * Set by language-specific session subclasses.
     */
    open val model: MDMModel? = null

    // ===== Convenience Delegates to Engine =====

    fun getElement(id: String) = engine.getElement(id)
    fun getAllElements() = engine.getAllElements()
    fun getElementsByClass(className: String) = engine.getElementsByClass(className)
    fun createElement(className: String) = engine.createElement(className)

    // ===== Mount Management =====

    /**
     * Mount a library by ID.
     * Only works if this session uses a MountableEngine.
     *
     * @param mountId The mount ID (must be registered in MountRegistry)
     * @throws IllegalStateException if engine is not a MountableEngine
     * @throws IllegalArgumentException if mount is not registered
     */
    fun mount(mountId: String) {
        val mountable = engine as? MountableEngine
            ?: throw IllegalStateException("Session does not use a MountableEngine - use createWithMounts()")
        mountable.mount(mountId)
    }

    /**
     * Unmount a library.
     * Only works if this session uses a MountableEngine.
     *
     * @param mountId The mount ID to unmount
     * @return true if the mount was removed, false if not mounted
     * @throws IllegalStateException if engine is not a MountableEngine
     */
    fun unmount(mountId: String): Boolean {
        val mountable = engine as? MountableEngine
            ?: throw IllegalStateException("Session does not use a MountableEngine - use createWithMounts()")
        return mountable.unmount(mountId)
    }

    /**
     * Get list of active mount IDs.
     * Returns empty list if engine is not a MountableEngine.
     */
    fun getActiveMounts(): List<String> {
        val mountable = engine as? MountableEngine ?: return emptyList()
        return mountable.getActiveMountIds()
    }

    /**
     * Check if this session supports mounts.
     */
    fun supportsMounts(): Boolean = engine is MountableEngine

    // ===== API/Statistics Methods =====

    /**
     * Get session statistics.
     */
    open fun getStatistics(): Map<String, Any> {
        val stats = mutableMapOf<String, Any>(
            "sessionId" to id,
            "sessionName" to name,
            "elementCount" to engine.elementCount(),
            "createdAt" to createdAt.toString()
        )

        // Add mount info if applicable
        val mountable = engine as? MountableEngine
        if (mountable != null) {
            stats["activeMounts"] = mountable.getActiveMountIds()
            stats["localElementCount"] = mountable.getLocalElements().size
        }

        return stats
    }

    /**
     * Clear all model content and reset the session.
     * Does not unmount libraries - use clearAll() for that.
     */
    open fun reset() {
        engine.clear()
        logger.info { "Session '$name' reset" }
    }

    /**
     * Clear all content including unmounting all libraries.
     * Only applicable to sessions with MountableEngine.
     */
    open fun clearAll() {
        val mountable = engine as? MountableEngine
        if (mountable != null) {
            mountable.clearAll()
        } else {
            engine.clear()
        }
        logger.info { "Session '$name' fully cleared" }
    }

    companion object {
        /**
         * Create a new session with the given parameters.
         */
        fun create(
            name: String,
            schema: MetamodelRegistry,
            factory: ElementFactory = DefaultElementFactory()
        ): Session {
            val engine = MDMEngine(schema, factory)
            return Session(
                name = name,
                engine = engine
            )
        }

        /**
         * Create a new session with mount support.
         * Uses a MountableEngine and auto-mounts implicit libraries.
         *
         * @param name Human-readable session name
         * @param schema The metamodel registry
         * @param factory Element factory (optional)
         * @return Session with MountableEngine that has implicit mounts active
         */
        fun createWithMounts(
            name: String,
            schema: MetamodelRegistry,
            factory: ElementFactory = DefaultElementFactory()
        ): Session {
            val engine = MountableEngine(schema, factory)
            engine.mountImplicit()
            return Session(
                name = name,
                engine = engine
            )
        }
    }
}

/**
 * Result of parsing source text.
 */
data class ParseResult(
    val success: Boolean,
    val rootElementId: String? = null,
    val errors: List<String> = emptyList()
)
