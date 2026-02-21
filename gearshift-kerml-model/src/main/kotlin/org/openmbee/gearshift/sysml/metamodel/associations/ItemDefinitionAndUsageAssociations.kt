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
 * Figure 14: Item Definition and Usage
 */
fun createItemDefinitionAndUsageAssociations(): List<MetaAssociation> {

    // Definition has ownedItem : ItemUsage [0..*] {ordered, derived, subsets ownedOccurrence}
    val itemOwningDefinitionOwnedItemAssociation = MetaAssociation(
        name = "itemOwningDefinitionOwnedItemAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "itemOwningDefinition",
            type = "Definition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("occurrenceOwningDefinition"),
            derivationConstraint = "deriveItemUsageItemOwningDefinition"
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedItem",
            type = "ItemUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("ownedOccurrence"),
            derivationConstraint = "deriveDefinitionOwnedItem"
        )
    )

    // Usage has nestedItem : ItemUsage [0..*] {ordered, derived, subsets nestedOccurrence}
    val itemOwningUsageNestedItemAssociation = MetaAssociation(
        name = "itemOwningUsageNestedItemAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "itemOwningUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("occurrenceOwningUsage"),
            derivationConstraint = "deriveItemUsageItemOwningUsage"
        ),
        targetEnd = MetaAssociationEnd(
            name = "nestedItem",
            type = "ItemUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("nestedOccurrence"),
            derivationConstraint = "deriveUsageNestedItem"
        )
    )

    // ItemUsage has itemDefinition : Structure [0..*] {ordered, derived, subsets occurrenceDefinition}
    val definedItemItemDefinitionAssociation = MetaAssociation(
        name = "definedItemItemDefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "definedItem",
            type = "ItemUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("definedOccurrence"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "itemDefinition",
            type = "Structure",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("occurrenceDefinition"),
            derivationConstraint = "deriveItemUsageItemDefinition"
        )
    )

    return listOf(
        definedItemItemDefinitionAssociation,
        itemOwningDefinitionOwnedItemAssociation,
        itemOwningUsageNestedItemAssociation
    )
}
