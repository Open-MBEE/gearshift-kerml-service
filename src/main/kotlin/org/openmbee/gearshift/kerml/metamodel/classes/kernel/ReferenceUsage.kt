package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML ReferenceUsage metaclass.
 * Specializes: Usage
 * A usage that references another element.
 */
fun createReferenceUsageMetaClass() = MetaClass(
    name = "ReferenceUsage",
    isAbstract = false,
    superclasses = listOf("Usage"),
    attributes = emptyList(),
    description = "A usage that references another element"
)
