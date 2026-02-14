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

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.mdm.framework.runtime.BranchData
import org.openmbee.mdm.framework.runtime.CommitData
import org.openmbee.mdm.framework.runtime.DataVersionData
import org.openmbee.mdm.framework.runtime.MDMObject
import org.openmbee.mdm.framework.runtime.MountableEngine
import org.openmbee.mdm.framework.runtime.ProjectMetadata
import org.openmbee.gearshift.kerml.KerMLModel
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

private val logger = KotlinLogging.logger {}

/**
 * Result of an element mutation (create, update, or delete).
 */
data class ElementMutationResult(
    val commit: CommitData,
    val elementIds: List<String>
)

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
    private val enableMounts: Boolean = false,
    private val backend: FileProjectBackend? = null
) {
    private val projects = ConcurrentHashMap<String, ProjectMetadata>()
    private val branches = ConcurrentHashMap<String, BranchData>()
    private val commits = ConcurrentHashMap<String, CommitData>()
    private val projectModels = ConcurrentHashMap<String, KerMLModel>()

    // Index: projectId -> list of commitIds (in order)
    private val projectCommits = ConcurrentHashMap<String, MutableList<String>>()
    // Index: projectId -> default branchId
    private val projectDefaultBranch = ConcurrentHashMap<String, String>()
    // Tracks the current KerML source text per project for persistence
    private val projectKermlSource = ConcurrentHashMap<String, String>()

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

        backend?.saveProject(projectId, this)
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
        backend?.saveProject(projectId, this)
        return updated
    }

    fun deleteProject(projectId: String): Boolean {
        if (projects.remove(projectId) == null) return false
        projectCommits.remove(projectId)?.forEach { commits.remove(it) }
        branches.values.filter { it.owningProject == projectId }.forEach { branches.remove(it.id) }
        projectDefaultBranch.remove(projectId)
        projectModels.remove(projectId)
        projectKermlSource.remove(projectId)
        backend?.deleteProject(projectId)
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

        // Reset and parse — kermlText becomes the authoritative source
        model.reset()
        model.modelRoot.setProperty("declaredName", "model")
        model.parseString(kermlText)
        projectKermlSource[projectId] = kermlText

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

        backend?.saveProject(projectId, this)
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

        backend?.saveProject(projectId, this)
        return commit
    }

    // === Model Access ===

    fun getModel(projectId: String): KerMLModel? = projectModels[projectId]

    fun getKermlSource(projectId: String): String = projectKermlSource[projectId] ?: ""

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

    // === Element Mutations ===

    /**
     * Add elements by parsing a KerML text fragment additively into the live model.
     * Returns the newly created element IDs and a new commit.
     */
    fun addElementsFromKerML(projectId: String, kermlText: String): ElementMutationResult? {
        projects[projectId] ?: return null
        val model = projectModels[projectId] ?: return null
        val branch = getDefaultBranch(projectId) ?: return null

        // Snapshot existing element IDs before parsing
        val existingIds = getLocalElementIds(model)

        // Parse additively (no reset) — parser creates all elements + implied relationships
        val parseResult = model.parseString(kermlText)
        if (parseResult == null) {
            logger.warn { "Failed to parse KerML fragment for project $projectId" }
            return null
        }
        // Append fragment to tracked source
        val existing = projectKermlSource[projectId] ?: ""
        projectKermlSource[projectId] = if (existing.isBlank()) kermlText else "$existing\n$kermlText"

        // Diff to find newly created elements
        val currentIds = getLocalElementIds(model)
        val newIds = currentIds - existingIds

        if (newIds.isEmpty()) {
            logger.debug { "KerML fragment parsed but no new elements were created" }
        }

        // Snapshot newly created elements as DataVersions
        val changes = newIds.mapNotNull { id ->
            val element = model.engine.getElement(id) ?: return@mapNotNull null
            DataVersionData(
                id = UUID.randomUUID().toString(),
                identityId = id,
                payload = element.getAllProperties() + mapOf("@type" to element.className)
            )
        }

        // Create commit and advance branch head
        val commit = createCommitInternal(projectId, branch, "Add elements from KerML", changes)
        return ElementMutationResult(commit, newIds.toList())
    }

    /**
     * Update primitive properties on an existing element.
     * Only non-derived, non-readOnly primitive attributes are allowed.
     *
     * @return mutation result, or null if project/element not found
     * @throws IllegalArgumentException if a property is invalid or not writable
     */
    fun updateElementProperties(
        projectId: String,
        elementId: String,
        properties: Map<String, Any?>
    ): ElementMutationResult? {
        projects[projectId] ?: return null
        val model = projectModels[projectId] ?: return null
        val branch = getDefaultBranch(projectId) ?: return null

        val element = model.engine.getElement(elementId)
            ?: throw NoSuchElementException("Element not found: $elementId")

        val metaClass = element.metaClass
        val registry = model.engine.schema
        val allClassNames = registry.getAllSuperclasses(metaClass.name) + metaClass.name

        // Collect all writable primitive attributes across the class hierarchy
        val writableProps = mutableMapOf<String, org.openmbee.mdm.framework.meta.MetaProperty>()
        for (className in allClassNames) {
            val cls = registry.getClass(className) ?: continue
            for (prop in cls.attributes) {
                if (!prop.isDerived && !prop.isReadOnly && prop.name !in writableProps) {
                    writableProps[prop.name] = prop
                }
            }
        }

        // Validate and apply each property
        for ((propName, value) in properties) {
            val metaProp = writableProps[propName]
                ?: throw IllegalArgumentException(
                    "Property '$propName' is not a writable primitive attribute on ${metaClass.name}. " +
                        "Writable properties: ${writableProps.keys.sorted()}"
                )

            // Type-check the value against the MetaProperty type
            validatePropertyValue(metaProp, value)

            model.engine.setPropertyValue(element, propName, value)
        }

        // Snapshot updated element
        val changes = listOf(
            DataVersionData(
                id = UUID.randomUUID().toString(),
                identityId = elementId,
                payload = element.getAllProperties() + mapOf("@type" to element.className)
            )
        )

        val commit = createCommitInternal(projectId, branch, "Update element $elementId", changes)
        return ElementMutationResult(commit, listOf(elementId))
    }

    /**
     * Delete an element and all its owned/composite children.
     * Returns the IDs of all deleted elements.
     */
    fun deleteElement(projectId: String, elementId: String): ElementMutationResult? {
        projects[projectId] ?: return null
        val model = projectModels[projectId] ?: return null
        val branch = getDefaultBranch(projectId) ?: return null

        model.engine.getElement(elementId)
            ?: throw NoSuchElementException("Element not found: $elementId")

        // Cascade delete — removes element + composite children
        val deletedIds = model.engine.deleteInstanceWithCascade(elementId)

        // Record deletions as DataVersions with null payload (tombstones)
        val changes = deletedIds.map { id ->
            DataVersionData(
                id = UUID.randomUUID().toString(),
                identityId = id,
                payload = null
            )
        }

        val commit = createCommitInternal(projectId, branch, "Delete element $elementId", changes)
        return ElementMutationResult(commit, deletedIds)
    }

    // === Internal helpers ===

    private fun getLocalElementIds(model: KerMLModel): Set<String> {
        val engine = model.engine
        val elements = if (engine is MountableEngine) {
            engine.getLocalElements()
        } else {
            engine.getAllElements()
        }
        return elements.mapNotNull { it.id }.toSet()
    }

    private fun createCommitInternal(
        projectId: String,
        branch: BranchData,
        description: String,
        changes: List<DataVersionData>
    ): CommitData {
        val commitId = UUID.randomUUID().toString()
        val commit = CommitData(
            id = commitId,
            description = description,
            owningProject = projectId,
            previousCommit = listOfNotNull(branch.headCommitId),
            changes = changes
        )
        commits[commitId] = commit
        projectCommits.getOrPut(projectId) { mutableListOf() }.add(commitId)
        branches[branch.id] = branch.copy(headCommitId = commitId)
        backend?.saveProject(projectId, this)
        return commit
    }

    private fun validatePropertyValue(metaProp: org.openmbee.mdm.framework.meta.MetaProperty, value: Any?) {
        if (value == null) return // null is always allowed (clears the property)
        when (metaProp.type) {
            "String" -> if (value !is String) {
                throw IllegalArgumentException("Property '${metaProp.name}' expects String, got ${value::class.simpleName}")
            }
            "Boolean" -> if (value !is Boolean) {
                throw IllegalArgumentException("Property '${metaProp.name}' expects Boolean, got ${value::class.simpleName}")
            }
            "Integer" -> if (value !is Number) {
                throw IllegalArgumentException("Property '${metaProp.name}' expects Integer, got ${value::class.simpleName}")
            }
            "Real", "UnlimitedNatural" -> if (value !is Number) {
                throw IllegalArgumentException("Property '${metaProp.name}' expects Number, got ${value::class.simpleName}")
            }
        }
    }

    // === Restore from persistence ===

    /**
     * Restore a project from persisted state.
     * Called by [FileProjectBackend.loadAll] during startup.
     *
     * Creates a fresh KerMLModel and parses the stored KerML source
     * to reconstruct the live model state.
     */
    fun restoreProject(
        project: ProjectMetadata,
        branch: BranchData,
        commitHistory: List<CommitData>,
        kermlSource: String
    ): Boolean {
        return try {
            // Create a fresh model and parse the stored KerML source
            val model = createModel(project.id, project.name, project.description)
            if (kermlSource.isNotBlank()) {
                val result = model.parseString(kermlSource)
                if (result == null) {
                    logger.warn { "Failed to parse stored KerML for project '${project.name}'" }
                    return false
                }
            }

            // Populate in-memory maps
            projects[project.id] = project
            projectModels[project.id] = model
            if (kermlSource.isNotBlank()) {
                projectKermlSource[project.id] = kermlSource
            }

            branches[branch.id] = branch
            projectDefaultBranch[project.id] = branch.id

            val commitIds = mutableListOf<String>()
            for (commit in commitHistory) {
                commits[commit.id] = commit
                commitIds.add(commit.id)
            }
            projectCommits[project.id] = commitIds

            true
        } catch (e: Exception) {
            logger.error(e) { "Failed to restore project '${project.name}'" }
            false
        }
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
