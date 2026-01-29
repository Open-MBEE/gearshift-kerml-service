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

import org.openmbee.gearshift.kerml.KerMLMetamodelLoader
import org.openmbee.gearshift.framework.runtime.MetamodelRegistry

/**
 * Main application entry point for the GearShift KerML Service.
 */
fun main(args: Array<String>) {
    println("=".repeat(70))
    println("GearShift KerML Service")
    println("Metadata-driven KerML Implementation using Gearshift Framework")
    println("=".repeat(70))
    println()

    // Initialize the schema (metamodel registry)
    val schema = MetamodelRegistry()
    println("Initializing KerML Metamodel...")
    KerMLMetamodelLoader.initialize(schema)
    println()

    // Validate metamodel
    val errors = schema.validate()
    if (errors.isEmpty()) {
        println("✓ Metamodel is valid!")
    } else {
        println("✗ Metamodel has errors:")
        errors.forEach { println("  - $it") }
    }
    println()

    // Show metamodel statistics
    val allClasses = schema.getAllClasses()
    println("Metamodel Statistics:")
    println("  Total MetaClasses: ${allClasses.size}")
    allClasses.take(10).forEach { metaClass ->
        println("  - ${metaClass.name}")
        println("      Superclasses: ${metaClass.superclasses.joinToString(", ")}")
        println("      Attributes: ${metaClass.attributes.size}")
        println("      Constraints: ${metaClass.constraints.size}")
    }
    if (allClasses.size > 10) {
        println("  ... and ${allClasses.size - 10} more")
    }
    println()

    // Create session manager and a session
    val sessionManager = SessionManager(schema)
    val session = sessionManager.createSession("Demo Session", ModelLanguage.KERML)

    println("Created session: ${session.name} (${session.id})")
    println()

    // Example: Create some instances
    println("Creating example instances...")

    // Create a Feature instance
    val feature = session.createElement("Feature")
    session.engine.setProperty(feature.id!!, "declaredName", "MyFeature")

    println("Created Feature instance: ${feature.id}")
    println("  declaredName = ${session.engine.getProperty(feature.id!!, "declaredName")}")
    println()

    // Query for all Features
    val allFeatures = session.getElementsByClass("Feature")
    println("Total Feature instances: ${allFeatures.size}")
    println()

    // Show element count
    println("Total elements in session: ${session.getAllElements().size}")
    println()

    // Cleanup
    sessionManager.closeAll()
    println("Session closed.")
}
