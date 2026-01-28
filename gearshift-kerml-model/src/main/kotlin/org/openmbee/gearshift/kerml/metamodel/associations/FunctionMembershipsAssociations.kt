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

import org.openmbee.gearshift.framework.meta.AggregationKind
import org.openmbee.gearshift.framework.meta.MetaAssociation
import org.openmbee.gearshift.framework.meta.MetaAssociationEnd

/**
 * Figure 32: Function Memberships
 * Defines associations for Function Memberships.
 */
fun createFunctionMembershipAssociations(): List<MetaAssociation> {

    // ResultExpressionMembership has ownedResultExpression : Expression [1..1] {redefines ownedMemberFeature}
    val owningResultExpressionMembershipOwnedResultExpressionAssociation = MetaAssociation(
        name = "owningResultExpressionMembershipOwnedResultExpressionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningResultExpressionMembership",
            type = "ResultExpressionMembership",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningFeatureMembership"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedResultExpression",
            type = "Expression",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            aggregation = AggregationKind.COMPOSITE,
            redefines = listOf("ownedMemberFeature"),
            derivationConstraint = "deriveResultExpressionMembershipOwnedResultExpression",
        )
    )

    return listOf(
        owningResultExpressionMembershipOwnedResultExpressionAssociation,
    )
}
