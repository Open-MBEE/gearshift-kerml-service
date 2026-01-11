package org.openmbee.gearshift.kerml.metamodel.classes.root

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML AnnotatingElement metaclass.
 * Specializes: Element
 * An element that annotates other elements.
 */
fun createAnnotatingElementMetaClass() = MetaClass(
    name = "AnnotatingElement",
    isAbstract = false,
    superclasses = listOf("Element"),
    attributes = emptyList(),
    description = "An element that annotates other elements"
)
