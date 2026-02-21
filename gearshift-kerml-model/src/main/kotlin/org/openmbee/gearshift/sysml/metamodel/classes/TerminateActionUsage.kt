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
 * SysML TerminateActionUsage metaclass.
 * Specializes: ActionUsage
 * A TerminateActionUsage is an ActionUsage that directly or indirectly specializes the ActionDefinition
 * TerminateAction from the Systems Model Library, which causes a given terminatedOccurrence to end
 * during its performance.
 */
fun createTerminateActionUsageMetaClass() = MetaClass(
    name = "TerminateActionUsage",
    isAbstract = false,
    superclasses = listOf("ActionUsage"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkTerminateActionUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Actions::terminateActions')",
            description = "A TerminateActionUsage must directly or indirectly specialize the ActionUsage Actions::terminateActions from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkTerminateActionUsageSubactionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isSubactionUsage() implies
                specializesFromLibrary('Actions::Action::terminateSubactions')
            """.trimIndent(),
            description = "A composite TerminateActionUsage that is a subaction must directly or indirectly specialize the ActionUsage Actions::Action::terminateSubactions from the Systems Model Library."
        ),
        MetaConstraint(
            name = "deriveTerminateActionUsageTerminatedOccurrenceArgument",
            type = ConstraintType.DERIVATION,
            expression = "argument(1)",
            description = "The terminatedOccurrenceArgument of a TerminateActionUsage is its first argument."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "terminateActionUsageTerminateActionsBinding",
            baseConcept = "Actions::terminateActions",
            bindingKind = BindingKind.SUBSETS
        ),
        SemanticBinding(
            name = "terminateActionUsageTerminateSubactionsBinding",
            baseConcept = "Actions::Action::terminateSubactions",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.OperationResult("isSubactionUsage")
        )
    ),
    description = "A TerminateActionUsage is an ActionUsage that causes a given terminatedOccurrence to end during its performance."
)
