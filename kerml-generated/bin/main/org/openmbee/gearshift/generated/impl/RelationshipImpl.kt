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

import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.meta.MetaClass as FrameworkMetaClass
import org.openmbee.mdm.framework.runtime.MDMObject
import org.openmbee.mdm.framework.runtime.OwnershipResolver
import org.openmbee.gearshift.generated.interfaces.*
import org.openmbee.gearshift.generated.interfaces.Annotation as KerMLAnnotation
import org.openmbee.gearshift.generated.interfaces.Function as KerMLFunction

/**
 * Implementation of Relationship.
 * An abstract base class for all relationships between elements
 */
abstract class RelationshipImpl(
    className: String,
    metaClass: FrameworkMetaClass,
    engine: MDMEngine
) : ElementImpl(className, metaClass, engine), Relationship {


    override var isImplied: Boolean
        get() {
            val rawValue = getProperty("isImplied")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(id!!, "isImplied", value)
        }

    override var ownedRelatedElement: List<Element>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedRelatedElement")
            return (rawValue as? List<*>)?.filterIsInstance<Element>() ?: emptyList()
        }
        set(value) {
            engine.setProperty(id!!, "ownedRelatedElement", value)
        }

    override var owningRelatedElement: Element?
        get() {
            val rawValue = engine.getProperty(id!!, "owningRelatedElement")
            return rawValue as? Element
        }
        set(value) {
            engine.setProperty(id!!, "owningRelatedElement", value)
        }

    override val relatedElement: List<Element>
        get() {
            val rawValue = engine.getProperty(id!!, "relatedElement")
            return (rawValue as? List<*>)?.filterIsInstance<Element>() ?: emptyList()
        }

    override var source: Set<Element>
        get() {
            val rawValue = engine.getProperty(id!!, "source")
            return (rawValue as? List<*>)?.filterIsInstance<Element>()?.toSet() ?: emptySet()
        }
        set(value) {
            engine.setProperty(id!!, "source", value)
        }

    override var target: Set<Element>
        get() {
            val rawValue = engine.getProperty(id!!, "target")
            return (rawValue as? List<*>)?.filterIsInstance<Element>()?.toSet() ?: emptySet()
        }
        set(value) {
            engine.setProperty(id!!, "target", value)
        }

    override fun libraryNamespace(): Namespace? {
        val result = engine.invokeOperation(id!!, "libraryNamespace")
        return result as? Namespace
    }

    override fun path(): String {
        val result = engine.invokeOperation(id!!, "path")
        return (result as? String) ?: ""
    }
}

