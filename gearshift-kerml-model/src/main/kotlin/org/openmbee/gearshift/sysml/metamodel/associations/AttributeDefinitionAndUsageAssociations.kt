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
 * Figure 10: Attribute Definition and Usage
 */
fun createAttributeDefinitionAndUsageAssociations(): List<MetaAssociation> {

    // Definition has ownedAttribute : AttributeUsage [0..*] {ordered, derived, subsets ownedUsage}
    val attributeOwningDefinitionOwnedAttributeAssociation = MetaAssociation(
        name = "attributeOwningDefinitionOwnedAttributeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "attributeOwningDefinition",
            type = "Definition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningDefinition"),
            derivationConstraint = "deriveAttributeUsageAttributeOwningDefinition"
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedAttribute",
            type = "AttributeUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("ownedUsage"),
            derivationConstraint = "deriveDefinitionOwnedAttribute"
        )
    )

    // Usage has nestedAttribute : AttributeUsage [0..*] {ordered, derived, subsets nestedUsage}
    val attributeOwningUsageNestedAttributeAssociation = MetaAssociation(
        name = "attributeOwningUsageNestedAttributeAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "attributeOwningUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningUsage"),
            derivationConstraint = "deriveAttributeUsageAttributeOwningUsage"
        ),
        targetEnd = MetaAssociationEnd(
            name = "nestedAttribute",
            type = "AttributeUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("nestedUsage"),
            derivationConstraint = "deriveUsageNestedAttribute"
        )
    )

    // AttributeUsage has attributeDefinition : DataType [0..*] {derived, redefines definition}
    val definedAttributeAttributeDefinitionAssociation = MetaAssociation(
        name = "definedAttributeAttributeDefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "definedAttribute",
            type = "AttributeUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("definedUsage"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "attributeDefinition",
            type = "DataType",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            redefines = listOf("definition"),
            derivationConstraint = "deriveAttributeUsageAttributeDefinition"
        )
    )

    return listOf(
        attributeOwningDefinitionOwnedAttributeAssociation,
        attributeOwningUsageNestedAttributeAssociation,
        definedAttributeAttributeDefinitionAssociation
    )
}
