package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML PartUsage metaclass.
 * Specializes: ItemUsage
 * A usage of a part.
 */
fun createPartUsageMetaClass() = MetaClass(
    name = "PartUsage",
    isAbstract = false,
    superclasses = listOf("ItemUsage"),
    attributes = emptyList(),
    description = "A usage of a part"
)
