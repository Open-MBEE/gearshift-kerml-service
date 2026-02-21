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
 * SysML ForkNode metaclass.
 * Specializes: ControlNode
 * A ForkNode is a ControlNode that must be followed by successor Actions as given by all its outgoing
 * Successions.
 */
fun createForkNodeMetaClass() = MetaClass(
    name = "ForkNode",
    isAbstract = false,
    superclasses = listOf("ControlNode"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkForkNodeSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Actions::Action::forks')",
            description = "A ForkNode must directly or indirectly specialize the ActionUsage Actions::Action::forks from the Systems Model Library."
        ),
        MetaConstraint(
            name = "validateForkNodeIncomingSuccessions",
            type = ConstraintType.VERIFICATION,
            expression = "targetConnector->selectByKind(Succession)->size() <= 1",
            description = "A ForkNode may have at most one incoming Succession."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "forkNodeForksBinding",
            baseConcept = "Actions::Action::forks",
            bindingKind = BindingKind.SUBSETS
        )
    ),
    description = "A ForkNode is a ControlNode that must be followed by successor Actions as given by all its outgoing Successions."
)
