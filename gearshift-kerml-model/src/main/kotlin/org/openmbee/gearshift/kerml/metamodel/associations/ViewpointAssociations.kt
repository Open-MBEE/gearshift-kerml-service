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

fun createViewpointAssociations(): List<MetaAssociation> {

    val viewpointOwningStructureOwnedViewpointAssociation = MetaAssociation(
        name = "viewpointOwningClassOwnedViewpointAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "viewpointOwningClass",
            type = "Class",
            lowerBound = 0,
            upperBound = 1,
            isDerived = false,
            isNavigable = false,
            subsets = listOf("typeWithFeature", "owningType"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedViewpoint",
            type = "ViewpointPredicate",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("ownedFeature", "feature")
        )
    )

    val viewpointPredicateViewpointAssociation = MetaAssociation(
        name = "viewpointPredicateViewpointAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "viewpointPredicate",
            type = "ViewpointPredicate",
            subsets = listOf("typedBooleanExpression"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "viewpoint",
            type = "Viewpoint",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            redefines = listOf("predicate")

        )
    )

    val viewpointForStakeholderViewpointStakeholderAssociation = MetaAssociation(
        name = "viewpointForStakeholderViewpointStakeholderAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "viewpointForStakeholder",
            type = "Viewpoint",
            isDerived = true,
            isNavigable = false,
            lowerBound = 0,
            upperBound = 1,
        ),
        targetEnd = MetaAssociationEnd(
            name = "viewpointStakeholder",
            type = "Feature",
            isDerived = true,
            isOrdered = true,
            lowerBound = 0,
            upperBound = -1,

            )
    )

    val viewpointPredicateForStakeholderViewpointStakeholderAssociation = MetaAssociation(
        name = "viewpointPredicateForStakeholderViewpointStakeholderAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "viewpointPredicateForStakeholder",
            type = "ViewpointPredicate",
            isDerived = true,
            isNavigable = false,
            lowerBound = 0,
            upperBound = 1,
        ),
        targetEnd = MetaAssociationEnd(
            name = "viewpointStakeholder",
            type = "Feature",
            isDerived = true,
            isOrdered = true,
            lowerBound = 0,
            upperBound = -1,

            )
    )


    return listOf(
        viewpointOwningStructureOwnedViewpointAssociation,
        viewpointPredicateViewpointAssociation,
        viewpointForStakeholderViewpointStakeholderAssociation,
        viewpointPredicateForStakeholderViewpointStakeholderAssociation
    )
}