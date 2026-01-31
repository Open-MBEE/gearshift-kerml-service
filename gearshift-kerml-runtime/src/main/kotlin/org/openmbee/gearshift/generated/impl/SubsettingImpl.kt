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
 * Implementation of Subsetting.
 * A specialization where one feature subsets another
 */
open class SubsettingImpl(
    wrapped: MDMObject,
    engine: MDMEngine
) : SpecializationImpl(wrapped, engine), Subsetting {

    override val owningFeature: Feature?
        get() {
            // Navigate via owningFeatureOwnedSubsettingAssociation (inverse direction)
            // Find the feature that owns this subsetting
            return engine.getAllElements()
                .filter { it.className == "Feature" || engine.schema.getClass(it.className)?.superclasses?.contains("Feature") == true }
                .firstOrNull { feature ->
                    val targets = engine.getLinkedTargets("owningFeatureOwnedSubsettingAssociation", feature.id!!)
                    targets.any { it.id == wrapped.id }
                }
                ?.let { Wrappers.wrap(it, engine) as Feature }
        }

    override var subsettedFeature: Feature
        get() {
            // Navigate via supersettingSubsettedFeatureAssociation
            val linkedTargets = engine.getLinkedTargets("supersettingSubsettedFeatureAssociation", wrapped.id!!)
            val linked = linkedTargets.firstOrNull()
            return linked?.let { Wrappers.wrap(it, engine) as Feature }
                ?: throw IllegalStateException("Required association end 'subsettedFeature' has no linked target")
        }
        set(value) {
            val targetObj = value as? MDMObject
                ?: (value as? BaseModelElementImpl)?.let { it.wrapped }
                ?: throw IllegalArgumentException("subsettedFeature must be an MDMObject")
            engine.link(
                sourceId = wrapped.id!!,
                targetId = targetObj.id!!,
                associationName = "supersettingSubsettedFeatureAssociation"
            )
        }

    override var subsettingFeature: Feature
        get() {
            // Navigate via subsettingSubsettingFeatureAssociation
            val linkedTargets = engine.getLinkedTargets("subsettingSubsettingFeatureAssociation", wrapped.id!!)
            val linked = linkedTargets.firstOrNull()
            // Also try the base Specialization.specific association which subsettingFeature redefines
            if (linked == null) {
                val specificTargets = engine.getLinkedTargets("specializationSpecificAssociation", wrapped.id!!)
                val specificLinked = specificTargets.firstOrNull()
                if (specificLinked != null) {
                    val wrappedObj = Wrappers.wrap(specificLinked, engine)
                    if (wrappedObj is Feature) return wrappedObj
                }
            }
            if (linked == null) {
                // Debug: show all links for this Subsetting
                println("DEBUG Subsetting ${wrapped.id} has no subsettingFeature link")
                println("DEBUG   isImplied=${wrapped.getProperty("isImplied")}")
                println("DEBUG   All links from this element:")
                val allElements = engine.getAllElements()
                for (assocName in listOf("subsettingSubsettingFeatureAssociation", "specializationSpecificAssociation", "supersettingSubsettedFeatureAssociation", "generalizationGeneralAssociation")) {
                    val targets = engine.getLinkedTargets(assocName, wrapped.id!!)
                    if (targets.isNotEmpty()) {
                        println("DEBUG     $assocName -> ${targets.map { it.id }}")
                    }
                }
            }
            return linked?.let { Wrappers.wrap(it, engine) as Feature }
                ?: throw IllegalStateException("Required association end 'subsettingFeature' has no linked target for Subsetting ${wrapped.id}, isImplied=${wrapped.getProperty("isImplied")}")
        }
        set(value) {
            val targetObj = value as? MDMObject
                ?: (value as? BaseModelElementImpl)?.let { it.wrapped }
                ?: throw IllegalArgumentException("subsettingFeature must be an MDMObject")
            println("DEBUG SubsettingImpl.setter: linking ${wrapped.id} -> ${targetObj.id}")
            // Link via the Subsetting-specific association
            engine.link(
                sourceId = wrapped.id!!,
                targetId = targetObj.id!!,
                associationName = "subsettingSubsettingFeatureAssociation"
            )
            // Also link via the base Specialization.specific association (which subsettingFeature redefines)
            engine.link(
                sourceId = wrapped.id!!,
                targetId = targetObj.id!!,
                associationName = "specializationSpecificAssociation"
            )
        }
}

