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

import org.openmbee.mdm.framework.runtime.BranchData
import org.openmbee.mdm.framework.runtime.CommitData
import org.openmbee.mdm.framework.runtime.DataVersionData
import org.openmbee.mdm.framework.runtime.MDMObject
import org.openmbee.mdm.framework.runtime.ProjectMetadata
import org.openmbee.gearshift.kerml.KerMLModel
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

/**
 * In-memory multi-project store implementing the SysML v2 version control model.
 *
 * Each project has:
 * - Metadata (id, name, description)
 * - A default "main" branch
 * - A commit history
 * - A live materialized KerMLModel (the current state)
 *
 * On project creation, an initial empty commit and "main" branch are auto-created.
 * On commit creation, KerML text is parsed and element state is snapshotted.
 */
class ProjectStore(
    private val enableMounts: Boolean = false
) {
    private val projects = ConcurrentHashMap<String, ProjectMetadata>()
    private val branches = ConcurrentHashMap<String, BranchData>()
    private val commits = ConcurrentHashMap<String, CommitData>()
    private val projectModels = ConcurrentHashMap<String, KerMLModel>()

    // Index: projectId -> list of commitIds (in order)
    private val projectCommits = ConcurrentHashMap<String, MutableList<String>>()
    // Index: projectId -> default branchId
    private val projectDefaultBranch = ConcurrentHashMap<String, String>()

    // === Project CRUD ===

    fun createProject(name: String, description: String? = null): ProjectMetadata {
        val projectId = UUID.randomUUID().toString()
        val project = ProjectMetadata(
            id = projectId,
            name = name,
            description = description
        )
        projects[projectId] = project

        // Create initial empty commit
        val commitId = UUID.randomUUID().toString()
        val commit = CommitData(
            id = commitId,
            description = "Initial empty commit",
            owningProject = projectId
        )
        commits[commitId] = commit
        projectCommits[projectId] = mutableListOf(commitId)

        // Create the default "main" branch
        val branchId = UUID.randomUUID().toString()
        val branch = BranchData(
            id = branchId,
            name = "main",
            owningProject = projectId,
            headCommitId = commitId
        )
        branches[branchId] = branch
        projectDefaultBranch[projectId] = branchId

        // Create a live KerMLModel for this project
        val model = createModel(projectId, name, description)
        projectModels[projectId] = model

        return project
    }

    fun getProject(projectId: String): ProjectMetadata? = projects[projectId]

    fun getAllProjects(): List<ProjectMetadata> = projects.values.toList()

    fun updateProject(projectId: String, name: String?, description: String?): ProjectMetadata? {
        val existing = projects[projectId] ?: return null
        val updated = existing.copy(
            name = name ?: existing.name,
            description = description ?: existing.description
        )
        projects[projectId] = updated
        return updated
    }

    fun deleteProject(projectId: String): Boolean {
        if (projects.remove(projectId) == null) return false
        projectCommits.remove(projectId)?.forEach { commits.remove(it) }
        branches.values.filter { it.owningProject == projectId }.forEach { branches.remove(it.id) }
        projectDefaultBranch.remove(projectId)
        projectModels.remove(projectId)
        return true
    }

    // === Branch Access ===

    fun getDefaultBranch(projectId: String): BranchData? {
        val branchId = projectDefaultBranch[projectId] ?: return null
        return branches[branchId]
    }

    // === Commit CRUD ===

    fun getCommit(commitId: String): CommitData? = commits[commitId]

    fun getCommitsForProject(projectId: String): List<CommitData> {
        val commitIds = projectCommits[projectId] ?: return emptyList()
        return commitIds.mapNotNull { commits[it] }
    }

    /**
     * Create a new commit with KerML text payload.
     * Parses the KerML, updates the live model, and snapshots element state as DataVersions.
     */
    fun createCommitWithKerML(
        projectId: String,
        description: String?,
        kermlText: String
    ): CommitData? {
        projects[projectId] ?: return null
        val model = projectModels[projectId] ?: return null
        val branch = getDefaultBranch(projectId) ?: return null
        val previousCommitId = branch.headCommitId

        // Reset and parse
        model.reset()
        model.modelRoot.setProperty("declaredName", "model")
        model.parseString(kermlText)

        // Snapshot current element state as DataVersions
        val elements = model.engine.getAllElements()
        val changes = elements.mapNotNull { element ->
            val elementId = element.id ?: return@mapNotNull null
            DataVersionData(
                id = UUID.randomUUID().toString(),
                identityId = elementId,
                payload = element.getAllProperties() + mapOf(
                    "@type" to element.className
                )
            )
        }

        val commitId = UUID.randomUUID().toString()
        val commit = CommitData(
            id = commitId,
            description = description,
            owningProject = projectId,
            previousCommit = listOfNotNull(previousCommitId),
            changes = changes
        )
        commits[commitId] = commit
        projectCommits.getOrPut(projectId) { mutableListOf() }.add(commitId)

        // Update branch head
        branches[branch.id] = branch.copy(headCommitId = commitId)

        return commit
    }

    /**
     * Create a commit with pre-built DataVersion changes.
     */
    fun createCommitWithChanges(
        projectId: String,
        description: String?,
        changes: List<DataVersionData>
    ): CommitData? {
        projects[projectId] ?: return null
        val branch = getDefaultBranch(projectId) ?: return null
        val previousCommitId = branch.headCommitId

        val commitId = UUID.randomUUID().toString()
        val commit = CommitData(
            id = commitId,
            description = description,
            owningProject = projectId,
            previousCommit = listOfNotNull(previousCommitId),
            changes = changes
        )
        commits[commitId] = commit
        projectCommits.getOrPut(projectId) { mutableListOf() }.add(commitId)

        // Update branch head
        branches[branch.id] = branch.copy(headCommitId = commitId)

        return commit
    }

    // === Model Access ===

    fun getModel(projectId: String): KerMLModel? = projectModels[projectId]

    /**
     * Get all elements at a specific commit.
     * Returns the live model state (linear history on main branch).
     */
    fun getElementsAtCommit(projectId: String, commitId: String): List<MDMObject>? {
        val commit = commits[commitId] ?: return null
        if (commit.owningProject != projectId) return null
        val model = projectModels[projectId] ?: return null
        return model.engine.getAllElements()
    }

    fun getElementAtCommit(projectId: String, commitId: String, elementId: String): MDMObject? {
        val commit = commits[commitId] ?: return null
        if (commit.owningProject != projectId) return null
        val model = projectModels[projectId] ?: return null
        return model.engine.getElement(elementId)
    }

    private fun createModel(
        projectId: String,
        name: String,
        description: String?
    ): KerMLModel {
        return if (enableMounts) {
            KerMLModel.initializeKernelLibrary()
            KerMLModel.createWithMounts(
                projectId = projectId,
                projectName = name,
                projectDescription = description
            )
        } else {
            KerMLModel(
                projectId = projectId,
                projectName = name,
                projectDescription = description
            )
        }
    }
}
