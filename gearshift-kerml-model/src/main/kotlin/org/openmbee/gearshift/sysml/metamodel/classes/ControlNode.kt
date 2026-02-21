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

import org.openmbee.mdm.framework.meta.BindingKind
import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.MetaOperation
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * SysML ControlNode metaclass.
 * Specializes: ActionUsage
 * A ControlNode is an ActionUsage that does not have any inherent behavior but provides constraints on incoming
 * and outgoing Successions that are used to control other Actions. A ControlNode must be a composite owned
 * usage of an ActionDefinition or ActionUsage.
 */
fun createControlNodeMetaClass() = MetaClass(
    name = "ControlNode",
    isAbstract = true,
    superclasses = listOf("ActionUsage"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkControlNodeSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Actions::Action::controls')",
            description = "A ControlNode must directly or indirectly specialize the ActionUsage Actions::Action::controls from the Systems Model Library."
        ),
        MetaConstraint(
            name = "validateControlNodeIncomingSuccessions",
            type = ConstraintType.VERIFICATION,
            expression = """
                targetConnector->selectByKind(Succession)->
                    collect(connectorEnd->at(2).multiplicity)->
                    forAll(targetMult |
                        multiplicityHasBounds(targetMult, 1, 1))
            """.trimIndent(),
            description = "All incoming Successions to a ControlNode must have a target multiplicity of 1..1."
        ),
        MetaConstraint(
            name = "validateControlNodeIsComposite",
            type = ConstraintType.VERIFICATION,
            expression = "isComposite",
            description = "A ControlNode must be composite."
        ),
        MetaConstraint(
            name = "validateControlNodeOutgoingSuccessions",
            type = ConstraintType.VERIFICATION,
            expression = """
                sourceConnector->selectByKind(Succession)->
                    collect(connectorEnd->at(1).multiplicity)->
                    forAll(sourceMult |
                        multiplicityHasBounds(sourceMult, 1, 1))
            """.trimIndent(),
            description = "All outgoing Successions from a ControlNode must have a source multiplicity of 1..1."
        ),
        MetaConstraint(
            name = "validateControlNodeOwningType",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningType <> null and
                (owningType.oclIsKindOf(ActionDefinition) or
                owningType.oclIsKindOf(ActionUsage))
            """.trimIndent(),
            description = "The owningType of a ControlNode must be an ActionDefinition or ActionUsage."
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "multiplicityHasBounds",
            returnType = "Boolean",
            returnLowerBound = 1,
            returnUpperBound = 1,
            parameters = listOf(
                MetaOperation.Parameter("mult", "Multiplicity"),
                MetaOperation.Parameter("lower", "Integer"),
                MetaOperation.Parameter("upper", "UnlimitedNatural")
            ),
            body = MetaOperation.ocl("""
                mult <> null and
                if mult.oclIsKindOf(MultiplicityRange) then
                    mult.oclAsType(MultiplicityRange).hasBounds(lower, upper)
                else
                    mult.allSuperTypes()->exists(
                        oclIsKindOf(MultiplicityRange) and
                        oclAsType(MultiplicityRange).hasBounds(lower, upper))
                endif
            """.trimIndent()),
            description = "Check that the given Multiplicity has lowerBound and upperBound expressions that are model-level evaluable to the given lower and upper values."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "controlNodeControlsBinding",
            baseConcept = "Actions::Action::controls",
            bindingKind = BindingKind.SUBSETS
        )
    ),
    description = "A ControlNode is an ActionUsage that provides constraints on incoming and outgoing Successions used to control other Actions."
)
