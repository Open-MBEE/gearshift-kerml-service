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
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * SysML DecisionNode metaclass.
 * Specializes: ControlNode
 * A DecisionNode is a ControlNode that makes a selection from its outgoing Successions.
 */
fun createDecisionNodeMetaClass() = MetaClass(
    name = "DecisionNode",
    isAbstract = false,
    superclasses = listOf("ControlNode"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkDecisionNodeOutgoingSuccessionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                sourceConnector->selectByKind(Succession)->
                    forAll(subsetsChain(self,
                        resolveGlobal('ControlPerformances::MergePerformance::outgoingHBLink')))
            """.trimIndent(),
            description = "All outgoing Successions from a DecisionNode must subset the inherited outgoingHBLink feature of the DecisionNode."
        ),
        MetaConstraint(
            name = "checkDecisionNodeSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Actions::Action::decisions')",
            description = "A DecisionNode must directly or indirectly specialize the ActionUsage Actions::Action::decisions from the Systems Model Library."
        ),
        MetaConstraint(
            name = "validateDecisionNodeIncomingSuccessions",
            type = ConstraintType.VERIFICATION,
            expression = "targetConnector->selectByKind(Succession)->size() <= 1",
            description = "A DecisionNode may have at most one incoming Succession."
        ),
        MetaConstraint(
            name = "validateDecisionNodeOutgoingSuccessions",
            type = ConstraintType.VERIFICATION,
            expression = """
                sourceConnector->selectByKind(Succession)->
                    collect(connectorEnd->at(2))->
                    forAll(targetMult |
                        multiplicityHasBounds(targetMult, 0, 1))
            """.trimIndent(),
            description = "All outgoing Successions from a DecisionNode must have a target multiplicity of 0..1."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "decisionNodeDecisionsBinding",
            baseConcept = "Actions::Action::decisions",
            bindingKind = BindingKind.SUBSETS
        )
    ),
    description = "A DecisionNode is a ControlNode that makes a selection from its outgoing Successions."
)
