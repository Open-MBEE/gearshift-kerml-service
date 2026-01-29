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
 * Implementation of Feature.
 * A type that is also a feature
 */
open class FeatureImpl(
    wrapped: MDMObject,
    engine: MDMEngine
) : TypeImpl(wrapped, engine), Feature {

    override var direction: String?
        get() {
            val rawValue = wrapped.getProperty("direction")
            return rawValue as? String
        }
        set(value) {
            engine.setProperty(wrapped.id!!, "direction", value)
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

    override val endOwningType: Type?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "endOwningType")
            return (rawValue as? MDMObject)?.let { Wrappers.wrap(it, engine) as Type }
        }

    override val featuringType: List<Type>
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "featuringType")
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

    override val ownedCrossSubsetting: CrossSubsetting?
        get() {
            val rawValue = engine.getProperty(wrapped.id!!, "ownedCrossSubsetting")
            return (rawValue as? MDMObject)?.let { Wrappers.wrap(it, engine) as CrossSubsetting }
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

    override fun allRedefinedFeatures(): List<Feature> {
        val result = engine.invokeOperation(wrapped.id!!, "allRedefinedFeatures")
        return (result as? List<*>)
            ?.filterIsInstance<MDMObject>()
            ?.map { Wrappers.wrap(it, engine) as Feature }
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

    override fun effectiveName(): String? {
        val result = engine.invokeOperation(wrapped.id!!, "effectiveName")
        return result as? String
    }

    override fun effectiveShortName(): String? {
        val result = engine.invokeOperation(wrapped.id!!, "effectiveShortName")
        return result as? String
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

    override fun namingFeature(): Feature? {
        val result = engine.invokeOperation(wrapped.id!!, "namingFeature")
        return (result as? MDMObject)?.let { Wrappers.wrap(it, engine) as Feature }
    }

    override fun ownedCrossFeature(): Feature? {
        val result = engine.invokeOperation(wrapped.id!!, "ownedCrossFeature")
        return (result as? MDMObject)?.let { Wrappers.wrap(it, engine) as Feature }
    }

    override fun redefines(redefinedFeature: Feature): Boolean? {
        val result = engine.invokeOperation(wrapped.id!!, "redefines", mapOf("redefinedFeature" to redefinedFeature))
        return result as? Boolean
    }

    override fun redefinesFromLibrary(libraryFeatureName: String): Boolean? {
        val result = engine.invokeOperation(wrapped.id!!, "redefinesFromLibrary", mapOf("libraryFeatureName" to libraryFeatureName))
        return result as? Boolean
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
}

