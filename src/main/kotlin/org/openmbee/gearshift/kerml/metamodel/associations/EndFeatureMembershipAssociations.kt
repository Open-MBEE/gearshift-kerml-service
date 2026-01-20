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

import org.openmbee.gearshift.metamodel.AggregationKind
import org.openmbee.gearshift.metamodel.MetaAssociation
import org.openmbee.gearshift.metamodel.MetaAssociationEnd

/**
 * Figure 21: End Feature Membership
 * Defines associations for End Feature Membership.
 */
fun createEndFeatureMembershipAssociations(): List<MetaAssociation> {

    // EndFeatureMembership has ownedMemberFeature : Feature [1..1] {derived, redefines ownedMemberFeature}
    val owningEndFeatureMembershipOwnedMemberFeatureAssociation = MetaAssociation(
        name = "owningEndFeatureMembershipOwnedMemberFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningEndFeatureMembership",
            type = "EndFeatureMembership",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isNavigable = false,
            redefines = listOf("owningFeatureMembership")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedMemberFeature",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            aggregation = AggregationKind.COMPOSITE,
            isDerived = true,
            redefines = listOf("ownedMemberFeature"),
            derivationConstraint = "deriveEndFeatureMembershipOwnedMemberFeature"
        )
    )

    return listOf(
        owningEndFeatureMembershipOwnedMemberFeatureAssociation
    )
}
