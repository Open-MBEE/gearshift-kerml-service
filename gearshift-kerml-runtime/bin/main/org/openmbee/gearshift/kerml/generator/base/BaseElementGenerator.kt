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
package org.openmbee.gearshift.kerml.generator.base

import org.openmbee.gearshift.generated.interfaces.Element
import org.openmbee.gearshift.generated.interfaces.Membership
import org.openmbee.gearshift.generated.interfaces.Relationship
import org.openmbee.gearshift.kerml.generator.GenerationContext

/**
 * Abstract base generator for all Element types.
 *
 * Provides common functionality for generating element identification
 * and visibility prefixes.
 */
abstract class BaseElementGenerator<T : Element> : KerMLGenerator<T> {

    /**
     * Generate the identification part of an element declaration.
     *
     * Grammar: `( '<' declaredShortName '>' )? declaredName?`
     *
     * @param element The element to generate identification for
     * @return The identification string (may be empty if no names declared)
     */
    protected fun generateIdentification(element: Element): String = buildString {
        element.declaredShortName?.let { shortName ->
            append("<")
            append(escapeNameIfNeeded(shortName))
            append(">")
            if (element.declaredName != null) {
                append(" ")
            }
        }
        element.declaredName?.let { name ->
            append(escapeNameIfNeeded(name))
        }
    }

    /**
     * Generate the visibility prefix for a membership.
     *
     * @param membership The membership to get visibility from (null = public)
     * @return The visibility keyword with trailing space, or empty if public
     */
    protected fun generateVisibility(membership: Membership?): String {
        val visibility = membership?.visibility ?: "public"
        return when (visibility) {
            "public" -> ""  // public is default, don't emit
            "private" -> "private "
            "protected" -> "protected "
            else -> ""
        }
    }

    /**
     * Check if a relationship should be emitted based on generation options.
     *
     * @param relationship The relationship to check
     * @param context The generation context with options
     * @return true if the relationship should be included in output
     */
    protected fun shouldEmitRelationship(relationship: Relationship, context: GenerationContext): Boolean {
        // If emitImpliedRelationships is false, skip implied relationships
        if (!context.options.emitImpliedRelationships && relationship.isImplied) {
            return false
        }
        return true
    }

    /**
     * Escape a name if it contains special characters or is a keyword.
     *
     * Per KerML spec, names that are not valid basic names must be
     * written as unrestricted names (enclosed in single quotes).
     *
     * @param name The name to potentially escape
     * @return The name, escaped if necessary
     */
    protected fun escapeNameIfNeeded(name: String): String {
        // Basic name pattern: starts with letter/underscore, contains only letters/digits/underscores
        val basicNamePattern = Regex("^[a-zA-Z_][a-zA-Z0-9_]*$")

        // Check if it's a keyword that needs escaping
        if (name in KERML_KEYWORDS) {
            return "'$name'"
        }

        // Check if it matches the basic name pattern
        if (basicNamePattern.matches(name)) {
            return name
        }

        // Otherwise, escape it as an unrestricted name
        return "'" + name.replace("\\", "\\\\").replace("'", "\\'") + "'"
    }

    companion object {
        /**
         * KerML keywords that must be escaped when used as names.
         */
        private val KERML_KEYWORDS = setOf(
            "about", "abstract", "alias", "all", "and", "as", "assign",
            "assoc", "behavior", "binding", "bool", "by", "class", "classifier",
            "comment", "composite", "conjugate", "conjugates", "conjugation",
            "connector", "const", "crosses", "datatype", "default", "dependency",
            "derived", "disjoint", "disjoining", "doc", "else", "end", "expr",
            "false", "feature", "featured", "featuring", "filter", "first", "flow",
            "for", "from", "function", "hastype", "if", "implies", "import", "in",
            "inout", "interaction", "intersects", "inv", "inverse", "inverting",
            "istype", "language", "library", "locale", "loop", "member", "membership",
            "metaclass", "metadata", "multiplicity", "namespace", "nonunique", "not",
            "null", "of", "or", "ordered", "out", "package", "portion", "predicate",
            "private", "protected", "public", "readonly", "redefines", "redefinition",
            "ref", "references", "rep", "return", "specialization", "specializes",
            "step", "struct", "subclassifier", "subsets", "subtype", "succession",
            "then", "to", "true", "type", "typed", "typing", "unions", "var", "xor"
        )
    }
}
