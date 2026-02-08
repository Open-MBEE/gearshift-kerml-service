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
import org.openmbee.gearshift.kerml.generator.base.BaseFeatureGenerator

class ConnectorGenerator : BaseFeatureGenerator<Connector>() {

    override fun generate(element: Connector, context: GenerationContext): String = buildString {
        append(context.currentIndent())
        append(generateFeaturePrefix(element))
        append("connector ")
        append(generateFeatureDeclaration(element, context))

        // Find connector ends from explicit members (end features with isEnd=true)
        val allMembers = getExplicitMembers(element, context)
        val endEntries = allMembers.mapNotNull { membership ->
            val el = when (membership) {
                is OwningMembership -> membership.ownedMemberElement
                else -> membership.memberElement
            }
            (el as? Feature)?.takeIf { it.isEnd }?.let { membership to it }
        }
        val ends = endEntries.map { it.second }
        val endMemberships = endEntries.map { it.first }.toSet()

        if (ends.size == 2) {
            // Binary connector: from end1 to end2
            val end1 = resolveEndName(ends[0], context)
            val end2 = resolveEndName(ends[1], context)
            append(" from $end1 to $end2")
        } else if (ends.size > 2) {
            // N-ary connector: (end1, end2, end3)
            val endNames = ends.map { resolveEndName(it, context) }
            append(" (${endNames.joinToString(", ")})")
        }

        // Generate body excluding end features that were already emitted inline
        if (endMemberships.isNotEmpty()) {
            append(generateTypeBodyExcluding(element, context, endMemberships))
        } else {
            append(generateTypeBody(element, context))
        }
    }

    /**
     * Generate the type body excluding specific memberships (e.g., end features
     * that were already emitted as from/to syntax).
     */
    private fun generateTypeBodyExcluding(
        type: Type,
        context: GenerationContext,
        excludedMemberships: Set<Membership>
    ): String {
        val members = getExplicitMembers(type, context)
            .filter { it !in excludedMemberships }

        if (members.isEmpty()) {
            return ";"
        }

        return buildString {
            appendLine(" {")
            val bodyContext = context.indent().withNamespace(type)
            val generatedElements = mutableSetOf<Element>()

            members.forEach { membership ->
                val element = when (membership) {
                    is OwningMembership -> membership.ownedMemberElement
                    else -> membership.memberElement
                }
                if (element in generatedElements) return@forEach
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

    companion object {
        fun resolveEndName(end: Feature, context: GenerationContext): String {
            // Try feature chains (dot paths like a1.p)
            try {
                val chainings = end.ownedFeatureChaining
                if (chainings.isNotEmpty()) {
                    return chainings.joinToString(".") { chaining ->
                        try {
                            context.resolveDisplayName(chaining.chainingFeature)
                        } catch (_: NullPointerException) { "?" }
                    }
                }
            } catch (_: Exception) { /* derived property evaluation may fail */ }

            // Try subsetted feature reference
            try {
                val subsettedFeature = end.ownedSubsetting.firstOrNull()?.subsettedFeature
                if (subsettedFeature != null) {
                    return context.resolveDisplayName(subsettedFeature)
                }
            } catch (_: Exception) { /* unresolved */ }

            // Fall back to end feature's declaredName directly (the parser stores
            // the reference name here for anonymous end features; we avoid
            // resolveDisplayName which would namespace-qualify it)
            return end.declaredName ?: ""
        }
    }
}
