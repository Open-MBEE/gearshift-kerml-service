package org.openmbee.gearshift.kerml.metamodel.classes.root

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML OwningMembership metaclass.
 * Specializes: Membership
 * A membership where the namespace owns the member element.
 */
fun createOwningMembershipMetaClass() = MetaClass(
    name = "OwningMembership",
    isAbstract = false,
    superclasses = listOf("Membership"),
    attributes = emptyList(),
    description = "A membership where the namespace owns the member element"
)
