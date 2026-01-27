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

    override fun visit(ctx: KerMLParser.LiteralExpressionContext, parseContext: ParseContext): LiteralExpression {
        // Dispatch to specific literal type
        ctx.literalBoolean()?.let { boolCtx ->
            return LiteralBooleanVisitor().visit(boolCtx, parseContext)
        }

        ctx.literalString()?.let { stringCtx ->
            return LiteralStringVisitor().visit(stringCtx, parseContext)
        }

        ctx.literalInteger()?.let { intCtx ->
            return LiteralIntegerVisitor().visit(intCtx, parseContext)
        }

        ctx.literalReal()?.let { realCtx ->
            return LiteralRealVisitor().visit(realCtx, parseContext)
        }

        ctx.literalInfinity()?.let { infCtx ->
            return LiteralInfinityVisitor().visit(infCtx, parseContext)
        }

        // Fallback - shouldn't reach here
        return parseContext.create<LiteralExpression>()
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

    override fun visit(ctx: KerMLParser.LiteralBooleanContext, parseContext: ParseContext): LiteralBoolean {
        val literal = parseContext.create<LiteralBoolean>()

        ctx.value?.let { boolValue ->
            literal.value = boolValue.TRUE() != null
        }

        // Create membership with parent
        createFeatureMembership(literal, parseContext)

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

    override fun visit(ctx: KerMLParser.LiteralStringContext, parseContext: ParseContext): LiteralString {
        val literal = parseContext.create<LiteralString>()

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
        createFeatureMembership(literal, parseContext)

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

    override fun visit(ctx: KerMLParser.LiteralIntegerContext, parseContext: ParseContext): LiteralInteger {
        val literal = parseContext.create<LiteralInteger>()

        ctx.value?.let { intValue ->
            literal.value = intValue.text.toIntOrNull() ?: 0
        }

        // Create membership with parent
        createFeatureMembership(literal, parseContext)

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

    override fun visit(ctx: KerMLParser.LiteralRealContext, parseContext: ParseContext): LiteralRational {
        val literal = parseContext.create<LiteralRational>()

        ctx.value?.let { realValue ->
            literal.value = realValue.text.toDoubleOrNull() ?: 0.0
        }

        // Create membership with parent
        createFeatureMembership(literal, parseContext)

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

    override fun visit(ctx: KerMLParser.LiteralInfinityContext, parseContext: ParseContext): LiteralInfinity {
        val literal = parseContext.create<LiteralInfinity>()

        // Create membership with parent
        createFeatureMembership(literal, parseContext)

        return literal
    }
}
