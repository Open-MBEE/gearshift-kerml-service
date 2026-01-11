package org.openmbee.gearshift.kerml.metamodel.classes.root

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Annotation metaclass.
 * Specializes: Relationship
 * A relationship between an annotating element and the element being annotated.
 */
fun createAnnotationMetaClass() = MetaClass(
    name = "Annotation",
    isAbstract = false,
    superclasses = listOf("Relationship"),
    attributes = emptyList(),
    description = "A relationship between an annotating element and the element being annotated"
)
