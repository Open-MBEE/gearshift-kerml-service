package org.openmbee.gearshift.kerml.metamodel.classes.root

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Namespace metaclass.
 * Specializes: Element
 * An element that can contain other elements as members.
 */
fun createNamespaceMetaClass() = MetaClass(
    name = "Namespace",
    isAbstract = false,
    superclasses = listOf("Element"),
    attributes = emptyList(),
    description = "An element that can contain other elements as members"
)
