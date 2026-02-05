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

import org.openmbee.gearshift.generated.interfaces.Import
import org.openmbee.gearshift.generated.interfaces.MembershipImport
import org.openmbee.gearshift.generated.interfaces.NamespaceImport
import org.openmbee.gearshift.kerml.generator.base.BaseElementGenerator

/**
 * Generator for Import elements.
 *
 * Per KerML spec 8.2.3.4.3: Imports make elements from one namespace visible in another.
 *
 * Grammar:
 * ```
 * import_
 *     : membershipImport
 *     | namespaceImport
 *     ;
 *
 * membershipImport
 *     : importPrefix 'import' ( isImportAll = 'all' )? ImportedMembership ';'
 *     ;
 *
 * namespaceImport
 *     : importPrefix 'import' ( isImportAll = 'all' )? ImportedNamespace ( '::' '*' ( '::' isRecursive = '**' )? )? ';'
 *     ;
 *
 * importPrefix
 *     : visibility?
 *     ;
 * ```
 */
class ImportGenerator : BaseElementGenerator<Import>() {

    override fun generate(element: Import, context: GenerationContext): String = buildString {
        val indent = context.currentIndent()

        // Indentation
        append(indent)

        // Visibility prefix
        when (element.visibility) {
            "private" -> append("private ")
            "protected" -> append("protected ")
            // public is default, don't emit
        }

        // IMPORT keyword
        append("import ")

        // isImportAll: 'all'
        if (element.isImportAll) {
            append("all ")
        }

        // Imported element (namespace or membership)
        when (element) {
            is NamespaceImport -> {
                val namespaceName = element.importedNamespace.qualifiedName
                    ?: element.importedNamespace.declaredName
                    ?: ""
                append(namespaceName)
                append("::*")
                if (element.isRecursive) {
                    append("::/**")
                }
            }
            is MembershipImport -> {
                val memberElement = element.importedMembership.memberElement
                val elementName = memberElement.qualifiedName
                    ?: memberElement.declaredName
                    ?: ""
                append(elementName)
                if (element.isRecursive) {
                    append("::/**")
                }
            }
            else -> {
                // Generic import - use importedElement
                val elementName = element.importedElement.qualifiedName
                    ?: element.importedElement.declaredName
                    ?: ""
                append(elementName)
            }
        }

        append(";")
    }
}
