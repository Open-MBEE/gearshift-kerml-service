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
package org.openmbee.gearshift.sysml.metamodel.associations

import org.openmbee.mdm.framework.meta.AggregationKind
import org.openmbee.mdm.framework.meta.MetaAssociation
import org.openmbee.mdm.framework.meta.MetaAssociationEnd

/**
 * Figure 31: State Membership
 */
fun createStateMembershipAssociations(): List<MetaAssociation> {

    // StateSubactionMembership has action : ActionUsage [1..1] {derived, redefines ownedMemberFeature}
    val stateSubactionMembershipActionAssociation = MetaAssociation(
        name = "stateSubactionMembershipActionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "stateSubactionMembership",
            type = "StateSubactionMembership",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningFeatureMembership"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "action",
            type = "ActionUsage",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            aggregation = AggregationKind.COMPOSITE,
            redefines = listOf("ownedMemberFeature"),
            derivationConstraint = "deriveStateSubactionMembershipAction"
        )
    )

    // TransitionFeatureMembership has transitionFeature : Step [1..1] {derived, redefines ownedMemberFeature}
    val transitionFeatureMembershipTransitionFeatureActionAssociation = MetaAssociation(
        name = "transitionFeatureMembershipTransitionFeatureActionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "transitionFeatureMembership",
            type = "TransitionFeatureMembership",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            aggregation = AggregationKind.COMPOSITE,
            subsets = listOf("owningFeatureMembership"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "transitionFeature",
            type = "Step",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            redefines = listOf("ownedMemberFeature"),
            derivationConstraint = "deriveTransitionFeatureMembershipTransitionFeature"
        )
    )

    return listOf(
        stateSubactionMembershipActionAssociation,
        transitionFeatureMembershipTransitionFeatureActionAssociation
    )
}
