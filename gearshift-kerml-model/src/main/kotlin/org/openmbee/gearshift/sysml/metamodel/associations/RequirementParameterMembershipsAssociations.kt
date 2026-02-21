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
 * Figure 41: Requirement Parameter Memberships
 */
fun createRequirementParameterMembershipsAssociations(): List<MetaAssociation> {

    // SubjectMembership has ownedSubjectParameter : Usage [1..1] {derived, redefines ownedMemberParameter}
    val owningSubjectMembershipOwnedSubjectParameterAssociation = MetaAssociation(
        name = "owningSubjectMembershipOwnedSubjectParameterAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningSubjectMembership",
            type = "SubjectMembership",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningParameterMembership"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedSubjectParameter",
            type = "Usage",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            aggregation = AggregationKind.COMPOSITE,
            redefines = listOf("ownedMemberParameter"),
            derivationConstraint = "deriveSubjectMembershipOwnedSubjectParameter"
        )
    )

    // ActorMembership has ownedActorParameter : PartUsage [1..1] {derived, redefines ownedMemberParameter}
    val owningActorMembershipOwnedActorParameterAssociation = MetaAssociation(
        name = "owningActorMembershipOwnedActorParameterAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningActorMembership",
            type = "ActorMembership",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningParameterMembership"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedActorParameter",
            type = "PartUsage",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            aggregation = AggregationKind.COMPOSITE,
            redefines = listOf("ownedMemberParameter"),
            derivationConstraint = "deriveActorMembershipOwnedActorParameter"
        )
    )

    // StakeholderMembership has ownedStakeholderParameter : PartUsage [1..1] {derived, redefines ownedMemberParameter}
    val owningStakeholderMembershipOwnedStakeholderParameterAssociation = MetaAssociation(
        name = "owningStakeholderMembershipOwnedStakeholderParameterAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningStakeholderMembership",
            type = "StakeholderMembership",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningParameterMembership"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedStakeholderParameter",
            type = "PartUsage",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            aggregation = AggregationKind.COMPOSITE,
            redefines = listOf("ownedMemberParameter"),
            derivationConstraint = "deriveStakeholderMembershipOwnedStakeholderParameter"
        )
    )

    return listOf(
        owningActorMembershipOwnedActorParameterAssociation,
        owningStakeholderMembershipOwnedStakeholderParameterAssociation,
        owningSubjectMembershipOwnedSubjectParameterAssociation
    )
}
