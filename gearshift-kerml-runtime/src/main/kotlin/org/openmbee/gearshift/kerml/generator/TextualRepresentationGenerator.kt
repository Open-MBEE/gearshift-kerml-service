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

import org.openmbee.gearshift.generated.interfaces.TextualRepresentation
import org.openmbee.gearshift.kerml.generator.base.BaseElementGenerator

class TextualRepresentationGenerator : BaseElementGenerator<TextualRepresentation>() {

    override fun generate(element: TextualRepresentation, context: GenerationContext): String = buildString {
        val indent = context.currentIndent()
        append(indent)

        append("rep ")
        append(generateIdentification(element))

        // Language
        append(" language \"")
        append(escapeStringValue(element.language))
        append("\"")

        // Body
        appendLine()
        append(indent)
        append("/* ")
        append(element.body)
        append(" */")
    }

    private fun escapeStringValue(value: String): String {
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
    }
}
