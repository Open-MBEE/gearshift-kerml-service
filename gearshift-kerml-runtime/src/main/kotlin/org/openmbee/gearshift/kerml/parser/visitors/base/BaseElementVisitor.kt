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

package org.openmbee.gearshift.kerml.parser.visitors.base

import org.openmbee.gearshift.generated.interfaces.ModelElement
import org.openmbee.gearshift.kerml.antlr.KerMLParser


/**
 * Abstract base class providing common functionality for typed visitors.
 *
 * @param Ctx The ANTLR parser context type this visitor handles
 * @param Result The typed wrapper type this visitor produces
 */
abstract class BaseTypedVisitor<Ctx, Result : ModelElement> : TypedKerMLVisitor<Ctx, Result> {

    /**
     * Parse the standard identification pattern (declaredShortName, declaredName).
     * Many KerML elements share this pattern.
     *
     * Handles both basic names and unrestricted names (names in single quotes).
     * For unrestricted names, strips the surrounding quotes and processes escape sequences.
     */
    protected fun parseIdentification(
        identification: KerMLParser.IdentificationContext?,
        element: org.openmbee.gearshift.generated.interfaces.Element
    ) {
        identification?.let { id ->
            id.declaredShortName?.let { shortName ->
                element.declaredShortName = unescapeName(shortName.text)
            }
            id.declaredName?.let { name ->
                element.declaredName = unescapeName(name.text)
            }
        }
    }

    /**
     * Process a KerML name, handling unrestricted names (in single quotes).
     *
     * Per KerML 8.2.2.3:
     * - Unrestricted names are enclosed in single quotes
     * - The quotes are NOT part of the name
     * - Escape sequences within the name are processed
     *
     * @param rawName The raw name text from the parser (may include quotes)
     * @return The unescaped name without surrounding quotes
     */
    protected fun unescapeName(rawName: String): String {
        // Check if this is an unrestricted name (starts and ends with single quote)
        if (rawName.startsWith("'") && rawName.endsWith("'") && rawName.length >= 2) {
            // Strip the surrounding quotes
            val inner = rawName.substring(1, rawName.length - 1)
            // Process escape sequences per KerML Table 4
            // Process \\ FIRST to avoid false matches like \\b being seen as \b
            return inner
                .replace("\\\\", "\u0000")  // temporarily replace \\ with null char
                .replace("\\'", "'")    // escaped single quote
                .replace("\\\"", "\"")  // escaped double quote
                .replace("\\b", "\b")   // backspace
                .replace("\\f", "\u000C") // form feed
                .replace("\\t", "\t")   // tab
                .replace("\\n", "\n")   // newline
                .replace("\\r", "\r")   // carriage return
                .replace("\u0000", "\\")    // restore \\ as single backslash
        }
        return rawName
    }

    /**
     * Parse a memberPrefix to extract visibility.
     * Returns "public", "private", or "protected". Defaults to "public" if not specified.
     */
    protected fun parseMemberPrefix(ctx: KerMLParser.MemberPrefixContext?): String {
        ctx?.visibilityIndicator()?.let { vis ->
            vis.PUBLIC()?.let { return "public" }
            vis.PRIVATE()?.let { return "private" }
            vis.PROTECTED()?.let { return "protected" }
        }
        return "public"
    }

    /**
     * Extract a qualified name from a QualifiedNameContext.
     * Handles unrestricted names (in single quotes) in each segment.
     */
    protected fun extractQualifiedName(ctx: KerMLParser.QualifiedNameContext): String {
        val names = ctx.NAME().map { unescapeName(it.text) }
        return if (ctx.DOLLAR() != null) {
            "\$::" + names.joinToString("::")
        } else {
            names.joinToString("::")
        }
    }

    /**
     * Compute the qualified name for an element given its parent qualified name.
     */
    protected fun computeQualifiedName(parentQualifiedName: String, declaredName: String?): String {
        return when {
            declaredName.isNullOrEmpty() -> parentQualifiedName
            parentQualifiedName.isEmpty() -> declaredName
            else -> "$parentQualifiedName::$declaredName"
        }
    }
}
