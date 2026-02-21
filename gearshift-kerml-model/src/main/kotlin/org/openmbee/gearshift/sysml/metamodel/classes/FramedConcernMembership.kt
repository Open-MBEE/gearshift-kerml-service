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
 * SysML FramedConcernMembership metaclass.
 * Specializes: RequirementConstraintMembership
 * A FramedConcernMembership is a RequirementConstraintMembership for a framed ConcernUsage of a
 * RequirementDefinition or RequirementUsage.
 */
fun createFramedConcernMembershipMetaClass() = MetaClass(
    name = "FramedConcernMembership",
    isAbstract = false,
    superclasses = listOf("RequirementConstraintMembership"),
    attributes = listOf(
        MetaProperty(
            name = "kind",
            type = "RequirementConstraintKind",
            lowerBound = 1,
            upperBound = 1,
            redefines = "kind",
            description = "The kind of a FramedConcernMembership must be requirement."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "validateFramedConcernMembershipConstraintKind",
            type = ConstraintType.VERIFICATION,
            expression = "kind = RequirementConstraintKind::requirement",
            description = "A FramedConcernMembership must have kind = requirement."
        )
    ),
    description = "A FramedConcernMembership is a RequirementConstraintMembership for a framed ConcernUsage of a RequirementDefinition or RequirementUsage."
)
