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
 * Implementation of TypeFeaturing.
 * A featuring relationship involving a type
 */
open class TypeFeaturingImpl(
    wrapped: MDMObject,
    engine: MDMEngine
) : FeaturingImpl(wrapped, engine), TypeFeaturing {

    override var featureOfType: Feature
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "featureOfType")
            val linked = (rawValue as? List<*>)?.filterIsInstance<MDMObject>()?.firstOrNull()
            return linked?.let { Wrappers.wrap(it, engine) as Feature }
                ?: throw IllegalStateException("Required association end 'featureOfType' has no linked target")
        }
        set(value) {
            val rawValue = (value as? BaseModelElementImpl)?.wrapped
            engine.setProperty(wrapped.id!!, "featureOfType", rawValue)
        }

    override var featuringType: Type
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "featuringType")
            val linked = (rawValue as? List<*>)?.filterIsInstance<MDMObject>()?.firstOrNull()
            return linked?.let { Wrappers.wrap(it, engine) as Type }
                ?: throw IllegalStateException("Required association end 'featuringType' has no linked target")
        }
        set(value) {
            val rawValue = (value as? BaseModelElementImpl)?.wrapped
            engine.setProperty(wrapped.id!!, "featuringType", rawValue)
        }

    override val owningFeatureOfType: Feature?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "owningFeatureOfType")
            return (rawValue as? MDMObject)?.let { Wrappers.wrap(it, engine) as Feature }
        }
}

