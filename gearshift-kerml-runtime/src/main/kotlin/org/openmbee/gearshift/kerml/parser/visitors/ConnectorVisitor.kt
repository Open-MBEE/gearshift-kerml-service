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

import org.openmbee.gearshift.generated.interfaces.Connector
import org.openmbee.gearshift.generated.interfaces.EndFeatureMembership
import org.openmbee.gearshift.generated.interfaces.Feature
import org.openmbee.gearshift.generated.interfaces.Subsetting
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseFeatureVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.registerReference

/**
 * Visitor for Connector elements.
 *
 * Per KerML spec 8.2.5.5: Connectors are features that represent links between features.
 *
 * Grammar:
 * ```
 * connector
 *     : featurePrefix CONNECTOR
 *       ( featureDeclaration? valuePart?
 *       | connectorDeclaration
 *       )
 *       typeBody
 *     ;
 * ```
 *
 * Connector extends Feature and Association. Uses inherited parsing from BaseFeatureVisitor.
 */
class ConnectorVisitor : BaseFeatureVisitor<KerMLParser.ConnectorContext, Connector>() {

    override fun visit(ctx: KerMLParser.ConnectorContext, kermlParseContext: KermlParseContext): Connector {
        val connector = kermlParseContext.create<Connector>()

        // Parse feature prefix (inherited from BaseFeatureVisitor)
        parseFeaturePrefixContext(ctx.featurePrefix(), connector)

        // Parse feature declaration (if present without connector declaration)
        ctx.featureDeclaration()?.let { decl ->
            parseFeatureDeclaration(decl, connector, kermlParseContext)
        }

        // Parse connector declaration (binary or n-ary) - connector-specific
        ctx.connectorDeclaration()?.let { connDecl ->
            parseConnectorDeclaration(connDecl, connector, kermlParseContext)
        }

        // Create child context for nested elements
        val childContext = kermlParseContext.withParent(connector, connector.declaredName ?: "")

        // Create membership with parent type (inherited from BaseTypeVisitor)
        createFeatureMembership(connector, kermlParseContext)

        // Parse value part (inherited from BaseFeatureVisitor)
        parseValuePart(ctx.valuePart(), connector, childContext)

        // Parse type body (inherited from BaseTypeVisitor)
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, childContext)
        }

        return connector
    }

    /**
     * Parse connector declaration (binary or n-ary) - connector-specific.
     */
    private fun parseConnectorDeclaration(
        ctx: KerMLParser.ConnectorDeclarationContext,
        connector: Connector,
        kermlParseContext: KermlParseContext
    ) {
        ctx.binaryConnectorDeclaration()?.let { binaryDecl ->
            parseBinaryConnectorDeclaration(binaryDecl, connector, kermlParseContext)
        }

        ctx.naryConnectorDeclaration()?.let { naryDecl ->
            parseNaryConnectorDeclaration(naryDecl, connector, kermlParseContext)
        }
    }

    /**
     * Parse binary connector declaration.
     */
    private fun parseBinaryConnectorDeclaration(
        ctx: KerMLParser.BinaryConnectorDeclarationContext,
        connector: Connector,
        kermlParseContext: KermlParseContext
    ) {
        ctx.featureDeclaration()?.let { decl ->
            parseFeatureDeclaration(decl, connector, kermlParseContext)
        }

        ctx.connectorEndMember()?.forEach { endMember ->
            parseConnectorEndMember(endMember, connector, kermlParseContext)
        }
    }

    /**
     * Parse n-ary connector declaration.
     */
    private fun parseNaryConnectorDeclaration(
        ctx: KerMLParser.NaryConnectorDeclarationContext,
        connector: Connector,
        kermlParseContext: KermlParseContext
    ) {
        ctx.featureDeclaration()?.let { decl ->
            parseFeatureDeclaration(decl, connector, kermlParseContext)
        }

        ctx.connectorEndMember()?.forEach { endMember ->
            parseConnectorEndMember(endMember, connector, kermlParseContext)
        }
    }

    /**
     * Parse connector end member.
     */
    private fun parseConnectorEndMember(
        ctx: KerMLParser.ConnectorEndMemberContext,
        connector: Connector,
        kermlParseContext: KermlParseContext
    ) {
        val endMembership = kermlParseContext.create<EndFeatureMembership>()
        val endFeature = kermlParseContext.create<Feature>()
        endFeature.isEnd = true
        endMembership.memberElement = endFeature

        ctx.connectorEnd()?.let { connEnd ->
            connEnd.declaredName?.let { endFeature.declaredName = it.text }
            connEnd.ownedReferenceSubsetting()?.let { refSubsetting ->
                val subsetting = kermlParseContext.create<Subsetting>()
                subsetting.subsettingFeature = endFeature

                // Establish ownership - link Subsetting as owned by the Feature
                kermlParseContext.engine.link(
                    sourceId = endFeature.id!!,
                    targetId = subsetting.id!!,
                    associationName = "owningFeatureOwnedSubsettingAssociation"
                )
                kermlParseContext.engine.link(
                    sourceId = endFeature.id!!,
                    targetId = subsetting.id!!,
                    associationName = "owningTypeOwnedSpecializationAssociation"
                )
                kermlParseContext.engine.link(
                    sourceId = endFeature.id!!,
                    targetId = subsetting.id!!,
                    associationName = "owningRelatedElementOwnedRelationshipAssociation"
                )

                refSubsetting.generalType()?.qualifiedName()?.let { qn ->
                    val name = extractQualifiedName(qn)
                    kermlParseContext.registerReference(subsetting, "subsettedFeature", name)
                }
            }
        }
    }
}
