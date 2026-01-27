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

import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseClassifierVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext
import org.openmbee.gearshift.generated.interfaces.Function as KerMLFunction

/**
 * Visitor for Function elements.
 *
 * Per KerML spec 8.2.5.7: Functions are behaviors that compute result values.
 *
 * Grammar:
 * ```
 * function
 *     : typePrefix FUNCTION
 *       classifierDeclaration functionBody
 *     ;
 *
 * functionBody
 *     : ';' | '{' functionBodyPart '}'
 *     ;
 * ```
 *
 * Function extends Behavior. Uses inherited parsing from BaseClassifierVisitor
 * with additional function-specific body parsing.
 */
class FunctionVisitor : BaseClassifierVisitor<KerMLParser.FunctionContext, KerMLFunction>() {

    override fun visit(ctx: KerMLParser.FunctionContext, parseContext: ParseContext): KerMLFunction {
        val function = parseContext.create<KerMLFunction>()

        // Parse typePrefix (inherited from BaseTypeVisitor)
        parseTypePrefix(ctx.typePrefix(), function)

        // Parse classifierDeclaration (inherited from BaseClassifierVisitor)
        ctx.classifierDeclaration()?.let { decl ->
            parseClassifierDeclaration(decl, function, parseContext)
        }

        // Create child context for nested elements
        val childContext = parseContext.withParent(function, function.declaredName ?: "")

        // Create ownership relationship with parent namespace
        createOwnershipMembership(function, parseContext)

        // Parse function body (function-specific)
        ctx.functionBody()?.let { body ->
            parseFunctionBody(body, childContext)
        }

        return function
    }

    /**
     * Parse function body.
     *
     * functionBody has typeBodyElements plus function-specific parts:
     * - returnFeatureMember
     * - resultExpressionMember
     */
    private fun parseFunctionBody(
        ctx: KerMLParser.FunctionBodyContext,
        parseContext: ParseContext
    ) {
        ctx.functionBodyPart()?.let { bodyPart ->
            // Parse type body elements (reuse inherited parseTypeBodyElement)
            bodyPart.typeBodyElement()?.forEach { bodyElement ->
                parseTypeBodyElement(bodyElement, parseContext)
            }

            // Parse return feature members (function-specific)
            bodyPart.returnFeatureMember()?.forEach { returnMember ->
                returnMember.featureElement()?.let { fe ->
                    parseFeatureElement(fe, parseContext)
                }
            }

            // Parse result expression member (function-specific)
            bodyPart.resultExpressionMember()?.let { _ ->
                // TODO: Parse result expression
            }
        }
    }
}
