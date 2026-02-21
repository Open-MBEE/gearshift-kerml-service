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
 * SysML ConstraintUsage metaclass.
 * Specializes: BooleanExpression, OccurrenceUsage
 * A ConstraintUsage is an OccurrenceUsage that is also a BooleanExpression, and, so, is typed by a
 * Predicate. Nominally, if the type is a ConstraintDefinition, a ConstraintUsage is a Usage of that
 * ConstraintDefinition. However, other kinds of kernel Predicates are also allowed, to permit use of
 * Predicates from the Kernel Model Libraries.
 */
fun createConstraintUsageMetaClass() = MetaClass(
    name = "ConstraintUsage",
    isAbstract = false,
    superclasses = listOf("BooleanExpression", "OccurrenceUsage"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkConstraintUsageCheckedConstraintSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningType <> null and
                (owningType.oclIsKindOf(ItemDefinition) or
                owningType.oclIsKindOf(ItemUsage)) implies
                specializesFromLibrary('Items::Item::checkedConstraints')
            """.trimIndent(),
            description = "A ConstraintUsage whose owningType is an ItemDefinition or ItemUsage must directly or indirectly specialize the ConstraintUsage Items::Item::checkedConstraints."
        ),
        MetaConstraint(
            name = "checkConstraintUsageRequirementConstraintSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningFeatureMembership <> null and
                owningFeatureMembership.oclIsKindOf(RequirementConstraintMembership) implies
                if owningFeatureMembership.oclAsType(RequirementConstraintMembership).kind =
                    RequirementConstraintKind::assumption then
                    specializesFromLibrary('Requirements::RequirementCheck::assumptions')
                else
                    specializesFromLibrary('Requirements::RequirementCheck::constraints')
                endif
            """.trimIndent(),
            description = "A ConstraintUsage whose owningFeatureMembership is a RequirementConstraintMembership must specialize assumptions or constraints from Requirements::RequirementCheck, depending on the kind."
        ),
        MetaConstraint(
            name = "checkConstraintUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Constraints::constraintChecks')",
            description = "A ConstraintUsage must directly or indirectly specialize the base ConstraintUsage Constraints::constraintChecks from the Systems Model Library."
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "modelLevelEvaluable",
            returnType = "Boolean",
            returnLowerBound = 1,
            returnUpperBound = 1,
            redefines = "modelLevelEvaluable",
            parameters = listOf(
                MetaOperation.Parameter("visited", "Feature", upperBound = -1)
            ),
            body = MetaOperation.ocl("false"),
            description = "A ConstraintUsage is not model-level evaluable."
        ),
        MetaOperation(
            name = "namingFeature",
            returnType = "Feature",
            returnLowerBound = 0,
            returnUpperBound = 1,
            redefines = "namingFeature",
            body = MetaOperation.ocl("""
                if owningFeatureMembership <> null and
                    owningFeatureMembership.oclIsKindOf(RequirementConstraintMembership) and
                    ownedReferenceSubsetting <> null then
                    ownedReferenceSubsetting.referencedFeature.featureTarget
                else
                    self.oclAsType(OccurrenceUsage).namingFeature()
                endif
            """.trimIndent()),
            description = "The namingFeature of a ConstraintUsage owned by a RequirementConstraintMembership with an ownedReferenceSubsetting is the featureTarget of the referencedFeature."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "constraintUsageConstraintChecksBinding",
            baseConcept = "Constraints::constraintChecks",
            bindingKind = BindingKind.SUBSETS
        ),
        SemanticBinding(
            name = "constraintUsageCheckedConstraintsBinding",
            baseConcept = "Items::Item::checkedConstraints",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.Or(
                listOf(
                    BindingCondition.OwningTypeIs("ItemDefinition"),
                    BindingCondition.OwningTypeIs("ItemUsage")
                )
            )
        )
    ),
    description = "A ConstraintUsage is an OccurrenceUsage that is also a BooleanExpression, typed by a Predicate."
)
