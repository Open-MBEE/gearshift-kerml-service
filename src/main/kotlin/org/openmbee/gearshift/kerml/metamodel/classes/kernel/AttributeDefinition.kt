package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML AttributeDefinition metaclass.
 * Specializes: Definition, DataType
 * A definition of an attribute.
 */
fun createAttributeDefinitionMetaClass() = MetaClass(
    name = "AttributeDefinition",
    isAbstract = false,
    superclasses = listOf("Definition", "DataType"),
    attributes = emptyList(),
    description = "A definition of an attribute"
)
