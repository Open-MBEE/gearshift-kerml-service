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
 * Implementation of View.
 * A Structure that exposes Elements for stakeholders, rendered by a Rendering and constrained by Viewpoints
 */
open class ViewImpl(
    className: String,
    metaClass: FrameworkMetaClass,
    engine: MDMEngine
) : StructureImpl(className, metaClass, engine), View {

    /**
     * Create a new View instance.
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
    ) : this("View", engine.schema.getClass("View")!!, engine) {
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
            val resolved = resolver.resolve(owner.className, "View")
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


    override val expose: Set<Expose>
        get() {
            val rawValue = engine.getProperty(id!!, "expose")
            return (rawValue as? List<*>)?.filterIsInstance<Expose>()?.toSet() ?: emptySet()
        }

    override val exposedElement: Set<Element>
        get() {
            val rawValue = engine.getProperty(id!!, "exposedElement")
            return (rawValue as? List<*>)?.filterIsInstance<Element>()?.toSet() ?: emptySet()
        }

    override val rendering: Rendering?
        get() {
            val rawValue = engine.getProperty(id!!, "rendering")
            return rawValue as? Rendering
        }

    override val satisfiedViewpoint: Set<ViewpointPredicate>
        get() {
            val rawValue = engine.getProperty(id!!, "satisfiedViewpoint")
            return (rawValue as? List<*>)?.filterIsInstance<ViewpointPredicate>()?.toSet() ?: emptySet()
        }

    override val subview: Set<View>
        get() {
            val rawValue = engine.getProperty(id!!, "subview")
            return (rawValue as? List<*>)?.filterIsInstance<View>()?.toSet() ?: emptySet()
        }
}

