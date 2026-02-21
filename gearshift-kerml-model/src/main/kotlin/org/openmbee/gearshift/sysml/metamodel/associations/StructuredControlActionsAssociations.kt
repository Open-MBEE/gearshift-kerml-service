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
 * Figure 29: Structured Control Actions
 */
fun createStructuredControlActionsAssociations(): List<MetaAssociation> {

    // IfActionUsage has thenAction : ActionUsage [1..1] {derived}
    val ifThenActionThenActionAssociation = MetaAssociation(
        name = "ifThenActionThenActionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "ifThenAction",
            type = "IfActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "thenAction",
            type = "ActionUsage",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveIfActionUsageThenAction"
        )
    )

    // IfActionUsage has elseAction : ActionUsage [0..1] {derived}
    val ifElseActionElseActionAssociation = MetaAssociation(
        name = "ifElseActionElseActionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "ifElseAction",
            type = "IfActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "elseAction",
            type = "ActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveIfActionUsageElseAction"
        )
    )

    // IfActionUsage has ifArgument : Expression [1..1] {derived}
    val ifActionIfArgumentAssociation = MetaAssociation(
        name = "ifActionIfArgumentAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "ifAction",
            type = "IfActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "ifArgument",
            type = "Expression",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveIfActionUsageIfArgument"
        )
    )

    // LoopActionUsage has bodyAction : ActionUsage [1..1] {derived}
    val loopActionBodyActionAssociation = MetaAssociation(
        name = "loopActionBodyActionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "loopAction",
            type = "LoopActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "bodyAction",
            type = "ActionUsage",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveLoopActionUsageBodyAction"
        )
    )

    // WhileLoopActionUsage has whileArgument : Expression [1..1] {derived}
    val whileLoopActionWhileArgumentAssociation = MetaAssociation(
        name = "whileLoopActionWhileArgumentAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "whileLoopAction",
            type = "WhileLoopActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "whileArgument",
            type = "Expression",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveWhileLoopActionUsageWhileArgument"
        )
    )

    // WhileLoopActionUsage has untilArgument : Expression [0..1] {derived}
    val untilLoopActionUntilArgumentAssociation = MetaAssociation(
        name = "untilLoopActionUntilArgumentAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "untilLoopAction",
            type = "WhileLoopActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "untilArgument",
            type = "Expression",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveWhileLoopActionUsageUntilArgument"
        )
    )

    // ForLoopActionUsage has seqArgument : Expression [1..1] {derived}
    val forLoopActionSeqArgumentAssociation = MetaAssociation(
        name = "forLoopActionSeqArgumentAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "forLoopAction",
            type = "ForLoopActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "seqArgument",
            type = "Expression",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveForLoopActionUsageSeqArgument"
        )
    )

    // ForLoopActionUsage has loopVariable : ReferenceUsage [1..1] {derived}
    val forLoopActionLoopVariableAssociation = MetaAssociation(
        name = "forLoopActionLoopVariableAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "forLoopAction",
            type = "ForLoopActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "loopVariable",
            type = "ReferenceUsage",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveForLoopActionUsageLoopVariable"
        )
    )

    return listOf(
        forLoopActionLoopVariableAssociation,
        forLoopActionSeqArgumentAssociation,
        ifActionIfArgumentAssociation,
        ifElseActionElseActionAssociation,
        ifThenActionThenActionAssociation,
        loopActionBodyActionAssociation,
        untilLoopActionUntilArgumentAssociation,
        whileLoopActionWhileArgumentAssociation
    )
}
