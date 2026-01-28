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

import org.openmbee.gearshift.framework.meta.ConstraintType
import org.openmbee.gearshift.framework.meta.MetaClass
import org.openmbee.gearshift.framework.meta.MetaConstraint
import org.openmbee.gearshift.framework.meta.MetaProperty

/**
 * KerML CollectExpression metaclass.
 * Specializes: OperatorExpression
 * A CollectExpression is an OperatorExpression whose operator is "collect", which resolves to the
 * Function ControlFunctions::collect from the Kernel Functions Library.
 */
fun createCollectExpressionMetaClass() = MetaClass(
    name = "CollectExpression",
    isAbstract = false,
    superclasses = listOf("OperatorExpression"),
    attributes = listOf(
        MetaProperty(
            name = "operator",
            type = "String",
            redefines = listOf("operator"),
            description = "The operator for this CollectExpression, which must be 'collect'."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "validateCollectExpressionOperator",
            type = ConstraintType.VERIFICATION,
            expression = "operator = 'collect'",
            description = "The operator of a CollectExpression must be 'collect'."
        )
    ),
    description = "A CollectExpression is an OperatorExpression whose operator is 'collect', which resolves to the Function ControlFunctions::collect from the Kernel Functions Library."
)
