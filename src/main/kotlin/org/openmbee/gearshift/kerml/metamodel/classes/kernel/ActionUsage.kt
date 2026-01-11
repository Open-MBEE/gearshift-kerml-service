package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML ActionUsage metaclass.
 * Specializes: OccurrenceUsage, Step
 * An occurrence usage that is also a step representing an action.
 */
fun createActionUsageMetaClass() = MetaClass(
    name = "ActionUsage",
    isAbstract = false,
    superclasses = listOf("OccurrenceUsage", "Step"),
    attributes = emptyList(),
    description = "An occurrence usage that is also a step representing an action"
)
