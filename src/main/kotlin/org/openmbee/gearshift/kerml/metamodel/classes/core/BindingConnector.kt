package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML BindingConnector metaclass.
 * Specializes: Connector
 * A connector that binds features together.
 */
fun createBindingConnectorMetaClass() = MetaClass(
    name = "BindingConnector",
    isAbstract = false,
    superclasses = listOf("Connector"),
    attributes = emptyList(),
    description = "A connector that binds features together"
)
