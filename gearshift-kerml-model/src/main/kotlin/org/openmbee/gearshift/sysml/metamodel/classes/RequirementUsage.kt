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
import org.openmbee.mdm.framework.meta.MetaProperty
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * SysML RequirementUsage metaclass.
 * Specializes: ConstraintUsage
 * A RequirementUsage is a Usage of a RequirementDefinition.
 */
fun createRequirementUsageMetaClass() = MetaClass(
    name = "RequirementUsage",
    isAbstract = false,
    superclasses = listOf("ConstraintUsage"),
    attributes = listOf(
        MetaProperty(
            name = "reqId",
            type = "String",
            lowerBound = 0,
            upperBound = 1,
            redefines = "declaredShortName",
            description = "An optional modeler-specified identifier for this RequirementUsage, which is the declaredShortName for the RequirementUsage."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "checkRequirementUsageObjectiveRedefinition",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningFeatureMembership <> null and
                owningFeatureMembership.oclIsKindOf(ObjectiveMembership) implies
                owningType.ownedSpecialization.general->forAll(gen |
                    (gen.oclIsKindOf(CaseDefinition) implies
                        redefines(gen.oclAsType(CaseDefinition).objectiveRequirement)) and
                    (gen.oclIsKindOf(CaseUsage) implies
                        redefines(gen.oclAsType(CaseUsage).objectiveRequirement)))
            """.trimIndent(),
            description = "A RequirementUsage whose owningFeatureMembership is a ObjectiveMembership must redefine the objectiveRequirement of each CaseDefinition or CaseUsage that is specialized by the owningType of the RequirementUsage."
        ),
        MetaConstraint(
            name = "checkRequirementUsageRequirementVerificationSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningFeatureMembership <> null and
                owningFeatureMembership.oclIsKindOf(RequirementVerificationMembership) implies
                specializesFromLibrary('VerificationCases::VerificationCase::obj::requirementVerifications')
            """.trimIndent(),
            description = "A RequirementUsage whose owningFeatureMembership is a RequirementVerificationMembership must directly or indirectly specialize the RequirementUsage VerificationCases::VerificationCase::obj::requirementVerifications."
        ),
        MetaConstraint(
            name = "checkRequirementUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Requirements::requirementChecks')",
            description = "A RequirementUsage must directly or indirectly specialize the base RequirementUsage Requirements::requirementChecks from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkRequirementUsageSubrequirementSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isComposite and owningType <> null and
                (owningType.oclIsKindOf(RequirementDefinition) or
                owningType.oclIsKindOf(RequirementUsage)) implies
                specializesFromLibrary('Requirements::RequirementCheck::subrequirements')
            """.trimIndent(),
            description = "A composite RequirementUsage whose owningType is a RequirementDefinition or RequirementUsage must directly or indirectly specialize the RequirementUsage Requirements::RequirementCheck::subrequirements from the Systems Model Library."
        ),
        MetaConstraint(
            name = "deriveRequirementUsageActorParameter",
            type = ConstraintType.DERIVATION,
            expression = """
                featureMembership->
                    selectByKind(ActorMembership).
                    ownedActorParameter
            """.trimIndent(),
            description = "The actorParameters of a RequirementUsage are the ownedActorParameters of the ActorMemberships of the RequirementUsage."
        ),
        MetaConstraint(
            name = "deriveRequirementUsageAssumedConstraint",
            type = ConstraintType.DERIVATION,
            expression = """
                ownedFeatureMembership->
                    selectByKind(RequirementConstraintMembership)->
                    select(kind = RequirementConstraintKind::assumption).
                    ownedConstraint
            """.trimIndent(),
            description = "The assumedConstraints of a RequirementUsage are the ownedConstraints of the RequirementConstraintMemberships with kind = assumption."
        ),
        MetaConstraint(
            name = "deriveRequirementUsageFramedConcern",
            type = ConstraintType.DERIVATION,
            expression = """
                featureMembership->
                    selectByKind(FramedConcernMembership).
                    ownedConcern
            """.trimIndent(),
            description = "The framedConcerns of a RequirementUsage are the ownedConcerns of the FramedConcernMemberships of the RequirementUsage."
        ),
        MetaConstraint(
            name = "deriveRequirementUsageRequiredConstraint",
            type = ConstraintType.DERIVATION,
            expression = """
                ownedFeatureMembership->
                    selectByKind(RequirementConstraintMembership)->
                    select(kind = RequirementConstraintKind::requirement).
                    ownedConstraint
            """.trimIndent(),
            description = "The requiredConstraints of a RequirementUsage are the ownedConstraints of the RequirementConstraintMemberships with kind = requirement."
        ),
        MetaConstraint(
            name = "deriveRequirementUsageStakeholderParameter",
            type = ConstraintType.DERIVATION,
            expression = """
                featureMembership->
                    selectByKind(StakeholderMembership).
                    ownedStakeholderParameter
            """.trimIndent(),
            description = "The stakeholderParameters of a RequirementUsage are the ownedStakeholderParameters of the StakeholderMemberships of the RequirementUsage."
        ),
        MetaConstraint(
            name = "deriveRequirementUsageSubjectParameter",
            type = ConstraintType.DERIVATION,
            expression = """
                let subjects : OrderedSet(SubjectMembership) =
                    featureMembership->selectByKind(SubjectMembership) in
                if subjects->isEmpty() then null
                else subjects->first().ownedSubjectParameter
                endif
            """.trimIndent(),
            description = "The subjectParameter of a RequirementUsage is the ownedSubjectParameter of its SubjectMembership (if any)."
        ),
        MetaConstraint(
            name = "deriveRequirementUsageText",
            type = ConstraintType.DERIVATION,
            expression = "documentation.body",
            description = "The texts of a RequirementUsage are the bodies of the documentation of the RequirementUsage."
        ),
        MetaConstraint(
            name = "validateRequirementUsageOnlyOneSubject",
            type = ConstraintType.VERIFICATION,
            expression = """
                featureMembership->
                    selectByKind(SubjectMembership)->
                    size() <= 1
            """.trimIndent(),
            description = "A RequirementUsage must have at most one featureMembership that is a SubjectMembership."
        ),
        MetaConstraint(
            name = "validateRequirementUsageSubjectParameterPosition",
            type = ConstraintType.VERIFICATION,
            expression = "input->notEmpty() and input->first() = subjectParameter",
            description = "The subjectParameter of a RequirementUsage must be its first input."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "requirementUsageRequirementChecksBinding",
            baseConcept = "Requirements::requirementChecks",
            bindingKind = BindingKind.SUBSETS
        ),
        SemanticBinding(
            name = "requirementUsageSubrequirementBinding",
            baseConcept = "Requirements::RequirementCheck::subrequirements",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.And(listOf(
                BindingCondition.IsComposite,
                BindingCondition.Or(listOf(
                    BindingCondition.OwningTypeIs("RequirementDefinition"),
                    BindingCondition.OwningTypeIs("RequirementUsage")
                ))
            ))
        )
    ),
    description = "A RequirementUsage is a Usage of a RequirementDefinition."
)
