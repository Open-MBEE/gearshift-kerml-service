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
package org.openmbee.gearshift.kerml.generator

import org.openmbee.gearshift.generated.interfaces.Element
import org.openmbee.gearshift.generated.interfaces.Namespace

/**
 * Options controlling KerML text generation.
 */
data class GenerationOptions(
    /**
     * Use symbolic syntax (`:>`, `:>>`, `:`) instead of keyword syntax
     * (`specializes`, `redefines`, `typed by`).
     */
    val preferSymbolicSyntax: Boolean = true,

    /**
     * Skip relationships that were created by semantic inference
     * (e.g., implicit subsetting to Base::Anything).
     */
    val emitImpliedRelationships: Boolean = false,

    /**
     * Maximum line length before wrapping (0 = no limit).
     */
    val maxLineLength: Int = 120,

    /**
     * Add blank lines between top-level members for readability.
     */
    val blankLinesBetweenMembers: Boolean = true,

    /**
     * Indentation string (default 4 spaces).
     */
    val indentString: String = "    "
)

/**
 * Context passed through the generator hierarchy during KerML text generation.
 * Mirrors KermlParseContext but for the inverse operation.
 */
data class GenerationContext(
    /**
     * Current indentation level.
     */
    val indentLevel: Int = 0,

    /**
     * Current namespace scope (for qualified name resolution).
     */
    val currentNamespace: Namespace? = null,

    /**
     * Set of imported namespace qualified names (for short name resolution).
     */
    val importedNamespaces: Set<String> = emptySet(),

    /**
     * Generation options.
     */
    val options: GenerationOptions = GenerationOptions()
) {
    /**
     * Create a new context with incremented indentation.
     */
    fun indent(): GenerationContext = copy(indentLevel = indentLevel + 1)

    /**
     * Create a new context with the given namespace as current scope.
     */
    fun withNamespace(ns: Namespace): GenerationContext = copy(currentNamespace = ns)

    /**
     * Create a new context with additional imports.
     */
    fun withImports(imports: Set<String>): GenerationContext =
        copy(importedNamespaces = importedNamespaces + imports)

    /**
     * Get the current indentation string.
     */
    fun currentIndent(): String = options.indentString.repeat(indentLevel)

    /**
     * Resolve the shortest valid display name for an element given the current
     * namespace context and imports.
     *
     * @param element The element to get a name for
     * @return The shortest unambiguous name that can be used in the current context
     */
    fun resolveDisplayName(element: Element): String {
        val qualifiedName = element.qualifiedName
        val declaredName = element.declaredName

        // If no qualified name, fall back to declared name
        if (qualifiedName.isNullOrEmpty()) {
            return declaredName ?: ""
        }

        // If element is in an imported namespace, we can use short name
        val elementNamespace = qualifiedName.substringBeforeLast("::", "")
        if (elementNamespace in importedNamespaces) {
            return declaredName ?: qualifiedName.substringAfterLast("::")
        }

        // If element is in current namespace or a child, use relative name
        currentNamespace?.qualifiedName?.let { currentQN ->
            if (qualifiedName.startsWith("$currentQN::")) {
                return qualifiedName.removePrefix("$currentQN::")
            }
        }

        // Otherwise use full qualified name
        return qualifiedName
    }

    /**
     * Get the appropriate specialization symbol based on options.
     */
    fun specializesSymbol(): String =
        if (options.preferSymbolicSyntax) ":>" else "specializes"

    /**
     * Get the appropriate typing symbol based on options.
     */
    fun typedBySymbol(): String =
        if (options.preferSymbolicSyntax) ":" else "typed by"

    /**
     * Get the appropriate subsetting symbol based on options.
     */
    fun subsetsSymbol(): String =
        if (options.preferSymbolicSyntax) ":>" else "subsets"

    /**
     * Get the appropriate redefinition symbol based on options.
     */
    fun redefinesSymbol(): String =
        if (options.preferSymbolicSyntax) ":>>" else "redefines"

    /**
     * Get the appropriate reference symbol based on options.
     */
    fun referencesSymbol(): String =
        if (options.preferSymbolicSyntax) "::>" else "references"

    /**
     * Get the appropriate conjugation symbol based on options.
     */
    fun conjugatesSymbol(): String =
        if (options.preferSymbolicSyntax) "~" else "conjugates"
}
