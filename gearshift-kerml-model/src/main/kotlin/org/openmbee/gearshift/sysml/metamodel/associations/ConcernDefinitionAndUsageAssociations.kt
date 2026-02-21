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
 * Figure 39: Concern Definition and Usage
 */
fun createConcernDefinitionAndUsageAssociations(): List<MetaAssociation> {

    // Definition has ownedConcern : ConcernUsage [0..*] {derived, subsets ownedRequirement}
    val concernOwningDefinitionOwnedConcernAssociation = MetaAssociation(
        name = "concernOwningDefinitionOwnedConcernAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "concernOwningDefinition",
            type = "Definition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("requirementOwningDefinition"),
            derivationConstraint = "deriveConcernUsageConcernOwningDefinition"
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedConcern",
            type = "ConcernUsage",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            subsets = listOf("ownedRequirement"),
            derivationConstraint = "deriveDefinitionOwnedConcern"
        )
    )

    // Usage has nestedConcern : ConcernUsage [0..*] {derived, subsets nestedRequirement}
    val concernOwningUsageNestedConcernAssociation = MetaAssociation(
        name = "concernOwningUsageNestedConcernAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "concernOwningUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            redefines = listOf("requirementOwningUsage"),
            derivationConstraint = "deriveConcernUsageConcernOwningUsage"
        ),
        targetEnd = MetaAssociationEnd(
            name = "nestedConcern",
            type = "ConcernUsage",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            subsets = listOf("nestedRequirement"),
            derivationConstraint = "deriveUsageNestedConcern"
        )
    )

    // ConcernUsage has concernDefinition : ConcernDefinition [0..1] {derived, redefines requirementDefinition}
    val definedConcernConcernDefinition = MetaAssociation(
        name = "definedConcernConcernDefinition",
        sourceEnd = MetaAssociationEnd(
            name = "definedConcern",
            type = "ConcernUsage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("definedRequirement"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "concernDefinition",
            type = "ConcernDefinition",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            redefines = listOf("requirementDefinition"),
            derivationConstraint = "deriveConcernUsageConcernDefinition"
        )
    )

    return listOf(
        concernOwningDefinitionOwnedConcernAssociation,
        concernOwningUsageNestedConcernAssociation,
        definedConcernConcernDefinition
    )
}
