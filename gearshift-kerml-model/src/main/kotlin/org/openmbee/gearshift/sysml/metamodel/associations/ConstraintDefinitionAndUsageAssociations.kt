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
 * Figure 35: Constraint Definition and Usage
 */
fun createConstraintDefinitionAndUsageAssociations(): List<MetaAssociation> {

    // Definition has ownedConstraint : ConstraintUsage [0..*] {ordered, derived, subsets ownedOccurrence}
    val constraintOwningDefinitionOwnedConstraintAssociation = MetaAssociation(
        name = "constraintOwningDefinitionOwnedConstraintAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "constraintOwningDefinition",
            type = "Definition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("occurrenceOwningDefinition"),
            derivationConstraint = "deriveConstraintUsageConstraintOwningDefinition"
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedConstraint",
            type = "ConstraintUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("ownedOccurrence"),
            derivationConstraint = "deriveDefinitionOwnedConstraint"
        )
    )

    // Usage has nestedConstraint : ConstraintUsage [0..*] {ordered, derived, subsets nestedOccurrence}
    val constraintOwningUsageNestedConstraintAssociation = MetaAssociation(
        name = "constraintOwningUsageNestedConstraintAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "constraintOwningUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            redefines = listOf("occurrenceOwningUsage"),
            derivationConstraint = "deriveConstraintUsageConstraintOwningUsage"
        ),
        targetEnd = MetaAssociationEnd(
            name = "nestedConstraint",
            type = "ConstraintUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("nestedOccurrence"),
            derivationConstraint = "deriveUsageNestedConstraint"
        )
    )

    // ConstraintUsage has constraintDefinition : Predicate [0..1] {derived, redefines predicate}
    val definedConstraintConstraintDefinitionAssociation = MetaAssociation(
        name = "definedConstraintConstraintDefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "definedConstraint",
            type = "ConstraintUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("typedBooleanExpression"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "constraintDefinition",
            type = "Predicate",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            redefines = listOf("predicate"),
            derivationConstraint = "deriveConstraintUsageConstraintDefinition"
        )
    )

    return listOf(
        constraintOwningDefinitionOwnedConstraintAssociation,
        constraintOwningUsageNestedConstraintAssociation,
        definedConstraintConstraintDefinitionAssociation
    )
}
