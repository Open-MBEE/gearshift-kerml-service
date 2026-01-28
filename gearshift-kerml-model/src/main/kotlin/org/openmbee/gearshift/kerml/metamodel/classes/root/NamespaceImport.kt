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
package org.openmbee.gearshift.kerml.metamodel.classes.root

import org.openmbee.gearshift.framework.meta.BodyLanguage
import org.openmbee.gearshift.framework.meta.ConstraintType
import org.openmbee.gearshift.framework.meta.MetaClass
import org.openmbee.gearshift.framework.meta.MetaConstraint
import org.openmbee.gearshift.framework.meta.MetaOperation
import org.openmbee.gearshift.framework.meta.MetaParameter

/**
 * KerML NamespaceImport metaclass.
 * Specializes: Import
 * An import that brings all visible members of a namespace into another namespace.
 *
 * Association ends (defined in ImportsAssociations.kt):
 * - importedNamespace : Namespace [1] {redefines target}
 */
fun createNamespaceImportMetaClass() = MetaClass(
    name = "NamespaceImport",
    isAbstract = false,
    superclasses = listOf("Import"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "deriveNamespaceImportImportedElement",
            type = ConstraintType.DERIVATION,
            expression = "importedNamespace",
            redefines = "deriveImportImportedElement",
            description = "The importedElement of a NamespaceImport is its importedNamespace"
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "importedMemberships",
            returnType = "Membership",
            returnUpperBound = -1,
            parameters = listOf(
                MetaParameter(name = "excluded", type = "Namespace", lowerBound = 0, upperBound = -1)
            ),
            redefines = "Import::importedMemberships",
            body = """
                if excluded->includes(importedNamespace) then Sequence{}
                else importedNamespace.visibleMemberships(excluded, isRecursive, isImportAll)
                endif
            """.trimIndent(),
            bodyLanguage = BodyLanguage.OCL,
            description = """
                Returns at least the visibleMemberships of the importedNamespace. If isRecursive = true,
                then Memberships are also recursively imported from any ownedMembers of the
                importedNamespace that are themselves Namespaces.
            """.trimIndent()
        )
    ),
    description = "An import that brings all visible members of a namespace into another namespace"
)
