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

/**
 * SysML StakeholderMembership metaclass.
 * Specializes: ParameterMembership
 * A StakeholderMembership is a ParameterMembership that identifies a PartUsage as a
 * stakeholderParameter of a RequirementDefinition or RequirementUsage, which specifies a role
 * played by an entity with concerns framed by the owningType.
 */
fun createStakeholderMembershipMetaClass() = MetaClass(
    name = "StakeholderMembership",
    isAbstract = false,
    superclasses = listOf("ParameterMembership"),
    constraints = listOf(
        MetaConstraint(
            name = "validateStakeholderMembershipOwningType",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningType.oclIsKindOf(RequirementUsage) or
                owningType.oclIsKindOf(RequirementDefinition)
            """.trimIndent(),
            description = "The owningType of a StakeholderMembership must be a RequirementDefinition or RequirementUsage."
        )
    ),
    description = "A StakeholderMembership is a ParameterMembership that identifies a PartUsage as a stakeholderParameter of a RequirementDefinition or RequirementUsage."
)
