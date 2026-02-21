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
 * SysML IfActionUsage metaclass.
 * Specializes: ActionUsage
 * An IfActionUsage is an ActionUsage that specifies that the thenAction ActionUsage should be performed if
 * the result of the ifArgument Expression is true. It may also optionally specify an elseAction ActionUsage
 * that is performed if the result of the ifArgument is false.
 */
fun createIfActionUsageMetaClass() = MetaClass(
    name = "IfActionUsage",
    isAbstract = false,
    superclasses = listOf("ActionUsage"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkIfActionUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                if elseAction = null then
                    specializesFromLibrary('Actions::ifThenActions')
                else
                    specializesFromLibrary('Actions::ifThenElseActions')
                endif
            """.trimIndent(),
            description = "An IfActionUsage must directly or indirectly specialize the ActionUsage Actions::ifThenActions from the Systems Model Library. If it has an elseAction, then it must directly or indirectly specialize Actions::ifThenElseActions."
        ),
        MetaConstraint(
            name = "checkIfActionUsageSubactionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isSubactionUsage() implies
                specializesFromLibrary('Actions::Action::ifSubactions')
            """.trimIndent(),
            description = "A composite IfActionUsage that is a subaction usage must directly or indirectly specialize the ActionUsage Actions::Action::ifSubactions from the Systems Model Library."
        ),
        MetaConstraint(
            name = "deriveIfActionUsageElseAction",
            type = ConstraintType.DERIVATION,
            expression = """
                let parameter : Feature = inputParameter(3) in
                if parameter <> null and parameter.oclIsKindOf(ActionUsage) then
                    parameter.oclAsType(ActionUsage)
                else
                    null
                endif
            """.trimIndent(),
            description = "The elseAction of an IfActionUsage is its third parameter, if there is one, which must then be an ActionUsage."
        ),
        MetaConstraint(
            name = "deriveIfActionUsageIfArgument",
            type = ConstraintType.DERIVATION,
            expression = """
                let parameter : Feature = inputParameter(1) in
                if parameter <> null and parameter.oclIsKindOf(Expression) then
                    parameter.oclAsType(Expression)
                else
                    null
                endif
            """.trimIndent(),
            description = "The ifArgument of an IfActionUsage is its first parameter, which must be an Expression."
        ),
        MetaConstraint(
            name = "deriveIfActionUsageThenAction",
            type = ConstraintType.DERIVATION,
            expression = """
                let parameter : Feature = inputParameter(2) in
                if parameter <> null and parameter.oclIsKindOf(ActionUsage) then
                    parameter.oclAsType(ActionUsage)
                else
                    null
                endif
            """.trimIndent(),
            description = "The thenAction of an IfActionUsage is its second parameter, which must be an ActionUsage."
        ),
        MetaConstraint(
            name = "validateIfActionUsageParameters",
            type = ConstraintType.VERIFICATION,
            expression = "inputParameters()->size() >= 2",
            description = "An IfActionUsage must have at least two owned input parameters."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "ifActionUsageIfThenActionsBinding",
            baseConcept = "Actions::ifThenActions",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.PropertyEquals("elseAction", "null")
        ),
        SemanticBinding(
            name = "ifActionUsageIfThenElseActionsBinding",
            baseConcept = "Actions::ifThenElseActions",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.PropertyNotEmpty("elseAction")
        ),
        SemanticBinding(
            name = "ifActionUsageIfSubactionsBinding",
            baseConcept = "Actions::Action::ifSubactions",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.OperationResult("isSubactionUsage")
        )
    ),
    description = "An IfActionUsage is an ActionUsage that specifies that the thenAction should be performed if the result of the ifArgument Expression is true."
)
