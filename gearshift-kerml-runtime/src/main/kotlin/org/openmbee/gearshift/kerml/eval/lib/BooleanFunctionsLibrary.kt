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
 * BooleanFunctions library — not, xor, ToBoolean.
 *
 * Note: `and`, `or`, `implies` are in [ControlFunctionsLibrary] as they have
 * short-circuit semantics. `|` and `&` are in [DataFunctionsLibrary] as they
 * are overloaded for both boolean and integer bitwise operations.
 *
 * KerML standard library: BooleanFunctions.kerml
 */
class BooleanFunctionsLibrary : KernelLibraryFunction {

    override val packageName: String = "BooleanFunctions"

    override fun register(registry: FunctionRegistry) {
        registry.register("not") { args -> applyNot(registry, args) }
        registry.register("xor") { args -> applyXor(registry, args) }
        registry.register("ToBoolean") { args -> applyToBoolean(registry, args) }
    }

    private fun applyNot(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = registry.extractBoolean(args.getOrNull(0))
        if (a != null) {
            return FunctionResult.LiteralElement(registry.createLiteralBoolean(!a))
        }
        return FunctionResult.Value(null)
    }

    private fun applyXor(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = registry.extractBoolean(args.getOrNull(0))
        val b = registry.extractBoolean(args.getOrNull(1))
        if (a != null && b != null) {
            return FunctionResult.LiteralElement(registry.createLiteralBoolean(a xor b))
        }
        return FunctionResult.Value(null)
    }

    private fun applyToBoolean(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val v = args.getOrNull(0)
        val result = when {
            v is Boolean -> v
            v is String -> v.equals("true", ignoreCase = true)
            else -> registry.extractBoolean(v)
        }
        return if (result != null) {
            FunctionResult.LiteralElement(registry.createLiteralBoolean(result))
        } else {
            FunctionResult.Value(null)
        }
    }
}
