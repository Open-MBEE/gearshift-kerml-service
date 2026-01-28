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
 * Figure 18: Subsetting
 * Defines associations for Subsetting, Redefinition, and ReferenceSubsetting relationships.
 */
fun createSubsettingAssociations(): List<MetaAssociation> {

    // Subsetting references the subsettedFeature Feature (target/general)
    val supersettingSubsettedFeatureAssociation = MetaAssociation(
        name = "supersettingSubsettedFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "supersetting",
            type = "Subsetting",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("generalization")
        ),
        targetEnd = MetaAssociationEnd(
            name = "subsettedFeature",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("general")
        )
    )

    // Subsetting references the subsettingFeature Feature (source/specific)
    val subsettingSubsettingFeatureAssociation = MetaAssociation(
        name = "subsettingSubsettingFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "subsetting",
            type = "Subsetting",
            lowerBound = 0,
            upperBound = -1,
            subsets = listOf("specialization")
        ),
        targetEnd = MetaAssociationEnd(
            name = "subsettingFeature",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("specific")
        )
    )

    // Feature owns its Subsetting relationships (composite)
    val owningFeatureOwnedSubsettingAssociation = MetaAssociation(
        name = "owningFeatureOwnedSubsettingAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningFeature",
            type = "Feature",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("subsettingFeature"),
            redefines = listOf("owningType")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedSubsetting",
            type = "Subsetting",
            lowerBound = 0,
            upperBound = -1,
            aggregation = AggregationKind.COMPOSITE,
            isDerived = true,
            subsets = listOf("subsetting", "ownedSpecialization"),
            derivationConstraint = "deriveFeatureOwnedSubsetting"
        )
    )

    // Redefinition references the redefinedFeature Feature (target/general)
    val redefiningRedefinedFeatureAssociation = MetaAssociation(
        name = "redefiningRedefinedFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "redefining",
            type = "Redefinition",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("supersetting")
        ),
        targetEnd = MetaAssociationEnd(
            name = "redefinedFeature",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("subsettedFeature")
        )
    )

    // Redefinition references the redefiningFeature Feature (source/specific)
    val redefinitionRedefiningFeatureAssociation = MetaAssociation(
        name = "redefinitionRedefiningFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "redefinition",
            type = "Redefinition",
            lowerBound = 0,
            upperBound = -1,
            subsets = listOf("subsetting")
        ),
        targetEnd = MetaAssociationEnd(
            name = "redefiningFeature",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("subsettingFeature")
        )
    )

    // Feature owns its Redefinition relationships (composite)
    val owningFeatureOwnedRedefinitionAssociation = MetaAssociation(
        name = "owningFeatureOwnedRedefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningFeature",
            type = "Feature",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("owningFeature")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedRedefinition",
            type = "Redefinition",
            lowerBound = 0,
            upperBound = -1,
            aggregation = AggregationKind.COMPOSITE,
            isDerived = true,
            subsets = listOf("ownedSubsetting"),
            derivationConstraint = "deriveFeatureOwnedRedefinition"
        )
    )

    // ReferenceSubsetting references the referencedFeature Feature (target)
    val referencingReferencedFeatureAssociation = MetaAssociation(
        name = "referencingReferencedFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "referencing",
            type = "ReferenceSubsetting",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("supersetting")
        ),
        targetEnd = MetaAssociationEnd(
            name = "referencedFeature",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("subsettedFeature")
        )
    )

    // Feature owns its ReferenceSubsetting relationship (composite, 0..1)
    val referencingFeatureOwnedReferenceSubsettingAssociation = MetaAssociation(
        name = "referencingFeatureOwnedReferenceSubsettingAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "referencingFeature",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            redefines = listOf("owningFeature", "subsettingFeature")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedReferenceSubsetting",
            type = "ReferenceSubsetting",
            lowerBound = 0,
            upperBound = 1,
            aggregation = AggregationKind.COMPOSITE,
            isDerived = true,
            subsets = listOf("ownedSubsetting"),
            derivationConstraint = "deriveFeatureOwnedReferenceSubsetting"
        )
    )

    return listOf(
        supersettingSubsettedFeatureAssociation,
        subsettingSubsettingFeatureAssociation,
        owningFeatureOwnedSubsettingAssociation,
        redefiningRedefinedFeatureAssociation,
        redefinitionRedefiningFeatureAssociation,
        owningFeatureOwnedRedefinitionAssociation,
        referencingReferencedFeatureAssociation,
        referencingFeatureOwnedReferenceSubsettingAssociation
    )
}
