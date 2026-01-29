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
 * Implementation of MetadataFeature.
 * A MetadataFeature is a Feature that is an AnnotatingElement used to annotate another Element with metadata. It is typed by a Metaclass.
 */
open class MetadataFeatureImpl(
    wrapped: MDMObject,
    engine: MDMEngine
) : AnnotatingElementImpl(wrapped, engine), MetadataFeature {

    override var direction: String?
        get() {
            val rawValue = wrapped.getProperty("direction")
            return rawValue as? String
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "direction", value)
        }

    override var isAbstract: Boolean
        get() {
            val rawValue = wrapped.getProperty("isAbstract")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "isAbstract", value)
        }

    override var isComposite: Boolean
        get() {
            val rawValue = wrapped.getProperty("isComposite")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "isComposite", value)
        }

    override var isConstant: Boolean
        get() {
            val rawValue = wrapped.getProperty("isConstant")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "isConstant", value)
        }

    override var isDerived: Boolean
        get() {
            val rawValue = wrapped.getProperty("isDerived")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "isDerived", value)
        }

    override var isEnd: Boolean
        get() {
            val rawValue = wrapped.getProperty("isEnd")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "isEnd", value)
        }

    override var isNonunique: Boolean
        get() {
            val rawValue = wrapped.getProperty("isNonunique")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "isNonunique", value)
        }

    override var isOrdered: Boolean
        get() {
            val rawValue = wrapped.getProperty("isOrdered")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "isOrdered", value)
        }

    override var isPortion: Boolean
        get() {
            val rawValue = wrapped.getProperty("isPortion")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "isPortion", value)
        }

    override var isReadOnly: Boolean
        get() {
            val rawValue = wrapped.getProperty("isReadOnly")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "isReadOnly", value)
        }

    override var isSufficient: Boolean
        get() {
            val rawValue = wrapped.getProperty("isSufficient")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "isSufficient", value)
        }

    override var isUnique: Boolean
        get() {
            val rawValue = wrapped.getProperty("isUnique")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "isUnique", value)
        }

    override var isVariable: Boolean
        get() {
            val rawValue = wrapped.getProperty("isVariable")
            return (rawValue as? Boolean) ?: false
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "isVariable", value)
        }

    override val metaclass: Metaclass?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "metaclass")
            return (rawValue as? MDMObject)?.let { Wrappers.wrap(it, engine) as Metaclass }
        }

    override val chainingFeature: List<Feature>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "chainingFeature")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Feature }
                ?: emptyList()
        }

    override val crossFeature: Feature?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "crossFeature")
            return (rawValue as? MDMObject)?.let { Wrappers.wrap(it, engine) as Feature }
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

    override val endOwningType: Type?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "endOwningType")
            return (rawValue as? MDMObject)?.let { Wrappers.wrap(it, engine) as Type }
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

    override val featuringType: List<Type>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "featuringType")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Type }
                ?: emptyList()
        }

    override val importedMembership: List<Membership>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "importedMembership")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Membership }
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

    override var invertingFeatureInverting: Set<FeatureInverting>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "invertingFeatureInverting")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as FeatureInverting }?.toSet()
                ?: emptySet()
        }
        set(value) {
            val rawValue = value.map { (it as BaseModelElementImpl).wrapped }
            engine.setProperty(wrapped.id!!, "invertingFeatureInverting", rawValue)
        }

    override val member: List<Element>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "member")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Element }
                ?: emptyList()
        }

    override val membership: List<Membership>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "membership")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Membership }
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

    override val ownedCrossSubsetting: CrossSubsetting?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedCrossSubsetting")
            return (rawValue as? MDMObject)?.let { Wrappers.wrap(it, engine) as CrossSubsetting }
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

    override val ownedFeatureChaining: List<FeatureChaining>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedFeatureChaining")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as FeatureChaining }
                ?: emptyList()
        }

    override val ownedFeatureInverting: Set<FeatureInverting>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedFeatureInverting")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as FeatureInverting }?.toSet()
                ?: emptySet()
        }

    override val ownedFeatureMembership: List<FeatureMembership>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedFeatureMembership")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as FeatureMembership }
                ?: emptyList()
        }

    override var ownedImport: List<Import>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedImport")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Import }
                ?: emptyList()
        }
        set(value) {
            val rawValue = value.map { (it as BaseModelElementImpl).wrapped }
            engine.setProperty(wrapped.id!!, "ownedImport", rawValue)
        }

    override val ownedIntersecting: List<Intersecting>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedIntersecting")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Intersecting }
                ?: emptyList()
        }

    override val ownedMember: List<Element>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedMember")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Element }
                ?: emptyList()
        }

    override var ownedMembership: List<Membership>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedMembership")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Membership }
                ?: emptyList()
        }
        set(value) {
            val rawValue = value.map { (it as BaseModelElementImpl).wrapped }
            engine.setProperty(wrapped.id!!, "ownedMembership", rawValue)
        }

    override val ownedRedefinition: Set<Redefinition>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedRedefinition")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Redefinition }?.toSet()
                ?: emptySet()
        }

    override val ownedReferenceSubsetting: ReferenceSubsetting?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedReferenceSubsetting")
            return (rawValue as? MDMObject)?.let { Wrappers.wrap(it, engine) as ReferenceSubsetting }
        }

    override val ownedSpecialization: List<Specialization>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedSpecialization")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Specialization }
                ?: emptyList()
        }

    override val ownedSubsetting: Set<Subsetting>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedSubsetting")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Subsetting }?.toSet()
                ?: emptySet()
        }

    override val ownedTypeFeaturing: List<TypeFeaturing>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedTypeFeaturing")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as TypeFeaturing }
                ?: emptyList()
        }

    override val ownedTyping: List<FeatureTyping>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedTyping")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as FeatureTyping }
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

    override val owningFeatureMembership: FeatureMembership?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "owningFeatureMembership")
            return (rawValue as? MDMObject)?.let { Wrappers.wrap(it, engine) as FeatureMembership }
        }

    override val owningType: Type?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "owningType")
            return (rawValue as? MDMObject)?.let { Wrappers.wrap(it, engine) as Type }
        }

    override var redefinition: Set<Redefinition>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "redefinition")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Redefinition }?.toSet()
                ?: emptySet()
        }
        set(value) {
            val rawValue = value.map { (it as BaseModelElementImpl).wrapped }
            engine.setProperty(wrapped.id!!, "redefinition", rawValue)
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

    override var subsetting: Set<Subsetting>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "subsetting")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Subsetting }?.toSet()
                ?: emptySet()
        }
        set(value) {
            val rawValue = value.map { (it as BaseModelElementImpl).wrapped }
            engine.setProperty(wrapped.id!!, "subsetting", rawValue)
        }

    override val type: List<Type>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "type")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Type }
                ?: emptyList()
        }

    override var typing: Set<FeatureTyping>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "typing")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as FeatureTyping }?.toSet()
                ?: emptySet()
        }
        set(value) {
            val rawValue = value.map { (it as BaseModelElementImpl).wrapped }
            engine.setProperty(wrapped.id!!, "typing", rawValue)
        }

    override val unioningType: List<Type>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "unioningType")
            return (rawValue as? List<*>)
                ?.filterIsInstance<MDMObject>()
                ?.map { Wrappers.wrap(it, engine) as Type }
                ?: emptyList()
        }

    override fun evaluateFeature(baseFeature: Feature): List<Element> {
        val result = engine.invokeOperation(wrapped.id!!, "evaluateFeature", mapOf("baseFeature" to baseFeature))
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Element }
            ?: emptyList()
    }

    override fun isSemantic(): Boolean? {
        val result = engine.invokeOperation(wrapped.id!!, "isSemantic")
        return result as? Boolean
    }

    override fun isSyntactic(): Boolean? {
        val result = engine.invokeOperation(wrapped.id!!, "isSyntactic")
        return result as? Boolean
    }

    override fun syntaxElement(): Element? {
        val result = engine.invokeOperation(wrapped.id!!, "syntaxElement")
        return (result as? MDMObject)?.let { Wrappers.wrap(it, engine) as Element }
    }

    override fun allRedefinedFeatures(): List<Feature> {
        val result = engine.invokeOperation(wrapped.id!!, "allRedefinedFeatures")
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Feature }
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

    override fun asCartesianProduct(): List<Type> {
        val result = engine.invokeOperation(wrapped.id!!, "asCartesianProduct")
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Type }
            ?: emptyList()
    }

    override fun canAccess(feature: Feature): Boolean? {
        val result = engine.invokeOperation(wrapped.id!!, "canAccess", mapOf("feature" to feature))
        return result as? Boolean
    }

    override fun directionFor(type: Type): String? {
        val result = engine.invokeOperation(wrapped.id!!, "directionFor", mapOf("type" to type))
        return result as? String
    }

    override fun directionOf(feature: Feature): String? {
        val result = engine.invokeOperation(wrapped.id!!, "directionOf", mapOf("feature" to feature))
        return result as? String
    }

    override fun directionOfExcluding(feature: Feature, excluded: List<Type>): String? {
        val result = engine.invokeOperation(wrapped.id!!, "directionOfExcluding", mapOf("feature" to feature, "excluded" to excluded))
        return result as? String
    }

    override fun importedMemberships(excluded: List<Namespace>): List<Membership> {
        val result = engine.invokeOperation(wrapped.id!!, "importedMemberships", mapOf("excluded" to excluded))
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Membership }
            ?: emptyList()
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

    override fun isCartesianProduct(): Boolean? {
        val result = engine.invokeOperation(wrapped.id!!, "isCartesianProduct")
        return result as? Boolean
    }

    override fun isCompatibleWith(otherType: Type): Boolean {
        val result = engine.invokeOperation(wrapped.id!!, "isCompatibleWith", mapOf("otherType" to otherType))
        return (result as? Boolean) ?: false
    }

    override fun isFeaturedWithin(type: Type?): Boolean? {
        val result = engine.invokeOperation(wrapped.id!!, "isFeaturedWithin", mapOf("type" to type))
        return result as? Boolean
    }

    override fun isFeaturingType(type: Type): Boolean? {
        val result = engine.invokeOperation(wrapped.id!!, "isFeaturingType", mapOf("type" to type))
        return result as? Boolean
    }

    override fun isOwnedCrossFeature(): Boolean? {
        val result = engine.invokeOperation(wrapped.id!!, "isOwnedCrossFeature")
        return result as? Boolean
    }

    override fun membershipsOfVisibility(visibility: String?, excluded: List<Namespace>): List<Membership> {
        val result = engine.invokeOperation(wrapped.id!!, "membershipsOfVisibility", mapOf("visibility" to visibility, "excluded" to excluded))
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Membership }
            ?: emptyList()
    }

    override fun multiplicities(): List<Multiplicity> {
        val result = engine.invokeOperation(wrapped.id!!, "multiplicities")
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Multiplicity }
            ?: emptyList()
    }

    override fun namesOf(element: Element): List<String> {
        val result = engine.invokeOperation(wrapped.id!!, "namesOf", mapOf("element" to element))
        @Suppress("UNCHECKED_CAST")
        return (result as? List<String>) ?: emptyList()
    }

    override fun namingFeature(): Feature? {
        val result = engine.invokeOperation(wrapped.id!!, "namingFeature")
        return (result as? MDMObject)?.let { Wrappers.wrap(it, engine) as Feature }
    }

    override fun nonPrivateMemberships(excludedNamespaces: List<Namespace>, excludedTypes: List<Type>, excludeImplied: Boolean): List<Membership> {
        val result = engine.invokeOperation(wrapped.id!!, "nonPrivateMemberships", mapOf("excludedNamespaces" to excludedNamespaces, "excludedTypes" to excludedTypes, "excludeImplied" to excludeImplied))
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Membership }
            ?: emptyList()
    }

    override fun ownedCrossFeature(): Feature? {
        val result = engine.invokeOperation(wrapped.id!!, "ownedCrossFeature")
        return (result as? MDMObject)?.let { Wrappers.wrap(it, engine) as Feature }
    }

    override fun qualificationOf(qualifiedName: String): String? {
        val result = engine.invokeOperation(wrapped.id!!, "qualificationOf", mapOf("qualifiedName" to qualifiedName))
        return result as? String
    }

    override fun redefines(redefinedFeature: Feature): Boolean? {
        val result = engine.invokeOperation(wrapped.id!!, "redefines", mapOf("redefinedFeature" to redefinedFeature))
        return result as? Boolean
    }

    override fun redefinesFromLibrary(libraryFeatureName: String): Boolean? {
        val result = engine.invokeOperation(wrapped.id!!, "redefinesFromLibrary", mapOf("libraryFeatureName" to libraryFeatureName))
        return result as? Boolean
    }

    override fun removeRedefinedFeatures(memberships: List<Membership>): List<Membership> {
        val result = engine.invokeOperation(wrapped.id!!, "removeRedefinedFeatures", mapOf("memberships" to memberships))
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Membership }
            ?: emptyList()
    }

    override fun resolve(qualifiedName: String): Membership? {
        val result = engine.invokeOperation(wrapped.id!!, "resolve", mapOf("qualifiedName" to qualifiedName))
        return (result as? MDMObject)?.let { Wrappers.wrap(it, engine) as Membership }
    }

    override fun resolveGlobal(qualifiedName: String): Membership? {
        val result = engine.invokeOperation(wrapped.id!!, "resolveGlobal", mapOf("qualifiedName" to qualifiedName))
        return (result as? MDMObject)?.let { Wrappers.wrap(it, engine) as Membership }
    }

    override fun resolveLocal(name: String): Membership? {
        val result = engine.invokeOperation(wrapped.id!!, "resolveLocal", mapOf("name" to name))
        return (result as? MDMObject)?.let { Wrappers.wrap(it, engine) as Membership }
    }

    override fun resolveVisible(name: String): Membership? {
        val result = engine.invokeOperation(wrapped.id!!, "resolveVisible", mapOf("name" to name))
        return (result as? MDMObject)?.let { Wrappers.wrap(it, engine) as Membership }
    }

    override fun specializes(supertype: Type): Boolean {
        val result = engine.invokeOperation(wrapped.id!!, "specializes", mapOf("supertype" to supertype))
        return (result as? Boolean) ?: false
    }

    override fun specializesFromLibrary(libraryTypeName: String): Boolean {
        val result = engine.invokeOperation(wrapped.id!!, "specializesFromLibrary", mapOf("libraryTypeName" to libraryTypeName))
        return (result as? Boolean) ?: false
    }

    override fun subsetsChain(first: Feature, second: Feature): Boolean? {
        val result = engine.invokeOperation(wrapped.id!!, "subsetsChain", mapOf("first" to first, "second" to second))
        return result as? Boolean
    }

    override fun supertypes(excludeImplied: Boolean): List<Type> {
        val result = engine.invokeOperation(wrapped.id!!, "supertypes", mapOf("excludeImplied" to excludeImplied))
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Type }
            ?: emptyList()
    }

    override fun typingFeatures(): List<Feature> {
        val result = engine.invokeOperation(wrapped.id!!, "typingFeatures")
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Feature }
            ?: emptyList()
    }

    override fun unqualifiedNameOf(qualifiedName: String): String {
        val result = engine.invokeOperation(wrapped.id!!, "unqualifiedNameOf", mapOf("qualifiedName" to qualifiedName))
        return (result as? String) ?: ""
    }

    override fun visibilityOf(mem: Membership): String {
        val result = engine.invokeOperation(wrapped.id!!, "visibilityOf", mapOf("mem" to mem))
        return (result as? String) ?: ""
    }

    override fun visibleMemberships(excluded: List<Namespace>, isRecursive: Boolean, includeAll: Boolean): List<Membership> {
        val result = engine.invokeOperation(wrapped.id!!, "visibleMemberships", mapOf("excluded" to excluded, "isRecursive" to isRecursive, "includeAll" to includeAll))
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Membership }
            ?: emptyList()
    }
}

