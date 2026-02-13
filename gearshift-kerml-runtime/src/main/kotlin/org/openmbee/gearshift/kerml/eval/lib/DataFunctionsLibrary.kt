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
 * DataFunctions library — arithmetic, comparison, bitwise, and range operators.
 *
 * Handles Integer/Rational (Real) promotion. Registers once for the numeric
 * hierarchy; leaf libraries only add type-specific functions.
 *
 * KerML standard library: DataFunctions.kerml
 */
class DataFunctionsLibrary : KernelLibraryFunction {

    override val packageName: String = "DataFunctions"

    override fun register(registry: FunctionRegistry) {
        // Arithmetic
        registry.register("+") { args -> applyAdd(registry, args) }
        registry.register("-") { args -> applySubtract(registry, args) }
        registry.register("*") { args -> applyMultiply(registry, args) }
        registry.register("/") { args -> applyDivide(registry, args) }
        registry.register("%") { args -> applyModulo(registry, args) }
        registry.register("**") { args -> applyPower(registry, args) }
        registry.register("minus") { args -> applyUnaryMinus(registry, args) }

        // Comparison
        registry.register("<") { args -> applyLessThan(registry, args) }
        registry.register(">") { args -> applyGreaterThan(registry, args) }
        registry.register("<=") { args -> applyLessEqual(registry, args) }
        registry.register(">=") { args -> applyGreaterEqual(registry, args) }

        // Min/Max
        registry.register("max") { args -> applyMax(registry, args) }
        registry.register("min") { args -> applyMin(registry, args) }

        // Range
        registry.register("..") { args -> applyRange(registry, args) }

        // Bitwise XOR (^)
        registry.register("^") { args -> applyBitwiseXor(registry, args) }

        // Bitwise complement (~)
        registry.register("~") { args -> applyBitwiseComplement(registry, args) }

        // Bitwise OR (|) and AND (&) for integers
        registry.register("|") { args -> applyBitwiseOr(registry, args) }
        registry.register("&") { args -> applyBitwiseAnd(registry, args) }
    }

    // === Arithmetic ===

    private fun applyAdd(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        // String concatenation takes priority
        val s1 = registry.extractString(args.getOrNull(0))
        val s2 = registry.extractString(args.getOrNull(1))
        if (s1 != null && s2 != null) {
            return FunctionResult.LiteralElement(registry.createLiteralString(s1 + s2))
        }

        val a = registry.extractNumber(args.getOrNull(0))
        val b = registry.extractNumber(args.getOrNull(1))
        if (a != null && b != null) {
            return registry.wrapNumericResult(registry.promoteAndApply(a, b) { x, y -> x + y })
        }
        return FunctionResult.Value(null)
    }

    private fun applySubtract(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        if (args.size == 1) return applyUnaryMinus(registry, args)
        val a = registry.extractNumber(args.getOrNull(0))
        val b = registry.extractNumber(args.getOrNull(1))
        if (a != null && b != null) {
            return registry.wrapNumericResult(registry.promoteAndApply(a, b) { x, y -> x - y })
        }
        return FunctionResult.Value(null)
    }

    private fun applyMultiply(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = registry.extractNumber(args.getOrNull(0))
        val b = registry.extractNumber(args.getOrNull(1))
        if (a != null && b != null) {
            return registry.wrapNumericResult(registry.promoteAndApply(a, b) { x, y -> x * y })
        }
        return FunctionResult.Value(null)
    }

    private fun applyDivide(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = registry.extractNumber(args.getOrNull(0))
        val b = registry.extractNumber(args.getOrNull(1))
        if (a != null && b != null) {
            if (b.toDouble() == 0.0) return FunctionResult.Value(null)
            if (a is Long && b is Long && a % b == 0L) {
                return registry.wrapNumericResult(a / b)
            }
            return registry.wrapNumericResult(a.toDouble() / b.toDouble())
        }
        return FunctionResult.Value(null)
    }

    private fun applyModulo(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = registry.extractNumber(args.getOrNull(0))
        val b = registry.extractNumber(args.getOrNull(1))
        if (a != null && b != null) {
            if (b.toDouble() == 0.0) return FunctionResult.Value(null)
            return if (a is Long && b is Long) {
                registry.wrapNumericResult(a % b)
            } else {
                registry.wrapNumericResult(a.toDouble() % b.toDouble())
            }
        }
        return FunctionResult.Value(null)
    }

    private fun applyPower(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = registry.extractNumber(args.getOrNull(0))
        val b = registry.extractNumber(args.getOrNull(1))
        if (a != null && b != null) {
            val result = Math.pow(a.toDouble(), b.toDouble())
            if (a is Long && b is Long && b >= 0 && result == result.toLong().toDouble()) {
                return registry.wrapNumericResult(result.toLong())
            }
            return registry.wrapNumericResult(result)
        }
        return FunctionResult.Value(null)
    }

    private fun applyUnaryMinus(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = registry.extractNumber(args.getOrNull(0))
        if (a != null) {
            return if (a is Long) registry.wrapNumericResult(-a) else registry.wrapNumericResult(-a.toDouble())
        }
        return FunctionResult.Value(null)
    }

    // === Comparison ===

    private fun applyLessThan(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = registry.extractNumber(args.getOrNull(0))
        val b = registry.extractNumber(args.getOrNull(1))
        if (a != null && b != null) {
            return FunctionResult.LiteralElement(registry.createLiteralBoolean(a.toDouble() < b.toDouble()))
        }
        return FunctionResult.Value(null)
    }

    private fun applyGreaterThan(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = registry.extractNumber(args.getOrNull(0))
        val b = registry.extractNumber(args.getOrNull(1))
        if (a != null && b != null) {
            return FunctionResult.LiteralElement(registry.createLiteralBoolean(a.toDouble() > b.toDouble()))
        }
        return FunctionResult.Value(null)
    }

    private fun applyLessEqual(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = registry.extractNumber(args.getOrNull(0))
        val b = registry.extractNumber(args.getOrNull(1))
        if (a != null && b != null) {
            return FunctionResult.LiteralElement(registry.createLiteralBoolean(a.toDouble() <= b.toDouble()))
        }
        return FunctionResult.Value(null)
    }

    private fun applyGreaterEqual(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = registry.extractNumber(args.getOrNull(0))
        val b = registry.extractNumber(args.getOrNull(1))
        if (a != null && b != null) {
            return FunctionResult.LiteralElement(registry.createLiteralBoolean(a.toDouble() >= b.toDouble()))
        }
        return FunctionResult.Value(null)
    }

    // === Min/Max ===

    private fun applyMax(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = registry.extractNumber(args.getOrNull(0))
        val b = registry.extractNumber(args.getOrNull(1))
        if (a != null && b != null) {
            return if (a.toDouble() >= b.toDouble()) {
                registry.wrapNumericResult(a)
            } else {
                registry.wrapNumericResult(b)
            }
        }
        return FunctionResult.Value(null)
    }

    private fun applyMin(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = registry.extractNumber(args.getOrNull(0))
        val b = registry.extractNumber(args.getOrNull(1))
        if (a != null && b != null) {
            return if (a.toDouble() <= b.toDouble()) {
                registry.wrapNumericResult(a)
            } else {
                registry.wrapNumericResult(b)
            }
        }
        return FunctionResult.Value(null)
    }

    // === Range ===

    private fun applyRange(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val start = registry.extractNumber(args.getOrNull(0))?.toLong()
        val end = registry.extractNumber(args.getOrNull(1))?.toLong()
        if (start != null && end != null) {
            val range = (start..end).map { registry.createLiteralInteger(it) }
            return FunctionResult.Value(range)
        }
        return FunctionResult.Value(null)
    }

    // === Bitwise ===

    private fun applyBitwiseXor(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = registry.extractNumber(args.getOrNull(0))?.toLong()
        val b = registry.extractNumber(args.getOrNull(1))?.toLong()
        if (a != null && b != null) {
            return registry.wrapNumericResult(a xor b)
        }
        return FunctionResult.Value(null)
    }

    private fun applyBitwiseComplement(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = registry.extractNumber(args.getOrNull(0))?.toLong()
        if (a != null) {
            return registry.wrapNumericResult(a.inv())
        }
        return FunctionResult.Value(null)
    }

    private fun applyBitwiseOr(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        // Try boolean first, fall back to integer bitwise
        val boolA = registry.extractBoolean(args.getOrNull(0))
        val boolB = registry.extractBoolean(args.getOrNull(1))
        if (boolA != null && boolB != null) {
            return FunctionResult.LiteralElement(registry.createLiteralBoolean(boolA || boolB))
        }
        val a = registry.extractNumber(args.getOrNull(0))?.toLong()
        val b = registry.extractNumber(args.getOrNull(1))?.toLong()
        if (a != null && b != null) {
            return registry.wrapNumericResult(a or b)
        }
        return FunctionResult.Value(null)
    }

    private fun applyBitwiseAnd(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        // Try boolean first, fall back to integer bitwise
        val boolA = registry.extractBoolean(args.getOrNull(0))
        val boolB = registry.extractBoolean(args.getOrNull(1))
        if (boolA != null && boolB != null) {
            return FunctionResult.LiteralElement(registry.createLiteralBoolean(boolA && boolB))
        }
        val a = registry.extractNumber(args.getOrNull(0))?.toLong()
        val b = registry.extractNumber(args.getOrNull(1))?.toLong()
        if (a != null && b != null) {
            return registry.wrapNumericResult(a and b)
        }
        return FunctionResult.Value(null)
    }
}
