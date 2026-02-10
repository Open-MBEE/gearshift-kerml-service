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

import org.openmbee.gearshift.generated.interfaces.View
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseClassifierVisitor

/**
 * Visitor for View elements.
 *
 * A View is a Structure that exposes Elements for stakeholders,
 * rendered by a Rendering and constrained by Viewpoints.
 *
 * Grammar:
 * ```
 * view
 *     : typePrefix VIEW
 *       classifierDeclaration viewBody
 *     ;
 *
 * viewBody
 *     : SEMICOLON
 *     | LBRACE viewBodyElement* RBRACE
 *     ;
 *
 * viewBodyElement
 *     : typeBodyElement
 *     | expose
 *     | viewRenderingMember
 *     ;
 * ```
 *
 * View extends Structure. Has a custom body that adds expose and render.
 */
class ViewVisitor : BaseClassifierVisitor<KerMLParser.ViewContext, View>() {

    override fun visit(ctx: KerMLParser.ViewContext, kermlParseContext: KermlParseContext): View {
        val view = kermlParseContext.create<View>()

        // Parse typePrefix (inherited from BaseTypeVisitor)
        parseTypePrefix(ctx.typePrefix(), view)

        // Parse classifierDeclaration (inherited from BaseClassifierVisitor)
        ctx.classifierDeclaration()?.let { decl ->
            parseClassifierDeclaration(decl, view, kermlParseContext)
        }

        // Create child context for nested elements
        val childContext = kermlParseContext.withParent(view, view.declaredName ?: "")

        // Create ownership relationship with parent namespace
        createOwnershipMembership(view, kermlParseContext)

        // Parse view body (custom: typeBodyElement + expose + viewRenderingMember)
        ctx.viewBody()?.let { body ->
            parseViewBody(body, childContext)
        }

        return view
    }

    private fun parseViewBody(
        ctx: KerMLParser.ViewBodyContext,
        kermlParseContext: KermlParseContext
    ) {
        ctx.viewBodyElement()?.forEach { bodyElement ->
            parseViewBodyElement(bodyElement, kermlParseContext)
        }
    }

    private fun parseViewBodyElement(
        ctx: KerMLParser.ViewBodyElementContext,
        kermlParseContext: KermlParseContext
    ) {
        // Expose
        ctx.expose()?.let { exposeCtx ->
            ExposeVisitor().visit(exposeCtx, kermlParseContext)
            return
        }

        // ViewRenderingMember
        ctx.viewRenderingMember()?.let { renderCtx ->
            ViewRenderingMemberVisitor().visit(renderCtx, kermlParseContext)
            return
        }

        // TypeBodyElement (inherited: features, nested types, imports, etc.)
        ctx.typeBodyElement()?.let { typeBodyElem ->
            parseTypeBodyElement(typeBodyElem, kermlParseContext)
        }
    }
}
