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
package org.openmbee.mdm.framework.query.gql.executor

/**
 * Represents the result of a GQL query as a table of bindings.
 *
 * Each row is a map of variable names to their bound values.
 * Columns are the variable names that appear in the result.
 */
data class BindingTable(
    val columns: List<String>,
    val rows: List<Map<String, Any?>>
) {
    /**
     * Check if the result is empty.
     */
    fun isEmpty(): Boolean = rows.isEmpty()

    /**
     * Check if the result is not empty.
     */
    fun isNotEmpty(): Boolean = rows.isNotEmpty()

    /**
     * Get the number of result rows.
     */
    val size: Int get() = rows.size

    /**
     * Get a row by index.
     */
    operator fun get(index: Int): Map<String, Any?> = rows[index]

    /**
     * Get all values for a specific column.
     */
    fun column(name: String): List<Any?> = rows.map { it[name] }

    /**
     * Get a single value by row index and column name.
     */
    fun getValue(rowIndex: Int, columnName: String): Any? = rows[rowIndex][columnName]

    /**
     * Iterate over rows.
     */
    operator fun iterator(): Iterator<Map<String, Any?>> = rows.iterator()

    /**
     * Convert to a list of maps (the underlying row representation).
     */
    fun toList(): List<Map<String, Any?>> = rows

    /**
     * Get the first row, or null if empty.
     */
    fun firstOrNull(): Map<String, Any?>? = rows.firstOrNull()

    /**
     * Get the first row, or throw if empty.
     */
    fun first(): Map<String, Any?> = rows.first()

    /**
     * Get the first value of the first column, or null.
     * Useful for single-value queries like COUNT(*).
     */
    fun scalar(): Any? {
        if (rows.isEmpty() || columns.isEmpty()) return null
        return rows.first()[columns.first()]
    }

    override fun toString(): String {
        if (isEmpty()) return "BindingTable(empty)"

        val sb = StringBuilder()
        sb.append("BindingTable(\n")
        sb.append("  columns: $columns\n")
        sb.append("  rows:\n")
        for ((i, row) in rows.withIndex()) {
            sb.append("    [$i] $row\n")
            if (i >= 9 && rows.size > 10) {
                sb.append("    ... (${rows.size - 10} more rows)\n")
                break
            }
        }
        sb.append(")")
        return sb.toString()
    }

    companion object {
        /**
         * Create an empty binding table.
         */
        val EMPTY = BindingTable(emptyList(), emptyList())

        /**
         * Create a binding table from a list of maps.
         * Columns are inferred from the keys in the first row.
         */
        fun fromRows(rows: List<Map<String, Any?>>): BindingTable {
            if (rows.isEmpty()) return EMPTY
            val columns = rows.first().keys.toList()
            return BindingTable(columns, rows)
        }

        /**
         * Create a single-row binding table.
         */
        fun singleRow(vararg pairs: Pair<String, Any?>): BindingTable {
            val row = pairs.toMap()
            return BindingTable(row.keys.toList(), listOf(row))
        }

        /**
         * Create a single-column binding table.
         */
        fun singleColumn(columnName: String, values: List<Any?>): BindingTable {
            val rows = values.map { mapOf(columnName to it) }
            return BindingTable(listOf(columnName), rows)
        }
    }
}
