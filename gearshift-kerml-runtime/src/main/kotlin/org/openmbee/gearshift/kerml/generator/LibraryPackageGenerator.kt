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

class LibraryPackageGenerator : BaseElementGenerator<LibraryPackage>() {

    override fun generate(element: LibraryPackage, context: GenerationContext): String = buildString {
        append(context.currentIndent())

        // Library prefix
        if (element.isStandard) {
            append("standard ")
        }
        append("library ")

        append("package ")
        append(generateIdentification(element))
        append(generateNamespaceBody(element, context))
    }

    private fun generateNamespaceBody(pkg: LibraryPackage, context: GenerationContext): String {
        val members = getExplicitMembers(pkg, context)

        if (members.isEmpty() && pkg.ownedImport.isEmpty()) {
            return ";"
        }

        return buildString {
            appendLine(" {")
            val bodyContext = context.indent().withNamespace(pkg)

            pkg.ownedImport.forEach { import ->
                val importText = GeneratorFactory.generate(import, bodyContext)
                if (importText.isNotEmpty()) {
                    appendLine(importText)
                }
            }

            if (pkg.ownedImport.isNotEmpty() && members.isNotEmpty()) {
                appendLine()
            }

            val generatedElements = mutableSetOf<Element>()

            members.forEach { membership ->
                val element = when (membership) {
                    is OwningMembership -> membership.ownedMemberElement
                    else -> membership.memberElement
                }

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

    private fun getExplicitMembers(pkg: LibraryPackage, context: GenerationContext): List<Membership> {
        return pkg.ownedMembership
            .filter { membership ->
                val element = when (membership) {
                    is OwningMembership -> membership.ownedMemberElement
                    else -> membership.memberElement
                }
                if (isInlineRelationship(element)) return@filter false
                if (element is Membership) return@filter false
                if (element is Relationship && !context.options.emitImpliedRelationships && element.isImplied) {
                    return@filter false
                }
                true
            }
    }

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
