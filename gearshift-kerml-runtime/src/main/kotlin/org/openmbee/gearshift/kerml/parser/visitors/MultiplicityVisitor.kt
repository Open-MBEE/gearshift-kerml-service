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
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseTypeVisitor

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

    override fun visit(ctx: KerMLParser.MultiplicityContext, kermlParseContext: KermlParseContext): MultiplicityRange {
        // Dispatch to specific multiplicity type
        ctx.multiplicitySubset()?.let { subset ->
            return parseMultiplicitySubset(subset, kermlParseContext)
        }

        ctx.multiplicityRange()?.let { range ->
            return parseMultiplicityRange(range, kermlParseContext)
        }

        // Fallback
        return kermlParseContext.create<MultiplicityRange>()
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
        kermlParseContext: KermlParseContext
    ): MultiplicityRange {
        val mult = kermlParseContext.create<MultiplicityRange>()

        // Parse identification
        ctx.identification()?.let { id ->
            parseIdentification(id, mult)
        }

        // Create child context for nested elements
        val childContext = kermlParseContext.withParent(mult, mult.declaredName ?: "")

        // Create ownership membership with parent namespace
        createOwnershipMembership(mult, kermlParseContext)

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
        kermlParseContext: KermlParseContext
    ): MultiplicityRange {
        val mult = kermlParseContext.create<MultiplicityRange>()

        // Parse identification
        ctx.identification()?.let { id ->
            parseIdentification(id, mult)
        }

        // Create child context for nested elements
        val childContext = kermlParseContext.withParent(mult, mult.declaredName ?: "")

        // Create ownership membership with parent namespace
        createOwnershipMembership(mult, kermlParseContext)

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
     * Parse an owned multiplicity (used in type/classifier/feature declarations).
     *
     * Grammar: ownedMultiplicity : ownedMultiplicityRange
     *          ownedMultiplicityRange : multiplicityBounds
     */
    fun parseOwnedMultiplicity(
        ctx: KerMLParser.OwnedMultiplicityContext,
        kermlParseContext: KermlParseContext
    ): MultiplicityRange {
        val mult = kermlParseContext.create<MultiplicityRange>()
        val childContext = kermlParseContext.withParent(mult, "")
        ctx.ownedMultiplicityRange()?.multiplicityBounds()?.let { bounds ->
            parseMultiplicityBounds(bounds, mult, childContext)
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
        kermlParseContext: KermlParseContext
    ) {
        val expressions = ctx.multiplicityExpressionMember()

        when (expressions.size) {
            1 -> {
                // Single bound: [n] means exactly n (lower = upper = n)
                val expr = parseMultiplicityExpression(expressions[0], kermlParseContext)
                // Both bounds are the same
            }

            2 -> {
                // Range: [lower..upper]
                val lowerExpr = parseMultiplicityExpression(expressions[0], kermlParseContext)
                val upperExpr = parseMultiplicityExpression(expressions[1], kermlParseContext)
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
        kermlParseContext: KermlParseContext
    ): Any? {
        ctx.literalExpression()?.let { literal ->
            return parseLiteralForMultiplicity(literal, kermlParseContext)
        }

        ctx.featureReferenceExpression()?.let { ref ->
            // Feature reference - e.g., referencing another multiplicity
            return FeatureReferenceExpressionVisitor().visit(ref, kermlParseContext)
        }

        return null
    }

    /**
     * Parse a literal expression for multiplicity bounds.
     */
    private fun parseLiteralForMultiplicity(
        ctx: KerMLParser.LiteralExpressionContext,
        kermlParseContext: KermlParseContext
    ): Any? {
        ctx.literalInteger()?.let { intLit ->
            val literal = kermlParseContext.create<LiteralInteger>()
            intLit.value?.let { value ->
                literal.value = value.text.toIntOrNull() ?: 0
            }
            createFeatureMembership(literal, kermlParseContext)
            return literal
        }

        ctx.literalInfinity()?.let { _ ->
            val literal = kermlParseContext.create<LiteralInfinity>()
            createFeatureMembership(literal, kermlParseContext)
            return literal
        }

        return null
    }
}
