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

package org.openmbee.gearshift.generated.interfaces

/**
 * An InvocationExpression is an InstantiationExpression whose instantiatedType must be a Behavior or a Feature typed by a single Behavior.
 */
interface InvocationExpression : InstantiationExpression {

    /**
     * Apply the Function that is the type of this InvocationExpression to the argument values resulting from evaluating each of the argument Expressions on the given target.
     */
    override fun evaluate(target: Element): List<Element>

    /**
     * An InvocationExpression is model-level evaluable if all its argument Expressions are model-level evaluable and its function is model-level evaluable.
     */
    override fun modelLevelEvaluable(visited: List<Feature>): Boolean?
}

