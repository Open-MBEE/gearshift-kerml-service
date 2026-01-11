package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Predicate metaclass.
 * Specializes: Function
 * A function that returns a boolean result.
 */
fun createPredicateMetaClass() = MetaClass(
    name = "Predicate",
    isAbstract = false,
    superclasses = listOf("Function"),
    attributes = emptyList(),
    description = "A function that returns a boolean result"
)
