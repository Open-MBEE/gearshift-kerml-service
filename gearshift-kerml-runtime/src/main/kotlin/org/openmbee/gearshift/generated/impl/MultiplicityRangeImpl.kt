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

import org.openmbee.gearshift.GearshiftEngine
import org.openmbee.gearshift.framework.runtime.MDMObject
import org.openmbee.gearshift.generated.interfaces.*
import org.openmbee.gearshift.generated.Wrappers
import org.openmbee.gearshift.generated.interfaces.Annotation as KerMLAnnotation
import org.openmbee.gearshift.generated.interfaces.Function as KerMLFunction

/**
 * Implementation of MultiplicityRange.
 * A MultiplicityRange is a Multiplicity whose value is defined to be the (inclusive) range of natural numbers given by the result of a lowerBound Expression and the result of an upperBound Expression.
 */
open class MultiplicityRangeImpl(
    wrapped: MDMObject,
    engine: GearshiftEngine
) : MultiplicityImpl(wrapped, engine), MultiplicityRange {

    override val bound: List<Expression>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "bound")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Expression }
                ?: emptyList()
        }

    override val lowerBound: Expression?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "lowerBound")
            return (rawValue as? MDMObject)?.let { Wrappers.wrap(it, engine) as Expression }
        }

    override val upperBound: Expression
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "upperBound")
            return (rawValue as MDMObject).let { Wrappers.wrap(it, engine) as Expression }
        }

    override fun hasBounds(lower: Int, upper: Int): Boolean? {
        val result = engine.invokeOperation(wrapped.id!!, "hasBounds", mapOf("lower" to lower, "upper" to upper))
        return result as? Boolean
    }

    override fun valueOf(bound: Expression?): Int? {
        val result = engine.invokeOperation(wrapped.id!!, "valueOf", mapOf("bound" to bound))
        return result as? Int
    }
}

