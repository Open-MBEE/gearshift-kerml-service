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
 * SysML PortUsage metaclass.
 * Specializes: OccurrenceUsage
 * A PortUsage is a usage of a PortDefinition. A PortUsage itself as well as all its nestedUsages
 * must be referential (non-composite).
 */
fun createPortUsageMetaClass() = MetaClass(
    name = "PortUsage",
    isAbstract = false,
    superclasses = listOf("OccurrenceUsage"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkPortUsageOwnedPortSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningType <> null and
                (owningType.oclIsKindOf(PartDefinition) or
                owningType.oclIsKindOf(PartUsage)) implies
                specializesFromLibrary('Parts::Part::ownedPorts')
            """.trimIndent(),
            description = "A PortUsage whose owningType is a PartDefinition or PartUsage must specialize Parts::Part::ownedPorts."
        ),
        MetaConstraint(
            name = "checkPortUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Ports::ports')",
            description = "A PortUsage must directly or indirectly specialize the PortUsage Ports::ports from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkPortUsageSubportSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isComposite and owningType <> null and
                (owningType.oclIsKindOf(PortDefinition) or
                owningType.oclIsKindOf(PortUsage)) implies
                specializesFromLibrary('Ports::Port::subports')
            """.trimIndent(),
            description = "A composite PortUsage with an owningType that is a PortDefinition or PortUsage must specialize Ports::Port::subports."
        ),
        MetaConstraint(
            name = "validatePortUsageIsReference",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningType = null or
                not owningType.oclIsKindOf(PortDefinition) and
                not owningType.oclIsKindOf(PortUsage) implies
                isReference
            """.trimIndent(),
            description = "Unless a PortUsage has an owningType that is a PortDefinition or a PortUsage, it must be referential."
        ),
        MetaConstraint(
            name = "validatePortUsageNestedUsagesNotComposite",
            type = ConstraintType.VERIFICATION,
            expression = """
                nestedUsage->
                    reject(oclIsKindOf(PortUsage))->
                    forAll(not isComposite)
            """.trimIndent(),
            description = "The nestedUsages of a PortUsage that are not themselves PortUsages must not be composite."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "portUsagePortsBinding",
            baseConcept = "Ports::ports",
            bindingKind = BindingKind.SUBSETS
        ),
        SemanticBinding(
            name = "portUsageOwnedPortsBinding",
            baseConcept = "Parts::Part::ownedPorts",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.Or(
                listOf(
                    BindingCondition.OwningTypeIs("PartDefinition"),
                    BindingCondition.OwningTypeIs("PartUsage")
                )
            )
        ),
        SemanticBinding(
            name = "portUsageSubportsBinding",
            baseConcept = "Ports::Port::subports",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.And(
                listOf(
                    BindingCondition.IsComposite,
                    BindingCondition.Or(
                        listOf(
                            BindingCondition.OwningTypeIs("PortDefinition"),
                            BindingCondition.OwningTypeIs("PortUsage")
                        )
                    )
                )
            )
        )
    ),
    description = "A PortUsage is a usage of a PortDefinition."
)
