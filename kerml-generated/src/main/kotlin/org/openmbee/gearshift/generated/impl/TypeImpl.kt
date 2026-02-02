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
 * Implementation of Type.
 * A namespace that can be specialized
 */
open class TypeImpl(
    className: String,
    metaClass: FrameworkMetaClass,
    engine: MDMEngine
) : NamespaceImpl(className, metaClass, engine), Type {

    /**
     * Create a new Type instance.
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
    ) : this("Type", engine.schema.getClass("Type")!!, engine) {
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
            val resolved = resolver.resolve(owner.className, "Type")
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


    override var isAbstract: Boolean
        get() {
            val rawValue = getProperty("isAbstract")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(id!!, "isAbstract", value)
        }

    override var isSufficient: Boolean
        get() {
            val rawValue = getProperty("isSufficient")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(id!!, "isSufficient", value)
        }

    override val differencedType: Set<Type>
        get() {
            val rawValue = engine.getProperty(id!!, "differencedType")
            return (rawValue as? List<*>)?.filterIsInstance<Type>()?.toSet() ?: emptySet()
        }

    override val differencingType: List<Type>
        get() {
            val rawValue = engine.getProperty(id!!, "differencingType")
            return (rawValue as? List<*>)?.filterIsInstance<Type>() ?: emptyList()
        }

    override val directedFeature: List<Feature>
        get() {
            val rawValue = engine.getProperty(id!!, "directedFeature")
            return (rawValue as? List<*>)?.filterIsInstance<Feature>() ?: emptyList()
        }

    override val endFeature: List<Feature>
        get() {
            val rawValue = engine.getProperty(id!!, "endFeature")
            return (rawValue as? List<*>)?.filterIsInstance<Feature>() ?: emptyList()
        }

    override val feature: List<Feature>
        get() {
            val rawValue = engine.getProperty(id!!, "feature")
            return (rawValue as? List<*>)?.filterIsInstance<Feature>() ?: emptyList()
        }

    override val featureMembership: List<FeatureMembership>
        get() {
            val rawValue = engine.getProperty(id!!, "featureMembership")
            return (rawValue as? List<*>)?.filterIsInstance<FeatureMembership>() ?: emptyList()
        }

    override val inheritedFeature: List<Feature>
        get() {
            val rawValue = engine.getProperty(id!!, "inheritedFeature")
            return (rawValue as? List<*>)?.filterIsInstance<Feature>() ?: emptyList()
        }

    override val inheritedMembership: List<Membership>
        get() {
            val rawValue = engine.getProperty(id!!, "inheritedMembership")
            return (rawValue as? List<*>)?.filterIsInstance<Membership>() ?: emptyList()
        }

    override val input: List<Feature>
        get() {
            val rawValue = engine.getProperty(id!!, "input")
            return (rawValue as? List<*>)?.filterIsInstance<Feature>() ?: emptyList()
        }

    override val intersectedType: Set<Type>
        get() {
            val rawValue = engine.getProperty(id!!, "intersectedType")
            return (rawValue as? List<*>)?.filterIsInstance<Type>()?.toSet() ?: emptySet()
        }

    override val intersectingType: List<Type>
        get() {
            val rawValue = engine.getProperty(id!!, "intersectingType")
            return (rawValue as? List<*>)?.filterIsInstance<Type>() ?: emptyList()
        }

    override val multiplicity: Multiplicity?
        get() {
            val rawValue = engine.getProperty(id!!, "multiplicity")
            return rawValue as? Multiplicity
        }

    override val output: List<Feature>
        get() {
            val rawValue = engine.getProperty(id!!, "output")
            return (rawValue as? List<*>)?.filterIsInstance<Feature>() ?: emptyList()
        }

    override val ownedConjugator: Conjugation?
        get() {
            val rawValue = engine.getProperty(id!!, "ownedConjugator")
            return rawValue as? Conjugation
        }

    override val ownedDifferencing: List<Differencing>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedDifferencing")
            return (rawValue as? List<*>)?.filterIsInstance<Differencing>() ?: emptyList()
        }

    override val ownedDisjoining: Set<Disjoining>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedDisjoining")
            return (rawValue as? List<*>)?.filterIsInstance<Disjoining>()?.toSet() ?: emptySet()
        }

    override val ownedEndFeature: List<Feature>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedEndFeature")
            return (rawValue as? List<*>)?.filterIsInstance<Feature>() ?: emptyList()
        }

    override val ownedFeature: List<Feature>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedFeature")
            return (rawValue as? List<*>)?.filterIsInstance<Feature>() ?: emptyList()
        }

    override val ownedFeatureMembership: List<FeatureMembership>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedFeatureMembership")
            return (rawValue as? List<*>)?.filterIsInstance<FeatureMembership>() ?: emptyList()
        }

    override val ownedIntersecting: List<Intersecting>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedIntersecting")
            return (rawValue as? List<*>)?.filterIsInstance<Intersecting>() ?: emptyList()
        }

    override val ownedSpecialization: List<Specialization>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedSpecialization")
            return (rawValue as? List<*>)?.filterIsInstance<Specialization>() ?: emptyList()
        }

    override val ownedUnioning: List<Unioning>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedUnioning")
            return (rawValue as? List<*>)?.filterIsInstance<Unioning>() ?: emptyList()
        }

    override var specialization: Set<Specialization>
        get() {
            val rawValue = engine.getProperty(id!!, "specialization")
            return (rawValue as? List<*>)?.filterIsInstance<Specialization>()?.toSet() ?: emptySet()
        }
        set(value) {
            engine.setProperty(id!!, "specialization", value)
        }

    override val unioningType: List<Type>
        get() {
            val rawValue = engine.getProperty(id!!, "unioningType")
            return (rawValue as? List<*>)?.filterIsInstance<Type>() ?: emptyList()
        }

    override fun allRedefinedFeaturesOf(membership: Membership): List<Feature> {
        val result = engine.invokeOperation(id!!, "allRedefinedFeaturesOf", mapOf("membership" to membership))
        return (result as? List<*>)?.filterIsInstance<Feature>() ?: emptyList()
    }

    override fun allSupertypes(): List<Type> {
        val result = engine.invokeOperation(id!!, "allSupertypes")
        return (result as? List<*>)?.filterIsInstance<Type>() ?: emptyList()
    }

    override fun directionOf(feature: Feature): String? {
        val result = engine.invokeOperation(id!!, "directionOf", mapOf("feature" to feature))
        return result as? String
    }

    override fun directionOfExcluding(feature: Feature, excluded: List<Type>): String? {
        val result = engine.invokeOperation(id!!, "directionOfExcluding", mapOf("feature" to feature, "excluded" to excluded))
        return result as? String
    }

    override fun inheritableMemberships(excludedNamespaces: List<Namespace>, excludedTypes: List<Type>, excludeImplied: Boolean): List<Membership> {
        val result = engine.invokeOperation(id!!, "inheritableMemberships", mapOf("excludedNamespaces" to excludedNamespaces, "excludedTypes" to excludedTypes, "excludeImplied" to excludeImplied))
        return (result as? List<*>)?.filterIsInstance<Membership>() ?: emptyList()
    }

    override fun inheritedMemberships(excludedNamespaces: List<Namespace>, excludedTypes: List<Type>, excludeImplied: Boolean): List<Membership> {
        val result = engine.invokeOperation(id!!, "inheritedMemberships", mapOf("excludedNamespaces" to excludedNamespaces, "excludedTypes" to excludedTypes, "excludeImplied" to excludeImplied))
        return (result as? List<*>)?.filterIsInstance<Membership>() ?: emptyList()
    }

    override fun isCompatibleWith(otherType: Type): Boolean {
        val result = engine.invokeOperation(id!!, "isCompatibleWith", mapOf("otherType" to otherType))
        return (result as? Boolean) ?: false
    }

    override fun multiplicities(): List<Multiplicity> {
        val result = engine.invokeOperation(id!!, "multiplicities")
        return (result as? List<*>)?.filterIsInstance<Multiplicity>() ?: emptyList()
    }

    override fun nonPrivateMemberships(excludedNamespaces: List<Namespace>, excludedTypes: List<Type>, excludeImplied: Boolean): List<Membership> {
        val result = engine.invokeOperation(id!!, "nonPrivateMemberships", mapOf("excludedNamespaces" to excludedNamespaces, "excludedTypes" to excludedTypes, "excludeImplied" to excludeImplied))
        return (result as? List<*>)?.filterIsInstance<Membership>() ?: emptyList()
    }

    override fun removeRedefinedFeatures(memberships: List<Membership>): List<Membership> {
        val result = engine.invokeOperation(id!!, "removeRedefinedFeatures", mapOf("memberships" to memberships))
        return (result as? List<*>)?.filterIsInstance<Membership>() ?: emptyList()
    }

    override fun specializes(supertype: Type): Boolean {
        val result = engine.invokeOperation(id!!, "specializes", mapOf("supertype" to supertype))
        return (result as? Boolean) ?: false
    }

    override fun specializesFromLibrary(libraryTypeName: String): Boolean {
        val result = engine.invokeOperation(id!!, "specializesFromLibrary", mapOf("libraryTypeName" to libraryTypeName))
        return (result as? Boolean) ?: false
    }

    override fun supertypes(excludeImplied: Boolean): List<Type> {
        val result = engine.invokeOperation(id!!, "supertypes", mapOf("excludeImplied" to excludeImplied))
        return (result as? List<*>)?.filterIsInstance<Type>() ?: emptyList()
    }

    override fun visibleMemberships(excluded: List<Namespace>, isRecursive: Boolean, includeAll: Boolean): List<Membership> {
        val result = engine.invokeOperation(id!!, "visibleMemberships", mapOf("excluded" to excluded, "isRecursive" to isRecursive, "includeAll" to includeAll))
        return (result as? List<*>)?.filterIsInstance<Membership>() ?: emptyList()
    }
}

