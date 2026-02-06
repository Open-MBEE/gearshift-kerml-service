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
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaProperty
import org.openmbee.mdm.framework.meta.SemanticBinding

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
    constraints = emptyList(),
    semanticBindings = listOf(
        SemanticBinding(
            name = "literalIntegerEvaluationsBinding",
            baseConcept = "Performances::literalIntegerEvaluations",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.Default
        )
    ),
    description = "A LiteralInteger is a LiteralExpression that provides an Integer value as a result."
)
