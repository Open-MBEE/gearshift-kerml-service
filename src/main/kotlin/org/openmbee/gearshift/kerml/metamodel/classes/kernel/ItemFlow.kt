package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML ItemFlow metaclass.
 * Specializes: Step, Connector
 * A step and connector that represents the flow of items.
 */
fun createItemFlowMetaClass() = MetaClass(
    name = "ItemFlow",
    isAbstract = false,
    superclasses = listOf("Step", "Connector"),
    attributes = emptyList(),
    description = "A step and connector that represents the flow of items"
)
