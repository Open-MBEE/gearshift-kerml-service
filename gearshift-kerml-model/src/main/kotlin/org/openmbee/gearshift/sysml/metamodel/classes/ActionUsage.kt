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
 * SysML ActionUsage metaclass.
 * Specializes: OccurrenceUsage, Step
 * An ActionUsage is an OccurrenceUsage that is also a Step representing an action.
 */
fun createActionUsageMetaClass() = MetaClass(
    name = "ActionUsage",
    isAbstract = false,
    superclasses = listOf("OccurrenceUsage", "Step"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkActionUsageOwnedActionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isComposite and owningType <> null and
                (owningType.oclIsKindOf(PartDefinition) or
                owningType.oclIsKindOf(PartUsage)) implies
                specializesFromLibrary('Parts::Part::ownedActions')
            """.trimIndent(),
            description = "A composite ActionUsage whose owningType is PartDefinition or PartUsage must directly or indirectly specialize the ActionUsage Parts::Part::ownedActions from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkActionUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Actions::actions')",
            description = "An ActionUsage must directly or indirectly specialize the ActionUsage Actions::actions from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkActionUsageStateActionRedefinition",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningFeatureMembership <> null and
                owningFeatureMembership.oclIsKindOf(StateSubactionMembership) implies
                let kind : StateSubactionKind =
                    owningFeatureMembership.oclAsType(StateSubactionMembership).kind in
                if kind = StateSubactionKind::entry then
                    redefinesFromLibrary('States::StateAction::entryAction')
                else if kind = StateSubactionKind::do then
                    redefinesFromLibrary('States::StateAction::doAction')
                else
                    redefinesFromLibrary('States::StateAction::exitAction')
                endif endif
            """.trimIndent(),
            description = "An ActionUsage that is the entry, do, or exit Action of a StateDefinition or StateUsage must redefine the entryAction, doAction, or exitAction feature, respectively, of the StateDefinition States::StateAction from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkActionUsageSubactionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isSubactionUsage() implies
                specializesFromLibrary('Actions::Action::subactions')
            """.trimIndent(),
            description = "A composite ActionUsage that is a subaction usage must directly or indirectly specialize the ActionUsage Actions::Action::subactions from the Systems Model Library."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "actionUsageActionsBinding",
            baseConcept = "Actions::actions",
            bindingKind = BindingKind.SUBSETS
        ),
        SemanticBinding(
            name = "actionUsageOwnedActionsBinding",
            baseConcept = "Parts::Part::ownedActions",
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
            name = "actionUsageSubactionsBinding",
            baseConcept = "Actions::Action::subactions",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.OperationResult("isSubactionUsage")
        )
    ),
    description = "An ActionUsage is an OccurrenceUsage that is also a Step representing an action."
)
