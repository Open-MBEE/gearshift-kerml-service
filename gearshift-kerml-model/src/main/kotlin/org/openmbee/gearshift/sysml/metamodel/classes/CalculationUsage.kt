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
import org.openmbee.mdm.framework.meta.MetaOperation
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * SysML CalculationUsage metaclass.
 * Specializes: ActionUsage, Expression
 * A CalculationUsage is an ActionUsage that is also an Expression, and, so, is typed by a Function.
 * Nominally, if the type is a CalculationDefinition, a CalculationUsage is a Usage of that
 * CalculationDefinition within a system. However, other kinds of kernel Functions are also allowed, to
 * permit use of Functions from the Kernel Model Libraries.
 */
fun createCalculationUsageMetaClass() = MetaClass(
    name = "CalculationUsage",
    isAbstract = false,
    superclasses = listOf("ActionUsage", "Expression"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkCalculationUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Calculations::calculations')",
            description = "A CalculationUsage must specialize directly or indirectly the CalculationUsage Calculations::calculations from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkCalculationUsageSubcalculationSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningType <> null and
                (owningType.oclIsKindOf(CalculationDefinition) or
                owningType.oclIsKindOf(CalculationUsage)) implies
                specializesFromLibrary('Calculations::Calculation::subcalculations')
            """.trimIndent(),
            description = "A composite CalculationUsage whose owningType is a CalculationDefinition or CalculationUsage must directly or indirectly specialize Calculations::Calculation::subcalculations."
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "modelLevelEvaluable",
            returnType = "Boolean",
            returnLowerBound = 1,
            returnUpperBound = 1,
            redefines = "modelLevelEvaluable",
            parameters = listOf(
                MetaOperation.Parameter("visited", "Feature", upperBound = -1)
            ),
            body = MetaOperation.ocl("false"),
            description = "A CalculationUsage is not model-level evaluable."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "calculationUsageCalculationsBinding",
            baseConcept = "Calculations::calculations",
            bindingKind = BindingKind.SUBSETS
        ),
        SemanticBinding(
            name = "calculationUsageSubcalculationsBinding",
            baseConcept = "Calculations::Calculation::subcalculations",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.Or(
                listOf(
                    BindingCondition.OwningTypeIs("CalculationDefinition"),
                    BindingCondition.OwningTypeIs("CalculationUsage")
                )
            )
        )
    ),
    description = "A CalculationUsage is an ActionUsage that is also an Expression, typed by a Function."
)
