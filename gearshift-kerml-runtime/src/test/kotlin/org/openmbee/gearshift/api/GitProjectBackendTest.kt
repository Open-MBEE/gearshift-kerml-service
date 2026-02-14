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

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.eclipse.jgit.api.Git
import org.openmbee.gearshift.kerml.KerMLTestSpec
import org.openmbee.gearshift.kerml.index.ModelIndex
import org.openmbee.mdm.framework.runtime.BranchData
import org.openmbee.mdm.framework.runtime.CommitData
import org.openmbee.mdm.framework.runtime.ProjectMetadata
import java.nio.file.Files
import java.util.UUID

class GitProjectBackendTest : KerMLTestSpec({

    lateinit var tmpDir: java.nio.file.Path
    lateinit var backend: GitProjectBackend

    beforeEach {
        tmpDir = Files.createTempDirectory("git-backend-test")
        backend = GitProjectBackend(tmpDir)
    }

    afterEach {
        Files.walk(tmpDir)
            .sorted(Comparator.reverseOrder())
            .forEach { Files.deleteIfExists(it) }
    }

    describe("GitProjectBackend") {

        it("should init repo and commit files") {
            val projectId = UUID.randomUUID().toString()
            val metadata = ProjectMetadata(id = projectId, name = "Test Project")
            val branchId = UUID.randomUUID().toString()
            val commitUuid = UUID.randomUUID().toString()
            val branch = BranchData(id = branchId, name = "main", owningProject = projectId, headCommitId = null)
            val commit = CommitData(id = commitUuid, description = "Initial", owningProject = projectId)

            val files = mapOf(
                "Vehicles.kerml" to "package Vehicles { class Vehicle; }"
            )
            val index = ModelIndex()
            index.put("id-1", "Vehicles::Vehicle")

            val revCommit = backend.commitState(
                projectId = projectId,
                metadata = metadata,
                branches = listOf(branch),
                commits = listOf(commit),
                files = files,
                index = index,
                message = "Initial commit",
                commitUuid = commitUuid
            )

            // Verify git log
            val git = backend.openRepo(projectId)
            git.use { g ->
                val log = g.log().call().toList()
                log shouldHaveSize 1
                log[0].fullMessage shouldContain commitUuid
                log[0].fullMessage shouldContain "Initial commit"
            }
        }

        it("should read files from working directory") {
            val projectId = UUID.randomUUID().toString()
            val metadata = ProjectMetadata(id = projectId, name = "Test")
            val commitUuid = UUID.randomUUID().toString()
            val branch = BranchData(id = "b1", name = "main", owningProject = projectId, headCommitId = null)
            val commit = CommitData(id = commitUuid, description = "Init", owningProject = projectId)

            val files = mapOf(
                "Vehicles.kerml" to "package Vehicles { class Vehicle; }",
                "Requirements.kerml" to "package Requirements { class Req; }"
            )
            val index = ModelIndex()

            backend.commitState(projectId, metadata, listOf(branch), listOf(commit), files, index, "Init", commitUuid)

            val readFiles = backend.readFilesFromWorkingDir(projectId)
            readFiles shouldHaveSize 2
            readFiles shouldContainKey "Vehicles.kerml"
            readFiles shouldContainKey "Requirements.kerml"
            readFiles["Vehicles.kerml"]!! shouldContain "class Vehicle"
        }

        it("should read files at a specific commit") {
            val projectId = UUID.randomUUID().toString()
            val metadata = ProjectMetadata(id = projectId, name = "Test")
            val branch = BranchData(id = "b1", name = "main", owningProject = projectId, headCommitId = null)

            // First commit
            val commit1Uuid = UUID.randomUUID().toString()
            val commit1 = CommitData(id = commit1Uuid, description = "v1", owningProject = projectId)
            val rev1 = backend.commitState(
                projectId, metadata, listOf(branch), listOf(commit1),
                mapOf("Vehicles.kerml" to "package Vehicles { class Vehicle; }"),
                ModelIndex(), "v1", commit1Uuid
            )

            // Second commit with different content
            val commit2Uuid = UUID.randomUUID().toString()
            val commit2 = CommitData(id = commit2Uuid, description = "v2", owningProject = projectId)
            backend.commitState(
                projectId, metadata, listOf(branch), listOf(commit1, commit2),
                mapOf("Vehicles.kerml" to "package Vehicles { class Vehicle; class Car; }"),
                ModelIndex(), "v2", commit2Uuid
            )

            // Read files at first commit
            val filesAtV1 = backend.readFilesAtRef(projectId, rev1.name)
            filesAtV1["Vehicles.kerml"]!! shouldContain "class Vehicle"
            // v1 should NOT have Car
            (filesAtV1["Vehicles.kerml"]!!.contains("Car")) shouldBe false
        }

        it("should create branch and verify isolation") {
            val projectId = UUID.randomUUID().toString()
            val metadata = ProjectMetadata(id = projectId, name = "Test")
            val branch = BranchData(id = "b1", name = "main", owningProject = projectId, headCommitId = null)
            val commitUuid = UUID.randomUUID().toString()
            val commit = CommitData(id = commitUuid, description = "Init", owningProject = projectId)

            // Initial commit on main
            backend.commitState(
                projectId, metadata, listOf(branch), listOf(commit),
                mapOf("Vehicles.kerml" to "package Vehicles { class Vehicle; }"),
                ModelIndex(), "Init", commitUuid
            )

            // Create a feature branch
            backend.createBranch(projectId, "feature-1")

            val branches = backend.listBranches(projectId)
            branches.map { it.name.removePrefix("refs/heads/") }.toSet() shouldBe setOf("feature-1", "main")
        }

        it("should commit index.json alongside kerml files") {
            val projectId = UUID.randomUUID().toString()
            val metadata = ProjectMetadata(id = projectId, name = "Test")
            val branch = BranchData(id = "b1", name = "main", owningProject = projectId, headCommitId = null)
            val commitUuid = UUID.randomUUID().toString()
            val commit = CommitData(id = commitUuid, description = "Init", owningProject = projectId)

            val index = ModelIndex()
            index.put("id-1", "Vehicles::Vehicle")
            index.put("id-2", "Vehicles::Car")

            backend.commitState(
                projectId, metadata, listOf(branch), listOf(commit),
                mapOf("Vehicles.kerml" to "package Vehicles { class Vehicle; class Car; }"),
                index, "Init", commitUuid
            )

            // Read index from working dir
            val readIndex = backend.readIndexFromWorkingDir(projectId)
            readIndex.shouldNotBeNull()
            readIndex.size() shouldBe 2
            readIndex.getQn("id-1") shouldBe "Vehicles::Vehicle"
            readIndex.getQn("id-2") shouldBe "Vehicles::Car"

            // Read index at HEAD via git
            val gitIndex = backend.readIndexAtRef(projectId, "HEAD")
            gitIndex.shouldNotBeNull()
            gitIndex.size() shouldBe 2
        }
    }
})
