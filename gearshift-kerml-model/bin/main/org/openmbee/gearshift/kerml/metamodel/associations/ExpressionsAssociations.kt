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

import org.openmbee.mdm.framework.meta.MetaAssociation
import org.openmbee.mdm.framework.meta.MetaAssociationEnd

/**
 * Figure 33: Expressions
 * Defines associations for Expressions.
 */
fun createExpressionAssociations(): List<MetaAssociation> {

    // InstantiationExpression has argument : Expression [0..*] {ordered, derived}
    val instantiationArgumentAssociation = MetaAssociation(
        name = "instantiationArgumentAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "instantiation",
            type = "InstantiationExpression",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            derivationConstraint = "computeExpressionInstantiation"
        ),
        targetEnd = MetaAssociationEnd(
            name = "argument",
            type = "Expression",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            derivationConstraint = "deriveInstantiationExpressionArgument"
        )
    )

    // InstantiationExpression has instantiatedType : Type [1..1] {derived, subsets member}
    val instantiationExpressionInstantiatedTypeAssociation = MetaAssociation(
        name = "instantiationExpressionInstantiatedTypeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "instantiationExpression",
            type = "InstantiationExpression",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            derivationConstraint = "computeTypeInstantiationExpression"
        ),
        targetEnd = MetaAssociationEnd(
            name = "instantiatedType",
            type = "Type",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("member"),
            derivationConstraint = "deriveInstantiationExpressionInstantiatedType",
        )
    )

    // FeatureReferenceExpression has referent : Feature [1..1] {derived, subsets member}
    val referenceExpressionReferentAssociation = MetaAssociation(
        name = "referenceExpressionReferentAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "referenceExpression",
            type = "FeatureReferenceExpression",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("namespace"),
            derivationConstraint = "computeFeatureReferenceExpression"
        ),
        targetEnd = MetaAssociationEnd(
            name = "referent",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("member"),
            derivationConstraint = "deriveFeatureReferenceExpressionReferent",
        )
    )

    // FeatureChainExpression has targetFeature : Feature [1..1] {derived, subsets member}
    val chainExpressionTargetFeatureAssociation = MetaAssociation(
        name = "chainExpressionTargetFeatureAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "chainExpression",
            type = "FeatureChainExpression",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("namespace"),
            derivationConstraint = "computeFeatureChainExpression"
        ),
        targetEnd = MetaAssociationEnd(
            name = "targetFeature",
            type = "Feature",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("member"),
            derivationConstraint = "deriveFeatureChainExpressionTargetFeature",
        )
    )

    // MetadataAccessExpression has referencedElement : Element [1..1] {derived, subsets member}
    val accessExpressionReferencedElementAssociation = MetaAssociation(
        name = "accessExpressionReferencedElementAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "accessExpression",
            type = "MetadataAccessExpression",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("namespace"),
            derivationConstraint = "computeElementAccessExpression"
        ),
        targetEnd = MetaAssociationEnd(
            name = "referencedElement",
            type = "Element",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("member"),
            derivationConstraint = "deriveMetadataAccessExpressionReferencedElement",
        )
    )

    return listOf(
        accessExpressionReferencedElementAssociation,
        chainExpressionTargetFeatureAssociation,
        instantiationArgumentAssociation,
        instantiationExpressionInstantiatedTypeAssociation,
        referenceExpressionReferentAssociation,
    )
}
