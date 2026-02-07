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
 * Figure 37: Feature Values
 * Defines associations for Feature Values.
 */
fun createFeatureValueAssociations(): List<MetaAssociation> {

    // FeatureValue has value : Expression [1..1] {redefines ownedMemberElement}
    val expressedValuationValueAssociation = MetaAssociation(
        name = "expressedValuationValueAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "expressedValuation",
            type = "FeatureValue",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("owningMembership"),
            derivationConstraint = "computeExpressionExpressedValuation"
        ),
        targetEnd = MetaAssociationEnd(
            name = "value",
            type = "Expression",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            aggregation = AggregationKind.COMPOSITE,
            redefines = listOf("ownedMemberElement"),
            derivationConstraint = "deriveFeatureValueValue"
        )
    )

    // FeatureValue has featureWithValue : Feature [1..1] {subsets membershipOwningNamespace}
    val valuationFeatureWithValueAssociation = MetaAssociation(
        name = "valuationFeatureWithValueAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "valuation",
            type = "FeatureValue",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("ownedMembership"),
            derivationConstraint = "computeFeatureValuation"
        ),
        targetEnd = MetaAssociationEnd(
            name = "featureWithValue",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("membershipOwningNamespace"),
            derivationConstraint = "deriveFeatureValueFeatureWithValue"
        )
    )

    return listOf(
        expressedValuationValueAssociation,
        valuationFeatureWithValueAssociation,
    )
}
