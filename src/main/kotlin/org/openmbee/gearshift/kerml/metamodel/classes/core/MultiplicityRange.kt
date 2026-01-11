package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML MultiplicityRange metaclass.
 * Specializes: Multiplicity
 * A multiplicity specified as a range with bounds.
 */
fun createMultiplicityRangeMetaClass() = MetaClass(
    name = "MultiplicityRange",
    isAbstract = false,
    superclasses = listOf("Multiplicity"),
    attributes = emptyList(),
    description = "A multiplicity specified as a range with bounds"
)
