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
import org.openmbee.mdm.framework.meta.MetaAssociation
import org.openmbee.mdm.framework.meta.MetaAssociationEnd

private val logger = KotlinLogging.logger {}

/**
 * Controls which properties are included during serialization.
 */
enum class SerializationMode {
    /** Include all properties including derived attributes (OCL evaluation). */
    FULL,
    /**
     * Skip expensive derived attributes (e.g. qualifiedName) to avoid costly OCL evaluation.
     * Critical display properties (name, shortName) are still included.
     * Still includes all stored properties and association ends.
     */
    SUMMARY;

    companion object {
        /**
         * Derived properties that are always included even in SUMMARY mode,
         * because they are essential for element display.
         */
        val SUMMARY_INCLUDED_DERIVED = setOf("name", "shortName")
    }
}

/**
 * Serializes MDMObject instances to the @id/@type JSON-LD format
 * defined by the SysML v2 API (ptc-25-04-21).
 *
 * Output format:
 * ```json
 * {
 *   "@id": "uuid",
 *   "@type": "Class",
 *   "declaredName": "Vehicle",
 *   "ownedMembership": [{"@id": "uuid"}, ...]
 * }
 * ```
 *
 * Association ends are serialized as `{"@id": "uuid"}` references (Identified pattern).
 */
class ElementSerializer(
    private val engine: MDMEngine
) {
    private val registry = engine.schema

    // Per-class association end cache — avoids re-scanning all associations for every element of the same type
    private val assocEndCache = HashMap<String, List<Triple<MetaAssociation, MetaAssociationEnd, Boolean>>>()

    /**
     * Serialize an MDMObject to a JSON-LD compatible map.
     */
    fun serialize(element: MDMObject, mode: SerializationMode = SerializationMode.FULL): Map<String, Any?> {
        val result = linkedMapOf<String, Any?>()
        val elementId = element.id ?: return result

        result["@id"] = elementId
        result["@type"] = element.className

        val metaClass = element.metaClass
        val allClassNames = registry.getAllSuperclasses(metaClass.name) + metaClass.name

        // Serialize primitive attributes
        for (className in allClassNames) {
            val cls = registry.getClass(className) ?: continue
            for (prop in cls.attributes) {
                if (result.containsKey(prop.name)) continue
                // In SUMMARY mode, skip expensive derived attributes but keep critical display ones
                if (mode == SerializationMode.SUMMARY && prop.isDerived
                    && prop.name !in SerializationMode.SUMMARY_INCLUDED_DERIVED) continue
                try {
                    val value = engine.getProperty(element, prop.name)
                    if (value != null) {
                        result[prop.name] = serializePrimitive(value)
                    }
                } catch (e: Exception) {
                    logger.debug { "Skipping property ${prop.name} on ${element.className}: ${e.message}" }
                }
            }
        }

        // Serialize association ends as @id references — use cached per-class list
        val applicableEnds = getApplicableAssociationEnds(metaClass.name, allClassNames)
        for ((association, end, isTargetEnd) in applicableEnds) {
            val endName = end.name
            if (result.containsKey(endName)) continue
            // In SUMMARY mode, skip derived association ends
            if (mode == SerializationMode.SUMMARY && end.isDerived) continue
            // Skip non-navigable source ends
            if (!isTargetEnd && !association.sourceEnd.isNavigable) continue
            try {
                val value = engine.getProperty(element, endName)
                if (value != null) {
                    result[endName] = serializeReference(value)
                }
            } catch (e: Exception) {
                logger.debug { "Skipping association end $endName: ${e.message}" }
            }
        }

        return result
    }

    /**
     * Get applicable association ends for a class, using pre-computed index or local cache.
     */
    private fun getApplicableAssociationEnds(
        className: String,
        allClassNames: Set<String>
    ): List<Triple<MetaAssociation, MetaAssociationEnd, Boolean>> {
        // Try pre-computed registry index first
        registry.getAssociationEndsForClass(className)?.let { return it }

        // Fall back to local cache
        return assocEndCache.getOrPut(className) {
            val ends = mutableListOf<Triple<MetaAssociation, MetaAssociationEnd, Boolean>>()
            for (association in registry.getAllAssociations()) {
                if (allClassNames.contains(association.sourceEnd.type)) {
                    ends.add(Triple(association, association.targetEnd, true))
                }
                if (allClassNames.contains(association.targetEnd.type)) {
                    ends.add(Triple(association, association.sourceEnd, false))
                }
            }
            ends
        }
    }

    /**
     * Serialize multiple elements.
     */
    fun serializeAll(
        elements: List<MDMObject>,
        mode: SerializationMode = SerializationMode.FULL
    ): List<Map<String, Any?>> {
        return elements.map { serialize(it, mode) }
    }

    private fun serializePrimitive(value: Any?): Any? {
        return when (value) {
            null -> null
            is String, is Boolean, is Number -> value
            is MDMObject -> mapOf("@id" to value.id)
            is List<*> -> value.map { serializePrimitive(it) }
            else -> value.toString()
        }
    }

    private fun serializeReference(value: Any?): Any? {
        return when (value) {
            null -> null
            is MDMObject -> mapOf("@id" to value.id)
            is List<*> -> value.mapNotNull { item ->
                when (item) {
                    is MDMObject -> mapOf("@id" to item.id)
                    else -> null
                }
            }
            else -> null
        }
    }
}
