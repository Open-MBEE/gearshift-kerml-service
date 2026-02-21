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
 * Figure 30: State Definition and Usage
 */
fun createStateDefinitionAndUsageAssociations(): List<MetaAssociation> {

    // Definition has ownedState : StateUsage [0..*] {ordered, derived, subsets ownedAction}
    val stateOwningDefinitionOwnedStateAssociation = MetaAssociation(
        name = "stateOwningDefinitionOwnedStateAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "stateOwningDefinition",
            type = "Definition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("actionOwningDefinition"),
            derivationConstraint = "deriveStateUsageStateOwningDefinition"
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedState",
            type = "StateUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("ownedAction"),
            derivationConstraint = "deriveDefinitionOwnedState"
        )
    )

    // Usage has nestedState : StateUsage [0..*] {ordered, derived, subsets nestedAction}
    val stateOwningUsageNestedStateAssociation = MetaAssociation(
        name = "stateOwningUsageNestedStateAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "stateOwningUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            redefines = listOf("actionOwningUsage"),
            derivationConstraint = "deriveStateUsageStateOwningUsage"
        ),
        targetEnd = MetaAssociationEnd(
            name = "nestedState",
            type = "StateUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("nestedAction"),
            derivationConstraint = "deriveUsageNestedState"
        )
    )

    // StateDefinition has entryAction : ActionUsage [0..1] {derived}
    val enteredStateDefinitionEntryActionAssociation = MetaAssociation(
        name = "enteredStateDefinitionEntryActionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "enteredStateDefinition",
            type = "StateDefinition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "entryAction",
            type = "ActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveStateDefinitionEntryAction"
        )
    )

    // StateDefinition has doAction : ActionUsage [0..1] {derived}
    val activeStateDefinitionDoActionAssociation = MetaAssociation(
        name = "activeStateDefinitionDoActionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "activeStateDefinition",
            type = "StateDefinition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "doAction",
            type = "ActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveStateDefinitionDoAction"
        )
    )

    // StateDefinition has exitAction : ActionUsage [0..1] {derived}
    val exitedStateDefinitionExitActionAssociation = MetaAssociation(
        name = "exitedStateDefinitionExitActionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "exitedStateDefinition",
            type = "StateDefinition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "exitAction",
            type = "ActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveStateDefinitionExitAction"
        )
    )

    // StateDefinition has state : StateUsage [0..*] {ordered, derived, subsets action}
    val featuringStateDefinitionStateAssociation = MetaAssociation(
        name = "featuringStateDefinitionStateAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "featuringStateDefinition",
            type = "StateDefinition",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("featuringBehavior"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "state",
            type = "StateUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("action"),
            derivationConstraint = "deriveStateDefinitionState"
        )
    )

    // StateUsage has stateDefinition : Behavior [0..*] {ordered, derived, redefines actionDefinition}
    val definedStateStateDefinitionAssociation = MetaAssociation(
        name = "definedStateStateDefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "definedState",
            type = "StateUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("definedAction"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "stateDefinition",
            type = "Behavior",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            redefines = listOf("actionDefinition"),
            derivationConstraint = "deriveStateUsageStateDefinition"
        )
    )

    // StateUsage has entryAction : ActionUsage [0..1] {derived}
    val enteredStateEntryActionAssociation = MetaAssociation(
        name = "enteredStateEntryActionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "enteredState",
            type = "StateUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "entryAction",
            type = "ActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveStateUsageEntryAction"
        )
    )

    // StateUsage has doAction : ActionUsage [0..1] {derived}
    val activeStateDoActionAssociation = MetaAssociation(
        name = "activeStateDoActionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "activeState",
            type = "StateUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "doAction",
            type = "ActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveStateUsageDoAction"
        )
    )

    // StateUsage has exitAction : ActionUsage [0..1] {derived}
    val exitedStateExitActionAssociation = MetaAssociation(
        name = "exitedStateExitActionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "exitedState",
            type = "StateUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "exitAction",
            type = "ActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveStateUsageExitAction"
        )
    )

    return listOf(
        activeStateDefinitionDoActionAssociation,
        activeStateDoActionAssociation,
        definedStateStateDefinitionAssociation,
        enteredStateDefinitionEntryActionAssociation,
        enteredStateEntryActionAssociation,
        exitedStateDefinitionExitActionAssociation,
        exitedStateExitActionAssociation,
        featuringStateDefinitionStateAssociation,
        stateOwningDefinitionOwnedStateAssociation,
        stateOwningUsageNestedStateAssociation
    )
}
