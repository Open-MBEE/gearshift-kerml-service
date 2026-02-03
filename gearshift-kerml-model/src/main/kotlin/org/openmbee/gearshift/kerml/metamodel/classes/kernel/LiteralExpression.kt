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

import org.openmbee.mdm.framework.meta.BindingCondition
import org.openmbee.mdm.framework.meta.BindingKind
import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.MetaOperation
import org.openmbee.mdm.framework.meta.MetaParameter
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * KerML LiteralExpression metaclass.
 * Specializes: Expression
 * A LiteralExpression is an Expression that provides a basic DataValue as a result.
 */
fun createLiteralExpressionMetaClass() = MetaClass(
    name = "LiteralExpression",
    isAbstract = false,
    superclasses = listOf("Expression"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "deriveLiteralExpressionIsModelLevelEvaluable",
            type = ConstraintType.REDEFINES_DERIVATION,
            expression = "true",
            description = "A LiteralExpression is always model-level evaluable."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "literalExpressionEvaluationsBinding",
            baseConcept = "Performances::literalEvaluations",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.Default
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
            body = MetaOperation.ocl("Sequence{self}"),
            description = "The model-level value of a LiteralExpression is itself."
        ),
        MetaOperation(
            name = "modelLevelEvaluable",
            returnType = "Boolean",
            parameters = listOf(
                MetaParameter(name = "visited", type = "Feature", lowerBound = 0, upperBound = -1)
            ),
            redefines = "modelLevelEvaluable",
            body = MetaOperation.ocl("true"),
            description = "A LiteralExpression is always model-level evaluable."
        )
    ),
    description = "A LiteralExpression is an Expression that provides a basic DataValue as a result."
)
