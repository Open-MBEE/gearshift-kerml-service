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
package org.openmbee.mdm.framework.runtime

/**
 * Evaluator for expressions in a specific language.
 * Implementations handle constraint evaluation, derived property computation, and operation execution.
 */
interface ExpressionEvaluator {
    /** The language this evaluator handles */
    val language: ExpressionLanguage

    /**
     * Evaluate an expression in the context of an element.
     *
     * @param expression The expression text
     * @param element The context element (self)
     * @param model The model for navigation and resolution
     * @param args Additional arguments (for operations)
     * @return The result of evaluation
     */
    fun evaluate(
        expression: String,
        element: MDMObject,
        model: MDMEngine,
        args: Map<String, Any?> = emptyMap()
    ): Any?
}
