package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Succession metaclass.
 * Specializes: Connector
 * A connector that represents succession between steps.
 */
fun createSuccessionMetaClass() = MetaClass(
    name = "Succession",
    isAbstract = false,
    superclasses = listOf("Connector"),
    attributes = emptyList(),
    description = "A connector that represents succession between steps"
)
