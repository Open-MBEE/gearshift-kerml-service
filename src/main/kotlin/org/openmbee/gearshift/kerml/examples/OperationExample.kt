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

import org.openmbee.gearshift.GearshiftEngine
import org.openmbee.gearshift.kerml.metamodel.classes.root.createElementMetaClass

/**
 * Example demonstrating how to invoke operations on metamodel instances.
 *
 * This example shows:
 * 1. How to define operations in a metaclass
 * 2. How to invoke operations using the GearshiftEngine
 * 3. How operations with simple bodies work (property access)
 */
fun main() {
    println("=== Operation Invocation Example ===\n")

    // Create the engine
    val engine = GearshiftEngine()

    // Register the Element metaclass (which has the effectiveName operation)
    engine.registerMetaClass(createElementMetaClass())

    // Create an Element instance
    val (elementId, element) = engine.createInstance("Element")
    println("Created Element instance with ID: $elementId")

    // Set the declaredName property
    engine.setProperty(elementId, "declaredName", "MyElement")
    println("Set declaredName to: 'MyElement'")

    // Invoke the effectiveName() operation
    println("\nInvoking effectiveName() operation...")
    val effectiveName = engine.invokeOperation(elementId, "effectiveName")
    println("Result: '$effectiveName'")

    // Demonstrate that effectiveName returns declaredName
    println("\nVerification:")
    val declaredName = engine.getProperty(elementId, "declaredName")
    println("  declaredName property: $declaredName")
    println("  effectiveName() result: $effectiveName")
    println("  Match: ${declaredName == effectiveName}")

    // Test with null declaredName
    println("\n--- Testing with null declaredName ---")
    val (element2Id, _) = engine.createInstance("Element")
    val effectiveName2 = engine.invokeOperation(element2Id, "effectiveName")
    println("effectiveName() with null declaredName: $effectiveName2")

    println("\n=== Example Complete ===")
}
