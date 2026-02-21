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
 * Figure 22: Flows
 */
fun createFlowsAssociations(): List<MetaAssociation> {

    // Definition has ownedFlow : FlowConnectionUsage [0..*] {derived, subsets ownedConnection}
    val flowOwningDefinitionOwnedFlowAssociation = MetaAssociation(
        name = "flowOwningDefinitionOwnedFlowAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "flowOwningDefinition",
            type = "Definition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("connectionOwningDefinition"),
            derivationConstraint = "deriveFlowConnectionUsageFlowOwningDefinition"
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedFlow",
            type = "FlowConnectionUsage",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            subsets = listOf("ownedConnection"),
            derivationConstraint = "deriveDefinitionOwnedFlow"
        )
    )

    // FlowConnectionDefinition has flowEnd : Usage [0..*] {ordered, derived, redefines associationEnd}
    val flowDefinitionWithEndFlowEndAssociation = MetaAssociation(
        name = "flowDefinitionWithEndFlowEndAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "flowDefinitionWithEnd",
            type = "FlowConnectionDefinition",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            redefines = listOf("associationWithEnd"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "flowEnd",
            type = "Usage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            redefines = listOf("associationEnd"),
            derivationConstraint = "deriveFlowConnectionDefinitionFlowEnd"
        )
    )

    // Usage has nestedFlow : FlowConnectionUsage [0..*] {ordered, derived, subsets nestedConnection}
    val flowOwningUsageNestedFlowAssociation = MetaAssociation(
        name = "flowOwningUsageNestedFlowAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "flowOwningUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("connectionOwningUsage"),
            derivationConstraint = "deriveFlowConnectionUsageFlowOwningUsage"
        ),
        targetEnd = MetaAssociationEnd(
            name = "nestedFlow",
            type = "FlowConnectionUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("nestedConnection"),
            derivationConstraint = "deriveUsageNestedFlow"
        )
    )

    // FlowConnectionUsage has flowDefinition : Interaction [0..*] {ordered, derived, subsets actionDefinition, interaction}
    val definedFlowFlowDefinitionAssociation = MetaAssociation(
        name = "definedFlowFlowDefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "definedFlow",
            type = "FlowConnectionUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            redefines = listOf("ownedAction"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "flowDefinition",
            type = "Interaction",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("actionDefinition", "interaction"),
            derivationConstraint = "deriveFlowConnectionUsageFlowDefinition"
        )
    )

    return listOf(
        definedFlowFlowDefinitionAssociation,
        flowDefinitionWithEndFlowEndAssociation,
        flowOwningDefinitionOwnedFlowAssociation,
        flowOwningUsageNestedFlowAssociation
    )
}
