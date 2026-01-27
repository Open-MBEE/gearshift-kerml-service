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

import org.openmbee.gearshift.generated.interfaces.Metaclass
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseClassifierVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext

/**
 * Visitor for Metaclass elements.
 *
 * Per KerML spec 8.2.3.3.4: Metaclasses are classifiers for metadata features.
 *
 * Grammar:
 * ```
 * metaclass
 *     : typePrefix METACLASS
 *       classifierDeclaration typeBody
 *     ;
 * ```
 *
 * Metaclass extends Structure.
 */
class MetaclassVisitor : BaseClassifierVisitor<KerMLParser.MetaclassContext, Metaclass>() {

    override fun visit(ctx: KerMLParser.MetaclassContext, parseContext: ParseContext): Metaclass {
        val metaclass = parseContext.create<Metaclass>()

        // Parse type prefix (inherited from BaseTypeVisitor)
        parseTypePrefix(ctx.typePrefix(), metaclass)

        // Parse classifier declaration (inherited from BaseClassifierVisitor)
        ctx.classifierDeclaration()?.let { decl ->
            parseClassifierDeclaration(decl, metaclass, parseContext)
        }

        // Create child context for nested elements
        val childContext = parseContext.withParent(metaclass, metaclass.declaredName ?: "")

        // Create ownership membership (inherited from BaseTypeVisitor)
        createOwnershipMembership(metaclass, parseContext)

        // Parse type body (inherited from BaseTypeVisitor)
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, childContext)
        }

        return metaclass
    }
}
