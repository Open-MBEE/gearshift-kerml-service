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
 * BaseFunctions library — equality, identity, ToString, indexing, sequence concat.
 *
 * KerML standard library: BaseFunctions.kerml
 */
class BaseFunctionsLibrary : KernelLibraryFunction {

    override val packageName: String = "BaseFunctions"

    override fun register(registry: FunctionRegistry) {
        // Equality
        registry.register("=") { args -> applyEqual(registry, args) }
        registry.register("==") { args -> applyEqual(registry, args) }
        registry.register("<>") { args -> applyNotEqual(registry, args) }
        registry.register("!=") { args -> applyNotEqual(registry, args) }

        // Identity
        registry.register("===") { args -> applyIdentical(registry, args) }
        registry.register("!==") { args -> applyNotIdentical(registry, args) }

        // ToString
        registry.register("ToString") { args -> applyToString(registry, args) }

        // Index
        registry.register("#") { args -> applyIndex(registry, args) }

        // Sequence concat (comma operator)
        registry.register(",") { args -> applySequenceConcat(registry, args) }
    }

    private fun applyEqual(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = args.getOrNull(0)
        val b = args.getOrNull(1)
        return FunctionResult.LiteralElement(registry.createLiteralBoolean(registry.valuesEqual(a, b)))
    }

    private fun applyNotEqual(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = args.getOrNull(0)
        val b = args.getOrNull(1)
        return FunctionResult.LiteralElement(registry.createLiteralBoolean(!registry.valuesEqual(a, b)))
    }

    private fun applyIdentical(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = args.getOrNull(0)
        val b = args.getOrNull(1)
        return FunctionResult.LiteralElement(registry.createLiteralBoolean(a === b))
    }

    private fun applyNotIdentical(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = args.getOrNull(0)
        val b = args.getOrNull(1)
        return FunctionResult.LiteralElement(registry.createLiteralBoolean(a !== b))
    }

    private fun applyToString(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val v = args.getOrNull(0)
        val str = when {
            v is String -> v
            v is org.openmbee.mdm.framework.runtime.MDMObject -> {
                val extracted = registry.extractString(v)
                    ?: registry.extractNumber(v)?.toString()
                    ?: registry.extractBoolean(v)?.toString()
                extracted ?: v.className
            }
            v != null -> v.toString()
            else -> "null"
        }
        return FunctionResult.LiteralElement(registry.createLiteralString(str))
    }

    private fun applyIndex(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val collection = args.getOrNull(0)
        val index = registry.extractNumber(args.getOrNull(1))?.toInt()
        if (collection is List<*> && index != null) {
            // KerML indexing is 1-based
            val zeroBasedIndex = index - 1
            return if (zeroBasedIndex in collection.indices) {
                FunctionResult.Value(collection[zeroBasedIndex])
            } else {
                FunctionResult.Value(null)
            }
        }
        return FunctionResult.Value(null)
    }

    private fun applySequenceConcat(@Suppress("UNUSED_PARAMETER") registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val result = mutableListOf<Any?>()
        for (arg in args) {
            when (arg) {
                is List<*> -> result.addAll(arg)
                null -> { /* skip nulls */ }
                else -> result.add(arg)
            }
        }
        return FunctionResult.Value(result)
    }
}
