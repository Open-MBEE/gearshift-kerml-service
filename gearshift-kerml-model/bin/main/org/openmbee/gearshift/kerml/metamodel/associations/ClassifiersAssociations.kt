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
 * Figure 16: Classifiers
 * Defines associations for Classifier metaclass and Subclassification.
 */
fun createClassifierAssociations(): List<MetaAssociation> {

    // Subclassification has superclassifier : Classifier [1..1] {redefines general}
    val superclassificationSuperclassifierAssociation = MetaAssociation(
        name = "superclassificationSuperclassifierAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "superclassification",
            type = "Subclassification",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("generalization")
        ),
        targetEnd = MetaAssociationEnd(
            name = "superclassifier",
            type = "Classifier",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("general")
        )
    )

    // Subclassification has subclassifier : Classifier [1..1] {redefines specific}
    val subclassificationSubclassifierAssociation = MetaAssociation(
        name = "subclassificationSubclassifierAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "subclassification",
            type = "Subclassification",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("specialization")
        ),
        targetEnd = MetaAssociationEnd(
            name = "subclassifier",
            type = "Classifier",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("specific")
        )
    )

    // Classifier has ownedSubclassification : Subclassification [0..*] {derived, subsets ownedSpecialization}
    val owningClassifierOwnedSubclassificationAssociation = MetaAssociation(
        name = "owningClassifierOwnedSubclassificationAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningClassifier",
            type = "Classifier",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            redefines = listOf("owningType")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedSubclassification",
            type = "Subclassification",
            lowerBound = 0,
            upperBound = -1,
            aggregation = AggregationKind.COMPOSITE,
            isDerived = true,
            subsets = listOf("ownedSpecialization"),
            derivationConstraint = "deriveClassifierOwnedSubclassification"
        )
    )

    return listOf(
        superclassificationSuperclassifierAssociation,
        subclassificationSubclassifierAssociation,
        owningClassifierOwnedSubclassificationAssociation
    )
}
