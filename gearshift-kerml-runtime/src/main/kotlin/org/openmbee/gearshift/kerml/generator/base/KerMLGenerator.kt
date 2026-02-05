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
package org.openmbee.gearshift.kerml.generator.base

import org.openmbee.gearshift.generated.interfaces.ModelElement
import org.openmbee.gearshift.kerml.generator.GenerationContext

/**
 * Base interface for KerML generators.
 *
 * Each generator handles a specific model element type and produces
 * KerML textual syntax. This is the inverse of TypedKerMLVisitor.
 *
 * @param T The model element type this generator handles
 */
interface KerMLGenerator<T : ModelElement> {

    /**
     * Generate KerML textual syntax for the given element.
     *
     * @param element The model element to generate syntax for
     * @param context The generation context with indentation and scope info
     * @return The KerML text representation of the element
     */
    fun generate(element: T, context: GenerationContext): String
}
