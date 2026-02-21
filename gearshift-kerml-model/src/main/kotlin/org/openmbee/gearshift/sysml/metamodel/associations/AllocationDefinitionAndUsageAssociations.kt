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
 * Figure 21: Allocation Definition and Usage
 */
fun createAllocationDefinitionAndUsageAssociations(): List<MetaAssociation> {

    // Definition has ownedAllocation : AllocationUsage [0..*] {ordered, derived, subsets ownedConnection}
    val allocationOwningDefinitionOwnedAllocationAssociation = MetaAssociation(
        name = "allocationOwningDefinitionOwnedAllocationAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "allocationOwningDefinition",
            type = "Definition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("connectionOwningDefinition"),
            derivationConstraint = "deriveAllocationUsageAllocationOwningDefinition"
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedAllocation",
            type = "AllocationUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("ownedConnection"),
            derivationConstraint = "deriveDefinitionOwnedAllocation"
        )
    )

    // Usage has nestedAllocation : AllocationUsage [0..*] {ordered, derived, subsets nestedConnection}
    val allocationOwningUsageNestedAllocationAssociation = MetaAssociation(
        name = "allocationOwningUsageNestedAllocationAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "allocationOwningUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("connectionOwningUsage"),
            derivationConstraint = "deriveAllocationUsageAllocationOwningUsage"
        ),
        targetEnd = MetaAssociationEnd(
            name = "nestedAllocation",
            type = "AllocationUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("nestedConnection"),
            derivationConstraint = "deriveUsageNestedAllocation"
        )
    )

    // AllocationDefinition has allocation : AllocationUsage [0..*] {ordered, derived, subsets usage}
    val featuringAllocationDefinitionAllocationAssociation = MetaAssociation(
        name = "featuringAllocationDefinitionAllocationAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "featuringAllocationDefinition",
            type = "AllocationDefinition",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("featuringDefinition"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "allocation",
            type = "AllocationUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("usage"),
            derivationConstraint = "deriveAllocationDefinitionAllocation"
        )
    )

    // AllocationUsage has allocationDefinition : AllocationDefinition [0..*] {ordered, derived, redefines connectionDefinition}
    val definedAllocationAllocationDefinitionAssociation = MetaAssociation(
        name = "definedAllocationAllocationDefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "definedAllocation",
            type = "AllocationUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("definedConnection"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "allocationDefinition",
            type = "AllocationDefinition",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            redefines = listOf("connectionDefinition"),
            derivationConstraint = "deriveAllocationUsageAllocationDefinition"
        )
    )

    return listOf(
        allocationOwningDefinitionOwnedAllocationAssociation,
        allocationOwningUsageNestedAllocationAssociation,
        definedAllocationAllocationDefinitionAssociation,
        featuringAllocationDefinitionAllocationAssociation
    )
}
