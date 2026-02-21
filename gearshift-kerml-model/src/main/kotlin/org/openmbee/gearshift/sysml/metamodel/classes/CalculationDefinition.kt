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
 * SysML CalculationDefinition metaclass.
 * Specializes: ActionDefinition, Function
 * A CalculationDefinition is an ActionDefinition that also defines a Function producing a result.
 */
fun createCalculationDefinitionMetaClass() = MetaClass(
    name = "CalculationDefinition",
    isAbstract = false,
    superclasses = listOf("ActionDefinition", "Function"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkCalculationDefinitionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Calculations::Calculation')",
            description = "A CalculationDefinition must directly or indirectly specialize the CalculationDefinition Calculations::Calculation from the Systems Model Library."
        ),
        MetaConstraint(
            name = "deriveCalculationDefinitionCalculation",
            type = ConstraintType.DERIVATION,
            expression = "action->selectByKind(CalculationUsage)",
            description = "The calculations of a CalculationDefinition are those of its actions that are CalculationUsages."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "calculationDefinitionCalculationBinding",
            baseConcept = "Calculations::Calculation",
            bindingKind = BindingKind.SPECIALIZES
        )
    ),
    description = "A CalculationDefinition is an ActionDefinition that also defines a Function producing a result."
)
