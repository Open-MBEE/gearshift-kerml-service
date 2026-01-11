package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Specialization metaclass.
 * Specializes: Relationship
 * A relationship that makes one type a specialization of another.
 */
fun createSpecializationMetaClass() = MetaClass(
    name = "Specialization",
    isAbstract = false,
    superclasses = listOf("Relationship"),
    attributes = emptyList(),
    description = "A relationship that makes one type a specialization of another"
)
