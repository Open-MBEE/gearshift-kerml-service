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

import org.openmbee.gearshift.framework.meta.BindingCondition
import org.openmbee.gearshift.framework.meta.BindingKind
import org.openmbee.gearshift.framework.meta.MetaClass
import org.openmbee.gearshift.framework.meta.MetaProperty
import org.openmbee.gearshift.framework.meta.SemanticBinding

/**
 * KerML LiteralRational metaclass.
 * Specializes: LiteralExpression
 * A LiteralRational is a LiteralExpression that provides a Rational value as a result. Its result
 * parameter must have the type Rational.
 */
fun createLiteralRationalMetaClass() = MetaClass(
    name = "LiteralRational",
    isAbstract = false,
    superclasses = listOf("LiteralExpression"),
    attributes = listOf(
        MetaProperty(
            name = "value",
            type = "Real",
            description = "The value whose rational approximation is the result of evaluating this LiteralRational."
        )
    ),
    constraints = emptyList(),
    semanticBindings = listOf(
        SemanticBinding(
            name = "literalRationalEvaluationsBinding",
            baseConcept = "Performances::literalRationalEvaluations",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.Default
        )
    ),
    description = "A LiteralRational is a LiteralExpression that provides a Rational value as a result."
)
