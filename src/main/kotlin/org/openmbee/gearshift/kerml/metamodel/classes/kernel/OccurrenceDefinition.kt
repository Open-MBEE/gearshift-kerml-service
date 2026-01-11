package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaProperty

/**
 * KerML OccurrenceDefinition metaclass.
 * Specializes: Definition, Class
 * A definition of an occurrence.
 */
fun createOccurrenceDefinitionMetaClass() = MetaClass(
    name = "OccurrenceDefinition",
    isAbstract = false,
    superclasses = listOf("Definition", "Class"),
    attributes = listOf(
        MetaProperty(
            name = "isIndividual",
            type = "Boolean",
            multiplicity = "1",
            description = "Whether this defines an individual occurrence"
        )
    ),
    description = "A definition of an occurrence"
)
