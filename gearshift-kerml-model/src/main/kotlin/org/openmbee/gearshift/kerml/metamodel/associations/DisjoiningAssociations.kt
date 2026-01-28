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
 * Figure 12: Disjoining
 * Defines associations for Disjoining relationships.
 */
fun createDisjoiningAssociations(): List<MetaAssociation> {

    // Disjoining has typeDisjoined : Type [1..1] {redefines source}
    val disjoiningTypeDisjoiningTypeDisjoinedAssociation = MetaAssociation(
        name = "disjoiningTypeDisjoiningTypeDisjoinedAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "disjoiningTypeDisjoining",
            type = "Disjoining",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("sourceRelationship")
        ),
        targetEnd = MetaAssociationEnd(
            name = "typeDisjoined",
            type = "Type",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("source")
        )
    )

    // Disjoining has disjoiningType : Type [1..1] {redefines target}
    val disjoinedTypeDisjoiningDisjoiningTypeAssociation = MetaAssociation(
        name = "disjoinedTypeDisjoiningDisjoiningTypeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "disjoinedTypeDisjoining",
            type = "Disjoining",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("targetRelationship")
        ),
        targetEnd = MetaAssociationEnd(
            name = "disjoiningType",
            type = "Type",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("target")
        )
    )

    // Type has ownedDisjoining : Disjoining [0..*] {derived, subsets disjoiningTypeDisjoining, ownedRelationship}
    val owningTypeOwnedDisjoiningAssociation = MetaAssociation(
        name = "owningTypeOwnedDisjoiningAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningType",
            type = "Type",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("typeDisjoined", "owningRelatedElement")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedDisjoining",
            type = "Disjoining",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            aggregation = AggregationKind.COMPOSITE,
            subsets = listOf("disjoiningTypeDisjoining", "ownedRelationship"),
            derivationConstraint = "deriveTypeOwnedDisjoining"
        )
    )

    return listOf(
        disjoiningTypeDisjoiningTypeDisjoinedAssociation,
        disjoinedTypeDisjoiningDisjoiningTypeAssociation,
        owningTypeOwnedDisjoiningAssociation
    )
}