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
 * SysML StateSubactionMembership metaclass.
 * Specializes: FeatureMembership
 * A StateSubactionMembership is a FeatureMembership for an entry, do or exit ActionUsage of a
 * StateDefinition or StateUsage.
 */
fun createStateSubactionMembershipMetaClass() = MetaClass(
    name = "StateSubactionMembership",
    isAbstract = false,
    superclasses = listOf("FeatureMembership"),
    attributes = listOf(
        MetaProperty(
            name = "kind",
            type = "StateSubactionKind",
            lowerBound = 1,
            upperBound = 1,
            description = "Whether this StateSubactionMembership is for an entry, do or exit ActionUsage."
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "validateStateSubactionMembershipOwningType",
            type = ConstraintType.VERIFICATION,
            expression = """
                owningType.oclIsKindOf(StateDefinition) or
                owningType.oclIsKindOf(StateUsage)
            """.trimIndent(),
            description = "The owningType of a StateSubactionMembership must be a StateDefinition or a StateUsage."
        )
    ),
    description = "A StateSubactionMembership is a FeatureMembership for an entry, do or exit ActionUsage of a StateDefinition or StateUsage."
)
