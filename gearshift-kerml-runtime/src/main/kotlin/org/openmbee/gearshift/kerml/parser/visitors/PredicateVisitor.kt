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

import org.openmbee.gearshift.generated.interfaces.Predicate
import org.openmbee.gearshift.generated.interfaces.ResultExpressionMembership
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseClassifierVisitor

/**
 * Visitor for Predicate elements.
 *
 * Per KerML spec 8.2.5.7.3: Predicates are functions that return boolean values.
 *
 * Grammar:
 * ```
 * predicate
 *     : typePrefix PREDICATE
 *       classifierDeclaration functionBody
 *     ;
 * ```
 *
 * Predicate extends Function. Uses inherited parsing from BaseClassifierVisitor
 * with function-specific body parsing.
 */
class PredicateVisitor : BaseClassifierVisitor<KerMLParser.PredicateContext, Predicate>() {

    override fun visit(ctx: KerMLParser.PredicateContext, kermlParseContext: KermlParseContext): Predicate {
        val predicate = kermlParseContext.create<Predicate>()

        // Parse typePrefix (inherited from BaseTypeVisitor)
        parseTypePrefix(ctx.typePrefix(), predicate)

        // Parse classifierDeclaration (inherited from BaseClassifierVisitor)
        ctx.classifierDeclaration()?.let { decl ->
            parseClassifierDeclaration(decl, predicate, kermlParseContext)
        }

        // Create child context for nested elements
        val childContext = kermlParseContext.withParent(predicate, predicate.declaredName ?: "")

        // Create ownership relationship with parent namespace
        createOwnershipMembership(predicate, kermlParseContext)

        // Parse function body (predicate uses functionBody like Function)
        ctx.functionBody()?.let { body ->
            parseFunctionBody(body, childContext)
        }

        return predicate
    }

    /**
     * Parse function body (reuses inherited methods).
     */
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
                    // Store result expression directly for efficient access by analysis services
                    (kermlParseContext.parent as? org.openmbee.mdm.framework.runtime.MDMObject)
                        ?.setProperty("resultExpression", resultExpr)
                }
            }
        }
    }
}
