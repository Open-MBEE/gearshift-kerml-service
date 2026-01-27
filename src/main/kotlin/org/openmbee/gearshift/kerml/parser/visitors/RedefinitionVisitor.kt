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

import org.openmbee.gearshift.generated.interfaces.Redefinition
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseRelationshipVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.registerReference

/**
 * Visitor for Redefinition elements.
 *
 * Per KerML spec 8.2.5.4.3: Redefinition is a Subsetting where the redefining feature
 * completely replaces the redefined feature in the context of its owning type.
 *
 * Grammar:
 * ```
 * redefinition
 *     : ( SPECIALIZATION identification )?
 *       REDEFINITION specificType
 *       redefinesToken generalType
 *       relationshipBody
 *     ;
 * ```
 *
 * Redefinition extends Subsetting.
 */
class RedefinitionVisitor : BaseRelationshipVisitor<KerMLParser.RedefinitionContext, Redefinition>() {

    override fun visit(ctx: KerMLParser.RedefinitionContext, parseContext: ParseContext): Redefinition {
        val redefinition = parseContext.create<Redefinition>()

        // Parse identification (optional)
        parseIdentification(ctx.identification(), redefinition)

        // Parse redefining feature (specificType)
        ctx.specificType()?.qualifiedName()?.let { qn ->
            val redefiningFeatureName = extractQualifiedName(qn)
            parseContext.registerReference(redefinition, "redefiningFeature", redefiningFeatureName)
        }

        // Parse redefined feature (generalType)
        ctx.generalType()?.qualifiedName()?.let { qn ->
            val redefinedFeatureName = extractQualifiedName(qn)
            parseContext.registerReference(redefinition, "redefinedFeature", redefinedFeatureName, isRedefinitionContext = true)
        }

        // Create membership with parent namespace (inherited from BaseRelationshipVisitor)
        createRelationshipMembership(redefinition, parseContext)

        // Parse relationship body (inherited from BaseRelationshipVisitor)
        ctx.relationshipBody()?.let { body ->
            val childContext = parseContext.withParent(redefinition, redefinition.declaredName ?: "")
            parseRelationshipBody(body, redefinition, childContext)
        }

        return redefinition
    }
}
