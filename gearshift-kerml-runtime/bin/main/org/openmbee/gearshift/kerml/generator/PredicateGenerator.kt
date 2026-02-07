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

import org.openmbee.gearshift.generated.interfaces.Predicate
import org.openmbee.gearshift.kerml.generator.base.BaseClassifierGenerator

class PredicateGenerator : BaseClassifierGenerator<Predicate>() {

    override fun generate(element: Predicate, context: GenerationContext): String = buildString {
        append(context.currentIndent())
        append(generateTypePrefix(element))
        append("predicate ")
        append(generateClassifierDeclaration(element, context))
        append(generateTypeBody(element, context))
    }
}
