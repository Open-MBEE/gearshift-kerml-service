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
 * Figure 36: Asserted Constraints
 */
fun createAssertedConstraintsAssociations(): List<MetaAssociation> {

    // AssertConstraintUsage has assertedConstraint : ConstraintUsage [1..1] {derived}
    val constraintAssertionAssertedConstraintAssociation = MetaAssociation(
        name = "constraintAssertionAssertedConstraintAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "constraintAssertion",
            type = "AssertConstraintUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "assertedConstraint",
            type = "ConstraintUsage",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveAssertConstraintUsageAssertedConstraint"
        )
    )

    return listOf(
        constraintAssertionAssertedConstraintAssociation
    )
}
