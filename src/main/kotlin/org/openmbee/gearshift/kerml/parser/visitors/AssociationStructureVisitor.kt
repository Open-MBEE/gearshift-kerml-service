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

import org.openmbee.gearshift.generated.interfaces.AssociationStructure
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseClassifierVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext

/**
 * Visitor for AssociationStructure elements.
 *
 * Per KerML spec 8.2.5.4: AssociationStructures are both associations and structures,
 * classifying links that are also composite values.
 *
 * Grammar:
 * ```
 * associationStructure
 *     : typePrefix ASSOC STRUCT
 *       classifierDeclaration typeBody
 *     ;
 * ```
 *
 * AssociationStructure extends both Association and Structure. Uses inherited parsing from BaseClassifierVisitor.
 */
class AssociationStructureVisitor : BaseClassifierVisitor<KerMLParser.AssociationStructureContext, AssociationStructure>() {

    override fun visit(ctx: KerMLParser.AssociationStructureContext, parseContext: ParseContext): AssociationStructure {
        val assocStruct = parseContext.create<AssociationStructure>()

        // Parse typePrefix (inherited from BaseTypeVisitor)
        parseTypePrefix(ctx.typePrefix(), assocStruct)

        // Parse classifierDeclaration (inherited from BaseClassifierVisitor)
        ctx.classifierDeclaration()?.let { decl ->
            parseClassifierDeclaration(decl, assocStruct, parseContext)
        }

        // Create child context for nested elements
        val childContext = parseContext.withParent(assocStruct, assocStruct.declaredName ?: "")

        // Create ownership relationship with parent namespace
        createOwnershipMembership(assocStruct, parseContext)

        // Parse type body (inherited from BaseTypeVisitor)
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, childContext)
        }

        return assocStruct
    }
}
