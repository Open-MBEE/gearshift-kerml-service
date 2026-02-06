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
 * Implementation of Namespace.
 * An element that can contain other elements as members
 */
open class NamespaceImpl(
    className: String,
    metaClass: FrameworkMetaClass,
    engine: MDMEngine
) : ElementImpl(className, metaClass, engine), Namespace {

    /**
     * Create a new Namespace instance.
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
    ) : this("Namespace", engine.schema.getClass("Namespace")!!, engine) {
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
            val resolved = resolver.resolve(owner.className, "Namespace")
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


    override val importedMembership: List<Membership>
        get() {
            val rawValue = engine.getProperty(id!!, "importedMembership")
            return (rawValue as? List<*>)?.filterIsInstance<Membership>() ?: emptyList()
        }

    override val member: List<Element>
        get() {
            val rawValue = engine.getProperty(id!!, "member")
            return (rawValue as? List<*>)?.filterIsInstance<Element>() ?: emptyList()
        }

    override val membership: List<Membership>
        get() {
            val rawValue = engine.getProperty(id!!, "membership")
            return (rawValue as? List<*>)?.filterIsInstance<Membership>() ?: emptyList()
        }

    override var ownedImport: List<Import>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedImport")
            return (rawValue as? List<*>)?.filterIsInstance<Import>() ?: emptyList()
        }
        set(value) {
            engine.setProperty(id!!, "ownedImport", value)
        }

    override val ownedMember: List<Element>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedMember")
            return (rawValue as? List<*>)?.filterIsInstance<Element>() ?: emptyList()
        }

    override var ownedMembership: List<Membership>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedMembership")
            return (rawValue as? List<*>)?.filterIsInstance<Membership>() ?: emptyList()
        }
        set(value) {
            engine.setProperty(id!!, "ownedMembership", value)
        }

    override fun importedMemberships(excluded: List<Namespace>): List<Membership> {
        val result = engine.invokeOperation(id!!, "importedMemberships", mapOf("excluded" to excluded))
        return (result as? List<*>)?.filterIsInstance<Membership>() ?: emptyList()
    }

    override fun membershipsOfVisibility(visibility: String?, excluded: List<Namespace>): List<Membership> {
        val result = engine.invokeOperation(id!!, "membershipsOfVisibility", mapOf("visibility" to visibility, "excluded" to excluded))
        return (result as? List<*>)?.filterIsInstance<Membership>() ?: emptyList()
    }

    override fun namesOf(element: Element): List<String> {
        val result = engine.invokeOperation(id!!, "namesOf", mapOf("element" to element))
        @Suppress("UNCHECKED_CAST")
        return (result as? List<String>) ?: emptyList()
    }

    override fun qualificationOf(qualifiedName: String): String? {
        val result = engine.invokeOperation(id!!, "qualificationOf", mapOf("qualifiedName" to qualifiedName))
        return result as? String
    }

    override fun resolve(qualifiedName: String): Membership? {
        val result = engine.invokeOperation(id!!, "resolve", mapOf("qualifiedName" to qualifiedName))
        return result as? Membership
    }

    override fun resolveGlobal(qualifiedName: String): Membership? {
        val result = engine.invokeOperation(id!!, "resolveGlobal", mapOf("qualifiedName" to qualifiedName))
        return result as? Membership
    }

    override fun resolveLocal(name: String): Membership? {
        val result = engine.invokeOperation(id!!, "resolveLocal", mapOf("name" to name))
        return result as? Membership
    }

    override fun resolveVisible(name: String): Membership? {
        val result = engine.invokeOperation(id!!, "resolveVisible", mapOf("name" to name))
        return result as? Membership
    }

    override fun unqualifiedNameOf(qualifiedName: String): String {
        val result = engine.invokeOperation(id!!, "unqualifiedNameOf", mapOf("qualifiedName" to qualifiedName))
        return (result as? String) ?: ""
    }

    override fun visibilityOf(mem: Membership): String {
        val result = engine.invokeOperation(id!!, "visibilityOf", mapOf("mem" to mem))
        return (result as? String) ?: ""
    }

    override fun visibleMemberships(excluded: List<Namespace>, isRecursive: Boolean, includeAll: Boolean): List<Membership> {
        val result = engine.invokeOperation(id!!, "visibleMemberships", mapOf("excluded" to excluded, "isRecursive" to isRecursive, "includeAll" to includeAll))
        return (result as? List<*>)?.filterIsInstance<Membership>() ?: emptyList()
    }
}

