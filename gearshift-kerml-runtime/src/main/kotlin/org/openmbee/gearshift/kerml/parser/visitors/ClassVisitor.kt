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
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseClassifierVisitor
import org.openmbee.gearshift.generated.interfaces.Class as KerMLClass

/**
 * Visitor for Class elements.
 *
 * Per KerML spec 8.2.5.2: Classes are classifiers that represent object types with identity.
 *
 * Grammar:
 * ```
 * class
 *     : typePrefix CLASS
 *       classifierDeclaration typeBody
 *     ;
 * ```
 *
 * Class extends Classifier. Uses inherited parsing from BaseClassifierVisitor.
 */
class ClassVisitor : BaseClassifierVisitor<KerMLParser.ClassContext, KerMLClass>() {

    override fun visit(ctx: KerMLParser.ClassContext, kermlParseContext: KermlParseContext): KerMLClass {
        // Extract name from declaration first (needed for ownership in secondary constructor)
        val declaredName = ctx.classifierDeclaration()?.identification()?.declaredName?.text

        // Create class with parent and name - secondary constructor handles ownership
        val cls = kermlParseContext.create<KerMLClass>(declaredName = declaredName)

        // Parse typePrefix (inherited from BaseTypeVisitor)
        parseTypePrefix(ctx.typePrefix(), cls)

        // Parse classifierDeclaration (inherited from BaseClassifierVisitor)
        ctx.classifierDeclaration()?.let { decl ->
            parseClassifierDeclaration(decl, cls, kermlParseContext)
        }

        // Create child context for nested elements
        val childContext = kermlParseContext.withParent(cls, cls.declaredName ?: "")

        // Parse type body (inherited from BaseTypeVisitor)
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, childContext)
        }

        return cls
    }
}
