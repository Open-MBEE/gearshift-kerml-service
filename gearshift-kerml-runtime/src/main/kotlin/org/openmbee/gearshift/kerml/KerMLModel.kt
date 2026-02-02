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

import io.github.oshai.kotlinlogging.KotlinLogging
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.openmbee.gearshift.generated.KerMLElementFactory
import org.openmbee.gearshift.generated.interfaces.Element
import org.openmbee.gearshift.generated.interfaces.ModelElement
import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.kerml.antlr.KerMLLexer
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KerMLErrorListener
import org.openmbee.gearshift.kerml.parser.KerMLParseError
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.TypedVisitorFactory
import org.openmbee.gearshift.kerml.parser.visitors.base.ReferenceCollector
import org.openmbee.gearshift.kerml.parser.visitors.base.ReferenceResolver
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MDMModel
import org.openmbee.mdm.framework.runtime.MetamodelRegistry
import org.openmbee.mdm.framework.runtime.Mount
import org.openmbee.mdm.framework.runtime.MountableEngine
import org.openmbee.mdm.framework.runtime.MountRegistry
import org.openmbee.mdm.framework.runtime.StandardMount
import java.nio.file.Path
import org.openmbee.gearshift.generated.interfaces.Package as KerMLPackage

private val logger = KotlinLogging.logger {}

/**
 * Result of a KerML parsing operation.
 */
data class KerMLParseResult(
    val success: Boolean,
    val rootNamespace: Namespace? = null,
    val errors: List<KerMLParseError> = emptyList()
)

/**
 * KerML model container with parsing and typed element access.
 *
 * Extends MDMModel with KerML-specific semantics including:
 * - KerML grammar parsing (from string or file)
 * - Typed element accessors via generated interfaces
 * - Semantic library loading
 * - Implied relationship handling
 *
 * Example usage:
 * ```kotlin
 * val model = KerMLModel()
 * val pkg = model.parseString("""
 *     package Vehicles {
 *         class Vehicle {
 *             feature wheels : Integer;
 *         }
 *         class Car :> Vehicle;
 *     }
 * """.trimIndent())
 *
 * if (pkg != null) {
 *     val car = model.allOfType<Class>().first { it.name == "Car" }
 *     println("Car extends: ${car.superclassifier.first().name}")
 * }
 *
 * // With project metadata:
 * val model = KerMLModel(
 *     projectId = "my-vehicle-model-v1",
 *     projectName = "Vehicle Model",
 *     projectDescription = "A KerML model of vehicle types"
 * )
 * println("Project: ${model.project.name} (${model.projectId})")
 * ```
 */
class KerMLModel(
    engine: MDMEngine = createKerMLEngine(),
    projectId: String? = null,
    projectName: String = "Untitled KerML Project",
    projectDescription: String? = null
) : MDMModel(engine, "Namespace", projectId, projectName, projectDescription) {

    private var lastParseResult: KerMLParseResult? = null

    /**
     * The KerML semantic handler for processing implied relationships.
     */
    private val semanticHandler = KerMLSemanticHandler(engine)

    /**
     * The model root as a typed Namespace.
     */
    val root: Namespace
        get() = modelRoot as Namespace

    init {
        // Register the KerML semantic handler for lifecycle events
        engine.registerLifecycleHandler(semanticHandler)

        // Auto-mount implicit libraries if using MountableEngine
        (engine as? MountableEngine)?.mountImplicit()
    }

    companion object {
        /**
         * Mount ID for the KerML Kernel Semantic Library.
         */
        const val KERNEL_LIBRARY_MOUNT_ID = "kerml-kernel-semantic-library"

        /**
         * Create an MDMEngine with the KerML metamodel loaded.
         * Does NOT include mount support - use createKerMLEngineWithMounts() for that.
         */
        fun createKerMLEngine(): MDMEngine {
            val schema = MetamodelRegistry()
            KerMLMetamodelLoader.initialize(schema)
            val factory = KerMLElementFactory()
            return MDMEngine(schema, factory)
        }

        /**
         * Create a MountableEngine with the KerML metamodel loaded.
         * Use this when you want mount support (for library sharing).
         *
         * Note: This does NOT auto-mount implicit libraries. That happens
         * in the KerMLModel constructor when using a MountableEngine.
         */
        fun createKerMLEngineWithMounts(): MountableEngine {
            val schema = MetamodelRegistry()
            KerMLMetamodelLoader.initialize(schema)
            val factory = KerMLElementFactory()
            return MountableEngine(schema, factory)
        }

        /**
         * Initialize the Kernel Semantic Library as an implicit mount.
         *
         * Call this once at application startup to make the library available
         * for all sessions. The library is loaded into a dedicated engine and
         * registered as an implicit mount.
         *
         * @param libraryPath Optional custom path to the library directory
         * @return The registered mount, or null if library is not available
         */
        fun initializeKernelLibrary(libraryPath: Path? = null): Mount? {
            // Check if already registered
            if (MountRegistry.isRegistered(KERNEL_LIBRARY_MOUNT_ID)) {
                logger.info { "Kernel library already registered" }
                return MountRegistry.get(KERNEL_LIBRARY_MOUNT_ID)
            }

            // Check if library is available
            if (!KerMLSemanticLibraryLoader.isLibraryAvailable()) {
                logger.warn { "KerML Kernel Semantic Library not found - implicit specializations will not work" }
                return null
            }

            try {
                // Create a dedicated engine for the library
                val libraryEngine = createKerMLEngine()

                // Create a temporary KerMLModel to use for parsing
                val libraryModel = KerMLModel(
                    engine = libraryEngine,
                    projectName = "KerML Kernel Semantic Library"
                )

                // Load the library using the existing loader
                val path = libraryPath ?: KerMLSemanticLibraryLoader.getLibraryPath()
                val results = KerMLSemanticLibraryLoader.loadLibrary(libraryModel, path)

                val successCount = results.count { it.success }
                val failCount = results.count { !it.success }

                if (successCount == 0) {
                    logger.error { "Failed to load any library files" }
                    return null
                }

                logger.info { "Loaded $successCount library files ($failCount failed)" }

                // Register as implicit mount
                val mount = MountRegistry.register(
                    id = KERNEL_LIBRARY_MOUNT_ID,
                    name = "KerML Kernel Semantic Library",
                    engine = libraryEngine,
                    priority = StandardMount.IMPLICIT_LIBRARY_PRIORITY,
                    isImplicit = true
                )

                logger.info { "Registered kernel library mount with ${libraryEngine.elementCount()} elements" }
                return mount
            } catch (e: Exception) {
                logger.error(e) { "Failed to initialize kernel library" }
                return null
            }
        }

        /**
         * Create a KerMLModel with mount support and auto-mounted kernel library.
         *
         * This is the recommended way to create a KerMLModel when you need
         * access to library types like Base::Anything.
         *
         * Example:
         * ```kotlin
         * // Initialize library once at startup
         * KerMLModel.initializeKernelLibrary()
         *
         * // Create models that share the library
         * val model = KerMLModel.createWithMounts()
         * model.parseString("class MyCar :> Base::Anything;")
         * ```
         */
        fun createWithMounts(
            projectId: String? = null,
            projectName: String = "Untitled KerML Project",
            projectDescription: String? = null
        ): KerMLModel {
            return KerMLModel(
                engine = createKerMLEngineWithMounts(),
                projectId = projectId,
                projectName = projectName,
                projectDescription = projectDescription
            )
        }
    }

    // ===== Typed Helper Methods =====

    /**
     * Get an element by ID as a typed ModelElement.
     */
    fun getTypedElement(id: String): ModelElement? {
        return engine.getElement(id) as? ModelElement
    }

    /**
     * Get all elements of a specific type.
     */
    inline fun <reified T : ModelElement> allOfType(): List<T> {
        val typeName = T::class.simpleName ?: return emptyList()
        return engine.getElementsByClass(typeName).filterIsInstance<T>()
    }

    /**
     * Find all elements matching a predicate.
     */
    inline fun <reified T : ModelElement> findAll(predicate: (T) -> Boolean): List<T> {
        return allOfType<T>().filter(predicate)
    }

    /**
     * Find an element by name.
     * Returns the first element of the specified type whose name or declaredName matches.
     */
    inline fun <reified T : Element> findByName(name: String): T? {
        return allOfType<T>().firstOrNull {
            it.name == name || it.declaredName == name
        }
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

        // Process any pending implied relationships after parsing
        semanticHandler.processAllPending()

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

        // Process any pending implied relationships after parsing
        semanticHandler.processAllPending()

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
        val parseContext = KermlParseContext(
            engine = engine,
            factory = engine.factory as KerMLElementFactory,
            referenceCollector = referenceCollector
        )

        // Use the typed visitor to parse the tree
        val rootNamespace = TypedVisitorFactory.Core.rootNamespace.visit(tree, parseContext)

        // Resolve collected references after parsing
        val resolver = ReferenceResolver(engine)
        resolver.resolveAll(referenceCollector)

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

    /**
     * Clear all parsed data and reset the model.
     */
    override fun reset() {
        lastParseResult = null
        super.reset()
    }
}

// Type alias for backwards compatibility
@Deprecated("Use KerMLModel instead", ReplaceWith("KerMLModel"))
typealias KerMLModelFactory = KerMLModel
