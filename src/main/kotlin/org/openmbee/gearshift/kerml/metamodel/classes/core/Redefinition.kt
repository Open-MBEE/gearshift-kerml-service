package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Redefinition metaclass.
 * Specializes: Subsetting
 * A subsetting where one feature redefines another.
 */
fun createRedefinitionMetaClass() = MetaClass(
    name = "Redefinition",
    isAbstract = false,
    superclasses = listOf("Subsetting"),
    attributes = emptyList(),
    description = "A subsetting where one feature redefines another"
)
