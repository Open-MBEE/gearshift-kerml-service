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
 * SysML RequirementDefinition metaclass.
 * Specializes: ConstraintDefinition
 * A RequirementDefinition is a ConstraintDefinition that defines a requirement used in the context of a
 * specification as a constraint that a valid solution must satisfy. The specification is relative to a specified subject,
 * possibly in collaboration with one or more external actors.
 */
fun createRequirementDefinitionMetaClass() = MetaClass(
    name = "RequirementDefinition",
    isAbstract = false,
    superclasses = listOf("ConstraintDefinition"),
    attributes = listOf(
        MetaProperty(
            name = "reqId",
            type = "String",
            lowerBound = 0,
            upperBound = 1,
            redefines = "declaredShortName",
            description = "An optional modeler-specified identifier for this RequirementDefinition, which is the declaredShortName for the RequirementDefinition."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "checkRequirementDefinitionSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('Requirements::RequirementCheck')",
            description = "A RequirementDefinition must directly or indirectly specialize the base RequirementDefinition Requirements::RequirementCheck from the Systems Model Library."
        ),
        MetaConstraint(
            name = "deriveRequirementDefinitionActorParameter",
            type = ConstraintType.DERIVATION,
            expression = """
                featureMembership->
                    selectByKind(ActorMembership).
                    ownedActorParameter
            """.trimIndent(),
            description = "The actorParameters of a RequirementDefinition are the ownedActorParameters of the ActorMemberships of the RequirementDefinition."
        ),
        MetaConstraint(
            name = "deriveRequirementDefinitionAssumedConstraint",
            type = ConstraintType.DERIVATION,
            expression = """
                ownedFeatureMembership->
                    selectByKind(RequirementConstraintMembership)->
                    select(kind = RequirementConstraintKind::assumption).
                    ownedConstraint
            """.trimIndent(),
            description = "The assumedConstraints of a RequirementDefinition are the ownedConstraints of the RequirementConstraintMemberships with kind = assumption."
        ),
        MetaConstraint(
            name = "deriveRequirementDefinitionFramedConcern",
            type = ConstraintType.DERIVATION,
            expression = """
                featureMembership->
                    selectByKind(FramedConcernMembership).
                    ownedConcern
            """.trimIndent(),
            description = "The framedConcerns of a RequirementDefinition are the ownedConcerns of the FramedConcernMemberships of the RequirementDefinition."
        ),
        MetaConstraint(
            name = "deriveRequirementDefinitionRequiredConstraint",
            type = ConstraintType.DERIVATION,
            expression = """
                ownedFeatureMembership->
                    selectByKind(RequirementConstraintMembership)->
                    select(kind = RequirementConstraintKind::requirement).
                    ownedConstraint
            """.trimIndent(),
            description = "The requiredConstraints of a RequirementDefinition are the ownedConstraints of the RequirementConstraintMemberships with kind = requirement."
        ),
        MetaConstraint(
            name = "deriveRequirementDefinitionStakeholderParameter",
            type = ConstraintType.DERIVATION,
            expression = """
                featureMembership->
                    selectByKind(StakeholderMembership).
                    ownedStakeholderParameter
            """.trimIndent(),
            description = "The stakeholderParameters of a RequirementDefinition are the ownedStakeholderParameters of the StakeholderMemberships of the RequirementDefinition."
        ),
        MetaConstraint(
            name = "deriveRequirementDefinitionSubjectParameter",
            type = ConstraintType.DERIVATION,
            expression = """
                let subjects : OrderedSet(SubjectMembership) =
                    featureMembership->selectByKind(SubjectMembership) in
                if subjects->isEmpty() then null
                else subjects->first().ownedSubjectParameter
                endif
            """.trimIndent(),
            description = "The subjectParameter of a RequirementDefinition is the ownedSubjectParameter of its SubjectMembership (if any)."
        ),
        MetaConstraint(
            name = "deriveRequirementDefinitionText",
            type = ConstraintType.DERIVATION,
            expression = "documentation.body",
            description = "The texts of a RequirementDefinition are the bodies of the documentation of the RequirementDefinition."
        ),
        MetaConstraint(
            name = "validateRequirementDefinitionOnlyOneSubject",
            type = ConstraintType.VERIFICATION,
            expression = """
                featureMembership->
                    selectByKind(SubjectMembership)->
                    size() <= 1
            """.trimIndent(),
            description = "A RequirementDefinition must have at most one featureMembership that is a SubjectMembership."
        ),
        MetaConstraint(
            name = "validateRequirementDefinitionSubjectParameterPosition",
            type = ConstraintType.VERIFICATION,
            expression = "input->notEmpty() and input->first() = subjectParameter",
            description = "The subjectParameter of a RequirementDefinition must be its first input."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "requirementDefinitionRequirementCheckBinding",
            baseConcept = "Requirements::RequirementCheck",
            bindingKind = BindingKind.SPECIALIZES
        )
    ),
    description = "A RequirementDefinition is a ConstraintDefinition that defines a requirement used in the context of a specification."
)
