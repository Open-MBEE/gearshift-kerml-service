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
 * KerML Function metaclass.
 * Specializes: Behavior
 * A behavior that represents a function.
 */
fun createFunctionMetaClass() = MetaClass(
    name = "Function",
    isAbstract = false,
    superclasses = listOf("Behavior"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkFunctionResultBindingConnector",
            type = ConstraintType.IMPLICIT_BINDING_CONNECTOR,
            expression = """
                ownedMembership.selectByKind(ResultExpressionMembership)->
                forAll(mem | ownedFeature.selectByKind(BindingConnector)->
                    exists(binding |
                        binding.relatedFeature->includes(result) and
                        binding.relatedFeature->includes(mem.ownedResultExpression.result)))
            """.trimIndent(),
            description = "If a Function has an Expression owned via a ResultExpressionMembership, then the owning Function must also own a BindingConnector between its result parameter and the result parameter of the resultExpression."
        ),
        MetaConstraint(
            name = "deriveFunctionResult",
            type = ConstraintType.DERIVATION,
            expression = """
                let resultParams : Sequence(Feature) =
                    featureMemberships->
                    selectByKind(ReturnParameterMembership).
                    ownedMemberParameter in
                if resultParams->notEmpty() then resultParams->first()
                else null
                endif
            """.trimIndent(),
            description = "The result parameter of a Function is its parameter owned (possibly in a supertype) via a ReturnParameterMembership (if any)."
        ),
        MetaConstraint(
            name = "validateFunctionResultExpressionMembership",
            type = ConstraintType.VERIFICATION,
            expression = "membership->selectByKind(ResultExpressionMembership)->size() <= 1",
            description = "A Function must have at most one ResultExpressionMembership."
        ),
        MetaConstraint(
            name = "validateFunctionResultParameterMembership",
            type = ConstraintType.VERIFICATION,
            expression = "featureMembership->selectByKind(ReturnParameterMembership)->size() = 1",
            description = "A Function must have exactly one featureMembership (owned or inherited) that is a ResultParameterMembership."
        ),
        MetaConstraint(
            name = "deriveFunctionExpression",
            type = ConstraintType.DERIVATION,
            expression = "step->selectByKind(Expression)",
            description = "The Expressions that are steps in the calculation of the result of this Function."
        ),
        MetaConstraint(
            name = "computeFeatureComputingExpression",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "Expression.allInstances()->select(e | e.result = self)",
            isNormative = false,
            description = "The Expressions that have this Feature as their result."
        ),
        MetaConstraint(
            name = "computeFeatureComputingFunction",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "Function.allInstances()->select(f | f.result = self)",
            isNormative = false,
            description = "The Functions that have this Feature as their result."
        ),
        MetaConstraint(
            name = "computeFunctionTypedExpression",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "Expression.allInstances()->select(e | e.function->includes(self))",
            isNormative = false,
            description = "The Expressions that have this Function as a type."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "functionEvaluationBinding",
            baseConcept = "Performances::Evaluation",
            bindingKind = BindingKind.SPECIALIZES,
            condition = BindingCondition.Default
        )
    ),
    description = "A behavior that represents a function"
)
