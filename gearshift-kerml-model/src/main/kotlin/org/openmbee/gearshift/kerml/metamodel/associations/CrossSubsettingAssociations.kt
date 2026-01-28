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

import org.openmbee.gearshift.framework.meta.AggregationKind
import org.openmbee.gearshift.framework.meta.MetaAssociation
import org.openmbee.gearshift.framework.meta.MetaAssociationEnd

/**
 * Figure 22: Cross Subsetting
 * Defines associations for Cross Subsetting relationships.
 */
fun createCrossSubsettingAssociations(): List<MetaAssociation> {

    // CrossSubsetting has crossedFeature : Feature [1..1] {redefines subsettedFeature}
    val crossSupersettingCrossedFeatureAssociation = MetaAssociation(
        name = "crossSupersettingCrossedFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "crossSupersetting",
            type = "CrossSubsetting",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            subsets = listOf("supersetting")
        ),
        targetEnd = MetaAssociationEnd(
            name = "crossedFeature",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("subsettedFeature")
        )
    )

    // Feature has ownedCrossSubsetting : CrossSubsetting [0..1] {derived, subsets ownedSubsetting}
    val crossingFeatureOwnedCrossSubsettingAssociation = MetaAssociation(
        name = "crossingFeatureOwnedCrossSubsettingAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "crossingFeature",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            redefines = listOf("owningFeature", "subsettingFeature")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedCrossSubsetting",
            type = "CrossSubsetting",
            lowerBound = 0,
            upperBound = 1,
            aggregation = AggregationKind.COMPOSITE,
            isDerived = true,
            subsets = listOf("ownedSubsetting"),
            derivationConstraint = "deriveFeatureOwnedCrossSubsetting"
        )
    )

    // Feature has crossFeature : Feature [0..1] {derived}
    val featureCrossingCrossFeatureAssociation = MetaAssociation(
        name = "featureCrossingCrossFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "featureCrossing",
            type = "Feature",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false
        ),
        targetEnd = MetaAssociationEnd(
            name = "crossFeature",
            type = "Feature",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveFeatureCrossFeature"
        )
    )

    return listOf(
        crossSupersettingCrossedFeatureAssociation,
        crossingFeatureOwnedCrossSubsettingAssociation,
        featureCrossingCrossFeatureAssociation
    )
}
