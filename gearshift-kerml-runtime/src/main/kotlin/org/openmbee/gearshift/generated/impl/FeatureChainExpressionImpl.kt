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

package org.openmbee.gearshift.generated.impl

import org.openmbee.gearshift.framework.runtime.MDMEngine
import org.openmbee.gearshift.framework.runtime.MDMObject
import org.openmbee.gearshift.generated.interfaces.*
import org.openmbee.gearshift.generated.Wrappers
import org.openmbee.gearshift.generated.interfaces.Annotation as KerMLAnnotation
import org.openmbee.gearshift.generated.interfaces.Function as KerMLFunction

/**
 * Implementation of FeatureChainExpression.
 * A FeatureChainExpression is an OperatorExpression whose operator is '.', which resolves to the Function ControlFunctions::'.' from the Kernel Functions Library.
 */
open class FeatureChainExpressionImpl(
    wrapped: MDMObject,
    engine: MDMEngine
) : OperatorExpressionImpl(wrapped, engine), FeatureChainExpression {

    override var operator: String
        get() {
            val rawValue = wrapped.getProperty("operator")
            return (rawValue as? String) ?: ""
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "operator", value)
        }

    override val targetFeature: Feature
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "targetFeature")
            return (rawValue as MDMObject).let { Wrappers.wrap(it, engine) as Feature }
        }

    override fun sourceTargetFeature(): Feature? {
        val result = engine.invokeOperation(wrapped.id!!, "sourceTargetFeature")
        return (result as? MDMObject)?.let { Wrappers.wrap(it, engine) as Feature }
    }
}

