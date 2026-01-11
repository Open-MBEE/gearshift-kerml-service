package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML AllocationDefinition metaclass.
 * Specializes: ConnectionDefinition
 * A definition of an allocation.
 */
fun createAllocationDefinitionMetaClass() = MetaClass(
    name = "AllocationDefinition",
    isAbstract = false,
    superclasses = listOf("ConnectionDefinition"),
    attributes = emptyList(),
    description = "A definition of an allocation"
)
