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

class SuccessionGenerator : BaseFeatureGenerator<Succession>() {

    override fun generate(element: Succession, context: GenerationContext): String = buildString {
        append(context.currentIndent())
        append(generateFeaturePrefix(element))
        append("succession ")
        append(generateFeatureDeclaration(element, context))

        // Find end features via structural containment (same approach as ConnectorGenerator)
        val allMembers = getExplicitMembers(element, context)
        val endEntries = allMembers.mapNotNull { membership ->
            val el = when (membership) {
                is OwningMembership -> membership.ownedMemberElement
                else -> membership.memberElement
            }
            (el as? Feature)?.takeIf { it.isEnd }?.let { membership to it }
        }
        val ends = endEntries.map { it.second }

        if (ends.size == 2) {
            val end1 = ConnectorGenerator.resolveEndName(ends[0], context)
            val end2 = ConnectorGenerator.resolveEndName(ends[1], context)
            if (end1.isNotEmpty() && end2.isNotEmpty()) {
                // Shorthand: succession first X then Y;
                append("first $end1 then $end2;")
                return@buildString
            }
        }

        append(generateTypeBody(element, context))
    }
}
