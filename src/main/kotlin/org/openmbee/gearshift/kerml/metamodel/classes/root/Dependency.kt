package org.openmbee.gearshift.kerml.metamodel.classes.root

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Dependency metaclass.
 * Specializes: Relationship
 * A relationship indicating that one element depends on another.
 */
fun createDependencyMetaClass() = MetaClass(
    name = "Dependency",
    isAbstract = false,
    superclasses = listOf("Relationship"),
    attributes = emptyList(),
    description = "A relationship indicating that one element depends on another"
)
