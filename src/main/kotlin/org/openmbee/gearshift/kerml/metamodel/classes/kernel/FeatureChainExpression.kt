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
import org.openmbee.gearshift.metamodel.MetaProperty

/**
 * KerML FeatureChainExpression metaclass.
 * Specializes: OperatorExpression
 * A FeatureChainExpression is an OperatorExpression whose operator is ".", which resolves to the
 * Function ControlFunctions::'.' from the Kernel Functions Library. It evaluates to the result of chaining the
 * result Feature of its single argument Expression with its targetFeature.
 */
fun createFeatureChainExpressionMetaClass() = MetaClass(
    name = "FeatureChainExpression",
    isAbstract = false,
    superclasses = listOf("OperatorExpression"),
    attributes = listOf(
        MetaProperty(
            name = "operator",
            type = "String",
            redefines = listOf("operator"),
            description = "The operator for this FeatureChainExpression, which must be '.'."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "checkFeatureChainExpressionResultSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                let inputParameters : Sequence(Feature) =
                    ownedFeatures->select(direction = _'in') in
                let sourceTargetFeature : Feature =
                    owningExpression.sourceTargetFeature() in
                sourceTargetFeature <> null and
                result.subsetsChain(inputParameters->first(), sourceTargetFeature) and
                result.owningType = self
            """.trimIndent(),
            description = "The result parameter of a FeatureChainExpression must specialize the feature chain of the FeatureChainExpression."
        ),
        MetaConstraint(
            name = "checkFeatureChainExpressionSourceTargetRedefinition",
            type = ConstraintType.VERIFICATION,
            expression = """
                let sourceTargetFeature : Feature = sourceTargetFeature() in
                sourceTargetFeature <> null and
                sourceTargetFeature.redefines(targetFeature)
            """.trimIndent(),
            description = "The first ownedFeature of the first owned input parameter of a FeatureChainExpression must redefine its targetFeature."
        ),
        MetaConstraint(
            name = "checkFeatureChainExpressionTargetRedefinition",
            type = ConstraintType.VERIFICATION,
            expression = """
                let sourceTargetFeature : Feature = sourceTargetFeature() in
                sourceTargetFeature <> null and
                sourceTargetFeature.redefinesFromLibrary('ControlFunctions::\'.\'::source::target')
            """.trimIndent(),
            description = "The first ownedFeature of the first owned input parameter of a FeatureChainExpression must redefine the Feature ControlFunctions::'.'::source::target from the Kernel Functions Library."
        ),
        MetaConstraint(
            name = "deriveFeatureChainExpressionTargetFeature",
            type = ConstraintType.DERIVATION,
            expression = """
                let nonParameterMemberships : Sequence(Membership) = ownedMembership->
                    reject(oclIsKindOf(ParameterMembership)) in
                if nonParameterMemberships->isEmpty() or
                    not nonParameterMemberships->first().memberElement.oclIsKindOf(Feature)
                then null
                else nonParameterMemberships->first().memberElement.oclAsType(Feature)
                endif
            """.trimIndent(),
            description = "The targetFeature of a FeatureChainExpression is the memberElement of its first ownedMembership that is not a ParameterMembership."
        ),
        MetaConstraint(
            name = "validateFeatureChainExpressionConformance",
            type = ConstraintType.VERIFICATION,
            expression = """
                argument->notEmpty() implies
                targetFeature.isFeaturedWithin(argument->first().result)
            """.trimIndent(),
            description = "The targetFeature of a FeatureChainExpression must be featured within the result parameter of the argument Expression of the FeatureChainExpression."
        ),
        MetaConstraint(
            name = "validateFeatureChainExpressionOperator",
            type = ConstraintType.VERIFICATION,
            expression = "operator = '.'",
            description = "The operator of a FeatureChainExpression must be '.'."
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "sourceTargetFeature",
            returnType = "Feature",
            returnLowerBound = 0,
            returnUpperBound = 1,
            body = """
                let inputParameters : Feature = ownedFeatures->
                    select(direction = _'in') in
                if inputParameters->isEmpty() or
                    inputParameters->first().ownedFeature->isEmpty()
                then null
                else inputParameters->first().ownedFeature->first()
                endif
            """.trimIndent(),
            description = "Return the first ownedFeature of the first owned input parameter of this FeatureChainExpression (if any)."
        )
    ),
    description = "A FeatureChainExpression is an OperatorExpression whose operator is '.', which resolves to the Function ControlFunctions::'.' from the Kernel Functions Library."
)
