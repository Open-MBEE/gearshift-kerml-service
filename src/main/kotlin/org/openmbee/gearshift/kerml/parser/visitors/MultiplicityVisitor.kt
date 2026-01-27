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

import org.openmbee.gearshift.generated.interfaces.LiteralInfinity
import org.openmbee.gearshift.generated.interfaces.LiteralInteger
import org.openmbee.gearshift.generated.interfaces.MultiplicityRange
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseTypeVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext

/**
 * Visitor for Multiplicity elements.
 *
 * Grammar:
 * ```
 * multiplicity
 *     : multiplicitySubset
 *     | multiplicityRange
 *     ;
 *
 * multiplicityRange
 *     : 'multiplicity' identification multiplicityBounds typeBody
 *     ;
 *
 * multiplicityBounds
 *     : '[' ( multiplicityExpressionMember '..' )? multiplicityExpressionMember ']'
 *     ;
 * ```
 */
class MultiplicityVisitor : BaseTypeVisitor<KerMLParser.MultiplicityContext, MultiplicityRange>() {

    override fun visit(ctx: KerMLParser.MultiplicityContext, parseContext: ParseContext): MultiplicityRange {
        // Dispatch to specific multiplicity type
        ctx.multiplicitySubset()?.let { subset ->
            return parseMultiplicitySubset(subset, parseContext)
        }

        ctx.multiplicityRange()?.let { range ->
            return parseMultiplicityRange(range, parseContext)
        }

        // Fallback
        return parseContext.create<MultiplicityRange>()
    }

    /**
     * Parse a MultiplicitySubset element.
     *
     * Grammar:
     * multiplicitySubset
     *     : 'multiplicity' identification subsets typeBody
     *     ;
     */
    private fun parseMultiplicitySubset(
        ctx: KerMLParser.MultiplicitySubsetContext,
        parseContext: ParseContext
    ): MultiplicityRange {
        val mult = parseContext.create<MultiplicityRange>()

        // Parse identification
        ctx.identification()?.let { id ->
            parseIdentification(id, mult)
        }

        // Create child context for nested elements
        val childContext = parseContext.withParent(mult, mult.declaredName ?: "")

        // Create ownership membership with parent namespace
        createOwnershipMembership(mult, parseContext)

        // Parse type body
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, childContext)
        }

        return mult
    }

    /**
     * Parse a MultiplicityRange element.
     *
     * Grammar:
     * multiplicityRange
     *     : 'multiplicity' identification multiplicityBounds typeBody
     *     ;
     */
    private fun parseMultiplicityRange(
        ctx: KerMLParser.MultiplicityRangeContext,
        parseContext: ParseContext
    ): MultiplicityRange {
        val mult = parseContext.create<MultiplicityRange>()

        // Parse identification
        ctx.identification()?.let { id ->
            parseIdentification(id, mult)
        }

        // Create child context for nested elements
        val childContext = parseContext.withParent(mult, mult.declaredName ?: "")

        // Create ownership membership with parent namespace
        createOwnershipMembership(mult, parseContext)

        // Parse multiplicity bounds
        ctx.multiplicityBounds()?.let { bounds ->
            parseMultiplicityBounds(bounds, mult, childContext)
        }

        // Parse type body
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, childContext)
        }

        return mult
    }

    /**
     * Parse multiplicity bounds to extract lower and upper bound values.
     *
     * Grammar:
     * multiplicityBounds
     *     : '[' ( multiplicityExpressionMember '..' )? multiplicityExpressionMember ']'
     *     ;
     */
    private fun parseMultiplicityBounds(
        ctx: KerMLParser.MultiplicityBoundsContext,
        mult: MultiplicityRange,
        parseContext: ParseContext
    ) {
        val expressions = ctx.multiplicityExpressionMember()

        when (expressions.size) {
            1 -> {
                // Single bound: [n] means exactly n (lower = upper = n)
                val expr = parseMultiplicityExpression(expressions[0], parseContext)
                // Both bounds are the same
            }
            2 -> {
                // Range: [lower..upper]
                val lowerExpr = parseMultiplicityExpression(expressions[0], parseContext)
                val upperExpr = parseMultiplicityExpression(expressions[1], parseContext)
            }
        }
    }

    /**
     * Parse a multiplicity expression member.
     *
     * Grammar:
     * multiplicityExpressionMember
     *     : literalExpression | featureReferenceExpression
     *     ;
     */
    private fun parseMultiplicityExpression(
        ctx: KerMLParser.MultiplicityExpressionMemberContext,
        parseContext: ParseContext
    ): Any? {
        ctx.literalExpression()?.let { literal ->
            return parseLiteralForMultiplicity(literal, parseContext)
        }

        ctx.featureReferenceExpression()?.let { ref ->
            // Feature reference - e.g., referencing another multiplicity
            return FeatureReferenceExpressionVisitor().visit(ref, parseContext)
        }

        return null
    }

    /**
     * Parse a literal expression for multiplicity bounds.
     */
    private fun parseLiteralForMultiplicity(
        ctx: KerMLParser.LiteralExpressionContext,
        parseContext: ParseContext
    ): Any? {
        ctx.literalInteger()?.let { intLit ->
            val literal = parseContext.create<LiteralInteger>()
            intLit.value?.let { value ->
                literal.value = value.text.toIntOrNull() ?: 0
            }
            createFeatureMembership(literal, parseContext)
            return literal
        }

        ctx.literalInfinity()?.let { _ ->
            val literal = parseContext.create<LiteralInfinity>()
            createFeatureMembership(literal, parseContext)
            return literal
        }

        return null
    }
}
