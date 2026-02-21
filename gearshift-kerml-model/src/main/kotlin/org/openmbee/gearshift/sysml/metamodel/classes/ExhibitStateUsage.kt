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
package org.openmbee.gearshift.sysml.metamodel.classes

import org.openmbee.mdm.framework.meta.BindingCondition
import org.openmbee.mdm.framework.meta.BindingKind
import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * SysML ExhibitStateUsage metaclass.
 * Specializes: PerformActionUsage, StateUsage
 * An ExhibitStateUsage is a StateUsage that represents the exhibiting of a StateUsage. Unless it is the
 * StateUsage itself, the StateUsage to be exhibited is related to the ExhibitStateUsage by a
 * ReferenceSubsetting Relationship. An ExhibitStateUsage is also a PerformActionUsage, with its
 * exhibitedState as the performedAction.
 */
fun createExhibitStateUsageMetaClass() = MetaClass(
    name = "ExhibitStateUsage",
    isAbstract = false,
    superclasses = listOf("PerformActionUsage", "StateUsage"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkExhibitStateUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningType <> null and
                (owningType.oclIsKindOf(PartDefinition) or
                owningType.oclIsKindOf(PartUsage)) implies
                specializesFromLibrary('Parts::Part::exhibitedStates')
            """.trimIndent(),
            description = "If an ExhibitStateUsage has an owningType that is a PartDefinition or PartUsage, then it must directly or indirectly specialize the StateUsage Parts::Part::exhibitedStates."
        ),
        MetaConstraint(
            name = "validateExhibitStateUsageReference",
            type = ConstraintType.VERIFICATION,
            expression = """
                referencedFeatureTarget() <> null implies
                referencedFeatureTarget().oclIsKindOf(StateUsage)
            """.trimIndent(),
            description = "If an ExhibitStateUsage has an ownedReferenceSubsetting, then the featureTarget of the referencedFeature must be a StateUsage."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "exhibitStateUsageExhibitedStatesBinding",
            baseConcept = "Parts::Part::exhibitedStates",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.Or(
                listOf(
                    BindingCondition.OwningTypeIs("PartDefinition"),
                    BindingCondition.OwningTypeIs("PartUsage")
                )
            )
        )
    ),
    description = "An ExhibitStateUsage is a StateUsage that represents the exhibiting of a StateUsage."
)
