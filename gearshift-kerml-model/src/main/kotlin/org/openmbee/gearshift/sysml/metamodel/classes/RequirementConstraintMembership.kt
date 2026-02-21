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

import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.MetaProperty

/**
 * SysML RequirementConstraintMembership metaclass.
 * Specializes: FeatureMembership
 * A RequirementConstraintMembership is a FeatureMembership for an assumed or required
 * ConstraintUsage of a RequirementDefinition or RequirementUsage.
 */
fun createRequirementConstraintMembershipMetaClass() = MetaClass(
    name = "RequirementConstraintMembership",
    isAbstract = false,
    superclasses = listOf("FeatureMembership"),
    attributes = listOf(
        MetaProperty(
            name = "kind",
            type = "RequirementConstraintKind",
            lowerBound = 1,
            upperBound = 1,
            description = "Whether the RequirementConstraintMembership is for an assumed or required ConstraintUsage."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "deriveRequirementConstraintMembershipReferencedConstraint",
            type = ConstraintType.DERIVATION,
            expression = """
                let referencedFeature : Feature =
                    ownedConstraint.referencedFeatureTarget() in
                if referencedFeature = null then ownedConstraint
                else if referencedFeature.oclIsKindOf(ConstraintUsage) then
                    referencedFeature.oclAsType(ConstraintUsage)
                else null
                endif endif
            """.trimIndent(),
            description = "The referencedConstraint of a RequirementConstraintMembership is the featureTarget of the referencedFeature of the ownedReferenceSubsetting of the ownedConstraint, if there is one, and, otherwise, the ownedConstraint itself."
        ),
        MetaConstraint(
            name = "validateRequirementConstraintMembershipIsComposite",
            type = ConstraintType.VERIFICATION,
            expression = "ownedConstraint.isComposite",
            description = "The ownedConstraint of a RequirementConstraintMembership must be composite."
        ),
        MetaConstraint(
            name = "validateRequirementConstraintMembershipOwningType",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningType.oclIsKindOf(RequirementDefinition) or
                owningType.oclIsKindOf(RequirementUsage)
            """.trimIndent(),
            description = "The owningType of a RequirementConstraintMembership must be a RequirementDefinition or a RequirementUsage."
        )
    ),
    description = "A RequirementConstraintMembership is a FeatureMembership for an assumed or required ConstraintUsage of a RequirementDefinition or RequirementUsage."
)
