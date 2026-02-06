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
 * A step that represents an expression
 */
interface Expression : Step {

    /**
     * Whether this Expression meets the constraints necessary to be evaluated at model level.
     */
    val isModelLevelEvaluable: Boolean

    val function: Function?

    val result: Feature

    /**
     * Model-level evaluate this Expression with the given target. If the result is a LiteralBoolean, return its value. Otherwise return false.
     */
    fun checkCondition(target: Element): Boolean?

    /**
     * If this Expression isModelLevelEvaluable, then evaluate it using the target as the contextElement for resolving Feature names and testing classification. The result is a collection of Elements, which, for a fully evaluable Expression, will be a LiteralExpression or a Feature that is not an Expression.
     */
    fun evaluate(target: Element): List<Element>

    /**
     * Return whether this Expression is model-level evaluable. The visited parameter is used to track possible circular Feature references made from FeatureReferenceExpressions. Such circular references are not allowed in model-level evaluable expressions.
     */
    fun modelLevelEvaluable(visited: List<Feature>): Boolean?
}

