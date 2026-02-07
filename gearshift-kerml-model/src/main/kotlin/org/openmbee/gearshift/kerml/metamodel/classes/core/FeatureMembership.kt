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
package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.OwnershipBinding

/**
 * KerML FeatureMembership metaclass.
 * Specializes: OwningMembership
 * A membership where the member is a feature.
 *
 * Association ends (defined in TypesAssociations.kt):
 * - owningType : Type [1] {derived, subsets type, redefines membershipOwningNamespace}
 * - ownedMemberFeature : Feature [1] {derived, composite, redefines ownedMemberElement}
 */
fun createFeatureMembershipMetaClass() = MetaClass(
    name = "FeatureMembership",
    isAbstract = false,
    superclasses = listOf("OwningMembership"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "computeFeatureMembershipType",
            type = ConstraintType.NON_NAVIGABLE_END,
            expression = "Type.allInstances()->select(t | t.featureMembership->includes(self))",
            isNormative = false,
            description = "The Types that have this FeatureMembership as a feature membership."
        ),
        MetaConstraint(
            name = "deriveFeatureMembershipOwnedMemberFeature",
            type = ConstraintType.REDEFINES_DERIVATION,
            expression = "if ownedMemberElement.oclIsKindOf(Feature) then ownedMemberElement.oclAsType(Feature) else null endif",
            description = "The Feature that this FeatureMembership relates to its owningType, making it an ownedFeature of the owningType."
        ),
        MetaConstraint(
            name = "deriveFeatureMembershipOwningType",
            type = ConstraintType.DERIVATION,
            expression = "if membershipOwningNamespace.oclIsKindOf(Type) then membershipOwningNamespace.oclAsType(Type) else null endif",
            description = "The Type that owns this FeatureMembership."
        )
    ),
    description = "A membership where the member is a feature",

    // FeatureMembership is an ownership intermediate for Type → Feature
    // Type → FeatureMembership → Feature
    // Types are derived from association ends: owningType->Type, ownedMemberFeature->Feature
    ownershipBinding = OwnershipBinding(
        ownedElementEnd = "ownedMemberFeature",
        ownerEnd = "owningType"
    )
)
