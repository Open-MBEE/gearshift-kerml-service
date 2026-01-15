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
 * Figure 15: Differencing
 * Defines associations for Differencing relationships.
 */
fun createDifferencingAssociations(): List<MetaAssociation> {

    // Conjugator references the conjugatedType (source)
    val differencedDifferencingDifferencingTypeAssociation = MetaAssociation(
        name = "differencedDifferencingDifferencingTypeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "differencedDifferencing",
            type = "Differencing",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("targetRelationship")
        ),
        targetEnd = MetaAssociationEnd(
            name = "differencingType",
            type = "Type",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("target")
        )
    )

    // Type owns its Conjugator relationships (composite)
    val typeDifferencedOwnedDifferencingAssociation = MetaAssociation(
        name = "typeDifferencedOwnedDifferencingAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "typeDifferenced",
            type = "Type",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("owningRelatedElement"),
            redefines = listOf("source")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedDifferencing",
            type = "Differencing",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            aggregation = AggregationKind.COMPOSITE,
            isOrdered = true,
            subsets = listOf("sourceRelationship", "ownedRelationship")
        )
    )

    val differencedTypeDifferencingTypeAssociation = MetaAssociation(
        name = "differencedTypeDifferencingTypeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "differencedType",
            type = "Type",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
        ),
        targetEnd = MetaAssociationEnd(
            name = "differencingType",
            type = "Type",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
        )
    )

    return listOf(
        differencedDifferencingDifferencingTypeAssociation,
        typeDifferencedOwnedDifferencingAssociation,
        differencedTypeDifferencingTypeAssociation
    )
}