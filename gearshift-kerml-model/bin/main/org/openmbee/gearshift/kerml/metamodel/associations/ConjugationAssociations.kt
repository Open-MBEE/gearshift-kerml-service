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
 * Figure 11: Conjugation
 * Defines associations for Conjugation relationships.
 */
fun createConjugationAssociations(): List<MetaAssociation> {

    // Conjugation has originalType : Type [1..1] {redefines target}
    val conjugationOriginalTypeAssociation = MetaAssociation(
        name = "conjugationOriginalTypeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "conjugation",
            type = "Conjugation",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("targetRelationship")
        ),
        targetEnd = MetaAssociationEnd(
            name = "originalType",
            type = "Type",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("target")
        )
    )

    // Conjugation has conjugatedType : Type [1..1] {redefines source}
    val conjugatorConjugatedTypeAssociation = MetaAssociation(
        name = "conjugatorConjugatedTypeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "conjugator",
            type = "Conjugation",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("sourceRelationship")
        ),
        targetEnd = MetaAssociationEnd(
            name = "conjugatedType",
            type = "Type",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("source")
        )
    )

    // Type has ownedConjugator : Conjugation [0..1] {derived, subsets conjugator, ownedRelationship}
    val owningTypeOwnedConjugatorAssociation = MetaAssociation(
        name = "ownedConjugatorOwningTypeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningType",
            type = "Type",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("conjugatedType", "owningRelatedElement")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedConjugator",
            type = "Conjugation",
            lowerBound = 0,
            upperBound = 1,
            aggregation = AggregationKind.COMPOSITE,
            isDerived = true,
            subsets = listOf("conjugator", "ownedRelationship"),
            derivationConstraint = "deriveTypeOwnedConjugator"
        )
    )

    return listOf(
        conjugationOriginalTypeAssociation,
        conjugatorConjugatedTypeAssociation,
        owningTypeOwnedConjugatorAssociation
    )
}