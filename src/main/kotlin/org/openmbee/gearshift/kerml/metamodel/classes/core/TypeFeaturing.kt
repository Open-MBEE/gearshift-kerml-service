package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML TypeFeaturing metaclass.
 * Specializes: Featuring
 * A featuring relationship involving a type.
 */
fun createTypeFeaturingMetaClass() = MetaClass(
    name = "TypeFeaturing",
    isAbstract = false,
    superclasses = listOf("Featuring"),
    attributes = emptyList(),
    description = "A featuring relationship involving a type"
)
