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
package org.openmbee.gearshift

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.gearshift.generated.KerMLElementFactory
import org.openmbee.gearshift.kerml.KerMLMetamodelLoader
import org.openmbee.mdm.framework.runtime.MetamodelRegistry
import org.openmbee.mdm.framework.runtime.SessionManager

private val logger = KotlinLogging.logger {}

/**
 * Main application entry point for the GearShift KerML Service.
 */
fun main(args: Array<String>) {
    logger.info { "GearShift KerML Service — Metadata-driven KerML Implementation using Mdm Framework" }

    // Initialize the schema (metamodel registry)
    val schema = MetamodelRegistry()
    logger.info { "Initializing KerML Metamodel..." }
    KerMLMetamodelLoader.initialize(schema)

    // Validate metamodel
    val errors = schema.validate()
    if (errors.isEmpty()) {
        logger.info { "Metamodel is valid" }
    } else {
        logger.error { "Metamodel has errors:" }
        errors.forEach { logger.error { "  - $it" } }
    }

    // Show metamodel statistics
    val allClasses = schema.getAllClasses()
    logger.info { "Metamodel Statistics: ${allClasses.size} total MetaClasses" }
    allClasses.take(10).forEach { metaClass ->
        logger.info { "  - ${metaClass.name} (superclasses: ${metaClass.superclasses.joinToString(", ")}, attributes: ${metaClass.attributes.size}, constraints: ${metaClass.constraints.size})" }
    }
    if (allClasses.size > 10) {
        logger.info { "  ... and ${allClasses.size - 10} more" }
    }

    // Create session manager and a session
    val factory = KerMLElementFactory()
    val sessionManager = SessionManager(schema, factory)
    val session = sessionManager.createSession("Demo Session")

    logger.info { "Created session: ${session.name} (${session.id})" }

    // Example: Create some instances
    logger.info { "Creating example instances..." }

    // Create a Feature instance
    val feature = session.createElement("Feature")
    session.engine.setProperty(feature.id!!, "declaredName", "MyFeature")

    logger.info { "Created Feature instance: ${feature.id}" }
    logger.info { "  declaredName = ${session.engine.getProperty(feature.id!!, "declaredName")}" }

    // Query for all Features
    val allFeatures = session.getElementsByClass("Feature")
    logger.info { "Total Feature instances: ${allFeatures.size}" }

    // Show element count
    logger.info { "Total elements in session: ${session.getAllElements().size}" }

    // Cleanup
    sessionManager.closeAll()
    logger.info { "Session closed." }
}
