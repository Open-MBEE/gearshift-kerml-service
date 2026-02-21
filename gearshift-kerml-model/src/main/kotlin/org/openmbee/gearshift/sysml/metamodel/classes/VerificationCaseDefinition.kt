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
 * SysML VerificationCaseDefinition metaclass.
 * Specializes: CaseDefinition
 * A VerificationCaseDefinition is a CaseDefinition for the purpose of verification of the subject
 * of the case against its requirements.
 */
fun createVerificationCaseDefinitionMetaClass() = MetaClass(
    name = "VerificationCaseDefinition",
    isAbstract = false,
    superclasses = listOf("CaseDefinition"),
    constraints = listOf(
        MetaConstraint(
            name = "checkVerificationCaseSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('VerificationCases::VerificationCase')",
            description = "A VerificationCaseDefinition must directly or indirectly specialize the base VerificationCaseDefinition VerificationCases::VerificationCase from the Systems Model Library."
        ),
        MetaConstraint(
            name = "deriveVerificationCaseDefinitionVerifiedRequirement",
            type = ConstraintType.DERIVATION,
            expression = """
                if objectiveRequirement = null then OrderedSet{}
                else
                    objectiveRequirement.featureMembership->
                        selectByKind(RequirementVerificationMembership).
                        verifiedRequirement->asOrderedSet()
                endif
            """.trimIndent(),
            description = "The verifiedRequirements of a VerificationCaseDefinition are the verifiedRequirements of its RequirementVerificationMemberships."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "verificationCaseDefinitionVerificationCaseBinding",
            baseConcept = "VerificationCases::VerificationCase",
            bindingKind = BindingKind.SPECIALIZES
        )
    ),
    description = "A VerificationCaseDefinition is a CaseDefinition for the purpose of verification of the subject of the case against its requirements."
)
