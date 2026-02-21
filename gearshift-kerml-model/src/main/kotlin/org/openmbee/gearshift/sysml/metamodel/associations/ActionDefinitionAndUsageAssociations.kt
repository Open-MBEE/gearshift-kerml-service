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
 * Figure 23: Action Definition and Usage
 */
fun createActionDefinitionAndUsageAssociations(): List<MetaAssociation> {

    // Definition has ownedAction : ActionUsage [0..*] {ordered, derived, subsets ownedOccurrence}
    val actionOwningDefinitionOwnedActionAssociation = MetaAssociation(
        name = "actionOwningDefinitionOwnedActionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "actionOwningDefinition",
            type = "Definition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("occurrenceOwningDefinition"),
            derivationConstraint = "deriveActionUsageActionOwningDefinition"
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedAction",
            type = "ActionUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("ownedOccurrence"),
            derivationConstraint = "deriveDefinitionOwnedAction"
        )
    )

    // ActionDefinition has action : ActionUsage [0..*] {ordered, derived, subsets usage}
    val featuringActionDefinitionActionAssociation = MetaAssociation(
        name = "featuringActionDefinitionActionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "featuringActionDefinition",
            type = "ActionDefinition",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("featuringBehavior", "featuringDefinition"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "action",
            type = "ActionUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("usage"),
            derivationConstraint = "deriveActionDefinitionAction"
        )
    )

    // Usage has nestedAction : ActionUsage [0..*] {ordered, derived, subsets nestedOccurrence}
    val actionOwningUsageNestedActionAssociation = MetaAssociation(
        name = "actionOwningUsageNestedActionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "actionOwningUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("occurrenceOwningUsage"),
            derivationConstraint = "deriveActionUsageActionOwningUsage"
        ),
        targetEnd = MetaAssociationEnd(
            name = "nestedAction",
            type = "ActionUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("nestedOccurrence"),
            derivationConstraint = "deriveUsageNestedAction"
        )
    )

    // ActionUsage has actionDefinition : Behavior [0..*] {ordered, derived, redefines behavior, occurrenceDefinition}
    val definedActionActionDefinition = MetaAssociation(
        name = "definedActionActionDefinition",
        sourceEnd = MetaAssociationEnd(
            name = "definedAction",
            type = "ActionUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("definedOccurrence", "typedStep"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "actionDefinition",
            type = "Behavior",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            redefines = listOf("behavior", "occurrenceDefinition"),
            derivationConstraint = "deriveActionUsageActionDefinition"
        )
    )

    return listOf(
        actionOwningDefinitionOwnedActionAssociation,
        actionOwningUsageNestedActionAssociation,
        definedActionActionDefinition,
        featuringActionDefinitionActionAssociation
    )
}
