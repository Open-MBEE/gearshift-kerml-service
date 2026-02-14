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
import org.openmbee.mdm.framework.meta.ModelElement
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * KerML Feature metaclass.
 * Specializes: Type
 * A type that is also a feature.
 */
fun createFeatureMetaClass() = MetaClass(
    name = "Feature",
    isAbstract = false,
    superclasses = listOf("Type"),
    attributes = listOf(
        MetaProperty(
            name = "direction",
            type = "FeatureDirectionKind",
            lowerBound = 0,
            description = "The direction of the feature"
        ),
        MetaProperty(
            name = "isComposite",
            type = "Boolean",
            description = "Whether this feature is composite"
        ),
        MetaProperty(
            name = "isConstant",
            type = "Boolean",
            description = "Whether this feature has a constant value"
        ),
        MetaProperty(
            name = "isDerived",
            type = "Boolean",
            description = "Whether this feature is derived"
        ),
        MetaProperty(
            name = "isEnd",
            type = "Boolean",
            description = "Whether this feature is an end feature"
        ),
        MetaProperty(
            name = "isNonunique",
            type = "Boolean",
            description = "Whether values of this feature are nonunique (allows duplicates)"
        ),
        MetaProperty(
            name = "isOrdered",
            type = "Boolean",
            description = "Whether values of this feature are ordered"
        ),
        MetaProperty(
            name = "isPortion",
            type = "Boolean",
            description = "Whether this feature is a portion"
        ),
        MetaProperty(
            name = "isReadOnly",
            type = "Boolean",
            description = "Whether this feature is read-only"
        ),
        MetaProperty(
            name = "isUnique",
            type = "Boolean",
            description = "Whether values of this feature are unique"
        ),
        MetaProperty(
            name = "isVariable",
            type = "Boolean",
            description = "Whether this feature is a variable"
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "allRedefinedFeatures",
            returnType = "Feature",
            returnLowerBound = 0,
            returnUpperBound = -1,
            body = MetaOperation.ocl("ownedRedefinition.redefinedFeature->closure(ownedRedefinition.redefinedFeature)->asOrderedSet()->prepend(self)"),
            description = "Return this Feature and all the Features that are directly or indirectly redefined by this Feature."
        ),
        MetaOperation(
            name = "asCartesianProduct",
            returnType = "Type",
            returnLowerBound = 0,
            returnUpperBound = -1,
            body = MetaOperation.ocl("featuringType->select(t | t.owner <> self)->union(featuringType->select(t | t.owner = self)->selectByKind(Feature).asCartesianProduct())->union(type)"),
            description = "If isCartesianProduct is true, then return the list of Types whose Cartesian product can be represented by this Feature."
        ),
        MetaOperation(
            name = "canAccess",
            returnType = "Boolean",
            parameters = listOf(
                MetaParameter(name = "feature", type = "Feature")
            ),
            body = MetaOperation.ocl("""
                let anythingType: Element = resolveGlobal('Base::Anything').memberElement in
                let allFeaturingTypes: Sequence(Type) = featuringType->closure(t |
                    if not t.oclIsKindOf(Feature) then Sequence{}
                    else
                        let featuringTypes: OrderedSet(Type) = t.oclAsType(Feature).featuringType in
                        if featuringTypes->isEmpty() then Sequence{anythingType}
                        else featuringTypes
                        endif
                    endif) in
                allFeaturingTypes->exists(t | feature.isFeaturedWithin(t))
            """.trimIndent()),
            description = "A Feature can access another feature if the other feature is featured within one of the direct or indirect featuringTypes of this Feature."
        ),
        MetaOperation(
            name = "directionFor",
            returnType = "FeatureDirectionKind",
            returnLowerBound = 0,
            returnUpperBound = 1,
            parameters = listOf(
                MetaParameter(name = "type", type = "Type")
            ),
            body = MetaOperation.ocl("type.directionOf(self)"),
            description = "Return the direction of this Feature relative to the given type."
        ),
        MetaOperation(
            name = "effectiveName",
            returnType = "String",
            returnLowerBound = 0,
            returnUpperBound = 1,
            redefines = "effectiveName",
            // Native implementation replaces OCL to avoid expensive derivation cascade.
            // The OCL version calls namingFeature() which navigates ownedRedefinition→redefinedFeature,
            // triggering inheritedMemberships. For the common case (declaredName is set), this
            // short-circuits immediately without any OCL evaluation.
            body = MetaOperation.native { element, _, engine ->
                val declaredName = element.getProperty("declaredName") as? String
                val declaredShortName = element.getProperty("declaredShortName") as? String
                if (declaredShortName != null || declaredName != null) {
                    declaredName
                } else {
                    // namingFeature = ownedRedefinition->at(1).redefinedFeature
                    val namingFeature = run {
                        val ownedSpecs = engine.getProperty(element, "ownedSpecialization")
                        val specList = when (ownedSpecs) {
                            is List<*> -> ownedSpecs.filterIsInstance<ModelElement>()
                            is ModelElement -> listOf(ownedSpecs)
                            else -> emptyList()
                        }
                        val firstRedef = specList.firstOrNull { engine.isInstanceOf(it, "Redefinition") }
                        firstRedef?.let { engine.getProperty(it, "redefinedFeature") as? ModelElement }
                    }
                    if (namingFeature != null) {
                        engine.invokeOperation(namingFeature.id!!, "effectiveName", emptyMap())
                    } else {
                        null
                    }
                }
            },
            description = "If a Feature has no declaredName or declaredShortName, then its effective name is given by the effective name of the Feature returned by the namingFeature() operation, if any."
        ),
        MetaOperation(
            name = "effectiveShortName",
            returnType = "String",
            returnLowerBound = 0,
            returnUpperBound = 1,
            redefines = "effectiveShortName",
            // Native implementation — same pattern as effectiveName above.
            body = MetaOperation.native { element, _, engine ->
                val declaredShortName = element.getProperty("declaredShortName") as? String
                val declaredName = element.getProperty("declaredName") as? String
                if (declaredShortName != null || declaredName != null) {
                    declaredShortName
                } else {
                    val namingFeature = run {
                        val ownedSpecs = engine.getProperty(element, "ownedSpecialization")
                        val specList = when (ownedSpecs) {
                            is List<*> -> ownedSpecs.filterIsInstance<ModelElement>()
                            is ModelElement -> listOf(ownedSpecs)
                            else -> emptyList()
                        }
                        val firstRedef = specList.firstOrNull { engine.isInstanceOf(it, "Redefinition") }
                        firstRedef?.let { engine.getProperty(it, "redefinedFeature") as? ModelElement }
                    }
                    if (namingFeature != null) {
                        engine.invokeOperation(namingFeature.id!!, "effectiveShortName", emptyMap())
                    } else {
                        null
                    }
                }
            },
            description = "If a Feature has no declaredShortName or declaredName, then its effective shortName is given by the effective shortName of the Feature returned by the namingFeature() operation, if any."
        ),
        MetaOperation(
            name = "isCartesianProduct",
            returnType = "Boolean",
            body = MetaOperation.ocl("""
                type->size() = 1 and
                featuringType->size() = 1 and
                (featuringType->first().owner = self implies
                    featuringType->first().oclIsKindOf(Feature) and
                    featuringType->first().oclAsType(Feature).isCartesianProduct())
            """.trimIndent()),
            description = "Check whether this Feature can be used to represent a Cartesian product of Types."
        ),
        MetaOperation(
            name = "isCompatibleWith",
            returnType = "Boolean",
            returnLowerBound = 1,
            returnUpperBound = 1,
            parameters = listOf(
                MetaParameter(name = "otherType", type = "Type")
            ),
            redefines = "isCompatibleWith",
            body = MetaOperation.ocl("""
                specializes(otherType) or
                otherType.oclIsKindOf(Feature) and
                ownedFeature->isEmpty() and
                otherType.ownedFeature->isEmpty() and
                ownedRedefinition.allRedefinedFeatures()->exists(f |
                    otherType.oclAsType(Feature).allRedefinedFeatures()->includes(f)) and
                canAccess(otherType.oclAsType(Feature))
            """.trimIndent()),
            description = "A Feature is compatible with another Type if it either directly or indirectly specializes the otherType or if the otherType is also a Feature and meets compatibility requirements."
        ),
        MetaOperation(
            name = "isFeaturedWithin",
            returnType = "Boolean",
            parameters = listOf(
                MetaParameter(name = "type", type = "Type", lowerBound = 0, upperBound = 1)
            ),
            body = MetaOperation.ocl("""
                if type = null then
                    featuringType->forAll(f | f = resolveGlobal('Base::Anything').memberElement)
                else
                    featuringType->forAll(f | type.isCompatibleWith(f)) or
                    isVariable and type.specializes(owningType) or
                    chainingFeature->notEmpty() and chainingFeature->first().isVariable and
                    type.specializes(chainingFeature->first().owningType)
                endif
            """.trimIndent()),
            description = "Return if the featuringTypes of this Feature are compatible with the given type. If type is null, then check if this Feature is explicitly or implicitly featured by Base::Anything."
        ),
        MetaOperation(
            name = "isFeaturingType",
            returnType = "Boolean",
            parameters = listOf(
                MetaParameter(name = "type", type = "Type")
            ),
            body = MetaOperation.ocl("""
                owningType <> null and
                if not isVariable then type = owningType
                else if owningType = resolveGlobal('Occurrences::Occurrence').memberElement then
                    type = resolveGlobal('Occurrences::Occurrence::snapshots').memberElement
                else
                    type.oclIsKindOf(Feature) and
                    let feature: Feature = type.oclAsType(Feature) in
                    feature.featuringType->includes(owningType) and
                    feature.redefinesFromLibrary('Occurrences::Occurrence::snapshots')
                endif endif
            """.trimIndent()),
            description = "Return whether the given type must be a featuringType of this Feature."
        ),
        MetaOperation(
            name = "isOwnedCrossFeature",
            returnType = "Boolean",
            body = MetaOperation.ocl("""
                owningNamespace <> null and
                owningNamespace.oclIsKindOf(Feature) and
                owningNamespace.oclAsType(Feature).ownedCrossFeature() = self
            """.trimIndent()),
            description = "Return whether this Feature is an owned crossFeature of an endFeature."
        ),
        MetaOperation(
            name = "namingFeature",
            returnType = "Feature",
            returnLowerBound = 0,
            returnUpperBound = 1,
            body = MetaOperation.ocl("""
                if ownedRedefinition->isEmpty() then
                    null
                else
                    ownedRedefinition->at(1).redefinedFeature
                endif
            """.trimIndent()),
            description = "By default, the namingFeature of a Feature is given by its first redefinedFeature of its first ownedRedefinition, if any."
        ),
        MetaOperation(
            name = "ownedCrossFeature",
            returnType = "Feature",
            returnLowerBound = 0,
            returnUpperBound = 1,
            body = MetaOperation.ocl("""
                if not isEnd or owningType = null then null
                else
                    let ownedMemberFeatures: Sequence(Feature) =
                        ownedMember->selectByKind(Feature)->
                        reject(oclIsKindOf(Multiplicity) or
                               oclIsKindOf(MetadataFeature) or
                               oclIsKindOf(FeatureValue))->
                        reject(owningMembership.oclIsKindOf(FeatureMembership)) in
                    if ownedMemberFeatures->isEmpty() then null
                    else ownedMemberFeatures->first()
                    endif
                endif
            """.trimIndent()),
            description = "If this Feature is an endFeature of its owningType, then return the first ownedMember of the Feature that is a Feature, but not a Multiplicity or a MetadataFeature, and whose owningMembership is not a FeatureMembership."
        ),
        MetaOperation(
            name = "redefines",
            returnType = "Boolean",
            parameters = listOf(
                MetaParameter(name = "redefinedFeature", type = "Feature")
            ),
            body = MetaOperation.ocl("ownedRedefinition.redefinedFeature->includes(redefinedFeature)"),
            description = "Check whether this Feature directly redefines the given redefinedFeature."
        ),
        MetaOperation(
            name = "redefinesFromLibrary",
            returnType = "Boolean",
            parameters = listOf(
                MetaParameter(name = "libraryFeatureName", type = "String")
            ),
            body = MetaOperation.ocl("""
                let mem: Membership = resolveGlobal(libraryFeatureName) in
                mem <> null and mem.memberElement.oclIsKindOf(Feature) and
                redefines(mem.memberElement.oclAsType(Feature))
            """.trimIndent()),
            description = "Check whether this Feature directly redefines the named library Feature. libraryFeatureName must conform to the syntax of a KerML qualified name and must resolve to a Feature in global scope."
        ),
        MetaOperation(
            name = "subsetsChain",
            returnType = "Boolean",
            parameters = listOf(
                MetaParameter(name = "first", type = "Feature"),
                MetaParameter(name = "second", type = "Feature")
            ),
            body = MetaOperation.ocl("""
                allSupertypes()->selectByKind(Feature)->
                exists(f | let n: Integer = f.chainingFeature->size() in
                    n >= 2 and
                    f.chainingFeature->at(n-1) = first and
                    f.chainingFeature->at(n) = second)
            """.trimIndent()),
            description = "Check whether this Feature directly or indirectly specializes a Feature whose last two chainingFeatures are the given Features first and second."
        ),
        MetaOperation(
            name = "supertypes",
            returnType = "Type",
            returnLowerBound = 0,
            returnUpperBound = -1,
            parameters = listOf(
                MetaParameter(name = "excludeImplied", type = "Boolean")
            ),
            redefines = "supertypes",
            body = MetaOperation.native { element, args, engine ->
                val excludeImplied = args["excludeImplied"] as? Boolean ?: false
                // Replicate Type.supertypes logic (can't call super via oclAsType)
                val conjugator = engine.getProperty(element, "ownedConjugator") as? ModelElement
                val baseSupertypes = if (conjugator != null) {
                    val originalType = engine.getProperty(conjugator, "originalType") as? ModelElement
                    if (originalType != null) mutableListOf<ModelElement>(originalType) else mutableListOf()
                } else {
                    val ownedSpecs = engine.getProperty(element, "ownedSpecialization")
                    val specList = when (ownedSpecs) {
                        is List<*> -> ownedSpecs.filterIsInstance<ModelElement>()
                        is ModelElement -> listOf(ownedSpecs)
                        else -> emptyList()
                    }
                    val filtered = if (excludeImplied) {
                        specList.filter { (engine.getProperty(it, "isImplied") as? Boolean) != true }
                    } else specList
                    filtered.mapNotNull { engine.getProperty(it, "general") as? ModelElement }.toMutableList()
                }
                // Append featureTarget if different from self
                val featureTarget = engine.getProperty(element, "featureTarget") as? ModelElement
                if (featureTarget != null && featureTarget.id != element.id) {
                    baseSupertypes.add(featureTarget)
                }
                baseSupertypes
            },
            description = "Return the supertypes of this Feature, including the featureTarget if different from self."
        ),
        MetaOperation(
            name = "typingFeatures",
            returnType = "Feature",
            returnLowerBound = 0,
            returnUpperBound = -1,
            body = MetaOperation.ocl("""
                if not isConjugated then
                    let subsettedFeatures: OrderedSet(Feature) =
                        ownedSubsetting->reject(s | s.oclIsKindOf(CrossSubsetting)).subsettedFeature in
                    if chainingFeature->isEmpty() or
                       subsettedFeature->includes(chainingFeature->last())
                    then subsettedFeatures
                    else subsettedFeatures->append(chainingFeature->last())
                    endif
                else if conjugator.originalType.oclIsKindOf(Feature) then
                    OrderedSet{conjugator.originalType.oclAsType(Feature)}
                else OrderedSet{}
                endif endif
            """.trimIndent()),
            description = "Return the Features used to determine the types of this Feature (other than this Feature itself). If this Feature is not conjugated, then the typingFeatures consist of all subsettedFeatures, except from CrossSubsetting, and the last chainingFeature (if any). If this Feature is conjugated, then the typingFeatures are only its originalType (if the originalType is a Feature)."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "checkFeatureCrossingSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "ownedCrossFeature() <> null implies crossFeature = ownedCrossFeature()",
            description = "If this Feature has isEnd = true and ownedCrossFeature returns a non-null value, then the crossFeature of the Feature must be the Feature returned from ownedCrossFeature."
        ),
        MetaConstraint(
            name = "checkFeatureEndRedefinition",
            type = ConstraintType.VERIFICATION,
            expression = """
                isEnd and owningType <> null implies
                let i: Integer = owningType.ownedEndFeature->indexOf(self) in
                owningType.ownedSpecialization.general->
                forAll(supertype |
                    supertype.endFeature->size() >= i implies
                    redefines(supertype.endFeature->at(i)))
            """.trimIndent(),
            description = "If a Feature has isEnd = true and an owningType that is not empty, then, for each direct supertype of its owningType, it must redefine the endFeature at the same position, if any."
        ),
        MetaConstraint(
            name = "checkFeatureFeatureMembershipTypeFeaturing",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningFeatureMembership <> null implies
                featuringType->exists(t | isFeaturingType(t))
            """.trimIndent(),
            description = "If a Feature is owned via a FeatureMembership, then it must have a featuringType for which the operation isFeaturingType returns true."
        ),
        MetaConstraint(
            name = "checkFeatureFlowFeatureRedefinition",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningType <> null and
                owningType.oclIsKindOf(FlowEnd) and
                owningType.ownedFeature->at(1) = self implies
                let flowType: Type = owningType.owningType in
                flowType <> null implies
                let i: Integer = flowType.ownedFeature->indexOf(owningType) in
                (i = 1 implies redefinesFromLibrary('Transfers::Transfer::source::sourceOutput')) and
                (i = 2 implies redefinesFromLibrary('Transfers::Transfer::target::targetInput'))
            """.trimIndent(),
            description = "If a Feature is the first ownedFeature of a first or second FlowEnd, then it must directly or indirectly specialize either Transfers::Transfer::source::sourceOutput or Transfers::Transfer::target::targetInput, respectively, from the Kernel Semantic Library."
        ),
        MetaConstraint(
            name = "checkFeatureOwnedCrossFeatureRedefinitionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isOwnedCrossFeature() implies
                ownedSubsetting.subsettedFeature->includesAll(
                    owner.oclAsType(Feature).ownedRedefinition.redefinedFeature->
                    select(crossFeature <> null).crossFeature)
            """.trimIndent(),
            description = "If this Feature is the ownedCrossFeature of an endFeature, then, for any endFeature that is redefined by the owning endFeature of this Feature, this Feature must subset the crossFeature of the redefined endFeature, if this exists."
        ),
        MetaConstraint(
            name = "checkFeatureOwnedCrossFeatureSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isOwnedCrossFeature() implies
                owner.oclAsType(Feature).type->forAll(t | self.specializes(t))
            """.trimIndent(),
            description = "If this Feature is the ownedCrossFeature of an endFeature, then it must directly or indirectly specialize the types of its owning endFeature."
        ),
        MetaConstraint(
            name = "checkFeatureOwnedCrossFeatureTypeFeaturing",
            type = ConstraintType.VERIFICATION,
            expression = """
                isOwnedCrossFeature() implies
                let otherEnds: OrderedSet(Feature) =
                    owner.oclAsType(Feature).owningType.endFeature->excluding(self) in
                if otherEnds->size() = 1 then
                    featuringType = otherEnds->first().type
                else
                    featuringType->size() = 1 and
                    featuringType->first().isCartesianProduct() and
                    featuringType->first().asCartesianProduct() = otherEnds.type and
                    featuringType->first().allSupertypes()->includesAll(
                        owner.oclAsType(Feature).ownedRedefinition.redefinedFeature->
                        select(crossFeature() <> null).crossFeature().featuringType)
                endif
            """.trimIndent(),
            description = "If this Feature is the ownedCrossFeature of an endFeature, then it must have featuringTypes consistent with the crossing from other endFeatures of the owningType of its endFeature."
        ),
        MetaConstraint(
            name = "checkFeatureParameterRedefinition",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningType <> null and
                not owningFeatureMembership.oclIsKindOf(ReturnParameterMembership) and
                (owningType.oclIsKindOf(Behavior) or
                 owningType.oclIsKindOf(Step)) and
                (owningType.oclIsKindOf(InvocationExpression) implies
                 not ownedRedefinition->exists(not isImplied))
                implies
                let i: Integer =
                    owningType.ownedFeature->select(direction <> null)->
                    reject(owningFeatureMembership.oclIsKindOf(ReturnParameterMembership))->
                    indexOf(self) in
                owningType.ownedSpecialization.general->
                forAll(supertype |
                    let ownedParameters: Sequence(Feature) =
                        supertype.ownedFeature->select(direction <> null)->
                        reject(owningFeatureMembership.oclIsKindOf(ReturnParameterMembership)) in
                    ownedParameters->size() >= i implies
                    redefines(ownedParameters->at(i)))
            """.trimIndent(),
            description = "If a Feature is a parameter of an owningType that is a Behavior or Step, but not a result parameter or a parameter of an InvocationExpression with at least one non-implied ownedRedefinition, then, for each direct supertype of its owningType that is also a Behavior or Step, it must redefine the parameter at the same position, if any."
        ),
        MetaConstraint(
            name = "checkFeatureResultRedefinition",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningType <> null and
                (owningType.oclIsKindOf(Function) and
                 self = owningType.oclAsType(Function).result or
                 owningType.oclIsKindOf(Expression) and
                 self = owningType.oclAsType(Expression).result) implies
                owningType.ownedSpecialization.general->
                select(oclIsKindOf(Function) or oclIsKindOf(Expression))->
                forAll(supertype |
                    redefines(
                        if supertype.oclIsKindOf(Function) then
                            supertype.oclAsType(Function).result
                        else
                            supertype.oclAsType(Expression).result
                        endif))
            """.trimIndent(),
            description = "If a Feature is a result parameter of an owningType that is a Function or Expression, then, for each direct supertype of its owningType that is also a Function or Expression, it must redefine the result parameter."
        ),
        MetaConstraint(
            name = "checkFeatureValuationSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                direction = null and
                ownedSpecialization->forAll(isImplied) implies
                ownedMembership->
                selectByKind(FeatureValue)->
                forAll(fv | specializes(fv.value.result))
            """.trimIndent(),
            description = "If a Feature has a FeatureValue, no ownedSpecializations that are not implied, and is not directed, then it must specialize the result of the valueExpression of the FeatureValue."
        ),
        MetaConstraint(
            name = "deriveFeatureAssociationWithEnd",
            type = ConstraintType.DERIVATION,
            expression = "typeWithEndFeature->selectByKind(Association)",
            isNormative = false,
            description = "The Associations where this Feature is an end feature."
        ),
        MetaConstraint(
            name = "deriveFeatureParameteredBehavior",
            type = ConstraintType.DERIVATION,
            expression = "typeWithFeature->selectByKind(Behavior)",
            isNormative = false,
            description = "The Behaviors that have this Feature as a parameter."
        ),
        MetaConstraint(
            name = "deriveFeatureParameteredStep",
            type = ConstraintType.DERIVATION,
            expression = "typeWithFeature->selectByKind(Step)",
            isNormative = false,
            description = "The Steps that have this Feature as a parameter."
        ),
        MetaConstraint(
            name = "deriveFeatureConnector",
            type = ConstraintType.DERIVATION,
            expression = "relationship->selectByKind(Connector)",
            isNormative = false,
            description = "The Connectors that relate this Feature."
        ),
        MetaConstraint(
            name = "deriveFeatureFeaturingConnector",
            type = ConstraintType.DERIVATION,
            expression = "typeWithEndFeature->selectByKind(Connector)",
            isNormative = false,
            description = "The Connectors that feature this Feature as an end."
        ),
        MetaConstraint(
            name = "deriveFeatureSourceConnector",
            type = ConstraintType.DERIVATION,
            expression = "sourceRelationship->selectByKind(Connector)",
            isNormative = false,
            description = "The Connectors where this Feature is the source."
        ),
        MetaConstraint(
            name = "deriveFeatureTargetConnector",
            type = ConstraintType.DERIVATION,
            expression = "targetRelationship->selectByKind(Connector)",
            isNormative = false,
            description = "The Connectors where this Feature is a target."
        ),
        MetaConstraint(
            name = "deriveFeatureFeatureCrossing",
            type = ConstraintType.DERIVATION,
            expression = "ownedCrossSubsetting.crossedFeature.chainingFeature->at(2).featuringType",
            isNormative = false,
            description = "Features that cross this Feature via CrossSubsetting."
        ),
        MetaConstraint(
            name = "deriveFeatureChainingFeature",
            type = ConstraintType.DERIVATION,
            expression = "ownedFeatureChaining.chainingFeature",
            description = "The chainingFeatures of a Feature are the chainingFeatures of its ownedFeatureChaininings."
        ),
        MetaConstraint(
            name = "deriveFeatureCrossFeature",
            type = ConstraintType.DERIVATION,
            expression = """
                if ownedCrossSubsetting = null then null
                else
                    let chainingFeatures: Sequence(Feature) =
                        ownedCrossSubsetting.crossedFeature.chainingFeature in
                    if chainingFeatures->size() < 2 then null
                    else chainingFeatures->at(2)
                    endif
                endif
            """.trimIndent(),
            description = "The crossFeature of a Feature is the second chainingFeature of the crossedFeature of the ownedCrossSubsetting of the Feature, if any."
        ),
        MetaConstraint(
            name = "deriveFeatureFeatureTarget",
            type = ConstraintType.DERIVATION,
            expression = "if chainingFeature->isEmpty() then self else chainingFeature->last() endif",
            description = "If a Feature has no chainingFeatures, then its featureTarget is the Feature itself, otherwise the featureTarget is the last of the chainingFeatures."
        ),
        MetaConstraint(
            name = "deriveFeatureFeaturingType",
            type = ConstraintType.DERIVATION,
            expression = """
                let featuringTypes: OrderedSet(Type) =
                    ownedTypeFeaturing.featuringType->asOrderedSet() in
                if chainingFeature->isEmpty() then featuringTypes
                else
                    featuringTypes->
                    union(chainingFeature->first().featuringType)->
                    asOrderedSet()
                endif
            """.trimIndent(),
            description = "The featuringTypes of a Feature include the featuringTypes of all the typeFeaturings of the Feature. If the Feature has chainingFeatures, then its featuringTypes also include the featuringTypes of the first chainingFeature."
        ),
        MetaConstraint(
            name = "deriveFeatureOwnedCrossSubsetting",
            type = ConstraintType.DERIVATION,
            expression = """
                let crossSubsettings: Sequence(CrossSubsetting) =
                    ownedSubsetting->selectByKind(CrossSubsetting) in
                if crossSubsettings->isEmpty() then null
                else crossSubsettings->first()
                endif
            """.trimIndent(),
            description = "The ownedCrossSubsetting of a Feature is the ownedSubsetting that is a CrossSubsetting, if any."
        ),
        MetaConstraint(
            name = "deriveFeatureOwningFeatureMembership",
            type = ConstraintType.DERIVATION,
            expression = "if owningMembership.oclIsKindOf(FeatureMembership) then owningMembership.oclAsType(FeatureMembership) else null endif",
            description = "The owningFeatureMembership is the owningMembership if it is a FeatureMembership, otherwise null."
        ),
        MetaConstraint(
            name = "deriveFeatureOwningType",
            type = ConstraintType.DERIVATION,
            expression = """
                if owningFeatureMembership <> null then
                    owningFeatureMembership.owningType
                else
                    null
                endif
            """.trimIndent(),
            description = "The Type that is the owningType of the owningFeatureMembership of this Feature."
        ),
        MetaConstraint(
            name = "deriveFeatureOwnedFeatureChaining",
            type = ConstraintType.DERIVATION,
            expression = "ownedRelationship->selectByKind(FeatureChaining)",
            description = "The ownedFeatureChaininings of a Feature are the ownedRelationships that are FeatureChaininings."
        ),
        MetaConstraint(
            name = "deriveFeatureOwnedFeatureInverting",
            type = ConstraintType.DERIVATION,
            expression = "ownedRelationship->selectByKind(FeatureInverting)->select(fi | fi.featureInverted = self)",
            description = "The ownedFeatureInvertings of a Feature are its ownedRelationships that are FeatureInvertings."
        ),
        MetaConstraint(
            name = "deriveFeatureOwnedRedefinition",
            type = ConstraintType.DERIVATION,
            expression = "ownedSubsetting->selectByKind(Redefinition)",
            description = "The ownedRedefinitions of a Feature are its ownedSubsettings that are Redefinitions."
        ),
        MetaConstraint(
            name = "deriveFeatureOwnedReferenceSubsetting",
            type = ConstraintType.DERIVATION,
            expression = """
                let referenceSubsettings: OrderedSet(ReferenceSubsetting) =
                    ownedSubsetting->selectByKind(ReferenceSubsetting) in
                if referenceSubsettings->isEmpty() then null
                else referenceSubsettings->first()
                endif
            """.trimIndent(),
            description = "The ownedReferenceSubsetting of a Feature is the first ownedSubsetting that is a ReferenceSubsetting (if any)."
        ),
        MetaConstraint(
            name = "deriveFeatureOwnedSubsetting",
            type = ConstraintType.DERIVATION,
            expression = "ownedSpecialization->selectByKind(Subsetting)",
            description = "The ownedSubsettings of a Feature are its ownedSpecializations that are Subsettings."
        ),
        MetaConstraint(
            name = "deriveFeatureOwnedTypeFeaturing",
            type = ConstraintType.DERIVATION,
            expression = "ownedRelationship->selectByKind(TypeFeaturing)->select(tf | tf.featureOfType = self)",
            description = "The ownedTypeFeaturings of a Feature are its ownedRelationships that are TypeFeaturings and which have the Feature as their featureOfType."
        ),
        MetaConstraint(
            name = "deriveFeatureOwnedTyping",
            type = ConstraintType.DERIVATION,
            expression = "ownedSpecialization->selectByKind(FeatureTyping)",
            description = "The ownedTypings of a Feature are its ownedSpecializations that are FeatureTypings. Note: There is an error in the specification (ownedGeneralization is a property that doesn't exist)."
        ),
        MetaConstraint(
            name = "deriveFeatureType",
            type = ConstraintType.DERIVATION,
            expression = "let types: OrderedSet(Type) = OrderedSet{self}->closure(typingFeatures()).typing.type->asOrderedSet() in types->reject(t1 | types->exists(t2 | t2 <> t1 and t2.specializes(t1)))",
            description = "The types of a Feature are the union of the types of its typings and the types of the Features it subsets, with all redundant supertypes removed. If the Feature has chainingFeatures, then the union also includes the types of the last chainingFeature."
        ),
        MetaConstraint(
            name = "validateFeatureChainingFeatureConformance",
            type = ConstraintType.VERIFICATION,
            expression = """
                Sequence{2..chainingFeature->size()}->forAll(i |
                    chainingFeature->at(i).isFeaturedWithin(chainingFeature->at(i-1)))
            """.trimIndent(),
            description = "Each chainingFeature (other than the first) must be featured within the previous chainingFeature."
        ),
        MetaConstraint(
            name = "validateFeatureChainingFeatureNotOne",
            type = ConstraintType.VERIFICATION,
            expression = "chainingFeature->size() <> 1",
            description = "A Feature must have either no chainingFeatures or more than one."
        ),
        MetaConstraint(
            name = "validateFeatureChainingFeaturesNotSelf",
            type = ConstraintType.VERIFICATION,
            expression = "chainingFeature->excludes(self)",
            description = "A Feature cannot be one of its own chainingFeatures."
        ),
        MetaConstraint(
            name = "validateFeatureConstantIsVariable",
            type = ConstraintType.VERIFICATION,
            expression = "isConstant implies isVariable",
            description = "A Feature with isConstant = true must have isVariable = true."
        ),
        MetaConstraint(
            name = "validateFeatureCrossFeatureSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                crossFeature <> null implies
                    ownedRedefinition.redefinedFeature.crossFeature->
                    forAll(f | f <> null implies crossFeature.specializes(f))
            """.trimIndent(),
            description = "If this Feature has a crossFeature, then, for any Feature that is redefined by this Feature, the crossFeature must specialize the crossFeature of the redefined endFeature, if this exists."
        ),
        MetaConstraint(
            name = "validateFeatureCrossFeatureType",
            type = ConstraintType.VERIFICATION,
            expression = "crossFeature <> null implies crossFeature.type->asSet() = type->asSet()",
            description = "The crossFeature of a Feature must have the same types as the Feature."
        ),
        MetaConstraint(
            name = "validateFeatureEndIsConstant",
            type = ConstraintType.VERIFICATION,
            expression = "isEnd and isVariable implies isConstant",
            description = "A Feature with isEnd = true and isVariable = true must have isConstant = true."
        ),
        MetaConstraint(
            name = "validateFeatureEndMultiplicity",
            type = ConstraintType.VERIFICATION,
            expression = """
                isEnd implies
                    multiplicities().allSupertypes()->flatten()->
                    selectByKind(MultiplicityRange)->exists(hasBounds(1,1))
            """.trimIndent(),
            description = "If a Feature has isEnd = true, then it must have multiplicity 1..1."
        ),
        MetaConstraint(
            name = "validateFeatureEndNoDirection",
            type = ConstraintType.VERIFICATION,
            expression = "isEnd implies direction = null",
            description = "A Feature with isEnd = true must have no direction."
        ),
        MetaConstraint(
            name = "validateFeatureEndNotDerivedAbstractCompositeOrPortion",
            type = ConstraintType.VERIFICATION,
            expression = "isEnd implies not (isDerived or isAbstract or isComposite or isPortion)",
            description = "A Feature with isEnd = true must have all of isDerived = false, isAbstract = false, isComposite = false, and isPortion = false."
        ),
        MetaConstraint(
            name = "validateFeatureIsVariable",
            type = ConstraintType.VERIFICATION,
            expression = """
                isVariable implies
                    owningType <> null and
                    owningType.specializesFromLibrary('Occurrences::Occurrence')
            """.trimIndent(),
            description = "A Feature with isVariable = true must have an owningType that directly or indirectly specializes the Class Occurrences::Occurrence from the Kernel Semantic Library."
        ),
        MetaConstraint(
            name = "validateFeatureMultiplicityDomain",
            type = ConstraintType.VERIFICATION,
            expression = "multiplicity <> null implies multiplicity.featuringType = featuringType",
            description = "If a Feature has a multiplicity, then the featuringTypes of the multiplicity must be the same as those of the Feature itself."
        ),
        MetaConstraint(
            name = "validateFeatureOwnedCrossSubsetting",
            type = ConstraintType.VERIFICATION,
            expression = "ownedSubsetting->selectByKind(CrossSubsetting)->size() <= 1",
            description = "A Feature must have at most one ownedSubsetting that is a CrossSubsetting."
        ),
        MetaConstraint(
            name = "validateFeatureOwnedReferenceSubsetting",
            type = ConstraintType.VERIFICATION,
            expression = "ownedSubsetting->selectByKind(ReferenceSubsetting)->size() <= 1",
            description = "A Feature must have at most one ownedSubsetting that is a ReferenceSubsetting."
        ),
        MetaConstraint(
            name = "validateFeaturePortionNotVariable",
            type = ConstraintType.VERIFICATION,
            expression = "isPortion implies not isVariable",
            description = "A Feature with isPortion = true must not have isVariable = true."
        ),
        MetaConstraint(
            name = "computeFeatureBaseFeature",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "FeatureChaining.allInstances()->select(fc | fc.featureTarget = self)",
            isNormative = false,
            description = "The FeatureChaininings that have this Feature as their featureTarget."
        ),
        MetaConstraint(
            name = "computeFeatureChainedFeature",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "FeatureChaining.allInstances()->select(fc | fc.chainingFeature = self)",
            isNormative = false,
            description = "The Features that have this Feature in their chainingFeatures."
        ),
        MetaConstraint(
            name = "computeFeatureChainExpression",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "FeatureChainExpression.allInstances()->select(fce | fce.targetFeature = self)",
            isNormative = false,
            description = "The FeatureChainExpressions that have this Feature as their targetFeature."
        ),
        MetaConstraint(
            name = "computeFeatureComputingExpression",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "Expression.allInstances()->select(e | e.result = self)",
            isNormative = false,
            description = "The Expressions that have this Feature as their result."
        ),
        MetaConstraint(
            name = "computeFeatureComputingFunction",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "Function.allInstances()->select(f | f.result = self)",
            isNormative = false,
            description = "The Functions that have this Feature as their result."
        ),
        MetaConstraint(
            name = "computeFeatureEndOwningType",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "EndFeatureMembership.allInstances()->select(efm | efm.ownedMemberFeature = self).owningType->any(true)",
            description = "The Type that is related to this Feature by an EndFeatureMembership in which the Feature is an ownedMemberFeature."
        ),
        MetaConstraint(
            name = "computeFeatureFlowFromOutput",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "Flow.allInstances()->select(f | f.sourceOutputFeature = self)",
            isNormative = false,
            description = "The Flows that have this Feature as their sourceOutputFeature."
        ),
        MetaConstraint(
            name = "computeFeatureFlowToInput",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "Flow.allInstances()->select(f | f.targetInputFeature = self)",
            isNormative = false,
            description = "The Flows that have this Feature as their targetInputFeature."
        ),
        MetaConstraint(
            name = "computeFeatureInheritingType",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "Type.allInstances()->select(t | t.inheritedFeature->includes(self))",
            isNormative = false,
            description = "The Types that have this Feature as an inherited feature."
        ),
        MetaConstraint(
            name = "computeFeatureOwningEndFeatureMembership",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "if owningFeatureMembership.oclIsKindOf(EndFeatureMembership) then owningFeatureMembership.oclAsType(EndFeatureMembership) else null endif",
            isNormative = false,
            description = "The EndFeatureMembership that owns this Feature, if owningFeatureMembership is an EndFeatureMembership."
        ),
        MetaConstraint(
            name = "computeFeatureOwningParameterMembership",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "if owningFeatureMembership.oclIsKindOf(ParameterMembership) then owningFeatureMembership.oclAsType(ParameterMembership) else null endif",
            isNormative = false,
            description = "The ParameterMembership that owns this Feature, if owningFeatureMembership is a ParameterMembership."
        ),
        MetaConstraint(
            name = "computeFeatureReferenceExpression",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "FeatureReferenceExpression.allInstances()->select(fre | fre.referent = self)",
            isNormative = false,
            description = "The FeatureReferenceExpressions that have this Feature as their referent."
        ),
        MetaConstraint(
            name = "computeFeatureTypeWithDirectedFeature",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "Type.allInstances()->select(t | t.directedFeature->includes(self))",
            isNormative = false,
            description = "The Types that have this Feature as a directed feature."
        ),
        MetaConstraint(
            name = "computeFeatureTypeWithEndFeature",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "Type.allInstances()->select(t | t.endFeature->includes(self))",
            isNormative = false,
            description = "The Types that have this Feature as an end feature."
        ),
        MetaConstraint(
            name = "computeFeatureTypeWithFeature",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "Type.allInstances()->select(t | t.feature->includes(self))",
            isNormative = false,
            description = "The Types that have this Feature as a feature."
        ),
        MetaConstraint(
            name = "computeFeatureTypeWithInput",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "Type.allInstances()->select(t | t.input->includes(self))",
            isNormative = false,
            description = "The Types that have this Feature as an input."
        ),
        MetaConstraint(
            name = "computeFeatureTypeWithOutput",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "Type.allInstances()->select(t | t.output->includes(self))",
            isNormative = false,
            description = "The Types that have this Feature as an output."
        ),
        MetaConstraint(
            name = "computeFeatureValuation",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "FeatureValue.allInstances()->select(fv | fv.featureWithValue = self)->any(true)",
            isNormative = false,
            description = "The FeatureValue that provides the value for this Feature."
        )
    ),
    semanticBindings = listOf(
        // Core binding: all Features subset Base::things
        SemanticBinding(
            name = "featureThingsBinding",
            baseConcept = "Base::things",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.Default
        ),
        // Conditional: Feature typed by DataType subsets Base::dataValues
        SemanticBinding(
            name = "featureDataValueBinding",
            baseConcept = "Base::dataValues",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.TypedBy("DataType")
        ),
        // Conditional: Feature typed by Structure subsets Objects::objects
        SemanticBinding(
            name = "featureObjectBinding",
            baseConcept = "Objects::objects",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.TypedBy("Structure")
        ),
        // Conditional: Feature typed by Class subsets Occurrences::occurrences
        SemanticBinding(
            name = "featureOccurrenceBinding",
            baseConcept = "Occurrences::occurrences",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.TypedBy("Class")
        ),
        // Conditional: End feature of Association/Connector subsets Links::Link::participant
        SemanticBinding(
            name = "featureEndParticipantBinding",
            baseConcept = "Links::Link::participant",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.And(
                listOf(
                    BindingCondition.IsEnd,
                    BindingCondition.Or(
                        listOf(
                            BindingCondition.OwningTypeIs("Association"),
                            BindingCondition.OwningTypeIs("Connector")
                        )
                    )
                )
            )
        ),
        // Conditional: isPortion and typed by Class with Class owningType -> Occurrences::Occurrence::portions
        SemanticBinding(
            name = "featurePortionBinding",
            baseConcept = "Occurrences::Occurrence::portions",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.And(
                listOf(
                    BindingCondition.IsPortion,
                    BindingCondition.TypedBy("Class"),
                    BindingCondition.OwningTypeTypedBy("Class")
                )
            )
        ),
        // Conditional: isComposite and typed by Structure with Structure owningType -> Objects::Object::subobjects
        SemanticBinding(
            name = "featureSubobjectBinding",
            baseConcept = "Objects::Object::subobjects",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.And(
                listOf(
                    BindingCondition.IsComposite,
                    BindingCondition.TypedBy("Structure"),
                    BindingCondition.OwningTypeTypedBy("Structure")
                )
            )
        ),
        // Conditional: isComposite and typed by Class with Class owningType -> Occurrences::Occurrence::suboccurrences
        SemanticBinding(
            name = "featureSuboccurrenceBinding",
            baseConcept = "Occurrences::Occurrence::suboccurrences",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.And(
                listOf(
                    BindingCondition.IsComposite,
                    BindingCondition.TypedBy("Class"),
                    BindingCondition.OwningTypeTypedBy("Class")
                )
            )
        ),
        // TypeFeaturing: Feature owned via FeatureMembership is featured by owningType
        SemanticBinding(
            name = "featureTypeFeaturing",
            baseConcept = "self::owningType",
            bindingKind = BindingKind.TYPE_FEATURES,
            condition = BindingCondition.HasOwningFeatureMembership
        ),
        // Redefinition: End feature redefines corresponding end in supertypes
        SemanticBinding(
            name = "featureEndRedefinition",
            baseConcept = "self::supertypeEndFeature",
            bindingKind = BindingKind.REDEFINES,
            condition = BindingCondition.IsEndWithOwningType
        )
    ),
    description = "A type that is also a feature"
)
