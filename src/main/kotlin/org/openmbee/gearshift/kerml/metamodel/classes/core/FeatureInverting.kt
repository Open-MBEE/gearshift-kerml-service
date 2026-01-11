package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML FeatureInverting metaclass.
 * Specializes: Relationship
 * A relationship that specifies a feature as the inverse of another.
 */
fun createFeatureInvertingMetaClass() = MetaClass(
    name = "FeatureInverting",
    isAbstract = false,
    superclasses = listOf("Relationship"),
    attributes = emptyList(),
    description = "A relationship that specifies a feature as the inverse of another"
)
