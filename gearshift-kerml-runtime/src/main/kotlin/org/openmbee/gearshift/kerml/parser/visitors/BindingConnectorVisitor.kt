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

import org.openmbee.gearshift.generated.interfaces.BindingConnector
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseFeatureVisitor

/**
 * Visitor for BindingConnector elements.
 *
 * Per KerML spec 8.2.5.5.2: BindingConnectors assert equality between connected features.
 *
 * Grammar:
 * ```
 * bindingConnector
 *     : featurePrefix BINDING
 *       bindingConnectorDeclaration typeBody
 *     ;
 * ```
 *
 * BindingConnector extends Connector. Uses inherited parsing from BaseFeatureVisitor.
 */
class BindingConnectorVisitor : BaseFeatureVisitor<KerMLParser.BindingConnectorContext, BindingConnector>() {

    override fun visit(
        ctx: KerMLParser.BindingConnectorContext,
        kermlParseContext: KermlParseContext
    ): BindingConnector {
        val binding = kermlParseContext.create<BindingConnector>()

        // Parse feature prefix (inherited from BaseFeatureVisitor)
        parseFeaturePrefixContext(ctx.featurePrefix(), binding)

        // Create child context so connector ends are owned by the binding connector
        val childContext = kermlParseContext.withParent(binding, binding.declaredName ?: "")

        // Parse binding connector declaration - binding-specific
        ctx.bindingConnectorDeclaration()?.let { decl ->
            parseBindingConnectorDeclaration(decl, binding, childContext)
        }

        // Create membership with parent type (inherited from BaseTypeVisitor)
        createFeatureMembership(binding, kermlParseContext)

        // Parse type body (inherited from BaseTypeVisitor)
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, childContext)
        }

        return binding
    }

    /**
     * Parse binding connector declaration - binding-specific.
     */
    private fun parseBindingConnectorDeclaration(
        ctx: KerMLParser.BindingConnectorDeclarationContext,
        binding: BindingConnector,
        kermlParseContext: KermlParseContext
    ) {
        ctx.featureDeclaration()?.let { decl ->
            parseFeatureDeclaration(decl, binding, kermlParseContext)
        }

        ctx.isSufficient?.let { binding.isSufficient = true }

        ctx.connectorEndMember()?.forEach { endMember ->
            parseConnectorEndMember(endMember, kermlParseContext)
        }
    }
}
