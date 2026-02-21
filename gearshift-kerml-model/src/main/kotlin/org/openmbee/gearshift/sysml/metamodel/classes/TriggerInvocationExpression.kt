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
package org.openmbee.gearshift.sysml.metamodel.classes

import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.MetaOperation
import org.openmbee.mdm.framework.meta.MetaProperty

/**
 * SysML TriggerInvocationExpression metaclass.
 * Specializes: InvocationExpression
 * A TriggerInvocationExpression is an InvocationExpression that invokes one of the trigger Functions
 * from the Kernel Semantic Library Triggers package, as indicated by its kind.
 */
fun createTriggerInvocationExpressionMetaClass() = MetaClass(
    name = "TriggerInvocationExpression",
    isAbstract = false,
    superclasses = listOf("InvocationExpression"),
    attributes = listOf(
        MetaProperty(
            name = "kind",
            type = "TriggerKind",
            lowerBound = 1,
            upperBound = 1,
            description = "Indicates which of the Functions from the Triggers model in the Kernel Semantic Library is to be invoked by this TriggerInvocationExpression."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "validateTriggerInvocationExpressionAfterArgument",
            type = ConstraintType.VERIFICATION,
            expression = """
                kind = TriggerKind::after implies
                argument->notEmpty() and
                argument->at(1).result.specializesFromLibrary('Quantities::ScalarQuantityValue') and
                let mRef : Element =
                    resolveGlobal('Quantities::TensorQuantityValue::mRef').ownedMemberElement in
                argument->at(1).result.feature->
                    select(ownedRedefinition.redefinedFeature->
                        closure(ownedRedefinition.redefinedFeature)->
                        includes(mRef))->
                    exists(specializesFromLibrary('ISQBase::DurationUnit'))
            """.trimIndent(),
            description = "If a TriggerInvocationExpression has kind = after, then it must have an argument Expression with a result that conforms to Quantities::ScalarQuantityValue and a feature that redefines Quantities::TensorQuantityValue::mRef and specializes ISQBase::DurationUnit."
        ),
        MetaConstraint(
            name = "validateTriggerInvocationExpressionAtArgument",
            type = ConstraintType.VERIFICATION,
            expression = """
                kind = TriggerKind::at implies
                argument->notEmpty() and
                argument->at(1).result.specializesFromLibrary('Time::TimeInstantValue')
            """.trimIndent(),
            description = "If a TriggerInvocationExpression has kind = at, then it must have an argument Expression with a result that conforms to Time::TimeInstantValue."
        ),
        MetaConstraint(
            name = "validateTriggerInvocationExpressionWhenArgument",
            type = ConstraintType.VERIFICATION,
            expression = """
                kind = TriggerKind::when implies
                argument->notEmpty() and
                argument->at(1).oclIsKindOf(FeatureReferenceExpression) and
                let referent : Feature =
                    argument->at(1).oclAsType(FeatureReferenceExpression).referent in
                referent.oclIsKindOf(Expression) and
                referent.oclAsType(Expression).result.specializesFromLibrary('ScalarValues::Boolean')
            """.trimIndent(),
            description = "If a TriggerInvocationExpression has kind = when, then it must have an argument that is a FeatureReferenceExpression whose referent is an Expression with a result that conforms to ScalarValues::Boolean."
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "instantiatedType",
            returnType = "Type",
            returnLowerBound = 1,
            returnUpperBound = 1,
            redefines = "instantiatedType",
            body = MetaOperation.ocl("""
                resolveGlobal(
                    if kind = TriggerKind::when then
                        'Triggers::TriggerWhen'
                    else if kind = TriggerKind::at then
                        'Triggers::TriggerAt'
                    else
                        'Triggers::TriggerAfter'
                    endif endif
                ).memberElement.oclAsType(Type)
            """.trimIndent()),
            description = "Return one of the Functions TriggerWhen, TriggerAt or TriggerAfter, from the Kernel Semantic Library Triggers package, depending on the kind."
        )
    ),
    description = "A TriggerInvocationExpression is an InvocationExpression that invokes one of the trigger Functions from the Kernel Semantic Library Triggers package."
)
