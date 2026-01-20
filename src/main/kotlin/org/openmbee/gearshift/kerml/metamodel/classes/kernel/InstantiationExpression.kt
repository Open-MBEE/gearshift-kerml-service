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

/**
 * KerML InstantiationExpression metaclass.
 * Specializes: Expression
 * An InstantiationExpression is an Expression that instantiates its instantiatedType, binding some or all
 * of the features of that Type to the results of its arguments.
 * InstantiationExpression is abstract, with concrete subclasses InvocationExpression and ConstructorExpression.
 */
fun createInstantiationExpressionMetaClass() = MetaClass(
    name = "InstantiationExpression",
    isAbstract = true,
    superclasses = listOf("Expression"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "deriveInstantiationExpressionInstantiatedType",
            type = ConstraintType.DERIVATION,
            expression = "instantiatedType()",
            description = "The instantiatedType of an InstantiationExpression is given by the result of the instantiatedType() operation."
        ),
        MetaConstraint(
            name = "validateInstantiationExpressionInstantiatedType",
            type = ConstraintType.VERIFICATION,
            expression = "instantiatedType() <> null",
            description = "An InstantiationExpression must have an InstantiatedType."
        ),
        MetaConstraint(
            name = "validateInstantiationExpressionResult",
            type = ConstraintType.VERIFICATION,
            expression = "result.owningType = self",
            description = "An InstantiationExpression must own its result parameter."
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "instantiatedType",
            returnType = "Type",
            returnLowerBound = 0,
            returnUpperBound = 1,
            body = """
                let members : Sequence(Element) = ownedMembership->
                    reject(oclIsKindOf(FeatureMembership)).memberElement in
                if members->isEmpty() or not members->first().oclIsKindOf(Type) then null
                else members->first().oclAsType(Type)
                endif
            """.trimIndent(),
            description = "Return the Type to act as the instantiatedType for this InstantiationExpression. By default, this is the memberElement of the first ownedMembership that is not a FeatureMembership, which must be a Type."
        )
    ),
    description = "An InstantiationExpression is an Expression that instantiates its instantiatedType, binding some or all of the features of that Type to the results of its arguments."
)
