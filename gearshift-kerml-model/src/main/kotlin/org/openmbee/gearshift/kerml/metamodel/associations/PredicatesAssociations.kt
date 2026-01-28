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

import org.openmbee.gearshift.framework.meta.MetaAssociation
import org.openmbee.gearshift.framework.meta.MetaAssociationEnd

/**
 * Figure 31: Predicates
 * Defines associations for Predicates.
 */
fun createPredicateAssociations(): List<MetaAssociation> {

    // BooleanExpression has predicate : Predicate [0..*] {ordered, derived, redefines function}
    val typedBooleanExpressionPredicateAssociation = MetaAssociation(
        name = "typedBooleanExpressionPredicateAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "typedBooleanExpression",
            type = "BooleanExpression",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("typedExpression"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "predicate",
            type = "Predicate",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            redefines = listOf("function"),
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
        )
    )

    return listOf(
        typedBooleanExpressionPredicateAssociation,
        typedExpressionFunctionAssociation,
    )
}
