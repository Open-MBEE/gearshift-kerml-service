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

import org.openmbee.gearshift.generated.interfaces.FeatureTyping
import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.generated.interfaces.RenderingFeature
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
 * ViewRenderingMembership extends FeatureMembership. The structural model is:
 *   View -[membershipOwningNamespace]-> ViewRenderingMembership
 *   ViewRenderingMembership -[ownedMemberElement]-> RenderingFeature
 *   RenderingFeature -[FeatureTyping.type]-> Rendering (the named definition)
 *
 * The derived properties then resolve as:
 *   ViewRenderingMembership.ownedRendering = ownedMemberFeature->selectByKind(RenderingFeature)->first()
 *   View.rendering = featureMembership->selectByKind(ViewRenderingMembership).ownedRendering->first()
 */
class ViewRenderingMemberVisitor : BaseRelationshipVisitor<KerMLParser.ViewRenderingMemberContext, ViewRenderingMembership>() {

    override fun visit(ctx: KerMLParser.ViewRenderingMemberContext, kermlParseContext: KermlParseContext): ViewRenderingMembership {
        // Create VRM without parent wrapping — it IS the membership, not a wrapped element
        val membership = kermlParseContext.createRelationship<ViewRenderingMembership>()

        // Parse visibility from memberPrefix
        val visibility = parseMemberPrefix(ctx.memberPrefix())
        membership.visibility = visibility

        // Link VRM to its owning View via associations.
        // Using engine.link() (not property setters) to create bidirectional links
        // so the VRM appears in the View's ownedRelationship/ownedMembership/featureMembership.
        kermlParseContext.parent?.let { parent ->
            if (parent is Namespace) {
                kermlParseContext.engine.link(
                    sourceId = parent.id!!,
                    targetId = membership.id!!,
                    associationName = "membershipOwningNamespaceOwnedMembershipAssociation"
                )
                kermlParseContext.engine.link(
                    sourceId = parent.id!!,
                    targetId = membership.id!!,
                    associationName = "owningRelatedElementOwnedRelationshipAssociation"
                )
            }
        }

        // Create a RenderingFeature as the owned member of this VRM.
        // Using createRelationship to skip OwnershipResolver — ownership is
        // established explicitly via the ownedMemberElement link below.
        val renderingFeature = kermlParseContext.createRelationship<RenderingFeature>()

        // Link RenderingFeature as ownedMemberElement of VRM (stored property).
        // This makes it discoverable via ownedMemberFeature->selectByKind(RenderingFeature).
        kermlParseContext.engine.link(
            sourceId = membership.id!!,
            targetId = renderingFeature.id!!,
            associationName = "owningMembershipOwnedMemberElementAssociation"
        )

        // Set up typing: create a FeatureTyping so the RenderingFeature is typed by the Rendering
        ctx.qualifiedName()?.let { qn ->
            val renderingName = extractQualifiedName(qn)
            val featureTyping = kermlParseContext.createRelationship<FeatureTyping>()
            featureTyping.typedFeature = renderingFeature

            // Link FeatureTyping as owned relationship of the RenderingFeature
            kermlParseContext.engine.link(
                sourceId = renderingFeature.id!!,
                targetId = featureTyping.id!!,
                associationName = "owningRelatedElementOwnedRelationshipAssociation"
            )
            kermlParseContext.engine.link(
                sourceId = renderingFeature.id!!,
                targetId = featureTyping.id!!,
                associationName = "owningTypeOwnedSpecializationAssociation"
            )

            // Link FeatureTyping -> RenderingFeature via typing associations
            kermlParseContext.engine.link(
                sourceId = featureTyping.id!!,
                targetId = renderingFeature.id!!,
                associationName = "typingTypedFeatureAssociation"
            )
            kermlParseContext.engine.link(
                sourceId = featureTyping.id!!,
                targetId = renderingFeature.id!!,
                associationName = "specializationSpecificAssociation"
            )

            // Register pending reference: FeatureTyping.type -> the named Rendering
            kermlParseContext.registerReference(featureTyping, "type", renderingName)
        }

        return membership
    }
}
