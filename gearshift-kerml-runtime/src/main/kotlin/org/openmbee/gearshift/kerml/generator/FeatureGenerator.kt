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

import org.openmbee.gearshift.generated.interfaces.Feature
import org.openmbee.gearshift.kerml.generator.base.BaseFeatureGenerator

/**
 * Generator for Feature elements.
 *
 * Per KerML spec 8.2.4.3: Features are typed structural elements that belong to Types.
 *
 * Grammar:
 * ```
 * feature
 *     : ( featurePrefix
 *         ( FEATURE | prefixMetadataMember )
 *         featureDeclaration?
 *       | ( endFeaturePrefix | basicFeaturePrefix )
 *         featureDeclaration
 *       )
 *       valuePart? typeBody
 *     ;
 * ```
 */
class FeatureGenerator : BaseFeatureGenerator<Feature>() {

    override fun generate(element: Feature, context: GenerationContext): String = buildString {
        val indent = context.currentIndent()

        // Indentation
        append(indent)

        // featurePrefix: direction derived abstract composite/portion var/const end
        append(generateFeaturePrefix(element))

        // FEATURE keyword - include when there's no identification or when needed for clarity
        if (needsFeatureKeyword(element)) {
            append("feature ")
        }

        // featureDeclaration
        append(generateFeatureDeclaration(element, context))

        // valuePart
        append(generateValuePart(element, context))

        // typeBody
        append(generateTypeBody(element, context))
    }

    /**
     * Determine if the 'feature' keyword is needed.
     *
     * The keyword can be omitted when the feature declaration provides
     * enough context (has a name, typing, etc.). However, it's required
     * for anonymous features or when there's no specialization.
     */
    private fun needsFeatureKeyword(feature: Feature): Boolean {
        // If the feature has no name and no specializations, we need the keyword
        val hasName = feature.declaredName != null || feature.declaredShortName != null
        val hasTyping = feature.ownedTyping.isNotEmpty()
        val hasSubsetting = feature.ownedSubsetting.isNotEmpty()
        val hasRedefinition = feature.ownedRedefinition.isNotEmpty()

        // If completely anonymous with no specializations, need the keyword
        if (!hasName && !hasTyping && !hasSubsetting && !hasRedefinition) {
            return true
        }

        // If it has modifiers but no name, the keyword helps readability
        if (!hasName && (feature.isDerived || feature.isComposite || feature.isPortion)) {
            return true
        }

        return false
    }
}
