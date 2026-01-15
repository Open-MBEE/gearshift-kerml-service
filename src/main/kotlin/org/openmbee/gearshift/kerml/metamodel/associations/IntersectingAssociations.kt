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
 * Figure 14: Intersecting
 * Defines associations for Intersecting relationships.
 */
fun createIntersectingAssociations(): List<MetaAssociation> {

    // Conjugator references the conjugatedType (source)
    val intersectedIntersectingIntersectingTypeAssociation = MetaAssociation(
        name = "intersectedIntersectingIntersectingTypeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "intersectedIntersecting",
            type = "Intersecting",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("targetRelationship")
        ),
        targetEnd = MetaAssociationEnd(
            name = "intersectingType",
            type = "Type",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("target")
        )
    )

    // Type owns its Conjugator relationships (composite)
    val typeIntersectedOwnedIntersectingAssociation = MetaAssociation(
        name = "typeIntersectedOwnedIntersectingAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "typeIntersected",
            type = "Type",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("owningRelatedElement"),
            redefines = listOf("source")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedIntersecting",
            type = "Intersecting",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            aggregation = AggregationKind.COMPOSITE,
            isOrdered = true,
            subsets = listOf("sourceRelationship", "ownedRelationship")
        )
    )

    val intersectedTypeIntersectingTypeAssociation = MetaAssociation(
        name = "intersectedTypeIntersectingTypeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "intersectedType",
            type = "Type",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
        ),
        targetEnd = MetaAssociationEnd(
            name = "intersectingType",
            type = "Type",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
        )
    )

    return listOf(
        intersectedIntersectingIntersectingTypeAssociation,
        typeIntersectedOwnedIntersectingAssociation,
        intersectedTypeIntersectingTypeAssociation
    )
}