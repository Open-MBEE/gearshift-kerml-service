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

import org.openmbee.gearshift.kerml.parser.KerMLParseCoordinator
import org.openmbee.gearshift.kerml.parser.KerMLParseResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

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
                val parseResult = factory.getCoordinator().parseFile(filePath)
                results.add(LibraryLoadResult(fileName, filePath, parseResult))
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
            val parseResult = factory.getCoordinator().parseFile(filePath)
            LibraryLoadResult("Base.kerml", filePath, parseResult)
        } else {
            LibraryLoadResult("Base.kerml", filePath, null, "File not found")
        }
    }

    /**
     * Get the library path from environment or use default.
     */
    fun getLibraryPath(): Path {
        val envPath = System.getenv("KERML_LIBRARY_PATH")
        return if (envPath != null) {
            Paths.get(envPath)
        } else {
            Paths.get(DEFAULT_LIBRARY_PATH)
        }
    }

    /**
     * Check if the library is available at the default or configured path.
     */
    fun isLibraryAvailable(): Boolean {
        val libraryPath = getLibraryPath()
        return Files.exists(libraryPath.resolve("Base.kerml"))
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
