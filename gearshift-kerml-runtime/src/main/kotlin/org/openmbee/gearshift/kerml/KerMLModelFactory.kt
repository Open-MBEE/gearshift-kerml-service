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

import org.openmbee.gearshift.framework.runtime.MDMEngine
import org.openmbee.gearshift.MDMModelFactory
import org.openmbee.gearshift.generated.Wrappers
import org.openmbee.gearshift.generated.interfaces.Element
import org.openmbee.gearshift.generated.interfaces.ModelElement
import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.generated.interfaces.OwningMembership
import org.openmbee.gearshift.kerml.parser.KerMLParseCoordinator
import org.openmbee.gearshift.kerml.parser.KerMLParseResult
import java.nio.file.Path
import org.openmbee.gearshift.generated.interfaces.Package as KerMLPackage

/**
 * Factory for parsing KerML text and accessing typed wrapper objects.
 * Extends MDMModelFactory with KerML-specific semantics.
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
 *
 * // With project metadata:
 * val factory = KerMLModelFactory(
 *     projectId = "my-vehicle-model-v1",
 *     projectName = "Vehicle Model",
 *     projectDescription = "A KerML model of vehicle types"
 * )
 * println("Project: ${factory.project.name} (${factory.projectId})")
 * ```
 */
class KerMLModelFactory(
    engine: MDMEngine = createKerMLEngine(),
    projectId: String? = null,
    projectName: String = "Untitled KerML Project",
    projectDescription: String? = null
) : MDMModelFactory(engine, projectId, projectName, projectDescription) {

    private val coordinator = KerMLParseCoordinator(engine)
    private var lastParseResult: KerMLParseResult? = null

    /**
     * The KerML semantic handler for processing implied relationships.
     */
    private val semanticHandler = KerMLSemanticHandler(engine)

    init {
        // Initialize the model root (from base class)
        initializeModelRoot()

        // Register the KerML semantic handler for lifecycle events
        engine.registerLifecycleHandler(semanticHandler)
    }

    companion object {
        /**
         * Create an MDMEngine with the KerML metamodel loaded.
         */
        fun createKerMLEngine(): MDMEngine {
            val schema = org.openmbee.gearshift.framework.runtime.MetamodelRegistry()
            KerMLMetamodelLoader.initialize(schema)
            return MDMEngine(schema)
        }
    }

    // ===== Typed Helper Methods (inline with reified) =====

    /**
     * Get a typed wrapper for an element by its ID.
     */
    inline fun <reified T : ModelElement> getAs(id: String): T? {
        return getAsElement(id) as? T
    }

    /**
     * Create a new instance of a model element type.
     */
    inline fun <reified T : ModelElement> create(): T {
        val typeName = T::class.simpleName ?: throw IllegalArgumentException("Cannot determine type name")
        return createByName(typeName) as T
    }

    /**
     * Get all elements of a specific type.
     */
    inline fun <reified T : ModelElement> allOfType(): List<T> {
        val typeName = T::class.simpleName ?: return emptyList()
        return allOfTypeByName(typeName).filterIsInstance<T>()
    }

    /**
     * Find all elements matching a predicate.
     */
    inline fun <reified T : ModelElement> findAll(predicate: (T) -> Boolean): List<T> {
        return allOfType<T>().filter(predicate)
    }

    // ===== Parsing Methods =====

    /**
     * Parse KerML text from a string.
     * Multiple calls accumulate elements in the model.
     *
     * Implied relationships (e.g., implicit specializations) are created automatically
     * via lifecycle handlers as elements are parsed.
     *
     * @param kermlText The KerML source code
     * @return The root Package if successful, null otherwise
     */
    fun parseString(kermlText: String): KerMLPackage? {
        // Don't reset - allow accumulation across multiple parses
        lastParseResult = coordinator.parseString(kermlText)

        if (lastParseResult!!.errors.isNotEmpty()) {
            return null
        }

        // Find the first Package in the parsed model
        return allOfType<KerMLPackage>().firstOrNull()
    }

    /**
     * Parse KerML from a file.
     * Multiple calls accumulate elements in the model.
     *
     * Implied relationships (e.g., implicit specializations) are created automatically
     * via lifecycle handlers as elements are parsed.
     *
     * @param path Path to the KerML file
     * @return The root Package if successful, null otherwise
     */
    fun parseFile(path: Path): KerMLPackage? {
        // Don't reset - allow accumulation across multiple parses
        lastParseResult = coordinator.parseFile(path)

        if (lastParseResult!!.errors.isNotEmpty()) {
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
     * Get the root element if one was parsed.
     */
    fun getRootElement(): Element? {
        val rootId = lastParseResult?.rootElementId ?: return null
        return getAs<Element>(rootId)
    }

    /**
     * Get all parsed elements by qualified name (from old coordinator).
     */
    fun getParsedElements(): Map<String, String> = coordinator.getParsedElements()

    // ===== Name Resolution =====

    /**
     * Find an element by its qualified name.
     * Resolves starting from the model root namespace.
     *
     * @param qualifiedName The qualified name (e.g., "Base::Anything")
     * @return The element or null if not found
     */
    fun findByQualifiedName(qualifiedName: String): Element? {
        // Try the coordinator's parsed elements map
        val id = coordinator.getParsedElements()[qualifiedName]
        if (id != null) {
            return getAs<Element>(id)
        }

        // TODO: Implement proper name resolution using KerML spec operations
        // For now, return null - name resolution will be reimplemented
        return null
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
     * Get the parse coordinator for advanced operations.
     */
    fun getCoordinator(): KerMLParseCoordinator = coordinator

    // ===== Model Management =====

    /**
     * Add a parsed namespace (or its contents) to the model root.
     * This is called when loading libraries or parsing content with the new visitor API.
     */
    fun addToModel(namespace: Namespace) {
        // Add the namespace's memberships to the model root
        for (membership in namespace.ownedMembership) {
            // Create a new OwningMembership linking to the model root
            val rootMembership = create<OwningMembership>()
            rootMembership.memberElement = membership.memberElement
            membership.memberName?.let { rootMembership.memberName = it }
            membership.memberShortName?.let { rootMembership.memberShortName = it }
            rootMembership.membershipOwningNamespace = modelRoot
        }
    }

    /**
     * Clear all parsed data and reset the factory.
     */
    override fun reset() {
        coordinator.reset()
        lastParseResult = null
        super.reset()
    }
}
