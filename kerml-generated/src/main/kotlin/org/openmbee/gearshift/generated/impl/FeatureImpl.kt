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
 * Implementation of Feature.
 * A type that is also a feature
 */
open class FeatureImpl(
    className: String,
    metaClass: FrameworkMetaClass,
    engine: MDMEngine
) : TypeImpl(className, metaClass, engine), Feature {

    /**
     * Create a new Feature instance.
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
    ) : this("Feature", engine.schema.getClass("Feature")!!, engine) {
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
            val resolved = resolver.resolve(owner.className, "Feature")
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

    override val endOwningType: Type?
        get() {
            val rawValue = engine.getProperty(id!!, "endOwningType")
            return rawValue as? Type
        }

    override val featuringType: List<Type>
        get() {
            val rawValue = engine.getProperty(id!!, "featuringType")
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

    override val ownedCrossSubsetting: CrossSubsetting?
        get() {
            val rawValue = engine.getProperty(id!!, "ownedCrossSubsetting")
            return rawValue as? CrossSubsetting
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

    override fun allRedefinedFeatures(): List<Feature> {
        val result = engine.invokeOperation(id!!, "allRedefinedFeatures")
        return (result as? List<*>)?.filterIsInstance<Feature>() ?: emptyList()
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

    override fun effectiveName(): String? {
        val result = engine.invokeOperation(id!!, "effectiveName")
        return result as? String
    }

    override fun effectiveShortName(): String? {
        val result = engine.invokeOperation(id!!, "effectiveShortName")
        return result as? String
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

    override fun namingFeature(): Feature? {
        val result = engine.invokeOperation(id!!, "namingFeature")
        return result as? Feature
    }

    override fun ownedCrossFeature(): Feature? {
        val result = engine.invokeOperation(id!!, "ownedCrossFeature")
        return result as? Feature
    }

    override fun redefines(redefinedFeature: Feature): Boolean? {
        val result = engine.invokeOperation(id!!, "redefines", mapOf("redefinedFeature" to redefinedFeature))
        return result as? Boolean
    }

    override fun redefinesFromLibrary(libraryFeatureName: String): Boolean? {
        val result = engine.invokeOperation(id!!, "redefinesFromLibrary", mapOf("libraryFeatureName" to libraryFeatureName))
        return result as? Boolean
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
}

