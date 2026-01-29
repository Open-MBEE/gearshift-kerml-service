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
 * Implementation of MembershipImport.
 * An import that brings a specific membership into a namespace
 */
open class MembershipImportImpl(
    wrapped: MDMObject,
    engine: MDMEngine
) : ImportImpl(wrapped, engine), MembershipImport {

    override var importedMembership: Membership
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "importedMembership")
            val linked = (rawValue as? List<*>)?.filterIsInstance<MDMObject>()?.firstOrNull()
            return linked?.let { Wrappers.wrap(it, engine) as Membership }
                ?: throw IllegalStateException("Required association end 'importedMembership' has no linked target")
        }
        set(value) {
            val rawValue = (value as? BaseModelElementImpl)?.wrapped
            engine.setProperty(wrapped.id!!, "importedMembership", rawValue)
        }

    override fun importedMemberships(excluded: List<Namespace>): List<Membership> {
        val result = engine.invokeOperation(wrapped.id!!, "importedMemberships", mapOf("excluded" to excluded))
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Membership }
            ?: emptyList()
    }
}

