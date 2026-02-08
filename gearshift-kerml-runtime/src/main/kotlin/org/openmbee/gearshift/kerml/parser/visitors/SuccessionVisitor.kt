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

import org.openmbee.gearshift.generated.interfaces.Succession
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseFeatureVisitor

/**
 * Visitor for Succession elements.
 *
 * Per KerML spec 8.2.5.5.3: Successions represent temporal ordering between occurrences.
 *
 * Grammar:
 * ```
 * succession
 *     : featurePrefix SUCCESSION
 *       successionDeclaration typeBody
 *     ;
 * ```
 *
 * Succession extends Connector. Uses inherited parsing from BaseFeatureVisitor.
 */
class SuccessionVisitor : BaseFeatureVisitor<KerMLParser.SuccessionContext, Succession>() {

    override fun visit(ctx: KerMLParser.SuccessionContext, kermlParseContext: KermlParseContext): Succession {
        val succession = kermlParseContext.create<Succession>()

        // Parse feature prefix (inherited from BaseFeatureVisitor)
        parseFeaturePrefixContext(ctx.featurePrefix(), succession)

        // Create child context so connector ends are owned by the succession
        val childContext = kermlParseContext.withParent(succession, succession.declaredName ?: "")

        // Parse succession declaration - succession-specific
        ctx.successionDeclaration()?.let { decl ->
            parseSuccessionDeclaration(decl, succession, childContext)
        }

        // Create membership with parent type (inherited from BaseTypeVisitor)
        createFeatureMembership(succession, kermlParseContext)

        // Parse type body (inherited from BaseTypeVisitor)
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, childContext)
        }

        return succession
    }

    /**
     * Parse succession declaration - succession-specific.
     */
    private fun parseSuccessionDeclaration(
        ctx: KerMLParser.SuccessionDeclarationContext,
        succession: Succession,
        kermlParseContext: KermlParseContext
    ) {
        ctx.featureDeclaration()?.let { decl ->
            parseFeatureDeclaration(decl, succession, kermlParseContext)
        }

        ctx.isSufficient?.let { succession.isSufficient = true }

        ctx.connectorEndMember()?.forEach { endMember ->
            parseConnectorEndMember(endMember, kermlParseContext)
        }
    }
}
