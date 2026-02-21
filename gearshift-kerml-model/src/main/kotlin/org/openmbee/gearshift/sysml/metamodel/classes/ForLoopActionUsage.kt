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
 * SysML ForLoopActionUsage metaclass.
 * Specializes: LoopActionUsage
 * A ForLoopActionUsage is a LoopActionUsage that specifies that its bodyAction ActionUsage should be
 * performed once for each value, in order, from the sequence of values obtained as the result of the seqArgument
 * Expression, with the loopVariable set to the value for each iteration.
 */
fun createForLoopActionUsageMetaClass() = MetaClass(
    name = "ForLoopActionUsage",
    isAbstract = false,
    superclasses = listOf("LoopActionUsage"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkForLoopActionUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Actions::forLoopActions')",
            description = "A ForLoopActionUsage must directly or indirectly specialize the ActionUsage Actions::forLoopActions from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkForLoopActionUsageSubactionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isSubactionUsage() implies
                specializesFromLibrary('Actions::Action::forLoops')
            """.trimIndent(),
            description = "A composite ForLoopActionUsage that is a subaction usage must directly or indirectly specialize the ActionUsage Actions::Action::forLoops from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkForLoopActionUsageVarRedefinition",
            type = ConstraintType.VERIFICATION,
            expression = """
                loopVariable <> null and
                loopVariable.redefinesFromLibrary('Actions::ForLoopAction::var')
            """.trimIndent(),
            description = "The loopVariable of a ForLoopActionUsage must redefine the ActionUsage Actions::ForLoopAction::var."
        ),
        MetaConstraint(
            name = "deriveForLoopActionUsageLoopVariable",
            type = ConstraintType.DERIVATION,
            expression = """
                if ownedFeature->isEmpty() or
                    not ownedFeature->first().oclIsKindOf(ReferenceUsage) then
                    null
                else
                    ownedFeature->first().oclAsType(ReferenceUsage)
                endif
            """.trimIndent(),
            description = "The loopVariable of a ForLoopActionUsage is its first ownedFeature, which must be a ReferenceUsage."
        ),
        MetaConstraint(
            name = "deriveForLoopActionUsageSeqArgument",
            type = ConstraintType.DERIVATION,
            expression = "argument(1)",
            description = "The seqArgument of a ForLoopActionUsage is its first argument Expression."
        ),
        MetaConstraint(
            name = "validateForLoopActionUsageLoopVariable",
            type = ConstraintType.VERIFICATION,
            expression = """
                ownedFeature->notEmpty() and
                ownedFeature->at(1).oclIsKindOf(ReferenceUsage)
            """.trimIndent(),
            description = "The first ownedFeature of a ForLoopActionUsage must be a ReferenceUsage."
        ),
        MetaConstraint(
            name = "validateForLoopActionUsageParameters",
            type = ConstraintType.VERIFICATION,
            expression = "inputParameters()->size() = 2",
            description = "A ForLoopActionUsage must have two owned input parameters."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "forLoopActionUsageForLoopActionsBinding",
            baseConcept = "Actions::forLoopActions",
            bindingKind = BindingKind.SUBSETS
        ),
        SemanticBinding(
            name = "forLoopActionUsageForLoopsBinding",
            baseConcept = "Actions::Action::forLoops",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.OperationResult("isSubactionUsage")
        )
    ),
    description = "A ForLoopActionUsage is a LoopActionUsage that specifies that its bodyAction should be performed once for each value from the seqArgument sequence."
)
