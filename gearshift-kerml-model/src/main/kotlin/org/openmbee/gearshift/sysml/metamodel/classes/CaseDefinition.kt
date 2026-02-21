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
import org.openmbee.mdm.framework.meta.SemanticBinding

/**
 * SysML CaseDefinition metaclass.
 * Specializes: CalculationDefinition
 * A CaseDefinition is a CalculationDefinition for a process, often involving collecting evidence or data,
 * relative to a subject, possibly involving the collaboration of one or more other actors, producing a result
 * that meets an objective.
 */
fun createCaseDefinitionMetaClass() = MetaClass(
    name = "CaseDefinition",
    isAbstract = false,
    superclasses = listOf("CalculationDefinition"),
    constraints = listOf(
        MetaConstraint(
            name = "checkCaseDefinitionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Cases::Case')",
            description = "A CaseDefinition must directly or indirectly specialize the base CaseDefinition Cases::Case from the Systems Model Library."
        ),
        MetaConstraint(
            name = "deriveCaseDefinitionActorParameter",
            type = ConstraintType.DERIVATION,
            expression = """
                featureMembership->
                    selectByKind(ActorMembership).
                    ownedActorParameter
            """.trimIndent(),
            description = "The actorParameters of a CaseDefinition are the ownedActorParameters of the ActorMemberships of the CaseDefinition."
        ),
        MetaConstraint(
            name = "deriveCaseDefinitionObjectiveRequirement",
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
            description = "The objectiveRequirement of a CaseDefinition is the ownedObjectiveRequirement of its ObjectiveMembership, if any."
        ),
        MetaConstraint(
            name = "deriveCaseDefinitionSubjectParameter",
            type = ConstraintType.DERIVATION,
            expression = """
                let subjectMems : OrderedSet(SubjectMembership) =
                    featureMembership->selectByKind(SubjectMembership) in
                if subjectMems->isEmpty() then null
                else subjectMems->first().ownedSubjectParameter
                endif
            """.trimIndent(),
            description = "The subjectParameter of a CaseDefinition is the ownedSubjectParameter of its SubjectMembership (if any)."
        ),
        MetaConstraint(
            name = "validateCaseDefinitionOnlyOneObjective",
            type = ConstraintType.VERIFICATION,
            expression = """
                featureMembership->
                    selectByKind(ObjectiveMembership)->
                    size() <= 1
            """.trimIndent(),
            description = "A CaseDefinition must have at most one featureMembership that is a ObjectiveMembership."
        ),
        MetaConstraint(
            name = "validateCaseDefinitionOnlyOneSubject",
            type = ConstraintType.VERIFICATION,
            expression = "featureMembership->selectByKind(SubjectMembership)->size() <= 1",
            description = "A CaseDefinition must have at most one featureMembership that is a SubjectMembership."
        ),
        MetaConstraint(
            name = "validateCaseDefinitionSubjectParameterPosition",
            type = ConstraintType.VERIFICATION,
            expression = "input->notEmpty() and input->first() = subjectParameter",
            description = "The subjectParameter of a CaseDefinition must be its first input."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "caseDefinitionCaseBinding",
            baseConcept = "Cases::Case",
            bindingKind = BindingKind.SPECIALIZES
        )
    ),
    description = "A CaseDefinition is a CalculationDefinition for a process, often involving collecting evidence or data, relative to a subject, possibly involving the collaboration of one or more other actors, producing a result that meets an objective."
)
