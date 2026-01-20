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
package org.openmbee.gearshift.kerml.metamodel.associations

import org.openmbee.gearshift.metamodel.MetaAssociation
import org.openmbee.gearshift.metamodel.MetaAssociationEnd

/**
 * Figure 30: Functions
 * Defines associations for Functions.
 */
fun createFunctionAssociations(): List<MetaAssociation> {

    // Function has expression : Expression [0..*] {derived, subsets ownedFeature}
    val computedFunctionExpressionAssociation = MetaAssociation(
        name = "computedFunctionExpressionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "computedFunction",
            type = "Function",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("featuringBehavior"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "expression",
            type = "Expression",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            subsets = listOf("step"),
        )
    )

    // Expression has result : Feature [1..1] {subsets output, redefines result}
    val computingExpressionResultAssociation = MetaAssociation(
        name = "computingExpressionResultAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "computingExpression",
            type = "Expression",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("parameteredStep"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "result",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("output", "parameter"),
            derivationConstraint = "deriveExpressionResult",
        )
    )

    // Function has result : Feature [0..1] {derived, ordered, subsets output}
    val computingFunctionResultAssociation = MetaAssociation(
        name = "computingFunctionResultAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "computingFunction",
            type = "Function",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("parameteredBehavior"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "result",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("output", "parameter"),
            derivationConstraint = "deriveFunctionResult",
        )
    )

    // Expression has function : Function [0..*] {ordered, derived, redefines behavior}
    val typedExpressionFunctionAssociation = MetaAssociation(
        name = "typedExpressionFunctionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "typedExpression",
            type = "Expression",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("typedStep"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "function",
            type = "Function",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            redefines = listOf("behavior"),
            derivationConstraint = "deriveExpressionFunction",
        )
    )

    return listOf(
        computedFunctionExpressionAssociation,
        computingExpressionResultAssociation,
        computingFunctionResultAssociation,
        typedExpressionFunctionAssociation,
    )
}
