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
 * Figure 38: Multiplicities
 * Defines associations for Multiplicities.
 */
fun createMultiplicityAssociations(): List<MetaAssociation> {

    // MultiplicityRange has bound : Expression [1..2] {ordered, derived, subsets ownedMember}
    val multiplicityBoundAssociation = MetaAssociation(
        name = "multiplicityBoundAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "multiplicity",
            type = "MultiplicityRange",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("owningType"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "bound",
            type = "Expression",
            lowerBound = 1,
            upperBound = 2,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("ownedMember"),
            derivationConstraint = "deriveMultiplicityRangeBound",
        )
    )

    // MultiplicityRange has lowerBound : Expression [0..1] {derived, subsets bound}
    val multiplicityLowerBoundAssociation = MetaAssociation(
        name = "multiplicityLowerBoundAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "multiplicity",
            type = "MultiplicityRange",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("multiplicity"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "lowerBound",
            type = "Expression",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("bound"),
            derivationConstraint = "deriveMultiplicityRangeLowerBound",
        )
    )

    // MultiplicityRange has upperBound : Expression [1..1] {derived, subsets bound}
    val multiplicityUpperBoundAssociation = MetaAssociation(
        name = "multiplicityUpperBoundAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "multiplicity",
            type = "MultiplicityRange",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("multiplicity"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "upperBound",
            type = "Expression",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("bound"),
            derivationConstraint = "deriveMultiplicityRangeUpperBound",
        )
    )

    return listOf(
        multiplicityBoundAssociation,
        multiplicityLowerBoundAssociation,
        multiplicityUpperBoundAssociation,
    )
}
