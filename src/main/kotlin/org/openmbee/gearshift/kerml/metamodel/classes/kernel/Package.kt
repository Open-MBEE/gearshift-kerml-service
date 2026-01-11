package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Package metaclass.
 * Specializes: Namespace
 * A namespace that groups related elements.
 */
fun createPackageMetaClass() = MetaClass(
    name = "Package",
    isAbstract = false,
    superclasses = listOf("Namespace"),
    attributes = emptyList(),
    description = "A namespace that groups related elements"
)
