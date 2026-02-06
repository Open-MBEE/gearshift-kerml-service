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
package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.MetaProperty

/**
 * KerML SelectExpression metaclass.
 * Specializes: OperatorExpression
 * A SelectExpression is an OperatorExpression whose operator is "select", which resolves to the
 * Function ControlFunctions::select from the Kernel Functions Library.
 */
fun createSelectExpressionMetaClass() = MetaClass(
    name = "SelectExpression",
    isAbstract = false,
    superclasses = listOf("OperatorExpression"),
    attributes = listOf(
        MetaProperty(
            name = "operator",
            type = "String",
            redefines = listOf("operator"),
            description = "The operator for a SelectExpression, which must be 'select'."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "checkSelectExpressionResultSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "argument->notEmpty() implies result.specializes(argument->first().result)",
            description = "The result of a SelectExpression must specialize the result parameter of the first argument of the SelectExpression."
        ),
        MetaConstraint(
            name = "validateSelectExpressionOperator",
            type = ConstraintType.VERIFICATION,
            expression = "operator = 'select'",
            description = "The operator of a SelectExpression must be 'select'."
        )
    ),
    description = "A SelectExpression is an OperatorExpression whose operator is 'select', which resolves to the Function ControlFunctions::select from the Kernel Functions Library."
)
