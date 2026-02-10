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

import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.generated.interfaces.ViewRenderingMembership
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseRelationshipVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.registerReference

/**
 * Visitor for ViewRenderingMember elements.
 *
 * A ViewRenderingMember creates a ViewRenderingMembership that
 * identifies the Rendering used by a View.
 *
 * Grammar:
 * ```
 * viewRenderingMember
 *     : memberPrefix RENDER
 *       qualifiedName typeBody
 *     ;
 * ```
 *
 * ViewRenderingMembership extends FeatureMembership.
 */
class ViewRenderingMemberVisitor : BaseRelationshipVisitor<KerMLParser.ViewRenderingMemberContext, ViewRenderingMembership>() {

    override fun visit(ctx: KerMLParser.ViewRenderingMemberContext, kermlParseContext: KermlParseContext): ViewRenderingMembership {
        val membership = kermlParseContext.create<ViewRenderingMembership>()

        // Parse visibility from memberPrefix
        val visibility = parseMemberPrefix(ctx.memberPrefix())
        membership.visibility = visibility

        // Parse the rendering reference
        ctx.qualifiedName()?.let { qn ->
            val renderingName = extractQualifiedName(qn)
            kermlParseContext.registerReference(membership, "ownedRendering", renderingName)
        }

        // Set owning namespace
        kermlParseContext.parent?.let { parent ->
            if (parent is Namespace) {
                membership.membershipOwningNamespace = parent
            }
        }

        return membership
    }
}
