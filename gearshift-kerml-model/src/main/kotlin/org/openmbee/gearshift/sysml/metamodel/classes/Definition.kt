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
package org.openmbee.gearshift.sysml.metamodel.classes

import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.MetaProperty

/**
 * KerML Definition metaclass.
 * Specializes: Classifier
 * A classifier that defines something.
 */
fun createDefinitionMetaClass() = MetaClass(
    name = "Definition",
    isAbstract = false,
    superclasses = listOf("Classifier"),
    attributes = listOf(
        MetaProperty(
            name = "isVariation",
            type = "Boolean",
            description = "Whether this is a variation definition"
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "deriveDefinitionDirectedUsage",
            type = ConstraintType.DERIVATION,
            expression = "directedFeature->selectByKind(Usage)",
            description = "The directedUsages of a Definition are all its directedFeatures that are Usages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedAction",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(ActionUsage)",
            description = "The ownedActions of a Definition are all its ownedUsages that are ActionUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedAllocation",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(AllocationUsage)",
            description = "The ownedAllocations of a Definition are all its ownedUsages that are AllocationUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedAnalysisCase",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(AnalysisCaseUsage)",
            description = "The ownedAnalysisCases of a Definition are all its ownedUsages that are AnalysisCaseUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedAttribute",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(AttributeUsage)",
            description = "The ownedAttributes of a Definition are all its ownedUsages that are AttributeUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedCalculation",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(CalculationUsage)",
            description = "The ownedCalculations of a Definition are all its ownedUsages that are CalculationUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedCase",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(CaseUsage)",
            description = "The ownedCases of a Definition are all its ownedUsages that are CaseUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedConcern",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(ConcernUsage)",
            description = "The ownedConcerns of a Definition are all its ownedUsages that are ConcernUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedConnection",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(ConnectorAsUsage)",
            description = "The ownedConnections of a Definition are all its ownedUsages that are ConnectorAsUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedConstraint",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(ConstraintUsage)",
            description = "The ownedConstraints of a Definition are all its ownedUsages that are ConstraintUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedEnumeration",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(EnumerationUsage)",
            description = "The ownedEnumerations of a Definition are all its ownedUsages that are EnumerationUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedFlow",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(FlowConnectionUsage)",
            description = "The ownedFlows of a Definition are all its ownedUsages that are FlowConnectionUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedInterface",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(ReferenceUsage)",
            description = "The ownedInterfaces of a Definition are all its ownedUsages that are InterfaceUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedItem",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(ItemUsage)",
            description = "The ownedItems of a Definition are all its ownedUsages that are ItemUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedMetadata",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(MetadataUsage)",
            description = "The ownedMetadata of a Definition are all its ownedUsages that are MetadataUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedOccurrence",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(OccurrenceUsage)",
            description = "The ownedOccurrences of a Definition are all its ownedUsages that are OccurrenceUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedPart",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(PartUsage)",
            description = "The ownedParts of a Definition are all its ownedUsages that are PartUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedPort",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(PortUsage)",
            description = "The ownedPorts of a Definition are all its ownedUsages that are PortUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedReference",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(ReferenceUsage)",
            description = "The ownedReferences of a Definition are all its ownedUsages that are ReferenceUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedRendering",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(RenderingUsage)",
            description = "The ownedRenderings of a Definition are all its ownedUsages that are RenderingUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedRequirement",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(RequirementUsage)",
            description = "The ownedRequirements of a Definition are all its ownedUsages that are RequirementUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedState",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(StateUsage)",
            description = "The ownedStates of a Definition are all its ownedUsages that are StateUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedTransition",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(TransitionUsage)",
            description = "The ownedTransitions of a Definition are all its ownedUsages that are TransitionUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedUsage",
            type = ConstraintType.DERIVATION,
            expression = "ownedFeature->selectByKind(Usage)",
            description = "The ownedUsages of a Definition are all its ownedFeatures that are Usages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedUseCase",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(UseCaseUsage)",
            description = "The ownedUseCases of a Definition are all its ownedUsages that are UseCaseUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedVerificationCase",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(VerificationCaseUsage)",
            description = "The ownedVerificationCases of a Definition are all its ownedUsages that are VerificationCaseUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedView",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(ViewUsage)",
            description = "The ownedViews of a Definition are all its ownedUsages that are ViewUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionOwnedViewpoint",
            type = ConstraintType.DERIVATION,
            expression = "ownedUsage->selectByKind(ViewpointUsage)",
            description = "The ownedViewpoints of a Definition are all its ownedUsages that are ViewpointUsages."
        ),
        MetaConstraint(
            name = "deriveDefinitionUsage",
            type = ConstraintType.DERIVATION,
            expression = "feature->selectByKind(Usage)",
            description = "The usages of a Definition are all its features that are Usages."
        ),
        MetaConstraint(
            name = "deriveDefinitionVariant",
            type = ConstraintType.DERIVATION,
            expression = "variantMembership.ownedVariantUsage",
            description = "The variants of a Definition are the ownedVariantUsages of its variantMemberships."
        ),
        MetaConstraint(
            name = "deriveDefinitionVariantMembership",
            type = ConstraintType.DERIVATION,
            expression = "ownedMembership->selectByKind(VariantMembership)",
            description = "The variantMemberships of a Definition are those ownedMemberships that are VariantMemberships."
        )
    ),
    description = "A classifier that defines something"
)
