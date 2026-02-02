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
package org.openmbee.gearshift.kerml.metamodel.associations

import org.openmbee.mdm.framework.meta.AggregationKind
import org.openmbee.mdm.framework.meta.MetaAssociation
import org.openmbee.mdm.framework.meta.MetaAssociationEnd

/**
 * Figure 7: Namespaces
 * Defines associations for Namespace and Membership relationships.
 */
fun createNamespaceAssociations(): List<MetaAssociation> {

    // Membership owns its member Element (composite ownership)
    val owningMembershipOwnedMemberElementAssociation = MetaAssociation(
        name = "owningMembershipOwnedMemberElementAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "owningMembership",
            type = "OwningMembership",
            lowerBound = 0,
            upperBound = 1,
            subsets = listOf("membership", "owningRelationship")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedMemberElement",
            type = "Element",
            lowerBound = 1,
            upperBound = 1,
            aggregation = AggregationKind.COMPOSITE,
            redefines = listOf("memberElement"),
            subsets = listOf("ownedRelatedElement")
        )
    )

    // Membership references its member Element
    val membershipMemberElementAssociation = MetaAssociation(
        name = "membershipMemberElementAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "membership",
            type = "Membership",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false
        ),
        targetEnd = MetaAssociationEnd(
            name = "memberElement",
            type = "Element",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("target")
        )
    )

    // Element knows its owning Namespace (derived, inverse of ownedMember)
    val ownedMemberOwningNamespaceAssociation = MetaAssociation(
        name = "ownedMemberOwningNamespaceAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "ownedMember",
            type = "Element",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("member"),
            derivationConstraint = "deriveNamespaceOwnedMember"
        ),
        targetEnd = MetaAssociationEnd(
            name = "owningNamespace",
            type = "Namespace",
            lowerBound = 0,
            upperBound = 1,
            isDerived = true,
            subsets = listOf("namespace"),
            derivationConstraint = "deriveElementOwningNamespace"
        )
    )

    // Namespace has members (derived from memberships)
    val namespaceMemberAssociation = MetaAssociation(
        name = "namespaceMemberAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "namespace",
            type = "Namespace",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false
        ),
        targetEnd = MetaAssociationEnd(
            name = "member",
            type = "Element",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            derivationConstraint = "deriveNamespaceMembers"
        )
    )

    // Membership belongs to a Namespace
    val membershipNamespaceMembershipAssociation = MetaAssociation(
        name = "membershipNamespaceMembershipAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "membershipNamespace",
            type = "Namespace",
            lowerBound = 1,
            upperBound = -1,
            isDerived = true,
            isUnion = true,
        ),
        targetEnd = MetaAssociationEnd(
            name = "membership",
            type = "Membership",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            isUnion = true,
            subsets = listOf("sourceRelationship")
        )
    )

    // Namespace owns Memberships (composite ownership)
    val membershipOwningNamespaceOwnedMembershipAssociation = MetaAssociation(
        name = "membershipOwningNamespaceOwnedMembershipAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "membershipOwningNamespace",
            type = "Namespace",
            lowerBound = 1,
            upperBound = 1,
            subsets = listOf("membershipNamespace", "owningRelatedElement"),
            redefines = listOf("source")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedMembership",
            type = "Membership",
            lowerBound = 0,
            upperBound = -1,
            aggregation = AggregationKind.COMPOSITE,
            isOrdered = true,
            subsets = listOf("membership", "ownedRelationship", "sourceRelationship")
        )
    )

    // Namespace imports Memberships via Import
    val importingNamespaceImportedMembershipAssociation = MetaAssociation(
        name = "importingNamespaceImportedMembershipAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "importingNamespace",
            type = "Namespace",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false,
            subsets = listOf("membershipNamespace"),
        ),
        targetEnd = MetaAssociationEnd(
            name = "importedMembership",
            type = "Membership",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isOrdered = true,
            subsets = listOf("membership"),
            derivationConstraint = "deriveNamespaceImportedMembership"
        )
    )

    return listOf(
        owningMembershipOwnedMemberElementAssociation,
        membershipMemberElementAssociation,
        ownedMemberOwningNamespaceAssociation,
        namespaceMemberAssociation,
        membershipNamespaceMembershipAssociation,
        membershipOwningNamespaceOwnedMembershipAssociation,
        importingNamespaceImportedMembershipAssociation
    )
}