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

/**
 * Visitor for LiteralExpression elements.
 *
 * Dispatches to specific literal type visitors based on the grammar:
 * ```
 * literalExpression
 *     : literalBoolean
 *     | literalString
 *     | literalInteger
 *     | literalReal
 *     | literalInfinity
 *     ;
 * ```
 */
class LiteralExpressionVisitor : BaseFeatureVisitor<KerMLParser.LiteralExpressionContext, LiteralExpression>() {

    override fun visit(
        ctx: KerMLParser.LiteralExpressionContext,
        kermlParseContext: KermlParseContext
    ): LiteralExpression {
        // Dispatch to specific literal type
        ctx.literalBoolean()?.let { boolCtx ->
            return LiteralBooleanVisitor().visit(boolCtx, kermlParseContext)
        }

        ctx.literalString()?.let { stringCtx ->
            return LiteralStringVisitor().visit(stringCtx, kermlParseContext)
        }

        ctx.literalInteger()?.let { intCtx ->
            return LiteralIntegerVisitor().visit(intCtx, kermlParseContext)
        }

        ctx.literalReal()?.let { realCtx ->
            return LiteralRealVisitor().visit(realCtx, kermlParseContext)
        }

        ctx.literalInfinity()?.let { infCtx ->
            return LiteralInfinityVisitor().visit(infCtx, kermlParseContext)
        }

        // Fallback - shouldn't reach here
        return kermlParseContext.create<LiteralExpression>()
    }
}

/**
 * Visitor for LiteralBoolean elements.
 *
 * Grammar:
 * ```
 * literalBoolean
 *     : value=booleanValue
 *     ;
 * booleanValue
 *     : TRUE | FALSE
 *     ;
 * ```
 */
class LiteralBooleanVisitor : BaseFeatureVisitor<KerMLParser.LiteralBooleanContext, LiteralBoolean>() {

    override fun visit(ctx: KerMLParser.LiteralBooleanContext, kermlParseContext: KermlParseContext): LiteralBoolean {
        val literal = kermlParseContext.create<LiteralBoolean>()

        ctx.value?.let { boolValue ->
            literal.value = boolValue.TRUE() != null
        }

        // Create membership with parent
        createFeatureMembership(literal, kermlParseContext)

        return literal
    }
}

/**
 * Visitor for LiteralString elements.
 *
 * Grammar:
 * ```
 * literalString
 *     : value=STRING_VALUE
 *     ;
 * ```
 */
class LiteralStringVisitor : BaseFeatureVisitor<KerMLParser.LiteralStringContext, LiteralString>() {

    override fun visit(ctx: KerMLParser.LiteralStringContext, kermlParseContext: KermlParseContext): LiteralString {
        val literal = kermlParseContext.create<LiteralString>()

        ctx.value?.let { stringValue ->
            // Remove surrounding quotes from the string value
            val text = stringValue.text
            literal.value = if (text.startsWith("\"") && text.endsWith("\"")) {
                text.substring(1, text.length - 1)
            } else {
                text
            }
        }

        // Create membership with parent
        createFeatureMembership(literal, kermlParseContext)

        return literal
    }
}

/**
 * Visitor for LiteralInteger elements.
 *
 * Grammar:
 * ```
 * literalInteger
 *     : value=DECIMAL_VALUE
 *     ;
 * ```
 */
class LiteralIntegerVisitor : BaseFeatureVisitor<KerMLParser.LiteralIntegerContext, LiteralInteger>() {

    override fun visit(ctx: KerMLParser.LiteralIntegerContext, kermlParseContext: KermlParseContext): LiteralInteger {
        val literal = kermlParseContext.create<LiteralInteger>()

        ctx.value?.let { intValue ->
            literal.value = intValue.text.toIntOrNull() ?: 0
        }

        // Create membership with parent
        createFeatureMembership(literal, kermlParseContext)

        return literal
    }
}

/**
 * Visitor for LiteralReal elements.
 *
 * Grammar:
 * ```
 * literalReal
 *     : value=realValue
 *     ;
 * realValue
 *     : DECIMAL_VALUE? '.' ( DECIMAL_VALUE | EXPONENTIAL_VALUE )
 *     | EXPONENTIAL_VALUE
 *     ;
 * ```
 */
class LiteralRealVisitor : BaseFeatureVisitor<KerMLParser.LiteralRealContext, LiteralRational>() {

    override fun visit(ctx: KerMLParser.LiteralRealContext, kermlParseContext: KermlParseContext): LiteralRational {
        val literal = kermlParseContext.create<LiteralRational>()

        ctx.value?.let { realValue ->
            literal.value = realValue.text.toDoubleOrNull() ?: 0.0
        }

        // Create membership with parent
        createFeatureMembership(literal, kermlParseContext)

        return literal
    }
}

/**
 * Visitor for LiteralInfinity elements.
 *
 * Grammar:
 * ```
 * literalInfinity
 *     : '*'
 *     ;
 * ```
 *
 * LiteralInfinity represents unbounded/unlimited multiplicity.
 */
class LiteralInfinityVisitor : BaseFeatureVisitor<KerMLParser.LiteralInfinityContext, LiteralInfinity>() {

    override fun visit(ctx: KerMLParser.LiteralInfinityContext, kermlParseContext: KermlParseContext): LiteralInfinity {
        val literal = kermlParseContext.create<LiteralInfinity>()

        // Create membership with parent
        createFeatureMembership(literal, kermlParseContext)

        return literal
    }
}
