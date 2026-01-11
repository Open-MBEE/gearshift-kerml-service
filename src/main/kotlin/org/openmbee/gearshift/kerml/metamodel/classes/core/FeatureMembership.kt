package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML FeatureMembership metaclass.
 * Specializes: OwningMembership
 * A membership where the member is a feature.
 */
fun createFeatureMembershipMetaClass() = MetaClass(
    name = "FeatureMembership",
    isAbstract = false,
    superclasses = listOf("OwningMembership"),
    attributes = emptyList(),
    description = "A membership where the member is a feature"
)
