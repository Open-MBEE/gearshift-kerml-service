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
 * Figure 36: Flows
 * Defines associations for Flows.
 */
fun createFlowAssociations(): List<MetaAssociation> {

    // Flow has FlowEnd : FlowEnd [0..2] {ordered, derived, redefines connectorEnd}
    val featuringFlowFlowEndAssociation = MetaAssociation(
        name = "featuringFlowFlowEndAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "featuringFlow",
            type = "Flow",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("featuringConnector"),
            derivationConstraint = "computeFlowEndFeaturingFlow"
        ),
        targetEnd = MetaAssociationEnd(
            name = "flowEnd",
            type = "FlowEnd",
            lowerBound = 0,
            upperBound = 2,
            isDerived = true,
            isOrdered = true,
            redefines = listOf("connectorEnd"),
            derivationConstraint = "deriveFlowFlowEnd",
        )
    )

    // Flow has sourceOutputFeature : Feature [0..1] {derived}
    val flowFromOutputSourceOutputFeatureAssociation = MetaAssociation(
        name = "flowFromOutputSourceOutputFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "flowFromOutput",
            type = "Flow",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            derivationConstraint = "computeFeatureFlowFromOutput"
        ),
        targetEnd = MetaAssociationEnd(
            name = "sourceOutputFeature",
            type = "Feature",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isOrdered = true,
            isUnique = false,
            derivationConstraint = "deriveFlowSourceOutputFeature",
        )
    )

    // Flow has itemType : Classifier [0..*] {derived}
    val flowForPayloadTypePayloadTypeAssociation = MetaAssociation(
        name = "flowForPayloadTypePayloadTypeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "flowForPayloadType",
            type = "Flow",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            derivationConstraint = "computeClassifierFlowForPayloadType"
        ),
        targetEnd = MetaAssociationEnd(
            name = "payloadType",
            type = "Classifier",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            isUnique = false,
            derivationConstraint = "deriveFlowPayloadType",
        )
    )

    // Flow has targetInputFeature : Feature [0..1] {derived}
    val flowToInputTargetInputFeatureAssociation = MetaAssociation(
        name = "flowToInputTargetInputFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "flowToInput",
            type = "Flow",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            derivationConstraint = "computeFeatureFlowToInput"
        ),
        targetEnd = MetaAssociationEnd(
            name = "targetInputFeature",
            type = "Feature",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isOrdered = true,
            isUnique = false,
            derivationConstraint = "deriveFlowTargetInputFeature",
        )
    )

    // Flow has payloadFeature : PayloadFeature [0..1] {derived, subsets ownedFeature}
    val flowWithPayloadFeaturePayloadFeatureAssociation = MetaAssociation(
        name = "flowWithPayloadFeaturePayloadFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "flowWithPayloadFeature",
            type = "Flow",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("owningType"),
            derivationConstraint = "computePayloadFeatureFlowWithPayloadFeature"
        ),
        targetEnd = MetaAssociationEnd(
            name = "payloadFeature",
            type = "PayloadFeature",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("ownedFeature"),
            derivationConstraint = "deriveFlowPayloadFeature",
        )
    )

    // Flow has interaction : Interaction [0..1] {derived, subsets type}
    val typedFlowInteractionAssociation = MetaAssociation(
        name = "typedFlowInteractionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "typedFlow",
            type = "Flow",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("typedConnector", "typedStep"),
            derivationConstraint = "computeFlowTypedFlow"
        ),
        targetEnd = MetaAssociationEnd(
            name = "interaction",
            type = "Interaction",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            redefines = listOf("association", "behavior"),
            derivationConstraint = "deriveFlowInteraction"
        )
    )

    return listOf(
        featuringFlowFlowEndAssociation,
        flowForPayloadTypePayloadTypeAssociation,
        flowFromOutputSourceOutputFeatureAssociation,
        flowToInputTargetInputFeatureAssociation,
        flowWithPayloadFeaturePayloadFeatureAssociation,
        typedFlowInteractionAssociation,
    )
}
