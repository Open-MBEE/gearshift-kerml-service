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

package org.openmbee.gearshift.generated.interfaces

/**
 * A type that is also a feature
 */
interface Feature : Type {

    /**
     * The direction of the feature
     */
    var direction: String?

    /**
     * Whether this feature is composite
     */
    var isComposite: Boolean

    /**
     * Whether this feature has a constant value
     */
    var isConstant: Boolean

    /**
     * Whether this feature is derived
     */
    var isDerived: Boolean

    /**
     * Whether this feature is an end feature
     */
    var isEnd: Boolean

    /**
     * Whether values of this feature are nonunique (allows duplicates)
     */
    var isNonunique: Boolean

    /**
     * Whether values of this feature are ordered
     */
    var isOrdered: Boolean

    /**
     * Whether this feature is a portion
     */
    var isPortion: Boolean

    /**
     * Whether this feature is read-only
     */
    var isReadOnly: Boolean

    /**
     * Whether values of this feature are unique
     */
    var isUnique: Boolean

    /**
     * Whether this feature is a variable
     */
    var isVariable: Boolean

    val chainingFeature: List<Feature>

    val crossFeature: Feature?

    val endOwningType: Type?

    val featuringType: List<Type>

    var invertingFeatureInverting: Set<FeatureInverting>

    val ownedCrossSubsetting: CrossSubsetting?

    val ownedFeatureChaining: List<FeatureChaining>

    val ownedFeatureInverting: Set<FeatureInverting>

    val ownedRedefinition: Set<Redefinition>

    val ownedReferenceSubsetting: ReferenceSubsetting?

    val ownedSubsetting: Set<Subsetting>

    val ownedTypeFeaturing: List<TypeFeaturing>

    val ownedTyping: List<FeatureTyping>

    val owningFeatureMembership: FeatureMembership?

    val owningType: Type?

    var redefinition: Set<Redefinition>

    var subsetting: Set<Subsetting>

    val type: List<Type>

    var typing: Set<FeatureTyping>

    /**
     * Return this Feature and all the Features that are directly or indirectly redefined by this Feature.
     */
    fun allRedefinedFeatures(): List<Feature>

    /**
     * If isCartesianProduct is true, then return the list of Types whose Cartesian product can be represented by this Feature.
     */
    fun asCartesianProduct(): List<Type>

    /**
     * A Feature can access another feature if the other feature is featured within one of the direct or indirect featuringTypes of this Feature.
     */
    fun canAccess(feature: Feature): Boolean?

    /**
     * Return the direction of this Feature relative to the given type.
     */
    fun directionFor(type: Type): String?

    /**
     * If a Feature has no declaredName or declaredShortName, then its effective name is given by the effective name of the Feature returned by the namingFeature() operation, if any.
     */
    override fun effectiveName(): String?

    /**
     * If a Feature has no declaredShortName or declaredName, then its effective shortName is given by the effective shortName of the Feature returned by the namingFeature() operation, if any.
     */
    override fun effectiveShortName(): String?

    /**
     * Check whether this Feature can be used to represent a Cartesian product of Types.
     */
    fun isCartesianProduct(): Boolean?

    /**
     * A Feature is compatible with another Type if it either directly or indirectly specializes the otherType or if the otherType is also a Feature and meets compatibility requirements.
     */
    override fun isCompatibleWith(otherType: Type): Boolean

    /**
     * Return if the featuringTypes of this Feature are compatible with the given type. If type is null, then check if this Feature is explicitly or implicitly featured by Base::Anything.
     */
    fun isFeaturedWithin(type: Type?): Boolean?

    /**
     * Return whether the given type must be a featuringType of this Feature.
     */
    fun isFeaturingType(type: Type): Boolean?

    /**
     * Return whether this Feature is an owned crossFeature of an endFeature.
     */
    fun isOwnedCrossFeature(): Boolean?

    /**
     * By default, the namingFeature of a Feature is given by its first redefinedFeature of its first ownedRedefinition, if any.
     */
    fun namingFeature(): Feature?

    /**
     * If this Feature is an endFeature of its owningType, then return the first ownedMember of the Feature that is a Feature, but not a Multiplicity or a MetadataFeature, and whose owningMembership is not a FeatureMembership.
     */
    fun ownedCrossFeature(): Feature?

    /**
     * Check whether this Feature directly redefines the given redefinedFeature.
     */
    fun redefines(redefinedFeature: Feature): Boolean?

    /**
     * Check whether this Feature directly redefines the named library Feature. libraryFeatureName must conform to the syntax of a KerML qualified name and must resolve to a Feature in global scope.
     */
    fun redefinesFromLibrary(libraryFeatureName: String): Boolean?

    /**
     * Check whether this Feature directly or indirectly specializes a Feature whose last two chainingFeatures are the given Features first and second.
     */
    fun subsetsChain(first: Feature, second: Feature): Boolean?

    /**
     * Return the supertypes of this Feature, including the featureTarget if different from self.
     */
    override fun supertypes(excludeImplied: Boolean): List<Type>

    /**
     * Return the Features used to determine the types of this Feature (other than this Feature itself). If this Feature is not conjugated, then the typingFeatures consist of all subsettedFeatures, except from CrossSubsetting, and the last chainingFeature (if any). If this Feature is conjugated, then the typingFeatures are only its originalType (if the originalType is a Feature).
     */
    fun typingFeatures(): List<Feature>
}

