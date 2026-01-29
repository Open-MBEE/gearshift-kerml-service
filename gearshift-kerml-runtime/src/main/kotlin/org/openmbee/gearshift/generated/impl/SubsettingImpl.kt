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
 * Implementation of Subsetting.
 * A specialization where one feature subsets another
 */
open class SubsettingImpl(
    wrapped: MDMObject,
    engine: MDMEngine
) : SpecializationImpl(wrapped, engine), Subsetting {

    override val owningFeature: Feature?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "owningFeature")
            return (rawValue as? MDMObject)?.let { Wrappers.wrap(it, engine) as Feature }
        }

    override var subsettedFeature: Feature
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "subsettedFeature")
            val linked = (rawValue as? List<*>)?.filterIsInstance<MDMObject>()?.firstOrNull()
            return linked?.let { Wrappers.wrap(it, engine) as Feature }
                ?: throw IllegalStateException("Required association end 'subsettedFeature' has no linked target")
        }
        set(value) {
            val rawValue = (value as? BaseModelElementImpl)?.wrapped
            engine.setProperty(wrapped.id!!, "subsettedFeature", rawValue)
        }

    override var subsettingFeature: Feature
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "subsettingFeature")
            val linked = (rawValue as? List<*>)?.filterIsInstance<MDMObject>()?.firstOrNull()
            return linked?.let { Wrappers.wrap(it, engine) as Feature }
                ?: throw IllegalStateException("Required association end 'subsettingFeature' has no linked target")
        }
        set(value) {
            val rawValue = (value as? BaseModelElementImpl)?.wrapped
            engine.setProperty(wrapped.id!!, "subsettingFeature", rawValue)
        }
}

