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
 * Figure 19: Feature Chaining
 * Defines associations for Feature Chaining relationships.
 */
fun createFeatureChainingAssociations(): List<MetaAssociation> {

    // FeatureChaining references the featureChained Feature (source)
    val baseFeatureFeatureTargetAssociation = MetaAssociation(
        name = "baseFeatureFeatureTargetAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "baseFeature",
            type = "FeatureChaining",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
        ),
        targetEnd = MetaAssociationEnd(
            name = "featureTarget",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveFeatureFeatureTarget"
        )
    )

    // Feature has chainingFeatures (derived, through FeatureChaining)
    val chainedFeatureChainingFeatureAssociation = MetaAssociation(
        name = "chainedFeatureChainingFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "chainedFeature",
            type = "Feature",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false
        ),
        targetEnd = MetaAssociationEnd(
            name = "chainingFeature",
            type = "Feature",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            isUnique = false,
            derivationConstraint = "deriveFeatureChainingFeature"
        )
    )

    // Feature owns its FeatureChaining relationships (composite)
    val featureChainedOwnedFeatureChainingAssociation = MetaAssociation(
        name = "featureChainedOwnedFeatureChainingAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "featureChained",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            redefines = listOf("source"),
            subsets = listOf("owningRelatedElement")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedFeatureChaining",
            type = "FeatureChaining",
            lowerBound = 0,
            upperBound = -1,
            aggregation = AggregationKind.COMPOSITE,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("ownedRelationship", "sourceRelationship"),
            derivationConstraint = "deriveFeatureOwnedFeatureChaining"
        )
    )

    // FeatureChaining references the chainingFeature Feature (target)
    val chainedFeatureChainingChainingFeatureAssociation = MetaAssociation(
        name = "chainedFeatureChainingChainingFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "chainedFeatureChaining",
            type = "FeatureChaining",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("targetRelationship")
        ),
        targetEnd = MetaAssociationEnd(
            name = "chainingFeature",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("target")
        )
    )

    return listOf(
        baseFeatureFeatureTargetAssociation,
        chainedFeatureChainingFeatureAssociation,
        featureChainedOwnedFeatureChainingAssociation,
        chainedFeatureChainingChainingFeatureAssociation
    )
}
