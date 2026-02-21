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
 * SysML ObjectiveMembership metaclass.
 * Specializes: FeatureMembership
 * An ObjectiveMembership is a FeatureMembership that indicates that its ownedObjectiveRequirement
 * is the objective RequirementUsage for its owningType, which must be a CaseDefinition or CaseUsage.
 */
fun createObjectiveMembershipMetaClass() = MetaClass(
    name = "ObjectiveMembership",
    isAbstract = false,
    superclasses = listOf("FeatureMembership"),
    constraints = listOf(
        MetaConstraint(
            name = "validateObjectiveMembershipIsComposite",
            type = ConstraintType.VERIFICATION,
            expression = "ownedObjectiveRequirement.isComposite",
            description = "The ownedObjectiveRequirement of an ObjectiveMembership must be composite."
        ),
        MetaConstraint(
            name = "validateObjectiveMembershipOwningType",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningType.oclIsKindOf(CaseDefinition) or
                owningType.oclIsKindOf(CaseUsage)
            """.trimIndent(),
            description = "The owningType of an ObjectiveMembership must be a CaseDefinition or CaseUsage."
        )
    ),
    description = "An ObjectiveMembership is a FeatureMembership that indicates that its ownedObjectiveRequirement is the objective RequirementUsage for its owningType."
)
