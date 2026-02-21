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
 * SysML PartUsage metaclass.
 * Specializes: ItemUsage
 * A PartUsage is a usage of a PartDefinition to represent a system or a part of a system. At least
 * one of the itemDefinitions of the PartUsage must be a PartDefinition.
 */
fun createPartUsageMetaClass() = MetaClass(
    name = "PartUsage",
    isAbstract = false,
    superclasses = listOf("ItemUsage"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkPartUsageActorSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningFeatureMembership <> null and
                owningFeatureMembership.oclIsKindOf(ActorMembership) implies
                    if owningType.oclIsKindOf(RequirementDefinition) or
                        owningType.oclIsKindOf(RequirementUsage)
                    then specializesFromLibrary('Requirements::RequirementCheck::actors')
                    else specializesFromLibrary('Cases::Case::actors')
                    endif
            """.trimIndent(),
            description = "If a PartUsage is owned via an ActorMembership, then it must specialize Requirements::RequirementCheck::actors or Cases::Case::actors depending on its owningType."
        ),
        MetaConstraint(
            name = "checkPartUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Parts::parts')",
            description = "A PartUsage must directly or indirectly specialize the PartUsage Parts::parts from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkPartUsageStakeholderSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningFeatureMembership <> null and
                owningFeatureMembership.oclIsKindOf(StakeholderMembership) implies
                specializesFromLibrary('Requirements::RequirementCheck::stakeholders')
            """.trimIndent(),
            description = "If a PartUsage is owned via a StakeholderMembership, then it must specialize Requirements::RequirementCheck::stakeholders."
        ),
        MetaConstraint(
            name = "checkPartUsageSubpartSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isComposite and owningType <> null and
                (owningType.oclIsKindOf(ItemDefinition) or
                owningType.oclIsKindOf(ItemUsage)) implies
                specializesFromLibrary('Items::Item::subparts')
            """.trimIndent(),
            description = "A composite PartUsage whose owningType is an ItemDefinition or ItemUsage must specialize Items::Item::subparts."
        ),
        MetaConstraint(
            name = "derivePartUsagePartDefinition",
            type = ConstraintType.DERIVATION,
            expression = "itemDefinition->selectByKind(PartDefinition)",
            description = "The partDefinitions of a PartUsage are those itemDefinitions that are PartDefinitions."
        ),
        MetaConstraint(
            name = "validatePartUsagePartDefinition",
            type = ConstraintType.VERIFICATION,
            expression = "partDefinition->notEmpty()",
            description = "At least one of the itemDefinitions of a PartUsage must be a PartDefinition."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "partUsagePartsBinding",
            baseConcept = "Parts::parts",
            bindingKind = BindingKind.SUBSETS
        ),
        SemanticBinding(
            name = "partUsageSubpartsBinding",
            baseConcept = "Items::Item::subparts",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.And(
                listOf(
                    BindingCondition.IsComposite,
                    BindingCondition.Or(
                        listOf(
                            BindingCondition.OwningTypeIs("ItemDefinition"),
                            BindingCondition.OwningTypeIs("ItemUsage")
                        )
                    )
                )
            )
        ),
        SemanticBinding(
            name = "partUsageActorsBinding",
            baseConcept = "Cases::Case::actors",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.HasElementOfType("owningFeatureMembership", "ActorMembership")
        ),
        SemanticBinding(
            name = "partUsageStakeholdersBinding",
            baseConcept = "Requirements::RequirementCheck::stakeholders",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.HasElementOfType("owningFeatureMembership", "StakeholderMembership")
        )
    ),
    description = "A PartUsage is a usage of a PartDefinition to represent a system or a part of a system."
)
