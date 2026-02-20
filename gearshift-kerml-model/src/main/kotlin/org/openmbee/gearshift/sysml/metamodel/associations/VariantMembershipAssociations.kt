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
 * Figure 9: Variant Membership
 */
fun createVariantMembershipAssociations(): List<MetaAssociation> {

    // Definition has variantMembership : VariantMembership [0..*] {ordered, derived, composite, subsets ownedMembership}
    val owningVariationDefinitionVariantMembershipAssociation = MetaAssociation(
        name = "owningVariationDefinitionVariantMembershipAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningVariationDefinition",
            type = "Definition",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("membershipOwningNamespace"),
            derivationConstraint = "deriveVariantMembershipOwningVariationDefinition"
        ),
        targetEnd = MetaAssociationEnd(
            name = "variantMembership",
            type = "VariantMembership",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            aggregation = AggregationKind.COMPOSITE,
            subsets = listOf("ownedMembership"),
            derivationConstraint = "deriveDefinitionVariantMembership"
        )
    )

    // Definition has variant : Usage [0..*] {derived, subsets ownedMember}
    val owningVariationDefinitionVariantAllocationAssociation = MetaAssociation(
        name = "owningVariationDefinitionVariantAllocationAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningVariationDefinition",
            type = "Definition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningNamespace"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "variant",
            type = "Usage",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            subsets = listOf("ownedMember"),
            derivationConstraint = "deriveDefinitionVariant"
        )
    )

    // Usage has variantMembership : VariantMembership [0..*] {derived, composite, subsets ownedMembership}
    val owningVariationUsageVariantMembershipAssociation = MetaAssociation(
        name = "owningVariationUsageVariantMembershipAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningVariationUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("membershipOwningNamespace"),
            derivationConstraint = "deriveVariantMembershipOwningVariationUsage"
        ),
        targetEnd = MetaAssociationEnd(
            name = "variantMembership",
            type = "VariantMembership",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            aggregation = AggregationKind.COMPOSITE,
            subsets = listOf("ownedMembership"),
            derivationConstraint = "deriveUsageVariantMembership"
        )
    )

    // VariantMembership has ownedVariantUsage : Usage [1..1] {derived, composite, redefines ownedMemberElement}
    val owningVariantMembershipOwnedVariantUsageAssociation = MetaAssociation(
        name = "owningVariantMembershipOwnedVariantUsageAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningVariantMembership",
            type = "VariantMembership",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("owningMembership")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedVariantUsage",
            type = "Usage",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            aggregation = AggregationKind.COMPOSITE,
            redefines = listOf("ownedMemberElement")
        )
    )

    // Usage has variant : Usage [0..*] {derived, subsets ownedMember}
    val owningVariationUsageVariantAssociation = MetaAssociation(
        name = "owningVariationUsageVariantAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningVariationUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningNamespace"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "variant",
            type = "Usage",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            subsets = listOf("ownedMember"),
            derivationConstraint = "deriveUsageVariant"
        )
    )

    return listOf(
        owningVariantMembershipOwnedVariantUsageAssociation,
        owningVariationDefinitionVariantAllocationAssociation,
        owningVariationDefinitionVariantMembershipAssociation,
        owningVariationUsageVariantAssociation,
        owningVariationUsageVariantMembershipAssociation
    )
}
