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
 * SysML FlowDefinition metaclass.
 * Specializes: ActionDefinition, Interaction
 * A FlowDefinition is an ActionDefinition that is also an Interaction (which is both a KerML Behavior
 * and Association), representing flows between Usages.
 */
fun createFlowDefinitionMetaClass() = MetaClass(
    name = "FlowDefinition",
    isAbstract = false,
    superclasses = listOf("ActionDefinition", "Interaction"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkFlowDefinitionBinarySpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                flowEnd->size() = 2 implies
                specializesFromLibrary('Flows::Message')
            """.trimIndent(),
            description = "A binary FlowDefinition must directly or indirectly specialize the base FlowDefinition Flows::Message from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkFlowDefinitionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Flows::MessageAction')",
            description = "A FlowDefinition must directly or indirectly specialize the base FlowDefinition Flows::MessageAction from the Systems Model Library."
        ),
        MetaConstraint(
            name = "validateFlowDefinitionFlowEnds",
            type = ConstraintType.VERIFICATION,
            expression = "flowEnd->size() <= 2",
            description = "A FlowDefinition may not have more than two flowEnds."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "flowDefinitionMessageActionBinding",
            baseConcept = "Flows::MessageAction",
            bindingKind = BindingKind.SPECIALIZES
        ),
        SemanticBinding(
            name = "flowDefinitionMessageBinding",
            baseConcept = "Flows::Message",
            bindingKind = BindingKind.SPECIALIZES,
            condition = BindingCondition.PropertyEquals("flowEnd->size()", "2")
        )
    ),
    description = "A FlowDefinition is an ActionDefinition that is also an Interaction, representing flows between Usages."
)
