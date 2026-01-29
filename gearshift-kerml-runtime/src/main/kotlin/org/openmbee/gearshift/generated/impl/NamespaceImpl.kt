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
 * Implementation of Namespace.
 * An element that can contain other elements as members
 */
open class NamespaceImpl(
    wrapped: MDMObject,
    engine: MDMEngine
) : ElementImpl(wrapped, engine), Namespace {

    override val importedMembership: List<Membership>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "importedMembership")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Membership }
                ?: emptyList()
        }

    override val member: List<Element>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "member")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Element }
                ?: emptyList()
        }

    override val membership: List<Membership>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "membership")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Membership }
                ?: emptyList()
        }

    override var ownedImport: List<Import>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedImport")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Import }
                ?: emptyList()
        }
        set(value) {
            val rawValue = value.map { (it as BaseModelElementImpl).wrapped }
            engine.setProperty(wrapped.id!!, "ownedImport", rawValue)
        }

    override val ownedMember: List<Element>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedMember")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Element }
                ?: emptyList()
        }

    override var ownedMembership: List<Membership>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedMembership")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Membership }
                ?: emptyList()
        }
        set(value) {
            val rawValue = value.map { (it as BaseModelElementImpl).wrapped }
            engine.setProperty(wrapped.id!!, "ownedMembership", rawValue)
        }

    override fun importedMemberships(excluded: List<Namespace>): List<Membership> {
        val result = engine.invokeOperation(wrapped.id!!, "importedMemberships", mapOf("excluded" to excluded))
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Membership }
            ?: emptyList()
    }

    override fun membershipsOfVisibility(visibility: String?, excluded: List<Namespace>): List<Membership> {
        val result = engine.invokeOperation(wrapped.id!!, "membershipsOfVisibility", mapOf("visibility" to visibility, "excluded" to excluded))
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Membership }
            ?: emptyList()
    }

    override fun namesOf(element: Element): List<String> {
        val result = engine.invokeOperation(wrapped.id!!, "namesOf", mapOf("element" to element))
        @Suppress("UNCHECKED_CAST")
        return (result as? List<String>) ?: emptyList()
    }

    override fun qualificationOf(qualifiedName: String): String? {
        val result = engine.invokeOperation(wrapped.id!!, "qualificationOf", mapOf("qualifiedName" to qualifiedName))
        return result as? String
    }

    override fun resolve(qualifiedName: String): Membership? {
        val result = engine.invokeOperation(wrapped.id!!, "resolve", mapOf("qualifiedName" to qualifiedName))
        return (result as? MDMObject)?.let { Wrappers.wrap(it, engine) as Membership }
    }

    override fun resolveGlobal(qualifiedName: String): Membership? {
        val result = engine.invokeOperation(wrapped.id!!, "resolveGlobal", mapOf("qualifiedName" to qualifiedName))
        return (result as? MDMObject)?.let { Wrappers.wrap(it, engine) as Membership }
    }

    override fun resolveLocal(name: String): Membership? {
        val result = engine.invokeOperation(wrapped.id!!, "resolveLocal", mapOf("name" to name))
        return (result as? MDMObject)?.let { Wrappers.wrap(it, engine) as Membership }
    }

    override fun resolveVisible(name: String): Membership? {
        val result = engine.invokeOperation(wrapped.id!!, "resolveVisible", mapOf("name" to name))
        return (result as? MDMObject)?.let { Wrappers.wrap(it, engine) as Membership }
    }

    override fun unqualifiedNameOf(qualifiedName: String): String {
        val result = engine.invokeOperation(wrapped.id!!, "unqualifiedNameOf", mapOf("qualifiedName" to qualifiedName))
        return (result as? String) ?: ""
    }

    override fun visibilityOf(mem: Membership): String {
        val result = engine.invokeOperation(wrapped.id!!, "visibilityOf", mapOf("mem" to mem))
        return (result as? String) ?: ""
    }

    override fun visibleMemberships(excluded: List<Namespace>, isRecursive: Boolean, includeAll: Boolean): List<Membership> {
        val result = engine.invokeOperation(wrapped.id!!, "visibleMemberships", mapOf("excluded" to excluded, "isRecursive" to isRecursive, "includeAll" to includeAll))
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Membership }
            ?: emptyList()
    }
}

