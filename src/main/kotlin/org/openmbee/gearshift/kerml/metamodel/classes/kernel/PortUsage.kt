package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML PortUsage metaclass.
 * Specializes: OccurrenceUsage
 * A usage representing a port.
 */
fun createPortUsageMetaClass() = MetaClass(
    name = "PortUsage",
    isAbstract = false,
    superclasses = listOf("OccurrenceUsage"),
    attributes = emptyList(),
    description = "A usage representing a port"
)
