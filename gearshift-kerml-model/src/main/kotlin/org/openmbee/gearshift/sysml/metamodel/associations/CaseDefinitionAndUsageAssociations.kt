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
 * Figure 42: Case Definition and Usage
 */
fun createCaseDefinitionAndUsageAssociations(): List<MetaAssociation> {

    // Definition has ownedCase : CaseUsage [0..*] {ordered, derived, subsets ownedCalculation}
    val caseOwningDefinitionOwnedCaseAssociation = MetaAssociation(
        name = "caseOwningDefinitionOwnedCaseAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "caseOwningDefinition",
            type = "Definition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("calculationOwningDefinition"),
            derivationConstraint = "deriveCaseUsageCaseOwningDefinition"
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedCase",
            type = "CaseUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("ownedCalculation"),
            derivationConstraint = "deriveDefinitionOwnedCase"
        )
    )

    // Usage has nestedCase : CaseUsage [0..*] {ordered, derived, subsets nestedCalculation}
    val caseOwningUsageNestedCaseAssociation = MetaAssociation(
        name = "caseOwningUsageNestedCaseAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "caseOwningUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            redefines = listOf("calculationOwningUsage"),
            derivationConstraint = "deriveCaseUsageCaseOwningUsage"
        ),
        targetEnd = MetaAssociationEnd(
            name = "nestedCase",
            type = "CaseUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("nestedCalculation"),
            derivationConstraint = "deriveUsageNestedCase"
        )
    )

    // CaseUsage has caseDefinition : CaseDefinition [0..1] {ordered, derived, redefines calculationDefinition}
    val definedCaseCaseDefinitionAssociation = MetaAssociation(
        name = "definedCaseCaseDefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "definedCase",
            type = "CaseUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("definedCalculation"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "caseDefinition",
            type = "CaseDefinition",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isOrdered = true,
            redefines = listOf("calculationDefinition"),
            derivationConstraint = "deriveCaseUsageCaseDefinition"
        )
    )

    // CaseDefinition has objectiveRequirement : RequirementUsage [0..1] {ordered, derived, subsets usage}
    val objectiveOwningCaseDefinitionObjectiveRequirementAssociation = MetaAssociation(
        name = "objectiveOwningCaseDefinitionObjectiveRequirementAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "objectiveOwningCaseDefinition",
            type = "CaseDefinition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningType"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "objectiveRequirement",
            type = "RequirementUsage",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("usage"),
            derivationConstraint = "deriveCaseDefinitionObjectiveRequirement"
        )
    )

    // CaseDefinition has actorParameter : PartUsage [0..*] {ordered, derived, subsets parameter, usage}
    val actorOwningCaseDefinitionActorParameterAssociation = MetaAssociation(
        name = "actorOwningCaseDefinitionActorParameterAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "actorOwningCaseDefinition",
            type = "CaseDefinition",
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
            derivationConstraint = "deriveCaseDefinitionActorParameter"
        )
    )

    // CaseDefinition has subjectParameter : Usage [1..1] {derived, subsets parameter, usage}
    val subjectOwningCaseDefinitionSubjectParameterAssociation = MetaAssociation(
        name = "subjectOwningCaseDefinitionSubjectParameterAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "subjectOwningCaseDefinition",
            type = "CaseDefinition",
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
            derivationConstraint = "deriveCaseDefinitionSubjectParameter"
        )
    )

    // CaseUsage has subjectParameter : Usage [1..1] {derived, subsets parameter, usage}
    val subjectOwningCaseSubjectParameterAssociation = MetaAssociation(
        name = "subjectOwningCaseSubjectParameterAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "subjectOwningCase",
            type = "CaseUsage",
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
            derivationConstraint = "deriveCaseUsageSubjectParameter"
        )
    )

    // CaseUsage has actorParameter : PartUsage [0..*] {ordered, derived, subsets parameter, usage}
    val actorOwningCaseActorParameterAssociation = MetaAssociation(
        name = "actorOwningCaseActorParameterAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "actorOwningCase",
            type = "CaseUsage",
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
            derivationConstraint = "deriveCaseUsageActorParameter"
        )
    )

    // CaseUsage has objectiveRequirement : RequirementUsage [0..1] {ordered, derived, subsets usage}
    val objectiveOwningCaseObjectiveRequirementAssociation = MetaAssociation(
        name = "objectiveOwningCaseObjectiveRequirementAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "objectiveOwningCase",
            type = "CaseUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("requirementOwningUsage"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "objectiveRequirement",
            type = "RequirementUsage",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("usage"),
            derivationConstraint = "deriveCaseUsageObjectiveRequirement"
        )
    )

    return listOf(
        actorOwningCaseActorParameterAssociation,
        actorOwningCaseDefinitionActorParameterAssociation,
        caseOwningDefinitionOwnedCaseAssociation,
        caseOwningUsageNestedCaseAssociation,
        definedCaseCaseDefinitionAssociation,
        objectiveOwningCaseDefinitionObjectiveRequirementAssociation,
        objectiveOwningCaseObjectiveRequirementAssociation,
        subjectOwningCaseDefinitionSubjectParameterAssociation,
        subjectOwningCaseSubjectParameterAssociation
    )
}
