package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Usage metaclass.
 * Specializes: Feature
 * A feature that represents a usage.
 */
fun createUsageMetaClass() = MetaClass(
    name = "Usage",
    isAbstract = false,
    superclasses = listOf("Feature"),
    attributes = emptyList(),
    description = "A feature that represents a usage"
)
