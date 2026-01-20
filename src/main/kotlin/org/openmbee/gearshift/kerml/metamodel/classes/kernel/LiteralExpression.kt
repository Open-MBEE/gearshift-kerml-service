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
import org.openmbee.gearshift.metamodel.MetaOperation
import org.openmbee.gearshift.metamodel.MetaParameter

/**
 * KerML LiteralExpression metaclass.
 * Specializes: Expression
 * A LiteralExpression is an Expression that provides a basic DataValue as a result.
 */
fun createLiteralExpressionMetaClass() = MetaClass(
    name = "LiteralExpression",
    isAbstract = true,
    superclasses = listOf("Expression"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkLiteralExpressionSpecialization",
            type = ConstraintType.IMPLICIT_SPECIALIZATION,
            expression = "specializesFromLibrary('Performances::literalEvaluations')",
            libraryTypeName = "Performances::literalEvaluations",
            description = "A LiteralExpression must directly or indirectly specialize the base LiteralExpression Performances::literalEvaluations from the Kernel Semantic Library."
        ),
        MetaConstraint(
            name = "deriveLiteralExpressionIsModelLevelEvaluable",
            type = ConstraintType.REDEFINES_DERIVATION,
            expression = "true",
            description = "A LiteralExpression is always model-level evaluable."
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "evaluate",
            returnType = "Element",
            returnLowerBound = 0,
            returnUpperBound = -1,
            parameters = listOf(
                MetaParameter(name = "target", type = "Element")
            ),
            redefines = "evaluate",
            preconditions = listOf("isModelLevelEvaluable"),
            body = "Sequence{self}",
            description = "The model-level value of a LiteralExpression is itself."
        ),
        MetaOperation(
            name = "modelLevelEvaluable",
            returnType = "Boolean",
            parameters = listOf(
                MetaParameter(name = "visited", type = "Feature", lowerBound = 0, upperBound = -1)
            ),
            redefines = "modelLevelEvaluable",
            body = "true",
            description = "A LiteralExpression is always model-level evaluable."
        )
    ),
    description = "A LiteralExpression is an Expression that provides a basic DataValue as a result."
)
