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
 * KerML InvocationExpression metaclass.
 * Specializes: InstantiationExpression
 * An InvocationExpression is an InstantiationExpression whose instantiatedType must be a
 * Behavior or a Feature typed by a single Behavior (such as a Step). Each of the input parameters of the
 * instantiatedType are bound to the result of an argument Expression.
 */
fun createInvocationExpressionMetaClass() = MetaClass(
    name = "InvocationExpression",
    isAbstract = false,
    superclasses = listOf("InstantiationExpression"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkInvocationExpressionBehaviorBindingConnector",
            type = ConstraintType.IMPLICIT_BINDING_CONNECTOR,
            expression = """
                not instantiatedType.oclIsKindOf(Function) and
                not (instantiatedType.oclIsKindOf(Feature) and
                    instantiatedType.oclAsType(Feature).type->exists(oclIsKindOf(Function))) implies
                ownedFeature.selectByKind(BindingConnector)->exists(
                    relatedFeature->includes(self) and
                    relatedFeature->includes(result))
            """.trimIndent(),
            description = "If the instantiatedType of an InvocationExpression is neither a Function nor a Feature whose type is a Function, then the InvocationExpression must own a BindingConnector between itself and its result parameter."
        ),
        MetaConstraint(
            name = "checkInvocationExpressionBehaviorResultSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                not instantiatedType.oclIsKindOf(Function) and
                not (instantiatedType.oclIsKindOf(Feature) and
                    instantiatedType.oclAsType(Feature).type->exists(oclIsKindOf(Function))) implies
                result.specializes(instantiatedType)
            """.trimIndent(),
            description = "If the instantiatedType of an InvocationExpression is neither a Function nor a Feature whose type is a Function, then the result of the InvocationExpression must specialize the instantiatedType."
        ),
        MetaConstraint(
            name = "checkInvocationExpressionDefaultValueBindingConnector",
            type = ConstraintType.IMPLICIT_BINDING_CONNECTOR,
            expression = "true",  // TBD in spec
            description = "An InvocationExpression must own a BindingConnector between the featureWithValue and value Expression of any FeatureValue that is the effective default value for a feature of the instantiatedType of the InvocationExpression."
        ),
        MetaConstraint(
            name = "checkInvocationExpressionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializes(instantiatedType)",
            description = "An InvocationExpression must specialize its instantiatedType."
        ),
        MetaConstraint(
            name = "deriveInvocationExpressionArgument",
            type = ConstraintType.DERIVATION,
            redefines = "deriveInstantiationExpressionArgument",
            expression = """
                instantiatedType.input->collect(inp |
                    ownedFeatures->select(redefines(inp)).valuation->
                    select(v | v <> null).value
                )
            """.trimIndent(),
            description = "The arguments of an InvocationExpression are the valueExpressions of the FeatureValues of its ownedFeatures, in an order corresponding to the order of the input parameters of the instantiatedType that the ownedFeatures redefine."
        ),
        MetaConstraint(
            name = "validateInvocationExpressionInstantiatedType",
            type = ConstraintType.VERIFICATION,
            expression = """
                instantiatedType.oclIsKindOf(Behavior) or
                instantiatedType.oclIsKindOf(Feature) and
                instantiatedType.type->exists(oclIsKindOf(Behavior)) and
                instantiatedType.type->size() = 1
            """.trimIndent(),
            description = "The instantiatedType of an InvocationExpression must be either a Behavior or a Feature with a single type, which is a Behavior."
        ),
        MetaConstraint(
            name = "validateInvocationExpressionNoDuplicateParameterRedefinition",
            type = ConstraintType.VERIFICATION,
            expression = """
                let features : OrderedSet(Feature) = instantiatedType.feature in
                input->forAll(inp1 | input->forAll(inp2 |
                    inp1 <> inp2 implies
                    inp1.ownedRedefinition.redefinedFeature->
                    intersection(inp2.ownedRedefinition.redefinedFeature)->
                    intersection(features)->isEmpty()))
            """.trimIndent(),
            description = "Two different ownedFeatures of an InvocationExpression must not redefine the same feature of the instantiatedType of the InvocationExpression."
        ),
        MetaConstraint(
            name = "validateInvocationExpressionOwnedFeatures",
            type = ConstraintType.VERIFICATION,
            expression = """
                ownedFeature->forAll(f |
                    f <> result implies
                    f.direction = FeatureDirectionKind::_'in')
            """.trimIndent(),
            description = "Other than its result, all the ownedFeatures of an InvocationExpression must have direction = in."
        ),
        MetaConstraint(
            name = "validateInvocationExpressionParameterRedefinition",
            type = ConstraintType.VERIFICATION,
            expression = """
                let parameters : OrderedSet(Feature) = instantiatedType.input in
                input->forAll(inp |
                    inp.ownedRedefinition.redefinedFeature->
                    intersection(parameters)->size() = 1)
            """.trimIndent(),
            description = "Each input parameter of an InvocationExpression must redefine exactly one input parameter of the instantiatedType of the InvocationExpression."
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
            body = "Sequence{}",  // Actual implementation is complex - apply Function to argument values
            description = "Apply the Function that is the type of this InvocationExpression to the argument values resulting from evaluating each of the argument Expressions on the given target."
        ),
        MetaOperation(
            name = "modelLevelEvaluable",
            returnType = "Boolean",
            parameters = listOf(
                MetaParameter(name = "visited", type = "Feature", lowerBound = 0, upperBound = -1)
            ),
            redefines = "modelLevelEvaluable",
            body = """
                argument->forAll(modelLevelEvaluable(visited)) and
                function.isModelLevelEvaluable
            """.trimIndent(),
            description = "An InvocationExpression is model-level evaluable if all its argument Expressions are model-level evaluable and its function is model-level evaluable."
        )
    ),
    description = "An InvocationExpression is an InstantiationExpression whose instantiatedType must be a Behavior or a Feature typed by a single Behavior."
)
