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

import org.openmbee.gearshift.generated.interfaces.MetadataFeature
import org.openmbee.gearshift.kerml.generator.base.BaseFeatureGenerator

class MetadataFeatureGenerator : BaseFeatureGenerator<MetadataFeature>() {

    override fun generate(element: MetadataFeature, context: GenerationContext): String = buildString {
        val indent = context.currentIndent()
        append(indent)

        val hasName = element.declaredName != null || element.declaredShortName != null
        val metaclassName = element.metaclass?.let { context.resolveDisplayName(it) }

        if (hasName) {
            // Long form: metadata Name : MetaclassName about Element { body }
            append("metadata ")
            append(generateFeatureDeclaration(element, context))

            // About clause
            val annotatedElements = element.annotatedElement
            if (annotatedElements.isNotEmpty()) {
                append(" about ")
                append(annotatedElements.joinToString(", ") { context.resolveDisplayName(it) })
            }

            append(generateTypeBody(element, context))
        } else {
            // Short form: @MetaclassName
            if (metaclassName != null) {
                append("@$metaclassName")
            } else {
                append("metadata")
            }

            val members = getExplicitMembers(element, context)
            if (members.isNotEmpty()) {
                append(generateTypeBody(element, context))
            } else {
                append(";")
            }
        }
    }
}
