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
 * Figure 11: Enumeration Definition and Usage
 */
fun createEnumerationDefinitionAndUsageAssociations(): List<MetaAssociation> {

    // Definition has ownedEnumeration : EnumerationUsage [0..*] {ordered, derived, subsets ownedAttribute}
    val enumerationOwningDefinitionOwnedEnumerationAssociation = MetaAssociation(
        name = "enumerationOwningDefinitionOwnedEnumerationAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "enumerationOwningDefinition",
            type = "Definition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("attributeOwningDefinition"),
            derivationConstraint = "deriveEnumerationUsageEnumerationOwningDefinition"
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedEnumeration",
            type = "EnumerationUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("ownedAttribute"),
            derivationConstraint = "deriveDefinitionOwnedEnumeration"
        )
    )

    // EnumerationDefinition has enumeratedValue : EnumerationUsage [0..*] {ordered, derived, subsets variant}
    val owningEnumerationDefinitionEnumeratedValueAssociation = MetaAssociation(
        name = "owningEnumerationDefinitionEnumeratedValueAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningEnumerationDefinition",
            type = "EnumerationDefinition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningVariationDefinition"),
            derivationConstraint = "deriveEnumerationUsageOwningEnumerationDefinition"
        ),
        targetEnd = MetaAssociationEnd(
            name = "enumeratedValue",
            type = "EnumerationUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("variant"),
            derivationConstraint = "deriveEnumerationDefinitionEnumeratedValue"
        )
    )

    // EnumerationUsage has enumerationDefinition : EnumerationDefinition [1..1] {derived, redefines attributeDefinition}
    val definedEnumerationEnumerationDefinitionAssociation = MetaAssociation(
        name = "definedEnumerationEnumerationDefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "definedEnumeration",
            type = "EnumerationUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("definedAttribute"),
            derivationConstraint = "computeEnumerationDefinitionDefinedEnumeration"
        ),
        targetEnd = MetaAssociationEnd(
            name = "enumerationDefinition",
            type = "EnumerationDefinition",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            redefines = listOf("attributeDefinition"),
            derivationConstraint = "deriveEnumerationUsageEnumerationDefinition"
        )
    )

    // Usage has nestedEnumeration : EnumerationUsage [0..*] {ordered, derived, subsets nestedAttribute}
    val enumerationOwningUsageNestedEnumerationAssociation = MetaAssociation(
        name = "enumerationOwningUsageNestedEnumerationAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "enumerationOwningUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningUsage"),
            derivationConstraint = "deriveEnumerationUsageEnumerationOwningUsage"
        ),
        targetEnd = MetaAssociationEnd(
            name = "nestedEnumeration",
            type = "EnumerationUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("nestedAttribute"),
            derivationConstraint = "deriveUsageNestedEnumeration"
        )
    )

    return listOf(
        definedEnumerationEnumerationDefinitionAssociation,
        enumerationOwningDefinitionOwnedEnumerationAssociation,
        enumerationOwningUsageNestedEnumerationAssociation,
        owningEnumerationDefinitionEnumeratedValueAssociation
    )
}
