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
 * Figure 34: Calculation Definition and Usage
 */
fun createCalculationDefinitionAndUsageAssociations(): List<MetaAssociation> {

    // Definition has ownedCalculation : CalculationUsage [0..*] {ordered, derived, subsets ownedAction}
    val calculationOwningDefinitionOwnedCalculationAssociation = MetaAssociation(
        name = "calculationOwningDefinitionOwnedCalculationAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "calculationOwningDefinition",
            type = "Definition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("actionOwningDefinition"),
            derivationConstraint = "deriveCalculationUsageCalculationOwningDefinition"
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedCalculation",
            type = "CalculationUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("ownedAction"),
            derivationConstraint = "deriveDefinitionOwnedCalculation"
        )
    )

    // Usage has nestedCalculation : CalculationUsage [0..*] {ordered, derived, subsets nestedAction}
    val calculationOwningUsageNestedCalculationAssociation = MetaAssociation(
        name = "calculationOwningUsageNestedCalculationAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "calculationOwningUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            redefines = listOf("actionOwningUsage"),
            derivationConstraint = "deriveCalculationUsageCalculationOwningUsage"
        ),
        targetEnd = MetaAssociationEnd(
            name = "nestedCalculation",
            type = "CalculationUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("nestedAction"),
            derivationConstraint = "deriveUsageNestedCalculation"
        )
    )

    // CalculationDefinition has calculation : CalculationUsage [0..*] {ordered, derived, subsets action, expression}
    val featuringCalculationDefinitionCalculationAssociation = MetaAssociation(
        name = "featuringCalculationDefinitionCalculationAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "featuringCalculationDefinition",
            type = "CalculationDefinition",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("computedFunction", "featuringActionDefinition"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "calculation",
            type = "CalculationUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("action", "expression"),
            derivationConstraint = "deriveCalculationDefinitionCalculation"
        )
    )

    // CalculationUsage has calculationDefinition : Function [0..*] {ordered, derived, redefines actionDefinition, function}
    val definedCalculationCalculationDefinitionAssociation = MetaAssociation(
        name = "definedCalculationCalculationDefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "definedCalculation",
            type = "CalculationUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("definedAction", "typedExpression"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "calculationDefinition",
            type = "Function",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            redefines = listOf("actionDefinition", "function"),
            derivationConstraint = "deriveCalculationUsageCalculationDefinition"
        )
    )

    return listOf(
        calculationOwningDefinitionOwnedCalculationAssociation,
        calculationOwningUsageNestedCalculationAssociation,
        definedCalculationCalculationDefinitionAssociation,
        featuringCalculationDefinitionCalculationAssociation
    )
}
