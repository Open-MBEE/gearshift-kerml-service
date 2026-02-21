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
 * SysML VerificationCaseUsage metaclass.
 * Specializes: CaseUsage
 * A VerificationCaseUsage is a Usage of a VerificationCaseDefinition.
 */
fun createVerificationCaseUsageMetaClass() = MetaClass(
    name = "VerificationCaseUsage",
    isAbstract = false,
    superclasses = listOf("CaseUsage"),
    constraints = listOf(
        MetaConstraint(
            name = "checkVerificationCaseUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = "specializesFromLibrary('VerificationCases::verificationCases')",
            description = "A VerificationCaseUsage must subset, directly or indirectly, the base VerificationCaseUsage VerificationCases::verificationCases from the Systems Model Library."
        ),
        MetaConstraint(
            name = "checkVerificationCaseUsageSubVerificationCaseSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                isComposite and owningType <> null and
                (owningType.oclIsKindOf(VerificationCaseDefinition) or
                owningType.oclIsKindOf(VerificationCaseUsage)) implies
                specializesFromLibrary('VerificationCases::VerificationCase::subVerificationCases')
            """.trimIndent(),
            description = "A composite VerificationCaseUsage whose owningType is a VerificationCaseDefinition or VerificationCaseUsage must specialize VerificationCases::VerificationCase::subVerificationCases."
        ),
        MetaConstraint(
            name = "deriveVerificationCaseUsageVerifiedRequirement",
            type = ConstraintType.DERIVATION,
            expression = """
                if objectiveRequirement = null then OrderedSet{}
                else
                    objectiveRequirement.featureMembership->
                        selectByKind(RequirementVerificationMembership).
                        verifiedRequirement->asOrderedSet()
                endif
            """.trimIndent(),
            description = "The verifiedRequirements of a VerificationCaseUsage are the verifiedRequirements of its RequirementVerificationMemberships."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "verificationCaseUsageVerificationCasesBinding",
            baseConcept = "VerificationCases::verificationCases",
            bindingKind = BindingKind.SUBSETS
        ),
        SemanticBinding(
            name = "verificationCaseUsageSubVerificationCaseBinding",
            baseConcept = "VerificationCases::VerificationCase::subVerificationCases",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.And(listOf(
                BindingCondition.IsComposite,
                BindingCondition.Or(listOf(
                    BindingCondition.OwningTypeIs("VerificationCaseDefinition"),
                    BindingCondition.OwningTypeIs("VerificationCaseUsage")
                ))
            ))
        )
    ),
    description = "A VerificationCaseUsage is a Usage of a VerificationCaseDefinition."
)
