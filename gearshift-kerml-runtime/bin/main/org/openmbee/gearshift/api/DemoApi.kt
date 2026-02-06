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
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.openmbee.mdm.framework.runtime.MDMObject
import org.openmbee.mdm.framework.runtime.MountableEngine
import org.openmbee.mdm.framework.runtime.MountRegistry
import org.openmbee.gearshift.kerml.KerMLModel

/**
 * Response for the library status endpoint.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class LibraryStatusResponse(
    val enabled: Boolean,
    val mounted: Boolean,
    val libraryId: String? = null,
    val libraryName: String? = null,
    val elementCount: Int? = null,
    val rootNamespaces: List<String> = emptyList()
)

/**
 * Response data class representing an element in the tree.
 */
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

/**
 * Response for the parse endpoint.
 */
data class ParseResponse(
    val success: Boolean,
    val rootId: String? = null,
    val tree: ElementNode? = null,
    val errors: List<String> = emptyList(),
    val statistics: Map<String, Any> = emptyMap()
)

/**
 * Request body for parsing KerML.
 */
data class ParseRequest(
    val kerml: String
)

/**
 * Demo API server for the Gearshift KerML Service.
 * Uses the visitor-based parsing architecture via KerMLModel.
 *
 * If enableMounts is true, the kernel library is initialized and mounted,
 * allowing references to library types like Base::Anything.
 */
class DemoApi(
    private val port: Int = 8080,
    private val enableMounts: Boolean = false
) {
    private val model: KerMLModel

    init {
        model = if (enableMounts) {
            // Initialize kernel library once (idempotent)
            KerMLModel.initializeKernelLibrary()
            // Create model with mount support
            KerMLModel.createWithMounts()
        } else {
            // Simple model without library support
            KerMLModel()
        }
    }

    private val engine get() = model.engine

    /**
     * Build a tree from the parsed model, showing elements owned by the root namespace.
     * The root namespace itself is an unnamed container, so we create a synthetic "Model" root
     * containing its direct children (packages, classes, etc.).
     */
    private fun buildTreeFromRoot(rootId: String): ElementNode {
        val visited = mutableSetOf<String>()
        val children = mutableListOf<ElementNode>()

        // Get owned memberships from the root namespace
        val ownedMemberships = engine.getLinkedTargets(
            "membershipOwningNamespaceOwnedMembershipAssociation",
            rootId
        )

        // For each membership, get the owned member element
        for (membership in ownedMemberships) {
            val membershipId = membership.id ?: continue

            // Try OwningMembership -> ownedMemberElement
            val ownedElements = engine.getLinkedTargets(
                "owningMembershipOwnedMemberElementAssociation",
                membershipId
            )
            for (ownedElement in ownedElements) {
                ownedElement.id?.let { id ->
                    children.add(buildTreeNode(id, visited))
                }
            }
        }

        // Return a synthetic root node representing the model
        return ElementNode(
            id = rootId,
            type = "Model",
            name = "Model",
            declaredName = null,
            children = children
        )
    }

    /**
     * Build a tree from the parsed model starting at the given root element.
     * Uses ownedRelationship -> ownedRelatedElement pattern to find children.
     */
    private fun buildTree(rootId: String): ElementNode {
        val visited = mutableSetOf<String>()
        return buildTreeNode(rootId, visited)
    }

    private fun buildTreeNode(elementId: String, visited: MutableSet<String>): ElementNode {
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

        val children = mutableListOf<ElementNode>()

        // Get ownedRelationship links (Namespace/Element -> Membership)
        val ownedMemberships = engine.getLinkedTargets(
            "membershipOwningNamespaceOwnedMembershipAssociation",
            elementId
        )

        // For each membership, get the owned member element
        for (membership in ownedMemberships) {
            val membershipId = membership.id ?: continue

            // Try OwningMembership -> ownedMemberElement
            val ownedElements = engine.getLinkedTargets(
                "owningMembershipOwnedMemberElementAssociation",
                membershipId
            )
            for (ownedElement in ownedElements) {
                ownedElement.id?.let { id ->
                    children.add(buildTreeNode(id, visited))
                }
            }

            // Also try FeatureMembership -> ownedMemberFeature
            val ownedFeatures = engine.getLinkedTargets(
                "owningFeatureMembershipOwnedMemberFeatureAssociation",
                membershipId
            )
            for (feature in ownedFeatures) {
                feature.id?.let { id ->
                    children.add(buildTreeNode(id, visited))
                }
            }
        }

        // Also check Type -> ownedFeatureMembership for features
        val ownedFeatureMemberships = engine.getLinkedTargets(
            "owningTypeOwnedFeatureMembershipAssociation",
            elementId
        )
        for (featureMembership in ownedFeatureMemberships) {
            val fmId = featureMembership.id ?: continue
            val features = engine.getLinkedTargets(
                "owningFeatureMembershipOwnedMemberFeatureAssociation",
                fmId
            )
            for (feature in features) {
                feature.id?.let { id ->
                    if (id !in visited) {
                        children.add(buildTreeNode(id, visited))
                    }
                }
            }
        }

        // Get the derived name property via engine (evaluates effectiveName())
        val derivedName = try {
            engine.getProperty(elementId, "name") as? String
        } catch (e: Exception) {
            null
        }

        // Get stored declaredName
        val declaredName = obj.getProperty("declaredName") as? String

        // Get select properties for display (excluding name-related ones we show separately)
        val properties = mutableMapOf<String, Any?>()
        obj.getAllProperties().forEach { (key, value) ->
            if (value != null && key !in listOf("name", "declaredName", "declaredShortName", "shortName")) {
                properties[key] = value
            }
        }

        // Build raw properties map with ALL attributes and association ends
        val rawProperties = mutableMapOf<String, Any?>()
        val associationEnds = mutableMapOf<String, Any?>()

        // Get all superclasses to collect inherited attributes
        val metaClass = obj.metaClass
        val allClassNames = engine.metamodelRegistry.getAllSuperclasses(metaClass.name) + metaClass.name

        // Collect all attributes from this class and all superclasses
        for (className in allClassNames) {
            val cls = engine.metamodelRegistry.getClass(className) ?: continue
            for (prop in cls.attributes) {
                if (!rawProperties.containsKey(prop.name)) {
                    try {
                        val value = engine.getProperty(elementId, prop.name)
                        rawProperties[prop.name] = value
                    } catch (e: Exception) {
                        rawProperties[prop.name] = null
                    }
                }
            }
        }

        // Collect all association ends applicable to this class
        for (association in engine.metamodelRegistry.getAllAssociations()) {
            // Check if target end applies to this class (source type matches our hierarchy)
            if (allClassNames.contains(association.sourceEnd.type)) {
                val endName = association.targetEnd.name
                if (!associationEnds.containsKey(endName)) {
                    try {
                        val value = engine.getProperty(elementId, endName)
                        associationEnds[endName] = formatAssociationValue(value)
                    } catch (e: Exception) {
                        associationEnds[endName] = null
                    }
                }
            }
            // Check if source end applies (navigable and target type matches)
            if (association.sourceEnd.isNavigable && allClassNames.contains(association.targetEnd.type)) {
                val endName = association.sourceEnd.name
                if (!associationEnds.containsKey(endName)) {
                    try {
                        val value = engine.getProperty(elementId, endName)
                        associationEnds[endName] = formatAssociationValue(value)
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

    /**
     * Format association end values for display.
     * Converts MDMObject references to readable strings.
     */
    private fun formatAssociationValue(value: Any?): Any? {
        return when (value) {
            null -> null
            is MDMObject -> {
                val name = value.getProperty("declaredName") as? String
                    ?: value.getProperty("name") as? String
                if (name != null) {
                    "${value.className}[$name]"
                } else {
                    "${value.className}[${value.id?.take(8)}...]"
                }
            }

            is List<*> -> {
                if (value.isEmpty()) {
                    emptyList<String>()
                } else {
                    value.map { formatAssociationValue(it) }
                }
            }

            else -> value.toString()
        }
    }

    /**
     * Start the API server.
     */
    fun start(wait: Boolean = true) {
        embeddedServer(Netty, port = port) {
            install(ContentNegotiation) {
                jackson {
                    enable(SerializationFeature.INDENT_OUTPUT)
                    setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                }
            }

            routing {
                get("/") {
                    // Serve the static HTML demo page
                    val indexHtml = this::class.java.classLoader.getResource("static/index.html")?.readText()
                    if (indexHtml != null) {
                        call.respondText(indexHtml, ContentType.Text.Html)
                    } else {
                        call.respondText(
                            """
                            Gearshift KerML Demo API
                            ========================

                            POST /parse - Parse KerML and return element tree
                              Body: { "kerml": "package Foo { class Bar; }" }

                            GET /health - Health check

                            Note: Static demo page not found. Run from resources directory.
                            """.trimIndent(),
                            ContentType.Text.Plain
                        )
                    }
                }

                get("/health") {
                    call.respond(mapOf("status" to "ok"))
                }

                get("/library-status") {
                    val mountableEngine = engine as? MountableEngine
                    if (mountableEngine == null || !enableMounts) {
                        call.respond(
                            LibraryStatusResponse(
                                enabled = false,
                                mounted = false
                            )
                        )
                        return@get
                    }

                    val libraryMount = MountRegistry.get(KerMLModel.KERNEL_LIBRARY_MOUNT_ID)
                    val isMounted = mountableEngine.isMounted(KerMLModel.KERNEL_LIBRARY_MOUNT_ID)

                    if (libraryMount != null && isMounted) {
                        val rootNamespaces = libraryMount.getRootNamespaces().mapNotNull { ns ->
                            ns.getProperty("declaredName") as? String
                                ?: ns.getProperty("name") as? String
                        }
                        call.respond(
                            LibraryStatusResponse(
                                enabled = true,
                                mounted = true,
                                libraryId = libraryMount.id,
                                libraryName = libraryMount.name,
                                elementCount = libraryMount.engine.getAllElements().size,
                                rootNamespaces = rootNamespaces
                            )
                        )
                    } else {
                        call.respond(
                            LibraryStatusResponse(
                                enabled = true,
                                mounted = false
                            )
                        )
                    }
                }

                post("/parse") {
                    try {
                        val request = call.receive<ParseRequest>()

                        // Reset factory state for fresh parse
                        model.reset()

                        // Parse the KerML using visitor-based architecture
                        val pkg = model.parseString(request.kerml)
                        val result = model.getLastParseResult()

                        if (result == null || !result.success) {
                            val errors = result?.errors?.map { it.message } ?: listOf("Unknown parse error")
                            call.respond(
                                HttpStatusCode.BadRequest,
                                ParseResponse(
                                    success = false,
                                    errors = errors
                                )
                            )
                            return@post
                        }

                        // Build the tree from elements owned by the root namespace
                        // The root namespace itself is an unnamed container, so we show its children
                        val rootElement = model.getRootElement()
                        val tree = rootElement?.id?.let { rootId ->
                            buildTreeFromRoot(rootId)
                        }

                        // Compute simple statistics from engine
                        val mountableEngine = engine as? MountableEngine
                        val localElements = mountableEngine?.getLocalElements() ?: engine.getAllElements()
                        val typeDistribution = localElements.groupBy { it.className }
                            .mapValues { it.value.size }

                        // Build mount info if available
                        val mountInfo = if (enableMounts && mountableEngine != null) {
                            val activeMounts = mountableEngine.getActiveMounts()
                            mapOf(
                                "libraryEnabled" to true,
                                "activeMounts" to activeMounts.map { mount ->
                                    mapOf(
                                        "id" to mount.id,
                                        "name" to mount.name,
                                        "elementCount" to mount.engine.getAllElements().size,
                                        "isImplicit" to mount.isImplicit
                                    )
                                }
                            )
                        } else {
                            mapOf("libraryEnabled" to false)
                        }

                        call.respond(
                            ParseResponse(
                                success = true,
                                rootId = rootElement?.id,
                                tree = tree,
                                statistics = mapOf(
                                    "totalObjects" to localElements.size,
                                    "typeDistribution" to typeDistribution,
                                    "mounts" to mountInfo
                                )
                            )
                        )
                    } catch (e: Exception) {
                        call.respond(
                            HttpStatusCode.InternalServerError,
                            ParseResponse(
                                success = false,
                                errors = listOf(e.message ?: "Unknown error")
                            )
                        )
                    }
                }
            }
        }.start(wait = wait)
    }
}

/**
 * Main entry point for the Demo API server.
 *
 * Usage: DemoApi [port] [--with-library]
 *   port: Server port (default: 8080)
 *   --with-library: Enable kernel library mounting for library type resolution
 */
fun main(args: Array<String>) {
    val port = args.firstOrNull { it.toIntOrNull() != null }?.toInt() ?: 8080
    val enableMounts = args.contains("--with-library")

    println("Starting Gearshift KerML Demo API on port $port...")
    if (enableMounts) {
        println("Library mounting enabled - loading kernel library...")
    }
    DemoApi(port, enableMounts).start()
}
