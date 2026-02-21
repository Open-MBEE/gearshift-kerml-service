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
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * SysML SendActionUsage metaclass.
 * Specializes: ActionUsage
 * A SendActionUsage is an ActionUsage that specifies the sending of a payload given by the result of its
 * payloadArgument Expression via a MessageTransfer whose source is given by the result of the
 * senderArgument Expression and whose target is given by the result of the receiverArgument Expression.
 */
fun createSendActionUsageMetaClass() = MetaClass(
    name = "SendActionUsage",
    isAbstract = false,
    superclasses = listOf("ActionUsage"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkSendActionUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Actions::sendActions')",
            description = "A SendActionUsage must directly or indirectly specialize the ActionUsage Actions::sendActions from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkSendActionUsageSubactionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isSubactionUsage() implies
                specializesFromLibrary('Actions::Action::sendSubactions')
            """.trimIndent(),
            description = "A composite SendActionUsage that is a subaction must directly or indirectly specialize the ActionUsage Actions::Action::sendSubactions from the Systems Model Library."
        ),
        MetaConstraint(
            name = "deriveSendActionUsagePayloadArgument",
            type = ConstraintType.DERIVATION,
            expression = "argument(1)",
            description = "The payloadArgument of a SendActionUsage is its first argument Expression."
        ),
        MetaConstraint(
            name = "deriveSendActionUsageReceiverArgument",
            type = ConstraintType.DERIVATION,
            expression = "argument(3)",
            description = "The receiverArgument of a SendActionUsage is its third argument Expression."
        ),
        MetaConstraint(
            name = "deriveSendActionUsageSenderArgument",
            type = ConstraintType.DERIVATION,
            expression = "argument(2)",
            description = "The senderArgument of a SendActionUsage is its second argument Expression."
        ),
        MetaConstraint(
            name = "validateSendActionParameters",
            type = ConstraintType.VERIFICATION,
            expression = "inputParameters()->size() >= 3",
            description = "A SendActionUsage must have at least three owned input parameters, corresponding to its payload, sender and receiver, respectively."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "sendActionUsageSendActionsBinding",
            baseConcept = "Actions::sendActions",
            bindingKind = BindingKind.SUBSETS
        ),
        SemanticBinding(
            name = "sendActionUsageSendSubactionsBinding",
            baseConcept = "Actions::Action::sendSubactions",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.OperationResult("isSubactionUsage")
        )
    ),
    description = "A SendActionUsage is an ActionUsage that specifies the sending of a payload via a MessageTransfer."
)
