package org.openmbee.gearshift.metamodel

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
}
