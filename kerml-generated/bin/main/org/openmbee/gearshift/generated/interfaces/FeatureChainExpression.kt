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
 * A FeatureChainExpression is an OperatorExpression whose operator is '.', which resolves to the Function ControlFunctions::'.' from the Kernel Functions Library.
 */
interface FeatureChainExpression : OperatorExpression {

    /**
     * The operator for this FeatureChainExpression, which must be '.'.
     */
    override var operator: String

    val targetFeature: Feature

    /**
     * Return the first ownedFeature of the first owned input parameter of this FeatureChainExpression (if any).
     */
    fun sourceTargetFeature(): Feature?
}

