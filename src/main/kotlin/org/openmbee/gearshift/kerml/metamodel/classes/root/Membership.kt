package org.openmbee.gearshift.kerml.metamodel.classes.root

import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaProperty

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
            name = "memberName",
            type = "String",
            multiplicity = "0..1",
            description = "The name of the member element"
        ),
        MetaProperty(
            name = "visibility",
            type = "VisibilityKind",
            multiplicity = "1",
            description = "The visibility of the member"
        )
    ),
    description = "A relationship that makes an element a member of a namespace"
)
