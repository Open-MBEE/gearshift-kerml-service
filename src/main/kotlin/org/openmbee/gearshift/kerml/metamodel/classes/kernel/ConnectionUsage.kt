package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML ConnectionUsage metaclass.
 * Specializes: ConnectorAsUsage, PartUsage
 * A connector usage that is also a part usage.
 */
fun createConnectionUsageMetaClass() = MetaClass(
    name = "ConnectionUsage",
    isAbstract = false,
    superclasses = listOf("ConnectorAsUsage", "PartUsage"),
    attributes = emptyList(),
    description = "A connector usage that is also a part usage"
)
