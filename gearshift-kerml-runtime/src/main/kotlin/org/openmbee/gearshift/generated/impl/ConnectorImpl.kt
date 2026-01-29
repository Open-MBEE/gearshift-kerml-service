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
 * Implementation of Connector.
 * A feature and relationship that represents a connector
 */
open class ConnectorImpl(
    wrapped: MDMObject,
    engine: MDMEngine
) : FeatureImpl(wrapped, engine), Connector {

    override var isImplied: Boolean
        get() {
            val rawValue = wrapped.getProperty("isImplied")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "isImplied", value)
        }

    override val association: List<Association>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "association")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Association }
                ?: emptyList()
        }

    override val connectorEnd: List<Feature>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "connectorEnd")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Feature }
                ?: emptyList()
        }

    override val defaultFeaturingType: Type?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "defaultFeaturingType")
            return (rawValue as? MDMObject)?.let { Wrappers.wrap(it, engine) as Type }
        }

    override val relatedFeature: List<Feature>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "relatedFeature")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Feature }
                ?: emptyList()
        }

    override val sourceFeature: Feature?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "sourceFeature")
            return (rawValue as? MDMObject)?.let { Wrappers.wrap(it, engine) as Feature }
        }

    override val targetFeature: List<Feature>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "targetFeature")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Feature }
                ?: emptyList()
        }

    override var ownedRelatedElement: List<Element>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedRelatedElement")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Element }
                ?: emptyList()
        }
        set(value) {
            val rawValue = value.map { (it as BaseModelElementImpl).wrapped }
            engine.setProperty(wrapped.id!!, "ownedRelatedElement", rawValue)
        }

    override var owningRelatedElement: Element?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "owningRelatedElement")
            val linked = (rawValue as? List<*>)?.filterIsInstance<MDMObject>()?.firstOrNull()
            return linked?.let { Wrappers.wrap(it, engine) as Element }
        }
        set(value) {
            val rawValue = (value as? BaseModelElementImpl)?.wrapped
            engine.setProperty(wrapped.id!!, "owningRelatedElement", rawValue)
        }

    override val relatedElement: List<Element>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "relatedElement")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Element }
                ?: emptyList()
        }

    override var source: Set<Element>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "source")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Element }?.toSet()
                ?: emptySet()
        }
        set(value) {
            val rawValue = value.map { (it as BaseModelElementImpl).wrapped }
            engine.setProperty(wrapped.id!!, "source", rawValue)
        }

    override var target: Set<Element>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "target")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Element }?.toSet()
                ?: emptySet()
        }
        set(value) {
            val rawValue = value.map { (it as BaseModelElementImpl).wrapped }
            engine.setProperty(wrapped.id!!, "target", rawValue)
        }
}

