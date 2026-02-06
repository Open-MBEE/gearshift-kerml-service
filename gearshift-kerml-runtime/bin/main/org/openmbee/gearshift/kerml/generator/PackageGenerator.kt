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

import org.openmbee.gearshift.generated.interfaces.*
import org.openmbee.gearshift.kerml.generator.base.BaseElementGenerator

/**
 * Generator for Package elements.
 *
 * Per KerML spec 8.2.3.4.2: A Package is a Namespace used to group Elements.
 *
 * Grammar:
 * ```
 * package
 *     : ( ownedRelationship += PrefixMetadataMember )*
 *       'package' Identification namespaceBody
 *     ;
 * ```
 */
class PackageGenerator : BaseElementGenerator<Package>() {

    override fun generate(element: Package, context: GenerationContext): String = buildString {
        val indent = context.currentIndent()

        // Indentation
        append(indent)

        // PACKAGE keyword
        append("package ")

        // Identification
        append(generateIdentification(element))

        // namespaceBody
        append(generateNamespaceBody(element, context))
    }

    /**
     * Generate the namespace body.
     *
     * Grammar: `namespaceBody = ';' | '{' namespaceBodyElement* '}'`
     */
    private fun generateNamespaceBody(pkg: Package, context: GenerationContext): String {
        val members = getExplicitMembers(pkg, context)

        if (members.isEmpty() && pkg.ownedImport.isEmpty()) {
            return ";"
        }

        return buildString {
            appendLine(" {")
            val bodyContext = context.indent().withNamespace(pkg)

            // Generate imports first
            pkg.ownedImport.forEach { import ->
                val importText = GeneratorFactory.generate(import, bodyContext)
                if (importText.isNotEmpty()) {
                    appendLine(importText)
                }
            }

            if (pkg.ownedImport.isNotEmpty() && members.isNotEmpty()) {
                appendLine()
            }

            // Track generated elements to avoid duplicates (use object identity)
            val generatedElements = mutableSetOf<Element>()

            // Generate members
            members.forEach { membership ->
                val element = when (membership) {
                    is OwningMembership -> membership.ownedMemberElement
                    else -> membership.memberElement
                }

                // Skip if already generated (avoid duplicates)
                if (element in generatedElements) {
                    return@forEach
                }
                generatedElements.add(element)

                val visibility = generateVisibility(membership)
                val elementText = GeneratorFactory.generate(element, bodyContext)

                if (elementText.isNotEmpty()) {
                    if (visibility.isNotEmpty()) {
                        append(bodyContext.currentIndent())
                        append(visibility)
                        appendLine(elementText.trimStart())
                    } else {
                        appendLine(elementText)
                    }

                    if (context.options.blankLinesBetweenMembers) {
                        appendLine()
                    }
                }
            }

            append(context.currentIndent())
            append("}")
        }
    }

    /**
     * Get the explicit members of a namespace for generation.
     *
     * Filters out:
     * - Relationship types that are inline (Subclassification, FeatureTyping, etc.)
     * - Membership types themselves
     * - Implied relationships
     */
    private fun getExplicitMembers(pkg: Package, context: GenerationContext): List<Membership> {
        return pkg.ownedMembership
            .filter { membership ->
                val element = when (membership) {
                    is OwningMembership -> membership.ownedMemberElement
                    else -> membership.memberElement
                }
                // Skip relationship types that are not body elements
                if (isInlineRelationship(element)) {
                    return@filter false
                }
                // Skip membership types - we want their contents, not the membership itself
                if (element is Membership) {
                    return@filter false
                }
                // Skip implied relationships unless configured to emit them
                if (element is Relationship && !context.options.emitImpliedRelationships && element.isImplied) {
                    return@filter false
                }
                true
            }
    }

    /**
     * Check if a relationship type should be generated inline in declarations.
     */
    private fun isInlineRelationship(element: Element): Boolean {
        return element is Specialization ||
               element is FeatureTyping ||
               element is Subsetting ||
               element is Redefinition ||
               element is Conjugation ||
               element is Disjoining ||
               element is Unioning ||
               element is Intersecting ||
               element is Differencing ||
               element is FeatureChaining ||
               element is TypeFeaturing
    }
}
