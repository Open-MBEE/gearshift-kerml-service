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
 * SysML ConstraintDefinition metaclass.
 * Specializes: Predicate, OccurrenceDefinition
 * A ConstraintDefinition is an OccurrenceDefinition that is also a Predicate that defines a constraint that
 * may be asserted to hold on a system or part of a system.
 */
fun createConstraintDefinitionMetaClass() = MetaClass(
    name = "ConstraintDefinition",
    isAbstract = false,
    superclasses = listOf("Predicate", "OccurrenceDefinition"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkConstraintDefinitionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Constraints::ConstraintCheck')",
            description = "A ConstraintDefinition must directly or indirectly specialize the base ConstraintDefinition Constraints::ConstraintCheck from the Systems Model Library."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "constraintDefinitionConstraintCheckBinding",
            baseConcept = "Constraints::ConstraintCheck",
            bindingKind = BindingKind.SPECIALIZES
        )
    ),
    description = "A ConstraintDefinition is an OccurrenceDefinition that is also a Predicate that defines a constraint."
)
