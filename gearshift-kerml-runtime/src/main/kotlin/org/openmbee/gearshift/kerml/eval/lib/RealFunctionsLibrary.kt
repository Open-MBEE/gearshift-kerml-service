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
import kotlin.math.floor
import kotlin.math.roundToLong
import kotlin.math.sqrt

/**
 * RealFunctions library — sqrt, floor, round, ToInteger, ToReal.
 *
 * KerML standard library: RealFunctions.kerml
 */
class RealFunctionsLibrary : KernelLibraryFunction {

    override val packageName: String = "RealFunctions"

    override fun register(registry: FunctionRegistry) {
        registry.register("ToReal") { args -> applyToReal(registry, args) }
        registry.register("floor") { args -> applyFloor(registry, args) }
        registry.register("round") { args -> applyRound(registry, args) }
        registry.register("sqrt") { args -> applySqrt(registry, args) }
    }

    private fun applySqrt(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val n = registry.extractNumber(args.getOrNull(0))
        if (n != null) {
            val result = sqrt(n.toDouble())
            return if (result.isNaN()) FunctionResult.Value(null) else registry.wrapNumericResult(result)
        }
        return FunctionResult.Value(null)
    }

    private fun applyFloor(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val n = registry.extractNumber(args.getOrNull(0))
        if (n != null) {
            return FunctionResult.LiteralElement(registry.createLiteralInteger(floor(n.toDouble()).toLong()))
        }
        return FunctionResult.Value(null)
    }

    private fun applyRound(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val n = registry.extractNumber(args.getOrNull(0))
        if (n != null) {
            return FunctionResult.LiteralElement(registry.createLiteralInteger(n.toDouble().roundToLong()))
        }
        return FunctionResult.Value(null)
    }

    private fun applyToReal(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
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
