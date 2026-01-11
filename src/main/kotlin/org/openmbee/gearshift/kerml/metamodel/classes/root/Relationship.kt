package org.openmbee.gearshift.kerml.metamodel.classes.root

import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaProperty

/**
 * KerML Relationship metaclass.
 * Specializes: Element
 * An abstract base class for all relationships between elements.
 */
fun createRelationshipMetaClass() = MetaClass(
    name = "Relationship",
    isAbstract = true,
    superclasses = listOf("Element"),
    attributes = listOf(
        MetaProperty(
            name = "isImplied",
            type = "Boolean",
            multiplicity = "1",
            description = "Whether this Relationship is implied or explicitly stated"
        )
    ),
    description = "An abstract base class for all relationships between elements"
)
