package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML ActionDefinition metaclass.
 * Specializes: OccurrenceDefinition, Behavior
 * A definition of an action.
 */
fun createActionDefinitionMetaClass() = MetaClass(
    name = "ActionDefinition",
    isAbstract = false,
    superclasses = listOf("OccurrenceDefinition", "Behavior"),
    attributes = emptyList(),
    description = "A definition of an action"
)
