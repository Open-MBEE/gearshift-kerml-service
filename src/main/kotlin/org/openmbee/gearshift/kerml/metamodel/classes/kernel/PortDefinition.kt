package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML PortDefinition metaclass.
 * Specializes: OccurrenceDefinition, Structure
 * A definition of a port.
 */
fun createPortDefinitionMetaClass() = MetaClass(
    name = "PortDefinition",
    isAbstract = false,
    superclasses = listOf("OccurrenceDefinition", "Structure"),
    attributes = emptyList(),
    description = "A definition of a port"
)
