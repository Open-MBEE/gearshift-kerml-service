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

import org.openmbee.gearshift.generated.interfaces.Conjugation
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseRelationshipVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.registerReference

/**
 * Visitor for Conjugation elements.
 *
 * Per KerML spec 8.2.3.3: Conjugation is a relationship where a conjugated type
 * has its feature directions reversed from its original type.
 *
 * Grammar:
 * ```
 * conjugation
 *     : ( CONJUGATION identification )?
 *       CONJUGATE
 *       ( qualifiedName
 *       | featureChain
 *       )
 *       conjugatesToken
 *       ( qualifiedName
 *       | featureChain
 *       )
 *       relationshipBody
 *     ;
 * ```
 *
 * Conjugation extends Relationship.
 */
class ConjugationVisitor : BaseRelationshipVisitor<KerMLParser.ConjugationContext, Conjugation>() {

    override fun visit(ctx: KerMLParser.ConjugationContext, kermlParseContext: KermlParseContext): Conjugation {
        val conjugation = kermlParseContext.create<Conjugation>()

        // Parse identification (optional)
        parseIdentification(ctx.identification(), conjugation)

        // Parse conjugated type reference (first qualifiedName or featureChain)
        val qualifiedNames = ctx.qualifiedName()
        val featureChains = ctx.featureChain()

        // First reference is the conjugated type
        if (qualifiedNames.size >= 1) {
            val conjugatedTypeName = extractQualifiedName(qualifiedNames[0])
            kermlParseContext.registerReference(conjugation, "conjugatedType", conjugatedTypeName)
        } else if (featureChains.size >= 1) {
            // Feature chain references require special handling
            parseFeatureChain(featureChains[0])
        }

        // Second reference is the original type
        if (qualifiedNames.size >= 2) {
            val originalTypeName = extractQualifiedName(qualifiedNames[1])
            kermlParseContext.registerReference(conjugation, "originalType", originalTypeName)
        } else if (featureChains.size >= 2) {
            // Feature chain references require special handling
            parseFeatureChain(featureChains[1])
        }

        // Create membership with parent namespace (inherited from BaseRelationshipVisitor)
        createRelationshipMembership(conjugation, kermlParseContext)

        // Parse relationship body (inherited from BaseRelationshipVisitor)
        ctx.relationshipBody()?.let { body ->
            val childContext = kermlParseContext.withParent(conjugation, conjugation.declaredName ?: "")
            parseRelationshipBody(body, conjugation, childContext)
        }

        return conjugation
    }
}
