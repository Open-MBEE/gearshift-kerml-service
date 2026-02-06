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
 * A namespace that can be specialized
 */
interface Type : Namespace {

    /**
     * Whether this type is abstract
     */
    var isAbstract: Boolean

    /**
     * Whether this type is sufficient
     */
    var isSufficient: Boolean

    val differencedType: Set<Type>

    val differencingType: List<Type>

    val directedFeature: List<Feature>

    val endFeature: List<Feature>

    val feature: List<Feature>

    val featureMembership: List<FeatureMembership>

    val inheritedFeature: List<Feature>

    val inheritedMembership: List<Membership>

    val input: List<Feature>

    val intersectedType: Set<Type>

    val intersectingType: List<Type>

    val multiplicity: Multiplicity?

    val output: List<Feature>

    val ownedConjugator: Conjugation?

    val ownedDifferencing: List<Differencing>

    val ownedDisjoining: Set<Disjoining>

    val ownedEndFeature: List<Feature>

    val ownedFeature: List<Feature>

    val ownedFeatureMembership: List<FeatureMembership>

    val ownedIntersecting: List<Intersecting>

    val ownedSpecialization: List<Specialization>

    val ownedUnioning: List<Unioning>

    var specialization: Set<Specialization>

    val unioningType: List<Type>

    /**
     * If the memberElement of the given membership is a Feature, then return all
Features directly or indirectly redefined by the memberElement.
     */
    fun allRedefinedFeaturesOf(membership: Membership): List<Feature>

    /**
     * Return this Type and all Types that are directly or transitively supertypes
of this Type (as determined by the supertypes operation with excludeImplied = false).
     */
    fun allSupertypes(): List<Type>

    /**
     * If the given feature is a feature of this Type, then return its direction
relative to this Type, taking conjugation into account.
     */
    fun directionOf(feature: Feature): String?

    /**
     * Return the direction of the given feature relative to this Type, excluding
a given set of Types from the search of supertypes of this Type.
     */
    fun directionOfExcluding(feature: Feature, excluded: List<Type>): String?

    /**
     * Return all the non-private Memberships of all the supertypes of this Type,
excluding any supertypes that are this Type or are in the given set of
excludedTypes. If excludeImplied = true, then also transitively exclude
any supertypes from implied Specializations.
     */
    fun inheritableMemberships(excludedNamespaces: List<Namespace>, excludedTypes: List<Type>, excludeImplied: Boolean): List<Membership>

    /**
     * Return the Memberships inheritable from supertypes of this Type with
redefinedFeatures removed. When computing inheritableMemberships, exclude
Imports of excludedNamespaces, Specializations of excludedTypes, and,
if excludeImplied = true, all implied Specializations.
     */
    fun inheritedMemberships(excludedNamespaces: List<Namespace>, excludedTypes: List<Type>, excludeImplied: Boolean): List<Membership>

    /**
     * By default, this Type is compatible with another Type if it directly
or indirectly specializes the otherType.
     */
    fun isCompatibleWith(otherType: Type): Boolean

    /**
     * Return the owned or inherited Multiplicities for this Type.
     */
    fun multiplicities(): List<Multiplicity>

    /**
     * Return the public, protected and inherited Memberships of this Type. When computing
imported Memberships, exclude the given set of excludedNamespaces. When computing
inheritedMemberships, exclude Types in the given set of excludedTypes. If
excludeImplied = true, then also exclude any supertypes from implied Specializations.
     */
    fun nonPrivateMemberships(excludedNamespaces: List<Namespace>, excludedTypes: List<Type>, excludeImplied: Boolean): List<Membership>

    /**
     * Return a subset of memberships, removing those Memberships whose memberElements
are Features and for which either: (1) The memberElement of the Membership is
included in redefinedFeatures of another Membership in memberships, or (2) One
of the redefinedFeatures of the Membership is a directly redefinedFeature of an
ownedFeature of this Type.
     */
    fun removeRedefinedFeatures(memberships: List<Membership>): List<Membership>

    /**
     * Check whether this Type is a direct or indirect specialization of the given supertype.
     */
    fun specializes(supertype: Type): Boolean

    /**
     * Check whether this Type is a direct or indirect specialization of the named library Type.
libraryTypeName must conform to the syntax of a KerML qualified name and must resolve
to a Type in global scope.
     */
    fun specializesFromLibrary(libraryTypeName: String): Boolean

    /**
     * If this Type is conjugated, then return just the originalType of the Conjugation.
Otherwise, return the general Types from all ownedSpecializations of this Type,
if excludeImplied = false, or all non-implied ownedSpecializations, if excludeImplied = true.
     */
    fun supertypes(excludeImplied: Boolean): List<Type>

    /**
     * The visibleMemberships of a Type include inheritedMemberships.
     */
    override fun visibleMemberships(excluded: List<Namespace>, isRecursive: Boolean, includeAll: Boolean): List<Membership>
}

