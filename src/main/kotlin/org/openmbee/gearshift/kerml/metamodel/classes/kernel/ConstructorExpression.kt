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
 * KerML ConstructorExpression metaclass.
 * Specializes: InstantiationExpression
 * A ConstructorExpression is an InstantiationExpression whose result specializes its
 * instantiatedType, binding some or all of the features of the instantiatedType to the results of its
 * argument Expressions.
 */
fun createConstructorExpressionMetaClass() = MetaClass(
    name = "ConstructorExpression",
    isAbstract = false,
    superclasses = listOf("InstantiationExpression"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkConstructorExpressionResultDefaultValueBindingConnector",
            type = ConstraintType.VERIFICATION,
            expression = "true",  // TBD in spec
            description = "The result of a ConstructorExpression must own a BindingConnector between the featureWithValue and valueExpression of any FeatureValue that is the effective default value for a feature of the instantiatedType of the InvocationExpression."
        ),
        MetaConstraint(
            name = "checkConstructorExpressionResultFeatureRedefinition",
            type = ConstraintType.VERIFICATION,
            expression = """
                let features : OrderedSet(Feature) = instantiatedType.feature->
                    select(owningMembership.visibility = VisibilityKind::public) in
                result.ownedFeature->forAll(f |
                    f.ownedRedefinition.redefinedFeature->
                    intersection(features)->size() = 1)
            """.trimIndent(),
            description = "Each ownedFeature of the result of a ConstructorExpression must redefine exactly one public feature of the instantiatedType of the ConstructorExpression."
        ),
        MetaConstraint(
            name = "checkConstructorExpressionResultSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "result.specializes(instantiatedType)",
            description = "The result of a ConstructorExpression must specialize the instantiatedType of the ConstructorExpression."
        ),
        MetaConstraint(
            name = "checkConstructorExpressionSpecialization",
            type = ConstraintType.IMPLICIT_SPECIALIZATION,
            expression = "specializes('Performances::constructorEvaluations')",
            libraryTypeName = "Performances::constructorEvaluations",
            description = "A ConstructorExpression must directly or indirectly specialize the Expression Performances::constructorEvaluations from the Kernel Semantic Library."
        ),
        MetaConstraint(
            name = "deriveConstructorExpressionArgument",
            type = ConstraintType.REDEFINES_DERIVATION,
            expression = """
                instantiatedType.feature->collect(f |
                    result.ownedFeatures->select(redefines(f)).valuation->
                    select(v | v <> null).value
                )
            """.trimIndent(),
            description = "The arguments of a ConstructorExpression are the valueExpressions of the FeatureValues of the ownedFeatures of its result parameter, in an order corresponding to the order of the features of the instantiatedType that the result ownedFeatures redefine."
        ),
        MetaConstraint(
            name = "validateConstructorExpressionNoDuplicateFeatureRedefinition",
            type = ConstraintType.VERIFICATION,
            expression = """
                let features : OrderedSet(Feature) = instantiatedType.feature->
                    select(visibility = VisibilityKind::public) in
                result.ownedFeature->forAll(f1 | result.ownedFeature->forAll(f2 |
                    f1 <> f2 implies
                    f1.ownedRedefinition.redefinedFeature->
                    intersection(f2.ownedRedefinition.redefinedFeature)->
                    intersection(features)->isEmpty()))
            """.trimIndent(),
            description = "Two different ownedFeatures of the result of a ConstructorExpression must not redefine the same feature of the instantiatedType of the ConstructorExpression."
        ),
        MetaConstraint(
            name = "validateConstructorExpressionOwnedFeatures",
            type = ConstraintType.VERIFICATION,
            expression = "ownedFeatures->excluding(result)->isEmpty()",
            description = "A ConstructorExpression must not have any ownedFeatures other than its result."
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "modelLevelEvaluable",
            returnType = "Boolean",
            parameters = listOf(
                MetaParameter(name = "visited", type = "Feature", lowerBound = 0, upperBound = -1)
            ),
            redefines = "modelLevelEvaluable",
            body = "argument->forAll(modelLevelEvaluable(visited))",
            description = "A ConstructorExpression is model-level evaluable if all its argument Expressions are model-level evaluable."
        )
    ),
    description = "A ConstructorExpression is an InstantiationExpression whose result specializes its instantiatedType, binding some or all of the features of the instantiatedType to the results of its argument Expressions."
)
