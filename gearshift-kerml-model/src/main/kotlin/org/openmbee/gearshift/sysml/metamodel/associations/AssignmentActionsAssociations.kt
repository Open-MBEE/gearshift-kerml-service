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
 * Figure 27: Assignment Actions
 */
fun createAssignmentActionsAssociations(): List<MetaAssociation> {

    // AssignmentActionUsage has targetArgument : Expression [0..1] {derived}
    val assignmentActionTargetArgumentAssociation = MetaAssociation(
        name = "assignmentActionTargetArgumentAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "assignmentAction",
            type = "AssignmentActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "targetArgument",
            type = "Expression",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveAssignmentActionUsageTargetArgument"
        )
    )

    // AssignmentActionUsage has valueExpression : Expression [0..1] {derived}
    val assigningActionValueExpressionAssociation = MetaAssociation(
        name = "assigningActionValueExpressionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "assigningAction",
            type = "AssignmentActionUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "valueExpression",
            type = "Expression",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveAssignmentActionUsageValueExpression"
        )
    )

    // AssignmentActionUsage has referent : Feature [1..1] {derived, subsets member}
    val assignmentReferentAssociation = MetaAssociation(
        name = "assignmentReferentAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "assignment",
            type = "AssignmentActionUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("namespace"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "referent",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("member"),
            derivationConstraint = "deriveAssignmentActionUsageReferent"
        )
    )

    return listOf(
        assigningActionValueExpressionAssociation,
        assignmentActionTargetArgumentAssociation,
        assignmentReferentAssociation
    )
}
