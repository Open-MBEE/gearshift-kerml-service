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
 * KerML FeatureReferenceExpression metaclass.
 * Specializes: Expression
 * A FeatureReferenceExpression is an Expression whose result is bound to a referent Feature.
 */
fun createFeatureReferenceExpressionMetaClass() = MetaClass(
    name = "FeatureReferenceExpression",
    isAbstract = false,
    superclasses = listOf("Expression"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkFeatureReferenceExpressionBindingConnector",
            type = ConstraintType.VERIFICATION,
            expression = """
                ownedMember->selectByKind(BindingConnector)->exists(b |
                    b.relatedFeatures->includes(targetFeature) and
                    b.relatedFeatures->includes(result))
            """.trimIndent(),
            description = "A FeatureReferenceExpression must have an ownedMember that is a BindingConnector between the referent and result of the FeatureReferenceExpression."
        ),
        MetaConstraint(
            name = "checkFeatureReferenceExpressionResultSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "result.owningType() = self and result.specializes(referent)",
            description = "The result parameter of a FeatureReferenceExpression must specialize the referent of the FeatureReferenceExpression."
        ),
        MetaConstraint(
            name = "deriveFeatureReferenceExpressionReferent",
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
            description = "The referent of a FeatureReferenceExpression is the memberElement of its first ownedMembership that is not a ParameterMembership."
        ),
        MetaConstraint(
            name = "validateFeatureReferenceExpressionReferentIsFeature",
            type = ConstraintType.VERIFICATION,
            expression = """
                let membership : Membership =
                    ownedMembership->reject(m | m.oclIsKindOf(ParameterMembership)) in
                membership->notEmpty() and
                membership->at(1).memberElement.oclIsKindOf(Feature)
            """.trimIndent(),
            description = "The first ownedMembership of a FeatureReferenceExpression that is not a ParameterMembership must have a Feature as its memberElement."
        ),
        MetaConstraint(
            name = "validateFeatureReferenceExpressionResult",
            type = ConstraintType.VERIFICATION,
            expression = "result.owningType = self",
            description = "A FeatureReferenceExpression must own its result parameter."
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
            body = """
                if not target.oclIsKindOf(Type) then Sequence{}
                else
                    let feature: Sequence(Feature) =
                        target.oclAsType(Type).feature->select(f |
                            f.ownedRedefinition.redefinedFeature->
                            includes(referent)) in
                    if feature->notEmpty() then
                        feature.valuation.value.evaluate(target)
                    else if referent.featuringType->isEmpty()
                        then referent
                    else Sequence{}
                    endif endif
                endif
            """.trimIndent(),
            description = "Evaluate the FeatureReferenceExpression on the target element."
        ),
        MetaOperation(
            name = "modelLevelEvaluable",
            returnType = "Boolean",
            parameters = listOf(
                MetaParameter(name = "visited", type = "Feature", lowerBound = 0, upperBound = -1)
            ),
            redefines = "modelLevelEvaluable",
            body = """
                referent.conformsTo('Anything::self') or
                visited->excludes(referent) and
                (referent.oclIsKindOf(Expression) and
                    referent.oclAsType(Expression).modelLevelEvaluable(visited->including(referent)) or
                referent.owningType <> null and
                    (referent.owningType.oclIsKindOf(Metaclass) or
                    referent.owningType.oclIsKindOf(MetadataFeature)) or
                referent.featuringType->isEmpty() and
                    (referent.valuation = null or
                    referent.valuation.modelLevelEvaluable(visited->including(referent))))
            """.trimIndent(),
            description = "A FeatureReferenceExpression is model-level evaluable based on specific conditions related to its referent."
        )
    ),
    description = "A FeatureReferenceExpression is an Expression whose result is bound to a referent Feature."
)
