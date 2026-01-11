package org.openmbee.gearshift.kerml.metamodel.classes.core

import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaProperty

/**
 * KerML Type metaclass.
 * Specializes: Namespace
 * A namespace that can be specialized.
 */
fun createTypeMetaClass() = MetaClass(
    name = "Type",
    isAbstract = false,
    superclasses = listOf("Namespace"),
    attributes = listOf(
        MetaProperty(
            name = "isAbstract",
            type = "Boolean",
            multiplicity = "1",
            description = "Whether this type is abstract"
        ),
        MetaProperty(
            name = "isSufficient",
            type = "Boolean",
            multiplicity = "1",
            description = "Whether this type is sufficient"
        )
    ),
    description = "A namespace that can be specialized"
)
