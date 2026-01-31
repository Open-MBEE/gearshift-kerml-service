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
 * Implementation of Specialization.
 * A relationship that makes one type a specialization of another
 */
open class SpecializationImpl(
    wrapped: MDMObject,
    engine: MDMEngine
) : RelationshipImpl(wrapped, engine), Specialization {

    override var general: Type
        get() {
            // Navigate via generalizationGeneralAssociation
            val linkedTargets = engine.getLinkedTargets("generalizationGeneralAssociation", wrapped.id!!)
            val linked = linkedTargets.firstOrNull()
            return linked?.let { Wrappers.wrap(it, engine) as Type }
                ?: throw IllegalStateException("Required association end 'general' has no linked target")
        }
        set(value) {
            val targetObj = value as? MDMObject
                ?: (value as? BaseModelElementImpl)?.let { it.wrapped }
                ?: throw IllegalArgumentException("general must be an MDMObject")
            engine.link(
                sourceId = wrapped.id!!,
                targetId = targetObj.id!!,
                associationName = "generalizationGeneralAssociation"
            )
        }

    override val owningType: Type?
        get() {
            // Navigate via owningTypeOwnedSpecializationAssociation (inverse direction)
            // Find the type that owns this specialization
            return engine.getAllElements()
                .filter { it.className == "Type" || engine.schema.getClass(it.className)?.superclasses?.contains("Type") == true }
                .firstOrNull { type ->
                    val targets = engine.getLinkedTargets("owningTypeOwnedSpecializationAssociation", type.id!!)
                    targets.any { it.id == wrapped.id }
                }
                ?.let { Wrappers.wrap(it, engine) as Type }
        }

    override var specific: Type
        get() {
            // Navigate via specializationSpecificAssociation
            val linkedTargets = engine.getLinkedTargets("specializationSpecificAssociation", wrapped.id!!)
            val linked = linkedTargets.firstOrNull()
            return linked?.let { Wrappers.wrap(it, engine) as Type }
                ?: throw IllegalStateException("Required association end 'specific' has no linked target")
        }
        set(value) {
            val targetObj = value as? MDMObject
                ?: (value as? BaseModelElementImpl)?.let { it.wrapped }
                ?: throw IllegalArgumentException("specific must be an MDMObject")
            engine.link(
                sourceId = wrapped.id!!,
                targetId = targetObj.id!!,
                associationName = "specializationSpecificAssociation"
            )
        }
}

