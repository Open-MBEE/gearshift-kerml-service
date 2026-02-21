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
import org.openmbee.mdm.framework.meta.MetaOperation
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * SysML PerformActionUsage metaclass.
 * Specializes: ActionUsage, EventOccurrenceUsage
 * A PerformActionUsage is an ActionUsage that represents the performance of an ActionUsage. Unless it is the
 * PerformActionUsage itself, the ActionUsage to be performed is related to the PerformActionUsage by a
 * ReferenceSubsetting relationship. A PerformActionUsage is also an EventOccurrenceUsage, with its
 * performedAction as the eventOccurrence.
 */
fun createPerformActionUsageMetaClass() = MetaClass(
    name = "PerformActionUsage",
    isAbstract = false,
    superclasses = listOf("ActionUsage", "EventOccurrenceUsage"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkPerformActionUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningType <> null and
                (owningType.oclIsKindOf(PartDefinition) or
                owningType.oclIsKindOf(PartUsage)) implies
                specializesFromLibrary('Parts::Part::performedActions')
            """.trimIndent(),
            description = "If a PerformActionUsage has an owningType that is a PartDefinition or PartUsage, then it must directly or indirectly specialize the ActionUsage Parts::Part::performedActions."
        ),
        MetaConstraint(
            name = "validatePerformActionUsageReference",
            type = ConstraintType.VERIFICATION,
            expression = """
                referencedFeatureTarget() <> null implies
                referencedFeatureTarget().oclIsKindOf(ActionUsage)
            """.trimIndent(),
            description = "If a PerformActionUsage has an ownedReferenceSubsetting, then the featureTarget of the referencedFeature must be an ActionUsage."
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "namingFeature",
            returnType = "Feature",
            returnLowerBound = 0,
            returnUpperBound = 1,
            redefines = "namingFeature",
            body = MetaOperation.ocl("""
                if performedAction <> self then performedAction
                else self.oclAsType(Usage).namingFeature()
                endif
            """.trimIndent()),
            description = "The namingFeature of a PerformActionUsage is its performedAction, if this is different than the PerformActionUsage."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "performActionUsagePerformedActionsBinding",
            baseConcept = "Parts::Part::performedActions",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.Or(
                listOf(
                    BindingCondition.OwningTypeIs("PartDefinition"),
                    BindingCondition.OwningTypeIs("PartUsage")
                )
            )
        )
    ),
    description = "A PerformActionUsage is an ActionUsage that represents the performance of an ActionUsage."
)
