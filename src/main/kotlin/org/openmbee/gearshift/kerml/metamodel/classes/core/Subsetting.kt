package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Subsetting metaclass.
 * Specializes: Specialization
 * A specialization where one feature subsets another.
 */
fun createSubsettingMetaClass() = MetaClass(
    name = "Subsetting",
    isAbstract = false,
    superclasses = listOf("Specialization"),
    attributes = emptyList(),
    description = "A specialization where one feature subsets another"
)
