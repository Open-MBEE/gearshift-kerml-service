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
 * Figure 20: Feature Inverting
 * Defines associations for Feature Inverting relationships.
 */
fun createFeatureInvertingAssociations(): List<MetaAssociation> {

    // FeatureInverting references the featureInverted Feature (source)
    val invertingFeatureInvertingFeatureInvertedAssociation = MetaAssociation(
        name = "invertingFeatureInvertingFeatureInvertedAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "invertingFeatureInverting",
            type = "FeatureInverting",
            lowerBound = 0,
            upperBound = -1,
            subsets = listOf("sourceRelationship")
        ),
        targetEnd = MetaAssociationEnd(
            name = "featureInverted",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("source")
        )
    )

    // FeatureInverting references the invertingFeature Feature (target)
    val invertedFeatureInvertingInvertingFeatureAssociation = MetaAssociation(
        name = "invertedFeatureInvertingInvertingFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "invertedFeatureInverting",
            type = "FeatureInverting",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("targetRelationship")
        ),
        targetEnd = MetaAssociationEnd(
            name = "invertingFeature",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("target")
        )
    )

    // Feature owns its FeatureInverting relationships (composite)
    val owningFeatureOwnedFeatureInvertingAssociation = MetaAssociation(
        name = "owningFeatureOwnedFeatureInvertingAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningFeature",
            type = "Feature",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("featureInverted", "owningRelatedElement"),
            derivationConstraint = "deriveFeatureInvertingOwningFeature"
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedFeatureInverting",
            type = "FeatureInverting",
            lowerBound = 0,
            upperBound = -1,
            aggregation = AggregationKind.COMPOSITE,
            isDerived = true,
            subsets = listOf("invertingFeatureInverting", "ownedRelationship"),
            derivationConstraint = "deriveFeatureOwnedFeatureInverting"
        )
    )

    return listOf(
        invertingFeatureInvertingFeatureInvertedAssociation,
        invertedFeatureInvertingInvertingFeatureAssociation,
        owningFeatureOwnedFeatureInvertingAssociation
    )
}
