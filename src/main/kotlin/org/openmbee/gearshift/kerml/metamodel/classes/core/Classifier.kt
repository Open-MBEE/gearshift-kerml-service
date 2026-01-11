package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Classifier metaclass.
 * Specializes: Type
 * A type that classifies instances.
 */
fun createClassifierMetaClass() = MetaClass(
    name = "Classifier",
    isAbstract = false,
    superclasses = listOf("Type"),
    attributes = emptyList(),
    description = "A type that classifies instances"
)
