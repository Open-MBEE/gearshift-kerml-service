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
 * Figure 16: Port Definition and Usage
 */
fun createPortDefinitionAndUsageAssociations(): List<MetaAssociation> {

    // Definition has ownedPort : PortUsage [0..*] {ordered, derived, subsets ownedUsage}
    val portOwningDefinitionOwnedPortAssociation = MetaAssociation(
        name = "portOwningDefinitionOwnedPortAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "portOwningDefinition",
            type = "Definition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningDefinition"),
            derivationConstraint = "derivePortUsagePortOwningDefinition"
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedPort",
            type = "PortUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("ownedUsage"),
            derivationConstraint = "deriveDefinitionOwnedPort"
        )
    )

    // Usage has nestedPort : PortUsage [0..*] {ordered, derived, subsets nestedUsage}
    val portOwningUsageNestedPortAssociation = MetaAssociation(
        name = "portOwningUsageNestedPortAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "portOwningUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            redefines = listOf("owningUsage"),
            derivationConstraint = "derivePortUsagePortOwningUsage"
        ),
        targetEnd = MetaAssociationEnd(
            name = "nestedPort",
            type = "PortUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("nestedUsage"),
            derivationConstraint = "deriveUsageNestedPort"
        )
    )

    // PortUsage has portDefinition : PortDefinition [0..*] {ordered, derived, redefines occurrenceDefinition}
    val definedPortPortDefinitionAssociation = MetaAssociation(
        name = "definedPortPortDefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "definedPort",
            type = "PortUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("definedOccurrence"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "portDefinition",
            type = "PortDefinition",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            redefines = listOf("occurrenceDefinition"),
            derivationConstraint = "derivePortUsagePortDefinition"
        )
    )

    return listOf(
        definedPortPortDefinitionAssociation,
        portOwningDefinitionOwnedPortAssociation,
        portOwningUsageNestedPortAssociation
    )
}
