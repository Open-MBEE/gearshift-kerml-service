package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML DataType metaclass.
 * Specializes: Classifier
 * A classifier that represents a data type.
 */
fun createDataTypeMetaClass() = MetaClass(
    name = "DataType",
    isAbstract = false,
    superclasses = listOf("Classifier"),
    attributes = emptyList(),
    description = "A classifier that represents a data type"
)
