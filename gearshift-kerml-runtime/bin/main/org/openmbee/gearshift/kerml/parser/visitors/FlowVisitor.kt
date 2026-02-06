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

import org.openmbee.gearshift.generated.interfaces.Flow
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseFeatureVisitor

/**
 * Visitor for Flow elements.
 *
 * Per KerML spec 8.2.5.9.2: Flows are steps that represent transfer of items.
 *
 * Grammar:
 * ```
 * flow
 *     : featurePrefix FLOW
 *       itemFlowDeclaration typeBody
 *     ;
 * ```
 *
 * Note: In KerML, Flow is typically ItemFlow which extends Step and Connector.
 * Using Flow as the closest available interface.
 */
class FlowVisitor : BaseFeatureVisitor<KerMLParser.FlowContext, Flow>() {

    override fun visit(ctx: KerMLParser.FlowContext, kermlParseContext: KermlParseContext): Flow {
        val flow = kermlParseContext.create<Flow>()

        // Parse feature prefix (inherited from BaseFeatureVisitor)
        parseFeaturePrefixContext(ctx.featurePrefix(), flow)

        // Parse item flow declaration - flow-specific
        ctx.itemFlowDeclaration()?.let { decl ->
            parseItemFlowDeclaration(decl, flow, kermlParseContext)
        }

        // Create child context for nested elements
        val childContext = kermlParseContext.withParent(flow, flow.declaredName ?: "")

        // Create membership with parent type (inherited from BaseTypeVisitor)
        createFeatureMembership(flow, kermlParseContext)

        // Parse type body (inherited from BaseTypeVisitor)
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, childContext)
        }

        return flow
    }

    /**
     * Parse item flow declaration - flow-specific.
     */
    private fun parseItemFlowDeclaration(
        ctx: KerMLParser.ItemFlowDeclarationContext,
        flow: Flow,
        kermlParseContext: KermlParseContext
    ) {
        // Parse feature declaration if present (inherited)
        ctx.featureDeclaration()?.let { decl ->
            parseFeatureDeclaration(decl, flow, kermlParseContext)
        }

        // Parse value part (inherited)
        parseValuePart(ctx.valuePart(), flow, kermlParseContext)

        // Parse item flow end members (from -> to)
        ctx.flowEndMember()?.forEach { _ ->
            // TODO: Parse flow end members
        }
    }
}
