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

/**
 * KerML BooleanExpression metaclass.
 * Specializes: Expression
 * An expression that evaluates to a boolean value.
 */
fun createBooleanExpressionMetaClass() = MetaClass(
    name = "BooleanExpression",
    isAbstract = false,
    superclasses = listOf("Expression"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkBooleanExpressionSpecialization",
            type = ConstraintType.IMPLICIT_SPECIALIZATION,
            expression = "specializesFromLibrary('Performances::booleanEvaluations')",
            libraryTypeName = "Performances::booleanEvaluations",
            description = "A BooleanExpression must directly or indirectly specialize the base BooleanExpression Performances::booleanEvaluations from the Kernel Semantic Library."
        ),
        MetaConstraint(
            name = "deriveBooleanExpressionPredicate",
            type = ConstraintType.REDEFINES_DERIVATION,
            expression = "type->selectByKind(Predicate)",
            description = "The Predicate that types this BooleanExpression."
        )
    ),
    description = "An expression that evaluates to a boolean value"
)
