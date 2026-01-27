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

import org.openmbee.gearshift.generated.interfaces.Disjoining
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseRelationshipVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext

/**
 * Visitor for Disjoining elements.
 *
 * Per KerML spec 8.2.3.4: Disjoining is a relationship asserting that two types
 * have no common instances.
 *
 * Grammar:
 * ```
 * disjoining
 *     : ( DISJOINING identification )?
 *       DISJOINT
 *       ( qualifiedName
 *       | featureChain
 *       )
 *       disjointToken
 *       ( qualifiedName
 *       | featureChain
 *       )
 *       relationshipBody
 *     ;
 * ```
 *
 * Disjoining extends Relationship.
 */
class DisjoiningVisitor : BaseRelationshipVisitor<KerMLParser.DisjoiningContext, Disjoining>() {

    override fun visit(ctx: KerMLParser.DisjoiningContext, parseContext: ParseContext): Disjoining {
        val disjoining = parseContext.create<Disjoining>()

        // Parse identification (optional)
        parseIdentification(ctx.identification(), disjoining)

        // Parse disjoint types (two qualifiedName or featureChain references)
        val qualifiedNames = ctx.qualifiedName()
        val featureChains = ctx.featureChain()

        // First reference is typeDisjoined
        if (qualifiedNames.size >= 1) {
            val typeName = extractQualifiedName(qualifiedNames[0])
            // TODO: Resolve and set disjoining.typeDisjoined
        } else if (featureChains.size >= 1) {
            val chain = parseFeatureChain(featureChains[0])
            // TODO: Resolve feature chain reference
        }

        // Second reference is disjoiningType
        if (qualifiedNames.size >= 2) {
            val typeName = extractQualifiedName(qualifiedNames[1])
            // TODO: Resolve and set disjoining.disjoiningType
        } else if (featureChains.size >= 2) {
            val chain = parseFeatureChain(featureChains[1])
            // TODO: Resolve feature chain reference
        }

        // Create membership with parent namespace (inherited from BaseRelationshipVisitor)
        createRelationshipMembership(disjoining, parseContext)

        // Parse relationship body (inherited from BaseRelationshipVisitor)
        ctx.relationshipBody()?.let { body ->
            val childContext = parseContext.withParent(disjoining, disjoining.declaredName ?: "")
            parseRelationshipBody(body, disjoining, childContext)
        }

        return disjoining
    }
}
