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
package org.openmbee.gearshift.framework.constraints.ocl

/**
 * OCL Standard Library operations.
 * Provides implementations of OCL collection and object operations.
 */
object OclStandardLibrary {

    // ===== Collection Operations =====

    /**
     * size() : Integer
     * Returns the number of elements in the collection.
     */
    fun size(collection: Collection<*>): Long = collection.size.toLong()

    /**
     * isEmpty() : Boolean
     * Returns true if the collection is empty.
     */
    fun isEmpty(collection: Collection<*>): Boolean = collection.isEmpty()

    /**
     * notEmpty() : Boolean
     * Returns true if the collection is not empty.
     */
    fun notEmpty(collection: Collection<*>): Boolean = collection.isNotEmpty()

    /**
     * includes(object) : Boolean
     * Returns true if the collection contains the object.
     */
    fun includes(collection: Collection<*>, obj: Any?): Boolean = collection.contains(obj)

    /**
     * excludes(object) : Boolean
     * Returns true if the collection does not contain the object.
     */
    fun excludes(collection: Collection<*>, obj: Any?): Boolean = !collection.contains(obj)

    /**
     * includesAll(c2) : Boolean
     * Returns true if the collection contains all elements of c2.
     */
    fun includesAll(collection: Collection<*>, c2: Collection<*>): Boolean = collection.containsAll(c2)

    /**
     * excludesAll(c2) : Boolean
     * Returns true if the collection contains none of the elements of c2.
     */
    fun excludesAll(collection: Collection<*>, c2: Collection<*>): Boolean = collection.none { it in c2 }

    /**
     * count(object) : Integer
     * Returns the number of times object appears in the collection.
     */
    fun count(collection: Collection<*>, obj: Any?): Long = collection.count { it == obj }.toLong()

    // ===== Set Operations =====

    /**
     * union(s) : Set
     * Returns the union of this set and s.
     */
    fun <T> union(set1: Collection<T>, set2: Collection<T>): Set<T> = (set1 + set2).toSet()

    /**
     * intersection(s) : Set
     * Returns the intersection of this set and s.
     */
    fun <T> intersection(set1: Collection<T>, set2: Collection<T>): Set<T> =
        set1.filter { it in set2 }.toSet()

    /**
     * - (s) : Set
     * Returns the set difference (elements in this but not in s).
     */
    fun <T> minus(set1: Collection<T>, set2: Collection<T>): Set<T> =
        set1.filter { it !in set2 }.toSet()

    /**
     * symmetricDifference(s) : Set
     * Returns elements in exactly one of the sets.
     */
    fun <T> symmetricDifference(set1: Collection<T>, set2: Collection<T>): Set<T> {
        val union = (set1 + set2).toSet()
        val intersection = set1.filter { it in set2 }.toSet()
        return union - intersection
    }

    // ===== Sequence Operations =====

    /**
     * first() : T
     * Returns the first element of the sequence.
     */
    fun <T> first(sequence: List<T>): T? = sequence.firstOrNull()

    /**
     * last() : T
     * Returns the last element of the sequence.
     */
    fun <T> last(sequence: List<T>): T? = sequence.lastOrNull()

    /**
     * at(i) : T
     * Returns the element at index i (1-based in OCL).
     */
    fun <T> at(sequence: List<T>, index: Int): T? = sequence.getOrNull(index - 1)

    /**
     * indexOf(object) : Integer
     * Returns the index of the object (1-based in OCL), or 0 if not found.
     */
    fun <T> indexOf(sequence: List<T>, obj: T): Long {
        val idx = sequence.indexOf(obj)
        return if (idx >= 0) (idx + 1).toLong() else 0L
    }

    /**
     * append(object) : Sequence
     * Returns a sequence with object added at the end.
     */
    fun <T> append(sequence: List<T>, obj: T): List<T> = sequence + obj

    /**
     * prepend(object) : Sequence
     * Returns a sequence with object added at the beginning.
     */
    fun <T> prepend(sequence: List<T>, obj: T): List<T> = listOf(obj) + sequence

    /**
     * insertAt(index, object) : Sequence
     * Returns a sequence with object inserted at index (1-based).
     */
    fun <T> insertAt(sequence: List<T>, index: Int, obj: T): List<T> {
        val mutable = sequence.toMutableList()
        mutable.add(index - 1, obj)
        return mutable
    }

    /**
     * subSequence(lower, upper) : Sequence
     * Returns elements from lower to upper (1-based, inclusive).
     */
    fun <T> subSequence(sequence: List<T>, lower: Int, upper: Int): List<T> =
        sequence.subList(lower - 1, upper)

    /**
     * reverse() : Sequence
     * Returns the sequence reversed.
     */
    fun <T> reverse(sequence: List<T>): List<T> = sequence.reversed()

    // ===== Flattening =====

    /**
     * flatten() : Collection
     * Flattens nested collections.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> flatten(collection: Collection<*>): List<T> {
        return collection.flatMap { element ->
            when (element) {
                is Collection<*> -> flatten(element)
                else -> listOf(element as T)
            }
        }
    }

    // ===== Conversion =====

    /**
     * asSet() : Set
     * Converts to a Set.
     */
    fun <T> asSet(collection: Collection<T>): Set<T> = collection.toSet()

    /**
     * asSequence() : Sequence
     * Converts to a Sequence (List).
     */
    fun <T> asSequence(collection: Collection<T>): List<T> = collection.toList()

    /**
     * asBag() : Bag
     * Converts to a Bag (List with duplicates allowed).
     */
    fun <T> asBag(collection: Collection<T>): List<T> = collection.toList()

    /**
     * asOrderedSet() : OrderedSet
     * Converts to an OrderedSet (LinkedHashSet).
     */
    fun <T> asOrderedSet(collection: Collection<T>): Set<T> = collection.toCollection(LinkedHashSet())

    // ===== Numeric Operations =====

    /**
     * sum() : Number
     * Returns the sum of numeric elements.
     */
    fun sum(collection: Collection<*>): Number {
        var intSum = 0L
        var hasDouble = false
        var doubleSum = 0.0

        for (element in collection) {
            when (element) {
                is Long -> intSum += element
                is Int -> intSum += element
                is Double -> {
                    hasDouble = true
                    doubleSum += element
                }
                is Float -> {
                    hasDouble = true
                    doubleSum += element
                }
                is Number -> intSum += element.toLong()
            }
        }

        return if (hasDouble) doubleSum + intSum else intSum
    }

    /**
     * min() : T
     * Returns the minimum element.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Comparable<T>> min(collection: Collection<T>): T? =
        collection.minOrNull()

    /**
     * max() : T
     * Returns the maximum element.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Comparable<T>> max(collection: Collection<T>): T? =
        collection.maxOrNull()

    // ===== Object Operations =====

    /**
     * oclIsUndefined() : Boolean
     * Returns true if the value is null/undefined.
     */
    fun oclIsUndefined(value: Any?): Boolean = value == null

    /**
     * oclIsInvalid() : Boolean
     * Returns true if the value represents an invalid/error state.
     * In our implementation, we don't have a separate invalid value.
     */
    fun oclIsInvalid(value: Any?): Boolean = false

    // ===== String Operations =====

    /**
     * concat(s) : String
     * Concatenates strings.
     */
    fun concat(s1: String, s2: String): String = s1 + s2

    /**
     * substring(lower, upper) : String
     * Returns substring from lower to upper (1-based, inclusive).
     */
    fun substring(s: String, lower: Int, upper: Int): String =
        s.substring(lower - 1, upper)

    /**
     * toInteger() : Integer
     * Converts string to integer.
     */
    fun toInteger(s: String): Long = s.toLong()

    /**
     * toReal() : Real
     * Converts string to real.
     */
    fun toReal(s: String): Double = s.toDouble()

    /**
     * toUpperCase() : String
     */
    fun toUpperCase(s: String): String = s.uppercase()

    /**
     * toLowerCase() : String
     */
    fun toLowerCase(s: String): String = s.lowercase()

    // ===== Boolean Operations =====

    /**
     * implies(b) : Boolean
     * Logical implication: a implies b = not a or b
     */
    fun implies(a: Boolean, b: Boolean): Boolean = !a || b

    /**
     * xor(b) : Boolean
     * Exclusive or.
     */
    fun xor(a: Boolean, b: Boolean): Boolean = a xor b
}
