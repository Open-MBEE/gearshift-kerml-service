package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Class metaclass.
 * Specializes: Classifier
 * A classifier that represents a class.
 */
fun createClassMetaClass() = MetaClass(
    name = "Class",
    isAbstract = false,
    superclasses = listOf("Classifier"),
    attributes = emptyList(),
    description = "A classifier that represents a class"
)
