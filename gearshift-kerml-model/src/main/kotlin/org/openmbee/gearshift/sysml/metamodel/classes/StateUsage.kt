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
import org.openmbee.mdm.framework.meta.MetaProperty
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * SysML StateUsage metaclass.
 * Specializes: ActionUsage
 * A StateUsage is an ActionUsage that is nominally the Usage of a StateDefinition. However, other kinds of
 * kernel Behaviors are also allowed as types, to permit use of Behaviors from the Kernel Model Libraries.
 * A StateUsage may be related to up to three of its ownedFeatures by StateSubactionMembership
 * Relationships, all of different kinds, corresponding to the entry, do and exit actions of the StateUsage.
 */
fun createStateUsageMetaClass() = MetaClass(
    name = "StateUsage",
    isAbstract = false,
    superclasses = listOf("ActionUsage"),
    attributes = listOf(
        MetaProperty(
            name = "isParallel",
            type = "Boolean",
            lowerBound = 1,
            upperBound = 1,
            description = "Whether the nestedStates of this StateUsage are to all be performed in parallel."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "checkStateUsageExclusiveStateSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isSubstateUsage(false) implies
                specializesFromLibrary('States::StateAction::exclusiveStates')
            """.trimIndent(),
            description = "A StateUsage that is a substate usage with a non-parallel owning StateDefinition or StateUsage must directly or indirectly specialize the StateUsage States::StateAction::exclusiveStates from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkStateUsageOwnedStateSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isComposite and owningType <> null and
                (owningType.oclIsKindOf(PartDefinition) or
                owningType.oclIsKindOf(PartUsage)) implies
                specializesFromLibrary('Parts::Part::ownedStates')
            """.trimIndent(),
            description = "A composite StateUsage whose owningType is a PartDefinition or PartUsage must directly or indirectly specialize the StateUsage Parts::Part::ownedStates from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkStateUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('States::stateActions')",
            description = "A StateUsage must directly or indirectly specialize the StateUsage States::stateActions from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkStateUsageSubstateSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isSubstateUsage(true) implies
                specializesFromLibrary('States::StateAction::substates')
            """.trimIndent(),
            description = "A StateUsage that is a substate usage with a owning StateDefinition or StateUsage that is parallel must directly or indirectly specialize the StateUsage States::StateAction::substates from the Systems Model Library."
        ),
        MetaConstraint(
            name = "deriveStateUsageDoAction",
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
            description = "The doAction of a StateUsage is the action of the owned StateSubactionMembership with kind = do."
        ),
        MetaConstraint(
            name = "deriveStateUsageEntryAction",
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
            description = "The entryAction of a StateUsage is the action of the owned StateSubactionMembership with kind = entry."
        ),
        MetaConstraint(
            name = "deriveStateUsageExitAction",
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
            description = "The exitAction of a StateUsage is the action of the owned StateSubactionMembership with kind = exit."
        ),
        MetaConstraint(
            name = "validateStateUsageParallelSubactions",
            type = ConstraintType.VERIFICATION,
            expression = """
                isParallel implies
                nestedAction.incomingTransition->isEmpty() and
                nestedAction.outgoingTransition->isEmpty()
            """.trimIndent(),
            description = "If a StateUsage is parallel, then its nestedActions must not have any incoming or outgoing Transitions."
        ),
        MetaConstraint(
            name = "validateStateUsageStateSubactionKind",
            type = ConstraintType.VERIFICATION,
            expression = """
                ownedMembership->
                    selectByKind(StateSubactionMembership)->
                    isUnique(kind)
            """.trimIndent(),
            description = "A StateUsage must not have more than one owned StateSubactionMembership of each kind."
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "isSubstateUsage",
            returnType = "Boolean",
            returnLowerBound = 1,
            returnUpperBound = 1,
            parameters = listOf(
                MetaOperation.Parameter("isParallel", "Boolean")
            ),
            body = MetaOperation.ocl("""
                isComposite and owningType <> null and
                (owningType.oclIsKindOf(StateDefinition) and
                    owningType.oclAsType(StateDefinition).isParallel = isParallel or
                owningType.oclIsKindOf(StateUsage) and
                    owningType.oclAsType(StateUsage).isParallel = isParallel) and
                not owningFeatureMembership.oclIsKindOf(StateSubactionMembership)
            """.trimIndent()),
            description = "Check if this StateUsage is composite and has an owningType that is a StateDefinition or StateUsage with the given value of isParallel, but is not an entry, do, or exit action."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "stateUsageStateActionsBinding",
            baseConcept = "States::stateActions",
            bindingKind = BindingKind.SUBSETS
        ),
        SemanticBinding(
            name = "stateUsageOwnedStatesBinding",
            baseConcept = "Parts::Part::ownedStates",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.And(
                listOf(
                    BindingCondition.IsComposite,
                    BindingCondition.Or(
                        listOf(
                            BindingCondition.OwningTypeIs("PartDefinition"),
                            BindingCondition.OwningTypeIs("PartUsage")
                        )
                    )
                )
            )
        ),
        SemanticBinding(
            name = "stateUsageExclusiveStatesBinding",
            baseConcept = "States::StateAction::exclusiveStates",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.OperationResult("isSubstateUsage(false)")
        ),
        SemanticBinding(
            name = "stateUsageSubstatesBinding",
            baseConcept = "States::StateAction::substates",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.OperationResult("isSubstateUsage(true)")
        )
    ),
    description = "A StateUsage is an ActionUsage that is nominally the Usage of a StateDefinition."
)
