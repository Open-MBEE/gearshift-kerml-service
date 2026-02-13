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
 * SequenceFunctions library — size, isEmpty, notEmpty, includes, excludes,
 * equals, head, tail, last, union, intersection, including, excluding,
 * includesOnly, subsequence, same.
 *
 * KerML standard library: SequenceFunctions.kerml
 */
class SequenceFunctionsLibrary : KernelLibraryFunction {

    override val packageName: String = "SequenceFunctions"

    override fun register(registry: FunctionRegistry) {
        registry.register("equals") { args -> applyEquals(registry, args) }
        registry.register("excludes") { args -> applyExcludes(registry, args) }
        registry.register("excluding") { args -> applyExcluding(registry, args) }
        registry.register("head") { args -> applyHead(args) }
        registry.register("includes") { args -> applyIncludes(registry, args) }
        registry.register("includesOnly") { args -> applyIncludesOnly(registry, args) }
        registry.register("including") { args -> applyIncluding(args) }
        registry.register("intersection") { args -> applyIntersection(registry, args) }
        registry.register("isEmpty") { args -> applyIsEmpty(registry, args) }
        registry.register("last") { args -> applyLast(args) }
        registry.register("notEmpty") { args -> applyNotEmpty(registry, args) }
        registry.register("same") { args -> applySame(registry, args) }
        registry.register("size") { args -> applySize(registry, args) }
        registry.register("subsequence") { args -> applySubsequence(registry, args) }
        registry.register("tail") { args -> applyTail(args) }
        registry.register("union") { args -> applyUnion(args) }
    }

    private fun toList(value: Any?): List<Any?> = when (value) {
        is List<*> -> value
        null -> emptyList()
        else -> listOf(value)
    }

    private fun applySize(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val collection = args.getOrNull(0)
        val size = when (collection) {
            is List<*> -> collection.size.toLong()
            null -> 0L
            else -> 1L
        }
        return FunctionResult.LiteralElement(registry.createLiteralInteger(size))
    }

    private fun applyIsEmpty(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val collection = args.getOrNull(0)
        val empty = when (collection) {
            is List<*> -> collection.isEmpty()
            null -> true
            else -> false
        }
        return FunctionResult.LiteralElement(registry.createLiteralBoolean(empty))
    }

    private fun applyNotEmpty(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val collection = args.getOrNull(0)
        val notEmpty = when (collection) {
            is List<*> -> collection.isNotEmpty()
            null -> false
            else -> true
        }
        return FunctionResult.LiteralElement(registry.createLiteralBoolean(notEmpty))
    }

    private fun applyIncludes(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val collection = args.getOrNull(0)
        val element = args.getOrNull(1)
        val includes = when (collection) {
            is List<*> -> collection.any { registry.valuesEqual(it, element) }
            else -> registry.valuesEqual(collection, element)
        }
        return FunctionResult.LiteralElement(registry.createLiteralBoolean(includes))
    }

    private fun applyExcludes(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val collection = args.getOrNull(0)
        val element = args.getOrNull(1)
        val excludes = when (collection) {
            is List<*> -> collection.none { registry.valuesEqual(it, element) }
            else -> !registry.valuesEqual(collection, element)
        }
        return FunctionResult.LiteralElement(registry.createLiteralBoolean(excludes))
    }

    private fun applyEquals(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = toList(args.getOrNull(0))
        val b = toList(args.getOrNull(1))
        if (a.size != b.size) {
            return FunctionResult.LiteralElement(registry.createLiteralBoolean(false))
        }
        val equal = a.zip(b).all { (x, y) -> registry.valuesEqual(x, y) }
        return FunctionResult.LiteralElement(registry.createLiteralBoolean(equal))
    }

    private fun applySame(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        // same checks identity (===), not value equality
        val a = toList(args.getOrNull(0))
        val b = toList(args.getOrNull(1))
        if (a.size != b.size) {
            return FunctionResult.LiteralElement(registry.createLiteralBoolean(false))
        }
        val same = a.zip(b).all { (x, y) -> x === y }
        return FunctionResult.LiteralElement(registry.createLiteralBoolean(same))
    }

    private fun applyIncludesOnly(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        // includesOnly(seq, elements) — seq only contains elements from the given set
        val collection = toList(args.getOrNull(0))
        val allowed = toList(args.getOrNull(1))
        val includesOnly = collection.all { item ->
            allowed.any { registry.valuesEqual(it, item) }
        }
        return FunctionResult.LiteralElement(registry.createLiteralBoolean(includesOnly))
    }

    private fun applyUnion(args: List<Any?>): FunctionResult {
        val a = toList(args.getOrNull(0))
        val b = toList(args.getOrNull(1))
        return FunctionResult.Value(a + b)
    }

    private fun applyIntersection(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val a = toList(args.getOrNull(0))
        val b = toList(args.getOrNull(1))
        val intersection = a.filter { item -> b.any { registry.valuesEqual(it, item) } }
        return FunctionResult.Value(intersection)
    }

    private fun applyIncluding(args: List<Any?>): FunctionResult {
        val collection = toList(args.getOrNull(0))
        val element = args.getOrNull(1)
        return FunctionResult.Value(collection + element)
    }

    private fun applyExcluding(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val collection = toList(args.getOrNull(0))
        val element = args.getOrNull(1)
        val excluded = collection.filterNot { registry.valuesEqual(it, element) }
        return FunctionResult.Value(excluded)
    }

    private fun applyHead(args: List<Any?>): FunctionResult {
        val collection = toList(args.getOrNull(0))
        return FunctionResult.Value(collection.firstOrNull())
    }

    private fun applyTail(args: List<Any?>): FunctionResult {
        val collection = toList(args.getOrNull(0))
        return FunctionResult.Value(if (collection.size > 1) collection.drop(1) else emptyList<Any?>())
    }

    private fun applyLast(args: List<Any?>): FunctionResult {
        val collection = toList(args.getOrNull(0))
        return FunctionResult.Value(collection.lastOrNull())
    }

    private fun applySubsequence(registry: FunctionRegistry, args: List<Any?>): FunctionResult {
        val collection = toList(args.getOrNull(0))
        val start = registry.extractNumber(args.getOrNull(1))?.toInt()
        val end = registry.extractNumber(args.getOrNull(2))?.toInt()
        if (start != null && end != null) {
            // KerML indexing is 1-based
            val zeroStart = (start - 1).coerceIn(0, collection.size)
            val zeroEnd = end.coerceIn(zeroStart, collection.size)
            return FunctionResult.Value(collection.subList(zeroStart, zeroEnd))
        }
        return FunctionResult.Value(collection)
    }
}
