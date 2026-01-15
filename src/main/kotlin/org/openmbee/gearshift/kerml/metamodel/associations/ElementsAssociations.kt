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
 * Figure 4: Elements
 * Defines associations for the Element metaclass and related relationships.
 */
fun createElementAssociations(): List<MetaAssociation> {
    val ownedElementOwnerAssociation = MetaAssociation(
        name = "ownedElementOwnerAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "ownedElement",
            type = "Element",
            isDerived = true,
            derivationConstraint = "deriveElementOwnedElement",
            isOrdered = true,
            lowerBound = 0,
            upperBound = -1
        ),
        targetEnd = MetaAssociationEnd(
            name = "owner",
            type = "Element",
            isDerived = true,
            derivationConstraint = "deriveElementOwner",
            lowerBound = 0,
            upperBound = 1
        )
    )

    val relationshipRelatedElementAssociation = MetaAssociation(
        name = "relationshipRelatedElementAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "relationship",
            type = "Relationship",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isUnion = true,
            isUnique = false,
            isDerived = true,
            derivationConstraint = "deriveRelationshipRelationship",
        ),
        targetEnd = MetaAssociationEnd(
            name = "relatedElement",
            type = "Element",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isUnique = false,
            isDerived = true,
            derivationConstraint = "deriveElementRelatedElement"
        )
    )

    val targetRelationshipTargetAssociation = MetaAssociation(
        name = "targetRelationshipTargetAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "targetRelationship",
            type = "Relationship",
            isNavigable = false,
            lowerBound = 0,
            upperBound = -1,
            subsets = listOf("relationship")
        ),
        targetEnd = MetaAssociationEnd(
            name = "target",
            type = "Element",
            lowerBound = 0,
            upperBound = -1,
            subsets = listOf("relatedElement")
        )
    )

    val sourceRelationshipSourceAssociation = MetaAssociation(
        name = "sourceRelationshipSourceAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "sourceRelationship",
            type = "Relationship",
            isNavigable = false,
            lowerBound = 0,
            upperBound = -1,
            subsets = listOf("relationship")
        ),
        targetEnd = MetaAssociationEnd(
            name = "source",
            type = "Element",
            lowerBound = 0,
            upperBound = -1,
            subsets = listOf("relatedElement")
        )
    )

    val owningRelationshipOwnedRelatedElementAssociation = MetaAssociation(
        name = "owningRelationshipOwnedRelatedElementAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningRelationship",
            type = "Relationship",
            lowerBound = 0,
            upperBound = 1,
            subsets = listOf("relationship")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedRelatedElement",
            type = "Element",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            aggregation = AggregationKind.COMPOSITE,
            subsets = listOf("relatedElement")
        )
    )

    val owningRelatedElementOwnedRelationship = MetaAssociation(
        name = "owningRelationshipOwnedRelatedElementAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningRelatedElement",
            "Element",
            lowerBound = 0,
            upperBound = 1,
            subsets = listOf("relatedElement")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedRelationship",
            type = "Relationship",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            aggregation = AggregationKind.COMPOSITE,
            subsets = listOf("relationship")
        )
    )

    return listOf(
        ownedElementOwnerAssociation,
        relationshipRelatedElementAssociation,
        targetRelationshipTargetAssociation,
        sourceRelationshipSourceAssociation,
        owningRelationshipOwnedRelatedElementAssociation,
        owningRelatedElementOwnedRelationship
    )
}
