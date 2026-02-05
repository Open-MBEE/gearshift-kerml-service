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

import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.generated.interfaces.NamespaceImport
import org.openmbee.gearshift.generated.interfaces.OwningMembership
import org.openmbee.gearshift.generated.interfaces.Package
import java.io.File

/**
 * Main entry point for generating KerML text from model elements.
 *
 * KerMLWriter provides methods to write a namespace (typically the root)
 * to a string or file, producing spec-compliant KerML textual syntax.
 *
 * Example usage:
 * ```kotlin
 * val model = KerMLModel().parseString(kermlText)
 * val writer = KerMLWriter()
 * val generatedText = writer.write(model.rootNamespace!!)
 * ```
 */
class KerMLWriter(
    private val options: GenerationOptions = GenerationOptions()
) {

    /**
     * Write a namespace to KerML text.
     *
     * @param namespace The root namespace to generate
     * @return The KerML text representation
     */
    fun write(namespace: Namespace): String {
        val imports = collectImports(namespace)
        val context = GenerationContext(
            options = options,
            importedNamespaces = imports,
            currentNamespace = namespace
        )

        return buildString {
            // For a root namespace (no declared name), just generate contents
            if (namespace.declaredName == null && namespace.declaredShortName == null) {
                appendRootNamespaceContents(namespace, context)
            } else {
                // For a named namespace, generate the full declaration
                append(GeneratorFactory.generate(namespace, context))
            }
        }.trimEnd() + "\n"
    }

    /**
     * Write a namespace to a file.
     *
     * @param namespace The root namespace to generate
     * @param file The file to write to
     */
    fun writeToFile(namespace: Namespace, file: File) {
        file.writeText(write(namespace))
    }

    /**
     * Write a package to KerML text.
     *
     * Convenience method that delegates to write(Namespace).
     *
     * @param pkg The package to generate
     * @return The KerML text representation
     */
    fun write(pkg: Package): String = write(pkg as Namespace)

    /**
     * Generate the contents of a root namespace (without wrapping syntax).
     */
    private fun StringBuilder.appendRootNamespaceContents(namespace: Namespace, context: GenerationContext) {
        // Generate imports first
        namespace.ownedImport.forEach { import ->
            val importText = GeneratorFactory.generate(import, context)
            if (importText.isNotEmpty()) {
                appendLine(importText)
            }
        }

        if (namespace.ownedImport.isNotEmpty()) {
            appendLine()
        }

        // Generate members
        namespace.ownedMembership.forEach { membership ->
            val element = when (membership) {
                is OwningMembership -> membership.ownedMemberElement
                else -> membership.memberElement
            }

            val elementText = GeneratorFactory.generate(element, context)
            if (elementText.isNotEmpty()) {
                appendLine(elementText)
                if (options.blankLinesBetweenMembers) {
                    appendLine()
                }
            }
        }
    }

    /**
     * Collect all imported namespace qualified names for name resolution.
     */
    private fun collectImports(namespace: Namespace): Set<String> {
        val imports = mutableSetOf<String>()

        namespace.ownedImport.forEach { import ->
            if (import is NamespaceImport) {
                import.importedNamespace.qualifiedName?.let { imports.add(it) }
            }
        }

        return imports
    }

    companion object {
        /**
         * Create a writer with default options.
         */
        fun default(): KerMLWriter = KerMLWriter()

        /**
         * Create a writer that uses keyword syntax instead of symbols.
         */
        fun verbose(): KerMLWriter = KerMLWriter(
            GenerationOptions(preferSymbolicSyntax = false)
        )

        /**
         * Create a writer that emits compact output (no blank lines).
         */
        fun compact(): KerMLWriter = KerMLWriter(
            GenerationOptions(blankLinesBetweenMembers = false)
        )
    }
}
