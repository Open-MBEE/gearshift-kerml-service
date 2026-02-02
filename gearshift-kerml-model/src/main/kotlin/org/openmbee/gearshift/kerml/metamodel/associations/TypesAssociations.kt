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
package org.openmbee.gearshift.kerml.metamodel.associations

import org.openmbee.mdm.framework.meta.AggregationKind
import org.openmbee.mdm.framework.meta.MetaAssociation
import org.openmbee.mdm.framework.meta.MetaAssociationEnd

/**
 * Figure 9: Types
 * Defines associations for Type metaclass.
 */
fun createTypeAssociations(): List<MetaAssociation> {

    // Type inherits Memberships from supertypes (derived)
    val inheritingTypeInheritedMembershipAssociation = MetaAssociation(
        name = "inheritingTypeInheritedMembershipAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "inheritingType",
            type = "Type",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("membershipNamespace"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "inheritedMembership",
            type = "Membership",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("membership"),
            derivationConstraint = "deriveTypeInheritedMembership"
        )
    )

    // Type has FeatureMemberships (derived union)
    val typeFeatureMembershipAssociation = MetaAssociation(
        name = "typeFeatureMembershipAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "type",
            type = "Type",
            lowerBound = 1,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            isUnion = true,
        ),
        targetEnd = MetaAssociationEnd(
            name = "featureMembership",
            type = "FeatureMembership",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            derivationConstraint = "deriveTypeFeatureMembership"
        )
    )

    // Type owns FeatureMemberships (composite)
    val owningTypeOwnedFeatureMembershipAssociation = MetaAssociation(
        name = "owningTypeOwnedFeatureMembershipAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningType",
            type = "Type",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("type"),
            redefines = listOf("membershipOwningNamespace")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedFeatureMembership",
            type = "FeatureMembership",
            lowerBound = 0,
            upperBound = -1,
            aggregation = AggregationKind.COMPOSITE,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("featureMembership", "ownedMembership"),
            derivationConstraint = "deriveTypeOwnedFeatureMembership"
        )
    )

    // FeatureMembership owns its member Feature (composite)
    val owningFeatureMembershipOwnedMemberFeatureAssociation = MetaAssociation(
        name = "owningFeatureMembershipOwnedMemberFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningFeatureMembership",
            type = "FeatureMembership",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("owningMembership")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedMemberFeature",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            aggregation = AggregationKind.COMPOSITE,
            isDerived = true,
            redefines = listOf("ownedMemberElement"),
        )
    )

    // Type has features (derived)
    val typeWithFeatureFeatureAssociation = MetaAssociation(
        name = "typeWithFeatureFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "typeWithFeature",
            type = "Type",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("namespace")
        ),
        targetEnd = MetaAssociationEnd(
            name = "feature",
            type = "Feature",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("member"),
            derivationConstraint = "deriveTypeFeature"
        )
    )

    // Type owns features (derived)
    val owningTypeOwnedFeatureAssociation = MetaAssociation(
        name = "owningTypeOwnedFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningType",
            type = "Type",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("featuringType", "owningNamespace", "typeWithFeature")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedFeature",
            type = "Feature",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("ownedMember"),
            derivationConstraint = "deriveTypeOwnedFeature"
        )
    )

    // Type has directed features (derived, direction != null)
    val typeWithDirectedFeatureDirectedFeatureAssociation = MetaAssociation(
        name = "typeWithDirectedFeatureDirectedFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "typeWithDirectedFeature",
            type = "Type",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("typeWithFeature")
        ),
        targetEnd = MetaAssociationEnd(
            name = "directedFeature",
            type = "Feature",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("feature"),
            derivationConstraint = "deriveTypeDirectedFeature"
        )
    )

    // Type has input features (derived, direction = in or inout)
    val typeWithInputInputAssociation = MetaAssociation(
        name = "typeWithInputInputAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "typeWithInput",
            type = "Type",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("typeWithFeature")
        ),
        targetEnd = MetaAssociationEnd(
            name = "input",
            type = "Feature",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("directedFeature"),
            derivationConstraint = "deriveTypeInput"
        )
    )

    // Type has output features (derived, direction = out or inout)
    val typeWithOutputOutputAssociation = MetaAssociation(
        name = "typeWithOutputOutputAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "typeWithOutput",
            type = "Type",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("typeWithFeature")
        ),
        targetEnd = MetaAssociationEnd(
            name = "output",
            type = "Feature",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("directedFeature"),
            derivationConstraint = "deriveTypeOutput"
        )
    )

    // Type has end features (isEnd = true)
    val typeWithEndFeatureEndFeatureAssociation = MetaAssociation(
        name = "typeWithEndFeatureEndFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "typeWithEndFeature",
            type = "Type",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("typeWithFeature")
        ),
        targetEnd = MetaAssociationEnd(
            name = "endFeature",
            type = "Feature",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("feature"),
            derivationConstraint = "deriveTypeEndFeature"
        )
    )

    // Type owns end features (composite)
    val endOwningTypeOwnedEndFeatureAssociation = MetaAssociation(
        name = "endOwningTypeOwnedEndFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "endOwningType",
            type = "Type",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("typeWithEndFeature", "owningType")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedEndFeature",
            type = "Feature",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("endFeature", "ownedFeature"),
            derivationConstraint = "deriveTypeOwnedEndFeature"
        )
    )

    // Type inherits features from supertypes (derived)
    val inheritingTypeInheritedFeatureAssociation = MetaAssociation(
        name = "inheritingTypeInheritedFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "inheritingType",
            type = "Type",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("typeWithFeature")
        ),
        targetEnd = MetaAssociationEnd(
            name = "inheritedFeature",
            type = "Feature",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("feature"),
            derivationConstraint = "deriveTypeInheritedFeature"
        )
    )

    // Type has multiplicity (optional, 0..1)
    val typeWithMultiplicityMultiplicityAssociation = MetaAssociation(
        name = "typeWithMultiplicityMultiplicityAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "typeWithMultiplicity",
            type = "Type",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isNavigable = false
        ),
        targetEnd = MetaAssociationEnd(
            name = "multiplicity",
            type = "Multiplicity",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("ownedMember"),
            derivationConstraint = "deriveTypeMultiplicity"
        )
    )

    return listOf(
        inheritingTypeInheritedMembershipAssociation,
        typeFeatureMembershipAssociation,
        owningTypeOwnedFeatureMembershipAssociation,
        owningFeatureMembershipOwnedMemberFeatureAssociation,
        typeWithFeatureFeatureAssociation,
        owningTypeOwnedFeatureAssociation,
        typeWithDirectedFeatureDirectedFeatureAssociation,
        typeWithInputInputAssociation,
        typeWithOutputOutputAssociation,
        typeWithEndFeatureEndFeatureAssociation,
        endOwningTypeOwnedEndFeatureAssociation,
        inheritingTypeInheritedFeatureAssociation,
        typeWithMultiplicityMultiplicityAssociation
    )
}
