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
 * Implementation of Import.
 * An abstract relationship that imports elements into a namespace
 */
abstract class ImportImpl(
    wrapped: MDMObject,
    engine: GearshiftEngine
) : RelationshipImpl(wrapped, engine), Import {

    override var isImportAll: Boolean
        get() {
            val rawValue = wrapped.getProperty("isImportAll")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "isImportAll", value)
        }

    override var isRecursive: Boolean
        get() {
            val rawValue = wrapped.getProperty("isRecursive")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "isRecursive", value)
        }

    override var visibility: String
        get() {
            val rawValue = wrapped.getProperty("visibility")
            return (rawValue as? String) ?: ""
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "visibility", value)
        }

    override var importOwningNamespace: Namespace
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "importOwningNamespace")
            val linked = (rawValue as? List<*>)?.filterIsInstance<MDMObject>()?.firstOrNull()
            return linked?.let { Wrappers.wrap(it, engine) as Namespace }
                ?: throw IllegalStateException("Required association end 'importOwningNamespace' has no linked target")
        }
        set(value) {
            val rawValue = (value as? BaseModelElementImpl)?.wrapped
            engine.setProperty(wrapped.id!!, "importOwningNamespace", rawValue)
        }

    override val importedElement: Element
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "importedElement")
            return (rawValue as MDMObject).let { Wrappers.wrap(it, engine) as Element }
        }

    abstract override fun importedMemberships(excluded: List<Namespace>): List<Membership>
}

