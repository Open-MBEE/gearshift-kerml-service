package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Connector metaclass.
 * Specializes: Feature, Relationship
 * A feature and relationship that represents a connector.
 */
fun createConnectorMetaClass() = MetaClass(
    name = "Connector",
    isAbstract = false,
    superclasses = listOf("Feature", "Relationship"),
    attributes = emptyList(),
    description = "A feature and relationship that represents a connector"
)
