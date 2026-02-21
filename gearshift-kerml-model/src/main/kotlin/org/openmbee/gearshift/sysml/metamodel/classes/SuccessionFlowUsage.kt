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
 * SysML SuccessionFlowUsage metaclass.
 * Specializes: FlowUsage, SuccessionFlow
 * A SuccessionFlowUsage is a FlowUsage that is also a KerML SuccessionFlow.
 */
fun createSuccessionFlowUsageMetaClass() = MetaClass(
    name = "SuccessionFlowUsage",
    isAbstract = false,
    superclasses = listOf("FlowUsage", "SuccessionFlow"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkSuccessionFlowUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Flows::successionFlows')",
            description = "A SuccessionFlowUsage must directly or indirectly specialize the base FlowUsage Flows::successionFlows from the Systems Model Library."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "successionFlowUsageSuccessionFlowsBinding",
            baseConcept = "Flows::successionFlows",
            bindingKind = BindingKind.SUBSETS
        )
    ),
    description = "A SuccessionFlowUsage is a FlowUsage that is also a KerML SuccessionFlow."
)
