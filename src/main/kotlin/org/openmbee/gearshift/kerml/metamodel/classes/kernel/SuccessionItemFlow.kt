package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML SuccessionItemFlow metaclass.
 * Specializes: ItemFlow, Succession
 * An item flow that is also a succession.
 */
fun createSuccessionItemFlowMetaClass() = MetaClass(
    name = "SuccessionItemFlow",
    isAbstract = false,
    superclasses = listOf("ItemFlow", "Succession"),
    attributes = emptyList(),
    description = "An item flow that is also a succession"
)
