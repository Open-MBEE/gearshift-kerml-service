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
 * SysML ConnectionUsage metaclass.
 * Specializes: PartUsage, ConnectorAsUsage
 * A ConnectionUsage is a ConnectorAsUsage that is also a PartUsage. Nominally, if its type is a
 * ConnectionDefinition, then a ConnectionUsage is a Usage of that ConnectionDefinition, representing a
 * connection between parts of a system. However, other kinds of kernel AssociationStructures are also allowed,
 * to permit use of AssociationStructures from the Kernel Model Libraries.
 */
fun createConnectionUsageMetaClass() = MetaClass(
    name = "ConnectionUsage",
    isAbstract = false,
    superclasses = listOf("PartUsage", "ConnectorAsUsage"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkConnectionUsageBinarySpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                ownedEndFeature->size() = 2 implies
                specializesFromLibrary('Connections::binaryConnections')
            """.trimIndent(),
            description = "A binary ConnectionUsage must directly or indirectly specialize the ConnectionUsage Connections::binaryConnections from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkConnectionUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Connections::connections')",
            description = "A ConnectionUsage must directly or indirectly specialize the ConnectionUsage Connections::connections from the Systems Model Library."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "connectionUsageConnectionsBinding",
            baseConcept = "Connections::connections",
            bindingKind = BindingKind.SUBSETS
        ),
        SemanticBinding(
            name = "connectionUsageBinaryConnectionsBinding",
            baseConcept = "Connections::binaryConnections",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.PropertyEquals("ownedEndFeature->size()", "2")
        )
    ),
    description = "A ConnectionUsage is a ConnectorAsUsage that is also a PartUsage."
)
