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
 * SysML VariantMembership metaclass.
 * Specializes: OwningMembership
 * A VariantMembership is a Membership between a variation point Definition or Usage and a Usage
 * that represents a variant in the context of that variation. The membershipOwningNamespace for the
 * VariantMembership must be either a Definition or a Usage with isVariation = true.
 */
fun createVariantMembershipMetaClass() = MetaClass(
    name = "VariantMembership",
    isAbstract = false,
    superclasses = listOf("OwningMembership"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "validateVariantMembershipOwningNamespace",
            type = ConstraintType.VERIFICATION,
            expression = """
                membershipOwningNamespace.oclIsKindOf(Definition) and
                membershipOwningNamespace.oclAsType(Definition).isVariation or
                membershipOwningNamespace.oclIsKindOf(Usage) and
                membershipOwningNamespace.oclAsType(Usage).isVariation
            """.trimIndent(),
            description = "The membershipOwningNamespace of a VariantMembership must be a variation-point Definition or Usage."
        )
    ),
    description = "A Membership between a variation point Definition or Usage and a Usage that represents a variant in the context of that variation."
)
