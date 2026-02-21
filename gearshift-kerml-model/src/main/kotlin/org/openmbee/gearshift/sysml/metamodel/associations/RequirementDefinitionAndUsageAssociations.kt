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
package org.openmbee.gearshift.sysml.metamodel.associations

import org.openmbee.mdm.framework.meta.MetaAssociation
import org.openmbee.mdm.framework.meta.MetaAssociationEnd

/**
 * Figure 37: Requirement Definition and Usage
 */
fun createRequirementDefinitionAndUsageAssociations(): List<MetaAssociation> {

    // Definition has ownedRequirement : RequirementUsage [0..*] {ordered, derived, subsets ownedConstraint}
    val requirementOwningDefinitionOwnedRequirementAssociation = MetaAssociation(
        name = "requirementOwningDefinitionOwnedRequirementAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "requirementOwningDefinition",
            type = "Definition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("constraintOwningDefinition"),
            derivationConstraint = "deriveRequirementUsageRequirementOwningDefinition"
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedRequirement",
            type = "RequirementUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("ownedConstraint"),
            derivationConstraint = "deriveDefinitionOwnedRequirement"
        )
    )

    // Usage has nestedRequirement : RequirementUsage [0..*] {ordered, derived, subsets nestedConstraint}
    val requirementOwningUsageNestedRequirementAssociation = MetaAssociation(
        name = "requirementOwningUsageNestedRequirementAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "requirementOwningUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            redefines = listOf("constraintOwningUsage"),
            derivationConstraint = "deriveRequirementUsageRequirementOwningUsage"
        ),
        targetEnd = MetaAssociationEnd(
            name = "nestedRequirement",
            type = "RequirementUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("nestedConstraint"),
            derivationConstraint = "deriveUsageNestedRequirement"
        )
    )

    // RequirementDefinition has requiredConstraint : ConstraintUsage [0..*] {ordered, derived, subsets ownedFeature}
    val requiringRequirementDefinitionRequiredConstraintAssociation = MetaAssociation(
        name = "requiringRequirementDefinitionRequiredConstraintAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "requiringRequirementDefinition",
            type = "RequirementDefinition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningType"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "requiredConstraint",
            type = "ConstraintUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("ownedFeature"),
            derivationConstraint = "deriveRequirementDefinitionRequiredConstraint"
        )
    )

    // RequirementDefinition has assumedConstraint : ConstraintUsage [0..*] {ordered, derived, subsets ownedFeature}
    val assumingRequirementDefinitionAssumedConstraintAssociation = MetaAssociation(
        name = "assumingRequirementDefinitionAssumedConstraintAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "assumingRequirementDefinition",
            type = "RequirementDefinition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningType"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "assumedConstraint",
            type = "ConstraintUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("ownedFeature"),
            derivationConstraint = "deriveRequirementDefinitionAssumedConstraint"
        )
    )

    // RequirementUsage has requirementDefinition : RequirementDefinition [0..1] {derived, redefines constraintDefinition}
    val definedRequirementRequirementDefinitionAssociation = MetaAssociation(
        name = "definedRequirementRequirementDefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "definedRequirement",
            type = "RequirementUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("definedConstraint"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "requirementDefinition",
            type = "RequirementDefinition",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            redefines = listOf("constraintDefinition"),
            derivationConstraint = "deriveRequirementUsageRequirementDefinition"
        )
    )

    // RequirementUsage has requiredConstraint : ConstraintUsage [0..*] {ordered, derived, subsets ownedFeature}
    val requiringRequirementRequiredConstraintAssociation = MetaAssociation(
        name = "requiringRequirementRequiredConstraintAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "requiringRequirement",
            type = "RequirementUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningType"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "requiredConstraint",
            type = "ConstraintUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("ownedFeature"),
            derivationConstraint = "deriveRequirementUsageRequiredConstraint"
        )
    )

    // RequirementUsage has assumedConstraint : ConstraintUsage [0..*] {ordered, derived, subsets ownedFeature}
    val assumingRequirementAssumedConstraintAssociation = MetaAssociation(
        name = "assumingRequirementAssumedConstraintAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "assumingRequirement",
            type = "RequirementUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningType"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "assumedConstraint",
            type = "ConstraintUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("ownedFeature"),
            derivationConstraint = "deriveRequirementUsageAssumedConstraint"
        )
    )

    // RequirementDefinition has framedConcern : ConcernUsage [0..*] {ordered, derived, subsets requiredConstraint}
    val framingRequirementDefinitionFramedConcernAssociation = MetaAssociation(
        name = "framingRequirementDefinitionFramedConcernAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "framingRequirementDefinition",
            type = "RequirementDefinition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("assumingRequirementDefinition"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "framedConcern",
            type = "ConcernUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("requiredConstraint"),
            derivationConstraint = "deriveRequirementDefinitionFramedConcern"
        )
    )

    // RequirementDefinition has stakeholderParameter : PartUsage [0..*] {ordered, derived, subsets parameter, usage}
    val stakeholderOwningRequirementDefinitionStakeholderParameterAssociation = MetaAssociation(
        name = "stakeholderOwningRequirementDefinitionStakeholderParameterAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "stakeholderOwningRequirementDefinition",
            type = "RequirementDefinition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("parameteredStep", "partOwningUsage"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "stakeholderParameter",
            type = "PartUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("parameter", "usage"),
            derivationConstraint = "deriveRequirementDefinitionStakeholderParameter"
        )
    )

    // RequirementDefinition has actorParameter : PartUsage [0..*] {ordered, derived, subsets parameter, usage}
    val actorOwningRequirementDefinitionActorParameterAssociation = MetaAssociation(
        name = "actorOwningRequirementDefinitionActorParameterAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "actorOwningRequirementDefinition",
            type = "RequirementDefinition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("parameteredBehavior", "partOwningDefinition"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "actorParameter",
            type = "PartUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("parameter", "usage"),
            derivationConstraint = "deriveRequirementDefinitionActorParameter"
        )
    )

    // RequirementDefinition has subjectParameter : Usage [1..1] {derived, subsets parameter, usage}
    val subjectOwningRequirementDefinitionSubjectParameterAssociation = MetaAssociation(
        name = "subjectOwningRequirementDefinitionSubjectParameterAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "subjectOwningRequirementDefinition",
            type = "RequirementDefinition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningDefinition", "parameteredBehavior"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "subjectParameter",
            type = "Usage",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("parameter", "usage"),
            derivationConstraint = "deriveRequirementDefinitionSubjectParameter"
        )
    )

    // RequirementUsage has subjectParameter : Usage [1..1] {derived, subsets parameter, usage}
    val subjectOwningRequirementSubjectParameterAssociation = MetaAssociation(
        name = "subjectOwningRequirementSubjectParameterAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "subjectOwningRequirement",
            type = "RequirementUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningUsage", "parameteredStep"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "subjectParameter",
            type = "Usage",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("parameter", "usage"),
            derivationConstraint = "deriveRequirementUsageSubjectParameter"
        )
    )

    // RequirementUsage has actorParameter : PartUsage [0..*] {ordered, derived, subsets parameter, usage}
    val actorOwningRequirementActorParameterAssociation = MetaAssociation(
        name = "actorOwningRequirementActorParameterAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "actorOwningRequirement",
            type = "RequirementUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("parameteredStep", "partOwningUsage"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "actorParameter",
            type = "PartUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("parameter", "usage"),
            derivationConstraint = "deriveRequirementUsageActorParameter"
        )
    )

    // RequirementUsage has stakeholderParameter : PartUsage [0..*] {ordered, derived, subsets parameter, usage}
    val stakeholderOwningRequirementStakeholderParameterAssociation = MetaAssociation(
        name = "stakeholderOwningRequirementStakeholderParameterAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "stakeholderOwningRequirement",
            type = "RequirementUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("parameteredStep", "partOwningUsage"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "stakeholderParameter",
            type = "PartUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("parameter", "usage"),
            derivationConstraint = "deriveRequirementUsageStakeholderParameter"
        )
    )

    // RequirementUsage has framedConcern : ConcernUsage [0..*] {ordered, derived, subsets requiredConstraint}
    val framingRequirementFramedConcernAssociation = MetaAssociation(
        name = "framingRequirementFramedConcernAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "framingRequirement",
            type = "RequirementUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("requiringRequirement"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "framedConcern",
            type = "ConcernUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("requiredConstraint"),
            derivationConstraint = "deriveRequirementUsageFramedConcern"
        )
    )

    return listOf(
        actorOwningRequirementActorParameterAssociation,
        actorOwningRequirementDefinitionActorParameterAssociation,
        assumingRequirementAssumedConstraintAssociation,
        assumingRequirementDefinitionAssumedConstraintAssociation,
        definedRequirementRequirementDefinitionAssociation,
        framingRequirementDefinitionFramedConcernAssociation,
        framingRequirementFramedConcernAssociation,
        requirementOwningDefinitionOwnedRequirementAssociation,
        requirementOwningUsageNestedRequirementAssociation,
        requiringRequirementDefinitionRequiredConstraintAssociation,
        requiringRequirementRequiredConstraintAssociation,
        stakeholderOwningRequirementDefinitionStakeholderParameterAssociation,
        stakeholderOwningRequirementStakeholderParameterAssociation,
        subjectOwningRequirementDefinitionSubjectParameterAssociation,
        subjectOwningRequirementSubjectParameterAssociation
    )
}
