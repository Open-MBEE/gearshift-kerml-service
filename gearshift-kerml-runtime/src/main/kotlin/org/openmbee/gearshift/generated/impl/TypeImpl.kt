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

import org.openmbee.gearshift.GearshiftEngine
import org.openmbee.gearshift.framework.runtime.MDMObject
import org.openmbee.gearshift.generated.interfaces.*
import org.openmbee.gearshift.generated.Wrappers
import org.openmbee.gearshift.generated.interfaces.Annotation as KerMLAnnotation
import org.openmbee.gearshift.generated.interfaces.Function as KerMLFunction

/**
 * Implementation of Type.
 * A namespace that can be specialized
 */
open class TypeImpl(
    wrapped: MDMObject,
    engine: GearshiftEngine
) : NamespaceImpl(wrapped, engine), Type {

    override var isAbstract: Boolean
        get() {
            val rawValue = wrapped.getProperty("isAbstract")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "isAbstract", value)
        }

    override var isSufficient: Boolean
        get() {
            val rawValue = wrapped.getProperty("isSufficient")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "isSufficient", value)
        }

    override val differencedType: Set<Type>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "differencedType")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Type }?.toSet()
                ?: emptySet()
        }

    override val differencingType: List<Type>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "differencingType")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Type }
                ?: emptyList()
        }

    override val directedFeature: List<Feature>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "directedFeature")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Feature }
                ?: emptyList()
        }

    override val endFeature: List<Feature>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "endFeature")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Feature }
                ?: emptyList()
        }

    override val feature: List<Feature>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "feature")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Feature }
                ?: emptyList()
        }

    override val featureMembership: List<FeatureMembership>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "featureMembership")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as FeatureMembership }
                ?: emptyList()
        }

    override val inheritedFeature: List<Feature>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "inheritedFeature")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Feature }
                ?: emptyList()
        }

    override val inheritedMembership: List<Membership>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "inheritedMembership")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Membership }
                ?: emptyList()
        }

    override val input: List<Feature>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "input")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Feature }
                ?: emptyList()
        }

    override val intersectedType: Set<Type>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "intersectedType")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Type }?.toSet()
                ?: emptySet()
        }

    override val intersectingType: List<Type>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "intersectingType")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Type }
                ?: emptyList()
        }

    override val multiplicity: Multiplicity?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "multiplicity")
            return (rawValue as? MDMObject)?.let { Wrappers.wrap(it, engine) as Multiplicity }
        }

    override val output: List<Feature>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "output")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Feature }
                ?: emptyList()
        }

    override val ownedConjugator: Conjugation?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedConjugator")
            return (rawValue as? MDMObject)?.let { Wrappers.wrap(it, engine) as Conjugation }
        }

    override val ownedDifferencing: List<Differencing>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedDifferencing")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Differencing }
                ?: emptyList()
        }

    override val ownedDisjoining: Set<Disjoining>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedDisjoining")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Disjoining }?.toSet()
                ?: emptySet()
        }

    override val ownedEndFeature: List<Feature>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedEndFeature")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Feature }
                ?: emptyList()
        }

    override val ownedFeature: List<Feature>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedFeature")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Feature }
                ?: emptyList()
        }

    override val ownedFeatureMembership: List<FeatureMembership>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedFeatureMembership")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as FeatureMembership }
                ?: emptyList()
        }

    override val ownedIntersecting: List<Intersecting>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedIntersecting")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Intersecting }
                ?: emptyList()
        }

    override val ownedSpecialization: List<Specialization>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedSpecialization")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Specialization }
                ?: emptyList()
        }

    override val ownedUnioning: List<Unioning>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedUnioning")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Unioning }
                ?: emptyList()
        }

    override var specialization: Set<Specialization>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "specialization")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Specialization }?.toSet()
                ?: emptySet()
        }
        set(value) {
            val rawValue = value.map { (it as BaseModelElementImpl).wrapped }
            engine.setProperty(wrapped.id!!, "specialization", rawValue)
        }

    override val unioningType: List<Type>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "unioningType")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Type }
                ?: emptyList()
        }

    override fun allRedefinedFeaturesOf(membership: Membership): List<Feature> {
        val result = engine.invokeOperation(wrapped.id!!, "allRedefinedFeaturesOf", mapOf("membership" to membership))
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Feature }
            ?: emptyList()
    }

    override fun allSupertypes(): List<Type> {
        val result = engine.invokeOperation(wrapped.id!!, "allSupertypes")
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Type }
            ?: emptyList()
    }

    override fun directionOf(feature: Feature): String? {
        val result = engine.invokeOperation(wrapped.id!!, "directionOf", mapOf("feature" to feature))
        return result as? String
    }

    override fun directionOfExcluding(feature: Feature, excluded: List<Type>): String? {
        val result = engine.invokeOperation(wrapped.id!!, "directionOfExcluding", mapOf("feature" to feature, "excluded" to excluded))
        return result as? String
    }

    override fun inheritableMemberships(excludedNamespaces: List<Namespace>, excludedTypes: List<Type>, excludeImplied: Boolean): List<Membership> {
        val result = engine.invokeOperation(wrapped.id!!, "inheritableMemberships", mapOf("excludedNamespaces" to excludedNamespaces, "excludedTypes" to excludedTypes, "excludeImplied" to excludeImplied))
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Membership }
            ?: emptyList()
    }

    override fun inheritedMemberships(excludedNamespaces: List<Namespace>, excludedTypes: List<Type>, excludeImplied: Boolean): List<Membership> {
        val result = engine.invokeOperation(wrapped.id!!, "inheritedMemberships", mapOf("excludedNamespaces" to excludedNamespaces, "excludedTypes" to excludedTypes, "excludeImplied" to excludeImplied))
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Membership }
            ?: emptyList()
    }

    override fun isCompatibleWith(otherType: Type): Boolean {
        val result = engine.invokeOperation(wrapped.id!!, "isCompatibleWith", mapOf("otherType" to otherType))
        return (result as? Boolean) ?: false
    }

    override fun multiplicities(): List<Multiplicity> {
        val result = engine.invokeOperation(wrapped.id!!, "multiplicities")
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Multiplicity }
            ?: emptyList()
    }

    override fun nonPrivateMemberships(excludedNamespaces: List<Namespace>, excludedTypes: List<Type>, excludeImplied: Boolean): List<Membership> {
        val result = engine.invokeOperation(wrapped.id!!, "nonPrivateMemberships", mapOf("excludedNamespaces" to excludedNamespaces, "excludedTypes" to excludedTypes, "excludeImplied" to excludeImplied))
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Membership }
            ?: emptyList()
    }

    override fun removeRedefinedFeatures(memberships: List<Membership>): List<Membership> {
        val result = engine.invokeOperation(wrapped.id!!, "removeRedefinedFeatures", mapOf("memberships" to memberships))
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Membership }
            ?: emptyList()
    }

    override fun specializes(supertype: Type): Boolean {
        val result = engine.invokeOperation(wrapped.id!!, "specializes", mapOf("supertype" to supertype))
        return (result as? Boolean) ?: false
    }

    override fun specializesFromLibrary(libraryTypeName: String): Boolean {
        val result = engine.invokeOperation(wrapped.id!!, "specializesFromLibrary", mapOf("libraryTypeName" to libraryTypeName))
        return (result as? Boolean) ?: false
    }

    override fun supertypes(excludeImplied: Boolean): List<Type> {
        val result = engine.invokeOperation(wrapped.id!!, "supertypes", mapOf("excludeImplied" to excludeImplied))
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Type }
            ?: emptyList()
    }

    override fun visibleMemberships(excluded: List<Namespace>, isRecursive: Boolean, includeAll: Boolean): List<Membership> {
        val result = engine.invokeOperation(wrapped.id!!, "visibleMemberships", mapOf("excluded" to excluded, "isRecursive" to isRecursive, "includeAll" to includeAll))
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Membership }
            ?: emptyList()
    }
}

