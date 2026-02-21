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
 * SysML MergeNode metaclass.
 * Specializes: ControlNode
 * A MergeNode is a ControlNode that asserts the merging of its incoming Successions. A MergeNode may have
 * at most one outgoing Succession.
 */
fun createMergeNodeMetaClass() = MetaClass(
    name = "MergeNode",
    isAbstract = false,
    superclasses = listOf("ControlNode"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkMergeNodeIncomingSuccessionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                targetConnector->selectByKind(Succession)->
                    forAll(subsetsChain(self,
                        resolveGlobal('ControlPerformances::MergePerformance::incomingHBLink')))
            """.trimIndent(),
            description = "All incoming Successions to a MergeNode must subset the inherited incomingHBLink feature of the MergeNode."
        ),
        MetaConstraint(
            name = "checkMergeNodeSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Actions::Action::merges')",
            description = "A MergeNode must directly or indirectly specialize the ActionUsage Actions::Action::merges from the Systems Model Library."
        ),
        MetaConstraint(
            name = "validateMergeNodeIncomingSuccessions",
            type = ConstraintType.VERIFICATION,
            expression = """
                targetConnector->selectByKind(Succession)->
                    collect(connectorEnd->at(1))->
                    forAll(sourceMult |
                        multiplicityHasBounds(sourceMult, 0, 1))
            """.trimIndent(),
            description = "All incoming Successions to a MergeNode must have a source multiplicity of 0..1."
        ),
        MetaConstraint(
            name = "validateMergeNodeOutgoingSuccessions",
            type = ConstraintType.VERIFICATION,
            expression = "sourceConnector->selectByKind(Succession)->size() <= 1",
            description = "A MergeNode may have at most one outgoing Succession."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "mergeNodeMergesBinding",
            baseConcept = "Actions::Action::merges",
            bindingKind = BindingKind.SUBSETS
        )
    ),
    description = "A MergeNode is a ControlNode that asserts the merging of its incoming Successions."
)
