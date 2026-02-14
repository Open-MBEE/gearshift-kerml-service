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

import org.openmbee.gearshift.kerml.index.ModelIndex
import org.openmbee.mdm.framework.runtime.BranchData
import org.openmbee.mdm.framework.runtime.CommitData
import org.openmbee.mdm.framework.runtime.ProjectMetadata

/**
 * Persisted project data returned during startup restore.
 */
data class PersistedProject(
    val project: ProjectMetadata,
    val branches: List<BranchData>,
    val commits: List<CommitData>,
    val files: Map<String, String>,
    val index: ModelIndex?
)

/**
 * Backend interface for project persistence.
 *
 * Implementations handle the actual storage mechanism (file system, git, etc.)
 * while [ProjectStore] manages the in-memory model state.
 */
interface ProjectBackend {
    fun saveProject(projectId: String, store: ProjectStore)
    fun deleteProject(projectId: String)
    fun loadAll(store: ProjectStore): Int
}
