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
package org.openmbee.gearshift.kerml.io

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.generated.interfaces.OwningMembership
import org.openmbee.gearshift.kerml.KerMLModel
import org.openmbee.gearshift.kerml.KerMLParseResult
import org.openmbee.gearshift.kerml.generator.KerMLWriter
import org.openmbee.mdm.framework.runtime.MountableEngine
import java.nio.file.Path
import kotlin.io.path.listDirectoryEntries
import org.openmbee.gearshift.generated.interfaces.Package as KerMLPackage

private val logger = KotlinLogging.logger {}

/**
 * Splits and reassembles a KerML model across multiple files.
 *
 * Each top-level package becomes its own `.kerml` file, improving git merge
 * granularity. Non-package top-level elements (if any) go into `_root.kerml`.
 *
 * The writer produces normalized, consistent formatting which reduces
 * spurious git merge conflicts.
 */
class ProjectFileLayout(private val writer: KerMLWriter = KerMLWriter.default()) {

    companion object {
        const val ROOT_FILE = "_root.kerml"
    }

    /**
     * Split a model into a map of filename to KerML text.
     *
     * Each top-level package produces a `{PackageName}.kerml` file.
     * Any non-package top-level elements are collected into [ROOT_FILE].
     *
     * @param model The model to split
     * @return Map of filename to KerML text content
     */
    fun writeToFiles(model: KerMLModel): Map<String, String> {
        val files = mutableMapOf<String, String>()

        // Find top-level packages: Package instances whose owningNamespace has no owner
        // (i.e., they sit directly under the implicit root namespace created by the parser)
        val topLevelPackages = findTopLevelPackages(model)

        for (pkg in topLevelPackages) {
            val name = pkg.declaredName ?: continue
            val text = writer.write(pkg)
            files["$name.kerml"] = text
            logger.debug { "Split package '$name' -> $name.kerml (${text.length} chars)" }
        }

        // Find non-package top-level namespaces (orphans)
        val topLevelNamespaces = findTopLevelNamespaces(model)
        val orphans = topLevelNamespaces.filter { it !is KerMLPackage }
        if (orphans.isNotEmpty()) {
            val texts = orphans.mapNotNull { ns ->
                try { writer.write(ns) } catch (_: Exception) { null }
            }.filter { it.isNotBlank() }
            if (texts.isNotEmpty()) {
                files[ROOT_FILE] = texts.joinToString("\n")
                logger.debug { "Collected ${texts.size} orphan elements into $ROOT_FILE" }
            }
        }

        logger.debug { "Split model into ${files.size} file(s)" }
        return files
    }

    /**
     * Find all top-level packages (direct children of a parser root namespace).
     */
    private fun findTopLevelPackages(model: KerMLModel): List<KerMLPackage> {
        val allPackages = model.allOfType<KerMLPackage>()
        return allPackages.filter { pkg ->
            val owner = pkg.owningNamespace
            // Top-level if owner is a root namespace (has no owner itself)
            owner != null && owner.owner == null
        }
    }

    /**
     * Find all top-level namespaces (including packages).
     */
    private fun findTopLevelNamespaces(model: KerMLModel): List<Namespace> {
        val allNamespaces = model.allOfType<Namespace>()
        return allNamespaces.filter { ns ->
            val owner = ns.owningNamespace
            owner != null && owner.owner == null
        }
    }

    /**
     * Read all `.kerml` files from a directory and parse them into the model.
     *
     * Files are sorted alphabetically for deterministic parse order.
     * `_root.kerml` (if present) is parsed first to ensure root-level
     * elements are available for cross-references.
     *
     * @param dir The directory containing `.kerml` files
     * @param model The model to parse into (should be freshly reset)
     * @return The parse result from the last file, or null if no files found
     */
    fun readFromDirectory(dir: Path, model: KerMLModel): KerMLParseResult? {
        val kermlFiles = dir.listDirectoryEntries("*.kerml").sorted()
        if (kermlFiles.isEmpty()) {
            logger.warn { "No .kerml files found in $dir" }
            return null
        }

        // Parse _root.kerml first if it exists, then the rest alphabetically
        val rootFile = kermlFiles.find { it.fileName.toString() == ROOT_FILE }
        val packageFiles = kermlFiles.filter { it.fileName.toString() != ROOT_FILE }
        val orderedFiles = listOfNotNull(rootFile) + packageFiles

        var lastResult: KerMLParseResult? = null
        for (file in orderedFiles) {
            logger.debug { "Parsing ${file.fileName}" }
            val pkg = model.parseFile(file)
            if (pkg != null) {
                lastResult = model.getLastParseResult()
            } else {
                logger.warn { "Failed to parse ${file.fileName}" }
                lastResult = model.getLastParseResult()
            }
        }

        logger.debug { "Parsed ${orderedFiles.size} file(s) from $dir" }
        return lastResult
    }
}
