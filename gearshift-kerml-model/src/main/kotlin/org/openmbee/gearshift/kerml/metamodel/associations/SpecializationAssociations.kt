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
 * Figure 10: Specialization
 * Defines associations for Specialization relationships.
 */
fun createSpecializationAssociations(): List<MetaAssociation> {

    // Specialization references the general Type (target)
    val generalizationGeneralAssociation = MetaAssociation(
        name = "generalizationGeneralAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "generalization",
            type = "Specialization",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            redefines = listOf("targetRelationship")
        ),
        targetEnd = MetaAssociationEnd(
            name = "general",
            type = "Type",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("target")
        )
    )

    // Specialization references the specific Type (source)
    val specializationSpecificAssociation = MetaAssociation(
        name = "specializationSpecificAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "specialization",
            type = "Specialization",
            lowerBound = 0,
            upperBound = -1,
            subsets = listOf("sourceRelationship")
        ),
        targetEnd = MetaAssociationEnd(
            name = "specific",
            type = "Type",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("source")
        )
    )

    // Type owns its Specialization relationships (composite)
    val owningTypeOwnedSpecializationAssociation = MetaAssociation(
        name = "owningTypeOwnedSpecializationAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningType",
            type = "Type",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("specific", "owningRelatedElement")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedSpecialization",
            type = "Specialization",
            lowerBound = 0,
            upperBound = -1,
            aggregation = AggregationKind.COMPOSITE,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("specialization", "ownedRelationship"),
            derivationConstraint = "deriveTypeOwnedSpecialization"
        )
    )

    return listOf(
        generalizationGeneralAssociation,
        specializationSpecificAssociation,
        owningTypeOwnedSpecializationAssociation
    )
}
