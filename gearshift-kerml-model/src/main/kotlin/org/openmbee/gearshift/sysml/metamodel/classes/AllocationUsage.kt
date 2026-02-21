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
 * SysML AllocationUsage metaclass.
 * Specializes: ConnectionUsage
 * An AllocationUsage is a usage of an AllocationDefinition asserting the allocation of the source feature to
 * the target feature.
 */
fun createAllocationUsageMetaClass() = MetaClass(
    name = "AllocationUsage",
    isAbstract = false,
    superclasses = listOf("ConnectionUsage"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkAllocationUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Allocations::allocations')",
            description = "An AllocationUsage must directly or indirectly specialize the AllocationUsage Allocations::allocations from the Systems Model Library."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "allocationUsageAllocationsBinding",
            baseConcept = "Allocations::allocations",
            bindingKind = BindingKind.SUBSETS
        )
    ),
    description = "An AllocationUsage is a usage of an AllocationDefinition asserting the allocation of the source feature to the target feature."
)
