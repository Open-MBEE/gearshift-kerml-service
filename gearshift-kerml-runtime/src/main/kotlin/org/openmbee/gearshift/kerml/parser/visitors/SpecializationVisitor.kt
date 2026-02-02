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

import org.openmbee.gearshift.generated.interfaces.Specialization
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseRelationshipVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.registerReference

/**
 * Visitor for Specialization elements.
 *
 * Per KerML spec 8.2.3.2: Specialization is a relationship between two Types
 * where the specific type inherits from the general type.
 *
 * Grammar:
 * ```
 * specialization
 *     : ( SPECIALIZATION identification )?
 *       SUBTYPE specificType
 *       specializesToken generalType
 *       relationshipBody
 *     ;
 * ```
 *
 * Specialization extends Relationship.
 */
class SpecializationVisitor : BaseRelationshipVisitor<KerMLParser.SpecializationContext, Specialization>() {

    override fun visit(ctx: KerMLParser.SpecializationContext, kermlParseContext: KermlParseContext): Specialization {
        val specialization = kermlParseContext.create<Specialization>()

        // Parse identification (optional)
        parseIdentification(ctx.identification(), specialization)

        // Parse specific type reference
        ctx.specificType()?.qualifiedName()?.let { qn ->
            val specificTypeName = extractQualifiedName(qn)
            kermlParseContext.registerReference(specialization, "specific", specificTypeName)
        }

        // Parse general type reference
        ctx.generalType()?.qualifiedName()?.let { qn ->
            val generalTypeName = extractQualifiedName(qn)
            kermlParseContext.registerReference(specialization, "general", generalTypeName)
        }

        // Create membership with parent namespace (inherited from BaseRelationshipVisitor)
        createRelationshipMembership(specialization, kermlParseContext)

        // Parse relationship body (inherited from BaseRelationshipVisitor)
        ctx.relationshipBody()?.let { body ->
            val childContext = kermlParseContext.withParent(specialization, specialization.declaredName ?: "")
            parseRelationshipBody(body, specialization, childContext)
        }

        return specialization
    }
}
