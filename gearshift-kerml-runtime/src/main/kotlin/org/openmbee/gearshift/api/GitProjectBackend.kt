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
package org.openmbee.gearshift.api

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.oshai.kotlinlogging.KotlinLogging
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.MergeResult
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.treewalk.TreeWalk
import org.openmbee.gearshift.kerml.index.ModelIndex
import org.openmbee.mdm.framework.runtime.BranchData
import org.openmbee.mdm.framework.runtime.CommitData
import org.openmbee.mdm.framework.runtime.ProjectMetadata
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant

private val logger = KotlinLogging.logger {}

/**
 * Persisted project metadata written to project.json.
 */
data class GitPersistedProjectState(
    val project: ProjectMetadata,
    val branches: List<BranchData>,
    val commits: List<GitPersistedCommit>
)

/**
 * Commit metadata without the DataVersionData changes payload.
 */
data class GitPersistedCommit(
    val id: String,
    val description: String?,
    val owningProject: String,
    val previousCommit: List<String> = emptyList(),
    val created: Instant = Instant.now()
)

/**
 * Git-backed project persistence using JGit.
 *
 * Each project is a bare git repository under `{dataDir}/projects/{projectId}/`.
 * Model files are split into per-package `.kerml` files by [ProjectFileLayout],
 * and the [ModelIndex] is stored in `.gearshift/index.json`.
 *
 * Storage layout:
 * ```
 * {dataDir}/projects/{projectId}/
 * ├── .git/
 * ├── project.json              # Project metadata
 * ├── .gearshift/
 * │   └── index.json            # ModelIndex (ID ↔ QN)
 * ├── SpacecraftDesign.kerml    # One per top-level package
 * ├── Requirements.kerml
 * └── ...
 * ```
 */
class GitProjectBackend(
    private val dataDir: Path
) : ProjectBackend {

    private val objectMapper = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule())
        enable(SerializationFeature.INDENT_OUTPUT)
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    private val projectsDir: Path = dataDir.resolve("projects")

    init {
        Files.createDirectories(projectsDir)
        logger.info { "Git persistence enabled at $dataDir" }
    }

    // ===== ProjectBackend interface =====

    override fun saveProject(projectId: String, store: ProjectStore) {
        // No-op for now — commits are saved via commitState()
        // This is called by ProjectStore after every mutation for backward compat.
        // The actual git commit happens in commitState().
    }

    override fun deleteProject(projectId: String) {
        val projectDir = projectsDir.resolve(projectId)
        if (Files.exists(projectDir)) {
            Files.walk(projectDir)
                .sorted(Comparator.reverseOrder())
                .forEach { Files.deleteIfExists(it) }
            logger.info { "Deleted git project $projectId" }
        }
    }

    override fun loadAll(store: ProjectStore): Int {
        if (!Files.isDirectory(projectsDir)) return 0

        var restored = 0
        Files.list(projectsDir)
            .filter { Files.isDirectory(it) }
            .filter { Files.exists(it.resolve("project.json")) }
            .forEach { projectDir ->
                try {
                    if (restoreProject(projectDir, store)) {
                        restored++
                    }
                } catch (e: Exception) {
                    logger.error(e) { "Failed to load project from $projectDir" }
                }
            }

        logger.info { "Restored $restored git project(s) from $dataDir" }
        return restored
    }

    // ===== Git operations =====

    /**
     * Initialize a new git repo for a project.
     */
    fun initProject(projectId: String): Git {
        val projectDir = projectsDir.resolve(projectId)
        Files.createDirectories(projectDir)
        val git = Git.init().setDirectory(projectDir.toFile()).call()
        logger.debug { "Initialized git repo for project $projectId" }
        return git
    }

    /**
     * Open an existing git repo for a project.
     */
    fun openRepo(projectId: String): Git {
        val projectDir = projectsDir.resolve(projectId)
        return Git.open(projectDir.toFile())
    }

    /**
     * Get the project directory path.
     */
    fun getProjectDir(projectId: String): Path = projectsDir.resolve(projectId)

    /**
     * Commit the current state of a project to git.
     *
     * @param projectId The project ID
     * @param metadata Project metadata to persist
     * @param branches All branch data
     * @param commits All commit history
     * @param files KerML file map (filename → content)
     * @param index The current ModelIndex
     * @param message Git commit message
     * @param commitUuid The SysML API commit UUID to embed in the message
     * @return The JGit RevCommit
     */
    fun commitState(
        projectId: String,
        metadata: ProjectMetadata,
        branches: List<BranchData>,
        commits: List<CommitData>,
        files: Map<String, String>,
        index: ModelIndex,
        message: String,
        commitUuid: String
    ): RevCommit {
        val projectDir = projectsDir.resolve(projectId)
        val git = if (Files.exists(projectDir.resolve(".git"))) {
            openRepo(projectId)
        } else {
            initProject(projectId)
        }

        git.use { g ->
            // Write project.json
            val state = GitPersistedProjectState(
                project = metadata,
                branches = branches,
                commits = commits.map { it.toGitPersistedCommit() }
            )
            val projectJsonPath = projectDir.resolve("project.json")
            objectMapper.writeValue(projectJsonPath.toFile(), state)

            // Write .gearshift/index.json
            val gearshiftDir = projectDir.resolve(".gearshift")
            Files.createDirectories(gearshiftDir)
            Files.writeString(gearshiftDir.resolve("index.json"), index.toJson())

            // Clean old .kerml files — remove any that are no longer in the file map
            Files.list(projectDir)
                .filter { it.fileName.toString().endsWith(".kerml") }
                .forEach { existing ->
                    if (existing.fileName.toString() !in files) {
                        Files.deleteIfExists(existing)
                    }
                }

            // Write .kerml files
            for ((name, content) in files) {
                Files.writeString(projectDir.resolve(name), content)
            }

            // Stage all changes
            g.add().addFilepattern(".").call()
            // Stage deletions
            g.add().addFilepattern(".").setUpdate(true).call()

            // Commit with embedded UUID
            val fullMessage = "[gearshift:$commitUuid] $message"
            val commit = g.commit()
                .setMessage(fullMessage)
                .setAuthor("Gearshift", "gearshift@openmbee.org")
                .call()

            logger.debug { "Git commit ${commit.name.take(7)} for project $projectId: $message" }
            return commit
        }
    }

    // ===== Branch operations =====

    fun createBranch(projectId: String, branchName: String, startPoint: String? = null): Ref {
        openRepo(projectId).use { git ->
            val cmd = git.branchCreate().setName(branchName)
            if (startPoint != null) {
                cmd.setStartPoint(startPoint)
            }
            val ref = cmd.call()
            logger.debug { "Created branch '$branchName' in project $projectId" }
            return ref
        }
    }

    fun deleteBranch(projectId: String, branchName: String) {
        openRepo(projectId).use { git ->
            git.branchDelete().setBranchNames(branchName).setForce(true).call()
            logger.debug { "Deleted branch '$branchName' in project $projectId" }
        }
    }

    fun listBranches(projectId: String): List<Ref> {
        openRepo(projectId).use { git ->
            return git.branchList().call()
        }
    }

    fun checkoutBranch(projectId: String, branchName: String) {
        openRepo(projectId).use { git ->
            git.checkout().setName(branchName).call()
            logger.debug { "Checked out branch '$branchName' in project $projectId" }
        }
    }

    // ===== Merge =====

    fun merge(projectId: String, sourceBranch: String): MergeResult {
        openRepo(projectId).use { git ->
            val sourceRef = git.repository.resolve("refs/heads/$sourceBranch")
                ?: throw IllegalArgumentException("Branch not found: $sourceBranch")
            val result = git.merge().include(sourceRef).call()
            logger.debug { "Merge '$sourceBranch' into current branch: ${result.mergeStatus}" }
            return result
        }
    }

    // ===== File reading =====

    /**
     * Read all .kerml files from the working directory of a project.
     */
    fun readFilesFromWorkingDir(projectId: String): Map<String, String> {
        val projectDir = projectsDir.resolve(projectId)
        val files = mutableMapOf<String, String>()
        if (!Files.isDirectory(projectDir)) return files

        Files.list(projectDir)
            .filter { it.fileName.toString().endsWith(".kerml") }
            .forEach { path ->
                files[path.fileName.toString()] = Files.readString(path)
            }

        return files
    }

    /**
     * Read all .kerml files at a specific git ref (branch name or commit SHA).
     */
    fun readFilesAtRef(projectId: String, ref: String): Map<String, String> {
        val files = mutableMapOf<String, String>()
        openRepo(projectId).use { git ->
            val repo = git.repository
            val commitId = repo.resolve(ref) ?: return files
            val revWalk = org.eclipse.jgit.revwalk.RevWalk(repo)
            val commit = revWalk.parseCommit(commitId)
            val tree = commit.tree

            val treeWalk = TreeWalk(repo)
            treeWalk.addTree(tree)
            treeWalk.isRecursive = true

            while (treeWalk.next()) {
                val path = treeWalk.pathString
                if (path.endsWith(".kerml")) {
                    val loader = repo.open(treeWalk.getObjectId(0))
                    files[path] = String(loader.bytes, Charsets.UTF_8)
                }
            }

            treeWalk.close()
            revWalk.close()
        }
        return files
    }

    /**
     * Read the ModelIndex from the working directory.
     */
    fun readIndexFromWorkingDir(projectId: String): ModelIndex? {
        val indexPath = projectsDir.resolve(projectId).resolve(".gearshift/index.json")
        if (!Files.exists(indexPath)) return null
        return ModelIndex.fromJson(Files.readString(indexPath))
    }

    /**
     * Read the ModelIndex at a specific git ref.
     */
    fun readIndexAtRef(projectId: String, ref: String): ModelIndex? {
        openRepo(projectId).use { git ->
            val repo = git.repository
            val commitId = repo.resolve(ref) ?: return null
            val revWalk = org.eclipse.jgit.revwalk.RevWalk(repo)
            val commit = revWalk.parseCommit(commitId)
            val tree = commit.tree

            val treeWalk = TreeWalk.forPath(repo, ".gearshift/index.json", tree) ?: return null
            val loader = repo.open(treeWalk.getObjectId(0))
            val json = String(loader.bytes, Charsets.UTF_8)

            treeWalk.close()
            revWalk.close()

            return ModelIndex.fromJson(json)
        }
    }

    // ===== Internal =====

    private fun restoreProject(projectDir: Path, store: ProjectStore): Boolean {
        val state = objectMapper.readValue<GitPersistedProjectState>(
            projectDir.resolve("project.json").toFile()
        )

        // Read KerML files from working directory
        val projectId = state.project.id
        val files = readFilesFromWorkingDir(projectId)
        val kermlSource = files.values.joinToString("\n\n")

        // Read index
        val index = readIndexFromWorkingDir(projectId)

        val commitHistory = state.commits.map { pc ->
            CommitData(
                id = pc.id,
                description = pc.description,
                owningProject = pc.owningProject,
                previousCommit = pc.previousCommit,
                created = pc.created
            )
        }

        // Use the first branch as the default (main)
        val defaultBranch = state.branches.firstOrNull() ?: return false

        val success = store.restoreProject(
            project = state.project,
            branch = defaultBranch,
            commitHistory = commitHistory,
            kermlSource = kermlSource
        )

        if (success) {
            logger.info { "Restored git project '${state.project.name}' (${state.project.id})" }
        }

        return success
    }

    private fun CommitData.toGitPersistedCommit() = GitPersistedCommit(
        id = id,
        description = description,
        owningProject = owningProject,
        previousCommit = previousCommit,
        created = created
    )
}
