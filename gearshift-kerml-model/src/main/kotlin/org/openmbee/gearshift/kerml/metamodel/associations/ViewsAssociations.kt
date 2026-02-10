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

    // Rendering has subrendering : Rendering [0..*] {derived}
    val renderingSubrenderingAssociation = MetaAssociation(
        name = "renderingSubrenderingAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningRendering",
            type = "Rendering",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isNavigable = false
        ),
        targetEnd = MetaAssociationEnd(
            name = "subrendering",
            type = "Rendering",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            derivationConstraint = "deriveRenderingSubrendering"
        )
    )

    // View has expose : Expose [0..*] {derived, subsets ownedRelationship}
    val viewExposeAssociation = MetaAssociation(
        name = "viewExposeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningView",
            type = "View",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isNavigable = false
        ),
        targetEnd = MetaAssociationEnd(
            name = "expose",
            type = "Expose",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            subsets = listOf("ownedRelationship"),
            derivationConstraint = "deriveViewExpose"
        )
    )

    // View has exposedElement : Element [0..*] {derived}
    val viewExposedElementAssociation = MetaAssociation(
        name = "viewExposedElementAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "viewForExposedElement",
            type = "View",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false
        ),
        targetEnd = MetaAssociationEnd(
            name = "exposedElement",
            type = "Element",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            derivationConstraint = "deriveViewExposedElement"
        )
    )

    // View has rendering : Rendering [0..1] {derived}
    val viewRenderingAssociation = MetaAssociation(
        name = "viewRenderingAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "renderedView",
            type = "View",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false
        ),
        targetEnd = MetaAssociationEnd(
            name = "rendering",
            type = "Rendering",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveViewRendering"
        )
    )

    // ViewRenderingMembership has ownedRendering : Rendering [1..1] {derived}
    val viewRenderingMembershipOwnedRenderingAssociation = MetaAssociation(
        name = "viewRenderingMembershipOwnedRenderingAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "renderingMembership",
            type = "ViewRenderingMembership",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isNavigable = false
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedRendering",
            type = "Rendering",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveViewRenderingMembershipOwnedRendering"
        )
    )

    // View has satisfiedViewpoint : ViewpointPredicate [0..*] {derived}
    val viewSatisfiedViewpointAssociation = MetaAssociation(
        name = "viewSatisfiedViewpointAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "satisfyingView",
            type = "View",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false
        ),
        targetEnd = MetaAssociationEnd(
            name = "satisfiedViewpoint",
            type = "ViewpointPredicate",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            derivationConstraint = "deriveViewSatisfiedViewpoint"
        )
    )

    // View has subview : View [0..*] {derived}
    val viewSubviewAssociation = MetaAssociation(
        name = "viewSubviewAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningView",
            type = "View",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isNavigable = false
        ),
        targetEnd = MetaAssociationEnd(
            name = "subview",
            type = "View",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            derivationConstraint = "deriveViewSubview"
        )
    )

    // ViewpointPredicate has viewpointDefinition : Viewpoint [0..*] {derived}
    val viewpointPredicateViewpointDefinitionAssociation = MetaAssociation(
        name = "viewpointPredicateViewpointDefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "typedViewpointPredicate",
            type = "ViewpointPredicate",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false
        ),
        targetEnd = MetaAssociationEnd(
            name = "viewpointDefinition",
            type = "Viewpoint",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            derivationConstraint = "deriveViewpointPredicateViewpointDefinition"
        )
    )

    return listOf(
        renderingSubrenderingAssociation,
        viewExposeAssociation,
        viewExposedElementAssociation,
        viewRenderingAssociation,
        viewRenderingMembershipOwnedRenderingAssociation,
        viewSatisfiedViewpointAssociation,
        viewSubviewAssociation,
        viewpointPredicateViewpointDefinitionAssociation,
    )
}
