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

fun createViewRenderingMembershipAssociations(): List<MetaAssociation> {

    val viewRenderingMembershipOwnedRenderingAssociation = MetaAssociation(
        name = "viewRenderingMembershipOwnedRenderingAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "viewRenderingMembership",
            type = "ViewRenderingMembership",
            isDerived = true,
            isNavigable = false,
            lowerBound = 0,
            upperBound = 1,
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedRendering",
            type = "RenderingFeature",
            isDerived = true,
            derivationConstraint = "deriveViewRenderingMembershipOwnedRendering",
            lowerBound = 1,
            upperBound = 1,
            aggregation = AggregationKind.COMPOSITE
        )
    )

    val referencingRenderingMembershipReferencedRenderingAssociation = MetaAssociation(
        name = "referencingRenderingMembershipReferencedRenderingAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "referencingRenderingMembership",
            type = "ViewRenderingMembership",
            isDerived = true,
            isNavigable = false,
            lowerBound = 0,
            upperBound = -1
        ),
        targetEnd = MetaAssociationEnd(
            name = "referencedRendering",
            type = "RenderingFeature",
            isDerived = true,
            lowerBound = 1,
            upperBound = 1,
        )
    )

    return listOf(
        viewRenderingMembershipOwnedRenderingAssociation,
        referencingRenderingMembershipReferencedRenderingAssociation
    )
}