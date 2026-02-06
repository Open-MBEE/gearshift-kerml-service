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

import org.openmbee.gearshift.generated.interfaces.*
import org.openmbee.gearshift.kerml.generator.GenerationContext
import org.openmbee.gearshift.kerml.generator.GeneratorFactory

/**
 * Abstract base generator for Type elements and their subclasses.
 *
 * Provides shared generation methods for:
 * - typePrefix (abstract)
 * - typeBody (members, features)
 * - typeRelationshipPart (disjoining, unioning, intersecting, differencing)
 *
 * Mirrors BaseTypeVisitor from the parser.
 */
abstract class BaseTypeGenerator<T : Type> : BaseElementGenerator<T>() {

    /**
     * Generate the type prefix (abstract, all keywords).
     *
     * Grammar: `typePrefix = ( ownedRelationship += PrefixMetadataMember )* ( 'abstract' )?`
     */
    protected open fun generateTypePrefix(type: Type): String = buildString {
        if (type.isAbstract) {
            append("abstract ")
        }
    }

    /**
     * Generate the type body containing members.
     *
     * Grammar: `typeBody = ';' | '{' typeBodyElement* '}'`
     *
     * @param type The type to generate body for
     * @param context The generation context
     * @return The type body string
     */
    protected open fun generateTypeBody(type: Type, context: GenerationContext): String {
        val members = getExplicitMembers(type, context)

        if (members.isEmpty()) {
            return ";"
        }

        return buildString {
            appendLine(" {")
            val bodyContext = context.indent().withNamespace(type)

            // Track generated elements to avoid duplicates (use object identity)
            val generatedElements = mutableSetOf<Element>()

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
                        // Remove the indent from element text since we're adding visibility
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
     * Generate the type relationship part (disjoint, unions, intersects, differences).
     *
     * Grammar: `typeRelationshipPart = disjoiningPart | unioningPart | intersectingPart | differencingPart`
     */
    protected open fun generateTypeRelationshipPart(type: Type, context: GenerationContext): String = buildString {
        // Disjoining: disjoint from Type1, Type2
        val disjoinings = type.ownedDisjoining.filter { shouldEmitRelationship(it, context) }
        if (disjoinings.isNotEmpty()) {
            append(" disjoint from ")
            append(disjoinings.mapNotNull { it.disjoiningType?.let { t -> context.resolveDisplayName(t) } }
                .joinToString(", "))
        }

        // Unioning: unions Type1, Type2
        val unionings = type.ownedUnioning.filter { shouldEmitRelationship(it, context) }
        if (unionings.isNotEmpty()) {
            append(" unions ")
            append(unionings.mapNotNull { it.unioningType?.let { t -> context.resolveDisplayName(t) } }
                .joinToString(", "))
        }

        // Intersecting: intersects Type1, Type2
        val intersectings = type.ownedIntersecting.filter { shouldEmitRelationship(it, context) }
        if (intersectings.isNotEmpty()) {
            append(" intersects ")
            append(intersectings.mapNotNull { it.intersectingType?.let { t -> context.resolveDisplayName(t) } }
                .joinToString(", "))
        }

        // Differencing: differences Type1, Type2
        val differencings = type.ownedDifferencing.filter { shouldEmitRelationship(it, context) }
        if (differencings.isNotEmpty()) {
            append(" differences ")
            append(differencings.mapNotNull { it.differencingType?.let { t -> context.resolveDisplayName(t) } }
                .joinToString(", "))
        }
    }

    /**
     * Get the explicit (non-implied) members of a type for generation.
     *
     * Filters out:
     * - Relationships that are implied (when emitImpliedRelationships is false)
     * - Relationship types that are not body elements (Subclassification, FeatureTyping, etc.)
     * - Membership types themselves (we only want their contained elements)
     * - Members without a valid element
     *
     * Orders members as: annotations, imports, non-features, features
     */
    protected fun getExplicitMembers(type: Type, context: GenerationContext): List<Membership> {
        return type.ownedMembership
            .filter { membership ->
                val element = when (membership) {
                    is OwningMembership -> membership.ownedMemberElement
                    else -> membership.memberElement
                }
                // Skip relationship types that are not body elements
                // These are handled inline in declarations (e.g., :> SuperClass)
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
            .sortedWith(membershipComparator())
    }

    /**
     * Check if a relationship type should be generated inline in declarations
     * rather than as a body element.
     *
     * These relationships are handled by the declaration generators:
     * - Subclassification -> :> syntax
     * - FeatureTyping -> : syntax
     * - Subsetting -> :> syntax
     * - Redefinition -> :>> syntax
     * - Conjugation -> ~ syntax
     * - Disjoining, Unioning, Intersecting, Differencing -> keyword syntax
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

    /**
     * Create a comparator for ordering memberships in output.
     *
     * Order: Comments/Documentation, TextualRepresentation, MetadataFeature,
     *        Imports, Non-features, Features
     */
    private fun membershipComparator(): Comparator<Membership> = compareBy { membership ->
        val element = when (membership) {
            is OwningMembership -> membership.ownedMemberElement
            else -> membership.memberElement
        }
        when (element) {
            is Comment -> 0
            is Documentation -> 0
            is TextualRepresentation -> 1
            is MetadataFeature -> 2
            is Import -> 3
            is Feature -> 5
            else -> 4  // Non-feature namespace elements
        }
    }
}
