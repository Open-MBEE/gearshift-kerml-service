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
import org.openmbee.mdm.framework.meta.MetaOperation
import org.openmbee.mdm.framework.meta.MetaParameter
import org.openmbee.mdm.framework.meta.MetaProperty
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * KerML Expression metaclass.
 * Specializes: Step
 * A step that represents an expression.
 */
fun createExpressionMetaClass() = MetaClass(
    name = "Expression",
    isAbstract = false,
    superclasses = listOf("Step"),
    attributes = listOf(
        MetaProperty(
            name = "isModelLevelEvaluable",
            type = "Boolean",
            isDerived = true,
            derivationConstraint = "deriveExpressionIsModelLevelEvaluable",
            description = "Whether this Expression meets the constraints necessary to be evaluated at model level."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "checkExpressionResultBindingConnector",
            type = ConstraintType.IMPLICIT_BINDING_CONNECTOR,
            expression = """
                ownedMembership.selectByKind(ResultExpressionMembership)->
                forAll(mem | ownedFeature.selectByKind(BindingConnector)->
                    exists(binding |
                        binding.relatedFeature->includes(result) and
                        binding.relatedFeature->includes(mem.ownedResultExpression.result)))
            """.trimIndent(),
            description = "If an Expression has an Expression owned via a ResultExpressionMembership, then the owning Expression must also own a BindingConnector between its result parameter and the result parameter of the resultExpression."
        ),
        MetaConstraint(
            name = "checkExpressionTypeFeaturing",
            type = ConstraintType.IMPLICIT_TYPE_FEATURING,
            expression = """
                owningMembership <> null and
                owningMembership.oclIsKindOf(FeatureValue) implies
                let featureWithValue : Feature =
                    owningMembership.oclAsType(FeatureValue).featureWithValue in
                featuringType = featureWithValue.featuringType
            """.trimIndent(),
            description = "If this Expression is owned by a FeatureValue, then it must have the same featuringTypes as the featureWithValue of the FeatureValue."
        ),
        MetaConstraint(
            name = "deriveExpressionFunction",
            type = ConstraintType.REDEFINES_DERIVATION,
            expression = "type->selectByKind(Function)",
            description = "The Function that types this Expression."
        ),
        MetaConstraint(
            name = "deriveExpressionIsModelLevelEvaluable",
            type = ConstraintType.DERIVATION,
            expression = "modelLevelEvaluable(Set(Element){})",
            description = "Whether an Expression isModelLevelEvaluable is determined by the modelLevelEvaluable() operation."
        ),
        MetaConstraint(
            name = "deriveExpressionResult",
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
            description = "The result parameter of an Expression is its parameter owned (possibly in a supertype) via a ReturnParameterMembership (if any)."
        ),
        MetaConstraint(
            name = "validateExpressionResultExpressionMembership",
            type = ConstraintType.VERIFICATION,
            expression = "membership->selectByKind(ResultExpressionMembership)->size() <= 1",
            description = "An Expression must have at most one ResultExpressionMembership."
        ),
        MetaConstraint(
            name = "validateExpressionResultParameterMembership",
            type = ConstraintType.VERIFICATION,
            expression = "featureMembership->selectByKind(ReturnParameterMembership)->size() = 1",
            description = "An Expression must have exactly one featureMembership (owned or inherited) that is a ResultParameterMembership."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "expressionEvaluationsBinding",
            baseConcept = "Performances::evaluations",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.Default
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "checkCondition",
            returnType = "Boolean",
            parameters = listOf(
                MetaParameter(name = "target", type = "Element")
            ),
            body = MetaOperation.ocl("""
                let results: Sequence(Element) = evaluate(target) in
                results->size() = 1 and
                results->first().oclIsKindOf(LiteralBoolean) and
                results->first().oclAsType(LiteralBoolean).value
            """.trimIndent()),
            description = "Model-level evaluate this Expression with the given target. If the result is a LiteralBoolean, return its value. Otherwise return false."
        ),
        MetaOperation(
            name = "evaluate",
            returnType = "Element",
            returnLowerBound = 0,
            returnUpperBound = -1,
            parameters = listOf(
                MetaParameter(name = "target", type = "Element")
            ),
            preconditions = listOf("isModelLevelEvaluable"),
            body = MetaOperation.ocl("""
                let resultExprs : Sequence(Expression) =
                    ownedFeatureMembership->
                    selectByKind(ResultExpressionMembership).
                    ownedResultExpression in
                if resultExprs->isEmpty() then Sequence{}
                else resultExprs->first().evaluate(target)
                endif
            """.trimIndent()),
            description = "If this Expression isModelLevelEvaluable, then evaluate it using the target as the contextElement for resolving Feature names and testing classification. The result is a collection of Elements, which, for a fully evaluable Expression, will be a LiteralExpression or a Feature that is not an Expression."
        ),
        MetaOperation(
            name = "modelLevelEvaluable",
            returnType = "Boolean",
            parameters = listOf(
                MetaParameter(name = "visited", type = "Feature", lowerBound = 0, upperBound = -1)
            ),
            body = MetaOperation.ocl("""
                ownedSpecialization->forAll(isImplied) and
                ownedFeature->forAll(f |
                    (directionOf(f) = FeatureDirectionKind::_'in' or f = result) and
                    f.ownedFeature->isEmpty() and f.valuation = null or
                    f.owningFeatureMembership.oclIsKindOf(ResultExpressionMembership) and
                    f.oclAsType(Expression).modelLevelEvaluable(visited)
                )
            """.trimIndent()),
            description = "Return whether this Expression is model-level evaluable. The visited parameter is used to track possible circular Feature references made from FeatureReferenceExpressions. Such circular references are not allowed in model-level evaluable expressions."
        )
    ),
    description = "A step that represents an expression"
)
