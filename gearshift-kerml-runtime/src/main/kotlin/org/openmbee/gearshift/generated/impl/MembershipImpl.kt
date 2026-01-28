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
 * Implementation of Membership.
 * A relationship that makes an element a member of a namespace
 */
open class MembershipImpl(
    wrapped: MDMObject,
    engine: GearshiftEngine
) : RelationshipImpl(wrapped, engine), Membership {

    override val memberElementId: String?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "memberElementId")
            return rawValue as? String
        }

    override var memberName: String?
        get() {
            val rawValue = wrapped.getProperty("memberName")
            return rawValue as? String
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "memberName", value)
        }

    override var memberShortName: String?
        get() {
            val rawValue = wrapped.getProperty("memberShortName")
            return rawValue as? String
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "memberShortName", value)
        }

    override var visibility: String
        get() {
            val rawValue = wrapped.getProperty("visibility")
            return (rawValue as? String) ?: ""
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "visibility", value)
        }

    override var memberElement: Element
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "memberElement")
            val linked = (rawValue as? List<*>)?.filterIsInstance<MDMObject>()?.firstOrNull()
            return linked?.let { Wrappers.wrap(it, engine) as Element }
                ?: throw IllegalStateException("Required association end 'memberElement' has no linked target")
        }
        set(value) {
            val rawValue = (value as? BaseModelElementImpl)?.wrapped
            engine.setProperty(wrapped.id!!, "memberElement", rawValue)
        }

    override val membershipNamespace: Set<Namespace>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "membershipNamespace")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Namespace }?.toSet()
                ?: emptySet()
        }

    override var membershipOwningNamespace: Namespace
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "membershipOwningNamespace")
            val linked = (rawValue as? List<*>)?.filterIsInstance<MDMObject>()?.firstOrNull()
            return linked?.let { Wrappers.wrap(it, engine) as Namespace }
                ?: throw IllegalStateException("Required association end 'membershipOwningNamespace' has no linked target")
        }
        set(value) {
            val rawValue = (value as? BaseModelElementImpl)?.wrapped
            engine.setProperty(wrapped.id!!, "membershipOwningNamespace", rawValue)
        }

    override fun isDistinguishableFrom(other: Membership): Boolean {
        val result = engine.invokeOperation(wrapped.id!!, "isDistinguishableFrom", mapOf("other" to other))
        return (result as? Boolean) ?: false
    }
}

