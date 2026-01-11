package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML InterfaceUsage metaclass.
 * Specializes: ConnectionUsage
 * A connection usage representing an interface.
 */
fun createInterfaceUsageMetaClass() = MetaClass(
    name = "InterfaceUsage",
    isAbstract = false,
    superclasses = listOf("ConnectionUsage"),
    attributes = emptyList(),
    description = "A connection usage representing an interface"
)
