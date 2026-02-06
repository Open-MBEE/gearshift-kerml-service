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
 * Implementation of Association.
 * A classifier and relationship that represents an association
 */
open class AssociationImpl(
    className: String,
    metaClass: FrameworkMetaClass,
    engine: MDMEngine
) : ClassifierImpl(className, metaClass, engine), Association {

    /**
     * Create a new Association instance.
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
    ) : this("Association", engine.schema.getClass("Association")!!, engine) {
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
            val resolved = resolver.resolve(owner.className, "Association")
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


    override var isImplied: Boolean
        get() {
            val rawValue = getProperty("isImplied")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(id!!, "isImplied", value)
        }

    override val associationEnd: Set<Feature>
        get() {
            val rawValue = engine.getProperty(id!!, "associationEnd")
            return (rawValue as? List<*>)?.filterIsInstance<Feature>()?.toSet() ?: emptySet()
        }

    override val relatedType: List<Type>
        get() {
            val rawValue = engine.getProperty(id!!, "relatedType")
            return (rawValue as? List<*>)?.filterIsInstance<Type>() ?: emptyList()
        }

    override val sourceType: Type?
        get() {
            val rawValue = engine.getProperty(id!!, "sourceType")
            return rawValue as? Type
        }

    override val targetType: Set<Type>
        get() {
            val rawValue = engine.getProperty(id!!, "targetType")
            return (rawValue as? List<*>)?.filterIsInstance<Type>()?.toSet() ?: emptySet()
        }

    override var ownedRelatedElement: List<Element>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedRelatedElement")
            return (rawValue as? List<*>)?.filterIsInstance<Element>() ?: emptyList()
        }
        set(value) {
            engine.setProperty(id!!, "ownedRelatedElement", value)
        }

    override var owningRelatedElement: Element?
        get() {
            val rawValue = engine.getProperty(id!!, "owningRelatedElement")
            return rawValue as? Element
        }
        set(value) {
            engine.setProperty(id!!, "owningRelatedElement", value)
        }

    override val relatedElement: List<Element>
        get() {
            val rawValue = engine.getProperty(id!!, "relatedElement")
            return (rawValue as? List<*>)?.filterIsInstance<Element>() ?: emptyList()
        }

    override var source: Set<Element>
        get() {
            val rawValue = engine.getProperty(id!!, "source")
            return (rawValue as? List<*>)?.filterIsInstance<Element>()?.toSet() ?: emptySet()
        }
        set(value) {
            engine.setProperty(id!!, "source", value)
        }

    override var target: Set<Element>
        get() {
            val rawValue = engine.getProperty(id!!, "target")
            return (rawValue as? List<*>)?.filterIsInstance<Element>()?.toSet() ?: emptySet()
        }
        set(value) {
            engine.setProperty(id!!, "target", value)
        }
}

