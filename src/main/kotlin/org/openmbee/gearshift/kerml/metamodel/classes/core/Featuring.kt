package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Featuring metaclass.
 * Specializes: Relationship
 * A relationship that features a type with features.
 */
fun createFeaturingMetaClass() = MetaClass(
    name = "Featuring",
    isAbstract = false,
    superclasses = listOf("Relationship"),
    attributes = emptyList(),
    description = "A relationship that features a type with features"
)
