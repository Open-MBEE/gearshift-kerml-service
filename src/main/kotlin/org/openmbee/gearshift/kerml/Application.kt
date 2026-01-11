package org.openmbee.gearshift.kerml

import org.openmbee.gearshift.GearshiftEngine

/**
 * Main application entry point for the GearShift KerML Service.
 */
fun main(args: Array<String>) {
    println("=".repeat(70))
    println("GearShift KerML Service")
    println("Metadata-driven KerML Implementation using Gearshift Framework")
    println("=".repeat(70))
    println()

    // Create the Gearshift engine
    val engine = GearshiftEngine()

    // Initialize KerML metamodel
    println("Initializing KerML Metamodel...")
    KerMLMetamodel.initialize(engine)
    println()

    // Validate metamodel
    val errors = engine.validateMetamodel()
    if (errors.isEmpty()) {
        println("✓ Metamodel is valid!")
    } else {
        println("✗ Metamodel has errors:")
        errors.forEach { println("  - $it") }
    }
    println()

    // Show metamodel statistics
    val allClasses = engine.metamodelRegistry.getAllClasses()
    println("Metamodel Statistics:")
    println("  Total MetaClasses: ${allClasses.size}")
    allClasses.forEach { metaClass ->
        println("  - ${metaClass.name}")
        println("      Superclasses: ${metaClass.superclasses.joinToString(", ")}")
        println("      Attributes: ${metaClass.attributes.size}")
        println("      Constraints: ${metaClass.constraints.size}")
    }
    println()

    // Example: Create some instances
    println("Creating example instances...")

    // Create a Feature instance
    val (featureId, feature) = engine.createInstance("Feature", "feature-1")
    engine.setProperty(featureId, "name", "MyFeature")
    engine.setProperty(featureId, "isAbstract", false)

    println("Created Feature instance: $featureId")
    println("  name = ${engine.getProperty(featureId, "name")}")
    println("  isAbstract = ${engine.getProperty(featureId, "isAbstract")}")
    println()

    // Query for all Features
    val allFeatures = engine.getInstancesByType("Feature")
    println("Total Feature instances: ${allFeatures.size}")
    println()

    // Repository statistics
    val stats = engine.getStatistics()
    println("Repository Statistics:")
    println("  Total instances: ${stats.totalObjects}")
    println("  Type distribution: ${stats.typeDistribution}")
    println()

    println("=".repeat(70))
    println("Gearshift KerML Service initialized successfully!")
    println("=".repeat(70))
}
