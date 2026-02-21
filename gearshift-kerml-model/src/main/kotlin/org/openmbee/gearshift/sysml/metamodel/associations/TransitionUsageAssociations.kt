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
 * Figure 33: Transition Usage
 */
fun createTransitionUsageAssociations(): List<MetaAssociation> {

    // Definition has ownedTransition : TransitionUsage [0..*] {ordered, derived, subsets ownedUsage}
    val transitionOwningDefinitionOwnedTransitionAssociation = MetaAssociation(
        name = "transitionOwningDefinitionOwnedTransitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "transitionOwningDefinition",
            type = "Definition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningDefinition"),
            derivationConstraint = "deriveTransitionUsageTransitionOwningDefinition"
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedTransition",
            type = "TransitionUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("ownedUsage"),
            derivationConstraint = "deriveDefinitionOwnedTransition"
        )
    )

    // Usage has nestedTransition : TransitionUsage [0..*] {ordered, derived, subsets nestedAction}
    // Note: sourceEnd redefines owningUsage (not actionOwningUsage)
    val transitionOwningUsageNestedTransitionAssociation = MetaAssociation(
        name = "transitionOwningUsageNestedTransitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "transitionOwningUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            redefines = listOf("owningUsage"),
            derivationConstraint = "deriveTransitionUsageTransitionOwningUsage"
        ),
        targetEnd = MetaAssociationEnd(
            name = "nestedTransition",
            type = "TransitionUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("nestedAction"),
            derivationConstraint = "deriveUsageNestedTransition"
        )
    )

    // TransitionUsage has triggerAction : AcceptActionUsage [0..*] {derived, subsets ownedFeature}
    val triggeredTransitionTriggerActionAssociation = MetaAssociation(
        name = "triggeredTransitionTriggerActionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "triggeredTransition",
            type = "TransitionUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningType"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "triggerAction",
            type = "AcceptActionUsage",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            subsets = listOf("ownedFeature"),
            derivationConstraint = "deriveTransitionUsageTriggerAction"
        )
    )

    // TransitionUsage has guardExpression : Expression [0..*] {derived, subsets ownedFeature}
    val guardedTransitionGuardExpressionAssociation = MetaAssociation(
        name = "guardedTransitionGuardExpressionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "guardedTransition",
            type = "TransitionUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningType"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "guardExpression",
            type = "Expression",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            subsets = listOf("ownedFeature"),
            derivationConstraint = "deriveTransitionUsageGuardExpression"
        )
    )

    // TransitionUsage has succession : Succession [1..1] {derived, subsets ownedMember}
    val linkedTransitionSuccessionAssociation = MetaAssociation(
        name = "linkedTransitionSuccessionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "linkedTransition",
            type = "TransitionUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningNamespace"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "succession",
            type = "Succession",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("ownedMember"),
            derivationConstraint = "deriveTransitionUsageSuccession"
        )
    )

    // TransitionUsage has effectAction : ActionUsage [0..*] {ordered, derived, subsets feature}
    val activeTransitionEffectActionAssociation = MetaAssociation(
        name = "activeTransitionEffectActionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "activeTransition",
            type = "TransitionUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningType"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "effectAction",
            type = "ActionUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("feature"),
            derivationConstraint = "deriveTransitionUsageEffectAction"
        )
    )

    // TransitionUsage has target : ActionUsage [1..1] {derived}
    val incomingTransitionTargetAssociation = MetaAssociation(
        name = "incomingTransitionTargetAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "incomingTransition",
            type = "TransitionUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "target",
            type = "ActionUsage",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveTransitionUsageTarget"
        )
    )

    // TransitionUsage has source : ActionUsage [1..1] {derived}
    val outgoingTransitionSourceAssociation = MetaAssociation(
        name = "outgoingTransitionSourceAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "outgoingTransition",
            type = "TransitionUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "source",
            type = "ActionUsage",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveTransitionUsageSource"
        )
    )

    return listOf(
        activeTransitionEffectActionAssociation,
        guardedTransitionGuardExpressionAssociation,
        incomingTransitionTargetAssociation,
        linkedTransitionSuccessionAssociation,
        outgoingTransitionSourceAssociation,
        transitionOwningDefinitionOwnedTransitionAssociation,
        transitionOwningUsageNestedTransitionAssociation,
        triggeredTransitionTriggerActionAssociation
    )
}
