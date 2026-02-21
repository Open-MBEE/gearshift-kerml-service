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
 * Figure 32: Exhibited States
 */
fun createExhibitedStatesAssociations(): List<MetaAssociation> {

    // ExhibitStateUsage has exhibitedState : StateUsage [1..1] {derived, redefines performedAction}
    val exhibitingStateExhibitedStateAssociation = MetaAssociation(
        name = "exhibitingStateExhibitedStateAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "exhibitingState",
            type = "ExhibitStateUsage",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            isDerived = true,
            subsets = listOf("performingAction"),
            derivationConstraint = MetaAssociationEnd.OPPOSITE_END
        ),
        targetEnd = MetaAssociationEnd(
            name = "exhibitedState",
            type = "StateUsage",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            redefines = listOf("performedAction"),
            derivationConstraint = "deriveExhibitStateUsageExhibitedState"
        )
    )

    return listOf(
        exhibitingStateExhibitedStateAssociation
    )
}
