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
package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.mdm.framework.meta.BindingCondition
import org.openmbee.mdm.framework.meta.BindingKind
import org.openmbee.mdm.framework.meta.BodyLanguage
import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.MetaOperation
import org.openmbee.mdm.framework.meta.MetaParameter
import org.openmbee.mdm.framework.meta.MetaProperty
import org.openmbee.mdm.framework.meta.SemanticBinding
import org.openmbee.mdm.framework.runtime.MDMObject

/**
 * KerML Type metaclass.
 * Specializes: Namespace
 * A namespace that can be specialized.
 */
fun createTypeMetaClass() = MetaClass(
    name = "Type",
    isAbstract = false,
    superclasses = listOf("Namespace"),
    attributes = listOf(
        MetaProperty(
            name = "isAbstract",
            type = "Boolean",
            description = "Whether this type is abstract"
        ),
        MetaProperty(
            name = "isSufficient",
            type = "Boolean",
            description = "Whether this type is sufficient"
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "deriveTypeAssociation",
            type = ConstraintType.DERIVATION,
            expression = "relationship->selectByKind(Association)",
            isNormative = false,
            description = "The Associations that relate this Type."
        ),
        MetaConstraint(
            name = "deriveTypeFeaturedConnector",
            type = ConstraintType.DERIVATION,
            expression = "feature->selectByKind(Connector)->select(c | c.defaultFeaturingType = self)",
            isNormative = false,
            description = "The Connectors whose default featuring type is this Type."
        ),
        MetaConstraint(
            name = "deriveTypeDifferencedType",
            type = ConstraintType.DERIVATION,
            expression = "ownedDifferencing.typeDifferenced",
            isNormative = false,
            description = "Types that are differenced by this Type's differencingTypes."
        ),
        MetaConstraint(
            name = "deriveTypeDifferencingType",
            type = ConstraintType.DERIVATION,
            expression = "ownedDifferencing.differencingType",
            description = "The differencingTypes of a Type are the differencingTypes of its ownedDifferencings, in the same order"
        ),
        MetaConstraint(
            name = "deriveTypeDirectedFeature",
            type = ConstraintType.DERIVATION,
            expression = "feature->select(f | directionOf(f) <> null)",
            description = "The directedFeatures of a Type are those features for which the direction is non-null"
        ),
        MetaConstraint(
            name = "deriveTypeEndFeature",
            type = ConstraintType.DERIVATION,
            expression = "feature->select(isEnd)",
            description = "The endFeatures of a Type are all its features for which isEnd = true"
        ),
        MetaConstraint(
            name = "deriveTypeFeature",
            type = ConstraintType.DERIVATION,
            expression = "featureMembership.ownedMemberFeature",
            description = "The features of a Type are the ownedMemberFeatures of its featureMemberships"
        ),
        MetaConstraint(
            name = "deriveTypeFeatureMembership",
            type = ConstraintType.DERIVATION,
            expression = "ownedFeatureMembership->union(inheritedMembership->selectByKind(FeatureMembership))",
            description = "The featureMemberships of a Type is the union of the ownedFeatureMemberships and those inheritedMemberships that are FeatureMemberships"
        ),
        MetaConstraint(
            name = "deriveTypeInheritedFeature",
            type = ConstraintType.DERIVATION,
            expression = "inheritedMembership->selectByKind(FeatureMembership).memberFeature",
            description = "The inheritedFeatures of this Type are the memberFeatures of the inheritedMemberships that are FeatureMemberships"
        ),
        MetaConstraint(
            name = "deriveTypeInheritedMembership",
            type = ConstraintType.DERIVATION,
            expression = "inheritedMemberships(Set{}, Set{}, false)",
            description = "The inheritedMemberships of a Type are determined by the inheritedMemberships() operation"
        ),
        MetaConstraint(
            name = "deriveTypeInput",
            type = ConstraintType.DERIVATION,
            expression = """
                feature->select(f |
                    let direction: FeatureDirectionKind = directionOf(f) in
                    direction = FeatureDirectionKind::_'in' or
                    direction = FeatureDirectionKind::inout)
            """.trimIndent(),
            description = "The inputs of a Type are those of its features that have a direction of in or inout relative to the Type, taking conjugation into account"
        ),
        MetaConstraint(
            name = "deriveTypeIntersectingType",
            type = ConstraintType.DERIVATION,
            expression = "ownedIntersecting.intersectingType",
            description = "The intersectingTypes of a Type are the intersectingTypes of its ownedIntersectings"
        ),
        MetaConstraint(
            name = "deriveTypeMultiplicity",
            type = ConstraintType.DERIVATION,
            expression = """
                let ownedMultiplicities: Sequence(Multiplicity) =
                    ownedMember->selectByKind(Multiplicity) in
                if ownedMultiplicities->isEmpty() then null
                else ownedMultiplicities->first()
                endif
            """.trimIndent(),
            description = "If a Type has an ownedMultiplicity, then that is its multiplicity. Otherwise, if the Type has an ownedSpecialization, then its multiplicity is the multiplicity of the general Type of that Specialization"
        ),
        MetaConstraint(
            name = "deriveTypeOutput",
            type = ConstraintType.DERIVATION,
            expression = """
                feature->select(f |
                    let direction: FeatureDirectionKind = directionOf(f) in
                    direction = FeatureDirectionKind::out or
                    direction = FeatureDirectionKind::inout)
            """.trimIndent(),
            description = "The outputs of a Type are those of its features that have a direction of out or inout relative to the Type, taking conjugation into account"
        ),
        MetaConstraint(
            name = "deriveTypeOwnedConjugator",
            type = ConstraintType.DERIVATION,
            expression = """
                let ownedConjugators: Sequence(Conjugation) =
                    ownedRelationship->selectByKind(Conjugation) in
                if ownedConjugators->isEmpty() then null
                else ownedConjugators->at(1)
                endif
            """.trimIndent(),
            description = "The ownedConjugator of a Type is the its single ownedRelationship that is a Conjugation"
        ),
        MetaConstraint(
            name = "deriveTypeOwnedDifferencing",
            type = ConstraintType.DERIVATION,
            expression = "ownedRelationship->selectByKind(Differencing)",
            description = "The ownedDifferencings of a Type are its ownedRelationships that are Differencings"
        ),
        MetaConstraint(
            name = "deriveTypeOwnedDisjoining",
            type = ConstraintType.DERIVATION,
            expression = "ownedRelationship->selectByKind(Disjoining)",
            description = "The ownedDisjoinings of a Type are the ownedRelationships that are Disjoinings"
        ),
        MetaConstraint(
            name = "deriveTypeOwnedEndFeature",
            type = ConstraintType.DERIVATION,
            expression = "ownedFeature->select(isEnd)",
            description = "The ownedEndFeatures of a Type are all its ownedFeatures for which isEnd = true"
        ),
        MetaConstraint(
            name = "deriveTypeOwnedFeature",
            type = ConstraintType.DERIVATION,
            expression = "ownedFeatureMembership.ownedMemberFeature",
            description = "The ownedFeatures of a Type are the ownedMemberFeatures of its ownedFeatureMemberships"
        ),
        MetaConstraint(
            name = "deriveTypeOwnedFeatureMembership",
            type = ConstraintType.DERIVATION,
            expression = "ownedRelationship->selectByKind(FeatureMembership)",
            description = "The ownedFeatureMemberships of a Type are its ownedMemberships that are FeatureMemberships"
        ),
        MetaConstraint(
            name = "deriveTypeOwnedIntersecting",
            type = ConstraintType.DERIVATION,
            expression = "ownedRelationship->selectByKind(Intersecting)",
            description = "The ownedIntersectings of a Type are the ownedRelationships that are Intersectings"
        ),
        MetaConstraint(
            name = "deriveTypeOwnedSpecialization",
            type = ConstraintType.DERIVATION,
            expression = "ownedRelationship->selectByKind(Specialization)->select(s | s.specific = self)",
            description = "The ownedSpecializations of a Type are the ownedRelationships that are Specializations whose specificType is the owningType"
        ),
        MetaConstraint(
            name = "deriveTypeOwnedUnioning",
            type = ConstraintType.DERIVATION,
            expression = "ownedRelationship->selectByKind(Unioning)",
            description = "The ownedUnionings of a Type are the ownedRelationships that are Unionings"
        ),
        MetaConstraint(
            name = "deriveTypeSourceAssociation",
            type = ConstraintType.DERIVATION,
            expression = "sourceRelationship->selectByKind(Association)",
            isNormative = false,
            description = "The Associations where this Type is the source."
        ),
        MetaConstraint(
            name = "deriveTypeTargetAssociation",
            type = ConstraintType.DERIVATION,
            expression = "targetRelationship->selectByKind(Association)",
            isNormative = false,
            description = "The Associations where this Type is the target."
        ),
        MetaConstraint(
            name = "deriveTypeUnioningType",
            type = ConstraintType.DERIVATION,
            expression = "ownedUnioning.unioningType",
            description = "The unioningTypes of a Type are the unioningTypes of its ownedUnionings"
        ),
        MetaConstraint(
            name = "validateTypeAtMostOneConjugator",
            type = ConstraintType.VERIFICATION,
            expression = "ownedRelationship->selectByKind(Conjugation)->size() <= 1",
            description = "A Type must have at most one ownedConjugation Relationship"
        ),
        MetaConstraint(
            name = "validateTypeDifferencingTypesNotSelf",
            type = ConstraintType.VERIFICATION,
            expression = "differencingType->excludes(self)",
            description = "A Type cannot be one of its own differencingTypes"
        ),
        MetaConstraint(
            name = "validateTypeIntersectingTypesNotSelf",
            type = ConstraintType.VERIFICATION,
            expression = "intersectingType->excludes(self)",
            description = "A Type cannot be one of its own intersectingTypes"
        ),
        MetaConstraint(
            name = "validateTypeOwnedDifferencingNotOne",
            type = ConstraintType.VERIFICATION,
            expression = "ownedDifferencing->size() <> 1",
            description = "A Type must not have exactly one ownedDifferencing"
        ),
        MetaConstraint(
            name = "validateTypeOwnedIntersectingNotOne",
            type = ConstraintType.VERIFICATION,
            expression = "ownedIntersecting->size() <> 1",
            description = "A Type must not have exactly one ownedIntersecting"
        ),
        MetaConstraint(
            name = "validateTypeOwnedMultiplicity",
            type = ConstraintType.VERIFICATION,
            expression = "ownedMember->selectByKind(Multiplicity)->size() <= 1",
            description = "A Type may have at most one ownedMember that is a Multiplicity"
        ),
        MetaConstraint(
            name = "validateTypeOwnedUnioningNotOne",
            type = ConstraintType.VERIFICATION,
            expression = "ownedUnioning->size() <> 1",
            description = "A Type must not have exactly one ownedUnioning"
        ),
        MetaConstraint(
            name = "validateTypeUnioningTypesNotSelf",
            type = ConstraintType.VERIFICATION,
            expression = "unioningType->excludes(self)",
            description = "A Type cannot be one of its own unioningTypes"
        ),
        MetaConstraint(
            name = "computeFeatureFeatureOfType",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "TypeFeaturing.allInstances()->select(tf | tf.featureOfType = self)",
            isNormative = false,
            description = "The TypeFeaturings that have this Feature as their featureOfType."
        ),
        MetaConstraint(
            name = "computeFeatureTypedFeature",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "FeatureTyping.allInstances()->select(ft | ft.typedFeature = self)",
            isNormative = false,
            description = "The FeatureTypings that have this Feature as their typedFeature."
        ),
        MetaConstraint(
            name = "computeMultiplicityTypeWithMultiplicity",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "Type.allInstances()->select(t | t.multiplicity = self)->any(true)",
            isNormative = false,
            description = "The Type that has this Multiplicity as its multiplicity."
        ),
        MetaConstraint(
            name = "computeTypeIntersectedType",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "ownedIntersecting.typeIntersected",
            isNormative = false,
            description = "Types that are intersected by this Type's intersectingTypes."
        ),
        MetaConstraint(
            name = "computeTypeInstantiationExpression",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "InstantiationExpression.allInstances()->select(ie | ie.instantiatedType = self)",
            isNormative = false,
            description = "The InstantiationExpressions that have this Type as their instantiatedType."
        ),
        MetaConstraint(
            name = "computeTypeUnionedType",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "ownedUnioning.typeUnioned",
            isNormative = false,
            description = "Types that are unioned by this Type's unioningTypes."
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "allRedefinedFeaturesOf",
            returnType = "Feature",
            returnUpperBound = -1,
            parameters = listOf(
                MetaParameter(name = "membership", type = "Membership")
            ),
            body = MetaOperation.ocl("""
                if not membership.memberElement.oclIsKindOf(Feature) then Set{}
                else membership.memberElement.oclAsType(Feature).allRedefinedFeatures()
                endif
            """.trimIndent()),
            description = """
                If the memberElement of the given membership is a Feature, then return all
                Features directly or indirectly redefined by the memberElement.
            """.trimIndent()
        ),
        MetaOperation(
            name = "allSupertypes",
            returnType = "Type",
            returnUpperBound = -1,
            body = MetaOperation.ocl("OrderedSet{self}->closure(supertypes(false))"),
            description = """
                Return this Type and all Types that are directly or transitively supertypes
                of this Type (as determined by the supertypes operation with excludeImplied = false).
            """.trimIndent()
        ),
        MetaOperation(
            name = "directionOf",
            returnType = "FeatureDirectionKind",
            parameters = listOf(
                MetaParameter(name = "feature", type = "Feature")
            ),
            body = MetaOperation.ocl("directionOfExcluding(feature, Set{})"),
            description = """
                If the given feature is a feature of this Type, then return its direction
                relative to this Type, taking conjugation into account.
            """.trimIndent()
        ),
        MetaOperation(
            name = "directionOfExcluding",
            returnType = "FeatureDirectionKind",
            parameters = listOf(
                MetaParameter(name = "feature", type = "Feature"),
                MetaParameter(name = "excluded", type = "Type", lowerBound = 0, upperBound = -1)
            ),
            body = MetaOperation.ocl("""
                let excludedSelf : Set(Type) = excluded->including(self) in
                if feature.owningType = self then feature.direction
                else
                    let directions : Sequence(FeatureDirectionKind) =
                        supertypes(false)->excluding(excludedSelf).
                        directionOfExcluding(feature, excludedSelf)->
                        select(d | d <> null) in
                    if directions->isEmpty() then null
                    else
                        let direction : FeatureDirectionKind = directions->first() in
                        if not isConjugated then direction
                        else if direction = FeatureDirectionKind::_'in' then FeatureDirectionKind::out
                        else if direction = FeatureDirectionKind::out then FeatureDirectionKind::_'in'
                        else direction
                        endif endif endif
                    endif
                endif
            """.trimIndent()),
            description = """
                Return the direction of the given feature relative to this Type, excluding
                a given set of Types from the search of supertypes of this Type.
            """.trimIndent()
        ),
        MetaOperation(
            name = "inheritableMemberships",
            returnType = "Membership",
            returnUpperBound = -1,
            parameters = listOf(
                MetaParameter(name = "excludedNamespaces", type = "Namespace", lowerBound = 0, upperBound = -1),
                MetaParameter(name = "excludedTypes", type = "Type", lowerBound = 0, upperBound = -1),
                MetaParameter(name = "excludeImplied", type = "Boolean")
            ),
            body = MetaOperation.native { element, args, engine ->
                val excludedTypes = when (val v = args["excludedTypes"]) {
                    is Collection<*> -> v.filterNotNull().toMutableSet()
                    is MDMObject -> mutableSetOf<Any>(v)
                    else -> mutableSetOf()
                }
                val excludeImplied = args["excludeImplied"] as? Boolean ?: false
                val excludedNamespaces = args["excludedNamespaces"] ?: emptySet<Any>()
                // excludingSelf = excludedTypes + {self}
                val excludingSelf = excludedTypes.also { it.add(element) }
                // Get supertypes
                val supertypes = engine.invokeOperation(
                    element.id!!, "supertypes", mapOf("excludeImplied" to excludeImplied)
                )
                val supertypeList = when (supertypes) {
                    is List<*> -> supertypes.filterIsInstance<MDMObject>()
                    is MDMObject -> listOf(supertypes)
                    else -> emptyList()
                }
                // Filter out excluded types
                val validSupertypes = supertypeList.filter { it !in excludingSelf }
                if (validSupertypes.isEmpty()) return@native emptyList<Any>()
                // Get nonPrivateMemberships from each supertype
                val result = mutableListOf<Any>()
                for (supertype in validSupertypes) {
                    val memberships = engine.invokeOperation(
                        supertype.id!!, "nonPrivateMemberships",
                        mapOf(
                            "excludedNamespaces" to excludedNamespaces,
                            "excludedTypes" to excludingSelf,
                            "excludeImplied" to excludeImplied
                        )
                    )
                    when (memberships) {
                        is Collection<*> -> result.addAll(memberships.filterNotNull())
                        is MDMObject -> result.add(memberships)
                    }
                }
                result
            },
            description = """
                Return all the non-private Memberships of all the supertypes of this Type,
                excluding any supertypes that are this Type or are in the given set of
                excludedTypes. If excludeImplied = true, then also transitively exclude
                any supertypes from implied Specializations.
            """.trimIndent()
        ),
        MetaOperation(
            name = "inheritedMemberships",
            returnType = "Membership",
            returnUpperBound = -1,
            parameters = listOf(
                MetaParameter(name = "excludedNamespaces", type = "Namespace", lowerBound = 0, upperBound = -1),
                MetaParameter(name = "excludedTypes", type = "Type", lowerBound = 0, upperBound = -1),
                MetaParameter(name = "excludeImplied", type = "Boolean")
            ),
            body = MetaOperation.native { element, args, engine ->
                val inheritable = engine.invokeOperation(element.id!!, "inheritableMemberships", args)
                val inheritableList = when (inheritable) {
                    is Collection<*> -> inheritable.filterNotNull().toList()
                    is MDMObject -> listOf(inheritable)
                    else -> emptyList()
                }
                if (inheritableList.isEmpty()) return@native emptyList<Any>()
                engine.invokeOperation(
                    element.id!!, "removeRedefinedFeatures",
                    mapOf("memberships" to inheritableList)
                ) ?: emptyList<Any>()
            },
            description = """
                Return the Memberships inheritable from supertypes of this Type with
                redefinedFeatures removed. When computing inheritableMemberships, exclude
                Imports of excludedNamespaces, Specializations of excludedTypes, and,
                if excludeImplied = true, all implied Specializations.
            """.trimIndent()
        ),
        MetaOperation(
            name = "isCompatibleWith",
            returnType = "Boolean",
            returnLowerBound = 1,
            returnUpperBound = 1,
            parameters = listOf(
                MetaParameter(name = "otherType", type = "Type")
            ),
            body = MetaOperation.ocl("specializes(otherType)"),
            description = """
                By default, this Type is compatible with another Type if it directly
                or indirectly specializes the otherType.
            """.trimIndent()
        ),
        MetaOperation(
            name = "multiplicities",
            returnType = "Multiplicity",
            returnUpperBound = -1,
            body = MetaOperation.ocl("""
                if multiplicity <> null then OrderedSet{multiplicity}
                else
                    ownedSpecialization.general->closure(t |
                        if t.multiplicity <> null then OrderedSet{}
                        else t.ownedSpecialization.general
                        endif
                    )->select(multiplicity <> null).multiplicity->asOrderedSet()
                endif
            """.trimIndent()),
            description = "Return the owned or inherited Multiplicities for this Type."
        ),
        MetaOperation(
            name = "nonPrivateMemberships",
            returnType = "Membership",
            returnUpperBound = -1,
            parameters = listOf(
                MetaParameter(name = "excludedNamespaces", type = "Namespace", lowerBound = 0, upperBound = -1),
                MetaParameter(name = "excludedTypes", type = "Type", lowerBound = 0, upperBound = -1),
                MetaParameter(name = "excludeImplied", type = "Boolean")
            ),
            body = MetaOperation.native { element, args, engine ->
                val excludedNamespaces = args["excludedNamespaces"] ?: emptySet<Any>()
                val result = mutableListOf<Any>()
                // Public memberships
                val publicMs = engine.invokeOperation(
                    element.id!!, "membershipsOfVisibility",
                    mapOf("visibility" to "public", "excluded" to excludedNamespaces)
                )
                when (publicMs) {
                    is Collection<*> -> result.addAll(publicMs.filterNotNull())
                    is MDMObject -> result.add(publicMs)
                }
                // Protected memberships
                val protectedMs = engine.invokeOperation(
                    element.id!!, "membershipsOfVisibility",
                    mapOf("visibility" to "protected", "excluded" to excludedNamespaces)
                )
                when (protectedMs) {
                    is Collection<*> -> result.addAll(protectedMs.filterNotNull())
                    is MDMObject -> result.add(protectedMs)
                }
                // Inherited memberships
                val inherited = engine.invokeOperation(element.id!!, "inheritedMemberships", args)
                when (inherited) {
                    is Collection<*> -> result.addAll(inherited.filterNotNull())
                    is MDMObject -> result.add(inherited)
                }
                result
            },
            description = """
                Return the public, protected and inherited Memberships of this Type. When computing
                imported Memberships, exclude the given set of excludedNamespaces. When computing
                inheritedMemberships, exclude Types in the given set of excludedTypes. If
                excludeImplied = true, then also exclude any supertypes from implied Specializations.
            """.trimIndent()
        ),
        MetaOperation(
            name = "removeRedefinedFeatures",
            returnType = "Membership",
            returnUpperBound = -1,
            parameters = listOf(
                MetaParameter(name = "memberships", type = "Membership", lowerBound = 0, upperBound = -1)
            ),
            body = MetaOperation.ocl("""
                let reducedMemberships : Sequence(Membership) =
                    memberships->reject(mem1 |
                        memberships->excluding(mem1)->
                            exists(mem2 | allRedefinedFeaturesOf(mem2)->
                                includes(mem1.memberElement))) in
                let redefinedFeatures : Set(Feature) =
                    ownedFeature.redefinition.redefinedFeature->asSet() in
                reducedMemberships->reject(mem | allRedefinedFeaturesOf(mem)->
                    exists(feature | redefinedFeatures->includes(feature)))
            """.trimIndent()),
            description = """
                Return a subset of memberships, removing those Memberships whose memberElements
                are Features and for which either: (1) The memberElement of the Membership is
                included in redefinedFeatures of another Membership in memberships, or (2) One
                of the redefinedFeatures of the Membership is a directly redefinedFeature of an
                ownedFeature of this Type.
            """.trimIndent()
        ),
        MetaOperation(
            name = "specializes",
            returnType = "Boolean",
            returnLowerBound = 1,
            returnUpperBound = 1,
            parameters = listOf(
                MetaParameter(name = "supertype", type = "Type")
            ),
            body = MetaOperation.ocl("""
                if isConjugated then
                    ownedConjugator.originalType.specializes(supertype)
                else
                    allSupertypes()->includes(supertype)
                endif
            """.trimIndent()),
            description = """
                Check whether this Type is a direct or indirect specialization of the given supertype.
            """.trimIndent()
        ),
        MetaOperation(
            name = "specializesFromLibrary",
            returnType = "Boolean",
            returnLowerBound = 1,
            returnUpperBound = 1,
            parameters = listOf(
                MetaParameter(name = "libraryTypeName", type = "String")
            ),
            body = MetaOperation.ocl("""
                let mem : Membership = resolveGlobal(libraryTypeName) in
                mem <> null and mem.memberElement.oclIsKindOf(Type) and
                specializes(mem.memberElement.oclAsType(Type))
            """.trimIndent()),
            description = """
                Check whether this Type is a direct or indirect specialization of the named library Type.
                libraryTypeName must conform to the syntax of a KerML qualified name and must resolve
                to a Type in global scope.
            """.trimIndent()
        ),
        MetaOperation(
            name = "supertypes",
            returnType = "Type",
            returnUpperBound = -1,
            parameters = listOf(
                MetaParameter(name = "excludeImplied", type = "Boolean")
            ),
            body = MetaOperation.native { element, args, engine ->
                val excludeImplied = args["excludeImplied"] as? Boolean ?: false
                val conjugator = engine.getProperty(element, "ownedConjugator") as? MDMObject
                if (conjugator != null) {
                    val originalType = engine.getProperty(conjugator, "originalType") as? MDMObject
                    return@native if (originalType != null) listOf(originalType) else emptyList<Any>()
                }
                val ownedSpecs = engine.getProperty(element, "ownedSpecialization")
                val specList = when (ownedSpecs) {
                    is List<*> -> ownedSpecs.filterIsInstance<MDMObject>()
                    is MDMObject -> listOf(ownedSpecs)
                    else -> emptyList()
                }
                val filtered = if (excludeImplied) {
                    specList.filter { (engine.getProperty(it, "isImplied") as? Boolean) != true }
                } else specList
                filtered.mapNotNull { engine.getProperty(it, "general") as? MDMObject }
            },
            description = """
                If this Type is conjugated, then return just the originalType of the Conjugation.
                Otherwise, return the general Types from all ownedSpecializations of this Type,
                if excludeImplied = false, or all non-implied ownedSpecializations, if excludeImplied = true.
            """.trimIndent()
        ),
        MetaOperation(
            name = "visibleMemberships",
            returnType = "Membership",
            returnUpperBound = -1,
            parameters = listOf(
                MetaParameter(name = "excluded", type = "Namespace", lowerBound = 0, upperBound = -1),
                MetaParameter(name = "isRecursive", type = "Boolean"),
                MetaParameter(name = "includeAll", type = "Boolean")
            ),
            redefines = "Namespace::visibleMemberships",
            body = MetaOperation.ocl("""
                let visibleMemberships : OrderedSet(Membership) =
                    self.oclAsType(Namespace).
                    visibleMemberships(excluded, isRecursive, includeAll) in
                let visibleInheritedMemberships : OrderedSet(Membership) =
                    inheritedMemberships(excluded->including(self), Set{}, isRecursive)->
                    select(includeAll or visibility = VisibilityKind::public) in
                visibleMemberships->union(visibleInheritedMemberships)
            """.trimIndent()),
            description = "The visibleMemberships of a Type include inheritedMemberships."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "typeAnythingBinding",
            baseConcept = "Base::Anything",
            bindingKind = BindingKind.SPECIALIZES,
            condition = BindingCondition.Default
        )
    ),
    description = "A namespace that can be specialized"
)
