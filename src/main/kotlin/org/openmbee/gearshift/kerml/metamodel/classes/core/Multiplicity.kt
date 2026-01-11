package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Multiplicity metaclass.
 * Specializes: Feature
 * A feature that specifies the multiplicity of another feature.
 */
fun createMultiplicityMetaClass() = MetaClass(
    name = "Multiplicity",
    isAbstract = false,
    superclasses = listOf("Feature"),
    attributes = emptyList(),
    description = "A feature that specifies the multiplicity of another feature"
)
