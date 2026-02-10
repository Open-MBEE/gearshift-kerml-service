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

import org.openmbee.gearshift.generated.interfaces.Rendering
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseClassifierVisitor

/**
 * Visitor for Rendering elements.
 *
 * A Rendering is a Structure that defines how a View is rendered
 * into a presentable format.
 *
 * Grammar:
 * ```
 * rendering
 *     : typePrefix RENDERING
 *       classifierDeclaration typeBody
 *     ;
 * ```
 *
 * Rendering extends Structure. Uses inherited parsing from BaseClassifierVisitor.
 */
class RenderingVisitor : BaseClassifierVisitor<KerMLParser.RenderingContext, Rendering>() {

    override fun visit(ctx: KerMLParser.RenderingContext, kermlParseContext: KermlParseContext): Rendering {
        val rendering = kermlParseContext.create<Rendering>()

        // Parse typePrefix (inherited from BaseTypeVisitor)
        parseTypePrefix(ctx.typePrefix(), rendering)

        // Parse classifierDeclaration (inherited from BaseClassifierVisitor)
        ctx.classifierDeclaration()?.let { decl ->
            parseClassifierDeclaration(decl, rendering, kermlParseContext)
        }

        // Create child context for nested elements
        val childContext = kermlParseContext.withParent(rendering, rendering.declaredName ?: "")

        // Create ownership relationship with parent namespace
        createOwnershipMembership(rendering, kermlParseContext)

        // Parse type body (inherited from BaseTypeVisitor)
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, childContext)
        }

        return rendering
    }
}
