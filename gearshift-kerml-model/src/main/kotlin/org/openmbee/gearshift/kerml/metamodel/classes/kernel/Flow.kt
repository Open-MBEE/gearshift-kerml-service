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

import org.openmbee.gearshift.framework.meta.ConstraintType
import org.openmbee.gearshift.framework.meta.MetaClass
import org.openmbee.gearshift.framework.meta.MetaConstraint

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
            name = "checkFlowSpecialization",
            type = ConstraintType.IMPLICIT_SPECIALIZATION,
            expression = "specializesFromLibrary('Transfers::transfers')",
            libraryTypeName = "Transfers::transfers",
            description = "A Flow must directly or indirectly specialize the Step Transfers::transfers from the Kernel Semantic Library."
        ),
        MetaConstraint(
            name = "checkFlowWithEndsSpecialization",
            type = ConstraintType.CONDITIONAL_IMPLICIT_SPECIALIZATION,
            expression = "ownedEndFeature->notEmpty() implies specializesFromLibrary('Transfers::flowTransfers')",
            libraryTypeName = "Transfers::flowTransfers",
            description = "A Flow with ownedEndFeatures must specialize the Step Transfers::flowTransfers from the Kernel Semantic Library."
        ),
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
        )
    ),
    description = "A Flow is a Step that represents the transfer of values from one Feature to another. Flows can take non-zero time to complete."
)
