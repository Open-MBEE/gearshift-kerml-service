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
 * Figure 12: Occurrence Definition and Usage
 */
fun createOccurrenceDefinitionAndUsageAssociations(): List<MetaAssociation> {

    // Definition has ownedOccurrence : OccurrenceUsage [0..*] {ordered, derived, subsets ownedUsage}
    val occurrenceOwningDefinitionOwnedOccurrenceAssociation = MetaAssociation(
        name = "occurrenceOwningDefinitionOwnedOccurrenceAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "occurrenceOwningDefinition",
            type = "Definition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningDefinition"),
            derivationConstraint = "deriveOccurrenceUsageOccurrenceOwningDefinition"
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedOccurrence",
            type = "OccurrenceUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("ownedUsage"),
            derivationConstraint = "deriveDefinitionOwnedOccurrence"
        )
    )

    // Usage has nestedOccurrence : OccurrenceUsage [0..*] {ordered, derived, subsets nestedUsage}
    val occurrenceOwningUsageNestedOccurrenceAssociation = MetaAssociation(
        name = "occurrenceOwningUsageNestedOccurrenceAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "occurrenceOwningUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningUsage"),
            derivationConstraint = "deriveOccurrenceUsageOccurrenceOwningUsage"
        ),
        targetEnd = MetaAssociationEnd(
            name = "nestedOccurrence",
            type = "OccurrenceUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("nestedUsage"),
            derivationConstraint = "deriveUsageNestedOccurrence"
        )
    )

    // OccurrenceUsage has individualDefinition : OccurrenceDefinition [0..1] {derived, subsets occurrenceDefinition}
    val individualUsageIndividualDefinitionAssociation = MetaAssociation(
        name = "individualUsageIndividualDefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "individualUsage",
            type = "OccurrenceUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "individualDefinition",
            type = "OccurrenceDefinition",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("occurrenceDefinition"),
            derivationConstraint = "deriveOccurrenceUsageIndividualDefinition"
        )
    )

    // OccurrenceUsage has occurrenceDefinition : Class [0..*] {ordered, derived, redefines definition}
    val definedOccurrenceOccurrenceDefinitionAssociation = MetaAssociation(
        name = "definedOccurrenceOccurrenceDefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "definedOccurrence",
            type = "OccurrenceUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("definedUsage"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "occurrenceDefinition",
            type = "Class",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            redefines = listOf("definition"),
            derivationConstraint = "deriveOccurrenceUsageOccurrenceDefinition"
        )
    )

    return listOf(
        definedOccurrenceOccurrenceDefinitionAssociation,
        individualUsageIndividualDefinitionAssociation,
        occurrenceOwningDefinitionOwnedOccurrenceAssociation,
        occurrenceOwningUsageNestedOccurrenceAssociation
    )
}
