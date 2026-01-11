package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Conjugation metaclass.
 * Specializes: Relationship
 * A relationship that creates a conjugated type.
 */
fun createConjugationMetaClass() = MetaClass(
    name = "Conjugation",
    isAbstract = false,
    superclasses = listOf("Relationship"),
    attributes = emptyList(),
    description = "A relationship that creates a conjugated type"
)
