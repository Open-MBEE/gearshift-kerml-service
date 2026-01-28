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
 * Figure 17: Features
 * Defines associations for Feature metaclass, TypeFeaturing, and FeatureTyping.
 */
fun createFeaturesAssociations(): List<MetaAssociation> {

    // TypeFeaturing references the featuringType Type (target)
    val typeFeaturingOfTypeFeaturingTypeAssociation = MetaAssociation(
        name = "typeFeaturingOfTypeFeaturingTypeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "typeFeaturingOfType",
            type = "TypeFeaturing",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("targetRelationship")
        ),
        targetEnd = MetaAssociationEnd(
            name = "featuringType",
            type = "Type",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("target")
        )
    )

    // TypeFeaturing references the featureOfType Feature (source)
    val typeFeaturingFeatureOfTypeAssociation = MetaAssociation(
        name = "typeFeaturingFeatureOfTypeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "typeFeaturing",
            type = "TypeFeaturing",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("sourceRelationship")
        ),
        targetEnd = MetaAssociationEnd(
            name = "featureOfType",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("source")
        )
    )

    // Feature owns its TypeFeaturing relationships (composite)
    val owningFeatureOfTypeOwnedTypeFeaturingAssociation = MetaAssociation(
        name = "owningFeatureOfTypeOwnedTypeFeaturingAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningFeatureOfType",
            type = "Feature",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("featureOfType", "owningRelatedElement")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedTypeFeaturing",
            type = "TypeFeaturing",
            lowerBound = 0,
            upperBound = -1,
            aggregation = AggregationKind.COMPOSITE,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("typeFeaturing", "ownedRelationship"),
            derivationConstraint = "deriveFeatureOwnedTypeFeaturing"
        )
    )

    // FeatureTyping references the type Type (target)
    val typingByTypeTypeAssociation = MetaAssociation(
        name = "typingByTypeTypeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "typingByType",
            type = "FeatureTyping",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("generalization")
        ),
        targetEnd = MetaAssociationEnd(
            name = "type",
            type = "Type",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("general")
        )
    )

    // FeatureTyping references the typedFeature Feature (source)
    val typingTypedFeatureAssociation = MetaAssociation(
        name = "typingTypedFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "typing",
            type = "FeatureTyping",
            lowerBound = 0,
            upperBound = -1,
            subsets = listOf("specialization")
        ),
        targetEnd = MetaAssociationEnd(
            name = "typedFeature",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("specific")
        )
    )

    // Feature owns its FeatureTyping relationships (composite)
    val owningFeatureOwnedTypingAssociation = MetaAssociation(
        name = "owningFeatureOwnedTypingAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningFeature",
            type = "Feature",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("typedFeature"),
            redefines = listOf("owningType")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedTyping",
            type = "FeatureTyping",
            lowerBound = 0,
            upperBound = -1,
            aggregation = AggregationKind.COMPOSITE,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("typing", "ownedSpecialization"),
            derivationConstraint = "deriveFeatureOwnedTyping"
        )
    )

    // Feature has featuringTypes (derived, through TypeFeaturing)
    val featureOfTypeFeaturingTypeAssociation = MetaAssociation(
        name = "featureOfTypeFeaturingTypeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "featureOfType",
            type = "Feature",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false
        ),
        targetEnd = MetaAssociationEnd(
            name = "featuringType",
            type = "Type",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            derivationConstraint = "deriveFeatureFeaturingType"
        )
    )

    // Feature has types (derived, through FeatureTyping)
    val typedFeatureTypeAssociation = MetaAssociation(
        name = "typedFeatureTypeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "typedFeature",
            type = "Feature",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false
        ),
        targetEnd = MetaAssociationEnd(
            name = "type",
            type = "Type",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            derivationConstraint = "deriveFeatureType"
        )
    )

    return listOf(
        typeFeaturingOfTypeFeaturingTypeAssociation,
        typeFeaturingFeatureOfTypeAssociation,
        owningFeatureOfTypeOwnedTypeFeaturingAssociation,
        typingByTypeTypeAssociation,
        typingTypedFeatureAssociation,
        owningFeatureOwnedTypingAssociation,
        featureOfTypeFeaturingTypeAssociation,
        typedFeatureTypeAssociation
    )
}
