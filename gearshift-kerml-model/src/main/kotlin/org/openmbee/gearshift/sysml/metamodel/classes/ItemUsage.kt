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
 * SysML ItemUsage metaclass.
 * Specializes: OccurrenceUsage
 * An ItemUsage is an OccurrenceUsage whose definition is a Structure. Nominally, if the definition
 * is an ItemDefinition, an ItemUsage is a usage of that ItemDefinition within a system. However,
 * other kinds of Kernel Structures are also allowed, to permit use of Structures from the Kernel
 * Model Libraries.
 */
fun createItemUsageMetaClass() = MetaClass(
    name = "ItemUsage",
    isAbstract = false,
    superclasses = listOf("OccurrenceUsage"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkItemUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Items::items')",
            description = "An ItemUsage must directly or indirectly specialize the Systems Model Library ItemUsage items."
        ),
        MetaConstraint(
            name = "checkItemUsageSubitemSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isComposite and owningType <> null and
                (owningType.oclIsKindOf(ItemDefinition) or
                owningType.oclIsKindOf(ItemUsage)) implies
                specializesFromLibrary('Items::Item::subitem')
            """.trimIndent(),
            description = "A composite ItemUsage whose owningType is an ItemDefinition or ItemUsage must specialize Items::Item::subitem."
        ),
        MetaConstraint(
            name = "deriveItemUsageItemDefinition",
            type = ConstraintType.DERIVATION,
            expression = "occurrenceDefinition->selectByKind(Structure)",
            description = "The itemDefinitions of an ItemUsage are those occurrenceDefinitions that are Structures."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "itemUsageItemsBinding",
            baseConcept = "Items::items",
            bindingKind = BindingKind.SUBSETS
        ),
        SemanticBinding(
            name = "itemUsageSubitemBinding",
            baseConcept = "Items::Item::subitem",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.And(
                listOf(
                    BindingCondition.IsComposite,
                    BindingCondition.Or(
                        listOf(
                            BindingCondition.OwningTypeIs("ItemDefinition"),
                            BindingCondition.OwningTypeIs("ItemUsage")
                        )
                    )
                )
            )
        )
    ),
    description = "An ItemUsage is an OccurrenceUsage whose definition is a Structure."
)
