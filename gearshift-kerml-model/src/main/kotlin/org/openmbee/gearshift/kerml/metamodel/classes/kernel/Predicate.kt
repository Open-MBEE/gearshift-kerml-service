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
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * KerML Predicate metaclass.
 * Specializes: Function
 * A function that returns a boolean result.
 */
fun createPredicateMetaClass() = MetaClass(
    name = "Predicate",
    isAbstract = false,
    superclasses = listOf("Function"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "computePredicateTypedBooleanExpression",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "BooleanExpression.allInstances()->select(be | be.predicate->includes(self))",
            isNormative = false,
            description = "The BooleanExpressions that have this Predicate as a type."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "predicateBooleanEvaluationBinding",
            baseConcept = "Performances::BooleanEvaluation",
            bindingKind = BindingKind.SPECIALIZES,
            condition = BindingCondition.Default
        )
    ),
    description = "A function that returns a boolean result"
)
