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

import org.openmbee.gearshift.metamodel.MetaAssociation
import org.openmbee.gearshift.metamodel.MetaAssociationEnd

/**
 * Figure 5: Dependencies
 * Defines associations for Dependency relationships.
 */
fun createDependencyAssociations(): List<MetaAssociation> {

    // Dependency has client : Element [1..*] {ordered, redefines source}
    val clientDependencyClientAssociation = MetaAssociation(
        name = "clientDependencyclientAssociation",
        sourceEnd = MetaAssociationEnd(
            name = "clientDependency",
            type = "Dependency",
            lowerBound = 0,
            upperBound = -1,
            subsets = listOf("sourceRelationship")
        ),
        targetEnd = MetaAssociationEnd(
            name = "client",
            type = "Element",
            lowerBound = 1,
            upperBound = -1,
            isOrdered = true,
            redefines = listOf("source"),

            )
    )

    // Dependency has supplier : Element [1..*] {ordered, redefines target}
    val supplierDependencySupplier = MetaAssociation(
        name = "supplierDependencySupplier",
        sourceEnd = MetaAssociationEnd(
            name = "supplierDependency",
            type = "Dependency",
            lowerBound = 0,
            upperBound = -1,
            subsets = listOf("targetRelationship")
        ),
        targetEnd = MetaAssociationEnd(
            name = "supplier",
            type = "Element",
            lowerBound = 1,
            upperBound = -1,
            isOrdered = true,
            redefines = listOf("target"),

            )
    )

    return listOf(clientDependencyClientAssociation, supplierDependencySupplier)
}
