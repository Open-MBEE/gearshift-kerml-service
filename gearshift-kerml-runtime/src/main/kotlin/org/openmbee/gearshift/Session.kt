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
package org.openmbee.gearshift

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.gearshift.framework.runtime.MDMEngine
import org.openmbee.gearshift.framework.runtime.MetamodelRegistry
import java.time.Instant
import java.util.UUID

private val logger = KotlinLogging.logger {}

/**
 * A modeling session containing a model and language-specific infrastructure.
 *
 * Session is the primary entry point for working with models. It:
 * - Owns an MDMModel instance
 * - Provides language-specific parsing and operations
 * - Manages lifecycle handlers for the language
 *
 * Sessions are created via SessionManager and can be serialized/restored.
 */
class Session private constructor(
    /** Unique identifier for this session */
    val id: String,

    /** Human-readable name for the session */
    val name: String,

    /** The engine (model container) */
    val engine: MDMEngine,

    /** The modeling language for this session */
    val language: ModelLanguage,

    /** When this session was created */
    val createdAt: Instant,
) {
    // TODO: Add language-specific parsers
    // private val kermlParser = KerMLParser()

    /**
     * Parse source text into the model.
     */
    fun parse(source: String): ParseResult {
        logger.debug { "Parsing source in session $name" }
        // TODO: Delegate to appropriate parser based on language
        return ParseResult(success = false, errors = listOf("Parser not yet implemented"))
    }

    /**
     * Load a library into the model.
     */
    fun loadLibrary(libraryName: String): Boolean {
        logger.debug { "Loading library $libraryName in session $name" }
        // TODO: Implement library loading
        return false
    }

    // ===== Convenience Delegates to Engine =====

    fun getElement(id: String) = engine.getElement(id)
    fun getAllElements() = engine.getAllElements()
    fun getElementsByClass(className: String) = engine.getElementsByClass(className)
    fun createElement(className: String) = engine.createElement(className)

    // ===== API/Statistics Methods =====

    /**
     * Get session statistics.
     */
    fun getStatistics(): Map<String, Any> = mapOf(
        "sessionId" to id,
        "sessionName" to name,
        "language" to language.name,
        "elementCount" to engine.elementCount(),
        "createdAt" to createdAt.toString()
    )

    /**
     * Clear all model content and reset the session.
     */
    fun reset() {
        engine.clear()
        logger.info { "Session '$name' reset" }
    }

    companion object {
        /**
         * Create a new session with the given name and language.
         */
        fun create(name: String, language: ModelLanguage, schema: MetamodelRegistry): Session {
            val engine = MDMEngine(schema)

            // Register language-specific lifecycle handlers
            // TODO: Register handlers based on language

            return Session(
                id = UUID.randomUUID().toString(),
                name = name,
                engine = engine,
                language = language,
                createdAt = Instant.now()
            )
        }
    }
}

/**
 * Supported modeling languages.
 */
enum class ModelLanguage {
    /** Kernel Modeling Language */
    KERML,

    /** Systems Modeling Language v2 */
    SYSML
}

/**
 * Result of parsing source text.
 */
data class ParseResult(
    val success: Boolean,
    val rootElementId: String? = null,
    val errors: List<String> = emptyList()
)
