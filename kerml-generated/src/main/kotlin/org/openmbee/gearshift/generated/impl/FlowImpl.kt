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
 * Implementation of Flow.
 * A Flow is a Step that represents the transfer of values from one Feature to another. Flows can take non-zero time to complete.
 */
open class FlowImpl(
    className: String,
    metaClass: FrameworkMetaClass,
    engine: MDMEngine
) : StepImpl(className, metaClass, engine), Flow {

    /**
     * Create a new Flow instance.
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
    ) : this("Flow", engine.schema.getClass("Flow")!!, engine) {
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
            val resolved = resolver.resolve(owner.className, "Flow")
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

    override val flowEnd: List<FlowEnd>
        get() {
            val rawValue = engine.getProperty(id!!, "flowEnd")
            return (rawValue as? List<*>)?.filterIsInstance<FlowEnd>() ?: emptyList()
        }

    override val interaction: List<Interaction>
        get() {
            val rawValue = engine.getProperty(id!!, "interaction")
            return (rawValue as? List<*>)?.filterIsInstance<Interaction>() ?: emptyList()
        }

    override val payloadFeature: PayloadFeature?
        get() {
            val rawValue = engine.getProperty(id!!, "payloadFeature")
            return rawValue as? PayloadFeature
        }

    override val payloadType: List<Classifier>
        get() {
            val rawValue = engine.getProperty(id!!, "payloadType")
            return (rawValue as? List<*>)?.filterIsInstance<Classifier>() ?: emptyList()
        }

    override val sourceOutputFeature: Feature?
        get() {
            val rawValue = engine.getProperty(id!!, "sourceOutputFeature")
            return rawValue as? Feature
        }

    override val targetInputFeature: Feature?
        get() {
            val rawValue = engine.getProperty(id!!, "targetInputFeature")
            return rawValue as? Feature
        }

    override val association: List<Association>
        get() {
            val rawValue = engine.getProperty(id!!, "association")
            return (rawValue as? List<*>)?.filterIsInstance<Association>() ?: emptyList()
        }

    override val connectorEnd: List<Feature>
        get() {
            val rawValue = engine.getProperty(id!!, "connectorEnd")
            return (rawValue as? List<*>)?.filterIsInstance<Feature>() ?: emptyList()
        }

    override val defaultFeaturingType: Type?
        get() {
            val rawValue = engine.getProperty(id!!, "defaultFeaturingType")
            return rawValue as? Type
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

    override val relatedFeature: List<Feature>
        get() {
            val rawValue = engine.getProperty(id!!, "relatedFeature")
            return (rawValue as? List<*>)?.filterIsInstance<Feature>() ?: emptyList()
        }

    override var source: Set<Element>
        get() {
            val rawValue = engine.getProperty(id!!, "source")
            return (rawValue as? List<*>)?.filterIsInstance<Element>()?.toSet() ?: emptySet()
        }
        set(value) {
            engine.setProperty(id!!, "source", value)
        }

    override val sourceFeature: Feature?
        get() {
            val rawValue = engine.getProperty(id!!, "sourceFeature")
            return rawValue as? Feature
        }

    override var target: Set<Element>
        get() {
            val rawValue = engine.getProperty(id!!, "target")
            return (rawValue as? List<*>)?.filterIsInstance<Element>()?.toSet() ?: emptySet()
        }
        set(value) {
            engine.setProperty(id!!, "target", value)
        }

    override val targetFeature: List<Feature>
        get() {
            val rawValue = engine.getProperty(id!!, "targetFeature")
            return (rawValue as? List<*>)?.filterIsInstance<Feature>() ?: emptyList()
        }
}

