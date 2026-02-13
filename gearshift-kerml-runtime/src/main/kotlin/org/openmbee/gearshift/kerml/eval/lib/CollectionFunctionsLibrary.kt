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
package org.openmbee.gearshift.kerml.eval.lib

import org.openmbee.gearshift.kerml.eval.FunctionRegistry
import org.openmbee.gearshift.kerml.eval.FunctionResult

/**
 * CollectionFunctions library — contains, containsAll.
 *
 * These operate on Collection types (which have an `elements` property) rather
 * than raw Sequences. When the argument is a raw list, delegates to
 * [SequenceFunctionsLibrary] semantics.
 *
 * KerML standard library: CollectionFunctions.kerml
 */
class CollectionFunctionsLibrary : KernelLibraryFunction {

    override val packageName: String = "CollectionFunctions"

    override fun register(registry: FunctionRegistry) {
        registry.register("contains") { args -> applyContains(registry, args) }
        registry.register("containsAll") { args -> applyContainsAll(registry, args) }
    }

    private fun toList(value: Any?): List<Any?> = when (value) {
        is List<*> -> value
        null -> emptyList()
        else -> listOf(value)
    }

    private fun applyContains(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val collection = toList(args.getOrNull(0))
        val element = args.getOrNull(1)
        val contains = collection.any { registry.valuesEqual(it, element) }
        return FunctionResult.LiteralElement(registry.createLiteralBoolean(contains))
    }

    private fun applyContainsAll(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val collection = toList(args.getOrNull(0))
        val elements = toList(args.getOrNull(1))
        val containsAll = elements.all { elem ->
            collection.any { registry.valuesEqual(it, elem) }
        }
        return FunctionResult.LiteralElement(registry.createLiteralBoolean(containsAll))
    }
}
