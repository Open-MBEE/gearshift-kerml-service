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
package org.openmbee.gearshift.sysml.metamodel.classes

import org.openmbee.mdm.framework.meta.BindingKind
import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.MetaOperation
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * SysML TransitionUsage metaclass.
 * Specializes: ActionUsage
 * A TransitionUsage is an ActionUsage representing a triggered transition between ActionUsages or
 * StateUsages. When triggered by a triggerAction, when its guardExpression is true, the
 * TransitionUsage asserts that its source is exited, then its effectAction (if any) is performed, and then its
 * target is entered.
 * A TransitionUsage can be related to some of its ownedFeatures using TransitionFeatureMembership
 * Relationships, corresponding to the triggerAction, guardExpression and effectAction of the TransitionUsage.
 */
fun createTransitionUsageMetaClass() = MetaClass(
    name = "TransitionUsage",
    isAbstract = false,
    superclasses = listOf("ActionUsage"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkTransitionUsageActionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isComposite and owningType <> null and
                (owningType.oclIsKindOf(ActionDefinition) or
                owningType.oclIsKindOf(ActionUsage)) and
                source <> null and not source.oclIsKindOf(StateUsage) implies
                specializesFromLibrary('Actions::Action::decisionTransitions')
            """.trimIndent(),
            description = "A composite TransitionUsage whose owningType is an ActionDefinition or ActionUsage and whose source is not a StateUsage must directly or indirectly specialize Actions::Action::decisionTransitions."
        ),
        MetaConstraint(
            name = "checkTransitionUsagePayloadSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                triggerAction->notEmpty() implies
                let payloadParameter : Feature = inputParameter(2) in
                payloadParameter <> null and
                payloadParameter.subsetsChain(triggerAction->at(1), triggerPayloadParameter())
            """.trimIndent(),
            description = "If a TransitionUsage has a triggerAction, then the payload parameter of the TransitionUsage subsets the Feature chain of the triggerAction and its payloadParameter."
        ),
        MetaConstraint(
            name = "checkTransitionUsageSourceBindingConnector",
            type = ConstraintType.VERIFICATION,
            expression = """
                ownedMember->selectByKind(BindingConnector)->exists(b |
                    b.relatedFeatures->includes(source) and
                    b.relatedFeatures->includes(inputParameter(1)))
            """.trimIndent(),
            description = "A TransitionUsage must have an ownedMember that is a BindingConnector between its source and its first input parameter."
        ),
        MetaConstraint(
            name = "checkTransitionUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Actions::transitionActions')",
            description = "A TransitionUsage must directly or indirectly specialize the ActionUsage Actions::transitionActions from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkTransitionUsageStateSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isComposite and owningType <> null and
                (owningType.oclIsKindOf(StateDefinition) or
                owningType.oclIsKindOf(StateUsage)) and
                source <> null and source.oclIsKindOf(StateUsage) implies
                specializesFromLibrary('States::StateAction::stateTransitions')
            """.trimIndent(),
            description = "A composite TransitionUsage whose owningType is a StateDefinition or StateUsage and whose source is a StateUsage must directly or indirectly specialize States::StateAction::stateTransitions."
        ),
        MetaConstraint(
            name = "checkTransitionUsageSuccessionBindingConnector",
            type = ConstraintType.VERIFICATION,
            expression = """
                ownedMember->selectByKind(BindingConnector)->exists(b |
                    b.relatedFeatures->includes(succession) and
                    b.relatedFeatures->includes(resolveGlobal(
                        'TransitionPerformances::TransitionPerformance::transitionLink')))
            """.trimIndent(),
            description = "A TransitionUsage must have an ownedMember that is a BindingConnector between its succession and the inherited Feature TransitionPerformances::TransitionPerformance::transitionLink."
        ),
        MetaConstraint(
            name = "checkTransitionUsageSuccessionSourceSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "succession.sourceFeature = source",
            description = "The sourceFeature of the succession of a TransitionUsage must be the source of the TransitionUsage."
        ),
        MetaConstraint(
            name = "checkTransitionUsageTransitionFeatureSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                triggerAction->forAll(specializesFromLibrary('Actions::TransitionAction::accepter')) and
                guardExpression->forAll(specializesFromLibrary('Actions::TransitionAction::guard')) and
                effectAction->forAll(specializesFromLibrary('Actions::TransitionAction::effect'))
            """.trimIndent(),
            description = "The triggerActions, guardExpressions, and effectActions of a TransitionUsage must specialize, respectively, the accepter, guard, and effect features of Actions::TransitionAction."
        ),
        MetaConstraint(
            name = "deriveTransitionUsageEffectAction",
            type = ConstraintType.DERIVATION,
            expression = """
                ownedFeatureMembership->
                    selectByKind(TransitionFeatureMembership)->
                    select(kind = TransitionFeatureKind::effect).transitionFeature->
                    selectByKind(ActionUsage)
            """.trimIndent(),
            description = "The effectActions of a TransitionUsage are the transitionFeatures of the ownedFeatureMemberships with kind = effect, which must all be ActionUsages."
        ),
        MetaConstraint(
            name = "deriveTransitionUsageGuardExpression",
            type = ConstraintType.DERIVATION,
            expression = """
                ownedFeatureMembership->
                    selectByKind(TransitionFeatureMembership)->
                    select(kind = TransitionFeatureKind::guard).transitionFeature->
                    selectByKind(Expression)
            """.trimIndent(),
            description = "The guardExpressions of a TransitionUsage are the transitionFeatures of the ownedFeatureMemberships with kind = guard, which must all be Expressions."
        ),
        MetaConstraint(
            name = "deriveTransitionUsageSource",
            type = ConstraintType.DERIVATION,
            expression = """
                let sourceFeature : Feature = sourceFeature() in
                if sourceFeature = null then null
                else sourceFeature.featureTarget.oclAsType(ActionUsage)
                endif
            """.trimIndent(),
            description = "The source of a TransitionUsage is the featureTarget of the result of sourceFeature(), which must be an ActionUsage."
        ),
        MetaConstraint(
            name = "deriveTransitionUsageSuccession",
            type = ConstraintType.DERIVATION,
            expression = "ownedMember->selectByKind(Succession)->at(1)",
            description = "The succession of a TransitionUsage is its first ownedMember that is a Succession."
        ),
        MetaConstraint(
            name = "deriveTransitionUsageTarget",
            type = ConstraintType.DERIVATION,
            expression = """
                if succession.targetFeature->isEmpty() then null
                else
                    let targetFeature : Feature =
                        succession.targetFeature->first().featureTarget in
                    if not targetFeature.oclIsKindOf(ActionUsage) then null
                    else targetFeature.oclAsType(ActionUsage)
                    endif
                endif
            """.trimIndent(),
            description = "The target of a TransitionUsage is given by the featureTarget of the targetFeature of its succession, which must be an ActionUsage."
        ),
        MetaConstraint(
            name = "deriveTransitionUsageTriggerAction",
            type = ConstraintType.DERIVATION,
            expression = """
                ownedFeatureMembership->
                    selectByKind(TransitionFeatureMembership)->
                    select(kind = TransitionFeatureKind::trigger).transitionFeature->
                    selectByKind(AcceptActionUsage)
            """.trimIndent(),
            description = "The triggerActions of a TransitionUsage are the transitionFeatures of the ownedFeatureMemberships with kind = trigger, which must all be AcceptActionUsages."
        ),
        MetaConstraint(
            name = "validateTransitionUsageParameters",
            type = ConstraintType.VERIFICATION,
            expression = """
                if triggerAction->isEmpty() then
                    inputParameters()->size() >= 1
                else
                    inputParameters()->size() >= 2
                endif
            """.trimIndent(),
            description = "A TransitionUsage must have at least one owned input parameter and, if it has a triggerAction, it must have at least two."
        ),
        MetaConstraint(
            name = "validateTransitionUsageSuccession",
            type = ConstraintType.VERIFICATION,
            expression = """
                let successions : Sequence(Succession) =
                    ownedMember->selectByKind(Succession) in
                successions->notEmpty() and
                successions->at(1).targetFeature.featureTarget->
                    forAll(oclIsKindOf(ActionUsage))
            """.trimIndent(),
            description = "A TransitionUsage must have an ownedMember that is a Succession with an ActionUsage as the featureTarget of its targetFeature."
        ),
        MetaConstraint(
            name = "validateTransitionUsageTriggerActions",
            type = ConstraintType.VERIFICATION,
            expression = """
                source <> null and not source.oclIsKindOf(StateUsage) implies
                triggerAction->isEmpty()
            """.trimIndent(),
            description = "If the source of a TransitionUsage is not a StateUsage, then the TransitionUsage must not have any triggerActions."
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "sourceFeature",
            returnType = "Feature",
            returnLowerBound = 0,
            returnUpperBound = 1,
            body = MetaOperation.ocl("""
                let features : Sequence(Feature) = ownedMembership->
                    reject(oclIsKindOf(FeatureMembership)).memberElement->
                    selectByKind(Feature)->
                    select(featureTarget.oclIsKindOf(ActionUsage)) in
                if features->isEmpty() then null
                else features->first()
                endif
            """.trimIndent()),
            description = "Return the Feature to be used as the source of the succession of this TransitionUsage."
        ),
        MetaOperation(
            name = "triggerPayloadParameter",
            returnType = "ReferenceUsage",
            returnLowerBound = 0,
            returnUpperBound = 1,
            body = MetaOperation.ocl("""
                if triggerAction->isEmpty() then null
                else triggerAction->first().payloadParameter
                endif
            """.trimIndent()),
            description = "Return the payloadParameter of the triggerAction of this TransitionUsage, if it has one."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "transitionUsageTransitionActionsBinding",
            baseConcept = "Actions::transitionActions",
            bindingKind = BindingKind.SUBSETS
        )
    ),
    description = "A TransitionUsage is an ActionUsage representing a triggered transition between ActionUsages or StateUsages."
)
