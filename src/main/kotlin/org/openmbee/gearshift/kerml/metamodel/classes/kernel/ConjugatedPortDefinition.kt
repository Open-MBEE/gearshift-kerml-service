package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML ConjugatedPortDefinition metaclass.
 * Specializes: PortDefinition
 * A definition of a conjugated port.
 */
fun createConjugatedPortDefinitionMetaClass() = MetaClass(
    name = "ConjugatedPortDefinition",
    isAbstract = false,
    superclasses = listOf("PortDefinition"),
    attributes = emptyList(),
    description = "A definition of a conjugated port"
)
