package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML AttributeUsage metaclass.
 * Specializes: Usage
 * A usage that represents an attribute.
 */
fun createAttributeUsageMetaClass() = MetaClass(
    name = "AttributeUsage",
    isAbstract = false,
    superclasses = listOf("Usage"),
    attributes = emptyList(),
    description = "A usage that represents an attribute"
)
