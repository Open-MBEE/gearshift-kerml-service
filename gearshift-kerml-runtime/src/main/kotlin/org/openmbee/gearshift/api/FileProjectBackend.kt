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

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.mdm.framework.runtime.BranchData
import org.openmbee.mdm.framework.runtime.CommitData
import org.openmbee.mdm.framework.runtime.ProjectMetadata
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

private val logger = KotlinLogging.logger {}

/**
 * Persisted project state — written to project.json.
 * Commits are stored without their DataVersionData changes to keep files small.
 */
data class PersistedProjectState(
    val project: ProjectMetadata,
    val branch: BranchData,
    val commits: List<PersistedCommit>
)

/**
 * Commit metadata without the changes payload.
 */
data class PersistedCommit(
    val id: String,
    val description: String?,
    val owningProject: String,
    val previousCommit: List<String> = emptyList(),
    val created: java.time.Instant = java.time.Instant.now()
)

/**
 * File-based persistence backend for ProjectStore.
 *
 * Storage format:
 * ```
 * {dataDir}/projects/{projectId}/
 * ├── project.json    # Project metadata, branch, commit list
 * └── model.kerml     # Current model state as KerML source
 * ```
 *
 * After every mutation, [saveProject] writes the current state to disk.
 * On startup, [loadAll] scans the directory and restores all projects.
 */
class FileProjectBackend(
    private val dataDir: Path
) {
    private val objectMapper: ObjectMapper = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule())
        enable(SerializationFeature.INDENT_OUTPUT)
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    private val projectsDir: Path = dataDir.resolve("projects")

    init {
        Files.createDirectories(projectsDir)
        logger.info { "File persistence enabled at $dataDir" }
    }

    /**
     * Save a project's current state to disk.
     * Uses atomic writes (temp file + move) to prevent corruption.
     */
    fun saveProject(projectId: String, store: ProjectStore) {
        try {
            val project = store.getProject(projectId) ?: return
            val branch = store.getDefaultBranch(projectId) ?: return
            val commits = store.getCommitsForProject(projectId)
            val model = store.getModel(projectId)

            val projectDir = projectsDir.resolve(projectId)
            Files.createDirectories(projectDir)

            // Serialize metadata
            val state = PersistedProjectState(
                project = project,
                branch = branch,
                commits = commits.map { it.toPersistedCommit() }
            )
            writeAtomically(projectDir.resolve("project.json")) { path ->
                objectMapper.writeValue(path.toFile(), state)
            }

            // Write the tracked KerML source text
            val kermlSource = store.getKermlSource(projectId)
            writeAtomically(projectDir.resolve("model.kerml")) { path ->
                Files.writeString(path, kermlSource)
            }

            logger.debug { "Saved project $projectId (${commits.size} commits)" }
        } catch (e: Exception) {
            logger.error(e) { "Failed to save project $projectId" }
        }
    }

    /**
     * Delete a project's persisted state from disk.
     */
    fun deleteProject(projectId: String) {
        try {
            val projectDir = projectsDir.resolve(projectId)
            if (Files.exists(projectDir)) {
                // Delete directory contents then the directory itself
                Files.walk(projectDir)
                    .sorted(Comparator.reverseOrder())
                    .forEach { Files.deleteIfExists(it) }
                logger.info { "Deleted persisted project $projectId" }
            }
        } catch (e: Exception) {
            logger.error(e) { "Failed to delete persisted project $projectId" }
        }
    }

    /**
     * Load all persisted projects into the store.
     * Called once at startup.
     *
     * @return Number of projects successfully restored
     */
    fun loadAll(store: ProjectStore): Int {
        if (!Files.isDirectory(projectsDir)) {
            return 0
        }

        var restored = 0
        Files.list(projectsDir)
            .filter { Files.isDirectory(it) }
            .filter { Files.exists(it.resolve("project.json")) }
            .forEach { projectDir ->
                try {
                    val state = objectMapper.readValue<PersistedProjectState>(
                        projectDir.resolve("project.json").toFile()
                    )

                    val kermlSource = projectDir.resolve("model.kerml").let { path ->
                        if (Files.exists(path)) Files.readString(path) else ""
                    }

                    val commitHistory = state.commits.map { pc ->
                        CommitData(
                            id = pc.id,
                            description = pc.description,
                            owningProject = pc.owningProject,
                            previousCommit = pc.previousCommit,
                            created = pc.created
                        )
                    }

                    val success = store.restoreProject(
                        project = state.project,
                        branch = state.branch,
                        commitHistory = commitHistory,
                        kermlSource = kermlSource
                    )

                    if (success) {
                        restored++
                        logger.info { "Restored project '${state.project.name}' (${state.project.id})" }
                    } else {
                        logger.warn { "Failed to restore project '${state.project.name}'" }
                    }
                } catch (e: Exception) {
                    logger.error(e) { "Failed to load project from $projectDir" }
                }
            }

        logger.info { "Restored $restored project(s) from $dataDir" }
        return restored
    }

    /**
     * Write a file atomically: write to a temp file, then rename.
     */
    private fun writeAtomically(target: Path, writeAction: (Path) -> Unit) {
        val temp = target.resolveSibling("${target.fileName}.tmp")
        try {
            writeAction(temp)
            Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE)
        } catch (e: Exception) {
            // ATOMIC_MOVE not supported on all filesystems — fall back to plain move
            try {
                Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING)
            } catch (fallback: Exception) {
                Files.deleteIfExists(temp)
                throw e
            }
        }
    }

    private fun CommitData.toPersistedCommit() = PersistedCommit(
        id = id,
        description = description,
        owningProject = owningProject,
        previousCommit = previousCommit,
        created = created
    )
}
