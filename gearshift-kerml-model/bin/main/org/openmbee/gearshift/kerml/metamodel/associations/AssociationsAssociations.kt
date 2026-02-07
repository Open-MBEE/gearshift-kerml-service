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

import org.openmbee.mdm.framework.meta.MetaAssociation
import org.openmbee.mdm.framework.meta.MetaAssociationEnd

/**
 * Figure 26: Associations
 * Defines associations for Association and Connector metaclasses.
 */
fun createAssociationAssociations(): List<MetaAssociation> {

    // Association has relatedType : Type [0..*] {ordered, nonunique, derived, redefines relatedElement}
    val associationRelatedTypeAssociation = MetaAssociation(
        name = "associationRelatedTypeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "association",
            type = "Association",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("relationship"),
            derivationConstraint = "deriveTypeAssociation"
        ),
        targetEnd = MetaAssociationEnd(
            name = "relatedType",
            type = "Type",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            isUnique = false,
            redefines = listOf("relatedElement"),
            derivationConstraint = "deriveAssociationRelatedType"
        )
    )

    // Association has sourceType : Type [0..1] {derived, subsets relatedType, redefines source}
    val sourceAssociationSourceTypeAssociation = MetaAssociation(
        name = "sourceAssociationSourceTypeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "sourceAssociation",
            type = "Association",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("association", "sourceRelationship"),
            derivationConstraint = "deriveTypeSourceAssociation"
        ),
        targetEnd = MetaAssociationEnd(
            name = "sourceType",
            type = "Type",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveAssociationSourceType",
            subsets = listOf("relatedType"),
            redefines = listOf("source"),
        )
    )

    // Association has targetType : Type [0..*] {derived, subsets relatedType, redefines target}
    val targetAssociationTargetTypeAssociation = MetaAssociation(
        name = "targetAssociationTargetTypeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "targetAssociation",
            type = "Association",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("relationship"),
            derivationConstraint = "deriveTypeTargetAssociation"
        ),
        targetEnd = MetaAssociationEnd(
            name = "targetType",
            type = "Type",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            subsets = listOf("relatedType"),
            redefines = listOf("target"),
            derivationConstraint = "deriveAssociationTargetType"
        )
    )

    // Association has associationEnd : Feature [0..*] {derived, redefines endFeature}
    val associationWithEndAssociationEnd = MetaAssociation(
        name = "associationWithEndAssociationEnd",
        sourceEnd = MetaAssociationEnd(
            name = "associationWithEnd",
            type = "Association",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("typeWithEndFeature"),
            derivationConstraint = "deriveFeatureAssociationWithEnd"
        ),
        targetEnd = MetaAssociationEnd(
            name = "associationEnd",
            type = "Feature",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            redefines = listOf("endFeature"),
            derivationConstraint = "deriveAssociationAssociationEnd"
        )
    )

    return listOf(
        associationRelatedTypeAssociation,
        sourceAssociationSourceTypeAssociation,
        targetAssociationTargetTypeAssociation,
        associationWithEndAssociationEnd
    )
}
