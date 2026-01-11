package org.openmbee.gearshift.kerml.metamodel.classes.root

import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaProperty

/**
 * KerML TextualRepresentation metaclass.
 * Specializes: AnnotatingElement
 * A textual representation of an element in a specific language.
 */
fun createTextualRepresentationMetaClass() = MetaClass(
    name = "TextualRepresentation",
    isAbstract = false,
    superclasses = listOf("AnnotatingElement"),
    attributes = listOf(
        MetaProperty(
            name = "language",
            type = "String",
            multiplicity = "1",
            description = "The language of the textual representation"
        ),
        MetaProperty(
            name = "body",
            type = "String",
            multiplicity = "1",
            description = "The text of the representation"
        )
    ),
    description = "A textual representation of an element in a specific language"
)
