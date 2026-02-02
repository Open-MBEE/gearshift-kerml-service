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
 * Implementation of Element.
 * Abstract root of the KerML element hierarchy
 */
abstract class ElementImpl(
    className: String,
    metaClass: FrameworkMetaClass,
    internal val engine: MDMEngine
) : MDMObject(className, metaClass), Element {


    override var aliasIds: List<String>
        get() {
            val rawValue = getProperty("aliasIds")
            @Suppress("UNCHECKED_CAST")
            return (rawValue as? List<String>) ?: emptyList()
        }
        set(value) {
            engine.setProperty(id!!, "aliasIds", value)
        }

    override var declaredName: String?
        get() {
            val rawValue = getProperty("declaredName")
            return rawValue as? String
        }
        set(value) {
            engine.setProperty(id!!, "declaredName", value)
        }

    override var declaredShortName: String?
        get() {
            val rawValue = getProperty("declaredShortName")
            return rawValue as? String
        }
        set(value) {
            engine.setProperty(id!!, "declaredShortName", value)
        }

    override var elementId: String
        get() {
            val rawValue = getProperty("elementId")
            return (rawValue as? String) ?: ""
        }
        set(value) {
            engine.setProperty(id!!, "elementId", value)
        }

    override var isImpliedIncluded: Boolean
        get() {
            val rawValue = getProperty("isImpliedIncluded")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(id!!, "isImpliedIncluded", value)
        }

    override val isLibraryElement: Boolean
        get() {
            val rawValue = engine.getProperty(id!!, "isLibraryElement")
            return (rawValue as? Boolean) ?: false
        }

    override val name: String?
        get() {
            val rawValue = engine.getProperty(id!!, "name")
            return rawValue as? String
        }

    override val qualifiedName: String?
        get() {
            val rawValue = engine.getProperty(id!!, "qualifiedName")
            return rawValue as? String
        }

    override val shortName: String?
        get() {
            val rawValue = engine.getProperty(id!!, "shortName")
            return rawValue as? String
        }

    override var clientDependency: Set<Dependency>
        get() {
            val rawValue = engine.getProperty(id!!, "clientDependency")
            return (rawValue as? List<*>)?.filterIsInstance<Dependency>()?.toSet() ?: emptySet()
        }
        set(value) {
            engine.setProperty(id!!, "clientDependency", value)
        }

    override val documentation: List<Documentation>
        get() {
            val rawValue = engine.getProperty(id!!, "documentation")
            return (rawValue as? List<*>)?.filterIsInstance<Documentation>() ?: emptyList()
        }

    override val ownedAnnotation: List<KerMLAnnotation>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedAnnotation")
            return (rawValue as? List<*>)?.filterIsInstance<KerMLAnnotation>() ?: emptyList()
        }

    override val ownedElement: List<Element>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedElement")
            return (rawValue as? List<*>)?.filterIsInstance<Element>() ?: emptyList()
        }

    override var ownedRelationship: List<Relationship>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedRelationship")
            return (rawValue as? List<*>)?.filterIsInstance<Relationship>() ?: emptyList()
        }
        set(value) {
            engine.setProperty(id!!, "ownedRelationship", value)
        }

    override val owner: Element?
        get() {
            val rawValue = engine.getProperty(id!!, "owner")
            return rawValue as? Element
        }

    override var owningMembership: OwningMembership?
        get() {
            val rawValue = engine.getProperty(id!!, "owningMembership")
            return rawValue as? OwningMembership
        }
        set(value) {
            engine.setProperty(id!!, "owningMembership", value)
        }

    override val owningNamespace: Namespace?
        get() {
            val rawValue = engine.getProperty(id!!, "owningNamespace")
            return rawValue as? Namespace
        }

    override var owningRelationship: Relationship?
        get() {
            val rawValue = engine.getProperty(id!!, "owningRelationship")
            return rawValue as? Relationship
        }
        set(value) {
            engine.setProperty(id!!, "owningRelationship", value)
        }

    override var supplierDependency: Set<Dependency>
        get() {
            val rawValue = engine.getProperty(id!!, "supplierDependency")
            return (rawValue as? List<*>)?.filterIsInstance<Dependency>()?.toSet() ?: emptySet()
        }
        set(value) {
            engine.setProperty(id!!, "supplierDependency", value)
        }

    override val textualRepresentation: List<TextualRepresentation>
        get() {
            val rawValue = engine.getProperty(id!!, "textualRepresentation")
            return (rawValue as? List<*>)?.filterIsInstance<TextualRepresentation>() ?: emptyList()
        }

    override fun effectiveName(): String? {
        val result = engine.invokeOperation(id!!, "effectiveName")
        return result as? String
    }

    override fun effectiveShortName(): String? {
        val result = engine.invokeOperation(id!!, "effectiveShortName")
        return result as? String
    }

    override fun escapedName(): String? {
        val result = engine.invokeOperation(id!!, "escapedName")
        return result as? String
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

