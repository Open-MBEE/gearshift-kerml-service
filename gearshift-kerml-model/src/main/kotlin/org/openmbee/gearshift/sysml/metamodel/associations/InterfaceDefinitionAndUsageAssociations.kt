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
 * Figure 20: Interface Definition and Usage
 */
fun createInterfaceDefinitionAndUsageAssociations(): List<MetaAssociation> {

    // Definition has ownedInterface : InterfaceUsage [0..*] {ordered, derived, subsets ownedConnection}
    val interfaceOwningDefinitionOwnedInterfaceAssociation = MetaAssociation(
        name = "interfaceOwningDefinitionOwnedInterfaceAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "interfaceOwningDefinition",
            type = "Definition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("connectionOwningDefinition"),
            derivationConstraint = "deriveInterfaceUsageInterfaceOwningDefinition"
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedInterface",
            type = "InterfaceUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("ownedConnection"),
            derivationConstraint = "deriveDefinitionOwnedInterface"
        )
    )

    // Usage has nestedInterface : InterfaceUsage [0..*] {ordered, derived, subsets nestedConnection}
    val interfaceOwningUsageNestedInterfaceAssociation = MetaAssociation(
        name = "interfaceOwningUsageNestedInterfaceAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "interfaceOwningUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("connectionOwningUsage"),
            derivationConstraint = "deriveInterfaceUsageInterfaceOwningUsage"
        ),
        targetEnd = MetaAssociationEnd(
            name = "nestedInterface",
            type = "InterfaceUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("nestedConnection"),
            derivationConstraint = "deriveUsageNestedInterface"
        )
    )

    // InterfaceUsage has interfaceDefinition : InterfaceDefinition [0..*] {ordered, derived, redefines connectionDefinition}
    val definedInterfaceInterfaceDefinitionAssociation = MetaAssociation(
        name = "definedInterfaceInterfaceDefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "definedInterface",
            type = "InterfaceUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("definedConnection"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "interfaceDefinition",
            type = "InterfaceDefinition",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            redefines = listOf("connectionDefinition"),
            derivationConstraint = "deriveInterfaceUsageInterfaceDefinition"
        )
    )

    // InterfaceDefinition has interfaceEnd : PortUsage [0..*] {ordered, derived, redefines connectionEnd}
    val interfaceDefinitionWithEndInterfaceEndAssociation = MetaAssociation(
        name = "interfaceDefinitionWithEndInterfaceEndAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "interfaceDefinitionWithEnd",
            type = "InterfaceDefinition",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("connectionDefinitionWithEnd"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "interfaceEnd",
            type = "PortUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            redefines = listOf("connectionEnd"),
            derivationConstraint = "deriveInterfaceDefinitionInterfaceEnd"
        )
    )

    return listOf(
        definedInterfaceInterfaceDefinitionAssociation,
        interfaceDefinitionWithEndInterfaceEndAssociation,
        interfaceOwningDefinitionOwnedInterfaceAssociation,
        interfaceOwningUsageNestedInterfaceAssociation
    )
}
