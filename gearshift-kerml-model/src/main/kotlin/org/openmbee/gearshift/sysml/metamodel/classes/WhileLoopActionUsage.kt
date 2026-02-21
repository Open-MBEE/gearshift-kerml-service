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
 * SysML WhileLoopActionUsage metaclass.
 * Specializes: LoopActionUsage
 * A WhileLoopActionUsage is a LoopActionUsage that specifies that the bodyAction ActionUsage should be
 * performed repeatedly while the result of the whileArgument Expression is true or until the result of the
 * untilArgument Expression (if provided) is true.
 */
fun createWhileLoopActionUsageMetaClass() = MetaClass(
    name = "WhileLoopActionUsage",
    isAbstract = false,
    superclasses = listOf("LoopActionUsage"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkWhileLoopActionUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Actions::whileLoopActions')",
            description = "A WhileLoopActionUsage must directly or indirectly specialize the ActionUsage Actions::whileLoopActions from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkWhileLoopActionUsageSubactionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isSubactionUsage() implies
                specializesFromLibrary('Actions::Action::whileLoops')
            """.trimIndent(),
            description = "A composite WhileLoopActionUsage that is a subaction usage must directly or indirectly specialize the ActionUsage Actions::Action::whileLoops from the Systems Model Library."
        ),
        MetaConstraint(
            name = "deriveWhileLoopActionUsageUntilArgument",
            type = ConstraintType.DERIVATION,
            expression = """
                let parameter : Feature = inputParameter(3) in
                if parameter <> null and parameter.oclIsKindOf(Expression) then
                    parameter.oclAsType(Expression)
                else
                    null
                endif
            """.trimIndent(),
            description = "The untilArgument of a WhileLoopActionUsage is its third input parameter, which, if it exists, must be an Expression."
        ),
        MetaConstraint(
            name = "deriveWhileLoopActionUsageWhileArgument",
            type = ConstraintType.DERIVATION,
            expression = """
                let parameter : Feature = inputParameter(1) in
                if parameter <> null and parameter.oclIsKindOf(Expression) then
                    parameter.oclAsType(Expression)
                else
                    null
                endif
            """.trimIndent(),
            description = "The whileArgument of a WhileLoopActionUsage is its first input parameter, which must be an Expression."
        ),
        MetaConstraint(
            name = "validateWhileLoopActionUsage",
            type = ConstraintType.VERIFICATION,
            expression = "inputParameters()->size() >= 2",
            description = "A WhileLoopActionUsage must have at least two owned input parameters."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "whileLoopActionUsageWhileLoopActionsBinding",
            baseConcept = "Actions::whileLoopActions",
            bindingKind = BindingKind.SUBSETS
        ),
        SemanticBinding(
            name = "whileLoopActionUsageWhileLoopsBinding",
            baseConcept = "Actions::Action::whileLoops",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.OperationResult("isSubactionUsage")
        )
    ),
    description = "A WhileLoopActionUsage is a LoopActionUsage that specifies that the bodyAction should be performed repeatedly while the whileArgument is true."
)
