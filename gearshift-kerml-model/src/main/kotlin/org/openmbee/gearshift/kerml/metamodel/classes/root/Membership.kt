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

import org.openmbee.mdm.framework.meta.BodyLanguage
import org.openmbee.mdm.framework.meta.ConstraintType
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.MetaConstraint
import org.openmbee.mdm.framework.meta.MetaOperation
import org.openmbee.mdm.framework.meta.MetaParameter
import org.openmbee.mdm.framework.meta.MetaProperty

/**
 * KerML Membership metaclass.
 * Specializes: Relationship
 * A relationship that makes an element a member of a namespace.
 */
fun createMembershipMetaClass() = MetaClass(
    name = "Membership",
    isAbstract = false,
    superclasses = listOf("Relationship"),
    attributes = listOf(
        MetaProperty(
            name = "memberElementId",
            type = "String",
            lowerBound = 0,
            isDerived = true,
            derivationConstraint = "deriveMembershipMemberElementId",
            description = "The elementId of the memberElement"
        ),
        MetaProperty(
            name = "memberName",
            type = "String",
            lowerBound = 0,
            description = "The name of the memberElement in the membershipOwningNamespace"
        ),
        MetaProperty(
            name = "memberShortName",
            type = "String",
            lowerBound = 0,
            description = "The short name of the memberElement in the membershipOwningNamespace"
        ),
        MetaProperty(
            name = "visibility",
            type = "VisibilityKind",
            description = "The visibility of the member"
        )
    ),
    constraints = listOf(
        MetaConstraint(
            name = "deriveMembershipMemberElementId",
            type = ConstraintType.DERIVATION,
            expression = "memberElement.elementId",
            description = "The memberElementId of a Membership is the elementId of its memberElement"
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "isDistinguishableFrom",
            returnType = "Boolean",
            returnLowerBound = 1,
            returnUpperBound = 1,
            parameters = listOf(
                MetaParameter(name = "other", type = "Membership")
            ),
            body = """
                not (memberElement.oclIsKindOf(other.memberElement.oclType()) or
                     other.memberElement.oclIsKindOf(memberElement.oclType())) or
                (memberShortName = null or
                 (memberShortName <> other.memberShortName and
                  memberShortName <> other.memberName)) and
                (memberName = null or
                 (memberName <> other.memberShortName and
                  memberName <> other.memberName))
            """.trimIndent(),
            bodyLanguage = BodyLanguage.OCL,
            description = """
                Whether this Membership is distinguishable from a given other Membership.
                By default, this is true if this Membership has no memberShortName or memberName;
                or each of the memberShortName and memberName are different than both of those
                of the other Membership; or neither of the metaclasses of the memberElement of this
                Membership and the memberElement of the other Membership conform to the other.
            """.trimIndent()
        )
    ),
    description = "A relationship that makes an element a member of a namespace"
)
