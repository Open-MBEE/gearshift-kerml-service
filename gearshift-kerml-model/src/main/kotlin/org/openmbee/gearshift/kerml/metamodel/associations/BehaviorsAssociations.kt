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
package org.openmbee.gearshift.kerml.metamodel.associations

import org.openmbee.mdm.framework.meta.MetaAssociation
import org.openmbee.mdm.framework.meta.MetaAssociationEnd

/**
 * Figure 28: Behaviors
 * Defines associations for Behaviors.
 */
fun createBehaviorAssociations(): List<MetaAssociation> {

    // Behavior has step : Step [0..*] {derived, subsets feature}
    val featuringBehaviorStepAssociation = MetaAssociation(
        name = "featuringBehaviorStepAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "featuringBehavior",
            type = "Behavior",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("typeWithFeature"),
            derivationConstraint = "deriveStepFeaturingBehavior"
        ),
        targetEnd = MetaAssociationEnd(
            name = "step",
            type = "Step",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            subsets = listOf("feature"),
            derivationConstraint = "deriveBehaviorStep"
        )
    )

    // Behavior has parameter : Feature [0..*] {ordered, derived, redefines directedFeature}
    val parameteredBehaviorParameterAssociation = MetaAssociation(
        name = "parameteredBehaviorParameterAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "parameteredBehavior",
            type = "Behavior",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("typeWithFeature"),
            derivationConstraint = "deriveFeatureParameteredBehavior"
        ),
        targetEnd = MetaAssociationEnd(
            name = "parameter",
            type = "Feature",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            redefines = listOf("directedFeature"),
            derivationConstraint = "deriveBehaviorParameter"
        )
    )

    // Step has parameter : Feature [0..*] {ordered, derived, redefines directedFeature}
    val parameteredStepParameterAssociation = MetaAssociation(
        name = "parameteredStepParameterAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "parameteredStep",
            type = "Step",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("typeWithFeature"),
            derivationConstraint = "deriveFeatureParameteredStep"
        ),
        targetEnd = MetaAssociationEnd(
            name = "parameter",
            type = "Feature",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            redefines = listOf("directedFeature"),
            derivationConstraint = "deriveStepParameter"
        )
    )

    // Step has behavior : Behavior [0..*] {ordered, derived, redefines type}
    val typedStepBehaviorAssociation = MetaAssociation(
        name = "typedStepBehaviorAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "typedStep",
            type = "Step",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("typedFeature"),
            derivationConstraint = "deriveBehaviorTypedStep"
        ),
        targetEnd = MetaAssociationEnd(
            name = "behavior",
            type = "Behavior",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("type"),
            derivationConstraint = "deriveStepBehavior"
        )
    )

    return listOf(
        featuringBehaviorStepAssociation,
        parameteredBehaviorParameterAssociation,
        parameteredStepParameterAssociation,
        typedStepBehaviorAssociation,
    )
}
