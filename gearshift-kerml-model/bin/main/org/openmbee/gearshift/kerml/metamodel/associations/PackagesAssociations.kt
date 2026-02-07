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

import org.openmbee.mdm.framework.meta.AggregationKind
import org.openmbee.mdm.framework.meta.MetaAssociation
import org.openmbee.mdm.framework.meta.MetaAssociationEnd

/**
 * Figure 40: Packages
 * Defines associations for Packages.
 */
fun createPackageAssociations(): List<MetaAssociation> {

    // Package has filterCondition : Expression [0..*] {derived}
    val conditionedPackageFilterConditionAssociation = MetaAssociation(
        name = "conditionedPackageFilterConditionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "conditionedPackage",
            type = "Package",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("owningNamespace"),
            derivationConstraint = "computeExpressionConditionedPackage"
        ),
        targetEnd = MetaAssociationEnd(
            name = "filterCondition",
            type = "Expression",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("ownedMember"),
            derivationConstraint = "derivePackageFilterCondition"
        )
    )

    // ElementFilterMembership has condition : Expression [1..1] {redefines ownedMemberElement}
    val owningFilterConditionAssociation = MetaAssociation(
        name = "owningFilterConditionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningFilter",
            type = "ElementFilterMembership",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("owningMembership"),
            derivationConstraint = "computeExpressionOwningFilter"
        ),
        targetEnd = MetaAssociationEnd(
            name = "condition",
            type = "Expression",
            lowerBound = 1,
            upperBound = 1,
            aggregation = AggregationKind.COMPOSITE,
            redefines = listOf("ownedMemberElement"),
        )
    )

    return listOf(
        conditionedPackageFilterConditionAssociation,
        owningFilterConditionAssociation,
    )
}
