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

import org.openmbee.gearshift.generated.interfaces.FeatureTyping
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseRelationshipVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext

/**
 * Visitor for FeatureTyping elements.
 *
 * Per KerML spec 8.2.5.3.2: FeatureTyping is a Specialization between a Feature
 * and its typing Type.
 *
 * Grammar:
 * ```
 * featureTyping
 *     : ( SPECIALIZATION identification )?
 *       TYPING typedFeature=qualifiedName
 *       typedBy generalType
 *       relationshipBody
 *     ;
 * ```
 *
 * FeatureTyping extends Specialization.
 */
class FeatureTypingVisitor : BaseRelationshipVisitor<KerMLParser.FeatureTypingContext, FeatureTyping>() {

    override fun visit(ctx: KerMLParser.FeatureTypingContext, parseContext: ParseContext): FeatureTyping {
        val featureTyping = parseContext.create<FeatureTyping>()

        // Parse identification (optional)
        parseIdentification(ctx.identification(), featureTyping)

        // Parse typedFeature reference
        ctx.typedFeature?.let { qn ->
            val typedFeatureName = extractQualifiedName(qn)
            // TODO: Resolve and set featureTyping.typedFeature
        }

        // Parse general type (the typing type) reference
        ctx.generalType()?.qualifiedName()?.let { qn ->
            val typeName = extractQualifiedName(qn)
            // TODO: Resolve and set featureTyping.type
        }

        // Create membership with parent namespace (inherited from BaseRelationshipVisitor)
        createRelationshipMembership(featureTyping, parseContext)

        // Parse relationship body (inherited from BaseRelationshipVisitor)
        ctx.relationshipBody()?.let { body ->
            val childContext = parseContext.withParent(featureTyping, featureTyping.declaredName ?: "")
            parseRelationshipBody(body, featureTyping, childContext)
        }

        return featureTyping
    }
}
