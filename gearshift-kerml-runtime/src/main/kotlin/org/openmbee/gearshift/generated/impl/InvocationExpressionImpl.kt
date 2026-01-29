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
 * Implementation of InvocationExpression.
 * An InvocationExpression is an InstantiationExpression whose instantiatedType must be a Behavior or a Feature typed by a single Behavior.
 */
open class InvocationExpressionImpl(
    wrapped: MDMObject,
    engine: MDMEngine
) : InstantiationExpressionImpl(wrapped, engine), InvocationExpression {

    override fun evaluate(target: Element): List<Element> {
        val result = engine.invokeOperation(wrapped.id!!, "evaluate", mapOf("target" to target))
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Element }
            ?: emptyList()
    }

    override fun modelLevelEvaluable(visited: List<Feature>): Boolean? {
        val result = engine.invokeOperation(wrapped.id!!, "modelLevelEvaluable", mapOf("visited" to visited))
        return result as? Boolean
    }
}

