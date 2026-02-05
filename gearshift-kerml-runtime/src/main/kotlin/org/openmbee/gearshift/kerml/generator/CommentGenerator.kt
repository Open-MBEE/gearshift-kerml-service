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

import org.openmbee.gearshift.generated.interfaces.Comment
import org.openmbee.gearshift.kerml.generator.base.BaseElementGenerator

/**
 * Generator for Comment elements.
 *
 * Per KerML spec 8.2.3.3.2: Comments provide textual annotations.
 *
 * Grammar:
 * ```
 * comment
 *     : ( 'comment' Identification
 *         ( 'about' ownedRelationship += Annotation
 *           ( ',' ownedRelationship += Annotation )*
 *         )?
 *       )?
 *       ( 'locale' locale = STRING_VALUE )?
 *       body = REGULAR_COMMENT
 *     ;
 * ```
 */
class CommentGenerator : BaseElementGenerator<Comment>() {

    override fun generate(element: Comment, context: GenerationContext): String = buildString {
        val indent = context.currentIndent()

        // Indentation
        append(indent)

        // If the comment has a name or annotations, use the full syntax
        val hasName = element.declaredName != null || element.declaredShortName != null
        val hasAnnotations = element.ownedAnnotation.isNotEmpty()

        if (hasName || hasAnnotations) {
            append("comment ")
            append(generateIdentification(element))

            // About clause
            if (hasAnnotations) {
                append(" about ")
                append(element.ownedAnnotation.mapNotNull { annotation ->
                    annotation.annotatedElement?.qualifiedName
                        ?: annotation.annotatedElement?.declaredName
                }.joinToString(", "))
            }
        }

        // Locale
        element.locale?.let { locale ->
            if (hasName || hasAnnotations) append(" ")
            append("locale \"")
            append(escapeStringValue(locale))
            append("\"")
        }

        // Body - as a REGULAR_COMMENT (/* ... */)
        if (hasName || hasAnnotations || element.locale != null) {
            appendLine()
            append(indent)
        }
        append("/* ")
        append(element.body)
        append(" */")
    }

    /**
     * Escape special characters in a string value.
     */
    private fun escapeStringValue(value: String): String {
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
    }
}
