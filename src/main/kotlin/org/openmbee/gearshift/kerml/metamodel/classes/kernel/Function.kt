package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Function metaclass.
 * Specializes: Behavior
 * A behavior that represents a function.
 */
fun createFunctionMetaClass() = MetaClass(
    name = "Function",
    isAbstract = false,
    superclasses = listOf("Behavior"),
    attributes = emptyList(),
    description = "A behavior that represents a function"
)
