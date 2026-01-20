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
 * KerML LiteralInteger metaclass.
 * Specializes: LiteralExpression
 * A LiteralInteger is a LiteralExpression that provides an Integer value as a result. Its result
 * parameter must have the type Integer.
 */
fun createLiteralIntegerMetaClass() = MetaClass(
    name = "LiteralInteger",
    isAbstract = false,
    superclasses = listOf("LiteralExpression"),
    attributes = listOf(
        MetaProperty(
            name = "value",
            type = "Integer",
            description = "The Integer value that is the result of evaluating this LiteralInteger."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "checkLiteralIntegerSpecialization",
            type = ConstraintType.IMPLICIT_SPECIALIZATION,
            expression = "specializesFromLibrary('Performances::literalIntegerEvaluations')",
            libraryTypeName = "Performances::literalIntegerEvaluations",
            description = "A LiteralInteger must directly or indirectly specialize Performances::literalIntegerEvaluations from the Kernel Semantic Library."
        )
    ),
    description = "A LiteralInteger is a LiteralExpression that provides an Integer value as a result."
)
