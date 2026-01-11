package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Step metaclass.
 * Specializes: Feature
 * A feature that represents a step in a behavior.
 */
fun createStepMetaClass() = MetaClass(
    name = "Step",
    isAbstract = false,
    superclasses = listOf("Feature"),
    attributes = emptyList(),
    description = "A feature that represents a step in a behavior"
)
