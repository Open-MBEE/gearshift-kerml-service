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

import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.MetaProperty

/**
 * SysML TransitionFeatureMembership metaclass.
 * Specializes: FeatureMembership
 * A TransitionFeatureMembership is a FeatureMembership for a trigger, guard or effect of a
 * TransitionUsage, whose transitionFeature is an AcceptActionUsage, Boolean-valued Expression or
 * ActionUsage, depending on its kind.
 */
fun createTransitionFeatureMembershipMetaClass() = MetaClass(
    name = "TransitionFeatureMembership",
    isAbstract = false,
    superclasses = listOf("FeatureMembership"),
    attributes = listOf(
        MetaProperty(
            name = "kind",
            type = "TransitionFeatureKind",
            lowerBound = 1,
            upperBound = 1,
            description = "Whether this TransitionFeatureMembership is for a trigger, guard or effect."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "validateTransitionFeatureMembershipEffectAction",
            type = ConstraintType.VERIFICATION,
            expression = """
                kind = TransitionFeatureKind::effect implies
                transitionFeature.oclIsKindOf(ActionUsage)
            """.trimIndent(),
            description = "If the kind of a TransitionFeatureMembership is effect, then its transitionFeature must be a kind of ActionUsage."
        ),
        MetaConstraint(
            name = "validateTransitionFeatureMembershipGuardExpression",
            type = ConstraintType.VERIFICATION,
            expression = """
                kind = TransitionFeatureKind::guard implies
                transitionFeature.oclIsKindOf(Expression) and
                let guard : Expression = transitionFeature.oclAsType(Expression) in
                guard.result.specializesFromLibrary('ScalarValues::Boolean') and
                guard.result.multiplicity <> null and
                guard.result.multiplicity.hasBounds(1, 1)
            """.trimIndent(),
            description = "If the kind of a TransitionFeatureMembership is guard, then its transitionFeature must be an Expression whose result is a Boolean value."
        ),
        MetaConstraint(
            name = "validateTransitionFeatureMembershipOwningType",
            type = ConstraintType.VERIFICATION,
            expression = "owningType.oclIsKindOf(TransitionUsage)",
            description = "The owningType of a TransitionFeatureMembership must be a TransitionUsage."
        ),
        MetaConstraint(
            name = "validateTransitionFeatureMembershipTriggerAction",
            type = ConstraintType.VERIFICATION,
            expression = """
                kind = TransitionFeatureKind::trigger implies
                transitionFeature.oclIsKindOf(AcceptActionUsage)
            """.trimIndent(),
            description = "If the kind of a TransitionFeatureMembership is trigger, then its transitionFeature must be a kind of AcceptActionUsage."
        )
    ),
    description = "A TransitionFeatureMembership is a FeatureMembership for a trigger, guard or effect of a TransitionUsage."
)
