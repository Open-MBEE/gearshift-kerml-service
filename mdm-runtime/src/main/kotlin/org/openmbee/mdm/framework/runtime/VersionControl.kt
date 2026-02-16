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
package org.openmbee.mdm.framework.runtime

import java.time.Instant

/**
 * Branch within a project, aligned with SysML v2 API Branch schema.
 */
data class BranchData(
    val id: String,
    val name: String,
    val owningProject: String,
    val headCommitId: String?,
    val created: Instant = Instant.now()
) {
    companion object {
        const val TYPE = "Branch"
    }
}

/**
 * Commit — a snapshot of model state within a project.
 * Aligned with SysML v2 API Commit schema.
 */
data class CommitData(
    val id: String,
    val description: String?,
    val owningProject: String,
    val previousCommit: List<String> = emptyList(),
    val created: Instant = Instant.now(),
    val changes: List<DataVersionData> = emptyList()
) {
    companion object {
        const val TYPE = "Commit"
    }
}

/**
 * DataVersion — a versioned change to a single element within a commit.
 * A null payload indicates deletion.
 */
data class DataVersionData(
    val id: String,
    val identityId: String,
    val payload: Map<String, Any?>?
) {
    companion object {
        const val TYPE = "DataVersion"
    }
}
