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

import org.openmbee.gearshift.generated.interfaces.Structure
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseClassifierVisitor

/**
 * Visitor for Structure elements.
 *
 * Per KerML spec 8.2.5.3: Structures are classes that represent composite value types.
 *
 * Grammar:
 * ```
 * structure
 *     : typePrefix STRUCT
 *       classifierDeclaration typeBody
 *     ;
 * ```
 *
 * Structure extends Class. Uses inherited parsing from BaseClassifierVisitor.
 */
class StructureVisitor : BaseClassifierVisitor<KerMLParser.StructureContext, Structure>() {

    override fun visit(ctx: KerMLParser.StructureContext, kermlParseContext: KermlParseContext): Structure {
        val structure = kermlParseContext.create<Structure>()

        // Parse typePrefix (inherited from BaseTypeVisitor)
        parseTypePrefix(ctx.typePrefix(), structure)

        // Parse classifierDeclaration (inherited from BaseClassifierVisitor)
        ctx.classifierDeclaration()?.let { decl ->
            parseClassifierDeclaration(decl, structure, kermlParseContext)
        }

        // Create child context for nested elements
        val childContext = kermlParseContext.withParent(structure, structure.declaredName ?: "")

        // Create ownership relationship with parent namespace
        createOwnershipMembership(structure, kermlParseContext)

        // Parse type body (inherited from BaseTypeVisitor)
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, childContext)
        }

        return structure
    }
}
