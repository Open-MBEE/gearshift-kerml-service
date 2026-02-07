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
 * Figure 27: Connectors
 * Defines associations for Connectors.
 */
fun createConnectorAssociations(): List<MetaAssociation> {

    // Connector has connectorEnd : Feature [0..*] {ordered, derived, redefines endFeature}
    val featuringConnectorConnectorEndAssociation = MetaAssociation(
        name = "featuringConnectorConnectorEndAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "featuringConnector",
            type = "Connector",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("typeWithEndFeature"),
            derivationConstraint = "deriveFeatureFeaturingConnector"
        ),
        targetEnd = MetaAssociationEnd(
            name = "connectorEnd",
            type = "Feature",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            redefines = listOf("endFeature"),
            derivationConstraint = "deriveConnectorConnectorEnd"
        )
    )

    // Connector has relatedFeature : Feature [0..*] {ordered, nonunique, derived, redefines relatedElement}
    val connectorRelatedFeatureAssociation = MetaAssociation(
        name = "connectorRelatedFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "connector",
            type = "Connector",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("relationship"),
            derivationConstraint = "deriveFeatureConnector"
        ),
        targetEnd = MetaAssociationEnd(
            name = "relatedFeature",
            type = "Feature",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            isUnique = false,
            redefines = listOf("relatedElement"),
            derivationConstraint = "deriveConnectorRelatedFeature"
        )
    )

    // Connector has sourceFeature : Feature [0..1] {ordered, derived, subsets relatedFeature, redefines source}
    val sourceConnectorSourceFeatureAssociation = MetaAssociation(
        name = "sourceConnectorSourceFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "sourceConnector",
            type = "Connector",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("connector", "sourceRelationship"),
            derivationConstraint = "deriveFeatureSourceConnector"
        ),
        targetEnd = MetaAssociationEnd(
            name = "sourceFeature",
            type = "Feature",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("relatedFeature"),
            redefines = listOf("source"),
            derivationConstraint = "deriveConnectorSourceFeature"
        )
    )

    // Connector has targetFeature : Feature [0..*] {ordered, derived, subsets relatedFeature, redefines target}
    val targetConnectorTargetFeatureAssociation = MetaAssociation(
        name = "targetConnectorTargetFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "targetConnector",
            type = "Connector",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("connector"),
            derivationConstraint = "deriveFeatureTargetConnector"
        ),
        targetEnd = MetaAssociationEnd(
            name = "targetFeature",
            type = "Feature",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("relatedFeature"),
            redefines = listOf("target"),
            derivationConstraint = "deriveConnectorTargetFeature"
        )
    )

    // Connector has association : Association [0..*] {ordered, derived, redefines type}
    val typedConnectorAssociationAssociation = MetaAssociation(
        name = "typedConnectorAssociationAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "typedConnector",
            type = "Connector",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("typedFeature"),
            derivationConstraint = "deriveAssociationTypedConnector"
        ),
        targetEnd = MetaAssociationEnd(
            name = "association",
            type = "Association",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            redefines = listOf("type"),
            derivationConstraint = "deriveConnectorAssociation"
        )
    )

    // Connector has defaultFeaturingType : Type [0..1] {derived}
    val featuredConnectorDefaultFeaturingTypeAssociation = MetaAssociation(
        name = "featuredConnectorDefaultFeaturingTypeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "featuredConnector",
            type = "Connector",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            derivationConstraint = "deriveTypeFeaturedConnector"
        ),
        targetEnd = MetaAssociationEnd(
            name = "defaultFeaturingType",
            type = "Type",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveConnectorDefaultFeaturingType"
        )
    )

    return listOf(
        connectorRelatedFeatureAssociation,
        featuredConnectorDefaultFeaturingTypeAssociation,
        featuringConnectorConnectorEndAssociation,
        sourceConnectorSourceFeatureAssociation,
        targetConnectorTargetFeatureAssociation,
        typedConnectorAssociationAssociation,
    )
}
