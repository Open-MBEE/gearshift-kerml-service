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

import com.fasterxml.jackson.annotation.JsonInclude
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.openmbee.mdm.framework.query.gql.parser.GqlParseException
import org.openmbee.mdm.framework.query.gql.query
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MDMObject
import org.openmbee.mdm.framework.runtime.MountableEngine
import org.openmbee.gearshift.kerml.generator.KerMLWriter

private val logger = KotlinLogging.logger {}

// === Data classes ===

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ElementNode(
    val id: String,
    val type: String,
    val name: String?,
    val declaredName: String?,
    val properties: Map<String, Any?> = emptyMap(),
    val rawProperties: Map<String, Any?> = emptyMap(),
    val associationEnds: Map<String, Any?> = emptyMap(),
    val children: List<ElementNode> = emptyList()
)

data class AssocEndRef(
    val id: String,
    val type: String,
    val display: String
)

data class QueryRequest(
    val gql: String,
    val includeLibrary: Boolean = false
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class QueryResponse(
    val success: Boolean,
    val columns: List<String> = emptyList(),
    val rows: List<Map<String, Any?>> = emptyList(),
    val rowCount: Int = 0,
    val errors: List<String> = emptyList()
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class GenerateResponse(
    val success: Boolean,
    val kerml: String? = null,
    val errors: List<String> = emptyList()
)

// === Traversal parameters ===

enum class DetailLevel { SUMMARY, FULL }

enum class RecurseStrategy { OWNERSHIP, COMPOSITION }

data class TreeParams(
    val depth: Int = -1,
    val detail: DetailLevel = DetailLevel.SUMMARY,
    val recurse: RecurseStrategy = RecurseStrategy.OWNERSHIP,
    val typeFilter: Set<String>? = null
)

// === Model traversal service ===

/**
 * Provides recursive traversal and query operations over an MDMEngine.
 * Stateless — instantiate per request with the target project's engine.
 */
class ModelQueryService(private val engine: MDMEngine) {

    fun buildTreeFromRoot(rootId: String, params: TreeParams = TreeParams()): ElementNode {
        val visited = mutableSetOf<String>()
        val children = mutableListOf<ElementNode>()

        for (membership in engine.getLinkedTargets("membershipOwningNamespaceOwnedMembershipAssociation", rootId)) {
            val membershipId = membership.id ?: continue
            for (ownedElement in engine.getLinkedTargets("owningMembershipOwnedMemberElementAssociation", membershipId)) {
                ownedElement.id?.let { id ->
                    children.add(buildTreeNode(id, visited, params))
                }
            }
        }

        return ElementNode(
            id = rootId,
            type = "Model",
            name = "Model",
            declaredName = null,
            children = children
        )
    }

    fun buildTreeNode(
        elementId: String,
        visited: MutableSet<String>,
        params: TreeParams
    ): ElementNode {
        if (elementId in visited) {
            val obj = engine.getInstance(elementId)
            return ElementNode(
                id = elementId,
                type = obj?.className ?: "Unknown",
                name = "[circular reference]",
                declaredName = null
            )
        }
        visited.add(elementId)

        val obj = engine.getInstance(elementId)
            ?: return ElementNode(
                id = elementId,
                type = "Unknown",
                name = null,
                declaredName = null
            )

        val children = if (params.depth == 0) {
            emptyList()
        } else {
            val childParams = params.copy(depth = if (params.depth > 0) params.depth - 1 else -1)
            when (params.recurse) {
                RecurseStrategy.OWNERSHIP -> collectOwnershipChildren(elementId, visited, childParams)
                RecurseStrategy.COMPOSITION -> collectCompositionChildren(elementId, visited, childParams)
            }
        }

        val declaredName = obj.getProperty("declaredName") as? String

        return if (params.detail == DetailLevel.FULL) {
            buildFullNode(obj, elementId, declaredName, children)
        } else {
            ElementNode(
                id = elementId,
                type = obj.className,
                name = declaredName,
                declaredName = declaredName,
                children = children
            )
        }
    }

    private fun matchesTypeFilter(obj: MDMObject, typeFilter: Set<String>?): Boolean {
        if (typeFilter == null) return true
        val className = obj.className
        if (className in typeFilter) return true
        val supers = engine.metamodelRegistry.getAllSuperclasses(className)
        return supers.any { it in typeFilter }
    }

    private fun collectOwnershipChildren(
        elementId: String,
        visited: MutableSet<String>,
        params: TreeParams
    ): List<ElementNode> {
        val children = mutableListOf<ElementNode>()
        val seen = mutableSetOf<String>()

        for (membership in engine.getLinkedTargets("membershipOwningNamespaceOwnedMembershipAssociation", elementId)) {
            val membershipId = membership.id ?: continue

            for (ownedElement in engine.getLinkedTargets("owningMembershipOwnedMemberElementAssociation", membershipId)) {
                val id = ownedElement.id ?: continue
                if (!seen.add(id)) continue
                if (params.typeFilter != null && !matchesTypeFilter(ownedElement, params.typeFilter)) continue
                children.add(buildTreeNode(id, visited, params))
            }

            for (feature in engine.getLinkedTargets("owningFeatureMembershipOwnedMemberFeatureAssociation", membershipId)) {
                val id = feature.id ?: continue
                if (!seen.add(id)) continue
                if (params.typeFilter != null && !matchesTypeFilter(feature, params.typeFilter)) continue
                children.add(buildTreeNode(id, visited, params))
            }
        }

        for (featureMembership in engine.getLinkedTargets("owningTypeOwnedFeatureMembershipAssociation", elementId)) {
            val fmId = featureMembership.id ?: continue
            for (feature in engine.getLinkedTargets("owningFeatureMembershipOwnedMemberFeatureAssociation", fmId)) {
                val id = feature.id ?: continue
                if (!seen.add(id)) continue
                if (params.typeFilter != null && !matchesTypeFilter(feature, params.typeFilter)) continue
                children.add(buildTreeNode(id, visited, params))
            }
        }

        return children
    }

    private fun collectCompositionChildren(
        elementId: String,
        visited: MutableSet<String>,
        params: TreeParams
    ): List<ElementNode> {
        val children = mutableListOf<ElementNode>()

        for (featureMembership in engine.getLinkedTargets("owningTypeOwnedFeatureMembershipAssociation", elementId)) {
            val fmId = featureMembership.id ?: continue
            for (feature in engine.getLinkedTargets("owningFeatureMembershipOwnedMemberFeatureAssociation", fmId)) {
                val featureId = feature.id ?: continue
                if (featureId in visited) continue

                val featureObj = engine.getInstance(featureId) ?: continue

                if (params.typeFilter != null) {
                    val matchesMeta = matchesTypeFilter(featureObj, params.typeFilter)
                    val featureType = resolveFeatureType(featureId)
                    val typeName = featureType?.getProperty("declaredName") as? String
                    val matchesTyping = typeName != null && typeName in params.typeFilter
                    if (!matchesMeta && !matchesTyping) continue
                }

                visited.add(featureId)
                val featureName = featureObj.getProperty("declaredName") as? String

                val featureType = resolveFeatureType(featureId)
                val featureChildren = if (featureType?.id != null && params.depth != 0) {
                    collectCompositionChildren(
                        featureType.id!!,
                        visited,
                        params.copy(depth = if (params.depth > 0) params.depth - 1 else -1)
                    )
                } else {
                    emptyList()
                }

                children.add(
                    if (params.detail == DetailLevel.FULL) {
                        buildFullNode(featureObj, featureId, featureName, featureChildren)
                    } else {
                        ElementNode(
                            id = featureId,
                            type = featureObj.className,
                            name = featureName,
                            declaredName = featureName,
                            children = featureChildren
                        )
                    }
                )
            }
        }

        return children
    }

    private fun resolveFeatureType(featureId: String): MDMObject? {
        for (typing in engine.getLinkedTargets("owningFeatureOwnedTypingAssociation", featureId)) {
            val typingId = typing.id ?: continue
            val types = engine.getLinkedTargets("typingByTypeTypeAssociation", typingId)
            if (types.isNotEmpty()) return types.first()
        }
        return null
    }

    private fun buildFullNode(
        obj: MDMObject,
        elementId: String,
        declaredName: String?,
        children: List<ElementNode>
    ): ElementNode {
        val derivedName = try {
            engine.getProperty(elementId, "name") as? String
        } catch (e: Exception) {
            logger.debug(e) { "Failed to compute derived name for $elementId" }
            null
        }

        val properties = mutableMapOf<String, Any?>()
        obj.getAllProperties().forEach { (key, value) ->
            if (value != null && key !in listOf("name", "declaredName", "declaredShortName", "shortName")) {
                properties[key] = value
            }
        }

        val rawProperties = mutableMapOf<String, Any?>()
        val associationEnds = mutableMapOf<String, Any?>()
        val metaClass = obj.metaClass
        val allClassNames = engine.metamodelRegistry.getAllSuperclasses(metaClass.name) + metaClass.name

        for (className in allClassNames) {
            val cls = engine.metamodelRegistry.getClass(className) ?: continue
            for (prop in cls.attributes) {
                if (!rawProperties.containsKey(prop.name)) {
                    try {
                        rawProperties[prop.name] = engine.getProperty(elementId, prop.name)
                    } catch (e: Exception) {
                        rawProperties[prop.name] = null
                    }
                }
            }
        }

        for (association in engine.metamodelRegistry.getAllAssociations()) {
            if (allClassNames.contains(association.sourceEnd.type)) {
                val endName = association.targetEnd.name
                if (!associationEnds.containsKey(endName)) {
                    try {
                        associationEnds[endName] = formatAssociationValue(engine.getProperty(elementId, endName))
                    } catch (e: Exception) {
                        associationEnds[endName] = null
                    }
                }
            }
            if (association.sourceEnd.isNavigable && allClassNames.contains(association.targetEnd.type)) {
                val endName = association.sourceEnd.name
                if (!associationEnds.containsKey(endName)) {
                    try {
                        associationEnds[endName] = formatAssociationValue(engine.getProperty(elementId, endName))
                    } catch (e: Exception) {
                        associationEnds[endName] = null
                    }
                }
            }
        }

        return ElementNode(
            id = elementId,
            type = obj.className,
            name = derivedName,
            declaredName = declaredName,
            properties = properties.ifEmpty { emptyMap() },
            rawProperties = rawProperties,
            associationEnds = associationEnds,
            children = children
        )
    }

    companion object {
        fun formatAssociationValue(value: Any?): Any? {
            return when (value) {
                null -> null
                is MDMObject -> {
                    val id = value.id ?: return null
                    val name = value.getProperty("declaredName") as? String
                        ?: value.getProperty("name") as? String
                    val display = if (name != null) {
                        "${value.className}[$name]"
                    } else {
                        "${value.className}[${id.take(8)}...]"
                    }
                    AssocEndRef(id = id, type = value.className, display = display)
                }
                is List<*> -> {
                    if (value.isEmpty()) emptyList<AssocEndRef>()
                    else value.mapNotNull { formatAssociationValue(it) as? AssocEndRef }
                }
                else -> value.toString()
            }
        }

        fun formatQueryValue(value: Any?): Any? {
            return when (value) {
                null -> null
                is MDMObject -> {
                    val name = value.getProperty("declaredName") as? String
                        ?: value.getProperty("name") as? String
                    if (name != null) "${value.className}[$name]"
                    else "${value.className}[${value.id?.take(8)}...]"
                }
                is List<*> -> value.map { formatQueryValue(it) }
                else -> value
            }
        }
    }
}

// === Route helpers ===

private fun parseTreeParams(call: io.ktor.server.application.ApplicationCall): TreeParams {
    val depthParam = call.request.queryParameters["depth"]?.toIntOrNull() ?: 0
    val detailParam = call.request.queryParameters["detail"] ?: "full"
    val recurseParam = call.request.queryParameters["recurse"] ?: "ownership"
    val typesParam = call.request.queryParameters["types"]

    return TreeParams(
        depth = depthParam,
        detail = if (detailParam == "summary") DetailLevel.SUMMARY else DetailLevel.FULL,
        recurse = when (recurseParam) {
            "composition" -> RecurseStrategy.COMPOSITION
            else -> RecurseStrategy.OWNERSHIP
        },
        typeFilter = typesParam?.split(",")?.map { it.trim() }?.toSet()
    )
}

private fun errorResponse(description: String): Map<String, Any> = mapOf(
    "@type" to "Error",
    "description" to description
)

/**
 * Install query and generate routes scoped to a project.
 *
 * Endpoints:
 * - POST /projects/{projectId}/query/gql
 * - GET  /projects/{projectId}/query/traverse/{elementId}?depth=&detail=&recurse=&types=
 * - GET  /projects/{projectId}/generate
 */
fun Route.projectQueryRoutes(store: ProjectStore) {

    route("/projects/{projectId}") {

        route("/query") {

            post("/gql") {
                val projectId = call.parameters["projectId"]!!
                val model = store.getModel(projectId)
                if (model == null) {
                    call.respond(HttpStatusCode.NotFound, errorResponse("Project not found: $projectId"))
                    return@post
                }

                try {
                    val request = call.receive<QueryRequest>()
                    val engine = model.engine

                    if (engine.getAllElements().isEmpty()) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            QueryResponse(success = false, errors = listOf("No model loaded. Commit KerML first."))
                        )
                        return@post
                    }

                    val result = engine.query(request.gql)

                    val mountableEngine = engine as? MountableEngine
                    val filteredRows = if (!request.includeLibrary && mountableEngine != null) {
                        val localIds = mountableEngine.getLocalElements().mapNotNull { it.id }.toSet()
                        result.rows.filter { row ->
                            row.values.all { value ->
                                when (value) {
                                    null -> true
                                    is MDMObject -> value.id == null || value.id in localIds
                                    is List<*> -> value.none { v -> v is MDMObject && v.id != null && v.id !in localIds }
                                    else -> true
                                }
                            }
                        }
                    } else {
                        result.rows
                    }

                    val formattedRows = filteredRows.map { row ->
                        row.mapValues { (_, value) -> ModelQueryService.formatQueryValue(value) }
                    }

                    call.respond(
                        QueryResponse(
                            success = true,
                            columns = result.columns,
                            rows = formattedRows,
                            rowCount = formattedRows.size
                        )
                    )
                } catch (e: GqlParseException) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        QueryResponse(success = false, errors = listOf("Parse error: ${e.message}"))
                    )
                } catch (e: Exception) {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        QueryResponse(success = false, errors = listOf(e.message ?: "Unknown error"))
                    )
                }
            }

            get("/traverse/{elementId}") {
                val projectId = call.parameters["projectId"]!!
                val elementId = call.parameters["elementId"]!!
                val model = store.getModel(projectId)
                if (model == null) {
                    call.respond(HttpStatusCode.NotFound, errorResponse("Project not found: $projectId"))
                    return@get
                }

                val obj = model.engine.getInstance(elementId)
                if (obj == null) {
                    call.respond(HttpStatusCode.NotFound, errorResponse("Element not found: $elementId"))
                    return@get
                }

                val params = parseTreeParams(call)
                val service = ModelQueryService(model.engine)
                val visited = mutableSetOf<String>()
                call.respond(service.buildTreeNode(elementId, visited, params))
            }
        }

        get("/generate") {
            val projectId = call.parameters["projectId"]!!
            val model = store.getModel(projectId)
            if (model == null) {
                call.respond(HttpStatusCode.NotFound, errorResponse("Project not found: $projectId"))
                return@get
            }

            try {
                val rootElement = model.getRootElement()
                if (rootElement == null || rootElement !is org.openmbee.gearshift.generated.interfaces.Namespace) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        GenerateResponse(success = false, errors = listOf("No root namespace found in project"))
                    )
                    return@get
                }

                val writer = KerMLWriter()
                val generated = writer.write(rootElement as org.openmbee.gearshift.generated.interfaces.Namespace)
                call.respond(GenerateResponse(success = true, kerml = generated))
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    GenerateResponse(success = false, errors = listOf(e.message ?: "Unknown error"))
                )
            }
        }
    }
}
