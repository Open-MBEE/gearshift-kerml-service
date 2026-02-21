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
 * SysML FlowUsage metaclass.
 * Specializes: Flow, ActionUsage, ConnectorAsUsage
 * A FlowUsage is an ActionUsage that is also a ConnectorAsUsage and a KerML Flow.
 */
fun createFlowUsageMetaClass() = MetaClass(
    name = "FlowUsage",
    isAbstract = false,
    superclasses = listOf("Flow", "ActionUsage", "ConnectorAsUsage"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkFlowUsageFlowSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                ownedEndFeature->notEmpty() implies
                specializesFromLibrary('Flows::flows')
            """.trimIndent(),
            description = "If a FlowUsage has ownedEndFeatures, it must directly or indirectly specialize the FlowUsage Flows::flows from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkFlowUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Flows::messages')",
            description = "A FlowUsage must directly or indirectly specialize the base FlowUsage Flows::messages from the Systems Model Library."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "flowUsageMessagesBinding",
            baseConcept = "Flows::messages",
            bindingKind = BindingKind.SUBSETS
        ),
        SemanticBinding(
            name = "flowUsageFlowsBinding",
            baseConcept = "Flows::flows",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.PropertyNotEmpty("ownedEndFeature")
        )
    ),
    description = "A FlowUsage is an ActionUsage that is also a ConnectorAsUsage and a KerML Flow."
)
