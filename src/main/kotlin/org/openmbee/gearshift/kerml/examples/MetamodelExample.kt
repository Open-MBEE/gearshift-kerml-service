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
package org.openmbee.gearshift.kerml.examples

import org.openmbee.gearshift.engine.MDMEngine
import org.openmbee.gearshift.engine.MetamodelRegistry
import org.openmbee.gearshift.metamodel.*
import org.openmbee.gearshift.repository.LinkRepository
import org.openmbee.gearshift.repository.ModelRepository

/**
 * Example demonstrating how to define KerML metamodel elements using Kotlin's
 * named parameters (feels like JSON!).
 */
fun main() {
    println("=== KerML Metamodel Example ===\n")

    // Example 1: Using Kotlin named parameters
    kotlinExample()

    // Example 2: Using JSON (pure declarative)
    jsonExample()

    // Example 3: Using MOF Engine
    MDMEngineExample()
}

/**
 * Example using Kotlin's named parameters.
 * This feels almost like writing JSON directly!
 */
private fun kotlinExample() {
    println("--- Kotlin Named Parameters Example ---")

    // Define a simple Element metaclass
    val element = MetaClass(
        name = "Element",
        isAbstract = true,
        description = "Root of the KerML hierarchy",
        attributes = listOf(
            MetaProperty(
                name = "name",
                type = "String",
                lowerBound = 0
            ),
            MetaProperty(
                name = "qualifiedName",
                type = "String",
                lowerBound = 0,
                isDerived = true,
                isReadOnly = true
            )
        )
    )

    // Define a Relationship metaclass
    val relationship = MetaClass(
        name = "Relationship",
        superclasses = listOf("Element"),
        isAbstract = true,
        description = "Represents relationships between elements",
        attributes = listOf(
            MetaProperty(
                name = "source",
                type = "Element",
                upperBound = -1,
                aggregation = AggregationKind.COMPOSITE
            ),
            MetaProperty(
                name = "target",
                type = "Element",
                upperBound = -1
            )
        )
    )

    println("Created MetaClass: ${element.name}")
    println("  Attributes: ${element.attributes.size}")
    println("Created MetaClass: ${relationship.name}")
    println("  Superclasses: ${relationship.superclasses}")
    println("  Attributes: ${relationship.attributes.size}")
    println()
}

/**
 * Example showing JSON serialization/deserialization using Jackson.
 * You can define your entire metamodel in JSON files!
 */
private fun jsonExample() {
    println("--- JSON Example (using Jackson) ---")

    // Create a MetaClass
    val feature = MetaClass(
        name = "Feature",
        superclasses = listOf("Element"),
        attributes = listOf(
            MetaProperty(
                name = "isAbstract",
                type = "Boolean",
                defaultValue = "false"
            ),
            MetaProperty(
                name = "ownedFeatures",
                type = "Feature",
                lowerBound = 0,
                upperBound = -1,
                aggregation = AggregationKind.COMPOSITE,
                isOrdered = true
            )
        ),
        constraints = listOf(
            MetaConstraint(
                name = "uniqueNames",
                language = "OCL",
                expression = "self.ownedFeatures->forAll(f1, f2 | f1 <> f2 implies f1.name <> f2.name)"
            )
        )
    )

    // Serialize to JSON
    val json = MetamodelLoader.toJson(feature)
    println("Serialized MetaClass to JSON:")
    println(json)
    println()

    // Deserialize from JSON
    val loaded = MetamodelLoader.loadMetaClassFromString(json)
    println("Loaded MetaClass: ${loaded.name}")
    println("  Superclasses: ${loaded.superclasses}")
    println("  Attributes: ${loaded.attributes.size}")
    println("  Constraints: ${loaded.constraints.size}")
    println()
}

/**
 * Example using the MOF Engine to create instances.
 */
private fun MDMEngineExample() {
    println("--- MOF Engine Example ---")

    // Create registry and register metaclasses
    val registry = MetamodelRegistry()

    val element = MetaClass(
        name = "Element",
        attributes = listOf(
            MetaProperty(
                name = "name",
                type = "String",
                lowerBound = 0
            )
        )
    )

    registry.registerClass(element)

    // Create MDM engine with repositories
    val engine = MDMEngine(registry, ModelRepository(), LinkRepository())

    // Create an instance
    val elementInstance = engine.createInstance("Element")
    engine.setProperty(elementInstance, "name", "MyElement")

    val name = engine.getProperty(elementInstance, "name") as? String
    println("Created instance with name: $name")

    // Validate instance
    val errors = engine.validate(elementInstance)
    if (errors.isEmpty()) {
        println("Instance is valid!")
    } else {
        println("Validation errors: $errors")
    }

    println()
}
