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
 * KerML LiteralString metaclass.
 * Specializes: LiteralExpression
 * A LiteralString is a LiteralExpression that provides a String value as a result. Its result parameter
 * must have the type String.
 */
fun createLiteralStringMetaClass() = MetaClass(
    name = "LiteralString",
    isAbstract = false,
    superclasses = listOf("LiteralExpression"),
    attributes = listOf(
        MetaProperty(
            name = "value",
            type = "String",
            description = "The String value that is the result of evaluating this LiteralString."
        )
    ),
    constraints = emptyList(),
    semanticBindings = listOf(
        SemanticBinding(
            name = "literalStringEvaluationsBinding",
            baseConcept = "Performances::literalStringEvaluations",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.Default
        )
    ),
    description = "A LiteralString is a LiteralExpression that provides a String value as a result."
)
