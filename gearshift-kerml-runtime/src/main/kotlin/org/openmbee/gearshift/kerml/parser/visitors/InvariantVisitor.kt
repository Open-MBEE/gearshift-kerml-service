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

import org.openmbee.gearshift.generated.interfaces.Invariant
import org.openmbee.gearshift.generated.interfaces.ResultExpressionMembership
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseFeatureVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext

/**
 * Visitor for Invariant elements.
 *
 * Per KerML spec 8.2.5.7.4: Invariants are boolean expressions that must hold.
 *
 * Grammar:
 * ```
 * invariant
 *     : featurePrefix
 *       INV ( TRUE | isNegated=FALSE )?
 *       featureDeclaration valuePart?
 *       functionBody
 *     ;
 * ```
 *
 * Invariant extends BooleanExpression.
 */
class InvariantVisitor : BaseFeatureVisitor<KerMLParser.InvariantContext, Invariant>() {

    override fun visit(ctx: KerMLParser.InvariantContext, parseContext: ParseContext): Invariant {
        val invariant = parseContext.create<Invariant>()

        // Parse feature prefix (inherited from BaseFeatureVisitor)
        parseFeaturePrefixContext(ctx.featurePrefix(), invariant)

        // Parse isNegated (false keyword means negated invariant) - invariant-specific
        ctx.isNegated?.let {
            invariant.isNegated = true
        }

        // Parse feature declaration (inherited from BaseFeatureVisitor)
        ctx.featureDeclaration()?.let { decl ->
            parseFeatureDeclaration(decl, invariant, parseContext)
        }

        // Create child context for nested elements
        val childContext = parseContext.withParent(invariant, invariant.declaredName ?: "")

        // Create membership with parent type (inherited from BaseTypeVisitor)
        createFeatureMembership(invariant, parseContext)

        // Parse value part (inherited from BaseFeatureVisitor)
        parseValuePart(ctx.valuePart(), invariant, childContext)

        // Parse function body - expression-specific (uses functionBody not typeBody)
        ctx.functionBody()?.let { body ->
            parseFunctionBody(body, childContext)
        }

        return invariant
    }

    /**
     * Parse function body - expression-specific.
     */
    private fun parseFunctionBody(
        ctx: KerMLParser.FunctionBodyContext,
        parseContext: ParseContext
    ) {
        ctx.functionBodyPart()?.let { bodyPart ->
            bodyPart.typeBodyElement()?.forEach { bodyElement ->
                parseTypeBodyElement(bodyElement, parseContext)
            }

            bodyPart.returnFeatureMember()?.forEach { returnMember ->
                returnMember.featureElement()?.let { fe ->
                    parseFeatureElement(fe, parseContext)
                }
            }

            bodyPart.resultExpressionMember()?.let { _ ->
                parseContext.create<ResultExpressionMembership>()
            }
        }
    }
}
