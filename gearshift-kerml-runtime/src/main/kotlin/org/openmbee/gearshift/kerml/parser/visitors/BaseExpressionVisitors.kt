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
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseFeatureVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.registerReference

/**
 * Visitor for BaseExpression elements.
 *
 * Dispatches to specific expression type visitors based on the grammar:
 * ```
 * baseExpression
 *     : nullExpression
 *     | literalExpression
 *     | featureReferenceExpression
 *     | metadataAccessExpression
 *     | invocationExpression
 *     | constructorExpression
 *     | bodyExpression
 *     ;
 * ```
 */
class BaseExpressionDispatcher : BaseFeatureVisitor<KerMLParser.BaseExpressionContext, Expression>() {

    override fun visit(ctx: KerMLParser.BaseExpressionContext, parseContext: ParseContext): Expression {
        ctx.nullExpression()?.let { nullCtx ->
            return NullExpressionVisitor().visit(nullCtx, parseContext)
        }

        ctx.literalExpression()?.let { literalCtx ->
            return LiteralExpressionVisitor().visit(literalCtx, parseContext)
        }

        ctx.featureReferenceExpression()?.let { refCtx ->
            return FeatureReferenceExpressionVisitor().visit(refCtx, parseContext)
        }

        ctx.metadataAccessExpression()?.let { metaCtx ->
            return MetadataAccessExpressionVisitor().visit(metaCtx, parseContext)
        }

        ctx.invocationExpression()?.let { invCtx ->
            return InvocationExpressionVisitor().visit(invCtx, parseContext)
        }

        ctx.constructorExpression()?.let { consCtx ->
            return ConstructorExpressionVisitor().visit(consCtx, parseContext)
        }

        ctx.bodyExpression()?.let { bodyCtx ->
            return BodyExpressionVisitor().visit(bodyCtx, parseContext)
        }

        // Fallback - shouldn't reach here
        return parseContext.create<Expression>()
    }
}

/**
 * Visitor for NullExpression elements.
 *
 * Grammar:
 * ```
 * nullExpression
 *     : NULL | '(' ')'
 *     ;
 * ```
 */
class NullExpressionVisitor : BaseFeatureVisitor<KerMLParser.NullExpressionContext, NullExpression>() {

    override fun visit(ctx: KerMLParser.NullExpressionContext, parseContext: ParseContext): NullExpression {
        val expr = parseContext.create<NullExpression>()

        // Create membership with parent
        createFeatureMembership(expr, parseContext)

        return expr
    }
}

/**
 * Visitor for FeatureReferenceExpression elements.
 *
 * Grammar:
 * ```
 * featureReferenceExpression
 *     : featureReferenceMember emptyResultMember
 *     ;
 * featureReferenceMember
 *     : featureReference
 *     ;
 * featureReference
 *     : qualifiedName
 *     ;
 * ```
 */
class FeatureReferenceExpressionVisitor : BaseFeatureVisitor<KerMLParser.FeatureReferenceExpressionContext, FeatureReferenceExpression>() {

    override fun visit(ctx: KerMLParser.FeatureReferenceExpressionContext, parseContext: ParseContext): FeatureReferenceExpression {
        val expr = parseContext.create<FeatureReferenceExpression>()

        // Parse the feature reference
        ctx.featureReferenceMember()?.featureReference()?.qualifiedName()?.let { qn ->
            val referencedName = extractQualifiedName(qn)
            parseContext.registerReference(expr, "referent", referencedName)
        }

        // Create membership with parent
        createFeatureMembership(expr, parseContext)

        return expr
    }
}

/**
 * Visitor for MetadataAccessExpression elements.
 *
 * Grammar:
 * ```
 * metadataAccessExpression
 *     : elementReferenceMember '.' 'metadata'
 *     ;
 * elementReferenceMember
 *     : qualifiedName
 *     ;
 * ```
 */
class MetadataAccessExpressionVisitor : BaseFeatureVisitor<KerMLParser.MetadataAccessExpressionContext, MetadataAccessExpression>() {

    override fun visit(ctx: KerMLParser.MetadataAccessExpressionContext, parseContext: ParseContext): MetadataAccessExpression {
        val expr = parseContext.create<MetadataAccessExpression>()

        // Parse the element reference
        ctx.elementReferenceMember()?.qualifiedName()?.let { qn ->
            val referencedName = extractQualifiedName(qn)
            parseContext.registerReference(expr, "referencedElement", referencedName)
        }

        // Create membership with parent
        createFeatureMembership(expr, parseContext)

        return expr
    }
}

/**
 * Visitor for InvocationExpression elements.
 *
 * Grammar:
 * ```
 * invocationExpression
 *     : instantiatedTypeMember argumentList emptyResultMember
 *     ;
 * instantiatedTypeMember
 *     : instantiatedTypeReference | ownedFeatureChainMember
 *     ;
 * instantiatedTypeReference
 *     : qualifiedName
 *     ;
 * ```
 */
class InvocationExpressionVisitor : BaseFeatureVisitor<KerMLParser.InvocationExpressionContext, InvocationExpression>() {

    override fun visit(ctx: KerMLParser.InvocationExpressionContext, parseContext: ParseContext): InvocationExpression {
        val expr = parseContext.create<InvocationExpression>()

        // Parse the invoked type
        ctx.instantiatedTypeMember()?.instantiatedTypeReference()?.qualifiedName()?.let { qn ->
            val typeName = extractQualifiedName(qn)
            // Register reference to the invoked function/type
            val typing = parseContext.create<FeatureTyping>()
            typing.typedFeature = expr
            parseContext.registerReference(typing, "type", typeName)
        }

        // Create membership with parent
        createFeatureMembership(expr, parseContext)

        return expr
    }
}

/**
 * Visitor for ConstructorExpression elements.
 *
 * Grammar:
 * ```
 * constructorExpression
 *     : 'new' instantiatedTypeMember constructorResultMember
 *     ;
 * ```
 */
class ConstructorExpressionVisitor : BaseFeatureVisitor<KerMLParser.ConstructorExpressionContext, ConstructorExpression>() {

    override fun visit(ctx: KerMLParser.ConstructorExpressionContext, parseContext: ParseContext): ConstructorExpression {
        val expr = parseContext.create<ConstructorExpression>()

        // Parse the instantiated type
        ctx.instantiatedTypeMember()?.instantiatedTypeReference()?.qualifiedName()?.let { qn ->
            val typeName = extractQualifiedName(qn)
            // Register reference to the constructed type
            val typing = parseContext.create<FeatureTyping>()
            typing.typedFeature = expr
            parseContext.registerReference(typing, "type", typeName)
        }

        // Create membership with parent
        createFeatureMembership(expr, parseContext)

        return expr
    }
}

/**
 * Visitor for BodyExpression elements.
 *
 * Grammar:
 * ```
 * bodyExpression
 *     : expressionBodyMember
 *     ;
 * expressionBodyMember
 *     : expressionBody
 *     ;
 * expressionBody
 *     : '{' functionBodyPart '}'
 *     ;
 * ```
 */
class BodyExpressionVisitor : BaseFeatureVisitor<KerMLParser.BodyExpressionContext, FeatureReferenceExpression>() {

    override fun visit(ctx: KerMLParser.BodyExpressionContext, parseContext: ParseContext): FeatureReferenceExpression {
        val expr = parseContext.create<FeatureReferenceExpression>()

        // Parse the expression body
        val childContext = parseContext.withParent(expr, "")
        ctx.expressionBodyMember()?.expressionBody()?.functionBodyPart()?.let { bodyPart ->
            parseFunctionBodyPart(bodyPart, childContext)
        }

        // Create membership with parent
        createFeatureMembership(expr, parseContext)

        return expr
    }

    /**
     * Parse function body part.
     */
    private fun parseFunctionBodyPart(ctx: KerMLParser.FunctionBodyPartContext, parseContext: ParseContext) {
        ctx.typeBodyElement()?.forEach { bodyElement ->
            parseTypeBodyElement(bodyElement, parseContext)
        }

        ctx.returnFeatureMember()?.forEach { returnMember ->
            returnMember.featureElement()?.let { fe ->
                parseFeatureElement(fe, parseContext)
            }
        }
    }
}
