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
 * SysML InterfaceUsage metaclass.
 * Specializes: ConnectionUsage
 * An InterfaceUsage is a Usage of an InterfaceDefinition to represent an interface connecting parts of a
 * system through specific ports.
 */
fun createInterfaceUsageMetaClass() = MetaClass(
    name = "InterfaceUsage",
    isAbstract = false,
    superclasses = listOf("ConnectionUsage"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkInterfaceUsageBinarySpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                ownedEndFeature->size() = 2 implies
                specializesFromLibrary('Interfaces::binaryInterfaces')
            """.trimIndent(),
            description = "A binary InterfaceUsage must directly or indirectly specialize the InterfaceUsage Interfaces::binaryInterfaces from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkInterfaceUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Interfaces::interfaces')",
            description = "An InterfaceUsage must directly or indirectly specialize the InterfaceUsage Interfaces::interfaces from the Systems Model Library."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "interfaceUsageInterfacesBinding",
            baseConcept = "Interfaces::interfaces",
            bindingKind = BindingKind.SUBSETS
        ),
        SemanticBinding(
            name = "interfaceUsageBinaryInterfacesBinding",
            baseConcept = "Interfaces::binaryInterfaces",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.PropertyEquals("ownedEndFeature->size()", "2")
        )
    ),
    description = "An InterfaceUsage is a Usage of an InterfaceDefinition to represent an interface connecting parts of a system through specific ports."
)
