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

import org.openmbee.gearshift.generated.interfaces.OwningMembership
import org.openmbee.gearshift.generated.interfaces.Package
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
        val members = pkg.ownedMembership

        if (members.isEmpty()) {
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

            // Generate members
            members.forEach { membership ->
                val element = when (membership) {
                    is OwningMembership -> membership.ownedMemberElement
                    else -> membership.memberElement
                }

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
}
