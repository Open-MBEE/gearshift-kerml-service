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
import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.kerml.antlr.KerMLLexer
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.RootNamespaceVisitor
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MetamodelRegistry
import org.openmbee.mdm.framework.runtime.Mount
import org.openmbee.mdm.framework.runtime.MountRegistry
import org.openmbee.mdm.framework.runtime.StandardMount
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

private val logger = KotlinLogging.logger {}

/**
 * Loads the KerML Kernel Semantic Library into the model.
 *
 * The Kernel Semantic Library provides the base types that all KerML elements
 * implicitly specialize or subset:
 * - Base::Anything - The top-level classifier that all Types must specialize
 * - Base::things - The top-level feature that all Features must subset
 * - And many other library types used by semantic constraints
 *
 * Per KerML 8.4.3 (Core Semantics), these library types are required for:
 * - checkTypeSpecialization: Creates implied Subclassification to Base::Anything
 * - checkFeatureSpecialization: Creates implied Subsetting to Base::things
 */
object KerMLSemanticLibraryLoader {

    /**
     * Default path to the Kernel Semantic Library.
     * Can be overridden by setting the KERML_LIBRARY_PATH environment variable.
     */
    private val DEFAULT_LIBRARY_PATH = "references/Kernel Semantic Library"

    /**
     * The ordered list of library files to load.
     * Order matters for dependency resolution.
     */
    private val LIBRARY_FILES = listOf(
        "Base.kerml",           // Base types: Anything, things, DataValue, etc.
        "Links.kerml",          // Link types
        "Objects.kerml",        // Object types
        "Occurrences.kerml",    // Occurrence types
        "Performances.kerml",   // Performance types
        "Transfers.kerml",      // Transfer types (for Flows)
        "Metaobjects.kerml"     // Metaobject types (for Metadata)
        // Additional libraries can be added as needed
    )

    /**
     * Load the Kernel Semantic Library into the given KerMLModelFactory.
     *
     * Uses the new typed visitor infrastructure for high-level API usage.
     *
     * @param factory The factory to load the library into
     * @param libraryPath Optional custom path to the library directory
     * @return List of parse results for each library file
     */
    fun loadLibrary(
        factory: KerMLModelFactory,
        libraryPath: Path = getLibraryPath()
    ): List<LibraryLoadResult> {
        val results = mutableListOf<LibraryLoadResult>()

        for (fileName in LIBRARY_FILES) {
            val filePath = libraryPath.resolve(fileName)
            if (Files.exists(filePath)) {
                val result = parseLibraryFile(factory, filePath, fileName)
                results.add(result)
            } else {
                results.add(LibraryLoadResult(fileName, filePath, null, "File not found"))
            }
        }

        return results
    }

    /**
     * Load only the Base library (minimal for core semantics).
     * This loads Base.kerml which contains Anything and things.
     *
     * Uses the new typed visitor infrastructure for high-level API usage.
     *
     * @param factory The factory to load the library into
     * @param libraryPath Optional custom path to the library directory
     * @return The parse result for Base.kerml
     */
    fun loadBaseLibrary(
        factory: KerMLModelFactory,
        libraryPath: Path = getLibraryPath()
    ): LibraryLoadResult {
        val filePath = libraryPath.resolve("Base.kerml")
        return if (Files.exists(filePath)) {
            parseLibraryFile(factory, filePath, "Base.kerml")
        } else {
            LibraryLoadResult("Base.kerml", filePath, null, "File not found")
        }
    }

    /**
     * Parse a library file using the new typed visitor infrastructure.
     * This uses high-level APIs which properly set up memberships and relationships.
     */
    private fun parseLibraryFile(
        factory: KerMLModelFactory,
        filePath: Path,
        fileName: String
    ): LibraryLoadResult {
        return try {
            println("DEBUG: Loading library file: $filePath")
            val lexer = KerMLLexer(CharStreams.fromPath(filePath))
            val tokens = CommonTokenStream(lexer)
            val parser = KerMLParser(tokens)

            val tree = parser.rootNamespace()
            println("DEBUG: Parsed tree, namespace body elements: ${tree.namespaceBodyElement().size}")

            // Use the new typed visitor with KermlParseContext
            val elementFactory = factory.engine.factory as KerMLElementFactory
            val kermlParseContext = KermlParseContext(factory.engine, elementFactory)
            val rootNamespace = RootNamespaceVisitor().visit(tree, kermlParseContext)
            println("DEBUG: Root namespace created with id: ${rootNamespace.id}")

            // Check what memberships we have
            val memberships = rootNamespace.ownedMembership
            println("DEBUG: Root namespace has ${memberships.size} ownedMemberships")
            for (m in memberships) {
                val element = m.memberElement
                println("DEBUG:   Membership: ${m.memberName} -> ${element?.let { "${it::class.simpleName}(${it.declaredName})" }}")

                // If it's a namespace, check its contents
                if (element is Namespace) {
                    val innerMemberships = element.ownedMembership
                    println("DEBUG:     Inner namespace has ${innerMemberships.size} ownedMemberships")
                    for (inner in innerMemberships.take(5)) {
                        println("DEBUG:       - ${inner.memberName} -> ${inner.memberElement?.let { "${it::class.simpleName}(${it.declaredName})" }}")
                    }
                    if (innerMemberships.size > 5) {
                        println("DEBUG:       ... and ${innerMemberships.size - 5} more")
                    }
                }
            }

            // The library root namespace stays separate - resolveGlobal searches
            // all root namespaces for top-level elements per KerML 7.2.5.3

            // Create a parse result for compatibility
            val parseResult = KerMLParseResult(
                success = true,
                rootNamespace = rootNamespace
            )

            LibraryLoadResult(fileName, filePath, parseResult)
        } catch (e: Exception) {
            println("DEBUG: Error loading library: ${e.message}")
            e.printStackTrace()
            LibraryLoadResult(fileName, filePath, null, "Parse error: ${e.message}")
        }
    }

    /**
     * Get the library path from environment or use default.
     * Searches up the directory tree to find the library in multi-module projects.
     */
    fun getLibraryPath(): Path {
        val envPath = System.getenv("KERML_LIBRARY_PATH")
        if (envPath != null) {
            return Paths.get(envPath)
        }

        // Try the default path first
        val defaultPath = Paths.get(DEFAULT_LIBRARY_PATH)
        if (Files.exists(defaultPath.resolve("Base.kerml"))) {
            return defaultPath
        }

        // Search up the directory tree (for multi-module projects)
        var current = Paths.get("").toAbsolutePath()
        repeat(5) {  // Check up to 5 levels up
            val candidate = current.resolve(DEFAULT_LIBRARY_PATH)
            if (Files.exists(candidate.resolve("Base.kerml"))) {
                return candidate
            }
            current = current.parent ?: return defaultPath
        }

        return defaultPath
    }

    /**
     * Check if the library is available at the default or configured path.
     */
    fun isLibraryAvailable(): Boolean {
        val libraryPath = getLibraryPath()
        return Files.exists(libraryPath.resolve("Base.kerml"))
    }

    /**
     * Load the Kernel Semantic Library into a dedicated engine for mounting.
     *
     * This creates a new engine, loads the library files into it, and returns
     * the engine ready for registration as a mount.
     *
     * @param libraryPath Optional custom path to the library directory
     * @return Pair of (engine, loadResults) or null if library not available
     */
    fun loadToSeparateEngine(
        libraryPath: Path = getLibraryPath()
    ): Pair<MDMEngine, List<LibraryLoadResult>>? {
        if (!isLibraryAvailable()) {
            logger.warn { "KerML library not available at $libraryPath" }
            return null
        }

        // Create a dedicated engine
        val schema = MetamodelRegistry()
        KerMLMetamodelLoader.initialize(schema)
        val factory = KerMLElementFactory()
        val engine = MDMEngine(schema, factory)

        // Create a temporary model for parsing
        val tempModel = KerMLModel(
            engine = engine,
            projectName = "KerML Kernel Semantic Library"
        )

        // Load all library files
        val results = loadLibrary(tempModel, libraryPath)

        val successCount = results.count { it.success }
        logger.info { "Loaded $successCount/${results.size} library files into separate engine (${engine.elementCount()} elements)" }

        return engine to results
    }

    /**
     * Load the library and register it as an implicit mount.
     *
     * This is a convenience method that combines loadToSeparateEngine()
     * and MountRegistry.register() into one call.
     *
     * @param mountId The ID to use for the mount (default: "kerml-kernel-semantic-library")
     * @param libraryPath Optional custom path to the library directory
     * @return The registered Mount, or null if loading failed
     */
    fun loadAndRegisterAsMount(
        mountId: String = "kerml-kernel-semantic-library",
        libraryPath: Path = getLibraryPath()
    ): Mount? {
        // Check if already registered
        if (MountRegistry.isRegistered(mountId)) {
            logger.info { "Mount '$mountId' already registered" }
            return MountRegistry.get(mountId)
        }

        // Load to separate engine
        val (engine, results) = loadToSeparateEngine(libraryPath) ?: return null

        val successCount = results.count { it.success }
        if (successCount == 0) {
            logger.error { "Failed to load any library files" }
            return null
        }

        // Register as implicit mount
        return MountRegistry.register(
            id = mountId,
            name = "KerML Kernel Semantic Library",
            engine = engine,
            priority = StandardMount.IMPLICIT_LIBRARY_PRIORITY,
            isImplicit = true
        )
    }
}

/**
 * Result of loading a library file.
 */
data class LibraryLoadResult(
    val fileName: String,
    val filePath: Path,
    val parseResult: KerMLParseResult?,
    val error: String? = null
) {
    val success: Boolean
        get() = parseResult?.success == true && error == null
}
