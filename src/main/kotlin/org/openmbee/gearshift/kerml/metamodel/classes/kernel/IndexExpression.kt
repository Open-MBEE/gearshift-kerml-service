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

import org.openmbee.gearshift.metamodel.ConstraintType
import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaConstraint
import org.openmbee.gearshift.metamodel.MetaProperty

/**
 * KerML IndexExpression metaclass.
 * Specializes: OperatorExpression
 * An IndexExpression is an OperatorExpression whose operator is "#", which resolves to the Function
 * BasicFunctions::'#' from the Kernel Functions Library.
 */
fun createIndexExpressionMetaClass() = MetaClass(
    name = "IndexExpression",
    isAbstract = false,
    superclasses = listOf("OperatorExpression"),
    attributes = listOf(
        MetaProperty(
            name = "operator",
            type = "String",
            redefines = listOf("operator"),
            description = "The operator for this IndexExpression, which must be '#'."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "checkIndexExpressionResultSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                arguments->notEmpty() and
                not arguments->first().result.specializesFromLibrary('Collections::Array') implies
                result.specializes(arguments->first().result)
            """.trimIndent(),
            description = "The result of an IndexExpression must specialize the result parameter of the first argument of the IndexExpression, unless that result already directly or indirectly specializes the DataType Collections::Array from the Kernel Data Type Library."
        ),
        MetaConstraint(
            name = "validateIndexExpressionOperator",
            type = ConstraintType.VERIFICATION,
            expression = "operator = '#'",
            description = "The operator of an IndexExpression must be '#'."
        )
    ),
    description = "An IndexExpression is an OperatorExpression whose operator is '#', which resolves to the Function BasicFunctions::'#' from the Kernel Functions Library."
)
