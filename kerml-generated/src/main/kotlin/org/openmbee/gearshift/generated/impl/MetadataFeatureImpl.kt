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
 * Implementation of MetadataFeature.
 * A MetadataFeature is a Feature that is an AnnotatingElement used to annotate another Element with metadata. It is typed by a Metaclass.
 */
open class MetadataFeatureImpl(
    className: String,
    metaClass: FrameworkMetaClass,
    engine: MDMEngine
) : AnnotatingElementImpl(className, metaClass, engine), MetadataFeature {

    /**
     * Create a new MetadataFeature instance.
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
    ) : this("MetadataFeature", engine.schema.getClass("MetadataFeature")!!, engine) {
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
            val resolved = resolver.resolve(owner.className, "MetadataFeature")
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


    override var direction: String?
        get() {
            val rawValue = getProperty("direction")
            return rawValue as? String
        }
        set(value) {
            engine.setProperty(id!!, "direction", value)
        }

    override var isAbstract: Boolean
        get() {
            val rawValue = getProperty("isAbstract")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(id!!, "isAbstract", value)
        }

    override var isComposite: Boolean
        get() {
            val rawValue = getProperty("isComposite")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(id!!, "isComposite", value)
        }

    override var isConstant: Boolean
        get() {
            val rawValue = getProperty("isConstant")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(id!!, "isConstant", value)
        }

    override var isDerived: Boolean
        get() {
            val rawValue = getProperty("isDerived")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(id!!, "isDerived", value)
        }

    override var isEnd: Boolean
        get() {
            val rawValue = getProperty("isEnd")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(id!!, "isEnd", value)
        }

    override var isNonunique: Boolean
        get() {
            val rawValue = getProperty("isNonunique")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(id!!, "isNonunique", value)
        }

    override var isOrdered: Boolean
        get() {
            val rawValue = getProperty("isOrdered")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(id!!, "isOrdered", value)
        }

    override var isPortion: Boolean
        get() {
            val rawValue = getProperty("isPortion")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(id!!, "isPortion", value)
        }

    override var isReadOnly: Boolean
        get() {
            val rawValue = getProperty("isReadOnly")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(id!!, "isReadOnly", value)
        }

    override var isSufficient: Boolean
        get() {
            val rawValue = getProperty("isSufficient")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(id!!, "isSufficient", value)
        }

    override var isUnique: Boolean
        get() {
            val rawValue = getProperty("isUnique")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(id!!, "isUnique", value)
        }

    override var isVariable: Boolean
        get() {
            val rawValue = getProperty("isVariable")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(id!!, "isVariable", value)
        }

    override val metaclass: Metaclass?
        get() {
            val rawValue = engine.getProperty(id!!, "metaclass")
            return rawValue as? Metaclass
        }

    override val chainingFeature: List<Feature>
        get() {
            val rawValue = engine.getProperty(id!!, "chainingFeature")
            return (rawValue as? List<*>)?.filterIsInstance<Feature>() ?: emptyList()
        }

    override val crossFeature: Feature?
        get() {
            val rawValue = engine.getProperty(id!!, "crossFeature")
            return rawValue as? Feature
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

    override val endOwningType: Type?
        get() {
            val rawValue = engine.getProperty(id!!, "endOwningType")
            return rawValue as? Type
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

    override val featuringType: List<Type>
        get() {
            val rawValue = engine.getProperty(id!!, "featuringType")
            return (rawValue as? List<*>)?.filterIsInstance<Type>() ?: emptyList()
        }

    override val importedMembership: List<Membership>
        get() {
            val rawValue = engine.getProperty(id!!, "importedMembership")
            return (rawValue as? List<*>)?.filterIsInstance<Membership>() ?: emptyList()
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

    override var invertingFeatureInverting: Set<FeatureInverting>
        get() {
            val rawValue = engine.getProperty(id!!, "invertingFeatureInverting")
            return (rawValue as? List<*>)?.filterIsInstance<FeatureInverting>()?.toSet() ?: emptySet()
        }
        set(value) {
            engine.setProperty(id!!, "invertingFeatureInverting", value)
        }

    override val member: List<Element>
        get() {
            val rawValue = engine.getProperty(id!!, "member")
            return (rawValue as? List<*>)?.filterIsInstance<Element>() ?: emptyList()
        }

    override val membership: List<Membership>
        get() {
            val rawValue = engine.getProperty(id!!, "membership")
            return (rawValue as? List<*>)?.filterIsInstance<Membership>() ?: emptyList()
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

    override val ownedCrossSubsetting: CrossSubsetting?
        get() {
            val rawValue = engine.getProperty(id!!, "ownedCrossSubsetting")
            return rawValue as? CrossSubsetting
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

    override val ownedFeatureChaining: List<FeatureChaining>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedFeatureChaining")
            return (rawValue as? List<*>)?.filterIsInstance<FeatureChaining>() ?: emptyList()
        }

    override val ownedFeatureInverting: Set<FeatureInverting>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedFeatureInverting")
            return (rawValue as? List<*>)?.filterIsInstance<FeatureInverting>()?.toSet() ?: emptySet()
        }

    override val ownedFeatureMembership: List<FeatureMembership>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedFeatureMembership")
            return (rawValue as? List<*>)?.filterIsInstance<FeatureMembership>() ?: emptyList()
        }

    override var ownedImport: List<Import>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedImport")
            return (rawValue as? List<*>)?.filterIsInstance<Import>() ?: emptyList()
        }
        set(value) {
            engine.setProperty(id!!, "ownedImport", value)
        }

    override val ownedIntersecting: List<Intersecting>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedIntersecting")
            return (rawValue as? List<*>)?.filterIsInstance<Intersecting>() ?: emptyList()
        }

    override val ownedMember: List<Element>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedMember")
            return (rawValue as? List<*>)?.filterIsInstance<Element>() ?: emptyList()
        }

    override var ownedMembership: List<Membership>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedMembership")
            return (rawValue as? List<*>)?.filterIsInstance<Membership>() ?: emptyList()
        }
        set(value) {
            engine.setProperty(id!!, "ownedMembership", value)
        }

    override val ownedRedefinition: Set<Redefinition>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedRedefinition")
            return (rawValue as? List<*>)?.filterIsInstance<Redefinition>()?.toSet() ?: emptySet()
        }

    override val ownedReferenceSubsetting: ReferenceSubsetting?
        get() {
            val rawValue = engine.getProperty(id!!, "ownedReferenceSubsetting")
            return rawValue as? ReferenceSubsetting
        }

    override val ownedSpecialization: List<Specialization>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedSpecialization")
            return (rawValue as? List<*>)?.filterIsInstance<Specialization>() ?: emptyList()
        }

    override val ownedSubsetting: Set<Subsetting>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedSubsetting")
            return (rawValue as? List<*>)?.filterIsInstance<Subsetting>()?.toSet() ?: emptySet()
        }

    override val ownedTypeFeaturing: List<TypeFeaturing>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedTypeFeaturing")
            return (rawValue as? List<*>)?.filterIsInstance<TypeFeaturing>() ?: emptyList()
        }

    override val ownedTyping: List<FeatureTyping>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedTyping")
            return (rawValue as? List<*>)?.filterIsInstance<FeatureTyping>() ?: emptyList()
        }

    override val ownedUnioning: List<Unioning>
        get() {
            val rawValue = engine.getProperty(id!!, "ownedUnioning")
            return (rawValue as? List<*>)?.filterIsInstance<Unioning>() ?: emptyList()
        }

    override val owningFeatureMembership: FeatureMembership?
        get() {
            val rawValue = engine.getProperty(id!!, "owningFeatureMembership")
            return rawValue as? FeatureMembership
        }

    override val owningType: Type?
        get() {
            val rawValue = engine.getProperty(id!!, "owningType")
            return rawValue as? Type
        }

    override var redefinition: Set<Redefinition>
        get() {
            val rawValue = engine.getProperty(id!!, "redefinition")
            return (rawValue as? List<*>)?.filterIsInstance<Redefinition>()?.toSet() ?: emptySet()
        }
        set(value) {
            engine.setProperty(id!!, "redefinition", value)
        }

    override var specialization: Set<Specialization>
        get() {
            val rawValue = engine.getProperty(id!!, "specialization")
            return (rawValue as? List<*>)?.filterIsInstance<Specialization>()?.toSet() ?: emptySet()
        }
        set(value) {
            engine.setProperty(id!!, "specialization", value)
        }

    override var subsetting: Set<Subsetting>
        get() {
            val rawValue = engine.getProperty(id!!, "subsetting")
            return (rawValue as? List<*>)?.filterIsInstance<Subsetting>()?.toSet() ?: emptySet()
        }
        set(value) {
            engine.setProperty(id!!, "subsetting", value)
        }

    override val type: List<Type>
        get() {
            val rawValue = engine.getProperty(id!!, "type")
            return (rawValue as? List<*>)?.filterIsInstance<Type>() ?: emptyList()
        }

    override var typing: Set<FeatureTyping>
        get() {
            val rawValue = engine.getProperty(id!!, "typing")
            return (rawValue as? List<*>)?.filterIsInstance<FeatureTyping>()?.toSet() ?: emptySet()
        }
        set(value) {
            engine.setProperty(id!!, "typing", value)
        }

    override val unioningType: List<Type>
        get() {
            val rawValue = engine.getProperty(id!!, "unioningType")
            return (rawValue as? List<*>)?.filterIsInstance<Type>() ?: emptyList()
        }

    override fun evaluateFeature(baseFeature: Feature): List<Element> {
        val result = engine.invokeOperation(id!!, "evaluateFeature", mapOf("baseFeature" to baseFeature))
        return (result as? List<*>)?.filterIsInstance<Element>() ?: emptyList()
    }

    override fun isSemantic(): Boolean? {
        val result = engine.invokeOperation(id!!, "isSemantic")
        return result as? Boolean
    }

    override fun isSyntactic(): Boolean? {
        val result = engine.invokeOperation(id!!, "isSyntactic")
        return result as? Boolean
    }

    override fun syntaxElement(): Element? {
        val result = engine.invokeOperation(id!!, "syntaxElement")
        return result as? Element
    }

    override fun allRedefinedFeatures(): List<Feature> {
        val result = engine.invokeOperation(id!!, "allRedefinedFeatures")
        return (result as? List<*>)?.filterIsInstance<Feature>() ?: emptyList()
    }

    override fun allRedefinedFeaturesOf(membership: Membership): List<Feature> {
        val result = engine.invokeOperation(id!!, "allRedefinedFeaturesOf", mapOf("membership" to membership))
        return (result as? List<*>)?.filterIsInstance<Feature>() ?: emptyList()
    }

    override fun allSupertypes(): List<Type> {
        val result = engine.invokeOperation(id!!, "allSupertypes")
        return (result as? List<*>)?.filterIsInstance<Type>() ?: emptyList()
    }

    override fun asCartesianProduct(): List<Type> {
        val result = engine.invokeOperation(id!!, "asCartesianProduct")
        return (result as? List<*>)?.filterIsInstance<Type>() ?: emptyList()
    }

    override fun canAccess(feature: Feature): Boolean? {
        val result = engine.invokeOperation(id!!, "canAccess", mapOf("feature" to feature))
        return result as? Boolean
    }

    override fun directionFor(type: Type): String? {
        val result = engine.invokeOperation(id!!, "directionFor", mapOf("type" to type))
        return result as? String
    }

    override fun directionOf(feature: Feature): String? {
        val result = engine.invokeOperation(id!!, "directionOf", mapOf("feature" to feature))
        return result as? String
    }

    override fun directionOfExcluding(feature: Feature, excluded: List<Type>): String? {
        val result = engine.invokeOperation(id!!, "directionOfExcluding", mapOf("feature" to feature, "excluded" to excluded))
        return result as? String
    }

    override fun importedMemberships(excluded: List<Namespace>): List<Membership> {
        val result = engine.invokeOperation(id!!, "importedMemberships", mapOf("excluded" to excluded))
        return (result as? List<*>)?.filterIsInstance<Membership>() ?: emptyList()
    }

    override fun inheritableMemberships(excludedNamespaces: List<Namespace>, excludedTypes: List<Type>, excludeImplied: Boolean): List<Membership> {
        val result = engine.invokeOperation(id!!, "inheritableMemberships", mapOf("excludedNamespaces" to excludedNamespaces, "excludedTypes" to excludedTypes, "excludeImplied" to excludeImplied))
        return (result as? List<*>)?.filterIsInstance<Membership>() ?: emptyList()
    }

    override fun inheritedMemberships(excludedNamespaces: List<Namespace>, excludedTypes: List<Type>, excludeImplied: Boolean): List<Membership> {
        val result = engine.invokeOperation(id!!, "inheritedMemberships", mapOf("excludedNamespaces" to excludedNamespaces, "excludedTypes" to excludedTypes, "excludeImplied" to excludeImplied))
        return (result as? List<*>)?.filterIsInstance<Membership>() ?: emptyList()
    }

    override fun isCartesianProduct(): Boolean? {
        val result = engine.invokeOperation(id!!, "isCartesianProduct")
        return result as? Boolean
    }

    override fun isCompatibleWith(otherType: Type): Boolean {
        val result = engine.invokeOperation(id!!, "isCompatibleWith", mapOf("otherType" to otherType))
        return (result as? Boolean) ?: false
    }

    override fun isFeaturedWithin(type: Type?): Boolean? {
        val result = engine.invokeOperation(id!!, "isFeaturedWithin", mapOf("type" to type))
        return result as? Boolean
    }

    override fun isFeaturingType(type: Type): Boolean? {
        val result = engine.invokeOperation(id!!, "isFeaturingType", mapOf("type" to type))
        return result as? Boolean
    }

    override fun isOwnedCrossFeature(): Boolean? {
        val result = engine.invokeOperation(id!!, "isOwnedCrossFeature")
        return result as? Boolean
    }

    override fun membershipsOfVisibility(visibility: String?, excluded: List<Namespace>): List<Membership> {
        val result = engine.invokeOperation(id!!, "membershipsOfVisibility", mapOf("visibility" to visibility, "excluded" to excluded))
        return (result as? List<*>)?.filterIsInstance<Membership>() ?: emptyList()
    }

    override fun multiplicities(): List<Multiplicity> {
        val result = engine.invokeOperation(id!!, "multiplicities")
        return (result as? List<*>)?.filterIsInstance<Multiplicity>() ?: emptyList()
    }

    override fun namesOf(element: Element): List<String> {
        val result = engine.invokeOperation(id!!, "namesOf", mapOf("element" to element))
        @Suppress("UNCHECKED_CAST")
        return (result as? List<String>) ?: emptyList()
    }

    override fun namingFeature(): Feature? {
        val result = engine.invokeOperation(id!!, "namingFeature")
        return result as? Feature
    }

    override fun nonPrivateMemberships(excludedNamespaces: List<Namespace>, excludedTypes: List<Type>, excludeImplied: Boolean): List<Membership> {
        val result = engine.invokeOperation(id!!, "nonPrivateMemberships", mapOf("excludedNamespaces" to excludedNamespaces, "excludedTypes" to excludedTypes, "excludeImplied" to excludeImplied))
        return (result as? List<*>)?.filterIsInstance<Membership>() ?: emptyList()
    }

    override fun ownedCrossFeature(): Feature? {
        val result = engine.invokeOperation(id!!, "ownedCrossFeature")
        return result as? Feature
    }

    override fun qualificationOf(qualifiedName: String): String? {
        val result = engine.invokeOperation(id!!, "qualificationOf", mapOf("qualifiedName" to qualifiedName))
        return result as? String
    }

    override fun redefines(redefinedFeature: Feature): Boolean? {
        val result = engine.invokeOperation(id!!, "redefines", mapOf("redefinedFeature" to redefinedFeature))
        return result as? Boolean
    }

    override fun redefinesFromLibrary(libraryFeatureName: String): Boolean? {
        val result = engine.invokeOperation(id!!, "redefinesFromLibrary", mapOf("libraryFeatureName" to libraryFeatureName))
        return result as? Boolean
    }

    override fun removeRedefinedFeatures(memberships: List<Membership>): List<Membership> {
        val result = engine.invokeOperation(id!!, "removeRedefinedFeatures", mapOf("memberships" to memberships))
        return (result as? List<*>)?.filterIsInstance<Membership>() ?: emptyList()
    }

    override fun resolve(qualifiedName: String): Membership? {
        val result = engine.invokeOperation(id!!, "resolve", mapOf("qualifiedName" to qualifiedName))
        return result as? Membership
    }

    override fun resolveGlobal(qualifiedName: String): Membership? {
        val result = engine.invokeOperation(id!!, "resolveGlobal", mapOf("qualifiedName" to qualifiedName))
        return result as? Membership
    }

    override fun resolveLocal(name: String): Membership? {
        val result = engine.invokeOperation(id!!, "resolveLocal", mapOf("name" to name))
        return result as? Membership
    }

    override fun resolveVisible(name: String): Membership? {
        val result = engine.invokeOperation(id!!, "resolveVisible", mapOf("name" to name))
        return result as? Membership
    }

    override fun specializes(supertype: Type): Boolean {
        val result = engine.invokeOperation(id!!, "specializes", mapOf("supertype" to supertype))
        return (result as? Boolean) ?: false
    }

    override fun specializesFromLibrary(libraryTypeName: String): Boolean {
        val result = engine.invokeOperation(id!!, "specializesFromLibrary", mapOf("libraryTypeName" to libraryTypeName))
        return (result as? Boolean) ?: false
    }

    override fun subsetsChain(first: Feature, second: Feature): Boolean? {
        val result = engine.invokeOperation(id!!, "subsetsChain", mapOf("first" to first, "second" to second))
        return result as? Boolean
    }

    override fun supertypes(excludeImplied: Boolean): List<Type> {
        val result = engine.invokeOperation(id!!, "supertypes", mapOf("excludeImplied" to excludeImplied))
        return (result as? List<*>)?.filterIsInstance<Type>() ?: emptyList()
    }

    override fun typingFeatures(): List<Feature> {
        val result = engine.invokeOperation(id!!, "typingFeatures")
        return (result as? List<*>)?.filterIsInstance<Feature>() ?: emptyList()
    }

    override fun unqualifiedNameOf(qualifiedName: String): String {
        val result = engine.invokeOperation(id!!, "unqualifiedNameOf", mapOf("qualifiedName" to qualifiedName))
        return (result as? String) ?: ""
    }

    override fun visibilityOf(mem: Membership): String {
        val result = engine.invokeOperation(id!!, "visibilityOf", mapOf("mem" to mem))
        return (result as? String) ?: ""
    }

    override fun visibleMemberships(excluded: List<Namespace>, isRecursive: Boolean, includeAll: Boolean): List<Membership> {
        val result = engine.invokeOperation(id!!, "visibleMemberships", mapOf("excluded" to excluded, "isRecursive" to isRecursive, "includeAll" to includeAll))
        return (result as? List<*>)?.filterIsInstance<Membership>() ?: emptyList()
    }
}

