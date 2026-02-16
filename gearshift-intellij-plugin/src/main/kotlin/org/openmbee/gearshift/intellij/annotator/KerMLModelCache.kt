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

package org.openmbee.gearshift.intellij.annotator

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.gearshift.intellij.KerMLFileType
import org.openmbee.gearshift.intellij.psi.KerMLDeclaration
import org.openmbee.gearshift.intellij.psi.KerMLFile
import org.openmbee.gearshift.kerml.KerMLModel
import org.openmbee.gearshift.kerml.KerMLParseResult
import org.openmbee.gearshift.kerml.parser.KerMLParseError
import org.openmbee.gearshift.settings.GearshiftSettings

private val logger = KotlinLogging.logger {}

/**
 * Project-level service: one [KerMLModel] per `.kerml` file.
 *
 * Cross-file features:
 * - **Declaration index** — PSI-based index of all qualified names across
 *   the project, used for import autocomplete.
 * - **Mount-on-import** — when a file has `import Foo::*`, the file that
 *   declares `Foo` is parsed into the current file's model so the import
 *   resolves natively.
 * - **Duplicate namespace warnings** — flags when the same top-level
 *   namespace name appears in multiple files.
 */
@Service(Service.Level.PROJECT)
class KerMLModelCache(private val project: Project) {

    // ── Per-file model cache ───────────────────────────────────────────

    private data class CacheEntry(
        val model: KerMLModel,
        val parseResult: KerMLParseResult,
        val modificationStamp: Long
    )

    private val cache = LinkedHashMap<String, CacheEntry>(16, 0.75f, true)
    private val maxCacheSize = 50

    // ── Declaration index ──────────────────────────────────────────────

    /**
     * A declaration known to the project index.
     * [qualifiedName] uses `::` separators (e.g., `SpacecraftDesign::Spacecraft`).
     */
    data class IndexEntry(
        val qualifiedName: String,
        val simpleName: String,
        val kind: String,
        val filePath: String
    )

    /** All indexed declarations across the project. */
    private var declarationIndex: List<IndexEntry> = emptyList()

    /** Top-level namespace name → file path (for mount-on-import lookup). */
    private var namespaceToFile: Map<String, String> = emptyMap()

    private var indexDirty = true

    // ── Duplicate namespace tracking ───────────────────────────────────

    private val fileNamespaces = mutableMapOf<String, List<String>>()
    private val duplicateWarnings = mutableMapOf<String, List<String>>()
    private var duplicatesDirty = true

    // ── Settings ───────────────────────────────────────────────────────

    private val settings = GearshiftSettings(
        processImpliedRelationships = false,
        // TODO: set to true once kernel library .kerml files are bundled
        //       with the plugin and KerMLModel.initializeKernelLibrary() is
        //       called at plugin startup
        autoMountLibraries = false,
        autoNameFeatures = false
    )

    // ── Public API ─────────────────────────────────────────────────────

    /**
     * Return the [KerMLModel] for a file.  If the file has imports that
     * reference other project files, those files are parsed into this
     * model first (mount-on-import).
     */
    @Synchronized
    fun getModel(filePath: String, modificationStamp: Long, text: String): KerMLModel {
        val existing = cache[filePath]
        if (existing != null && existing.modificationStamp == modificationStamp) {
            return existing.model
        }
        return buildFileModel(filePath, modificationStamp, text).model
    }

    /**
     * Convenience for the annotator.
     */
    @Synchronized
    fun getParseResult(filePath: String, modificationStamp: Long, text: String): AnnotationResult {
        val existing = cache[filePath]
        val entry = if (existing != null && existing.modificationStamp == modificationStamp) {
            existing
        } else {
            buildFileModel(filePath, modificationStamp, text)
        }

        if (duplicatesDirty) rebuildDuplicateWarnings()

        return AnnotationResult(
            parseErrors = if (!entry.parseResult.success) entry.parseResult.errors else emptyList(),
            duplicateWarnings = duplicateWarnings[filePath] ?: emptyList()
        )
    }

    /**
     * Return the project-wide declaration index for import autocomplete.
     * Each entry is a qualified name (e.g., `SpacecraftDesign::Spacecraft`)
     * with its declaration kind and source file.
     */
    @Synchronized
    fun getDeclarationIndex(): List<IndexEntry> {
        if (indexDirty) rebuildDeclarationIndex()
        return declarationIndex
    }

    @Synchronized
    fun removeFile(filePath: String) {
        cache.remove(filePath)
        if (fileNamespaces.remove(filePath) != null) {
            duplicatesDirty = true
        }
        indexDirty = true
    }

    data class AnnotationResult(
        val parseErrors: List<KerMLParseError>,
        val duplicateWarnings: List<String>
    )

    // ── Model building with mount-on-import ────────────────────────────

    private fun buildFileModel(filePath: String, modificationStamp: Long, text: String): CacheEntry {
        while (cache.size >= maxCacheSize) {
            val oldest = cache.keys.iterator().next()
            cache.remove(oldest)
            fileNamespaces.remove(oldest)
        }

        val model = KerMLModel(settings = settings)

        // Mount-on-import: parse referenced files into this model first
        if (indexDirty) rebuildDeclarationIndex()
        val importTargets = extractImportRootNames(text)
        val mounted = mutableSetOf<String>()
        mountImportedFiles(model, importTargets, filePath, mounted)

        // Now parse the file itself — imports resolve against mounted content
        model.parseString(text)
        val result = model.getLastParseResult() ?: KerMLParseResult(success = true)

        val entry = CacheEntry(model, result, modificationStamp)
        cache[filePath] = entry

        // Update namespace tracking
        val names = extractTopLevelNamespaceNames(text)
        val oldNames = fileNamespaces[filePath]
        if (oldNames != names) {
            fileNamespaces[filePath] = names
            duplicatesDirty = true
            indexDirty = true
        }

        return entry
    }

    /**
     * Recursively mount files that define the namespaces referenced by
     * import statements.  Tracks [mounted] to prevent cycles.
     */
    private fun mountImportedFiles(
        model: KerMLModel,
        importRootNames: Set<String>,
        currentFilePath: String,
        mounted: MutableSet<String>
    ) {
        for (name in importRootNames) {
            val sourceFile = namespaceToFile[name] ?: continue
            if (sourceFile == currentFilePath) continue
            if (sourceFile in mounted) continue
            mounted.add(sourceFile)

            val sourceText = readFileContent(sourceFile) ?: continue

            // Recursively mount that file's imports first
            val transitiveDeps = extractImportRootNames(sourceText)
            mountImportedFiles(model, transitiveDeps, sourceFile, mounted)

            // Parse the dependency into the model
            model.parseString(sourceText)
        }
    }

    private fun readFileContent(filePath: String): String? {
        return try {
            ApplicationManager.getApplication().runReadAction<String?> {
                val vfs = com.intellij.openapi.vfs.LocalFileSystem.getInstance()
                val vf = vfs.findFileByPath(filePath) ?: return@runReadAction null
                String(vf.contentsToByteArray(), vf.charset)
            }
        } catch (e: Exception) {
            null
        }
    }

    // ── Declaration index (PSI-based) ──────────────────────────────────

    private fun rebuildDeclarationIndex() {
        val entries = mutableListOf<IndexEntry>()
        val nsToFile = mutableMapOf<String, String>()

        try {
            ApplicationManager.getApplication().runReadAction {
                val scope = GlobalSearchScope.projectScope(project)
                val kermlFiles = FileTypeIndex.getFiles(KerMLFileType, scope)
                val psiManager = PsiManager.getInstance(project)

                for (vf in kermlFiles) {
                    val psiFile = psiManager.findFile(vf) as? KerMLFile ?: continue
                    val filePath = vf.path

                    // Walk top-level declarations
                    val topDecls = PsiTreeUtil.getChildrenOfType(psiFile, KerMLDeclaration::class.java)
                        ?: continue

                    for (decl in topDecls) {
                        val name = decl.declaredName ?: continue
                        val kind = decl.declarationKeyword ?: "element"
                        entries.add(IndexEntry(name, name, kind, filePath))
                        nsToFile[name] = filePath

                        // Index nested declarations (one level deep for qualified imports)
                        indexNestedDeclarations(decl, name, filePath, entries)
                    }

                    // Update fileNamespaces as a side effect
                    val topNames = topDecls.mapNotNull { it.declaredName }
                    if (fileNamespaces[filePath] != topNames) {
                        fileNamespaces[filePath] = topNames
                        duplicatesDirty = true
                    }
                }
            }
        } catch (e: Exception) {
            logger.warn(e) { "Failed to rebuild declaration index" }
        }

        declarationIndex = entries
        namespaceToFile = nsToFile
        indexDirty = false
    }

    private fun indexNestedDeclarations(
        parent: KerMLDeclaration,
        parentQualifiedName: String,
        filePath: String,
        entries: MutableList<IndexEntry>
    ) {
        for (child in parent.nestedDeclarations) {
            val name = child.declaredName ?: continue
            val kind = child.declarationKeyword ?: "element"
            val qualifiedName = "$parentQualifiedName::$name"
            entries.add(IndexEntry(qualifiedName, name, kind, filePath))

            // Recurse for deeper nesting
            indexNestedDeclarations(child, qualifiedName, filePath, entries)
        }
    }

    // ── Duplicate namespace warnings ───────────────────────────────────

    private fun rebuildDuplicateWarnings() {
        duplicateWarnings.clear()

        if (indexDirty) rebuildDeclarationIndex()

        val nameToFiles = mutableMapOf<String, MutableList<String>>()
        for ((filePath, names) in fileNamespaces) {
            for (name in names) {
                nameToFiles.getOrPut(name) { mutableListOf() }.add(filePath)
            }
        }

        for ((name, files) in nameToFiles) {
            if (files.size <= 1) continue
            for (file in files) {
                val others = files.filter { it != file }
                val otherNames = others.joinToString(", ") { it.substringAfterLast('/') }
                val warning = "Namespace '$name' is also defined in: $otherNames"
                val list = duplicateWarnings.getOrPut(file) { mutableListOf() }
                (list as MutableList).add(warning)
            }
        }

        duplicatesDirty = false
    }

    companion object {
        fun getInstance(project: Project): KerMLModelCache {
            return project.getService(KerMLModelCache::class.java)
        }

        /** Extracts top-level namespace names from raw text (fast regex scan). */
        private val TOP_LEVEL_NS_PATTERN = Regex(
            """^\s*(?:(?:library|standard)\s+)?(?:package|namespace)\s+(?:'([^']+)'|(\w+))""",
            RegexOption.MULTILINE
        )

        fun extractTopLevelNamespaceNames(text: String): List<String> {
            val names = mutableListOf<String>()
            for (match in TOP_LEVEL_NS_PATTERN.findAll(text)) {
                val name = match.groupValues[1].ifEmpty { match.groupValues[2] }
                if (name.isNotEmpty()) names.add(name)
            }
            return names
        }

        /**
         * Extract the root namespace name from each import statement.
         * `import Foo::Bar::*` → `Foo`
         * `import Baz::*`      → `Baz`
         */
        private val IMPORT_PATTERN = Regex(
            """import\s+(?:all\s+)?(?:'([^']+)'|(\w+))""",
            RegexOption.MULTILINE
        )

        fun extractImportRootNames(text: String): Set<String> {
            val names = mutableSetOf<String>()
            for (match in IMPORT_PATTERN.findAll(text)) {
                val name = match.groupValues[1].ifEmpty { match.groupValues[2] }
                if (name.isNotEmpty()) names.add(name)
            }
            return names
        }
    }
}
