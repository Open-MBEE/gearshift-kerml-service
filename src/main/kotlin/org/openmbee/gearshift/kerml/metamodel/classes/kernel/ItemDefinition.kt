package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML ItemDefinition metaclass.
 * Specializes: OccurrenceDefinition, Structure
 * A definition of an item.
 */
fun createItemDefinitionMetaClass() = MetaClass(
    name = "ItemDefinition",
    isAbstract = false,
    superclasses = listOf("OccurrenceDefinition", "Structure"),
    attributes = emptyList(),
    description = "A definition of an item"
)
