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
 * Implementation of Element.
 * Abstract root of the KerML element hierarchy.
 *
 * Now extends MDMObject via BaseModelElementImpl - the impl IS the model object.
 */
abstract class ElementImpl(
    wrapped: MDMObject,
    engine: MDMEngine
) : BaseModelElementImpl(wrapped, engine), Element {

    override var aliasIds: List<String>
        get() {
            val rawValue = wrapped.getProperty("aliasIds")
            @Suppress("UNCHECKED_CAST")
            return (rawValue as? List<String>) ?: emptyList()
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "aliasIds", value)
        }

    override var declaredName: String?
        get() {
            val rawValue = wrapped.getProperty("declaredName")
            return rawValue as? String
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "declaredName", value)
        }

    override var declaredShortName: String?
        get() {
            val rawValue = wrapped.getProperty("declaredShortName")
            return rawValue as? String
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "declaredShortName", value)
        }

    override var elementId: String
        get() {
            val rawValue = wrapped.getProperty("elementId")
            return (rawValue as? String) ?: ""
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "elementId", value)
        }

    override var isImpliedIncluded: Boolean
        get() {
            val rawValue = wrapped.getProperty("isImpliedIncluded")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "isImpliedIncluded", value)
        }

    override val isLibraryElement: Boolean
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "isLibraryElement")
            return (rawValue as? Boolean) ?: false
        }

    override val name: String?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "name")
            return rawValue as? String
        }

    override val qualifiedName: String?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "qualifiedName")
            return rawValue as? String
        }

    override val shortName: String?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "shortName")
            return rawValue as? String
        }

    override var clientDependency: Set<Dependency>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "clientDependency")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Dependency }?.toSet()
                ?: emptySet()
        }
        set(value) {
            val rawValue = value.map { (it as BaseModelElementImpl).wrapped }
            engine.setProperty(wrapped.id!!, "clientDependency", rawValue)
        }

    override val documentation: List<Documentation>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "documentation")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Documentation }
                ?: emptyList()
        }

    override val ownedAnnotation: List<KerMLAnnotation>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedAnnotation")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as KerMLAnnotation }
                ?: emptyList()
        }

    override val ownedElement: List<Element>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedElement")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Element }
                ?: emptyList()
        }

    override var ownedRelationship: List<Relationship>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedRelationship")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Relationship }
                ?: emptyList()
        }
        set(value) {
            val rawValue = value.map { (it as BaseModelElementImpl).wrapped }
            engine.setProperty(wrapped.id!!, "ownedRelationship", rawValue)
        }

    override val owner: Element?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "owner")
            return (rawValue as? MDMObject)?.let { Wrappers.wrap(it, engine) as Element }
        }

    override var owningMembership: OwningMembership?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "owningMembership")
            val linked = (rawValue as? List<*>)?.filterIsInstance<MDMObject>()?.firstOrNull()
            return linked?.let { Wrappers.wrap(it, engine) as OwningMembership }
        }
        set(value) {
            val rawValue = (value as? BaseModelElementImpl)?.wrapped
            engine.setProperty(wrapped.id!!, "owningMembership", rawValue)
        }

    override val owningNamespace: Namespace?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "owningNamespace")
            return (rawValue as? MDMObject)?.let { Wrappers.wrap(it, engine) as Namespace }
        }

    override var owningRelationship: Relationship?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "owningRelationship")
            val linked = (rawValue as? List<*>)?.filterIsInstance<MDMObject>()?.firstOrNull()
            return linked?.let { Wrappers.wrap(it, engine) as Relationship }
        }
        set(value) {
            val rawValue = (value as? BaseModelElementImpl)?.wrapped
            engine.setProperty(wrapped.id!!, "owningRelationship", rawValue)
        }

    override var supplierDependency: Set<Dependency>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "supplierDependency")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Dependency }?.toSet()
                ?: emptySet()
        }
        set(value) {
            val rawValue = value.map { (it as BaseModelElementImpl).wrapped }
            engine.setProperty(wrapped.id!!, "supplierDependency", rawValue)
        }

    override val textualRepresentation: List<TextualRepresentation>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "textualRepresentation")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as TextualRepresentation }
                ?: emptyList()
        }

    override fun effectiveName(): String? {
        val result = engine.invokeOperation(wrapped.id!!, "effectiveName")
        return result as? String
    }

    override fun effectiveShortName(): String? {
        val result = engine.invokeOperation(wrapped.id!!, "effectiveShortName")
        return result as? String
    }

    override fun escapedName(): String? {
        val result = engine.invokeOperation(wrapped.id!!, "escapedName")
        return result as? String
    }

    override fun libraryNamespace(): Namespace? {
        val result = engine.invokeOperation(wrapped.id!!, "libraryNamespace")
        return (result as? MDMObject)?.let { Wrappers.wrap(it, engine) as Namespace }
    }

    override fun path(): String {
        val result = engine.invokeOperation(wrapped.id!!, "path")
        return (result as? String) ?: ""
    }
}
