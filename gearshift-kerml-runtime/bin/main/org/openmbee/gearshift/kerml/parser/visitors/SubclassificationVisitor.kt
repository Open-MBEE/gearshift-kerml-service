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

import org.openmbee.gearshift.generated.interfaces.Subclassification
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseRelationshipVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.registerReference

/**
 * Visitor for Subclassification elements.
 *
 * Per KerML spec 8.2.4.2.2: Subclassification is a Specialization between Classifiers.
 *
 * Grammar:
 * ```
 * subclassification
 *     : ( SPECIALIZATION identification )?
 *       SUBCLASSIFIER subclassifier=qualifiedName
 *       specializesToken superclassifier=qualifiedName
 *       relationshipBody
 *     ;
 * ```
 *
 * Subclassification extends Specialization.
 */
class SubclassificationVisitor : BaseRelationshipVisitor<KerMLParser.SubclassificationContext, Subclassification>() {

    override fun visit(
        ctx: KerMLParser.SubclassificationContext,
        kermlParseContext: KermlParseContext
    ): Subclassification {
        val subclassification = kermlParseContext.create<Subclassification>()

        // Parse identification (optional)
        parseIdentification(ctx.identification(), subclassification)

        // Parse subclassifier reference
        ctx.subclassifier?.let { qn ->
            val subclassifierName = extractQualifiedName(qn)
            kermlParseContext.registerReference(subclassification, "subclassifier", subclassifierName)
        }

        // Parse superclassifier reference
        ctx.superclassifier?.let { qn ->
            val superclassifierName = extractQualifiedName(qn)
            kermlParseContext.registerReference(subclassification, "superclassifier", superclassifierName)
        }

        // Create membership with parent namespace (inherited from BaseRelationshipVisitor)
        createRelationshipMembership(subclassification, kermlParseContext)

        // Parse relationship body (inherited from BaseRelationshipVisitor)
        ctx.relationshipBody()?.let { body ->
            val childContext = kermlParseContext.withParent(subclassification, subclassification.declaredName ?: "")
            parseRelationshipBody(body, subclassification, childContext)
        }

        return subclassification
    }
}
