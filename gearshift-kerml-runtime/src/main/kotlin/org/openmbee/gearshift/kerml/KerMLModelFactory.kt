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

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.openmbee.gearshift.MDMModelFactory
import org.openmbee.gearshift.framework.runtime.MDMEngine
import org.openmbee.gearshift.generated.interfaces.Element
import org.openmbee.gearshift.generated.interfaces.ModelElement
import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.generated.interfaces.OwningMembership
import org.openmbee.gearshift.kerml.antlr.KerMLLexer
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KerMLErrorListener
import org.openmbee.gearshift.kerml.parser.KerMLParseError
import org.openmbee.gearshift.kerml.parser.visitors.TypedVisitorFactory
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.ReferenceCollector
import java.nio.file.Path
import org.openmbee.gearshift.generated.interfaces.Package as KerMLPackage

/**
 * Result of a KerML parsing operation.
 */
data class KerMLParseResult(
    val success: Boolean,
    val rootNamespace: Namespace? = null,
    val errors: List<KerMLParseError> = emptyList()
)

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
        lastParseResult = parseKerML(CharStreams.fromString(kermlText))

        if (!lastParseResult!!.success) {
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
        lastParseResult = parseKerML(CharStreams.fromPath(path))

        if (!lastParseResult!!.success) {
            return null
        }

        // Find the first Package in the parsed model
        return allOfType<KerMLPackage>().firstOrNull()
    }

    /**
     * Parse KerML from a CharStream using the visitor-based architecture.
     */
    private fun parseKerML(input: org.antlr.v4.runtime.CharStream): KerMLParseResult {
        val errorListener = KerMLErrorListener()

        // Create ANTLR lexer and parser
        val lexer = KerMLLexer(input)
        lexer.removeErrorListeners()
        lexer.addErrorListener(errorListener)

        val tokens = CommonTokenStream(lexer)
        val parser = KerMLParser(tokens)
        parser.removeErrorListeners()
        parser.addErrorListener(errorListener)

        // Parse the root namespace
        val tree = parser.rootNamespace()

        // Check for syntax errors
        if (errorListener.hasErrors) {
            return KerMLParseResult(
                success = false,
                errors = errorListener.errors
            )
        }

        // Create parse context with reference collector
        val referenceCollector = ReferenceCollector()
        val parseContext = ParseContext(
            engine = engine,
            referenceCollector = referenceCollector
        )

        // Use the typed visitor to parse the tree
        val rootNamespace = TypedVisitorFactory.Core.rootNamespace.visit(tree, parseContext)

        // TODO: Resolve collected references after parsing
        // For now, references are collected but resolution is deferred

        return KerMLParseResult(
            success = true,
            rootNamespace = rootNamespace
        )
    }

    /**
     * Get the last parse result for detailed error information.
     */
    fun getLastParseResult(): KerMLParseResult? = lastParseResult

    /**
     * Get the root element if one was parsed.
     */
    fun getRootElement(): Element? {
        return lastParseResult?.rootNamespace as? Element
    }

    // ===== Name Resolution =====

    /**
     * Find an element by its qualified name.
     * Resolves starting from the model root namespace.
     *
     * @param qualifiedName The qualified name (e.g., "Base::Anything")
     * @return The element or null if not found
     */
    fun findByQualifiedName(qualifiedName: String): Element? {
        // TODO: Implement proper name resolution using KerML spec operations
        // For now, search by name parts
        val parts = qualifiedName.split("::")
        if (parts.isEmpty()) return null

        // Try to find element with matching declared name
        return allOfType<Element>().firstOrNull { element ->
            element.declaredName == parts.last()
        }
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
        lastParseResult = null
        super.reset()
    }
}
