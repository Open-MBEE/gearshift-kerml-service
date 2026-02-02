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
package org.openmbee.mdm.framework.meta

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.inputStream
import kotlin.io.path.outputStream

/**
 * Utilities for loading and saving metamodel definitions from/to JSON using Jackson.
 * Allows you to define your metamodel in JSON files for a declarative approach.
 */
object MetamodelLoader {

    val mapper: ObjectMapper = createObjectMapper()

    private fun createObjectMapper(): ObjectMapper {
        return ObjectMapper().apply {
            registerKotlinModule()
            registerModule(JavaTimeModule())
            enable(SerializationFeature.INDENT_OUTPUT)
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        }
    }

    /**
     * Load a MetaClass from a JSON file.
     */
    fun loadMetaClass(jsonFile: Path): MetaClass {
        return jsonFile.inputStream().use { mapper.readValue(it, MetaClass::class.java) }
    }

    /**
     * Load a MetaClass from a JSON string.
     */
    fun loadMetaClassFromString(json: String): MetaClass {
        return mapper.readValue(json, MetaClass::class.java)
    }

    /**
     * Load a MetaClass from an InputStream.
     */
    fun loadMetaClass(inputStream: InputStream): MetaClass {
        return mapper.readValue(inputStream, MetaClass::class.java)
    }

    /**
     * Load a MetaAssociation from a JSON file.
     */
    fun loadMetaAssociation(jsonFile: Path): MetaAssociation {
        return jsonFile.inputStream().use { mapper.readValue(it, MetaAssociation::class.java) }
    }

    /**
     * Load a MetaAssociation from a JSON string.
     */
    fun loadMetaAssociationFromString(json: String): MetaAssociation {
        return mapper.readValue(json, MetaAssociation::class.java)
    }

    /**
     * Save a MetaClass to a JSON file.
     */
    fun saveMetaClass(metaClass: MetaClass, jsonFile: Path) {
        jsonFile.outputStream().use { mapper.writeValue(it, metaClass) }
    }

    /**
     * Convert a MetaClass to a JSON string.
     */
    fun toJson(metaClass: MetaClass): String {
        return mapper.writeValueAsString(metaClass)
    }

    /**
     * Save a MetaAssociation to a JSON file.
     */
    fun saveMetaAssociation(association: MetaAssociation, jsonFile: Path) {
        jsonFile.outputStream().use { mapper.writeValue(it, association) }
    }

    /**
     * Convert a MetaAssociation to a JSON string.
     */
    fun toJson(association: MetaAssociation): String {
        return mapper.writeValueAsString(association)
    }

    /**
     * Load an OwnershipConfig from a JSON file.
     */
    fun loadOwnershipConfig(jsonFile: Path): OwnershipConfig {
        return jsonFile.inputStream().use { mapper.readValue(it, OwnershipConfig::class.java) }
    }

    /**
     * Load an OwnershipConfig from a JSON string.
     */
    fun loadOwnershipConfigFromString(json: String): OwnershipConfig {
        return mapper.readValue(json, OwnershipConfig::class.java)
    }

    /**
     * Load an OwnershipConfig from an InputStream.
     */
    fun loadOwnershipConfig(inputStream: InputStream): OwnershipConfig {
        return mapper.readValue(inputStream, OwnershipConfig::class.java)
    }

    /**
     * Save an OwnershipConfig to a JSON file.
     */
    fun saveOwnershipConfig(config: OwnershipConfig, jsonFile: Path) {
        jsonFile.outputStream().use { mapper.writeValue(it, config) }
    }

    /**
     * Convert an OwnershipConfig to a JSON string.
     */
    fun toJson(config: OwnershipConfig): String {
        return mapper.writeValueAsString(config)
    }
}
