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
 * SysML PartDefinition metaclass.
 * Specializes: ItemDefinition
 * A PartDefinition is an ItemDefinition of a Class of systems or parts of systems. Note that all
 * parts may be considered items for certain purposes, but not all items are parts that can perform
 * actions within a system.
 */
fun createPartDefinitionMetaClass() = MetaClass(
    name = "PartDefinition",
    isAbstract = false,
    superclasses = listOf("ItemDefinition"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkPartDefinitionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Parts::Part')",
            description = "A PartDefinition must directly or indirectly specialize the base PartDefinition Parts::Part from the Systems Model Library."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "partDefinitionPartBinding",
            baseConcept = "Parts::Part",
            bindingKind = BindingKind.SPECIALIZES
        )
    ),
    description = "A PartDefinition is an ItemDefinition of a Class of systems or parts of systems."
)
