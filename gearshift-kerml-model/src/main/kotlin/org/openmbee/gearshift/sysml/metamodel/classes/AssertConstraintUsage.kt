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
 * SysML AssertConstraintUsage metaclass.
 * Specializes: Invariant, ConstraintUsage
 * An AssertConstraintUsage is a ConstraintUsage that is also an Invariant and, so, is asserted to be true
 * (by default). Unless it is the AssertConstraintUsage itself, the asserted ConstraintUsage is related to the
 * AssertConstraintUsage by a ReferenceSubsetting Relationship.
 */
fun createAssertConstraintUsageMetaClass() = MetaClass(
    name = "AssertConstraintUsage",
    isAbstract = false,
    superclasses = listOf("Invariant", "ConstraintUsage"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "checkAssertConstraintUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                if isNegated then
                    specializesFromLibrary('Constraints::negatedConstraintChecks')
                else
                    specializesFromLibrary('Constraints::assertedConstraintChecks')
                endif
            """.trimIndent(),
            description = "If an AssertConstraintUsage is negated, then it must specialize Constraints::negatedConstraintChecks. Otherwise, it must specialize Constraints::assertedConstraintChecks."
        ),
        MetaConstraint(
            name = "deriveAssertConstraintUsageAssertedConstraint",
            type = ConstraintType.DERIVATION,
            expression = """
                if referencedFeatureTarget() = null then self
                else if referencedFeatureTarget().oclIsKindOf(ConstraintUsage) then
                    referencedFeatureTarget().oclAsType(ConstraintUsage)
                else null
                endif endif
            """.trimIndent(),
            description = "If an AssertConstraintUsage has no ownedReferenceSubsetting, then its assertedConstraint is the AssertConstraintUsage itself. Otherwise, the assertedConstraint is the featureTarget of the referencedFeature, which must be a ConstraintUsage."
        ),
        MetaConstraint(
            name = "validateAssertConstraintUsageReference",
            type = ConstraintType.VERIFICATION,
            expression = """
                referencedFeatureTarget() <> null implies
                referencedFeatureTarget().oclIsKindOf(ConstraintUsage)
            """.trimIndent(),
            description = "If an AssertConstraintUsage has an ownedReferenceSubsetting, then the featureTarget of its referencedFeature must be a ConstraintUsage."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "assertConstraintUsageAssertedBinding",
            baseConcept = "Constraints::assertedConstraintChecks",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.Not(
                BindingCondition.PropertyEquals("isNegated", "true")
            )
        ),
        SemanticBinding(
            name = "assertConstraintUsageNegatedBinding",
            baseConcept = "Constraints::negatedConstraintChecks",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.PropertyEquals("isNegated", "true")
        )
    ),
    description = "An AssertConstraintUsage is a ConstraintUsage that is also an Invariant, asserted to be true by default."
)
