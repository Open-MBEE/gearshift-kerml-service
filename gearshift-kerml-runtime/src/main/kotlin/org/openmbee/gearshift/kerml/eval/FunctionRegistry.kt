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

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MDMObject

private val logger = KotlinLogging.logger {}

/**
 * Result of applying a kernel function.
 */
sealed class FunctionResult {
    data class Value(val value: Any?) : FunctionResult()
    data class LiteralElement(val element: MDMObject) : FunctionResult()
}

/**
 * A kernel function that takes a list of arguments and returns a [FunctionResult].
 */
typealias KernelFunction = (List<Any?>) -> FunctionResult

/**
 * Central registry for KerML kernel library functions.
 *
 * Holds the function map and provides shared helper methods for extracting values
 * from literal MDMObjects, creating literal elements, and numeric promotion.
 * Library classes register their functions into this registry via [register].
 */
class FunctionRegistry(val engine: MDMEngine) {

    private val functions = mutableMapOf<String, KernelFunction>()

    /**
     * Register a function under the given operator name.
     */
    fun register(operator: String, function: KernelFunction) {
        functions[operator] = function
        logger.trace { "Registered kernel function: $operator" }
    }

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
     * Check whether this registry has a function for the given operator.
     */
    fun hasOperator(operator: String): Boolean = operator in functions

    /**
     * Return the number of registered functions.
     */
    val size: Int get() = functions.size

    // === Literal Creation Helpers ===

    fun createLiteralBoolean(value: Boolean): MDMObject {
        val literal = engine.createElement("LiteralBoolean")
        engine.setPropertyValue(literal, "value", value)
        return literal
    }

    fun createLiteralInteger(value: Long): MDMObject {
        val literal = engine.createElement("LiteralInteger")
        engine.setPropertyValue(literal, "value", value)
        return literal
    }

    fun createLiteralRational(value: Double): MDMObject {
        val literal = engine.createElement("LiteralRational")
        engine.setPropertyValue(literal, "value", value)
        return literal
    }

    fun createLiteralString(value: String): MDMObject {
        val literal = engine.createElement("LiteralString")
        engine.setPropertyValue(literal, "value", value)
        return literal
    }

    // === Value Extraction Helpers ===

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

    fun extractBoolean(value: Any?): Boolean? = when (value) {
        is Boolean -> value
        is MDMObject -> when (value.className) {
            "LiteralBoolean" -> engine.getPropertyValue(value, "value") as? Boolean
            else -> null
        }
        else -> null
    }

    fun extractString(value: Any?): String? = when (value) {
        is String -> value
        is MDMObject -> when (value.className) {
            "LiteralString" -> engine.getPropertyValue(value, "value") as? String
            else -> null
        }
        else -> null
    }

    // === Numeric Helpers ===

    /**
     * Promote two numbers to a common type and apply the operation.
     * If either operand is a Double, promotes both to Double.
     * Otherwise stays as Long.
     */
    fun promoteAndApply(a: Number, b: Number, op: (Double, Double) -> Double): Number {
        return if (a is Double || b is Double || a is Float || b is Float) {
            op(a.toDouble(), b.toDouble())
        } else {
            val result = op(a.toDouble(), b.toDouble())
            if (result == result.toLong().toDouble() && !result.isInfinite()) {
                result.toLong()
            } else {
                result
            }
        }
    }

    fun wrapNumericResult(value: Number): FunctionResult {
        return when (value) {
            is Long -> FunctionResult.LiteralElement(createLiteralInteger(value))
            is Int -> FunctionResult.LiteralElement(createLiteralInteger(value.toLong()))
            else -> FunctionResult.LiteralElement(createLiteralRational(value.toDouble()))
        }
    }

    /**
     * Compare two values for equality with type promotion.
     */
    fun valuesEqual(a: Any?, b: Any?): Boolean {
        if (a == null && b == null) return true
        if (a == null || b == null) return false

        val numA = extractNumber(a)
        val numB = extractNumber(b)
        if (numA != null && numB != null) {
            return numA.toDouble() == numB.toDouble()
        }

        val boolA = extractBoolean(a)
        val boolB = extractBoolean(b)
        if (boolA != null && boolB != null) {
            return boolA == boolB
        }

        val strA = extractString(a)
        val strB = extractString(b)
        if (strA != null && strB != null) {
            return strA == strB
        }

        if (a is MDMObject && b is MDMObject) {
            return a.id == b.id
        }

        return a == b
    }

    fun isNullExpression(value: Any?): Boolean {
        return value is MDMObject && value.className == "NullExpression"
    }
}
