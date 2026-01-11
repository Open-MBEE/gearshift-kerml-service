package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML PartDefinition metaclass.
 * Specializes: ItemDefinition
 * A definition of a part.
 */
fun createPartDefinitionMetaClass() = MetaClass(
    name = "PartDefinition",
    isAbstract = false,
    superclasses = listOf("ItemDefinition"),
    attributes = emptyList(),
    description = "A definition of a part"
)
