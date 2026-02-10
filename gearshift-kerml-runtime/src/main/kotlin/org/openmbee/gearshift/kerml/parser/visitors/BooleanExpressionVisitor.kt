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

import org.openmbee.gearshift.generated.interfaces.BooleanExpression
import org.openmbee.gearshift.generated.interfaces.ResultExpressionMembership
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseFeatureVisitor

/**
 * Visitor for BooleanExpression elements.
 *
 * Per KerML spec 8.2.5.7.4: BooleanExpressions are expressions typed by Predicates.
 *
 * Grammar:
 * ```
 * booleanExpression
 *     : featurePrefix
 *       BOOL featureDeclaration valuePart?
 *       functionBody
 *     ;
 * ```
 *
 * BooleanExpression extends Expression.
 */
class BooleanExpressionVisitor : BaseFeatureVisitor<KerMLParser.BooleanExpressionContext, BooleanExpression>() {

    override fun visit(
        ctx: KerMLParser.BooleanExpressionContext,
        kermlParseContext: KermlParseContext
    ): BooleanExpression {
        val boolExpr = kermlParseContext.create<BooleanExpression>()

        // Parse feature prefix (inherited from BaseFeatureVisitor)
        parseFeaturePrefixContext(ctx.featurePrefix(), boolExpr)

        // Parse feature declaration (inherited from BaseFeatureVisitor)
        ctx.featureDeclaration()?.let { decl ->
            parseFeatureDeclaration(decl, boolExpr, kermlParseContext)
        }

        // Create child context for nested elements
        val childContext = kermlParseContext.withParent(boolExpr, boolExpr.declaredName ?: "")

        // Create membership with parent type (inherited from BaseTypeVisitor)
        createFeatureMembership(boolExpr, kermlParseContext)

        // Parse value part (inherited from BaseFeatureVisitor)
        parseValuePart(ctx.valuePart(), boolExpr, childContext)

        // Parse function body - expression-specific (uses functionBody not typeBody)
        ctx.functionBody()?.let { body ->
            parseFunctionBody(body, childContext)
        }

        return boolExpr
    }

    /**
     * Parse function body - expression-specific.
     */
    private fun parseFunctionBody(
        ctx: KerMLParser.FunctionBodyContext,
        kermlParseContext: KermlParseContext
    ) {
        ctx.functionBodyPart()?.let { bodyPart ->
            bodyPart.typeBodyElement()?.forEach { bodyElement ->
                parseTypeBodyElement(bodyElement, kermlParseContext)
            }

            bodyPart.returnFeatureMember()?.forEach { returnMember ->
                returnMember.featureElement()?.let { fe ->
                    parseFeatureElement(fe, kermlParseContext)
                }
            }

            bodyPart.resultExpressionMember()?.let { remCtx ->
                val membership = kermlParseContext.create<ResultExpressionMembership>()
                remCtx.ownedExpression()?.let { ownedExprCtx ->
                    val resultExpr = OwnedExpressionVisitor().visit(ownedExprCtx, kermlParseContext)
                    membership.ownedMemberElement = resultExpr
                    // Store result expression directly for efficient access by analysis services
                    (kermlParseContext.parent as? org.openmbee.mdm.framework.runtime.MDMObject)
                        ?.setProperty("resultExpression", resultExpr)
                }
            }
        }
    }
}
