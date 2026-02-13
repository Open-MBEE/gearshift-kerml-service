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
import kotlin.math.absoluteValue

/**
 * NumericalFunctions library — abs, isZero, isUnit, sum, product.
 *
 * KerML standard library: NumericalFunctions.kerml
 */
class NumericalFunctionsLibrary : KernelLibraryFunction {

    override val packageName: String = "NumericalFunctions"

    override fun register(registry: FunctionRegistry) {
        registry.register("abs") { args -> applyAbs(registry, args) }
        registry.register("isUnit") { args -> applyIsUnit(registry, args) }
        registry.register("isZero") { args -> applyIsZero(registry, args) }
        registry.register("product") { args -> applyProduct(registry, args) }
        registry.register("sum") { args -> applySum(registry, args) }
    }

    private fun applyAbs(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val n = registry.extractNumber(args.getOrNull(0))
        if (n != null) {
            return if (n is Long) {
                registry.wrapNumericResult(n.absoluteValue)
            } else {
                registry.wrapNumericResult(n.toDouble().absoluteValue)
            }
        }
        return FunctionResult.Value(null)
    }

    private fun applyIsZero(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val n = registry.extractNumber(args.getOrNull(0))
        if (n != null) {
            return FunctionResult.LiteralElement(registry.createLiteralBoolean(n.toDouble() == 0.0))
        }
        return FunctionResult.Value(null)
    }

    private fun applyIsUnit(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val n = registry.extractNumber(args.getOrNull(0))
        if (n != null) {
            return FunctionResult.LiteralElement(registry.createLiteralBoolean(n.toDouble() == 1.0))
        }
        return FunctionResult.Value(null)
    }

    private fun applySum(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val collection = args.getOrNull(0)
        val items = when (collection) {
            is List<*> -> collection
            else -> args
        }
        var hasDouble = false
        var longSum = 0L
        var doubleSum = 0.0
        for (item in items) {
            val n = registry.extractNumber(item) ?: continue
            if (n is Double || n is Float) hasDouble = true
            longSum += n.toLong()
            doubleSum += n.toDouble()
        }
        return if (hasDouble) registry.wrapNumericResult(doubleSum) else registry.wrapNumericResult(longSum)
    }

    private fun applyProduct(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val collection = args.getOrNull(0)
        val items = when (collection) {
            is List<*> -> collection
            else -> args
        }
        var hasDouble = false
        var longProd = 1L
        var doubleProd = 1.0
        for (item in items) {
            val n = registry.extractNumber(item) ?: continue
            if (n is Double || n is Float) hasDouble = true
            longProd *= n.toLong()
            doubleProd *= n.toDouble()
        }
        return if (hasDouble) registry.wrapNumericResult(doubleProd) else registry.wrapNumericResult(longProd)
    }
}
