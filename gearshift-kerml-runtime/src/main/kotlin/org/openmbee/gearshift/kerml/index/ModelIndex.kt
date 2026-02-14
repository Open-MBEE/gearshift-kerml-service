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
package org.openmbee.gearshift.kerml.index

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

/**
 * Bidirectional map of element ID to qualified name.
 *
 * Ensures element IDs are stable across reparses by mapping each element's
 * structural identity (qualified name / path) to a persistent UUID. When a
 * model is reparsed, the [IndexReconciler] uses the previous ModelIndex to
 * remap freshly-assigned UUIDs back to their original values for elements
 * whose qualified name has not changed.
 */
class ModelIndex {
    private val idToQn: MutableMap<String, String> = mutableMapOf()
    private val qnToId: MutableMap<String, String> = mutableMapOf()

    fun put(id: String, qualifiedName: String) {
        // Remove any stale reverse mapping for the old QN of this ID
        idToQn[id]?.let { oldQn -> qnToId.remove(oldQn) }
        // Remove any stale forward mapping for the old ID of this QN
        qnToId[qualifiedName]?.let { oldId -> idToQn.remove(oldId) }

        idToQn[id] = qualifiedName
        qnToId[qualifiedName] = id
    }

    fun getQn(id: String): String? = idToQn[id]

    fun getId(qualifiedName: String): String? = qnToId[qualifiedName]

    fun remove(id: String) {
        val qn = idToQn.remove(id)
        if (qn != null) {
            qnToId.remove(qn)
        }
    }

    fun entries(): Set<Map.Entry<String, String>> = idToQn.entries

    fun size(): Int = idToQn.size

    fun toJson(): String = objectMapper.writeValueAsString(idToQn)

    companion object {
        private val objectMapper = jacksonObjectMapper()

        fun fromJson(json: String): ModelIndex {
            val map: Map<String, String> = objectMapper.readValue(json)
            val index = ModelIndex()
            for ((id, qn) in map) {
                index.put(id, qn)
            }
            return index
        }
    }
}
