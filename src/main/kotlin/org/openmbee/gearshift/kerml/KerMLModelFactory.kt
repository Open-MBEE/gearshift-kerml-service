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
package org.openmbee.gearshift.kerml

import org.openmbee.gearshift.GearshiftEngine
import org.openmbee.gearshift.generated.Wrappers
import org.openmbee.gearshift.generated.interfaces.*
import org.openmbee.gearshift.kerml.parser.KerMLParseCoordinator
import org.openmbee.gearshift.kerml.parser.KerMLParseResult
import java.nio.file.Path
import org.openmbee.gearshift.generated.interfaces.Package as KerMLPackage

/**
 * Factory for parsing KerML text and accessing typed wrapper objects.
 * Provides a high-level API for working with KerML models.
 *
 * Example usage:
 * ```kotlin
 * val factory = KerMLModelFactory()
 * val pkg = factory.parseString("""
 *     package Vehicles {
 *         class Vehicle {
 *             feature wheels : Integer;
 *         }
 *         class Car :> Vehicle;
 *     }
 * """.trimIndent())
 *
 * if (pkg != null) {
 *     val car = factory.allOfType<Class>().first { it.name == "Car" }
 *     println("Car extends: ${car.superclassifier.first().name}")
 * }
 * ```
 */
class KerMLModelFactory(
    val engine: GearshiftEngine = GearshiftEngine()
) {
    private val coordinator = KerMLParseCoordinator(engine)
    private var lastParseResult: KerMLParseResult? = null

    init {
        // Load the KerML metamodel
        KerMLMetamodelLoader.initialize(engine)
    }

    /**
     * Parse KerML text from a string.
     *
     * @param kermlText The KerML source code
     * @return The root Package if successful, null otherwise
     */
    fun parseString(kermlText: String): KerMLPackage? {
        coordinator.reset()
        lastParseResult = coordinator.parseString(kermlText)

        if (!lastParseResult!!.success) {
            return null
        }

        // Find the first Package in the parsed model
        return allOfType<KerMLPackage>().firstOrNull()
    }

    /**
     * Parse KerML from a file.
     *
     * @param path Path to the KerML file
     * @return The root Package if successful, null otherwise
     */
    fun parseFile(path: Path): KerMLPackage? {
        coordinator.reset()
        lastParseResult = coordinator.parseFile(path)

        if (!lastParseResult!!.success) {
            return null
        }

        // Find the first Package in the parsed model
        return allOfType<KerMLPackage>().firstOrNull()
    }

    /**
     * Get the last parse result for detailed error information.
     */
    fun getLastParseResult(): KerMLParseResult? = lastParseResult

    /**
     * Get a typed wrapper for an element by its ID.
     *
     * @param id The element ID
     * @return The typed wrapper or null if not found
     */
    inline fun <reified T : ModelElement> getAs(id: String): T? {
        val obj = engine.getInstance(id) ?: return null
        val wrapper = Wrappers.wrap(obj, engine)
        return wrapper as? T
    }

    /**
     * Get all elements of a specific type.
     *
     * @return List of all elements matching the type
     */
    inline fun <reified T : ModelElement> allOfType(): List<T> {
        val typeName = T::class.simpleName ?: return emptyList()
        return engine.getInstancesByType(typeName).mapNotNull { obj ->
            val wrapper = Wrappers.wrap(obj, engine)
            wrapper as? T
        }
    }

    /**
     * Get the root element if one was parsed.
     */
    fun getRootElement(): Element? {
        val rootId = lastParseResult?.rootElementId ?: return null
        return getAs<Element>(rootId)
    }

    /**
     * Get all parsed elements by qualified name.
     */
    fun getParsedElements(): Map<String, String> = coordinator.getParsedElements()

    /**
     * Find an element by its qualified name.
     *
     * @param qualifiedName The qualified name (e.g., "Vehicles::Car")
     * @return The element or null if not found
     */
    fun findByQualifiedName(qualifiedName: String): Element? {
        val id = coordinator.getParsedElements()[qualifiedName] ?: return null
        return getAs<Element>(id)
    }

    /**
     * Find an element by its simple name.
     * If multiple elements have the same name, returns the first match.
     *
     * @param name The simple name to search for
     * @return The element or null if not found
     */
    inline fun <reified T : Element> findByName(name: String): T? {
        return allOfType<T>().firstOrNull { it.name == name || it.declaredName == name }
    }

    /**
     * Find all elements matching a predicate.
     */
    inline fun <reified T : ModelElement> findAll(predicate: (T) -> Boolean): List<T> {
        return allOfType<T>().filter(predicate)
    }

    /**
     * Get the parse coordinator for advanced operations.
     */
    fun getCoordinator(): KerMLParseCoordinator = coordinator

    /**
     * Clear all parsed data and reset the factory.
     */
    fun reset() {
        coordinator.reset()
        engine.clearInstances()
        lastParseResult = null
    }
}
