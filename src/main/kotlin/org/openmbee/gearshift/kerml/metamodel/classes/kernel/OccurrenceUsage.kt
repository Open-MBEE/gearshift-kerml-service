package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaProperty

/**
 * KerML OccurrenceUsage metaclass.
 * Specializes: Usage
 * A usage that represents an occurrence.
 */
fun createOccurrenceUsageMetaClass() = MetaClass(
    name = "OccurrenceUsage",
    isAbstract = false,
    superclasses = listOf("Usage"),
    attributes = listOf(
        MetaProperty(
            name = "isIndividual",
            type = "Boolean",
            multiplicity = "1",
            description = "Whether this occurrence represents an individual"
        )
    ),
    description = "A usage that represents an occurrence"
)
