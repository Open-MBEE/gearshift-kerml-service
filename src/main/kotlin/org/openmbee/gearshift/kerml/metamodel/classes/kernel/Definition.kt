package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaProperty

/**
 * KerML Definition metaclass.
 * Specializes: Classifier
 * A classifier that defines something.
 */
fun createDefinitionMetaClass() = MetaClass(
    name = "Definition",
    isAbstract = false,
    superclasses = listOf("Classifier"),
    attributes = listOf(
        MetaProperty(
            name = "isVariation",
            type = "Boolean",
            multiplicity = "1",
            description = "Whether this is a variation definition"
        )
    ),
    description = "A classifier that defines something"
)
