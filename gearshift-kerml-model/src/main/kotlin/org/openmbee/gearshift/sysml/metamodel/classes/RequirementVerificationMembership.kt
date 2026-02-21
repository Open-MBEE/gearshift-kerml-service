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
 * SysML RequirementVerificationMembership metaclass.
 * Specializes: RequirementConstraintMembership
 * A RequirementVerificationMembership is a RequirementConstraintMembership used in the objective
 * of a VerificationCase to identify a RequirementUsage that is verified by the VerificationCase.
 */
fun createRequirementVerificationMembershipMetaClass() = MetaClass(
    name = "RequirementVerificationMembership",
    isAbstract = false,
    superclasses = listOf("RequirementConstraintMembership"),
    attributes = listOf(
        MetaProperty(
            name = "kind",
            type = "RequirementConstraintKind",
            lowerBound = 1,
            upperBound = 1,
            redefines = "kind",
            description = "The kind of a RequirementVerificationMembership must be requirement."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "validateRequirementVerificationMembershipKind",
            type = ConstraintType.VERIFICATION,
            expression = "kind = RequirementConstraintKind::requirement",
            description = "A RequirementVerificationMembership must have kind = requirement."
        ),
        MetaConstraint(
            name = "validateRequirementVerificationMembershipOwningType",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningType.oclIsKindOf(RequirementUsage) and
                owningType.owningFeatureMembership <> null and
                owningType.owningFeatureMembership.oclIsKindOf(ObjectiveMembership)
            """.trimIndent(),
            description = "The owningType of a RequirementVerificationMembership must be a RequirementUsage that is owned by an ObjectiveMembership."
        )
    ),
    description = "A RequirementVerificationMembership is a RequirementConstraintMembership used in the objective of a VerificationCase to identify a RequirementUsage that is verified by the VerificationCase."
)
