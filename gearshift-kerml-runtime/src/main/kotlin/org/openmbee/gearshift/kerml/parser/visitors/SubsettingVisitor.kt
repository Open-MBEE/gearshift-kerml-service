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

import org.openmbee.gearshift.generated.interfaces.Subsetting
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseRelationshipVisitor
import org.openmbee.gearshift.kerml.parser.visitors.base.ParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.registerReference

/**
 * Visitor for Subsetting elements.
 *
 * Per KerML spec 8.2.5.4.2: Subsetting is a Specialization between Features
 * where the subsetting feature's values are a subset of the subsetted feature's values.
 *
 * Grammar:
 * ```
 * subsetting
 *     : ( SPECIALIZATION identification )?
 *       SUBSET specificType
 *       subsetsToken generalType
 *       relationshipBody
 *     ;
 * ```
 *
 * Subsetting extends Specialization.
 */
class SubsettingVisitor : BaseRelationshipVisitor<KerMLParser.SubsettingContext, Subsetting>() {

    override fun visit(ctx: KerMLParser.SubsettingContext, parseContext: ParseContext): Subsetting {
        val subsetting = parseContext.create<Subsetting>()

        // Parse identification (optional)
        parseIdentification(ctx.identification(), subsetting)

        // Parse subsetting feature (specificType)
        ctx.specificType()?.qualifiedName()?.let { qn ->
            val subsettingFeatureName = extractQualifiedName(qn)
            parseContext.registerReference(subsetting, "subsettingFeature", subsettingFeatureName)
        }

        // Parse subsetted feature (generalType)
        ctx.generalType()?.qualifiedName()?.let { qn ->
            val subsettedFeatureName = extractQualifiedName(qn)
            parseContext.registerReference(subsetting, "subsettedFeature", subsettedFeatureName)
        }

        // Create membership with parent namespace (inherited from BaseRelationshipVisitor)
        createRelationshipMembership(subsetting, parseContext)

        // Parse relationship body (inherited from BaseRelationshipVisitor)
        ctx.relationshipBody()?.let { body ->
            val childContext = parseContext.withParent(subsetting, subsetting.declaredName ?: "")
            parseRelationshipBody(body, subsetting, childContext)
        }

        return subsetting
    }
}
