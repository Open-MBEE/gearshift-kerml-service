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
 * Figure 8: Imports
 * Defines associations for Import, NamespaceImport, and MembershipImport.
 */
fun createImportAssociations(): List<MetaAssociation> {

    // Import is owned by importing Namespace (composite ownership)
    val importOwningNamespaceOwnedImportAssociation = MetaAssociation(
        name = "importOwningNamespaceOwnedImportAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "importOwningNamespace",
            type = "Namespace",
            lowerBound = 1,
            upperBound = 1,
            subsets = listOf("owningRelatedElement"),
            redefines = listOf("source")
        ),
        targetEnd = MetaAssociationEnd(
            name = "ownedImport",
            type = "Import",
            lowerBound = 0,
            upperBound = -1,
            aggregation = AggregationKind.COMPOSITE,
            isOrdered = true,
            subsets = listOf("ownedRelationship", "sourceRelationship")
        )
    )

    // NamespaceImport references the imported Namespace
    val importImportedNamespaceAssociation = MetaAssociation(
        name = "importImportedNamespaceAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "import",
            type = "NamespaceImport",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            subsets = listOf("targetRelationship")
        ),
        targetEnd = MetaAssociationEnd(
            name = "importedNamespace",
            type = "Namespace",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("target")
        )
    )

    // MembershipImport references the imported Membership
    val importImportedMembershipAssociation = MetaAssociation(
        name = "importImportedMembershipAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "import",
            type = "MembershipImport",
            lowerBound = 0,
            upperBound = -1,
            isNavigable = false,
            redefines = listOf("targetRelationship")
        ),
        targetEnd = MetaAssociationEnd(
            name = "importedMembership",
            type = "Membership",
            lowerBound = 1,
            upperBound = 1,
            redefines = listOf("target")
        )
    )

    // Import has derived importedElement (subclasses provide derivation via constraints)
    val membershipImportImportedElementAssociation = MetaAssociation(
        name = "membershipImportImportedElementAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "membershipImport",
            type = "Import",
            lowerBound = 0,
            upperBound = -1,
            isDerived = true,
            isNavigable = false
        ),
        targetEnd = MetaAssociationEnd(
            name = "importedElement",
            type = "Element",
            lowerBound = 1,
            upperBound = 1,
            isDerived = true,
            derivationConstraint = "deriveImportImportedElement"
        )
    )

    return listOf(
        importOwningNamespaceOwnedImportAssociation,
        importImportedNamespaceAssociation,
        importImportedMembershipAssociation,
        membershipImportImportedElementAssociation
    )
}
