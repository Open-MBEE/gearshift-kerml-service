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
package org.openmbee.gearshift.kerml.generator

import org.openmbee.gearshift.generated.interfaces.BooleanExpression
import org.openmbee.gearshift.kerml.generator.base.BaseFeatureGenerator

class BooleanExpressionGenerator : BaseFeatureGenerator<BooleanExpression>() {

    override fun generate(element: BooleanExpression, context: GenerationContext): String = buildString {
        append(context.currentIndent())
        append(generateFeaturePrefix(element))
        append("bool ")
        append(generateFeatureDeclaration(element, context))
        append(generateValuePart(element, context))
        append(generateTypeBody(element, context))
    }
}
