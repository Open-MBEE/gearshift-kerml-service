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
package org.openmbee.gearshift.kerml.eval

import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MDMObject

/**
 * Registry mapping KerML operator strings to native evaluation functions.
 *
 * Covers the KerML standard library function packages:
 * - BaseFunctions: arithmetic (+, -, *, /, %, **), comparison (<, >, <=, >=, ==, !=), equality (=, <>)
 * - DataFunctions: string operations (ToString, +)
 * - ControlFunctions: conditional (if, ??), index (#)
 *
 * Handles Integer/Rational (Real) promotion and short-circuit boolean evaluation.
 */
class KernelFunctionLibrary(private val engine: MDMEngine) {

    /**
     * Result of applying a kernel function.
     */
    sealed class FunctionResult {
        data class Value(val value: Any?) : FunctionResult()
        data class LiteralElement(val element: MDMObject) : FunctionResult()
    }

    private val functions: Map<String, (List<Any?>) -> FunctionResult> = buildFunctionMap()

    /**
     * Apply a kernel function by operator name.
     *
     * @param operator The KerML operator string (e.g., "+", "and", "if")
     * @param arguments Evaluated argument values
     * @return The result of applying the function, or null if the operator is unknown
     */
    fun apply(operator: String, arguments: List<Any?>): FunctionResult? {
        val fn = functions[operator] ?: return null
        return fn(arguments)
    }

    /**
     * Check whether this library has a native implementation for the given operator.
     */
    fun hasOperator(operator: String): Boolean = operator in functions

    /**
     * Create a LiteralBoolean MDMObject with the given value.
     */
    fun createLiteralBoolean(value: Boolean): MDMObject {
        val literal = engine.createElement("LiteralBoolean")
        engine.setPropertyValue(literal, "value", value)
        return literal
    }

    /**
     * Create a LiteralInteger MDMObject with the given value.
     */
    fun createLiteralInteger(value: Long): MDMObject {
        val literal = engine.createElement("LiteralInteger")
        engine.setPropertyValue(literal, "value", value)
        return literal
    }

    /**
     * Create a LiteralRational MDMObject with the given value.
     */
    fun createLiteralRational(value: Double): MDMObject {
        val literal = engine.createElement("LiteralRational")
        engine.setPropertyValue(literal, "value", value)
        return literal
    }

    /**
     * Create a LiteralString MDMObject with the given value.
     */
    fun createLiteralString(value: String): MDMObject {
        val literal = engine.createElement("LiteralString")
        engine.setPropertyValue(literal, "value", value)
        return literal
    }

    /**
     * Extract a numeric value from a literal MDMObject or raw value.
     */
    fun extractNumber(value: Any?): Number? = when (value) {
        is Number -> value
        is MDMObject -> when (value.className) {
            "LiteralInteger" -> engine.getPropertyValue(value, "value") as? Number
            "LiteralRational" -> engine.getPropertyValue(value, "value") as? Number
            "LiteralInfinity" -> Double.POSITIVE_INFINITY
            else -> null
        }
        else -> null
    }

    /**
     * Extract a boolean value from a literal MDMObject or raw value.
     */
    fun extractBoolean(value: Any?): Boolean? = when (value) {
        is Boolean -> value
        is MDMObject -> when (value.className) {
            "LiteralBoolean" -> engine.getPropertyValue(value, "value") as? Boolean
            else -> null
        }
        else -> null
    }

    /**
     * Extract a string value from a literal MDMObject or raw value.
     */
    fun extractString(value: Any?): String? = when (value) {
        is String -> value
        is MDMObject -> when (value.className) {
            "LiteralString" -> engine.getPropertyValue(value, "value") as? String
            else -> null
        }
        else -> null
    }

    private fun buildFunctionMap(): Map<String, (List<Any?>) -> FunctionResult> {
        val map = mutableMapOf<String, (List<Any?>) -> FunctionResult>()

        // === BaseFunctions: Arithmetic ===
        map["+"] = ::applyAdd
        map["-"] = ::applySubtract
        map["*"] = ::applyMultiply
        map["/"] = ::applyDivide
        map["%"] = ::applyModulo
        map["**"] = ::applyPower

        // === BaseFunctions: Comparison ===
        map["<"] = ::applyLessThan
        map[">"] = ::applyGreaterThan
        map["<="] = ::applyLessEqual
        map[">="] = ::applyGreaterEqual

        // === BaseFunctions: Equality ===
        map["="] = ::applyEqual
        map["=="] = ::applyEqual
        map["<>"] = ::applyNotEqual
        map["!="] = ::applyNotEqual

        // === BaseFunctions: Boolean ===
        map["and"] = ::applyAnd
        map["or"] = ::applyOr
        map["not"] = ::applyNot
        map["xor"] = ::applyXor
        map["implies"] = ::applyImplies

        // === BaseFunctions: Unary minus ===
        map["minus"] = ::applyUnaryMinus

        // === DataFunctions: String ===
        map["ToString"] = ::applyToString
        map["Size"] = ::applyStringSize
        map["Substring"] = ::applySubstring
        map["Concat"] = ::applyConcat

        // === ControlFunctions ===
        map["if"] = ::applyIf
        map["??"] = ::applyNullCoalesce
        map["#"] = ::applyIndex

        return map
    }

    // === Arithmetic Implementations ===

    private fun applyAdd(args: List<Any?>): FunctionResult {
        // String concatenation
        val s1 = extractString(args.getOrNull(0))
        val s2 = extractString(args.getOrNull(1))
        if (s1 != null && s2 != null) {
            return FunctionResult.LiteralElement(createLiteralString(s1 + s2))
        }

        val a = extractNumber(args.getOrNull(0))
        val b = extractNumber(args.getOrNull(1))
        if (a != null && b != null) {
            return wrapNumericResult(promoteAndApply(a, b) { x, y -> x + y })
        }
        return FunctionResult.Value(null)
    }

    private fun applySubtract(args: List<Any?>): FunctionResult {
        if (args.size == 1) return applyUnaryMinus(args)
        val a = extractNumber(args.getOrNull(0))
        val b = extractNumber(args.getOrNull(1))
        if (a != null && b != null) {
            return wrapNumericResult(promoteAndApply(a, b) { x, y -> x - y })
        }
        return FunctionResult.Value(null)
    }

    private fun applyMultiply(args: List<Any?>): FunctionResult {
        val a = extractNumber(args.getOrNull(0))
        val b = extractNumber(args.getOrNull(1))
        if (a != null && b != null) {
            return wrapNumericResult(promoteAndApply(a, b) { x, y -> x * y })
        }
        return FunctionResult.Value(null)
    }

    private fun applyDivide(args: List<Any?>): FunctionResult {
        val a = extractNumber(args.getOrNull(0))
        val b = extractNumber(args.getOrNull(1))
        if (a != null && b != null) {
            if (b.toDouble() == 0.0) return FunctionResult.Value(null)
            // Integer division stays integer if both operands are integer
            if (a is Long && b is Long && a % b == 0L) {
                return wrapNumericResult(a / b)
            }
            return wrapNumericResult(a.toDouble() / b.toDouble())
        }
        return FunctionResult.Value(null)
    }

    private fun applyModulo(args: List<Any?>): FunctionResult {
        val a = extractNumber(args.getOrNull(0))
        val b = extractNumber(args.getOrNull(1))
        if (a != null && b != null) {
            if (b.toDouble() == 0.0) return FunctionResult.Value(null)
            return if (a is Long && b is Long) {
                wrapNumericResult(a % b)
            } else {
                wrapNumericResult(a.toDouble() % b.toDouble())
            }
        }
        return FunctionResult.Value(null)
    }

    private fun applyPower(args: List<Any?>): FunctionResult {
        val a = extractNumber(args.getOrNull(0))
        val b = extractNumber(args.getOrNull(1))
        if (a != null && b != null) {
            val result = Math.pow(a.toDouble(), b.toDouble())
            // If both args are integer and result is exact integer, return as integer
            if (a is Long && b is Long && b >= 0 && result == result.toLong().toDouble()) {
                return wrapNumericResult(result.toLong())
            }
            return wrapNumericResult(result)
        }
        return FunctionResult.Value(null)
    }

    private fun applyUnaryMinus(args: List<Any?>): FunctionResult {
        val a = extractNumber(args.getOrNull(0))
        if (a != null) {
            return if (a is Long) wrapNumericResult(-a) else wrapNumericResult(-a.toDouble())
        }
        return FunctionResult.Value(null)
    }

    // === Comparison Implementations ===

    private fun applyLessThan(args: List<Any?>): FunctionResult {
        val a = extractNumber(args.getOrNull(0))
        val b = extractNumber(args.getOrNull(1))
        if (a != null && b != null) {
            return FunctionResult.LiteralElement(createLiteralBoolean(a.toDouble() < b.toDouble()))
        }
        return FunctionResult.Value(null)
    }

    private fun applyGreaterThan(args: List<Any?>): FunctionResult {
        val a = extractNumber(args.getOrNull(0))
        val b = extractNumber(args.getOrNull(1))
        if (a != null && b != null) {
            return FunctionResult.LiteralElement(createLiteralBoolean(a.toDouble() > b.toDouble()))
        }
        return FunctionResult.Value(null)
    }

    private fun applyLessEqual(args: List<Any?>): FunctionResult {
        val a = extractNumber(args.getOrNull(0))
        val b = extractNumber(args.getOrNull(1))
        if (a != null && b != null) {
            return FunctionResult.LiteralElement(createLiteralBoolean(a.toDouble() <= b.toDouble()))
        }
        return FunctionResult.Value(null)
    }

    private fun applyGreaterEqual(args: List<Any?>): FunctionResult {
        val a = extractNumber(args.getOrNull(0))
        val b = extractNumber(args.getOrNull(1))
        if (a != null && b != null) {
            return FunctionResult.LiteralElement(createLiteralBoolean(a.toDouble() >= b.toDouble()))
        }
        return FunctionResult.Value(null)
    }

    // === Equality Implementations ===

    private fun applyEqual(args: List<Any?>): FunctionResult {
        val a = args.getOrNull(0)
        val b = args.getOrNull(1)
        val result = valuesEqual(a, b)
        return FunctionResult.LiteralElement(createLiteralBoolean(result))
    }

    private fun applyNotEqual(args: List<Any?>): FunctionResult {
        val a = args.getOrNull(0)
        val b = args.getOrNull(1)
        val result = !valuesEqual(a, b)
        return FunctionResult.LiteralElement(createLiteralBoolean(result))
    }

    private fun valuesEqual(a: Any?, b: Any?): Boolean {
        if (a == null && b == null) return true
        if (a == null || b == null) return false

        // Numeric comparison with promotion
        val numA = extractNumber(a)
        val numB = extractNumber(b)
        if (numA != null && numB != null) {
            return numA.toDouble() == numB.toDouble()
        }

        // Boolean comparison
        val boolA = extractBoolean(a)
        val boolB = extractBoolean(b)
        if (boolA != null && boolB != null) {
            return boolA == boolB
        }

        // String comparison
        val strA = extractString(a)
        val strB = extractString(b)
        if (strA != null && strB != null) {
            return strA == strB
        }

        // Object identity
        if (a is MDMObject && b is MDMObject) {
            return a.id == b.id
        }

        return a == b
    }

    // === Boolean Implementations ===

    private fun applyAnd(args: List<Any?>): FunctionResult {
        val a = extractBoolean(args.getOrNull(0))
        // Short-circuit: if first is false, result is false
        if (a == false) return FunctionResult.LiteralElement(createLiteralBoolean(false))
        val b = extractBoolean(args.getOrNull(1))
        if (a != null && b != null) {
            return FunctionResult.LiteralElement(createLiteralBoolean(a && b))
        }
        return FunctionResult.Value(null)
    }

    private fun applyOr(args: List<Any?>): FunctionResult {
        val a = extractBoolean(args.getOrNull(0))
        // Short-circuit: if first is true, result is true
        if (a == true) return FunctionResult.LiteralElement(createLiteralBoolean(true))
        val b = extractBoolean(args.getOrNull(1))
        if (a != null && b != null) {
            return FunctionResult.LiteralElement(createLiteralBoolean(a || b))
        }
        return FunctionResult.Value(null)
    }

    private fun applyNot(args: List<Any?>): FunctionResult {
        val a = extractBoolean(args.getOrNull(0))
        if (a != null) {
            return FunctionResult.LiteralElement(createLiteralBoolean(!a))
        }
        return FunctionResult.Value(null)
    }

    private fun applyXor(args: List<Any?>): FunctionResult {
        val a = extractBoolean(args.getOrNull(0))
        val b = extractBoolean(args.getOrNull(1))
        if (a != null && b != null) {
            return FunctionResult.LiteralElement(createLiteralBoolean(a xor b))
        }
        return FunctionResult.Value(null)
    }

    private fun applyImplies(args: List<Any?>): FunctionResult {
        val a = extractBoolean(args.getOrNull(0))
        // Short-circuit: if antecedent is false, implication is true
        if (a == false) return FunctionResult.LiteralElement(createLiteralBoolean(true))
        val b = extractBoolean(args.getOrNull(1))
        if (a != null && b != null) {
            return FunctionResult.LiteralElement(createLiteralBoolean(!a || b))
        }
        return FunctionResult.Value(null)
    }

    // === String Implementations ===

    private fun applyToString(args: List<Any?>): FunctionResult {
        val v = args.getOrNull(0)
        val str = when {
            v is String -> v
            v is MDMObject -> {
                val extracted = extractString(v) ?: extractNumber(v)?.toString() ?: extractBoolean(v)?.toString()
                extracted ?: v.className
            }
            v != null -> v.toString()
            else -> "null"
        }
        return FunctionResult.LiteralElement(createLiteralString(str))
    }

    private fun applyStringSize(args: List<Any?>): FunctionResult {
        val s = extractString(args.getOrNull(0))
        if (s != null) {
            return FunctionResult.LiteralElement(createLiteralInteger(s.length.toLong()))
        }
        return FunctionResult.Value(null)
    }

    private fun applySubstring(args: List<Any?>): FunctionResult {
        val s = extractString(args.getOrNull(0))
        val start = extractNumber(args.getOrNull(1))?.toInt()
        val end = extractNumber(args.getOrNull(2))?.toInt()
        if (s != null && start != null && end != null) {
            val clampedStart = start.coerceIn(0, s.length)
            val clampedEnd = end.coerceIn(clampedStart, s.length)
            return FunctionResult.LiteralElement(createLiteralString(s.substring(clampedStart, clampedEnd)))
        }
        return FunctionResult.Value(null)
    }

    private fun applyConcat(args: List<Any?>): FunctionResult {
        val s1 = extractString(args.getOrNull(0))
        val s2 = extractString(args.getOrNull(1))
        if (s1 != null && s2 != null) {
            return FunctionResult.LiteralElement(createLiteralString(s1 + s2))
        }
        return FunctionResult.Value(null)
    }

    // === Control Function Implementations ===

    private fun applyIf(args: List<Any?>): FunctionResult {
        // if(condition, thenValue, elseValue)
        val condition = extractBoolean(args.getOrNull(0))
        return if (condition == true) {
            FunctionResult.Value(args.getOrNull(1))
        } else {
            FunctionResult.Value(args.getOrNull(2))
        }
    }

    private fun applyNullCoalesce(args: List<Any?>): FunctionResult {
        // ??(value, default) - returns value if non-null, else default
        val value = args.getOrNull(0)
        return if (value != null && !isNullExpression(value)) {
            FunctionResult.Value(value)
        } else {
            FunctionResult.Value(args.getOrNull(1))
        }
    }

    private fun applyIndex(args: List<Any?>): FunctionResult {
        // #(sequence, index) - returns element at index
        val collection = args.getOrNull(0)
        val index = extractNumber(args.getOrNull(1))?.toInt()
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

    // === Helpers ===

    private fun isNullExpression(value: Any?): Boolean {
        return value is MDMObject && value.className == "NullExpression"
    }

    /**
     * Promote two numbers to a common type and apply the operation.
     * If either operand is a Double, promotes both to Double.
     * Otherwise stays as Long.
     */
    private fun promoteAndApply(a: Number, b: Number, op: (Double, Double) -> Double): Number {
        return if (a is Double || b is Double || a is Float || b is Float) {
            op(a.toDouble(), b.toDouble())
        } else {
            val result = op(a.toDouble(), b.toDouble())
            // If result is exact integer, return as Long
            if (result == result.toLong().toDouble() && !result.isInfinite()) {
                result.toLong()
            } else {
                result
            }
        }
    }

    private fun wrapNumericResult(value: Number): FunctionResult {
        return when (value) {
            is Long -> FunctionResult.LiteralElement(createLiteralInteger(value))
            is Int -> FunctionResult.LiteralElement(createLiteralInteger(value.toLong()))
            else -> FunctionResult.LiteralElement(createLiteralRational(value.toDouble()))
        }
    }
}
