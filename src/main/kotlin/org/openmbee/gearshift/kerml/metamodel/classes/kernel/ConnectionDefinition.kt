package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML ConnectionDefinition metaclass.
 * Specializes: PartDefinition, AssociationStructure
 * A definition of a connection.
 */
fun createConnectionDefinitionMetaClass() = MetaClass(
    name = "ConnectionDefinition",
    isAbstract = false,
    superclasses = listOf("PartDefinition", "AssociationStructure"),
    attributes = emptyList(),
    description = "A definition of a connection"
)
