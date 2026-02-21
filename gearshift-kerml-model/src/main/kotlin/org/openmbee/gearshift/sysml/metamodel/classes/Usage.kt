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
import org.openmbee.mdm.framework.meta.MetaOperation
import org.openmbee.mdm.framework.meta.MetaProperty

/**
 * SysML Usage metaclass.
 * Specializes: Feature
 * A feature that represents a usage.
 */
fun createUsageMetaClass() = MetaClass(
    name = "Usage",
    isAbstract = false,
    superclasses = listOf("Feature"),
    attributes = listOf(
        MetaProperty(
            name = "isReference",
            type = "Boolean",
            isDerived = true,
            derivationConstraint = "deriveUsageIsReference",
            description = "Whether this Usage is a referential Usage, that is, it has isComposite = false."
        ),
        MetaProperty(
            name = "mayTimeVary",
            type = "Boolean",
            isDerived = true,
            derivationConstraint = "deriveUsageMayTimeVary",
            description = "Whether this Usage may time vary."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "checkUsageVariationDefinitionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningVariationDefinition <> null implies
                specializes(owningVariationDefinition)
            """.trimIndent(),
            description = "If a Usage has an owningVariationDefinition, then it must directly or indirectly specialize that Definition."
        ),
        MetaConstraint(
            name = "checkUsageVariationUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningVariationUsage <> null implies
                specializes(owningVariationUsage)
            """.trimIndent(),
            description = "If a Usage has an owningVariationUsage, then it must directly or indirectly specialize that Usage."
        ),
        MetaConstraint(
            name = "checkUsageVariationUsageTypeFeaturing",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningVariationUsage <> null implies
                featuringType->asSet() = owningVariationUsage.featuringType->asSet()
            """.trimIndent(),
            description = "If a Usage has an owningVariationUsage, then it must have the same featuringTypes as that Usage."
        ),
        MetaConstraint(
            name = "deriveUsageDirectedUsage",
            type = ConstraintType.DERIVATION,
            expression = "directedFeature->selectByKind(Usage)",
            description = "The directedUsages of a Usage are all its directedFeatures that are Usages."
        ),
        MetaConstraint(
            name = "deriveUsageIsReference",
            type = ConstraintType.DERIVATION,
            expression = "not isComposite",
            description = "A Usage is referential if it is not composite."
        ),
        MetaConstraint(
            name = "deriveUsageMayTimeVary",
            type = ConstraintType.DERIVATION,
            expression = """
                owningType <> null and
                owningType.specializesFromLibrary('Occurrences::Occurrence') and
                not (
                    isPortion or
                    specializesFromLibrary('Links::SelfLink') or
                    specializesFromLibrary('Occurrences::HappensLink') or
                    isComposite and specializesFromLibrary('Actions::Action')
                )
            """.trimIndent(),
            description = "A Usage mayTimeVary if its owningType specializes Occurrences::Occurrence and it is not a portion, SelfLink, HappensLink, or a composite Action."
        ),
        MetaConstraint(
            name = "deriveUsageNestedAction",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(ActionUsage)",
            description = "The nestedActions of a Usage are all its nestedUsages that are ActionUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedAllocation",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(AllocationUsage)",
            description = "The nestedAllocations of a Usage are all its nestedUsages that are AllocationUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedAnalysisCase",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(AnalysisCaseUsage)",
            description = "The nestedAnalysisCases of a Usage are all its nestedUsages that are AnalysisCaseUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedAttribute",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(AttributeUsage)",
            description = "The nestedAttributes of a Usage are all its nestedUsages that are AttributeUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedCalculation",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(CalculationUsage)",
            description = "The nestedCalculations of a Usage are all its nestedUsages that are CalculationUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedCase",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(CaseUsage)",
            description = "The nestedCases of a Usage are all its nestedUsages that are CaseUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedConcern",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(ConcernUsage)",
            description = "The nestedConcerns of a Usage are all its nestedUsages that are ConcernUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedConnection",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(ConnectorAsUsage)",
            description = "The nestedConnections of a Usage are all its nestedUsages that are ConnectorAsUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedConstraint",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(ConstraintUsage)",
            description = "The nestedConstraints of a Usage are all its nestedUsages that are ConstraintUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedEnumeration",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(EnumerationUsage)",
            description = "The nestedEnumerations of a Usage are all its nestedUsages that are EnumerationUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedFlow",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(FlowConnectionUsage)",
            description = "The nestedFlows of a Usage are all its nestedUsages that are FlowConnectionUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedInterface",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(ReferenceUsage)",
            description = "The nestedInterfaces of a Usage are all its nestedUsages that are InterfaceUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedItem",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(ItemUsage)",
            description = "The nestedItems of a Usage are all its nestedUsages that are ItemUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedMetadata",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(MetadataUsage)",
            description = "The nestedMetadata of a Usage are all its nestedUsages that are MetadataUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedOccurrence",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(OccurrenceUsage)",
            description = "The nestedOccurrences of a Usage are all its nestedUsages that are OccurrenceUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedPart",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(PartUsage)",
            description = "The nestedParts of a Usage are all its nestedUsages that are PartUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedPort",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(PortUsage)",
            description = "The nestedPorts of a Usage are all its nestedUsages that are PortUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedReference",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(ReferenceUsage)",
            description = "The nestedReferences of a Usage are all its nestedUsages that are ReferenceUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedRendering",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(RenderingUsage)",
            description = "The nestedRenderings of a Usage are all its nestedUsages that are RenderingUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedRequirement",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(RequirementUsage)",
            description = "The nestedRequirements of a Usage are all its nestedUsages that are RequirementUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedState",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(StateUsage)",
            description = "The nestedStates of a Usage are all its nestedUsages that are StateUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedTransition",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(TransitionUsage)",
            description = "The nestedTransitions of a Usage are all its nestedUsages that are TransitionUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedUsage",
            type = ConstraintType.DERIVATION,
            expression = "ownedFeature->selectByKind(Usage)",
            description = "The nestedUsages of a Usage are all its ownedFeatures that are Usages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedUseCase",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(UseCaseUsage)",
            description = "The nestedUseCases of a Usage are all its nestedUsages that are UseCaseUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedVerificationCase",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(VerificationCaseUsage)",
            description = "The nestedVerificationCases of a Usage are all its nestedUsages that are VerificationCaseUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedView",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(ViewUsage)",
            description = "The nestedViews of a Usage are all its nestedUsages that are ViewUsages."
        ),
        MetaConstraint(
            name = "deriveUsageNestedViewpoint",
            type = ConstraintType.DERIVATION,
            expression = "nestedUsage->selectByKind(ViewpointUsage)",
            description = "The nestedViewpoints of a Usage are all its nestedUsages that are ViewpointUsages."
        ),
        MetaConstraint(
            name = "deriveUsageUsage",
            type = ConstraintType.DERIVATION,
            expression = "feature->selectByKind(Usage)",
            description = "The usages of a Usage are all its features that are Usages."
        ),
        MetaConstraint(
            name = "deriveUsageVariant",
            type = ConstraintType.DERIVATION,
            expression = "variantMembership.ownedVariantUsage",
            description = "The variants of a Usage are the ownedVariantUsages of its variantMemberships."
        ),
        MetaConstraint(
            name = "deriveUsageVariantMembership",
            type = ConstraintType.DERIVATION,
            expression = "ownedMembership->selectByKind(VariantMembership)",
            description = "The variantMemberships of a Usage are those ownedMemberships that are VariantMemberships."
        ),
        MetaConstraint(
            name = "validateUsageIsReferential",
            type = ConstraintType.VERIFICATION,
            expression = """
                direction <> null or isEnd or featuringType->isEmpty() implies
                isReference
            """.trimIndent(),
            description = "A Usage that is directed, an end feature or has no featuringTypes must be referential."
        ),
        MetaConstraint(
            name = "validateUsageVariationIsAbstract",
            type = ConstraintType.VERIFICATION,
            expression = "isVariation implies isAbstract",
            description = "If a Usage is a variation, then it must be abstract."
        ),
        MetaConstraint(
            name = "validateUsageVariationOwnedFeatureMembership",
            type = ConstraintType.VERIFICATION,
            expression = "isVariation implies ownedFeatureMembership->isEmpty()",
            description = "If a Usage is a variation, then it must not have any ownedFeatureMemberships."
        ),
        MetaConstraint(
            name = "validateUsageVariationSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isVariation implies
                not ownedSpecialization.specific->exists(
                    oclIsKindOf(Definition) and
                    oclAsType(Definition).isVariation or
                    oclIsKindOf(Usage) and
                    oclAsType(Usage).isVariation)
            """.trimIndent(),
            description = "A variation Usage may not specialize any variation Definition or Usage."
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "namingFeature",
            returnType = "Feature",
            returnLowerBound = 0,
            returnUpperBound = 1,
            redefines = "namingFeature",
            body = MetaOperation.ocl("""
                if not owningMembership.oclIsKindOf(VariantMembership) then
                    self.oclAsType(Feature).namingFeature()
                else if ownedReferenceSubsetting = null then null
                else ownedReferenceSubsetting.referencedFeature
                endif endif
            """.trimIndent()),
            description = "If this Usage is a variant, then its namingFeature is the referencedFeature of its ownedReferenceSubsetting."
        ),
        MetaOperation(
            name = "referencedFeatureTarget",
            returnType = "Feature",
            returnLowerBound = 0,
            returnUpperBound = 1,
            body = MetaOperation.ocl("""
                if ownedReferenceSubsetting = null then null
                else ownedReferenceSubsetting.referencedFeature.featureTarget
                endif
            """.trimIndent()),
            description = "If ownedReferenceSubsetting is not null, return the featureTarget of the referencedFeature of the ownedReferenceSubsetting."
        )
    ),
    description = "A feature that represents a usage"
)
