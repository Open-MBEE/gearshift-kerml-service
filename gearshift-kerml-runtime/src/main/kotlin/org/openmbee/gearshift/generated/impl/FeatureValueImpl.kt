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
 * Implementation of FeatureValue.
 * A FeatureValue is a Membership that identifies a particular memberExpression that provides the value of the Feature that owns the FeatureValue.
 */
open class FeatureValueImpl(
    wrapped: MDMObject,
    engine: MDMEngine
) : OwningMembershipImpl(wrapped, engine), FeatureValue {

    override var isDefault: Boolean
        get() {
            val rawValue = wrapped.getProperty("isDefault")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "isDefault", value)
        }

    override var isInitial: Boolean
        get() {
            val rawValue = wrapped.getProperty("isInitial")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "isInitial", value)
        }

    override val featureWithValue: Feature
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "featureWithValue")
            return (rawValue as MDMObject).let { Wrappers.wrap(it, engine) as Feature }
        }

    override val value: Expression
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "value")
            return (rawValue as MDMObject).let { Wrappers.wrap(it, engine) as Expression }
        }
}

