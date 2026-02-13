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
 * StringFunctions library — string operations (Size, Substring, Concat) and
 * string comparison operators.
 *
 * Note: String `+` (concatenation) is handled by [DataFunctionsLibrary] since
 * `+` is overloaded for both numeric and string operands.
 *
 * KerML standard library: StringFunctions.kerml
 */
class StringFunctionsLibrary : KernelLibraryFunction {

    override val packageName: String = "StringFunctions"

    override fun register(registry: FunctionRegistry) {
        registry.register("Concat") { args -> applyConcat(registry, args) }
        registry.register("Length") { args -> applyLength(registry, args) }
        registry.register("Size") { args -> applySize(registry, args) }
        registry.register("Substring") { args -> applySubstring(registry, args) }

        // String comparison (registered with String prefix to avoid overwriting numeric)
        registry.register("String<") { args -> applyStringLessThan(registry, args) }
        registry.register("String>") { args -> applyStringGreaterThan(registry, args) }
        registry.register("String<=") { args -> applyStringLessEqual(registry, args) }
        registry.register("String>=") { args -> applyStringGreaterEqual(registry, args) }
    }

    private fun applyConcat(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val s1 = registry.extractString(args.getOrNull(0))
        val s2 = registry.extractString(args.getOrNull(1))
        if (s1 != null && s2 != null) {
            return FunctionResult.LiteralElement(registry.createLiteralString(s1 + s2))
        }
        return FunctionResult.Value(null)
    }

    private fun applyLength(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val s = registry.extractString(args.getOrNull(0))
        if (s != null) {
            return FunctionResult.LiteralElement(registry.createLiteralInteger(s.length.toLong()))
        }
        return FunctionResult.Value(null)
    }

    private fun applySize(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val s = registry.extractString(args.getOrNull(0))
        if (s != null) {
            return FunctionResult.LiteralElement(registry.createLiteralInteger(s.length.toLong()))
        }
        return FunctionResult.Value(null)
    }

    private fun applySubstring(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val s = registry.extractString(args.getOrNull(0))
        val start = registry.extractNumber(args.getOrNull(1))?.toInt()
        val end = registry.extractNumber(args.getOrNull(2))?.toInt()
        if (s != null && start != null && end != null) {
            val clampedStart = start.coerceIn(0, s.length)
            val clampedEnd = end.coerceIn(clampedStart, s.length)
            return FunctionResult.LiteralElement(registry.createLiteralString(s.substring(clampedStart, clampedEnd)))
        }
        return FunctionResult.Value(null)
    }

    // === String comparison ===

    private fun applyStringLessThan(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = registry.extractString(args.getOrNull(0))
        val b = registry.extractString(args.getOrNull(1))
        if (a != null && b != null) {
            return FunctionResult.LiteralElement(registry.createLiteralBoolean(a < b))
        }
        return FunctionResult.Value(null)
    }

    private fun applyStringGreaterThan(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = registry.extractString(args.getOrNull(0))
        val b = registry.extractString(args.getOrNull(1))
        if (a != null && b != null) {
            return FunctionResult.LiteralElement(registry.createLiteralBoolean(a > b))
        }
        return FunctionResult.Value(null)
    }

    private fun applyStringLessEqual(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = registry.extractString(args.getOrNull(0))
        val b = registry.extractString(args.getOrNull(1))
        if (a != null && b != null) {
            return FunctionResult.LiteralElement(registry.createLiteralBoolean(a <= b))
        }
        return FunctionResult.Value(null)
    }

    private fun applyStringGreaterEqual(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = registry.extractString(args.getOrNull(0))
        val b = registry.extractString(args.getOrNull(1))
        if (a != null && b != null) {
            return FunctionResult.LiteralElement(registry.createLiteralBoolean(a >= b))
        }
        return FunctionResult.Value(null)
    }
}
