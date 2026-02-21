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
 * Figure 19: Connection Definition and Usage
 */
fun createConnectionDefinitionAndUsageAssociations(): List<MetaAssociation> {

    // Definition has ownedConnection : ConnectorAsUsage [0..*] {ordered, derived, subsets ownedUsage}
    val connectionOwningDefinitionOwnedConnectionAssociation = MetaAssociation(
        name = "connectionOwningDefinitionOwnedConnectionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "connectionOwningDefinition",
            type = "Definition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("partOwningDefinition"),
            derivationConstraint = "deriveConnectionUsageConnectionOwningDefinition"
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedConnection",
            type = "ConnectorAsUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("ownedUsage"),
            derivationConstraint = "deriveDefinitionOwnedConnection"
        )
    )

    // ConnectionDefinition has connectionEnd : Usage [0..*] {ordered, derived, redefines associationEnd}
    val connectionDefinitionWithEndConnectionEndAssociation = MetaAssociation(
        name = "connectionDefinitionWithEndConnectionEndAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "connectionDefinitionWithEnd",
            type = "ConnectionDefinition",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            redefines = listOf("associationWithEnd"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "connectionEnd",
            type = "Usage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            redefines = listOf("associationEnd"),
            derivationConstraint = "deriveConnectionDefinitionConnectionEnd"
        )
    )

    // Usage has nestedConnection : ConnectionUsage [0..*] {ordered, derived, subsets nestedUsage}
    val connectionOwningUsageNestedConnectionAssociation = MetaAssociation(
        name = "connectionOwningUsageNestedConnectionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "connectionOwningUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("partOwningUsage"),
            derivationConstraint = "deriveConnectionUsageConnectionOwningUsage"
        ),
        targetEnd = MetaAssociationEnd(
            name = "nestedConnection",
            type = "ConnectionUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("nestedUsage"),
            derivationConstraint = "deriveUsageNestedConnection"
        )
    )

    // ConnectionUsage has connectionDefinition : AssociationStructure [0..*] {ordered, derived, subsets itemDefinition, redefines association}
    val definedConnectionConnectionDefinitionAssociation = MetaAssociation(
        name = "definedConnectionConnectionDefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "definedConnection",
            type = "ConnectionUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("typedConnector"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "connectionDefinition",
            type = "AssociationStructure",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("itemDefinition"),
            redefines = listOf("association"),
            derivationConstraint = "deriveConnectionUsageConnectionDefinition"
        )
    )

    return listOf(
        connectionDefinitionWithEndConnectionEndAssociation,
        connectionOwningDefinitionOwnedConnectionAssociation,
        connectionOwningUsageNestedConnectionAssociation,
        definedConnectionConnectionDefinitionAssociation
    )
}
