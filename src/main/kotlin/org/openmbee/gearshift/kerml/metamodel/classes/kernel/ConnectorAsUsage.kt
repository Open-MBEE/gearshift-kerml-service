package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML ConnectorAsUsage metaclass.
 * Specializes: Usage, Connector
 * A usage that is also a connector.
 */
fun createConnectorAsUsageMetaClass() = MetaClass(
    name = "ConnectorAsUsage",
    isAbstract = false,
    superclasses = listOf("Usage", "Connector"),
    attributes = emptyList(),
    description = "A usage that is also a connector"
)
