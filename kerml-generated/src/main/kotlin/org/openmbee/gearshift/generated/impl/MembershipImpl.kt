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
 * Implementation of Membership.
 * A relationship that makes an element a member of a namespace
 */
open class MembershipImpl(
    className: String,
    metaClass: FrameworkMetaClass,
    engine: MDMEngine
) : RelationshipImpl(className, metaClass, engine), Membership {

    /**
     * Create a new Membership instance.
     * @param parent The owning Element (optional)
     */
    constructor(
        engine: MDMEngine,
        parent: Element? = null,
        aliasIds: List<String> = emptyList(),
        declaredName: String? = null,
        declaredShortName: String? = null,
        elementId: String = "",
        isImpliedIncluded: Boolean = false
    ) : this("Membership", engine.schema.getClass("Membership")!!, engine) {
        this.id = java.util.UUID.randomUUID().toString()
        engine.registerElement(this)

        if (aliasIds.isNotEmpty()) this.aliasIds = aliasIds
        declaredName?.let { this.declaredName = it }
        declaredShortName?.let { this.declaredShortName = it }
        this.elementId = elementId
        this.isImpliedIncluded = isImpliedIncluded

        // Establish ownership via appropriate intermediate
        parent?.let { owner ->
            val resolver = OwnershipResolver(engine.schema)
            val resolved = resolver.resolve(owner.className, "Membership")
            if (resolved != null) {
                val membership = engine.createElement(resolved.intermediateType)
                engine.setProperty(membership.id!!, resolved.binding.ownedElementEnd, this)
                engine.setProperty(membership.id!!, resolved.binding.ownerEnd, owner)
                // Set member names on membership for navigation
                declaredName?.let { engine.setProperty(membership.id!!, "memberName", it) }
                declaredShortName?.let { engine.setProperty(membership.id!!, "memberShortName", it) }
            }
        }
    }


    override val memberElementId: String?
        get() {
            val rawValue = engine.getProperty(id!!, "memberElementId")
            return rawValue as? String
        }

    override var memberName: String?
        get() {
            val rawValue = getProperty("memberName")
            return rawValue as? String
        }
        set(value) {
            engine.setProperty(id!!, "memberName", value)
        }

    override var memberShortName: String?
        get() {
            val rawValue = getProperty("memberShortName")
            return rawValue as? String
        }
        set(value) {
            engine.setProperty(id!!, "memberShortName", value)
        }

    override var visibility: String
        get() {
            val rawValue = getProperty("visibility")
            return (rawValue as? String) ?: ""
        }
        set(value) {
            engine.setProperty(id!!, "visibility", value)
        }

    override var memberElement: Element
        get() {
            val rawValue = engine.getProperty(id!!, "memberElement")
            return rawValue as Element
        }
        set(value) {
            engine.setProperty(id!!, "memberElement", value)
        }

    override val membershipNamespace: Set<Namespace>
        get() {
            val rawValue = engine.getProperty(id!!, "membershipNamespace")
            return (rawValue as? List<*>)?.filterIsInstance<Namespace>()?.toSet() ?: emptySet()
        }

    override var membershipOwningNamespace: Namespace
        get() {
            val rawValue = engine.getProperty(id!!, "membershipOwningNamespace")
            return rawValue as Namespace
        }
        set(value) {
            engine.setProperty(id!!, "membershipOwningNamespace", value)
        }

    override fun isDistinguishableFrom(other: Membership): Boolean {
        val result = engine.invokeOperation(id!!, "isDistinguishableFrom", mapOf("other" to other))
        return (result as? Boolean) ?: false
    }
}

