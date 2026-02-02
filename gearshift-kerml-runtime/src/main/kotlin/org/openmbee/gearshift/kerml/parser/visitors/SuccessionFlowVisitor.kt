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

import org.openmbee.gearshift.generated.interfaces.SuccessionFlow
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseFeatureVisitor

/**
 * Visitor for SuccessionFlow elements.
 *
 * Per KerML spec 8.2.5.9.2: SuccessionFlows combine succession with item flow.
 *
 * Grammar:
 * ```
 * successionFlow
 *     : featurePrefix SUCCESSION FLOW
 *       itemFlowDeclaration typeBody
 *     ;
 * ```
 *
 * SuccessionFlow extends both Succession and ItemFlow.
 */
class SuccessionFlowVisitor : BaseFeatureVisitor<KerMLParser.SuccessionFlowContext, SuccessionFlow>() {

    override fun visit(ctx: KerMLParser.SuccessionFlowContext, kermlParseContext: KermlParseContext): SuccessionFlow {
        val succFlow = kermlParseContext.create<SuccessionFlow>()

        // Parse feature prefix (inherited from BaseFeatureVisitor)
        parseFeaturePrefixContext(ctx.featurePrefix(), succFlow)

        // Parse item flow declaration - flow-specific
        ctx.itemFlowDeclaration()?.let { decl ->
            parseItemFlowDeclaration(decl, succFlow, kermlParseContext)
        }

        // Create child context for nested elements
        val childContext = kermlParseContext.withParent(succFlow, succFlow.declaredName ?: "")

        // Create membership with parent type (inherited from BaseTypeVisitor)
        createFeatureMembership(succFlow, kermlParseContext)

        // Parse type body (inherited from BaseTypeVisitor)
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, childContext)
        }

        return succFlow
    }

    /**
     * Parse item flow declaration - flow-specific.
     */
    private fun parseItemFlowDeclaration(
        ctx: KerMLParser.ItemFlowDeclarationContext,
        succFlow: SuccessionFlow,
        kermlParseContext: KermlParseContext
    ) {
        // Parse feature declaration if present (inherited)
        ctx.featureDeclaration()?.let { decl ->
            parseFeatureDeclaration(decl, succFlow, kermlParseContext)
        }

        // Parse value part (inherited)
        parseValuePart(ctx.valuePart(), succFlow, kermlParseContext)

        // Parse item flow end members
        ctx.flowEndMember()?.forEach { _ ->
            // TODO: Parse flow end members
        }
    }
}
