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
 * KerML LiteralInfinity metaclass.
 * Specializes: LiteralExpression
 * A LiteralInfinity is a LiteralExpression that provides the positive infinity value (*). Its result must
 * have the type Positive.
 */
fun createLiteralInfinityMetaClass() = MetaClass(
    name = "LiteralInfinity",
    isAbstract = false,
    superclasses = listOf("LiteralExpression"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkLiteralInfinitySpecialization",
            type = ConstraintType.IMPLICIT_SPECIALIZATION,
            expression = "specializesFromLibrary('Performances::literalIntegerEvaluations')",
            libraryTypeName = "Performances::literalIntegerEvaluations",
            description = "A LiteralInfinity must directly or indirectly specialize Performances::literalIntegerEvaluations from the Kernel Semantic Library."
        )
    ),
    description = "A LiteralInfinity is a LiteralExpression that provides the positive infinity value (*)."
)
