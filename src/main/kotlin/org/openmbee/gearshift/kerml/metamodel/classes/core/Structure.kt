package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Structure metaclass.
 * Specializes: Class
 * A class that represents a structure.
 */
fun createStructureMetaClass() = MetaClass(
    name = "Structure",
    isAbstract = false,
    superclasses = listOf("Class"),
    attributes = emptyList(),
    description = "A class that represents a structure"
)
