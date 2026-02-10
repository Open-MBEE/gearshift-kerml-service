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

import org.openmbee.gearshift.generated.interfaces.ResultExpressionMembership
import org.openmbee.gearshift.generated.interfaces.Viewpoint
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseClassifierVisitor

/**
 * Visitor for Viewpoint elements.
 *
 * A Viewpoint is a Predicate that defines the concerns and constraints
 * a View must satisfy for its stakeholders.
 *
 * Grammar:
 * ```
 * viewpoint
 *     : typePrefix VIEWPOINT
 *       classifierDeclaration functionBody
 *     ;
 * ```
 *
 * Viewpoint extends Predicate. Uses functionBody like Predicate/Function.
 */
class ViewpointVisitor : BaseClassifierVisitor<KerMLParser.ViewpointContext, Viewpoint>() {

    override fun visit(ctx: KerMLParser.ViewpointContext, kermlParseContext: KermlParseContext): Viewpoint {
        val viewpoint = kermlParseContext.create<Viewpoint>()

        // Parse typePrefix (inherited from BaseTypeVisitor)
        parseTypePrefix(ctx.typePrefix(), viewpoint)

        // Parse classifierDeclaration (inherited from BaseClassifierVisitor)
        ctx.classifierDeclaration()?.let { decl ->
            parseClassifierDeclaration(decl, viewpoint, kermlParseContext)
        }

        // Create child context for nested elements
        val childContext = kermlParseContext.withParent(viewpoint, viewpoint.declaredName ?: "")

        // Create ownership relationship with parent namespace
        createOwnershipMembership(viewpoint, kermlParseContext)

        // Parse function body (viewpoint uses functionBody like Predicate)
        ctx.functionBody()?.let { body ->
            parseFunctionBody(body, childContext)
        }

        return viewpoint
    }

    private fun parseFunctionBody(
        ctx: KerMLParser.FunctionBodyContext,
        kermlParseContext: KermlParseContext
    ) {
        ctx.functionBodyPart()?.let { bodyPart ->
            // Parse type body elements (reuse inherited parseTypeBodyElement)
            bodyPart.typeBodyElement()?.forEach { bodyElement ->
                parseTypeBodyElement(bodyElement, kermlParseContext)
            }

            // Parse return feature members
            bodyPart.returnFeatureMember()?.forEach { returnMember ->
                returnMember.featureElement()?.let { fe ->
                    parseFeatureElement(fe, kermlParseContext)
                }
            }

            // Parse result expression member
            bodyPart.resultExpressionMember()?.let { remCtx ->
                val membership = kermlParseContext.create<ResultExpressionMembership>()
                remCtx.ownedExpression()?.let { ownedExprCtx ->
                    val resultExpr = OwnedExpressionVisitor().visit(ownedExprCtx, kermlParseContext)
                    membership.ownedMemberElement = resultExpr
                    (kermlParseContext.parent as? org.openmbee.mdm.framework.runtime.MDMObject)
                        ?.setProperty("resultExpression", resultExpr)
                }
            }
        }
    }
}
