package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML ItemUsage metaclass.
 * Specializes: OccurrenceUsage
 * A usage of an item.
 */
fun createItemUsageMetaClass() = MetaClass(
    name = "ItemUsage",
    isAbstract = false,
    superclasses = listOf("OccurrenceUsage"),
    attributes = emptyList(),
    description = "A usage of an item"
)
