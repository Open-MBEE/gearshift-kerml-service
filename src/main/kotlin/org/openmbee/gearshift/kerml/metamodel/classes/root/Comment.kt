package org.openmbee.gearshift.kerml.metamodel.classes.root

import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaProperty

/**
 * KerML Comment metaclass.
 * Specializes: AnnotatingElement
 * A textual comment that annotates elements.
 */
fun createCommentMetaClass() = MetaClass(
    name = "Comment",
    isAbstract = false,
    superclasses = listOf("AnnotatingElement"),
    attributes = listOf(
        MetaProperty(
            name = "body",
            type = "String",
            multiplicity = "1",
            description = "The text of the comment"
        ),
        MetaProperty(
            name = "locale",
            type = "String",
            multiplicity = "0..1",
            description = "The locale of the comment text"
        )
    ),
    description = "A textual comment that annotates elements"
)
