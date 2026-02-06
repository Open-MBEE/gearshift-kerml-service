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
package org.openmbee.gearshift.kerml

/**
 * Utility functions for handling KerML names.
 *
 * KerML has two types of names:
 * - Basic names: Start with letter or underscore, contain letters, digits, underscores
 * - Unrestricted names: Enclosed in single quotes, can contain any characters
 *
 * Per KerML 8.2.2.3, unrestricted names use escape sequences for special characters.
 */
object KerMLNames {

    /**
     * Regex pattern for valid basic names.
     * Basic names start with a letter or underscore and contain only letters, digits, and underscores.
     */
    private val BASIC_NAME_PATTERN = Regex("^[a-zA-Z_][a-zA-Z0-9_]*$")

    /**
     * Check if a name requires quoting (is not a valid basic name).
     *
     * @param name The name to check (without quotes)
     * @return true if the name needs to be quoted as an unrestricted name
     */
    fun needsQuoting(name: String): Boolean {
        if (name.isEmpty()) return true
        return !BASIC_NAME_PATTERN.matches(name)
    }

    /**
     * Quote a name if necessary, adding single quotes and escaping special characters.
     *
     * @param name The name to quote (without quotes)
     * @return The name ready for use in KerML syntax (quoted if necessary)
     */
    fun quoteName(name: String): String {
        if (!needsQuoting(name)) {
            return name
        }
        // Escape special characters per KerML Table 4
        val escaped = name
            .replace("\\", "\\\\")  // backslash first
            .replace("'", "\\'")    // single quote
            .replace("\"", "\\\"")  // double quote
            .replace("\b", "\\b")   // backspace
            .replace("\u000C", "\\f") // form feed
            .replace("\t", "\\t")   // tab
            .replace("\n", "\\n")   // newline
            .replace("\r", "\\r")   // carriage return
        return "'$escaped'"
    }

    /**
     * Unescape a name, removing quotes and processing escape sequences.
     *
     * @param rawName The raw name (may include surrounding quotes)
     * @return The unescaped name without quotes
     */
    fun unescapeName(rawName: String): String {
        if (rawName.startsWith("'") && rawName.endsWith("'") && rawName.length >= 2) {
            val inner = rawName.substring(1, rawName.length - 1)
            // Process \\ FIRST to avoid false matches like \\b being seen as \b
            return inner
                .replace("\\\\", "\u0000")  // temporarily replace \\ with null char
                .replace("\\'", "'")
                .replace("\\\"", "\"")
                .replace("\\b", "\b")
                .replace("\\f", "\u000C")
                .replace("\\t", "\t")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\u0000", "\\")    // restore \\ as single backslash
        }
        return rawName
    }

    /**
     * Build a qualified name from segments, quoting each segment if necessary.
     *
     * @param segments The name segments (without quotes)
     * @return A qualified name string with proper quoting
     */
    fun buildQualifiedName(vararg segments: String): String {
        return segments.joinToString("::") { quoteName(it) }
    }

    /**
     * Parse a qualified name into unescaped segments.
     *
     * @param qualifiedName The qualified name string (may include quoted segments)
     * @return List of unescaped name segments
     */
    fun parseQualifiedName(qualifiedName: String): List<String> {
        return qualifiedName.split("::").map { unescapeName(it) }
    }
}
