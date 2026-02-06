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

import org.openmbee.gearshift.generated.interfaces.Step
import org.openmbee.gearshift.kerml.antlr.KerMLParser
import org.openmbee.gearshift.kerml.parser.KermlParseContext
import org.openmbee.gearshift.kerml.parser.visitors.base.BaseFeatureVisitor

/**
 * Visitor for Step elements.
 *
 * Per KerML spec 8.2.5.6.2: Steps are features typed by Behaviors.
 *
 * Grammar:
 * ```
 * step
 *     : featurePrefix
 *       STEP featureDeclaration valuePart?
 *       typeBody
 *     ;
 * ```
 *
 * Step extends Feature. Uses inherited parsing from BaseFeatureVisitor.
 */
class StepVisitor : BaseFeatureVisitor<KerMLParser.StepContext, Step>() {

    override fun visit(ctx: KerMLParser.StepContext, kermlParseContext: KermlParseContext): Step {
        val step = kermlParseContext.create<Step>()

        // Parse feature prefix (inherited from BaseFeatureVisitor)
        parseFeaturePrefixContext(ctx.featurePrefix(), step)

        // Parse feature declaration (inherited from BaseFeatureVisitor)
        ctx.featureDeclaration()?.let { decl ->
            parseFeatureDeclaration(decl, step, kermlParseContext)
        }

        // Create child context for nested elements
        val childContext = kermlParseContext.withParent(step, step.declaredName ?: "")

        // Create membership with parent type (inherited from BaseTypeVisitor)
        createFeatureMembership(step, kermlParseContext)

        // Parse value part (inherited from BaseFeatureVisitor)
        parseValuePart(ctx.valuePart(), step, childContext)

        // Parse type body (inherited from BaseTypeVisitor)
        ctx.typeBody()?.let { body ->
            parseTypeBody(body, childContext)
        }

        return step
    }
}
