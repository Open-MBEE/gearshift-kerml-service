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
 * SysML CaseUsage metaclass.
 * Specializes: CalculationUsage
 * A CaseUsage is a Usage of a CaseDefinition.
 */
fun createCaseUsageMetaClass() = MetaClass(
    name = "CaseUsage",
    isAbstract = false,
    superclasses = listOf("CalculationUsage"),
    constraints = listOf(
        MetaConstraint(
            name = "checkCaseUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Cases::cases')",
            description = "A CaseUsage must directly or indirectly specialize the base CaseUsage Cases::cases from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkCaseUsageSubcaseSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isComposite and owningType <> null and
                (owningType.oclIsKindOf(CaseDefinition) or
                owningType.oclIsKindOf(CaseUsage)) implies
                specializesFromLibrary('Cases::Case::subcases')
            """.trimIndent(),
            description = "A composite CaseUsage whose owningType is a CaseDefinition or CaseUsage must directly or indirectly specialize the CaseUsage Cases::Case::subcases."
        ),
        MetaConstraint(
            name = "deriveCaseUsageActorParameter",
            type = ConstraintType.DERIVATION,
            expression = """
                featureMembership->
                    selectByKind(ActorMembership).
                    ownedActorParameter
            """.trimIndent(),
            description = "The actorParameters of a CaseUsage are the ownedActorParameters of the ActorMemberships of the CaseUsage."
        ),
        MetaConstraint(
            name = "deriveCaseUsageObjectiveRequirement",
            type = ConstraintType.DERIVATION,
            expression = """
                let objectives : OrderedSet(RequirementUsage) =
                    featureMembership->
                        selectByKind(ObjectiveMembership).
                        ownedObjectiveRequirement in
                if objectives->isEmpty() then null
                else objectives->first()
                endif
            """.trimIndent(),
            description = "The objectiveRequirement of a CaseUsage is the RequirementUsage it owns via an ObjectiveMembership, if any."
        ),
        MetaConstraint(
            name = "deriveCaseUsageSubjectParameter",
            type = ConstraintType.DERIVATION,
            expression = """
                let subjects : OrderedSet(SubjectMembership) =
                    featureMembership->selectByKind(SubjectMembership) in
                if subjects->isEmpty() then null
                else subjects->first().ownedSubjectParameter
                endif
            """.trimIndent(),
            description = "The subjectParameter of a CaseUsage is the ownedSubjectParameter of its SubjectMembership (if any)."
        ),
        MetaConstraint(
            name = "validateCaseUsageOnlyOneObjective",
            type = ConstraintType.VERIFICATION,
            expression = """
                featureMembership->
                    selectByKind(ObjectiveMembership)->
                    size() <= 1
            """.trimIndent(),
            description = "A CaseUsage must have at most one featureMembership that is a ObjectiveMembership."
        ),
        MetaConstraint(
            name = "validateCaseUsageOnlyOneSubject",
            type = ConstraintType.VERIFICATION,
            expression = """
                featureMembership->
                    selectByKind(SubjectMembership)->
                    size() <= 1
            """.trimIndent(),
            description = "A CaseUsage must have at most one featureMembership that is a SubjectMembership."
        ),
        MetaConstraint(
            name = "validateCaseUsageSubjectParameterPosition",
            type = ConstraintType.VERIFICATION,
            expression = "input->notEmpty() and input->first() = subjectParameter",
            description = "The subjectParameter of a CaseUsage must be its first input."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "caseUsageCasesBinding",
            baseConcept = "Cases::cases",
            bindingKind = BindingKind.SUBSETS
        ),
        SemanticBinding(
            name = "caseUsageSubcaseBinding",
            baseConcept = "Cases::Case::subcases",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.And(listOf(
                BindingCondition.IsComposite,
                BindingCondition.Or(listOf(
                    BindingCondition.OwningTypeIs("CaseDefinition"),
                    BindingCondition.OwningTypeIs("CaseUsage")
                ))
            ))
        )
    ),
    description = "A CaseUsage is a Usage of a CaseDefinition."
)
