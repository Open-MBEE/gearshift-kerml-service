package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Interaction metaclass.
 * Specializes: Behavior, Association
 * A behavior that is also an association representing an interaction.
 */
fun createInteractionMetaClass() = MetaClass(
    name = "Interaction",
    isAbstract = false,
    superclasses = listOf("Behavior", "Association"),
    attributes = emptyList(),
    description = "A behavior that is also an association representing an interaction"
)
