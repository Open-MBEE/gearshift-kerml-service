package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Disjoining metaclass.
 * Specializes: Relationship
 * A relationship that specifies two types are disjoint.
 */
fun createDisjoiningMetaClass() = MetaClass(
    name = "Disjoining",
    isAbstract = false,
    superclasses = listOf("Relationship"),
    attributes = emptyList(),
    description = "A relationship that specifies two types are disjoint"
)
