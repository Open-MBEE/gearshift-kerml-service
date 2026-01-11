package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML FlowConnectionUsage metaclass.
 * Specializes: ItemFlow, ConnectorAsUsage
 * An item flow used as a flow connection.
 */
fun createFlowConnectionUsageMetaClass() = MetaClass(
    name = "FlowConnectionUsage",
    isAbstract = false,
    superclasses = listOf("ItemFlow", "ConnectorAsUsage"),
    attributes = emptyList(),
    description = "An item flow used as a flow connection"
)
