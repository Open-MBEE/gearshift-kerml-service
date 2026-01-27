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
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseFeatureVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext

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

    override fun visit(ctx: KerMLParser.SuccessionContext, parseContext: ParseContext): Succession {
        val succession = parseContext.create<Succession>()

        // Parse feature prefix (inherited from BaseFeatureVisitor)
        parseFeaturePrefixContext(ctx.featurePrefix(), succession)

        // Parse succession declaration - succession-specific
        ctx.successionDeclaration()?.let { decl ->
            parseSuccessionDeclaration(decl, succession, parseContext)
        }

        // Create child context for nested elements
        val childContext = parseContext.withParent(succession, succession.declaredName ?: "")

        // Create membership with parent type (inherited from BaseTypeVisitor)
        createFeatureMembership(succession, parseContext)

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
        parseContext: ParseContext
    ) {
        ctx.featureDeclaration()?.let { decl ->
            parseFeatureDeclaration(decl, succession, parseContext)
        }

        ctx.isSufficient?.let { succession.isSufficient = true }

        ctx.connectorEndMember()?.forEach { _ ->
            // TODO: Parse connector end members for succession (first -> then)
        }
    }
}
