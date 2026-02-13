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

fun createRenderingAssociations(): List<MetaAssociation> {

    val renderingOwningClassOwnedRenderingAssociation = MetaAssociation(
        name = "renderingOwningClassOwnedRenderingAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "renderingOwningClass",
            type = "Class",
            isDerived = true,
            isNavigable = false,
            lowerBound = 0,
            upperBound = 1,
            subsets = listOf("typeWithFeature", "owningType"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedRendering",
            type = "RenderingFeature",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("ownedFeature", "feature")

        )
    )

    val renderingFeatureRenderingAssociation = MetaAssociation(
        name = "renderingFeatureRenderingAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "renderingFeature",
            type = "RenderingFeature",
            isDerived = true,
            isNavigable = false,
            lowerBound = 0,
            upperBound = -1,
            subsets = listOf("typedFeature"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "rendering",
            type = "Rendering",
            isDerived = true,
            lowerBound = 0,
            upperBound = 1,
            redefines = listOf("type"),

            )
    )

    val featuringRenderingRenderingFeatureAssociation = MetaAssociation(
        name = "featuringRenderingRenderingFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "featuringRendering",
            type = "Rendering",
            isDerived = true,
            isNavigable = false,
            lowerBound = 0,
            upperBound = -1,
            subsets = listOf("typeWithFeature"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "renderingFeature",
            type = "RenderingFeature",
            isDerived = true,
            isOrdered = true,
            lowerBound = 0,
            upperBound = 1,
            subsets = listOf("feature"),
        )
    )

    return listOf(
        renderingOwningClassOwnedRenderingAssociation,
        renderingFeatureRenderingAssociation,
        featuringRenderingRenderingFeatureAssociation
    )
}