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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.openmbee.gearshift.kerml.KerMLModel
import org.openmbee.gearshift.settings.GearshiftSettings
import org.openmbee.mdm.framework.runtime.MountRegistry

private val logger = KotlinLogging.logger {}

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
 * Gearshift KerML API server.
 *
 * All model operations are project-scoped via the SysML v2 API:
 * - POST /projects — create a project
 * - POST /projects/{projectId}/commits — commit KerML
 * - GET  /projects/{projectId}/query/traverse/{elementId} — recursive traversal
 * - POST /projects/{projectId}/query/gql — flat tabular query
 * - GET  /projects/{projectId}/generate — emit canonical KerML
 */
class GearshiftServer(
    private val port: Int = 8080,
    private val enableMounts: Boolean = false,
    private val settings: GearshiftSettings = GearshiftSettings.DEFAULT
) {
    private val projectStore: ProjectStore
    private val simulationSessionManager = SimulationSessionManager()

    init {
        if (enableMounts) {
            KerMLModel.initializeKernelLibrary()
        }

        val backend = settings.dataDir?.let { dir ->
            FileProjectBackend(java.nio.file.Path.of(dir))
        }
        projectStore = ProjectStore(enableMounts, backend)

        if (backend != null) {
            val restored = backend.loadAll(projectStore)
            logger.info { "Startup: restored $restored project(s) from ${settings.dataDir}" }
        }
    }

    fun start(wait: Boolean = true) {
        embeddedServer(Netty, port = port) {
            install(ContentNegotiation) {
                jackson {
                    registerModule(com.fasterxml.jackson.module.kotlin.KotlinModule.Builder().build())
                    registerModule(JavaTimeModule())
                    enable(SerializationFeature.INDENT_OUTPUT)
                    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
                }
            }

            if (settings.corsAllowedOrigins.isNotEmpty()) {
                install(CORS) {
                    settings.corsAllowedOrigins.forEach { origin ->
                        if (origin == "*") {
                            anyHost()
                        } else {
                            allowHost(
                                origin.removePrefix("http://").removePrefix("https://"),
                                schemes = listOf("http", "https")
                            )
                        }
                    }
                    allowHeader(HttpHeaders.ContentType)
                    allowHeader(HttpHeaders.Authorization)
                    allowMethod(HttpMethod.Get)
                    allowMethod(HttpMethod.Post)
                    allowMethod(HttpMethod.Put)
                    allowMethod(HttpMethod.Delete)
                    allowMethod(HttpMethod.Options)
                }
            }

            routing {
                sysmlApiRoutes(projectStore)
                projectQueryRoutes(projectStore)
                parametricAnalysisRoutes(projectStore)
                simulationRoutes(simulationSessionManager)

                get("/") {
                    call.respondText(
                        """
                        Gearshift KerML API
                        ===================

                        Projects:   POST/GET/PUT/DELETE /projects[/{projectId}]
                        Commits:    POST/GET /projects/{projectId}/commits[/{commitId}]
                        Elements:   GET /projects/{projectId}/commits/{commitId}/elements[/{elementId}]
                        Traverse:   GET /projects/{projectId}/query/traverse/{elementId}?depth=&detail=&recurse=&types=
                        GQL Query:  POST /projects/{projectId}/query/gql
                        Generate:   GET /projects/{projectId}/generate
                        Health:     GET /health
                        Library:    GET /library-status
                        """.trimIndent(),
                        ContentType.Text.Plain
                    )
                }

                get("/health") {
                    call.respond(mapOf("status" to "ok"))
                }

                get("/library-status") {
                    if (!enableMounts) {
                        call.respond(LibraryStatusResponse(enabled = false, mounted = false))
                        return@get
                    }

                    val libraryMount = MountRegistry.get(KerMLModel.KERNEL_LIBRARY_MOUNT_ID)
                    if (libraryMount != null) {
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
                        call.respond(LibraryStatusResponse(enabled = true, mounted = false))
                    }
                }
            }
        }.start(wait = wait)
    }
}

/**
 * Main entry point for the Gearshift KerML API server.
 *
 * Usage: [port] [--no-library] [--data-dir=<path>]
 *   port: Server port (default: 8080)
 *   --no-library: Disable kernel library mounting (library is enabled by default)
 *   --data-dir=<path>: Enable file-based persistence at the given directory
 *   -Dgearshift.dataDir=<path>: Alternative via system property
 */
fun main(args: Array<String>) {
    val port = args.firstOrNull { it.toIntOrNull() != null }?.toInt() ?: 8080
    val enableMounts = !args.contains("--no-library")
    val dataDir = args.firstOrNull { it.startsWith("--data-dir=") }
        ?.substringAfter("--data-dir=")
        ?: System.getProperty("gearshift.dataDir")

    val settings = GearshiftSettings(
        serverPort = port,
        corsAllowedOrigins = listOf("http://localhost:4200"),
        dataDir = dataDir
    )

    logger.info { "Starting Gearshift KerML API on port $port" }
    logger.info { "CORS allowed origins: ${settings.corsAllowedOrigins}" }
    if (enableMounts) {
        logger.info { "Library mounting enabled - loading kernel library..." }
    } else {
        logger.info { "Library mounting disabled (--no-library)" }
    }
    if (dataDir != null) {
        logger.info { "Persistence enabled at $dataDir" }
    } else {
        logger.info { "Persistence disabled (use --data-dir=<path> or -Dgearshift.dataDir=<path> to enable)" }
    }
    GearshiftServer(port, enableMounts, settings).start()
}
