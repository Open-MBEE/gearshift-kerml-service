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
 * IntegerFunctions library — integer-specific conversions.
 *
 * Note: Range (`..`) and arithmetic are in [DataFunctionsLibrary].
 *
 * KerML standard library: IntegerFunctions.kerml
 */
class IntegerFunctionsLibrary : KernelLibraryFunction {

    override val packageName: String = "IntegerFunctions"

    override fun register(registry: FunctionRegistry) {
        registry.register("ToInteger") { args -> applyToInteger(registry, args) }
        registry.register("ToNatural") { args -> applyToNatural(registry, args) }
    }

    private fun applyToInteger(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val v = args.getOrNull(0)
        val n = registry.extractNumber(v)
        if (n != null) {
            return FunctionResult.LiteralElement(registry.createLiteralInteger(n.toLong()))
        }
        val s = registry.extractString(v)
        if (s != null) {
            val parsed = s.toLongOrNull()
            if (parsed != null) {
                return FunctionResult.LiteralElement(registry.createLiteralInteger(parsed))
            }
        }
        return FunctionResult.Value(null)
    }

    private fun applyToNatural(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val n = registry.extractNumber(args.getOrNull(0))
        if (n != null) {
            val value = n.toLong()
            return if (value >= 0) {
                FunctionResult.LiteralElement(registry.createLiteralInteger(value))
            } else {
                FunctionResult.Value(null)
            }
        }
        return FunctionResult.Value(null)
    }
}
