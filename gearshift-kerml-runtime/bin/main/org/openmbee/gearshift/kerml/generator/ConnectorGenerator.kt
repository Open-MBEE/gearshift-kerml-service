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

import org.openmbee.gearshift.generated.interfaces.Connector
import org.openmbee.gearshift.kerml.generator.base.BaseFeatureGenerator

class ConnectorGenerator : BaseFeatureGenerator<Connector>() {

    override fun generate(element: Connector, context: GenerationContext): String = buildString {
        append(context.currentIndent())
        append(generateFeaturePrefix(element))
        append("connector ")
        append(generateFeatureDeclaration(element, context))

        // Connector ends
        val ends = element.connectorEnd
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

        append(generateTypeBody(element, context))
    }

    companion object {
        fun resolveEndName(end: org.openmbee.gearshift.generated.interfaces.Feature, context: GenerationContext): String {
            // Try to find the subsetted feature reference
            val subsettedFeature = end.ownedSubsetting.firstOrNull()?.subsettedFeature
            return if (subsettedFeature != null) {
                context.resolveDisplayName(subsettedFeature)
            } else {
                context.resolveDisplayName(end)
            }
        }
    }
}
