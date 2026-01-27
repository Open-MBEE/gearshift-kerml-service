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

import org.openmbee.gearshift.generated.interfaces.Classifier
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseClassifierVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext

/**
 * Visitor for generic Classifier elements.
 *
 * Per KerML spec 8.2.4.2: Classifiers are types whose instances can be classified.
 *
 * Grammar:
 * ```
 * classifier
 *     : typePrefix CLASSIFIER
 *       classifierDeclaration typeBody
 *     ;
 * ```
 *
 * Classifier extends Type and is the base for Class, DataType, Association, etc.
 */
class ClassifierVisitor : BaseClassifierVisitor<KerMLParser.ClassifierContext, Classifier>() {

    override fun visit(ctx: KerMLParser.ClassifierContext, parseContext: ParseContext): Classifier {
        val classifier = parseContext.create<Classifier>()

        // Parse typePrefix (inherited from BaseTypeVisitor)
        parseTypePrefix(ctx.typePrefix(), classifier)

        // Parse classifierDeclaration (inherited from BaseClassifierVisitor)
        ctx.classifierDeclaration()?.let { decl ->
            parseClassifierDeclaration(decl, classifier, parseContext)
        }

        // Create child context for nested elements
        val childContext = parseContext.withParent(classifier, classifier.declaredName ?: "")

        // Create ownership relationship with parent namespace
        createOwnershipMembership(classifier, parseContext)

        // Parse type body (inherited from BaseTypeVisitor)
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, childContext)
        }

        return classifier
    }
}
