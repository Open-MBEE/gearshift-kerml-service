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
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

/**
 * TrigFunctions library — trigonometric operations.
 *
 * All trig functions operate in radians. `deg` and `rad` convert between
 * degrees and radians.
 *
 * KerML standard library: TrigFunctions.kerml
 */
class TrigFunctionsLibrary : KernelLibraryFunction {

    override val packageName: String = "TrigFunctions"

    override fun register(registry: FunctionRegistry) {
        registry.register("arccos") { args -> applyArccos(registry, args) }
        registry.register("arcsin") { args -> applyArcsin(registry, args) }
        registry.register("arctan") { args -> applyArctan(registry, args) }
        registry.register("cos") { args -> applyCos(registry, args) }
        registry.register("cot") { args -> applyCot(registry, args) }
        registry.register("deg") { args -> applyDeg(registry, args) }
        registry.register("rad") { args -> applyRad(registry, args) }
        registry.register("sin") { args -> applySin(registry, args) }
        registry.register("tan") { args -> applyTan(registry, args) }
    }

    private fun applySin(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val n = registry.extractNumber(args.getOrNull(0))
        if (n != null) return registry.wrapNumericResult(sin(n.toDouble()))
        return FunctionResult.Value(null)
    }

    private fun applyCos(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val n = registry.extractNumber(args.getOrNull(0))
        if (n != null) return registry.wrapNumericResult(cos(n.toDouble()))
        return FunctionResult.Value(null)
    }

    private fun applyTan(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val n = registry.extractNumber(args.getOrNull(0))
        if (n != null) return registry.wrapNumericResult(tan(n.toDouble()))
        return FunctionResult.Value(null)
    }

    private fun applyCot(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val n = registry.extractNumber(args.getOrNull(0))
        if (n != null) {
            val tanVal = tan(n.toDouble())
            return if (tanVal == 0.0) FunctionResult.Value(null) else registry.wrapNumericResult(1.0 / tanVal)
        }
        return FunctionResult.Value(null)
    }

    private fun applyArcsin(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val n = registry.extractNumber(args.getOrNull(0))
        if (n != null) {
            val result = asin(n.toDouble())
            return if (result.isNaN()) FunctionResult.Value(null) else registry.wrapNumericResult(result)
        }
        return FunctionResult.Value(null)
    }

    private fun applyArccos(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val n = registry.extractNumber(args.getOrNull(0))
        if (n != null) {
            val result = acos(n.toDouble())
            return if (result.isNaN()) FunctionResult.Value(null) else registry.wrapNumericResult(result)
        }
        return FunctionResult.Value(null)
    }

    private fun applyArctan(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val n = registry.extractNumber(args.getOrNull(0))
        if (n != null) return registry.wrapNumericResult(atan(n.toDouble()))
        return FunctionResult.Value(null)
    }

    private fun applyDeg(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        // Convert radians to degrees
        val n = registry.extractNumber(args.getOrNull(0))
        if (n != null) return registry.wrapNumericResult(Math.toDegrees(n.toDouble()))
        return FunctionResult.Value(null)
    }

    private fun applyRad(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        // Convert degrees to radians
        val n = registry.extractNumber(args.getOrNull(0))
        if (n != null) return registry.wrapNumericResult(Math.toRadians(n.toDouble()))
        return FunctionResult.Value(null)
    }
}
