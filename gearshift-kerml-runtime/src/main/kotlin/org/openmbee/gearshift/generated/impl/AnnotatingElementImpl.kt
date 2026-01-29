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
 * Implementation of AnnotatingElement.
 * An element that annotates other elements
 */
open class AnnotatingElementImpl(
    wrapped: MDMObject,
    engine: MDMEngine
) : ElementImpl(wrapped, engine), AnnotatingElement {

    override val annotatedElement: List<Element>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "annotatedElement")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Element }
                ?: emptyList()
        }

    override val annotation: List<KerMLAnnotation>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "annotation")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as KerMLAnnotation }
                ?: emptyList()
        }

    override val ownedAnnotatingRelationship: List<KerMLAnnotation>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedAnnotatingRelationship")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as KerMLAnnotation }
                ?: emptyList()
        }

    override var owningAnnotatingRelationship: KerMLAnnotation?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "owningAnnotatingRelationship")
            val linked = (rawValue as? List<*>)?.filterIsInstance<MDMObject>()?.firstOrNull()
            return linked?.let { Wrappers.wrap(it, engine) as KerMLAnnotation }
        }
        set(value) {
            val rawValue = (value as? BaseModelElementImpl)?.wrapped
            engine.setProperty(wrapped.id!!, "owningAnnotatingRelationship", rawValue)
        }
}

