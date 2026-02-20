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
 * Figure 8: Definition and Usage
 */
fun createDefinitionAndUsageAssociations(): List<MetaAssociation> {

    // Usage has definition : Classifier [0..*] {ordered, derived, redefines type}
    val definedUsageDefinitionAssociation = MetaAssociation(
        name = "definedUsageDefinitionAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "definedUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("typedFeature"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "definition",
            type = "Classifier",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            redefines = listOf("type"),
            derivationConstraint = "deriveUsageDefinition"
        )
    )

    // Definition has ownedUsage : Usage [0..*] {ordered, derived, subsets ownedFeature, usage}
    // Usage has owningDefinition : Definition [0..1] {derived, subsets featuringDefinition, owningType}
    val owningDefinitionOwnedUsageAssociation = MetaAssociation(
        name = "owningDefinitionOwnedUsageAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningDefinition",
            type = "Definition",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("featuringDefinition", "owningType"),
            derivationConstraint = "deriveUsageOwningDefinition"
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("ownedFeature", "usage"),
            derivationConstraint = "deriveDefinitionOwnedUsage"
        )
    )

    // Definition has usage : Usage [0..*] {ordered, derived, subsets feature}
    val featuringDefinitionUsageAssociation = MetaAssociation(
        name = "featuringDefinitionUsageAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "featuringDefinition",
            type = "Definition",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("typeWithFeature"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "usage",
            type = "Usage",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("feature"),
            derivationConstraint = "deriveDefinitionUsage"
        )
    )

    // Definition has directedUsage : Usage [0..*] {ordered, derived, subsets directedFeature, usage}
    val definitionWithDirectedUsageDirectedUsageAssociation = MetaAssociation(
        name = "definitionWithDirectedUsageDirectedUsageAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "definitionWithDirectedUsage",
            type = "Definition",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("featuringDefinition", "typeWithDirectedFeature"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "directedUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("directedFeature", "usage"),
            derivationConstraint = "deriveDefinitionDirectedUsage"
        )
    )

    // Definition has ownedReference : ReferenceUsage [0..*] {ordered, derived, subsets ownedUsage}
    val referenceOwningDefinitionOwnedReferenceAssociation = MetaAssociation(
        name = "referenceOwningDefinitionOwnedReferenceAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "referenceOwningDefinition",
            type = "Definition",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningDefinition"),
            derivationConstraint = "deriveReferenceUsageReferenceOwningDefinition"
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedReference",
            type = "ReferenceUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("ownedUsage"),
            derivationConstraint = "deriveDefinitionOwnedReference"
        )
    )

    // Usage has nestedReference : ReferenceUsage [0..*] {ordered, derived, subsets nestedUsage}
    val referenceOwningUsageNestedUsageAssociation = MetaAssociation(
        name = "referenceOwningUsageNestedUsageAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "referenceOwningUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = 1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("owningUsage"),
            derivationConstraint = "deriveReferenceUsageReferenceOwningUsage"
        ),
        targetEnd = MetaAssociationEnd(
            name = "nestedReference",
            type = "ReferenceUsage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("nestedUsage"),
            derivationConstraint = "deriveUsageNestedReference"
        )
    )

    // Usage has nestedUsage : Usage [0..*] {ordered, derived, subsets ownedFeature, usage}
    // Usage has owningUsage : Usage [0..1] {derived, subsets owningType}
    val owningUsageNestedUsageAssociation = MetaAssociation(
        name = "owningUsageNestedUsageAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("owningType"),
            derivationConstraint = "deriveUsageOwningUsage"
        ),
        targetEnd = MetaAssociationEnd(
            name = "nestedUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("ownedFeature", "usage"),
            derivationConstraint = "deriveUsageNestedUsage"
        )
    )

    // Usage has usage : Usage [0..*] {ordered, derived, subsets feature}
    val featuringUsageUsageAssociation = MetaAssociation(
        name = "featuringUsageUsageAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "featuringUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("typeWithFeature"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "usage",
            type = "Usage",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("feature"),
            derivationConstraint = "deriveUsageUsage"
        )
    )

    // Usage has directedUsage : Usage [0..*] {ordered, derived, subsets directedFeature, usage}
    val usageWithDirectedUsageDirectedUsageAssociation = MetaAssociation(
        name = "usageWithDirectedUsageDirectedUsageAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "usageWithDirectedUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("typeWithDirectedFeature"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "directedUsage",
            type = "Usage",
            lowerBound = 0,
            upperBound = -1,
            isOrdered = true,
            isDerived = true,
            subsets = listOf("directedFeature", "usage"),
            derivationConstraint = "deriveUsageDirectedUsage"
        )
    )

    return listOf(
        definedUsageDefinitionAssociation,
        definitionWithDirectedUsageDirectedUsageAssociation,
        featuringDefinitionUsageAssociation,
        featuringUsageUsageAssociation,
        owningDefinitionOwnedUsageAssociation,
        owningUsageNestedUsageAssociation,
        referenceOwningDefinitionOwnedReferenceAssociation,
        referenceOwningUsageNestedUsageAssociation,
        usageWithDirectedUsageDirectedUsageAssociation
    )
}
