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
 * SysML InterfaceDefinition metaclass.
 * Specializes: ConnectionDefinition
 * An InterfaceDefinition is a ConnectionDefinition all of whose ends are PortUsages, defining an
 * interface between elements that interact through such ports.
 */
fun createInterfaceDefinitionMetaClass() = MetaClass(
    name = "InterfaceDefinition",
    isAbstract = false,
    superclasses = listOf("ConnectionDefinition"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkInterfaceDefinitionBinarySpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                ownedEndFeature->size() = 2 implies
                specializesFromLibrary('Interfaces::BinaryInterface')
            """.trimIndent(),
            description = "A binary InterfaceDefinition must directly or indirectly specialize the InterfaceDefinition Interfaces::BinaryInterface from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkInterfaceDefinitionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Interfaces::Interface')",
            description = "An InterfaceDefinition must directly or indirectly specialize the InterfaceDefinition Interfaces::Interface from the Systems Model Library."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "interfaceDefinitionInterfaceBinding",
            baseConcept = "Interfaces::Interface",
            bindingKind = BindingKind.SPECIALIZES
        ),
        SemanticBinding(
            name = "interfaceDefinitionBinaryInterfaceBinding",
            baseConcept = "Interfaces::BinaryInterface",
            bindingKind = BindingKind.SPECIALIZES,
            condition = BindingCondition.PropertyEquals("ownedEndFeature->size()", "2")
        )
    ),
    description = "An InterfaceDefinition is a ConnectionDefinition all of whose ends are PortUsages, defining an interface between elements that interact through such ports."
)
