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

import org.openmbee.gearshift.generated.interfaces.Association
import org.openmbee.gearshift.kerml.generator.base.BaseClassifierGenerator

/**
 * Generator for Association elements.
 *
 * Per KerML spec: Associations are classifiers that classify links between objects.
 *
 * Grammar:
 * ```
 * association
 *     : typePrefix 'assoc'
 *       classifierDeclaration typeBody
 *     ;
 * ```
 */
class AssociationGenerator : BaseClassifierGenerator<Association>() {

    override fun generate(element: Association, context: GenerationContext): String = buildString {
        val indent = context.currentIndent()

        // Indentation
        append(indent)

        // typePrefix: abstract?
        append(generateTypePrefix(element))

        // ASSOC keyword
        append("assoc ")

        // classifierDeclaration
        append(generateClassifierDeclaration(element, context))

        // typeBody
        append(generateTypeBody(element, context))
    }
}
