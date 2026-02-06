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
package org.openmbee.gearshift.kerml.parser.visitors

import org.openmbee.gearshift.generated.interfaces.Feature
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseFeatureVisitor

/**
 * Visitor for Feature elements.
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
 *
 * Feature extends Type. Uses inherited parsing from BaseFeatureVisitor.
 */
class FeatureVisitor : BaseFeatureVisitor<KerMLParser.FeatureContext, Feature>() {

    override fun visit(ctx: KerMLParser.FeatureContext, kermlParseContext: KermlParseContext): Feature {
        // Extract names before creating element (needed for deterministic library IDs)
        val featureIdentification = ctx.featureDeclaration()?.featureIdentification()
        val (shortName, name) = extractFeatureIdentificationNames(featureIdentification)

        // Create feature with names for deterministic ID generation
        val feature = kermlParseContext.create<Feature>(
            declaredName = name,
            declaredShortName = shortName
        )

        // Parse feature prefix (handles featurePrefix, basicFeaturePrefix, endFeaturePrefix)
        parseFeaturePrefixFromContext(ctx, feature)

        // Parse feature declaration (inherited from BaseFeatureVisitor)
        // Note: names are already set above
        ctx.featureDeclaration()?.let { decl ->
            parseFeatureDeclaration(decl, feature, kermlParseContext)
        }

        // Create child context for nested elements
        val childContext = kermlParseContext.withParent(feature, feature.declaredName ?: "")

        // Note: Ownership is handled automatically by the FeatureImpl constructor
        // via OwnershipResolver, which creates a FeatureMembership with the correct
        // association ends (ownedMemberFeature, owningType).

        // Parse value part (inherited from BaseFeatureVisitor)
        parseValuePart(ctx.valuePart(), feature, childContext)

        // Parse type body (inherited from BaseTypeVisitor)
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, childContext)
        }

        return feature
    }

    /**
     * Parse feature prefix from the feature context.
     *
     * The feature grammar has multiple ways to specify the prefix:
     * - featurePrefix (which contains basicFeaturePrefix or endFeaturePrefix)
     * - standalone basicFeaturePrefix
     * - standalone endFeaturePrefix
     */
    private fun parseFeaturePrefixFromContext(ctx: KerMLParser.FeatureContext, feature: Feature) {
        // Handle featurePrefix
        parseFeaturePrefixContext(ctx.featurePrefix(), feature)

        // Handle standalone basicFeaturePrefix
        ctx.basicFeaturePrefix()?.let { basicPrefix ->
            parseBasicFeaturePrefix(basicPrefix, feature)
        }

        // Handle standalone endFeaturePrefix
        ctx.endFeaturePrefix()?.let { endPrefix ->
            parseEndFeaturePrefix(endPrefix, feature)
        }
    }
}
