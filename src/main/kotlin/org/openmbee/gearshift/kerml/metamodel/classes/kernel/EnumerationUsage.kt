package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML EnumerationUsage metaclass.
 * Specializes: AttributeUsage
 * An attribute usage for an enumeration value.
 */
fun createEnumerationUsageMetaClass() = MetaClass(
    name = "EnumerationUsage",
    isAbstract = false,
    superclasses = listOf("AttributeUsage"),
    attributes = emptyList(),
    description = "An attribute usage for an enumeration value"
)
