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

import org.openmbee.mdm.framework.meta.BindingKind
import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.MetaProperty
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * SysML StateDefinition metaclass.
 * Specializes: ActionDefinition
 * A StateDefinition is the Definition of the Behavior of a system or part of a system in a certain state
 * condition. A StateDefinition may be related to up to three of its ownedFeatures by StateSubactionMembership
 * Relationships, all of different kinds, corresponding to the entry, do and exit actions of the StateDefinition.
 */
fun createStateDefinitionMetaClass() = MetaClass(
    name = "StateDefinition",
    isAbstract = false,
    superclasses = listOf("ActionDefinition"),
    attributes = listOf(
        MetaProperty(
            name = "isParallel",
            type = "Boolean",
            lowerBound = 1,
            upperBound = 1,
            description = "Whether the ownedStates of this StateDefinition are to all be performed in parallel."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "checkStateDefinitionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('States::StateAction')",
            description = "A StateDefinition must directly or indirectly specialize the StateDefinition States::StateAction from the Systems Model Library."
        ),
        MetaConstraint(
            name = "deriveStateDefinitionDoAction",
            type = ConstraintType.DERIVATION,
            expression = """
                let doMemberships : Sequence(StateSubactionMembership) =
                    ownedMembership->
                        selectByKind(StateSubactionMembership)->
                        select(kind = StateSubactionKind::do) in
                if doMemberships->isEmpty() then null
                else doMemberships->at(1)
                endif
            """.trimIndent(),
            description = "The doAction of a StateDefinition is the action of the owned StateSubactionMembership with kind = do."
        ),
        MetaConstraint(
            name = "deriveStateDefinitionEntryAction",
            type = ConstraintType.DERIVATION,
            expression = """
                let entryMemberships : Sequence(StateSubactionMembership) =
                    ownedMembership->
                        selectByKind(StateSubactionMembership)->
                        select(kind = StateSubactionKind::entry) in
                if entryMemberships->isEmpty() then null
                else entryMemberships->at(1)
                endif
            """.trimIndent(),
            description = "The entryAction of a StateDefinition is the action of the owned StateSubactionMembership with kind = entry."
        ),
        MetaConstraint(
            name = "deriveStateDefinitionExitAction",
            type = ConstraintType.DERIVATION,
            expression = """
                let exitMemberships : Sequence(StateSubactionMembership) =
                    ownedMembership->
                        selectByKind(StateSubactionMembership)->
                        select(kind = StateSubactionKind::exit) in
                if exitMemberships->isEmpty() then null
                else exitMemberships->at(1)
                endif
            """.trimIndent(),
            description = "The exitAction of a StateDefinition is the action of the owned StateSubactionMembership with kind = exit."
        ),
        MetaConstraint(
            name = "deriveStateDefinitionState",
            type = ConstraintType.DERIVATION,
            expression = "action->selectByKind(StateUsage)",
            description = "The states of a StateDefinition are those of its actions that are StateUsages."
        ),
        MetaConstraint(
            name = "validateStateDefinitionParallelSubactions",
            type = ConstraintType.VERIFICATION,
            expression = """
                isParallel implies
                ownedAction.incomingTransition->isEmpty() and
                ownedAction.outgoingTransition->isEmpty()
            """.trimIndent(),
            description = "If a StateDefinition is parallel, then its ownedActions must not have any incoming or outgoing Transitions."
        ),
        MetaConstraint(
            name = "validateStateDefinitionStateSubactionKind",
            type = ConstraintType.VERIFICATION,
            expression = """
                ownedMembership->
                    selectByKind(StateSubactionMembership)->
                    isUnique(kind)
            """.trimIndent(),
            description = "A StateDefinition must not have more than one owned StateSubactionMembership of each kind."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "stateDefinitionStateActionBinding",
            baseConcept = "States::StateAction",
            bindingKind = BindingKind.SPECIALIZES
        )
    ),
    description = "A StateDefinition is the Definition of the Behavior of a system or part of a system in a certain state condition."
)
