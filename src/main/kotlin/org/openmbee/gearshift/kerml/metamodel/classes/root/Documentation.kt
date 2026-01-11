package org.openmbee.gearshift.kerml.metamodel.classes.root

import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaProperty

/**
 * KerML Documentation metaclass.
 * Specializes: Comment
 * Formal documentation for an element.
 */
fun createDocumentationMetaClass() = MetaClass(
    name = "Documentation",
    isAbstract = false,
    superclasses = listOf("Comment"),
    attributes = emptyList(),
    description = "Formal documentation for an element"
)
