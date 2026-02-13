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
 * Views associations.
 * Defines associations for View, Rendering, Viewpoint, ViewpointPredicate, and ViewRenderingMembership.
 */
fun createViewsAssociations(): List<MetaAssociation> {

    val viewOwningClassOwnedViewAssociation = MetaAssociation(
        name = "viewOwningClassOwnedViewAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "viewOwningClass",
            type = "Class",
            isDerived = true,
            isNavigable = false,
            lowerBound = 0,
            upperBound = 1,
            isOrdered = true,
            subsets = listOf("typeWithFeature", "owningType"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedView",
            type = "SubView",
            isDerived = true,
            lowerBound = 0,
            upperBound = 1,
            subsets = listOf("ownedFeature", "feature"),
        )
    )

    val typedSubViewViewAssociation = MetaAssociation(
        name = "typingViewViewAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "typedSubView",
            type = "SubView",
            subsets = listOf("typedFeature"),
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            derivationConstraint = "computeSubViewTypedView"
        ),
        targetEnd = MetaAssociationEnd(
            name = "view",
            type = "View",
            isDerived = true,
            isOrdered = true,
            lowerBound = 0,
            upperBound = 1,
            redefines = listOf("type"),
            derivationConstraint = "computeViewView"
        )
    )

    val featuringViewSubViewAssociation = MetaAssociation(
        name = "featuringViewViewAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "featuringView",
            type = "View",
            isDerived = true,
            isNavigable = false,
            lowerBound = 0,
            upperBound = -1,
            subsets = listOf("typeWithFeature"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "subview",
            type = "SubView",
            isDerived = true,
            isOrdered = true,
            lowerBound = 0,
            upperBound = -1,
            subsets = listOf("feature"),
        )
    )

    val owningViewViewConditionAssociation = MetaAssociation(
        name = "owningViewViewConditionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningView",
            type = "View",
            isDerived = true,
            isNavigable = false,
            upperBound = 0,
            lowerBound = 1,
            subsets = listOf("owningType"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "viewCondition",
            type = "Expression",
            isDerived = true,
            isOrdered = true,
            lowerBound = 0,
            upperBound = -1,
            subsets = listOf("ownedMember"),
        )
    )

    val owningSubViewViewConditionAssociation = MetaAssociation(
        name = "owningSubViewViewConditionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningSubView",
            type = "SubView",
            isDerived = true,
            isNavigable = false,
            upperBound = 0,
            lowerBound = 1,
            subsets = listOf("owningType"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "viewCondition",
            type = "Expression",
            isDerived = true,
            isOrdered = true,
            lowerBound = 0,
            upperBound = -1,
            subsets = listOf("ownedMember"),
        )
    )

    val viewpointSatisfyingViewSatisfiedViewpointAssociation = MetaAssociation(
        name = "viewpointSatisfyingViewSatisfiedViewpointAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "viewpointSatisfyingView",
            type = "View",
            isDerived = true,
            isNavigable = false,
            upperBound = 0,
            lowerBound = 1,
            subsets = listOf("typeWithFeature", "owningType"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "satisfiedViewpoint",
            type = "ViewpointPredicate",
            isDerived = true,
            isOrdered = true,
            lowerBound = 0,
            upperBound = -1,
            subsets = listOf("ownedFeature", "feature"),
        )
    )

    val viewpointSatisfyingSubViewSatisfiedViewpointAssociation = MetaAssociation(
        name = "viewpointSatisfyingSubViewSatisfiedViewpointAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "viewpointSatisfyingSubView",
            type = "SubView",
            isDerived = true,
            isNavigable = false,
            upperBound = 0,
            lowerBound = 1,
            subsets = listOf("typeWithFeature"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "satisfiedViewpoint",
            type = "ViewpointPredicate",
            isDerived = true,
            isOrdered = true,
            lowerBound = 0,
            upperBound = -1,
            subsets = listOf("ownedFeature", "feature"),
        )
    )

    val renderingOwningViewViewRenderingAssociation = MetaAssociation(
        name = "renderingOwningViewViewRenderingAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "renderingOwningView",
            type = "View",
            isDerived = true,
            isNavigable = false,
            upperBound = 0,
            lowerBound = 1,
            subsets = listOf("typeWithFeature", "owningType"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "viewRendering",
            type = "RenderingFeature",
            isDerived = true,
            derivationConstraint = "deriveViewRendering",
            lowerBound = 0,
            upperBound = 1,
        )
    )

    val renderingOwningSubViewViewRenderingAssociation = MetaAssociation(
        name = "renderingOwningSubViewViewRenderingAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "renderingOwningSubView",
            type = "SubView",
            isDerived = true,
            isNavigable = false,
            upperBound = 0,
            lowerBound = 1,
            subsets = listOf("typeWithFeature"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "viewRendering",
            type = "RenderingFeature",
            isDerived = true,
            lowerBound = 0,
            upperBound = 1,
        )
    )

    return listOf(
        viewOwningClassOwnedViewAssociation,
        typedSubViewViewAssociation,
        featuringViewSubViewAssociation,
        owningViewViewConditionAssociation,
        owningSubViewViewConditionAssociation,
        viewpointSatisfyingViewSatisfiedViewpointAssociation,
        viewpointSatisfyingSubViewSatisfiedViewpointAssociation,
        renderingOwningViewViewRenderingAssociation,
        renderingOwningSubViewViewRenderingAssociation
    )
}
