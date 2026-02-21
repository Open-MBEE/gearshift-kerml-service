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
 * Figure 40: Requirement Constraint Membership
 */
fun createRequirementConstraintMembershipAssociations(): List<MetaAssociation> {

    // RequirementConstraintMembership has ownedConstraint : ConstraintUsage [1..1] {derived, redefines ownedMemberFeature}
    val requirementConstraintMembershipOwnedConstraintAssociation = MetaAssociation(
        name = "requirementConstraintMembershipOwnedConstraintAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "requirementConstraintMembership",
            type = "RequirementConstraintMembership",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningFeatureMembership"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedConstraint",
            type = "ConstraintUsage",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            aggregation = AggregationKind.COMPOSITE,
            redefines = listOf("ownedMemberFeature"),
            derivationConstraint = "deriveRequirementConstraintMembershipOwnedConstraint"
        )
    )

    // RequirementConstraintMembership has referencedConstraint : ConstraintUsage [1..1] {derived}
    val referencingConstraintMembershipReferencedConstraintAssociation = MetaAssociation(
        name = "referencingConstraintMembershipReferencedConstraintAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "referencingConstraintMembership",
            type = "RequirementConstraintMembership",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "referencedConstraint",
            type = "ConstraintUsage",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveRequirementConstraintMembershipReferencedConstraint"
        )
    )

    // FramedConcernMembership has ownedConcern : ConcernUsage [1..1] {derived, redefines ownedConstraint}
    val framedConstraintMembershipOwnedConcernAssociation = MetaAssociation(
        name = "framedConstraintMembershipOwnedConcernAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "framedConstraintMembership",
            type = "FramedConcernMembership",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("requirementConstraintMembership"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedConcern",
            type = "ConcernUsage",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            aggregation = AggregationKind.COMPOSITE,
            redefines = listOf("ownedConstraint"),
            derivationConstraint = "deriveFramedConcernMembershipOwnedConcern"
        )
    )

    // FramedConcernMembership has referencedConcern : ConcernUsage [1..1] {derived, redefines referencedConstraint}
    val referencingConcernReferencedConcernAssociation = MetaAssociation(
        name = "referencingConcernReferencedConcernAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "referencingConcern",
            type = "FramedConcernMembership",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("referencingConstraintMembership"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "referencedConcern",
            type = "ConcernUsage",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            redefines = listOf("referencedConstraint"),
            derivationConstraint = "deriveFramedConcernMembershipReferencedConcern"
        )
    )

    return listOf(
        framedConstraintMembershipOwnedConcernAssociation,
        referencingConcernReferencedConcernAssociation,
        referencingConstraintMembershipReferencedConstraintAssociation,
        requirementConstraintMembershipOwnedConstraintAssociation
    )
}
