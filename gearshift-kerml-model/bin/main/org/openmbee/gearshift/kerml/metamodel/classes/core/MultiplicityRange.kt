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
package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.MetaOperation
import org.openmbee.mdm.framework.meta.MetaParameter

/**
 * KerML MultiplicityRange metaclass.
 * Specializes: Multiplicity
 * A MultiplicityRange is a Multiplicity whose value is defined to be the (inclusive) range of natural
 * numbers given by the result of a lowerBound Expression and the result of an upperBound Expression.
 */
fun createMultiplicityRangeMetaClass() = MetaClass(
    name = "MultiplicityRange",
    isAbstract = false,
    superclasses = listOf("Multiplicity"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkMultiplicityRangeExpressionTypeFeaturing",
            type = ConstraintType.VERIFICATION,
            expression = "bound->forAll(b | b.featuringType = self.featuringType)",
            description = "The bounds of a MultiplicityRange must have the same featuringTypes as the MultiplicityRange."
        ),
        MetaConstraint(
            name = "deriveMultiplicityRangeBound",
            type = ConstraintType.DERIVATION,
            expression = "if upperBound = null then Sequence{} else if lowerBound = null then Sequence{upperBound} else Sequence{lowerBound, upperBound} endif endif",
            description = "The bounds of a MultiplicityRange are the lowerBound (if any) followed by the upperBound."
        ),
        MetaConstraint(
            name = "deriveMultiplicityRangeLowerBound",
            type = ConstraintType.DERIVATION,
            expression = "let ownedExpressions : Sequence(Expression) = ownedMember->selectByKind(Expression) in if ownedExpressions->size() < 2 then null else ownedExpressions->first() endif",
            description = "If a MultiplicityRange has two ownedMembers that are Expressions, then the lowerBound is the first of these, otherwise it is null."
        ),
        MetaConstraint(
            name = "deriveMultiplicityRangeUpperBound",
            type = ConstraintType.DERIVATION,
            expression = "let ownedExpressions : Sequence(Expression) = ownedMember->selectByKind(Expression) in if ownedExpressions->isEmpty() then null else if ownedExpressions->size() = 1 then ownedExpressions->at(1) else ownedExpressions->at(2) endif endif",
            description = "If a MultiplicityRange has one ownedMember that is an Expression, then this is the upperBound. If it has more than one ownedMember that is an Expression, then the upperBound is the second of those. Otherwise, it is null."
        ),
        MetaConstraint(
            name = "validateMultiplicityRangeBoundResultTypes",
            type = ConstraintType.VERIFICATION,
            expression = "bound->forAll(b | b.result.specializesFromLibrary('ScalarValues::Integer') and let value : UnlimitedNatural = valueOf(b) in value <> null implies value >= 0)",
            description = "The results of the bound Expression(s) of a MultiplicityRange must be typed by ScalarValues::Integer from the Kernel Data Types Library. If a bound is model-level evaluable, then it must evaluate to a non-negative value."
        ),
        MetaConstraint(
            name = "validateMultiplicityRangeBounds",
            type = ConstraintType.VERIFICATION,
            expression = "if lowerBound = null then ownedMember->notEmpty() and ownedMember->at(1) = upperBound else ownedMember->size() > 1 and ownedMember->at(1) = lowerBound and ownedMember->at(2) = upperBound endif",
            description = "The lowerBound (if any) and upperBound Expressions must be the first ownedMembers of a MultiplicityRange."
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "hasBounds",
            returnType = "Boolean",
            parameters = listOf(
                MetaParameter(name = "lower", type = "Integer"),
                MetaParameter(name = "upper", type = "UnlimitedNatural")
            ),
            body = MetaOperation.ocl("valueOf(upperBound) = upper and let lowerValue: UnlimitedNatural = valueOf(lowerBound) in (lowerValue = lower or lowerValue = null and (lower = upper or lower = 0 and upper = *))"),
            description = "Check whether this MultiplicityRange represents the range bounded by the given values lower and upper, presuming the lowerBound and upperBound Expressions are model-level evaluable."
        ),
        MetaOperation(
            name = "valueOf",
            returnType = "UnlimitedNatural",
            returnLowerBound = 0,
            returnUpperBound = 1,
            parameters = listOf(
                MetaParameter(name = "bound", type = "Expression", lowerBound = 0, upperBound = 1)
            ),
            body = MetaOperation.ocl("if bound = null or not bound.isModelLevelEvaluable then null else let boundEval: Sequence(Element) = bound.evaluate(owningType) in if boundEval->size() <> 1 then null else let valueEval: Element = boundEval->at(1) in if valueEval.oclIsKindOf(LiteralInfinity) then * else if valueEval.oclIsKindOf(LiteralInteger) then let value : Integer = valueEval.oclAsKindOf(LiteralInteger).value in if value >= 0 then value else null endif else null endif endif endif endif"),
            description = "Evaluate the given bound Expression (at model level) and return the result represented as a MOF UnlimitedNatural value."
        )
    ),
    description = "A MultiplicityRange is a Multiplicity whose value is defined to be the (inclusive) range of natural numbers given by the result of a lowerBound Expression and the result of an upperBound Expression."
)
