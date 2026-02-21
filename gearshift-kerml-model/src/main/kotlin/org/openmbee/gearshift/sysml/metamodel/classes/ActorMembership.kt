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
 * SysML ActorMembership metaclass.
 * Specializes: ParameterMembership
 * An ActorMembership is a ParameterMembership that identifies a PartUsage as an actor parameter, which
 * specifies a role played by an external entity in interaction with the owningType of the ActorMembership.
 */
fun createActorMembershipMetaClass() = MetaClass(
    name = "ActorMembership",
    isAbstract = false,
    superclasses = listOf("ParameterMembership"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "validateActorMembershipOwningType",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningType.oclIsKindOf(RequirementUsage) or
                owningType.oclIsKindOf(RequirementDefinition) or
                owningType.oclIsKindOf(CaseDefinition) or
                owningType.oclIsKindOf(CaseUsage)
            """.trimIndent(),
            description = "The owningType of an ActorMembership must be a RequirementDefinition, RequirementUsage, CaseDefinition, or CaseUsage."
        )
    ),
    description = "An ActorMembership is a ParameterMembership that identifies a PartUsage as an actor parameter."
)
