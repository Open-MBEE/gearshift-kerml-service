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
 * SysML SatisfyRequirementUsage metaclass.
 * Specializes: AssertConstraintUsage, RequirementUsage
 * A SatisfyRequirementUsage is an AssertConstraintUsage that asserts, by default, that a satisfied
 * RequirementUsage is true for a specific satisfyingFeature, or, if isNegated = true, that the
 * RequirementUsage is false.
 */
fun createSatisfyRequirementUsageMetaClass() = MetaClass(
    name = "SatisfyRequirementUsage",
    isAbstract = false,
    superclasses = listOf("AssertConstraintUsage", "RequirementUsage"),
    constraints = listOf(
        MetaConstraint(
            name = "checkSatisfyRequirementUsageBindingConnector",
            type = ConstraintType.VERIFICATION,
            expression = """
                ownedMember->selectByKind(BindingConnector)->
                select(b |
                    b.relatedElement->includes(subjectParameter) and
                    b.relatedElement->exists(r | r <> subjectParameter))->
                size() = 1
            """.trimIndent(),
            description = "A SatisfyRequirementUsage must have exactly one ownedMember that is a BindingConnector between its subjectParameter and some Feature other than the subjectParameter."
        ),
        MetaConstraint(
            name = "checkSatisfyRequirementUsageSpecialization",
            type = ConstraintType.VERIFICATION,
            expression = """
                if isNegated then
                    specializesFromLibrary('Requirements::notSatisfiedRequirementChecks')
                else
                    specializesFromLibrary('Requirements::satisfiedRequirementChecks')
                endif
            """.trimIndent(),
            description = "If a SatisfyRequirementUsage is negated, then it must specialize Requirements::notSatisfiedRequirementChecks. Otherwise, it must specialize Requirements::satisfiedRequirementChecks."
        ),
        MetaConstraint(
            name = "deriveSatisfyRequirementUsageSatisfyingFeature",
            type = ConstraintType.DERIVATION,
            expression = """
                let bindings : BindingConnector = ownedMember->
                    selectByKind(BindingConnector)->
                    select(b | b.relatedElement->includes(subjectParameter)) in
                if bindings->isEmpty() or
                    not bindings->first().relatedElement->exists(r | r <> subjectParameter)
                then null
                else bindings->first().relatedElement->any(r | r <> subjectParameter)
                endif
            """.trimIndent(),
            description = "The satisfyingFeature of a SatisfyRequirementUsage is the Feature to which the subjectParameter is bound."
        ),
        MetaConstraint(
            name = "validateSatisfyRequirementUsageReference",
            type = ConstraintType.VERIFICATION,
            expression = """
                referencedFeatureTarget() <> null implies
                referencedFeatureTarget().oclIsKindOf(RequirementUsage)
            """.trimIndent(),
            description = "If a SatisfyRequirementUsage has an ownedReferenceSubsetting, then the featureTarget of its referencedFeature must be a RequirementUsage."
        )
    ),
    semanticBindings = listOf(
        SemanticBinding(
            name = "satisfyRequirementUsageSatisfiedBinding",
            baseConcept = "Requirements::satisfiedRequirementChecks",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.Not(
                BindingCondition.PropertyEquals("isNegated", true)
            )
        ),
        SemanticBinding(
            name = "satisfyRequirementUsageNotSatisfiedBinding",
            baseConcept = "Requirements::notSatisfiedRequirementChecks",
            bindingKind = BindingKind.SUBSETS,
            condition = BindingCondition.PropertyEquals("isNegated", true)
        )
    ),
    description = "A SatisfyRequirementUsage is an AssertConstraintUsage that asserts, by default, that a satisfied RequirementUsage is true for a specific satisfyingFeature."
)
