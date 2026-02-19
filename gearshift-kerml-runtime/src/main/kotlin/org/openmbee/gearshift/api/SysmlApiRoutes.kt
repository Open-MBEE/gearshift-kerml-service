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

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.openmbee.mdm.framework.runtime.BranchData
import org.openmbee.mdm.framework.runtime.CommitData
import org.openmbee.mdm.framework.runtime.DataVersionData
import org.openmbee.mdm.framework.runtime.ElementSerializer
import org.openmbee.mdm.framework.runtime.MountableEngine
import org.openmbee.mdm.framework.runtime.SerializationMode
import org.openmbee.mdm.framework.runtime.ProjectMetadata
import java.util.UUID

// === Request DTOs ===

data class CreateProjectRequest(
    val name: String,
    val description: String? = null
)

data class UpdateProjectRequest(
    val name: String? = null,
    val description: String? = null
)

data class CreateCommitRequest(
    val description: String? = null,
    val kerml: String? = null,
    val change: List<DataVersionInput>? = null
)

data class DataVersionInput(
    val identity: DataIdentityInput,
    val payload: Map<String, Any?>?
)

data class DataIdentityInput(
    val `@id`: String
)

data class CreateElementsRequest(
    val kerml: String
)

data class UpdateElementRequest(
    val properties: Map<String, Any?>
)

data class ResolveQualifiedNamesRequest(
    val qualifiedNames: List<String>
)

// === Response serialization helpers ===

private fun ProjectMetadata.toApiResponse(defaultBranch: BranchData?): Map<String, Any?> = linkedMapOf(
    "@id" to id,
    "@type" to ProjectMetadata.TYPE,
    "name" to name,
    "description" to description,
    "created" to created.toString(),
    "defaultBranch" to if (defaultBranch != null) mapOf("@id" to defaultBranch.id) else null
)

private fun BranchData.toApiResponse(): Map<String, Any?> = linkedMapOf(
    "@id" to id,
    "@type" to BranchData.TYPE,
    "name" to name,
    "created" to created.toString(),
    "owningProject" to mapOf("@id" to owningProject),
    "head" to if (headCommitId != null) mapOf("@id" to headCommitId) else null
)

private fun CommitData.toApiResponse(): Map<String, Any?> = linkedMapOf(
    "@id" to id,
    "@type" to CommitData.TYPE,
    "created" to created.toString(),
    "description" to description,
    "owningProject" to mapOf("@id" to owningProject),
    "previousCommit" to previousCommit.map { mapOf("@id" to it) }
)

private fun DataVersionData.toApiResponse(): Map<String, Any?> = linkedMapOf(
    "@id" to id,
    "@type" to DataVersionData.TYPE,
    "identity" to mapOf("@type" to "DataIdentity", "@id" to identityId),
    "payload" to payload
)

private fun errorResponse(description: String): Map<String, Any> = mapOf(
    "@type" to "Error",
    "description" to description
)

/**
 * Install SysML v2 API routes on a Ktor routing scope.
 *
 * Endpoints:
 * - GET/POST/PUT/DELETE /projects[/{projectId}]
 * - GET/POST /projects/{projectId}/commits[/{commitId}]
 * - GET /projects/{projectId}/commits/{commitId}/elements[/{elementId}]
 * - GET /projects/{projectId}/commits/{commitId}/roots
 * - GET /projects/{projectId}/commits/{commitId}/elements/{elementId}/relationships
 * - POST /projects/{projectId}/elements (create elements via KerML)
 * - PATCH /projects/{projectId}/elements/{elementId} (update primitive properties)
 * - DELETE /projects/{projectId}/elements/{elementId} (cascade delete)
 * - GET /projects/{projectId}/export
 * - POST /projects/import
 */
fun Route.sysmlApiRoutes(store: ProjectStore) {

    // === Project endpoints ===

    route("/projects") {

        get {
            val projects = store.getAllProjects().map { project ->
                project.toApiResponse(store.getDefaultBranch(project.id))
            }
            call.respond(projects)
        }

        post {
            try {
                val request = call.receive<CreateProjectRequest>()
                if (request.name.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, errorResponse("Project name is required"))
                    return@post
                }
                val project = store.createProject(request.name, request.description)
                val branch = store.getDefaultBranch(project.id)
                call.respond(HttpStatusCode.Created, project.toApiResponse(branch))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, errorResponse(e.message ?: "Invalid request"))
            }
        }

        route("/{projectId}") {

            get {
                val projectId = call.parameters["projectId"]!!
                val project = store.getProject(projectId)
                if (project == null) {
                    call.respond(HttpStatusCode.NotFound, errorResponse("Project not found: $projectId"))
                    return@get
                }
                val branch = store.getDefaultBranch(projectId)
                call.respond(project.toApiResponse(branch))
            }

            put {
                val projectId = call.parameters["projectId"]!!
                try {
                    val request = call.receive<UpdateProjectRequest>()
                    val updated = store.updateProject(projectId, request.name, request.description)
                    if (updated == null) {
                        call.respond(HttpStatusCode.NotFound, errorResponse("Project not found: $projectId"))
                        return@put
                    }
                    val branch = store.getDefaultBranch(projectId)
                    call.respond(updated.toApiResponse(branch))
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, errorResponse(e.message ?: "Invalid request"))
                }
            }

            delete {
                val projectId = call.parameters["projectId"]!!
                if (store.deleteProject(projectId)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, errorResponse("Project not found: $projectId"))
                }
            }
        }
    }

    // === Commit endpoints ===

    route("/projects/{projectId}/commits") {

        get {
            val projectId = call.parameters["projectId"]!!
            if (store.getProject(projectId) == null) {
                call.respond(HttpStatusCode.NotFound, errorResponse("Project not found: $projectId"))
                return@get
            }
            val commits = store.getCommitsForProject(projectId).map { it.toApiResponse() }
            call.respond(commits)
        }

        post {
            val projectId = call.parameters["projectId"]!!
            if (store.getProject(projectId) == null) {
                call.respond(HttpStatusCode.NotFound, errorResponse("Project not found: $projectId"))
                return@post
            }
            try {
                val request = call.receive<CreateCommitRequest>()
                val commit = if (request.kerml != null) {
                    store.createCommitWithKerML(projectId, request.description, request.kerml)
                } else if (request.change != null) {
                    val changes = request.change.map { dv ->
                        DataVersionData(
                            id = UUID.randomUUID().toString(),
                            identityId = dv.identity.`@id`,
                            payload = dv.payload
                        )
                    }
                    store.createCommitWithChanges(projectId, request.description, changes)
                } else {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        errorResponse("Either 'kerml' or 'change' must be provided")
                    )
                    return@post
                }

                if (commit == null) {
                    call.respond(HttpStatusCode.InternalServerError, errorResponse("Failed to create commit"))
                    return@post
                }
                call.respond(HttpStatusCode.Created, commit.toApiResponse())
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, errorResponse(e.message ?: "Invalid request"))
            }
        }

        get("/{commitId}") {
            val projectId = call.parameters["projectId"]!!
            val commitId = call.parameters["commitId"]!!
            val commit = store.getCommit(commitId)
            if (commit == null || commit.owningProject != projectId) {
                call.respond(HttpStatusCode.NotFound, errorResponse("Commit not found: $commitId"))
                return@get
            }
            call.respond(commit.toApiResponse())
        }
    }

    // === Element endpoints ===

    route("/projects/{projectId}/commits/{commitId}") {

        get("/elements") {
            val projectId = call.parameters["projectId"]!!
            val commitId = call.parameters["commitId"]!!

            val elements = store.getElementsAtCommit(projectId, commitId)
            if (elements == null) {
                call.respond(HttpStatusCode.NotFound, errorResponse("Project or commit not found"))
                return@get
            }

            val model = store.getModel(projectId) ?: run {
                call.respond(HttpStatusCode.NotFound, errorResponse("Model not found"))
                return@get
            }

            // Filter to local elements only (exclude mounted library elements)
            val localElements = filterLocalElements(model, elements)
            val serializer = ElementSerializer(model.engine)
            call.respond(serializer.serializeAll(localElements, SerializationMode.SUMMARY))
        }

        get("/elements/{elementId}") {
            val projectId = call.parameters["projectId"]!!
            val commitId = call.parameters["commitId"]!!
            val elementId = call.parameters["elementId"]!!

            val element = store.getElementAtCommit(projectId, commitId, elementId)
            if (element == null) {
                call.respond(HttpStatusCode.NotFound, errorResponse("Element not found: $elementId"))
                return@get
            }

            val model = store.getModel(projectId) ?: run {
                call.respond(HttpStatusCode.NotFound, errorResponse("Model not found"))
                return@get
            }

            val serializer = ElementSerializer(model.engine)
            call.respond(serializer.serialize(element))
        }

        get("/roots") {
            val projectId = call.parameters["projectId"]!!
            val commitId = call.parameters["commitId"]!!

            val elements = store.getElementsAtCommit(projectId, commitId)
            if (elements == null) {
                call.respond(HttpStatusCode.NotFound, errorResponse("Project or commit not found"))
                return@get
            }

            val model = store.getModel(projectId) ?: run {
                call.respond(HttpStatusCode.NotFound, errorResponse("Model not found"))
                return@get
            }

            val rootNamespaces = model.engine.getRootNamespaces()
            val serializer = ElementSerializer(model.engine)
            call.respond(serializer.serializeAll(rootNamespaces, SerializationMode.SUMMARY))
        }

        // === Relationship endpoint ===

        get("/elements/{relatedElementId}/relationships") {
            val projectId = call.parameters["projectId"]!!
            val commitId = call.parameters["commitId"]!!
            val relatedElementId = call.parameters["relatedElementId"]!!

            val element = store.getElementAtCommit(projectId, commitId, relatedElementId)
            if (element == null) {
                call.respond(HttpStatusCode.NotFound, errorResponse("Element not found: $relatedElementId"))
                return@get
            }

            val model = store.getModel(projectId) ?: run {
                call.respond(HttpStatusCode.NotFound, errorResponse("Model not found"))
                return@get
            }

            // Get all links for this element and serialize the relationship elements
            val links = model.engine.getLinks(relatedElementId)
            val serializer = ElementSerializer(model.engine)
            val relationships = links.mapNotNull { link ->
                // Find the relationship element itself (the Membership/Relationship object)
                // For KerML, relationships are first-class elements
                val relElement = if (link.sourceId == relatedElementId) {
                    model.engine.getElement(link.targetId)
                } else {
                    model.engine.getElement(link.sourceId)
                }
                relElement?.let { serializer.serialize(it) }
            }.distinctBy { it["@id"] }

            call.respond(relationships)
        }

        // === Qualified Name Resolution ===

        get("/elements/byQualifiedName") {
            val projectId = call.parameters["projectId"]!!
            val qualifiedName = call.request.queryParameters["qualifiedName"]
                ?: return@get call.respond(HttpStatusCode.BadRequest, errorResponse("qualifiedName parameter required"))

            val model = store.getModel(projectId) ?: run {
                call.respond(HttpStatusCode.NotFound, errorResponse("Model not found"))
                return@get
            }

            val index = model.engine.qualifiedNameIndex
                ?: return@get call.respond(HttpStatusCode.ServiceUnavailable, errorResponse("QN index not built"))

            val elementId = index.resolveQualifiedName(qualifiedName)
                ?: return@get call.respond(HttpStatusCode.NotFound, errorResponse("No element with qualifiedName: $qualifiedName"))

            val element = model.engine.getElement(elementId)
                ?: return@get call.respond(HttpStatusCode.NotFound, errorResponse("Element not found: $elementId"))

            val serializer = ElementSerializer(model.engine)
            call.respond(serializer.serialize(element))
        }

        post("/elements/resolve") {
            val projectId = call.parameters["projectId"]!!

            val model = store.getModel(projectId) ?: run {
                call.respond(HttpStatusCode.NotFound, errorResponse("Model not found"))
                return@post
            }

            val index = model.engine.qualifiedNameIndex
                ?: return@post call.respond(HttpStatusCode.ServiceUnavailable, errorResponse("QN index not built"))

            val request = call.receive<ResolveQualifiedNamesRequest>()
            val serializer = ElementSerializer(model.engine)

            val results = request.qualifiedNames.mapNotNull { qn ->
                index.resolveQualifiedName(qn)?.let { id ->
                    model.engine.getElement(id)?.let { element ->
                        serializer.serialize(element)
                    }
                }
            }
            call.respond(results)
        }
    }

    // === Element mutation endpoints ===
    // Mutations operate on the live model (branch head), not a specific commit.
    // Each mutation creates a new commit and returns it in the response.

    route("/projects/{projectId}/elements") {

        post {
            val projectId = call.parameters["projectId"]!!
            if (store.getProject(projectId) == null) {
                call.respond(HttpStatusCode.NotFound, errorResponse("Project not found: $projectId"))
                return@post
            }
            try {
                val request = call.receive<CreateElementsRequest>()
                if (request.kerml.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, errorResponse("KerML text is required"))
                    return@post
                }

                val result = store.addElementsFromKerML(projectId, request.kerml)
                if (result == null) {
                    call.respond(HttpStatusCode.BadRequest, errorResponse("Failed to parse KerML fragment"))
                    return@post
                }

                val model = store.getModel(projectId)!!
                val serializer = ElementSerializer(model.engine)
                val newElements = result.elementIds.mapNotNull { id ->
                    model.engine.getElement(id)?.let { serializer.serialize(it) }
                }

                call.respond(HttpStatusCode.Created, mapOf(
                    "commit" to result.commit.toApiResponse(),
                    "elements" to newElements
                ))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, errorResponse(e.message ?: "Invalid request"))
            }
        }

        patch("/{elementId}") {
            val projectId = call.parameters["projectId"]!!
            val elementId = call.parameters["elementId"]!!
            if (store.getProject(projectId) == null) {
                call.respond(HttpStatusCode.NotFound, errorResponse("Project not found: $projectId"))
                return@patch
            }
            try {
                val request = call.receive<UpdateElementRequest>()
                if (request.properties.isEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, errorResponse("At least one property is required"))
                    return@patch
                }

                val result = store.updateElementProperties(projectId, elementId, request.properties)
                if (result == null) {
                    call.respond(HttpStatusCode.NotFound, errorResponse("Project not found: $projectId"))
                    return@patch
                }

                val model = store.getModel(projectId)!!
                val serializer = ElementSerializer(model.engine)
                val element = model.engine.getElement(elementId)!!

                call.respond(mapOf(
                    "commit" to result.commit.toApiResponse(),
                    "element" to serializer.serialize(element)
                ))
            } catch (e: NoSuchElementException) {
                call.respond(HttpStatusCode.NotFound, errorResponse(e.message ?: "Element not found"))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, errorResponse(e.message ?: "Invalid property"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, errorResponse(e.message ?: "Invalid request"))
            }
        }

        delete("/{elementId}") {
            val projectId = call.parameters["projectId"]!!
            val elementId = call.parameters["elementId"]!!
            if (store.getProject(projectId) == null) {
                call.respond(HttpStatusCode.NotFound, errorResponse("Project not found: $projectId"))
                return@delete
            }
            try {
                val result = store.deleteElement(projectId, elementId)
                if (result == null) {
                    call.respond(HttpStatusCode.NotFound, errorResponse("Project not found: $projectId"))
                    return@delete
                }

                call.respond(mapOf(
                    "commit" to result.commit.toApiResponse(),
                    "deletedElementIds" to result.elementIds
                ))
            } catch (e: NoSuchElementException) {
                call.respond(HttpStatusCode.NotFound, errorResponse(e.message ?: "Element not found"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, errorResponse(e.message ?: "Delete failed"))
            }
        }
    }

    // === Export/Import endpoints ===

    get("/projects/{projectId}/export") {
        val projectId = call.parameters["projectId"]!!
        val project = store.getProject(projectId)
        if (project == null) {
            call.respond(HttpStatusCode.NotFound, errorResponse("Project not found: $projectId"))
            return@get
        }

        val model = store.getModel(projectId) ?: run {
            call.respond(HttpStatusCode.NotFound, errorResponse("Model not found"))
            return@get
        }

        val branch = store.getDefaultBranch(projectId)
        val commits = store.getCommitsForProject(projectId)
        val localElements = filterLocalElements(model, model.engine.getAllElements())
        val serializer = ElementSerializer(model.engine)

        val exportData = linkedMapOf<String, Any?>(
            "project" to project.toApiResponse(branch),
            "commits" to commits.map { it.toApiResponse() },
            "elements" to serializer.serializeAll(localElements)
        )

        call.respond(exportData)
    }

    post("/projects/import") {
        try {
            val importData = call.receive<Map<String, Any?>>()
            @Suppress("UNCHECKED_CAST")
            val projectData = importData["project"] as? Map<String, Any?>
            if (projectData == null) {
                call.respond(HttpStatusCode.BadRequest, errorResponse("Missing 'project' field in import data"))
                return@post
            }

            val name = projectData["name"] as? String ?: "Imported Project"
            val description = projectData["description"] as? String
            val project = store.createProject(name, description)
            val branch = store.getDefaultBranch(project.id)

            call.respond(HttpStatusCode.Created, project.toApiResponse(branch))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, errorResponse(e.message ?: "Invalid import data"))
        }
    }
}

/**
 * Filter elements to only include local (non-library) elements.
 */
private fun filterLocalElements(
    model: org.openmbee.gearshift.kerml.KerMLModel,
    elements: List<org.openmbee.mdm.framework.runtime.MDMObject>
): List<org.openmbee.mdm.framework.runtime.MDMObject> {
    val mountableEngine = model.engine as? MountableEngine
    return if (mountableEngine != null) {
        mountableEngine.getLocalElements()
    } else {
        elements
    }
}
