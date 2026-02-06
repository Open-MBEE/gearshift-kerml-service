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

import org.openmbee.gearshift.generated.interfaces.Association
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseClassifierVisitor

/**
 * Visitor for Association elements.
 *
 * Per KerML spec 8.2.5.4: Associations are classifiers that classify links between objects.
 *
 * Grammar:
 * ```
 * association
 *     : typePrefix ASSOC
 *       classifierDeclaration typeBody
 *     ;
 * ```
 *
 * Association extends Classifier and Relationship. Uses inherited parsing from BaseClassifierVisitor.
 */
class AssociationVisitor : BaseClassifierVisitor<KerMLParser.AssociationContext, Association>() {

    override fun visit(ctx: KerMLParser.AssociationContext, kermlParseContext: KermlParseContext): Association {
        val association = kermlParseContext.create<Association>()

        // Parse typePrefix (inherited from BaseTypeVisitor)
        parseTypePrefix(ctx.typePrefix(), association)

        // Parse classifierDeclaration (inherited from BaseClassifierVisitor)
        ctx.classifierDeclaration()?.let { decl ->
            parseClassifierDeclaration(decl, association, kermlParseContext)
        }

        // Create child context for nested elements
        val childContext = kermlParseContext.withParent(association, association.declaredName ?: "")

        // Create ownership relationship with parent namespace
        createOwnershipMembership(association, kermlParseContext)

        // Parse type body (inherited from BaseTypeVisitor)
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, childContext)
        }

        return association
    }
}
