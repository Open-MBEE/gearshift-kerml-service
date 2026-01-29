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
 * Implementation of Package.
 * A Namespace used to group Elements, without any instance-level semantics
 */
open class PackageImpl(
    wrapped: MDMObject,
    engine: MDMEngine
) : NamespaceImpl(wrapped, engine), Package {

    override val filterCondition: List<Expression>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "filterCondition")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Expression }
                ?: emptyList()
        }

    override fun importedMemberships(excluded: List<Namespace>): List<Membership> {
        val result = engine.invokeOperation(wrapped.id!!, "importedMemberships", mapOf("excluded" to excluded))
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Membership }
            ?: emptyList()
    }

    override fun includeAsMember(element: Element): Boolean? {
        val result = engine.invokeOperation(wrapped.id!!, "includeAsMember", mapOf("element" to element))
        return result as? Boolean
    }
}

