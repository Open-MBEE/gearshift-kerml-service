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
 * RationalFunctions library — Rational-specific conversions.
 *
 * Note: `floor` and `round` are in [RealFunctionsLibrary] since Real subsumes Rational
 * in KerML. `ToInteger` is in [IntegerFunctionsLibrary].
 *
 * KerML standard library: RationalFunctions.kerml
 */
class RationalFunctionsLibrary : KernelLibraryFunction {

    override val packageName: String = "RationalFunctions"

    override fun register(registry: FunctionRegistry) {
        registry.register("ToRational") { args -> applyToRational(registry, args) }
    }

    private fun applyToRational(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val v = args.getOrNull(0)
        val n = registry.extractNumber(v)
        if (n != null) {
            return FunctionResult.LiteralElement(registry.createLiteralRational(n.toDouble()))
        }
        val s = registry.extractString(v)
        if (s != null) {
            val parsed = s.toDoubleOrNull()
            if (parsed != null) {
                return FunctionResult.LiteralElement(registry.createLiteralRational(parsed))
            }
        }
        return FunctionResult.Value(null)
    }
}
