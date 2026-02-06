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
 * Implementation of Import.
 * An abstract relationship that imports elements into a namespace
 */
abstract class ImportImpl(
    className: String,
    metaClass: FrameworkMetaClass,
    engine: MDMEngine
) : RelationshipImpl(className, metaClass, engine), Import {


    override var isImportAll: Boolean
        get() {
            val rawValue = getProperty("isImportAll")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(id!!, "isImportAll", value)
        }

    override var isRecursive: Boolean
        get() {
            val rawValue = getProperty("isRecursive")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(id!!, "isRecursive", value)
        }

    override var visibility: String
        get() {
            val rawValue = getProperty("visibility")
            return (rawValue as? String) ?: ""
        }
        set(value) {
            engine.setProperty(id!!, "visibility", value)
        }

    override var importOwningNamespace: Namespace
        get() {
            val rawValue = engine.getProperty(id!!, "importOwningNamespace")
            return rawValue as Namespace
        }
        set(value) {
            engine.setProperty(id!!, "importOwningNamespace", value)
        }

    override val importedElement: Element
        get() {
            val rawValue = engine.getProperty(id!!, "importedElement")
            return rawValue as Element
        }

    abstract override fun importedMemberships(excluded: List<Namespace>): List<Membership>
}

