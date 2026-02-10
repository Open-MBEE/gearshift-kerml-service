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

import org.openmbee.gearshift.generated.interfaces.Expression
import org.openmbee.gearshift.generated.interfaces.OperatorExpression
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseFeatureVisitor
import org.openmbee.mdm.framework.runtime.MDMObject

/**
 * Visitor for the left-recursive `ownedExpression` grammar rule.
 *
 * Handles all operator expression forms defined in KerML 8.2.5.8.1:
 * - Binary operators: `x > 0`, `a + b`, `x == 42`
 * - Conditional binary operators: `a or b`, `a and b`, `a implies b`
 * - Unary operators: `-x`, `not b`
 * - Conditional (ternary): `if cond ? then : else`
 * - Primary expressions (delegates to [BaseExpressionDispatcher])
 *
 * Grammar (refactored with direct left recursion):
 * ```
 * ownedExpression
 *     : IF ownedExpression QUESTION ownedExpression ELSE ownedExpression emptyResultMember
 *     | ownedExpression conditionalBinaryOperator ownedExpression emptyResultMember
 *     | ownedExpression binaryOperator ownedExpression emptyResultMember
 *     | unaryOperator ownedExpression emptyResultMember
 *     | ...
 *     | primaryExpression
 *     ;
 * ```
 */
class OwnedExpressionVisitor : BaseFeatureVisitor<KerMLParser.OwnedExpressionContext, Expression>() {

    override fun visit(ctx: KerMLParser.OwnedExpressionContext, kermlParseContext: KermlParseContext): Expression {
        // Conditional (ternary): IF ownedExpression QUESTION ownedExpression ELSE ownedExpression
        if (ctx.IF() != null) {
            val subExprs = ctx.ownedExpression()
            if (subExprs.size >= 3) {
                return visitOperator("if", subExprs, kermlParseContext)
            }
        }

        // Binary operator: ownedExpression binaryOperator ownedExpression
        ctx.binaryOperator()?.let { opCtx ->
            val subExprs = ctx.ownedExpression()
            if (subExprs.size >= 2) {
                return visitOperator(opCtx.text, subExprs, kermlParseContext)
            }
        }

        // Conditional binary operator: ownedExpression conditionalBinaryOperator ownedExpression
        ctx.conditionalBinaryOperator()?.let { opCtx ->
            val subExprs = ctx.ownedExpression()
            if (subExprs.size >= 2) {
                return visitOperator(opCtx.text, subExprs, kermlParseContext)
            }
        }

        // Unary operator: unaryOperator ownedExpression
        ctx.unaryOperator()?.let { opCtx ->
            val subExprs = ctx.ownedExpression()
            if (subExprs.isNotEmpty()) {
                return visitOperator(opCtx.text, subExprs, kermlParseContext)
            }
        }

        // Primary expression (literals, references, invocations, parenthesized)
        ctx.primaryExpression()?.let { primCtx ->
            return visitPrimaryExpression(primCtx, kermlParseContext)
        }

        // Fallback for unhandled alternatives (classification, cast, extent, etc.)
        return kermlParseContext.create<Expression>()
    }

    /**
     * Create an OperatorExpression with the given operator and argument sub-expressions.
     */
    private fun visitOperator(
        operator: String,
        subExprs: List<KerMLParser.OwnedExpressionContext>,
        kermlParseContext: KermlParseContext
    ): OperatorExpression {
        val opExpr = kermlParseContext.create<OperatorExpression>()
        opExpr.operator = operator

        // Create child context with OperatorExpression as parent
        val childContext = kermlParseContext.withParent(opExpr, "")

        // Visit each argument sub-expression and collect them for direct access
        val args = mutableListOf<Expression>()
        for (subExpr in subExprs) {
            args.add(visit(subExpr, childContext))
        }

        // Store arguments as raw property for direct access by analysis services
        // (avoids navigating derived properties like ownedFeature/argument)
        (opExpr as? MDMObject)?.setProperty("_arguments", args)

        // Link this expression to its parent
        createFeatureMembership(opExpr, kermlParseContext)

        return opExpr
    }

    /**
     * Handle primary expressions (the base case of expression recursion).
     *
     * primaryExpression is also left-recursive:
     * ```
     * primaryExpression
     *     : primaryExpression '[' sequenceExpressionListMember ']'
     *     | primaryExpression '.' featureChainMember
     *     | ...
     *     | '(' sequenceExpressionList ')'
     *     | baseExpression
     *     ;
     * ```
     */
    private fun visitPrimaryExpression(
        ctx: KerMLParser.PrimaryExpressionContext,
        kermlParseContext: KermlParseContext
    ): Expression {
        // Base expression (literals, references, invocations)
        ctx.baseExpression()?.let { baseCtx ->
            return BaseExpressionDispatcher().visit(baseCtx, kermlParseContext)
        }

        // Parenthesized expression: '(' sequenceExpressionList ')'
        ctx.sequenceExpressionList()?.let { seqCtx ->
            seqCtx.ownedExpression()?.let { innerExpr ->
                return visit(innerExpr, kermlParseContext)
            }
        }

        // Fallback for unhandled primary expression forms
        return kermlParseContext.create<Expression>()
    }
}
