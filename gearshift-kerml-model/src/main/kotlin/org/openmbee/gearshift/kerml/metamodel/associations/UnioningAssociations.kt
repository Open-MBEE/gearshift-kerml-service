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
 * Figure 13: Unioning
 * Defines associations for Unioning relationships.
 */
fun createUnioningAssociations(): List<MetaAssociation> {

    // Conjugator references the conjugatedType (source)
    val unionedUnioningUnioningTypeAssociation = MetaAssociation(
        name = "unionedUnioningUnioningTypeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "unionedUnioning",
            type = "Unioning",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("targetRelationship")
        ),
        targetEnd = MetaAssociationEnd(
            name = "unioningType",
            type = "Type",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("target")
        )
    )

    // Type owns its Conjugator relationships (composite)
    val typeUnionedOwnedUnioningAssociation = MetaAssociation(
        name = "typeUnionedOwnedUnioningAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "typeUnioned",
            type = "Type",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("owningRelatedElement"),
            redefines = listOf("source"),
            derivationConstraint = "deriveUnioningTypeUnioned"
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedUnioning",
            type = "Unioning",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            aggregation = AggregationKind.COMPOSITE,
            isOrdered = true,
            subsets = listOf("sourceRelationship", "ownedRelationship"),
            derivationConstraint = "deriveTypeOwnedUnioning"
        )
    )

    val unionedTypeUnioningTypeAssociation = MetaAssociation(
        name = "unionedTypeUnioningTypeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "unionedType",
            type = "Type",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = "computeTypeUnionedType"
        ),
        targetEnd = MetaAssociationEnd(
            name = "unioningType",
            type = "Type",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            derivationConstraint = "deriveTypeUnioningType"
        )
    )

    return listOf(
        unionedUnioningUnioningTypeAssociation,
        typeUnionedOwnedUnioningAssociation,
        unionedTypeUnioningTypeAssociation
    )
}