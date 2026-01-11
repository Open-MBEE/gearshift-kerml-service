package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML InterfaceDefinition metaclass.
 * Specializes: ConnectionDefinition
 * A definition of an interface.
 */
fun createInterfaceDefinitionMetaClass() = MetaClass(
    name = "InterfaceDefinition",
    isAbstract = false,
    superclasses = listOf("ConnectionDefinition"),
    attributes = emptyList(),
    description = "A definition of an interface"
)
