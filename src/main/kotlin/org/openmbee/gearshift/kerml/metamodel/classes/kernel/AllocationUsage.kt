package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML AllocationUsage metaclass.
 * Specializes: ConnectionUsage
 * A connection usage representing an allocation.
 */
fun createAllocationUsageMetaClass() = MetaClass(
    name = "AllocationUsage",
    isAbstract = false,
    superclasses = listOf("ConnectionUsage"),
    attributes = emptyList(),
    description = "A connection usage representing an allocation"
)
