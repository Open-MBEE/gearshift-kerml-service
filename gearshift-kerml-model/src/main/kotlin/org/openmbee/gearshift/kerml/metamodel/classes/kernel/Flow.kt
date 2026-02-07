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
package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.mdm.framework.meta.BindingCondition
import org.openmbee.mdm.framework.meta.BindingKind
import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * KerML Flow metaclass.
 * Specializes: Step, Connector
 * A Flow is a Step that represents the transfer of values from one Feature to another.
 * Flows can take non-zero time to complete.
 */
fun createFlowMetaClass() = MetaClass(
    name = "Flow",
    isAbstract = false,
    superclasses = listOf("Step", "Connector"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "deriveFlowFlowEnd",
            type = ConstraintType.DERIVATION,
            expression = "connectorEnd->selectByKind(FlowEnd)",
            description = "The flowEnds of a Flow are all its connectorEnds that are FlowEnds."
        ),
        MetaConstraint(
            name = "deriveFlowPayloadFeature",
            type = ConstraintType.DERIVATION,
            expression = "let payloadFeatures : Sequence(PayloadFeature) = ownedFeature->selectByKind(PayloadFeature) in if payloadFeatures->isEmpty() then null else payloadFeatures->first() endif",
            description = "The payloadFeature of a Flow is the single one of its ownedFeatures that is a PayloadFeature."
        ),
        MetaConstraint(
            name = "deriveFlowPayloadType",
            type = ConstraintType.DERIVATION,
            expression = "if payloadFeature = null then Sequence{} else payloadFeature.type endif",
            description = "The payloadTypes of a Flow are the types of the payloadFeature of the Flow (if any)."
        ),
        MetaConstraint(
            name = "deriveFlowSourceOutputFeature",
            type = ConstraintType.DERIVATION,
            expression = "if connectorEnd->isEmpty() or connectorEnd.ownedFeature->isEmpty() then null else connectorEnd.ownedFeature->first() endif",
            description = "The sourceOutputFeature of a Flow is the first ownedFeature of the first connectorEnd of the Flow."
        ),
        MetaConstraint(
            name = "deriveFlowTargetInputFeature",
            type = ConstraintType.DERIVATION,
            expression = "if connectorEnd->size() < 2 or connectorEnd->at(2).ownedFeature->isEmpty() then null else connectorEnd->at(2).ownedFeature->first() endif",
            description = "The targetInputFeature of a Flow is the first ownedFeature of the second connectorEnd of the Flow."
        ),
        MetaConstraint(
            name = "validateFlowPayloadFeature",
            type = ConstraintType.VERIFICATION,
            expression = "ownedFeature->selectByKind(PayloadFeature)->size() <= 1",
            description = "A Flow must have at most one ownedFeature that is a PayloadFeature."
        ),
        MetaConstraint(
            name = "computeFlowEndFeaturingFlow",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "Flow.allInstances()->select(f | f.flowEnd->includes(self))",
            isNormative = false,
            description = "The Flows that have this FlowEnd as an end."
        ),
        MetaConstraint(
            name = "computeFeatureFlowFromOutput",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "Flow.allInstances()->select(f | f.sourceOutputFeature = self)",
            isNormative = false,
            description = "The Flows that have this Feature as their sourceOutputFeature."
        ),
        MetaConstraint(
            name = "computeClassifierFlowForPayloadType",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "Flow.allInstances()->select(f | f.payloadType->includes(self))",
            isNormative = false,
            description = "The Flows that have this Classifier as a payloadType."
        ),
        MetaConstraint(
            name = "computeFeatureFlowToInput",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "Flow.allInstances()->select(f | f.targetInputFeature = self)",
            isNormative = false,
            description = "The Flows that have this Feature as their targetInputFeature."
        ),
        MetaConstraint(
            name = "computePayloadFeatureFlowWithPayloadFeature",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "Flow.allInstances()->select(f | f.payloadFeature = self)->any(true)",
            isNormative = false,
            description = "The Flow that has this PayloadFeature as its payloadFeature."
        ),
        MetaConstraint(
            name = "computeFlowTypedFlow",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "Flow.allInstances()->select(f | f.interaction->includes(self))",
            isNormative = false,
            description = "The Flows that have this Interaction as a type."
        ),
        MetaConstraint(
            name = "deriveFlowInteraction",
            type = ConstraintType.DERIVATION,
            expression = "type->selectByKind(Interaction)",
            isNormative = false,
            description = "The Interactions that type this Flow."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "flowTransfersBinding",
            baseConcept = "Transfers::transfers",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.Default
        ),
        // Flow with ownedEndFeatures subsets flowTransfers
        SemanticBinding(
            name = "flowWithEndsBinding",
            baseConcept = "Transfers::flowTransfers",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.CollectionNotEmpty("ownedEndFeature")
        )
    ),
    description = "A Flow is a Step that represents the transfer of values from one Feature to another. Flows can take non-zero time to complete."
)
