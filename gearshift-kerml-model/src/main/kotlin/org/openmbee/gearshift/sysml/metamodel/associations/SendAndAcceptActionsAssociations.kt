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
 * Figure 26: Send and Accept Actions
 */
fun createSendAndAcceptActionsAssociations(): List<MetaAssociation> {

    // SendActionUsage has payloadArgument : Expression [1..1] {derived}
    val sendingActionUsagePayloadArgumentAssociation = MetaAssociation(
        name = "sendingActionUsagePayloadArgumentAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "sendingActionUsage",
            type = "SendActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "payloadArgument",
            type = "Expression",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveSendActionUsagePayloadArgument"
        )
    )

    // SendActionUsage has senderArgument : Expression [0..1] {derived}
    val senderActionUsageSenderArgumentAssociation = MetaAssociation(
        name = "senderActionUsageSenderArgumentAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "senderActionUsage",
            type = "SendActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "senderArgument",
            type = "Expression",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveSendActionUsageSenderArgument"
        )
    )

    // SendActionUsage has receiverArgument : Expression [0..1] {derived}
    val sendActionUsageReceiverArgumentAssociation = MetaAssociation(
        name = "sendActionUsageReceiverArgumentAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "sendActionUsage",
            type = "SendActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "receiverArgument",
            type = "Expression",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveSendActionUsageReceiverArgument"
        )
    )

    // AcceptActionUsage has receiverArgument : Expression [0..1] {derived}
    val acceptActionUsageReceiverArgumentAssociation = MetaAssociation(
        name = "acceptActionUsageReceiverArgumentAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "acceptActionUsage",
            type = "AcceptActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "receiverArgument",
            type = "Expression",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveAcceptActionUsageReceiverArgument"
        )
    )

    // AcceptActionUsage has payloadArgument : Expression [0..1] {derived}
    val acceptingActionUsagePayloadArgumentAssociation = MetaAssociation(
        name = "acceptingActionUsagePayloadArgumentAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "acceptingActionUsage",
            type = "AcceptActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "payloadArgument",
            type = "Expression",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveAcceptActionUsagePayloadArgument"
        )
    )

    // AcceptActionUsage has payloadParameter : ReferenceUsage [1..1] {derived, subsets nestedReference, parameter}
    val owningAcceptActionUsagePayloadParameterAssociation = MetaAssociation(
        name = "owningAcceptActionUsagePayloadParameterAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningAcceptActionUsage",
            type = "AcceptActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("referenceOwningUsage"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "payloadParameter",
            type = "ReferenceUsage",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("nestedReference", "parameter"),
            derivationConstraint = "deriveAcceptActionUsagePayloadParameter"
        )
    )

    return listOf(
        acceptActionUsageReceiverArgumentAssociation,
        acceptingActionUsagePayloadArgumentAssociation,
        owningAcceptActionUsagePayloadParameterAssociation,
        sendActionUsageReceiverArgumentAssociation,
        senderActionUsageSenderArgumentAssociation,
        sendingActionUsagePayloadArgumentAssociation
    )
}
