package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Metaclass metaclass.
 * Specializes: Structure
 * A structure that represents a metaclass.
 */
fun createMetaclassMetaClass() = MetaClass(
    name = "Metaclass",
    isAbstract = false,
    superclasses = listOf("Structure"),
    attributes = emptyList(),
    description = "A structure that represents a metaclass"
)
