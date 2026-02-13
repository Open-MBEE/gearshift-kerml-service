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
 * ControlFunctions library — conditional, null coalesce, boolean logic, and
 * higher-order sequence operations (forAll, exists, select, reject, reduce, etc.).
 *
 * KerML standard library: ControlFunctions.kerml
 */
class ControlFunctionsLibrary : KernelLibraryFunction {

    override val packageName: String = "ControlFunctions"

    override fun register(registry: FunctionRegistry) {
        // Conditional
        registry.register("if") { args -> applyIf(registry, args) }
        registry.register("??") { args -> applyNullCoalesce(registry, args) }

        // Boolean logic
        registry.register("and") { args -> applyAnd(registry, args) }
        registry.register("or") { args -> applyOr(registry, args) }
        registry.register("implies") { args -> applyImplies(registry, args) }

        // Higher-order sequence operations
        registry.register("collect") { args -> applyCollect(registry, args) }
        registry.register("select") { args -> applySelect(registry, args) }
        registry.register("reject") { args -> applyReject(registry, args) }
        registry.register("forAll") { args -> applyForAll(registry, args) }
        registry.register("exists") { args -> applyExists(registry, args) }
        registry.register("reduce") { args -> applyReduce(registry, args) }
        registry.register("selectOne") { args -> applySelectOne(registry, args) }

        // Aggregate boolean checks
        registry.register("allTrue") { args -> applyAllTrue(registry, args) }
        registry.register("anyTrue") { args -> applyAnyTrue(registry, args) }

        // Optimization
        registry.register("minimize") { args -> applyMinimize(registry, args) }
        registry.register("maximize") { args -> applyMaximize(registry, args) }
    }

    private fun applyIf(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val condition = registry.extractBoolean(args.getOrNull(0))
        return if (condition == true) {
            FunctionResult.Value(args.getOrNull(1))
        } else {
            FunctionResult.Value(args.getOrNull(2))
        }
    }

    private fun applyNullCoalesce(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val value = args.getOrNull(0)
        return if (value != null && !registry.isNullExpression(value)) {
            FunctionResult.Value(value)
        } else {
            FunctionResult.Value(args.getOrNull(1))
        }
    }

    private fun applyAnd(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = registry.extractBoolean(args.getOrNull(0))
        if (a == false) return FunctionResult.LiteralElement(registry.createLiteralBoolean(false))
        val b = registry.extractBoolean(args.getOrNull(1))
        if (a != null && b != null) {
            return FunctionResult.LiteralElement(registry.createLiteralBoolean(a && b))
        }
        return FunctionResult.Value(null)
    }

    private fun applyOr(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = registry.extractBoolean(args.getOrNull(0))
        if (a == true) return FunctionResult.LiteralElement(registry.createLiteralBoolean(true))
        val b = registry.extractBoolean(args.getOrNull(1))
        if (a != null && b != null) {
            return FunctionResult.LiteralElement(registry.createLiteralBoolean(a || b))
        }
        return FunctionResult.Value(null)
    }

    private fun applyImplies(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = registry.extractBoolean(args.getOrNull(0))
        if (a == false) return FunctionResult.LiteralElement(registry.createLiteralBoolean(true))
        val b = registry.extractBoolean(args.getOrNull(1))
        if (a != null && b != null) {
            return FunctionResult.LiteralElement(registry.createLiteralBoolean(!a || b))
        }
        return FunctionResult.Value(null)
    }

    // === Higher-order sequence operations ===

    private fun toList(value: Any?): List<Any?> = when (value) {
        is List<*> -> value
        null -> emptyList()
        else -> listOf(value)
    }

    private fun applyCollect(@Suppress("UNUSED_PARAMETER") registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        // collect(sequence, body) — body is a function applied to each element
        // When called as a library function (not expression syntax), the body
        // has already been evaluated per-element by the evaluator, so we just flatten.
        val collection = toList(args.getOrNull(0))
        return FunctionResult.Value(collection)
    }

    private fun applySelect(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        // select(sequence, predicate) — filter by predicate result
        val collection = toList(args.getOrNull(0))
        val predicate = args.getOrNull(1)
        if (predicate is List<*>) {
            // If predicate results match collection size, zip and filter
            if (predicate.size == collection.size) {
                val filtered = collection.zip(predicate).filter { (_, p) ->
                    registry.extractBoolean(p) == true
                }.map { (e, _) -> e }
                return FunctionResult.Value(filtered)
            }
        }
        return FunctionResult.Value(collection)
    }

    private fun applyReject(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        // reject(sequence, predicate) — inverse of select
        val collection = toList(args.getOrNull(0))
        val predicate = args.getOrNull(1)
        if (predicate is List<*>) {
            if (predicate.size == collection.size) {
                val filtered = collection.zip(predicate).filter { (_, p) ->
                    registry.extractBoolean(p) != true
                }.map { (e, _) -> e }
                return FunctionResult.Value(filtered)
            }
        }
        return FunctionResult.Value(collection)
    }

    private fun applyForAll(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        // forAll(sequence, predicate) — true if predicate holds for all elements
        val predicateResults = toList(args.getOrNull(1).let { if (it == null) args.getOrNull(0) else it })
        val allTrue = predicateResults.all { registry.extractBoolean(it) == true }
        return FunctionResult.LiteralElement(registry.createLiteralBoolean(allTrue))
    }

    private fun applyExists(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        // exists(sequence, predicate) — true if predicate holds for at least one element
        val predicateResults = toList(args.getOrNull(1).let { if (it == null) args.getOrNull(0) else it })
        val anyTrue = predicateResults.any { registry.extractBoolean(it) == true }
        return FunctionResult.LiteralElement(registry.createLiteralBoolean(anyTrue))
    }

    private fun applyReduce(@Suppress("UNUSED_PARAMETER") registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        // reduce(sequence, accumulator, body) — reduction over sequence
        // At the library-function level, the evaluator handles iteration.
        // This is a stub for when called via arrow syntax with pre-evaluated results.
        val collection = toList(args.getOrNull(0))
        return FunctionResult.Value(collection.lastOrNull())
    }

    private fun applySelectOne(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        // selectOne(sequence, predicate) — first element matching predicate
        val collection = toList(args.getOrNull(0))
        val predicate = args.getOrNull(1)
        if (predicate is List<*> && predicate.size == collection.size) {
            val first = collection.zip(predicate).firstOrNull { (_, p) ->
                registry.extractBoolean(p) == true
            }?.first
            return FunctionResult.Value(first)
        }
        return FunctionResult.Value(collection.firstOrNull())
    }

    // === Aggregate boolean checks ===

    private fun applyAllTrue(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val collection = toList(args.getOrNull(0))
        val allTrue = collection.all { registry.extractBoolean(it) == true }
        return FunctionResult.LiteralElement(registry.createLiteralBoolean(allTrue))
    }

    private fun applyAnyTrue(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val collection = toList(args.getOrNull(0))
        val anyTrue = collection.any { registry.extractBoolean(it) == true }
        return FunctionResult.LiteralElement(registry.createLiteralBoolean(anyTrue))
    }

    // === Optimization ===

    private fun applyMinimize(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val collection = toList(args.getOrNull(0))
        val min = collection.mapNotNull { registry.extractNumber(it) }.minByOrNull { it.toDouble() }
        return if (min != null) registry.wrapNumericResult(min) else FunctionResult.Value(null)
    }

    private fun applyMaximize(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val collection = toList(args.getOrNull(0))
        val max = collection.mapNotNull { registry.extractNumber(it) }.maxByOrNull { it.toDouble() }
        return if (max != null) registry.wrapNumericResult(max) else FunctionResult.Value(null)
    }
}
