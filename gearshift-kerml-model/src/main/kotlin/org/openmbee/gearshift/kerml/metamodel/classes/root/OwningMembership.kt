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
import org.openmbee.gearshift.framework.meta.MetaProperty

/**
 * KerML OwningMembership metaclass.
 * Specializes: Membership
 * A membership where the namespace owns the member element.
 *
 * Association ends (defined in NamespacesAssociations.kt):
 * - ownedMemberElement : Element [1] {composite, redefines memberElement}
 */
fun createOwningMembershipMetaClass() = MetaClass(
    name = "OwningMembership",
    isAbstract = false,
    superclasses = listOf("Membership"),
    attributes = listOf(
        MetaProperty(
            name = "ownedMemberName",
            type = "String",
            lowerBound = 0,
            isDerived = true,
            derivationConstraint = "deriveOwningMembershipOwnedMemberName",
            description = "The name of the ownedMemberElement"
        ),
        MetaProperty(
            name = "ownedMemberShortName",
            type = "String",
            lowerBound = 0,
            isDerived = true,
            derivationConstraint = "deriveOwningMembershipOwnedMemberShortName",
            description = "The shortName of the ownedMemberElement"
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "deriveOwningMembershipOwnedMemberName",
            type = ConstraintType.DERIVATION,
            expression = "ownedMemberElement.name",
            description = "The ownedMemberName of an OwningMembership is the name of its ownedMemberElement"
        ),
        MetaConstraint(
            name = "deriveOwningMembershipOwnedMemberShortName",
            type = ConstraintType.DERIVATION,
            expression = "ownedMemberElement.shortName",
            description = "The ownedMemberShortName of an OwningMembership is the shortName of its ownedMemberElement"
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "path",
            returnType = "String",
            returnLowerBound = 1,
            returnUpperBound = 1,
            redefines = "path",
            body = """
                if ownedMemberElement.qualifiedName <> null then
                    ownedMemberElement.qualifiedName + '/owningMembership'
                else self.oclAsType(Relationship).path()
                endif
            """.trimIndent(),
            bodyLanguage = BodyLanguage.OCL,
            isQuery = true,
            description = """
                If the ownedMemberElement of this OwningMembership has a non-null qualifiedName,
                then return the string constructed by appending to that qualifiedName the string
                "/owningMembership". Otherwise, return the path of the OwningMembership as
                specified for a Relationship.
            """.trimIndent()
        )
    ),
    description = "A membership where the namespace owns the member element"
)
