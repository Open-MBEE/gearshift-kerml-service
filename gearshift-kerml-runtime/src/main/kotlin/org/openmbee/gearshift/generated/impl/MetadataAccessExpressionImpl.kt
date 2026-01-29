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
 * Implementation of MetadataAccessExpression.
 * A MetadataAccessExpression is an Expression whose result is a sequence of instances of Metaclasses representing the metadataFeatures of the referencedElement.
 */
open class MetadataAccessExpressionImpl(
    wrapped: MDMObject,
    engine: MDMEngine
) : ExpressionImpl(wrapped, engine), MetadataAccessExpression {

    override val referencedElement: Element
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "referencedElement")
            return (rawValue as MDMObject).let { Wrappers.wrap(it, engine) as Element }
        }

    override fun evaluate(target: Element): List<Element> {
        val result = engine.invokeOperation(wrapped.id!!, "evaluate", mapOf("target" to target))
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Element }
            ?: emptyList()
    }

    override fun metaclassFeature(metaclass: Metaclass): Feature? {
        val result = engine.invokeOperation(wrapped.id!!, "metaclassFeature", mapOf("metaclass" to metaclass))
        return (result as? MDMObject)?.let { Wrappers.wrap(it, engine) as Feature }
    }

    override fun modelLevelEvaluable(visited: List<Feature>): Boolean? {
        val result = engine.invokeOperation(wrapped.id!!, "modelLevelEvaluable", mapOf("visited" to visited))
        return result as? Boolean
    }
}

