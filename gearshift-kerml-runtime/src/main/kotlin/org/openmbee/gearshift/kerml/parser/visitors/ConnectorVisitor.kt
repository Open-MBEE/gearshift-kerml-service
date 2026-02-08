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
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseFeatureVisitor

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

        // Create child context so connector ends and body elements are owned by the connector
        val childContext = kermlParseContext.withParent(connector, connector.declaredName ?: "")

        // Parse connector declaration (binary or n-ary) with child context
        // so EndFeatureMemberships become children of the connector
        ctx.connectorDeclaration()?.let { connDecl ->
            parseConnectorDeclaration(connDecl, connector, childContext)
        }

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
            parseConnectorEndMember(endMember, kermlParseContext)
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
            parseConnectorEndMember(endMember, kermlParseContext)
        }
    }
}
