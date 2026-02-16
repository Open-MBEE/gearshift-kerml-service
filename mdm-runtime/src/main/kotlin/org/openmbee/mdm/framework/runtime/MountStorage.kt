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
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant

private val logger = KotlinLogging.logger {}

/**
 * Metadata about a stored mount.
 */
data class MountMetadata(
    val id: String,
    val name: String,
    val isImplicit: Boolean,
    val priority: Int,
    val elementCount: Int,
    val savedAt: Instant
)

/**
 * Interface for persisting and loading mount engines.
 *
 * MountStorage allows pre-loading standard libraries at build time and
 * fast startup by loading serialized engines instead of re-parsing files.
 *
 * Default implementation uses a simple JSON format for metadata with
 * the engine content serialized separately.
 */
interface MountStorage {
    /**
     * Save a mount to storage.
     *
     * @param mount The mount to save
     * @param path The path to save to (directory for multi-file, file for single)
     */
    fun save(mount: Mount, path: Path)

    /**
     * Load a mount from storage.
     *
     * @param path The path to load from
     * @param schema The schema to use for the loaded engine
     * @return The loaded mount, or null if not found/invalid
     */
    fun load(path: Path, schema: MetamodelRegistry): Mount?

    /**
     * List available mounts in a directory.
     *
     * @param directory The directory to scan
     * @return List of metadata for available mounts
     */
    fun listAvailable(directory: Path): List<MountMetadata>
}

/**
 * Simple JSON-based mount storage implementation.
 *
 * Stores mounts as directories containing:
 * - metadata.json: Mount metadata (id, name, priority, etc.)
 * - elements.json: Serialized elements
 * - links.json: Serialized links
 *
 * This is a placeholder implementation - full GQL support can be added later.
 */
class JsonMountStorage : MountStorage {

    override fun save(mount: Mount, path: Path) {
        logger.info { "Saving mount '${mount.id}' to $path" }

        // Create directory if needed
        Files.createDirectories(path)

        // Save metadata
        val metadata = MountMetadata(
            id = mount.id,
            name = mount.name,
            isImplicit = mount.isImplicit,
            priority = mount.priority,
            elementCount = mount.engine.elementCount(),
            savedAt = Instant.now()
        )
        val metadataPath = path.resolve("metadata.json")
        Files.writeString(metadataPath, serializeMetadata(metadata))

        // Save elements (simplified JSON serialization)
        val elementsPath = path.resolve("elements.json")
        Files.writeString(elementsPath, serializeElements(mount.engine))

        // Save links
        val linksPath = path.resolve("links.json")
        Files.writeString(linksPath, serializeLinks(mount.engine))

        logger.info { "Saved mount '${mount.id}' with ${metadata.elementCount} elements" }
    }

    override fun load(path: Path, schema: MetamodelRegistry): Mount? {
        if (!Files.isDirectory(path)) {
            logger.warn { "Mount path is not a directory: $path" }
            return null
        }

        val metadataPath = path.resolve("metadata.json")
        if (!Files.exists(metadataPath)) {
            logger.warn { "Mount metadata not found: $metadataPath" }
            return null
        }

        try {
            // Load metadata
            val metadataJson = Files.readString(metadataPath)
            val metadata = deserializeMetadata(metadataJson)

            // Create engine with the schema
            val engine = MDMEngine(schema)

            // Load elements
            val elementsPath = path.resolve("elements.json")
            if (Files.exists(elementsPath)) {
                val elementsJson = Files.readString(elementsPath)
                deserializeElements(elementsJson, engine)
            }

            // Load links
            val linksPath = path.resolve("links.json")
            if (Files.exists(linksPath)) {
                val linksJson = Files.readString(linksPath)
                deserializeLinks(linksJson, engine)
            }

            val mount = StandardMount(
                id = metadata.id,
                name = metadata.name,
                engine = engine,
                priority = metadata.priority,
                isImplicit = metadata.isImplicit
            )

            logger.info { "Loaded mount '${mount.id}' with ${engine.elementCount()} elements" }
            return mount
        } catch (e: Exception) {
            logger.error(e) { "Failed to load mount from $path" }
            return null
        }
    }

    override fun listAvailable(directory: Path): List<MountMetadata> {
        if (!Files.isDirectory(directory)) {
            return emptyList()
        }

        return Files.list(directory)
            .filter { Files.isDirectory(it) }
            .filter { Files.exists(it.resolve("metadata.json")) }
            .map { path ->
                try {
                    val metadataJson = Files.readString(path.resolve("metadata.json"))
                    deserializeMetadata(metadataJson)
                } catch (e: Exception) {
                    logger.warn { "Failed to read metadata from $path: ${e.message}" }
                    null
                }
            }
            .filter { it != null }
            .map { it!! }
            .toList()
    }

    private fun serializeMetadata(metadata: MountMetadata): String {
        return """
            {
                "id": "${escapeJson(metadata.id)}",
                "name": "${escapeJson(metadata.name)}",
                "isImplicit": ${metadata.isImplicit},
                "priority": ${metadata.priority},
                "elementCount": ${metadata.elementCount},
                "savedAt": "${metadata.savedAt}"
            }
        """.trimIndent()
    }

    private fun deserializeMetadata(json: String): MountMetadata {
        // Simple JSON parsing without external dependencies
        val id = extractJsonString(json, "id")
        val name = extractJsonString(json, "name")
        val isImplicit = extractJsonBoolean(json, "isImplicit")
        val priority = extractJsonInt(json, "priority")
        val elementCount = extractJsonInt(json, "elementCount")
        val savedAt = Instant.parse(extractJsonString(json, "savedAt"))

        return MountMetadata(id, name, isImplicit, priority, elementCount, savedAt)
    }

    private fun serializeElements(engine: MDMEngine): String {
        val sb = StringBuilder()
        sb.append("[\n")
        val elements = engine.getAllElements()
        elements.forEachIndexed { index, element ->
            sb.append("  {\n")
            sb.append("    \"id\": \"${escapeJson(element.id ?: "")}\",\n")
            sb.append("    \"className\": \"${escapeJson(element.className)}\",\n")
            sb.append("    \"properties\": {\n")

            // Serialize stored properties
            val metaClass = element.metaClass
            val props = mutableListOf<String>()
            for (attr in metaClass.attributes.filter { !it.isDerived }) {
                val value = element.getProperty(attr.name)
                if (value != null) {
                    val jsonValue = when (value) {
                        is String -> "\"${escapeJson(value)}\""
                        is Boolean, is Number -> value.toString()
                        else -> "\"${escapeJson(value.toString())}\""
                    }
                    props.add("      \"${attr.name}\": $jsonValue")
                }
            }
            sb.append(props.joinToString(",\n"))
            sb.append("\n    }\n")
            sb.append("  }")
            if (index < elements.size - 1) sb.append(",")
            sb.append("\n")
        }
        sb.append("]")
        return sb.toString()
    }

    private fun deserializeElements(json: String, engine: MDMEngine) {
        // Simplified element deserialization
        // This is a basic implementation - a full implementation would use a proper JSON parser
        val elementPattern = Regex(""""id":\s*"([^"]+)".*?"className":\s*"([^"]+)"""", RegexOption.DOT_MATCHES_ALL)
        val matches = elementPattern.findAll(json)

        for (match in matches) {
            val id = match.groupValues[1]
            val className = match.groupValues[2]
            try {
                engine.createInstance(className, id)
            } catch (e: Exception) {
                logger.warn { "Failed to create element $className with id $id: ${e.message}" }
            }
        }
    }

    private fun serializeLinks(engine: MDMEngine): String {
        val sb = StringBuilder()
        sb.append("[\n")
        val allLinks = mutableListOf<MDMLink>()
        for (element in engine.getAllElements()) {
            allLinks.addAll(engine.getLinks(element.id!!))
        }
        val uniqueLinks = allLinks.distinctBy { it.id }

        uniqueLinks.forEachIndexed { index, link ->
            sb.append("  {\n")
            sb.append("    \"id\": \"${escapeJson(link.id)}\",\n")
            sb.append("    \"associationName\": \"${escapeJson(link.associationName)}\",\n")
            sb.append("    \"sourceId\": \"${escapeJson(link.sourceId)}\",\n")
            sb.append("    \"targetId\": \"${escapeJson(link.targetId)}\"\n")
            sb.append("  }")
            if (index < uniqueLinks.size - 1) sb.append(",")
            sb.append("\n")
        }
        sb.append("]")
        return sb.toString()
    }

    private fun deserializeLinks(json: String, engine: MDMEngine) {
        // Simplified link deserialization
        val linkPattern = Regex(
            """"associationName":\s*"([^"]+)".*?"sourceId":\s*"([^"]+)".*?"targetId":\s*"([^"]+)"""",
            RegexOption.DOT_MATCHES_ALL
        )
        val matches = linkPattern.findAll(json)

        for (match in matches) {
            val associationName = match.groupValues[1]
            val sourceId = match.groupValues[2]
            val targetId = match.groupValues[3]
            try {
                engine.link(sourceId, targetId, associationName)
            } catch (e: Exception) {
                logger.warn { "Failed to create link $associationName from $sourceId to $targetId: ${e.message}" }
            }
        }
    }

    private fun escapeJson(s: String): String =
        s.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")

    private fun extractJsonString(json: String, key: String): String {
        val pattern = Regex(""""$key":\s*"([^"\\]*(?:\\.[^"\\]*)*)"""")
        val match = pattern.find(json)
        return match?.groupValues?.get(1)?.replace("\\\"", "\"")?.replace("\\n", "\n") ?: ""
    }

    private fun extractJsonBoolean(json: String, key: String): Boolean {
        val pattern = Regex(""""$key":\s*(true|false)""")
        val match = pattern.find(json)
        return match?.groupValues?.get(1) == "true"
    }

    private fun extractJsonInt(json: String, key: String): Int {
        val pattern = Regex(""""$key":\s*(\d+)""")
        val match = pattern.find(json)
        return match?.groupValues?.get(1)?.toIntOrNull() ?: 0
    }
}
