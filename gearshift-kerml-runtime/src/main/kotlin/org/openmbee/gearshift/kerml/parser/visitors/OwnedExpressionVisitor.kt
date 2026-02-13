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

import org.openmbee.gearshift.generated.interfaces.*
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseFeatureVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.registerReference
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
 * Binary operators are split into precedence groups in the grammar so ANTLR
 * builds correct parse trees (e.g., `a == b + c` → `a == (b + c)`).
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

        // Binary operators — check each precedence-grouped sub-rule
        val binaryOp = ctx.equalityOperator()
            ?: ctx.relationalOperator()
            ?: ctx.additiveOperator()
            ?: ctx.multiplicativeOperator()
            ?: ctx.exponentialOperator()
            ?: ctx.bitwiseOperator()
            ?: ctx.rangeOperator()
        if (binaryOp != null) {
            val subExprs = ctx.ownedExpression()
            if (subExprs.size >= 2) {
                return visitOperator(binaryOp.text, subExprs, kermlParseContext)
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
     *     | primaryExpression HASH '(' sequenceExpressionListMember ')'
     *     | primaryExpression '.' featureChainMember
     *     | primaryExpression '.' bodyArgumentMember
     *     | primaryExpression '.?' bodyArgumentMember
     *     | primaryExpression '->' invocationTypeMember (body|funcRef|argList) emptyResultMember
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

        // Left-recursive cases — all have a source primaryExpression
        val sourcePrimCtx = ctx.primaryExpression() ?: return kermlParseContext.create<Expression>()

        // Dot navigation: primaryExpression '.' featureChainMember
        ctx.featureChainMember()?.let { chainMember ->
            return visitFeatureChainExpression(sourcePrimCtx, chainMember, kermlParseContext)
        }

        // Collect/Select with body: primaryExpression ('.' | '.?') bodyArgumentMember
        ctx.bodyArgumentMember()?.let { bodyMember ->
            val isSelect = ctx.DOT_QUESTION() != null
            return visitCollectOrSelectExpression(sourcePrimCtx, bodyMember, isSelect, kermlParseContext)
        }

        // Arrow operation: primaryExpression '->' invocationTypeMember (body|funcRef|argList)
        if (ctx.ARROW() != null) {
            ctx.invocationTypeMember()?.let { typeMember ->
                return visitFunctionOperationExpression(sourcePrimCtx, typeMember, ctx, kermlParseContext)
            }
        }

        // Fallback for unhandled primary expression forms (bracket, index, etc.)
        return kermlParseContext.create<Expression>()
    }

    /**
     * Handle dot-navigation: `source.featureName`
     *
     * Creates a FeatureChainExpression with operator='.', the source as first argument,
     * and the target feature name stored for the evaluator.
     */
    private fun visitFeatureChainExpression(
        sourcePrimCtx: KerMLParser.PrimaryExpressionContext,
        chainMember: KerMLParser.FeatureChainMemberContext,
        kermlParseContext: KermlParseContext
    ): Expression {
        val chainExpr = kermlParseContext.create<FeatureChainExpression>()
        chainExpr.operator = "."

        val childContext = kermlParseContext.withParent(chainExpr, "")

        // Visit source expression as the first argument
        val sourceExpr = visitPrimaryExpression(sourcePrimCtx, childContext)
        (chainExpr as MDMObject).setProperty("_arguments", listOf(sourceExpr))

        // Extract target feature name from featureChainMember
        val featureName = chainMember.featureReferenceMember()
            ?.featureReference()?.qualifiedName()?.let { extractQualifiedName(it) }
        if (featureName != null) {
            (chainExpr as MDMObject).setProperty("_targetFeatureName", featureName)
            // Also create a Membership so the derived targetFeature property can work
            val membership = kermlParseContext.create<Membership>()
            membership.membershipOwningNamespace = chainExpr
            kermlParseContext.registerReference(membership, "memberElement", featureName)
        }

        createFeatureMembership(chainExpr, kermlParseContext)
        return chainExpr
    }

    /**
     * Handle collect/select with body: `source.{body}` or `source.?{predicate}`
     */
    private fun visitCollectOrSelectExpression(
        sourcePrimCtx: KerMLParser.PrimaryExpressionContext,
        bodyMember: KerMLParser.BodyArgumentMemberContext,
        isSelect: Boolean,
        kermlParseContext: KermlParseContext
    ): Expression {
        val expr = if (isSelect) {
            kermlParseContext.create<SelectExpression>().also { it.operator = "select" }
        } else {
            kermlParseContext.create<CollectExpression>().also { it.operator = "collect" }
        }

        val childContext = kermlParseContext.withParent(expr, "")

        // Visit source expression
        val sourceExpr = visitPrimaryExpression(sourcePrimCtx, childContext)

        // Visit body expression
        val bodyExpr = bodyMember.bodyArgument()?.bodyArgumentValue()?.bodyExpression()?.let { bodyCtx ->
            BodyExpressionVisitor().visit(bodyCtx, childContext)
        } ?: kermlParseContext.create<Expression>()

        (expr as MDMObject).setProperty("_arguments", listOf(sourceExpr, bodyExpr))
        createFeatureMembership(expr, kermlParseContext)
        return expr
    }

    /**
     * Handle arrow operation: `source->funcName(args)` or `source->funcName{body}`
     *
     * Creates an InvocationExpression for the function, with the source as the first
     * (implicit) argument.
     */
    private fun visitFunctionOperationExpression(
        sourcePrimCtx: KerMLParser.PrimaryExpressionContext,
        typeMember: KerMLParser.InvocationTypeMemberContext,
        ctx: KerMLParser.PrimaryExpressionContext,
        kermlParseContext: KermlParseContext
    ): Expression {
        val invExpr = kermlParseContext.create<InvocationExpression>()

        val childContext = kermlParseContext.withParent(invExpr, "")

        // Visit source expression (becomes first implicit argument)
        val sourceExpr = visitPrimaryExpression(sourcePrimCtx, childContext)

        // Resolve the function type: invocationTypeMember -> invocationType -> ownedFeatureTyping -> generalType -> qualifiedName
        val funcName = typeMember.invocationType()?.ownedFeatureTyping()
            ?.generalType()?.qualifiedName()?.let { extractQualifiedName(it) }
        if (funcName != null) {
            (invExpr as MDMObject).setProperty("_functionName", funcName)
            val typing = kermlParseContext.create<FeatureTyping>()
            typing.typedFeature = invExpr
            kermlParseContext.registerReference(typing, "type", funcName)
        }

        // Collect additional arguments from body or argument list
        val additionalArgs = mutableListOf<Expression>()
        ctx.bodyArgumentMember()?.bodyArgument()?.bodyArgumentValue()?.bodyExpression()?.let { bodyCtx ->
            additionalArgs.add(BodyExpressionVisitor().visit(bodyCtx, childContext))
        }
        // TODO: handle argumentList and functionReferenceArgumentMember if needed

        (invExpr as MDMObject).setProperty("_arguments", listOf(sourceExpr) + additionalArgs)
        createFeatureMembership(invExpr, kermlParseContext)
        return invExpr
    }
}
