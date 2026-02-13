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
     * Base path for library resources on the classpath.
     */
    private const val RESOURCE_BASE_PATH = "kerml-library"

    /**
     * Default file system path (fallback if not found on classpath).
     */
    private val DEFAULT_LIBRARY_PATH = "references/Kernel Semantic Library"

    /**
     * The ordered list of library files to load with their subdirectory.
     * Order matters for dependency resolution - base types must be loaded first.
     */
    private data class LibraryFile(val name: String, val subdir: String)

    private val LIBRARY_FILES = listOf(
        // === Kernel Semantic Library (core types) ===
        LibraryFile("Base.kerml", "Kernel Semantic Library"),
        LibraryFile("Links.kerml", "Kernel Semantic Library"),
        LibraryFile("Objects.kerml", "Kernel Semantic Library"),
        LibraryFile("Occurrences.kerml", "Kernel Semantic Library"),
        LibraryFile("Performances.kerml", "Kernel Semantic Library"),
        LibraryFile("Transfers.kerml", "Kernel Semantic Library"),
        LibraryFile("Metaobjects.kerml", "Kernel Semantic Library"),
        LibraryFile("Clocks.kerml", "Kernel Semantic Library"),
        LibraryFile("ControlPerformances.kerml", "Kernel Semantic Library"),
        LibraryFile("FeatureReferencingPerformances.kerml", "Kernel Semantic Library"),
        LibraryFile("Observation.kerml", "Kernel Semantic Library"),
        LibraryFile("SpatialFrames.kerml", "Kernel Semantic Library"),
        LibraryFile("StatePerformances.kerml", "Kernel Semantic Library"),
        LibraryFile("TransitionPerformances.kerml", "Kernel Semantic Library"),
        LibraryFile("Triggers.kerml", "Kernel Semantic Library"),
        LibraryFile("KerML.kerml", "Kernel Semantic Library"),

        // === Kernel Data Type Library ===
        LibraryFile("ScalarValues.kerml", "Kernel Data Type Library"),
        LibraryFile("Collections.kerml", "Kernel Data Type Library"),
        LibraryFile("VectorValues.kerml", "Kernel Data Type Library"),

        // === Kernel Function Library ===
        LibraryFile("BaseFunctions.kerml", "Kernel Function Library"),
        LibraryFile("ScalarFunctions.kerml", "Kernel Function Library"),
        LibraryFile("BooleanFunctions.kerml", "Kernel Function Library"),
        LibraryFile("IntegerFunctions.kerml", "Kernel Function Library"),
        LibraryFile("NaturalFunctions.kerml", "Kernel Function Library"),
        LibraryFile("NumericalFunctions.kerml", "Kernel Function Library"),
        LibraryFile("RationalFunctions.kerml", "Kernel Function Library"),
        LibraryFile("RealFunctions.kerml", "Kernel Function Library"),
        LibraryFile("ComplexFunctions.kerml", "Kernel Function Library"),
        LibraryFile("TrigFunctions.kerml", "Kernel Function Library"),
        LibraryFile("StringFunctions.kerml", "Kernel Function Library"),
        LibraryFile("CollectionFunctions.kerml", "Kernel Function Library"),
        LibraryFile("SequenceFunctions.kerml", "Kernel Function Library"),
        LibraryFile("VectorFunctions.kerml", "Kernel Function Library"),
        LibraryFile("ControlFunctions.kerml", "Kernel Function Library"),
        LibraryFile("DataFunctions.kerml", "Kernel Function Library"),
        LibraryFile("OccurrenceFunctions.kerml", "Kernel Function Library"),

        // === Views Library (non-normative extension) ===
        LibraryFile("Views.kerml", "Views Library"),

        // === DocGen Library (non-normative extension) ===
        LibraryFile("DocGen.kerml", "DocGen Library")
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
        libraryPath: Path? = null
    ): List<LibraryLoadResult> {
        val results = mutableListOf<LibraryLoadResult>()

        for (libFile in LIBRARY_FILES) {
            val result = loadLibraryFile(factory, libFile, libraryPath)
            results.add(result)
        }

        return results
    }

    /**
     * Load a single library file, trying classpath first, then file system.
     */
    private fun loadLibraryFile(
        factory: KerMLModelFactory,
        libFile: LibraryFile,
        overridePath: Path?
    ): LibraryLoadResult {
        // Try classpath first
        val resourcePath = "$RESOURCE_BASE_PATH/${libFile.subdir}/${libFile.name}"
        val inputStream = javaClass.classLoader.getResourceAsStream(resourcePath)

        if (inputStream != null) {
            return try {
                val content = inputStream.bufferedReader().use { it.readText() }
                parseLibraryContent(factory, content, libFile.name)
            } catch (e: Exception) {
                LibraryLoadResult(libFile.name, Paths.get(resourcePath), null, "Parse error: ${e.message}")
            }
        }

        // Fallback to file system
        val basePath = overridePath?.parent ?: getLibraryPath().parent ?: Paths.get("references")
        val filePath = basePath.resolve(libFile.subdir).resolve(libFile.name)

        return if (Files.exists(filePath)) {
            parseLibraryFile(factory, filePath, libFile.name)
        } else {
            LibraryLoadResult(libFile.name, filePath, null, "File not found (tried classpath: $resourcePath)")
        }
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
            logger.debug { "Loading library file: $filePath" }
            val lexer = KerMLLexer(CharStreams.fromPath(filePath))
            val tokens = CommonTokenStream(lexer)
            val parser = KerMLParser(tokens)

            val tree = parser.rootNamespace()
            logger.debug { "Parsed tree, namespace body elements: ${tree.namespaceBodyElement().size}" }

            // Use the new typed visitor with KermlParseContext
            // Mark as library context so elements get deterministic IDs based on qualified names
            val elementFactory = factory.engine.factory as KerMLElementFactory
            val kermlParseContext = KermlParseContext(factory.engine, elementFactory).asLibraryContext()
            val rootNamespace = RootNamespaceVisitor().visit(tree, kermlParseContext)

            // Assign spec-compliant UUID v5 IDs to all library elements
            LibraryElementIdAssigner.assignIds(rootNamespace)

            logger.debug { "Root namespace created with id: ${rootNamespace.id}" }

            // Check what memberships we have
            val memberships = rootNamespace.ownedMembership
            logger.debug { "Root namespace has ${memberships.size} ownedMemberships" }
            for (m in memberships) {
                val element = m.memberElement
                logger.debug { "  Membership: ${m.memberName} -> ${element?.let { "${it::class.simpleName}(${it.declaredName})" }}" }

                // If it's a namespace, check its contents
                if (element is Namespace) {
                    val innerMemberships = element.ownedMembership
                    logger.debug { "    Inner namespace has ${innerMemberships.size} ownedMemberships" }
                    for (inner in innerMemberships.take(5)) {
                        logger.debug { "      - ${inner.memberName} -> ${inner.memberElement?.let { "${it::class.simpleName}(${it.declaredName})" }}" }
                    }
                    if (innerMemberships.size > 5) {
                        logger.debug { "      ... and ${innerMemberships.size - 5} more" }
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
            logger.error(e) { "Error loading library file: ${e.message}" }
            LibraryLoadResult(fileName, filePath, null, "Parse error: ${e.message}")
        }
    }

    /**
     * Parse library content from a string (for classpath resources).
     */
    private fun parseLibraryContent(
        factory: KerMLModelFactory,
        content: String,
        fileName: String
    ): LibraryLoadResult {
        return try {
            logger.debug { "Loading library from classpath: $fileName" }
            val lexer = KerMLLexer(CharStreams.fromString(content))
            val tokens = CommonTokenStream(lexer)
            val parser = KerMLParser(tokens)

            val tree = parser.rootNamespace()

            // Use the new typed visitor with KermlParseContext
            // Mark as library context so elements get deterministic IDs based on qualified names
            val elementFactory = factory.engine.factory as KerMLElementFactory
            val kermlParseContext = KermlParseContext(factory.engine, elementFactory).asLibraryContext()
            val rootNamespace = RootNamespaceVisitor().visit(tree, kermlParseContext)

            // Assign spec-compliant UUID v5 IDs to all library elements
            LibraryElementIdAssigner.assignIds(rootNamespace)

            val parseResult = KerMLParseResult(
                success = true,
                rootNamespace = rootNamespace
            )

            LibraryLoadResult(fileName, Paths.get(fileName), parseResult)
        } catch (e: Exception) {
            logger.error(e) { "Error loading library $fileName from classpath" }
            LibraryLoadResult(fileName, Paths.get(fileName), null, "Parse error: ${e.message}")
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
     * Check if the library is available (classpath or file system).
     */
    fun isLibraryAvailable(): Boolean {
        // Check classpath first
        val resourcePath = "$RESOURCE_BASE_PATH/Kernel Semantic Library/Base.kerml"
        if (javaClass.classLoader.getResource(resourcePath) != null) {
            return true
        }

        // Fall back to file system
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
        ViewsExtensionLoader.initialize(schema)
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
