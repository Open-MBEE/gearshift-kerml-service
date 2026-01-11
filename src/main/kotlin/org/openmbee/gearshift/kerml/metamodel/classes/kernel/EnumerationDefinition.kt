package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML EnumerationDefinition metaclass.
 * Specializes: AttributeDefinition
 * A definition of an enumeration type.
 */
fun createEnumerationDefinitionMetaClass() = MetaClass(
    name = "EnumerationDefinition",
    isAbstract = false,
    superclasses = listOf("AttributeDefinition"),
    attributes = emptyList(),
    description = "A definition of an enumeration type"
)
