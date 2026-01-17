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

import org.openmbee.gearshift.metamodel.BodyLanguage
import org.openmbee.gearshift.metamodel.ConstraintType
import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaConstraint
import org.openmbee.gearshift.metamodel.MetaOperation
import org.openmbee.gearshift.metamodel.MetaParameter

/**
 * KerML MembershipImport metaclass.
 * Specializes: Import
 * An import that brings a specific membership into a namespace.
 *
 * Association ends (redefines Import):
 * - importedElement : Element [1] {derived, redefines importedElement}
 */
fun createMembershipImportMetaClass() = MetaClass(
    name = "MembershipImport",
    isAbstract = false,
    superclasses = listOf("Import"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "deriveMembershipImportImportedElement",
            type = ConstraintType.DERIVATION,
            expression = "importedMembership.memberElement",
            redefines = "deriveImportImportedElement",
            description = "The importedElement of a MembershipImport is the memberElement of its importedMembership"
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
                if not isRecursive or
                   not importedElement.oclIsKindOf(Namespace) or
                   excluded->includes(importedElement)
                then Sequence{importedMembership}
                else importedElement.oclAsType(Namespace).
                     visibleMemberships(excluded, true, isImportAll)->
                     prepend(importedMembership)
                endif
            """.trimIndent(),
            bodyLanguage = BodyLanguage.OCL,
            description = """
                Returns at least the importedMembership. If isRecursive = true and the memberElement
                of the importedMembership is a Namespace, then Memberships are also recursively
                imported from that Namespace.
            """.trimIndent()
        )
    ),
    description = "An import that brings a specific membership into a namespace"
)
