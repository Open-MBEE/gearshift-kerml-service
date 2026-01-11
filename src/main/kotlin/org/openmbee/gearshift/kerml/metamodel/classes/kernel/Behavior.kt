package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Behavior metaclass.
 * Specializes: Class
 * A class that represents behavior.
 */
fun createBehaviorMetaClass() = MetaClass(
    name = "Behavior",
    isAbstract = false,
    superclasses = listOf("Class"),
    attributes = emptyList(),
    description = "A class that represents behavior"
)
