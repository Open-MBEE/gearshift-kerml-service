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
 * Implementation of InstantiationExpression.
 * An InstantiationExpression is an Expression that instantiates its instantiatedType, binding some or all of the features of that Type to the results of its arguments.
 */
abstract class InstantiationExpressionImpl(
    wrapped: MDMObject,
    engine: MDMEngine
) : ExpressionImpl(wrapped, engine), InstantiationExpression {

    override val argument: List<Expression>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "argument")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Expression }
                ?: emptyList()
        }

    override val instantiatedType: Type
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "instantiatedType")
            return (rawValue as MDMObject).let { Wrappers.wrap(it, engine) as Type }
        }

    override fun instantiatedType(): Type? {
        val result = engine.invokeOperation(wrapped.id!!, "instantiatedType")
        return (result as? MDMObject)?.let { Wrappers.wrap(it, engine) as Type }
    }
}

