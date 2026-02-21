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
 * SysML AllocationDefinition metaclass.
 * Specializes: ConnectionDefinition
 * An AllocationDefinition is a ConnectionDefinition that specifies that some or all of the responsibility to
 * realize the intent of the source is allocated to the target instances. Such allocations define mappings across the
 * various structures and hierarchies of a system model, perhaps as a precursor to more rigorous specifications and
 * implementations. An AllocationDefinition can itself be refined using nested allocations that give a finer-
 * grained decomposition of the containing allocation mapping.
 */
fun createAllocationDefinitionMetaClass() = MetaClass(
    name = "AllocationDefinition",
    isAbstract = false,
    superclasses = listOf("ConnectionDefinition"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkAllocationDefinitionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Allocations::Allocation')",
            description = "An AllocationDefinition must directly or indirectly specialize the AllocationDefinition Allocations::Allocation from the Systems Model Library."
        ),
        MetaConstraint(
            name = "deriveAllocationDefinitionAllocation",
            type = ConstraintType.DERIVATION,
            expression = "usage->selectByKind(AllocationUsage)",
            description = "The allocations of an AllocationDefinition are all its usages that are AllocationUsages."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "allocationDefinitionAllocationBinding",
            baseConcept = "Allocations::Allocation",
            bindingKind = BindingKind.SPECIALIZES
        )
    ),
    description = "An AllocationDefinition is a ConnectionDefinition that specifies that some or all of the responsibility to realize the intent of the source is allocated to the target instances."
)
