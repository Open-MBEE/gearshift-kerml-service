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

import org.openmbee.mdm.framework.meta.BindingCondition
import org.openmbee.mdm.framework.meta.BindingKind
import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.MetaOperation
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * SysML AcceptActionUsage metaclass.
 * Specializes: ActionUsage
 * An AcceptActionUsage is an ActionUsage that specifies the acceptance of an incoming Transfer from the
 * Occurrence given by the result of its receiverArgument Expression. (If no receiverArgument is provided,
 * the default is the this context of the AcceptActionUsage.) The payload of the accepted Transfer is output on its
 * payloadParameter. Which Transfers may be accepted is determined by conformance to the typing and
 * (potentially) binding of the payloadParameter.
 */
fun createAcceptActionUsageMetaClass() = MetaClass(
    name = "AcceptActionUsage",
    isAbstract = false,
    superclasses = listOf("ActionUsage"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkAcceptActionUsageReceiverBindingConnector",
            type = ConstraintType.VERIFICATION,
            expression = """
                payloadArgument <> null and
                payloadArgument.oclIsKindOf(TriggerInvocationExpression) implies
                let invocation : Expression =
                    payloadArgument.oclAsType(Expression) in
                parameter->size() >= 2 and
                invocation.parameter->size() >= 2 and
                ownedFeature->selectByKind(BindingConnector)->exists(b |
                    b.relatedFeatures->includes(parameter->at(2)) and
                    b.relatedFeatures->includes(invocation.parameter->at(2)))
            """.trimIndent(),
            description = "If the payloadArgument of an AcceptActionUsage is a TriggerInvocationExpression, then the AcceptActionUsage must have an ownedFeature that is a BindingConnector between its receiver parameter and the receiver parameter of the TriggerInvocationExpression."
        ),
        MetaConstraint(
            name = "checkAcceptActionUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                not isTriggerAction() implies
                specializesFromLibrary('Actions::acceptActions')
            """.trimIndent(),
            description = "An AcceptActionUsage that is not the triggerAction of a TransitionUsage must directly or indirectly specialize the ActionUsage Actions::acceptActions from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkAcceptActionUsageSubactionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isSubactionUsage() and not isTriggerAction() implies
                specializesFromLibrary('Actions::Action::acceptSubactions')
            """.trimIndent(),
            description = "A composite AcceptActionUsage that is a subaction usage, but is not the triggerAction of a TransitionUsage, must directly or indirectly specialize the ActionUsage Actions::Action::acceptSubactions from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkAcceptActionUsageTriggerActionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isTriggerAction() implies
                specializesFromLibrary('Actions::TransitionAction::accepter')
            """.trimIndent(),
            description = "An AcceptActionUsage that is the triggerAction of a TransitionUsage must directly or indirectly specialize the ActionUsage Actions::TransitionAction::accepter from the Systems Model Library."
        ),
        MetaConstraint(
            name = "deriveAcceptActionUsagePayloadArgument",
            type = ConstraintType.DERIVATION,
            expression = "argument(1)",
            description = "The payloadArgument of an AcceptActionUsage is its first argument Expression."
        ),
        MetaConstraint(
            name = "deriveAcceptActionUsagePayloadParameter",
            type = ConstraintType.DERIVATION,
            expression = """
                if parameter->isEmpty() then null
                else parameter->first() endif
            """.trimIndent(),
            description = "The payloadParameter of an AcceptActionUsage is its first parameter."
        ),
        MetaConstraint(
            name = "deriveAcceptActionUsageReceiverArgument",
            type = ConstraintType.DERIVATION,
            expression = "argument(2)",
            description = "The receiverArgument of an AcceptActionUsage is its second argument Expression."
        ),
        MetaConstraint(
            name = "validateAcceptActionUsageParameters",
            type = ConstraintType.VERIFICATION,
            expression = "inputParameters()->size() >= 2",
            description = "An AcceptActionUsage must have at least two input parameters, corresponding to its payload and receiver, respectively."
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "isTriggerAction",
            returnType = "Boolean",
            returnLowerBound = 1,
            returnUpperBound = 1,
            body = MetaOperation.ocl("""
                owningType <> null and
                owningType.oclIsKindOf(TransitionUsage) and
                owningType.oclAsType(TransitionUsage).triggerAction->includes(self)
            """.trimIndent()),
            description = "Check if this AcceptActionUsage is the triggerAction of a TransitionUsage."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "acceptActionUsageAcceptActionsBinding",
            baseConcept = "Actions::acceptActions",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.Not(BindingCondition.OperationResult("isTriggerAction"))
        ),
        SemanticBinding(
            name = "acceptActionUsageAcceptSubactionsBinding",
            baseConcept = "Actions::Action::acceptSubactions",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.And(
                listOf(
                    BindingCondition.OperationResult("isSubactionUsage"),
                    BindingCondition.Not(BindingCondition.OperationResult("isTriggerAction"))
                )
            )
        ),
        SemanticBinding(
            name = "acceptActionUsageTriggerActionBinding",
            baseConcept = "Actions::TransitionAction::accepter",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.OperationResult("isTriggerAction")
        )
    ),
    description = "An AcceptActionUsage is an ActionUsage that specifies the acceptance of an incoming Transfer."
)
