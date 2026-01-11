package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML CalculationDefinition metaclass.
 * Specializes: ActionDefinition, Function
 * A definition of a calculation.
 */
fun createCalculationDefinitionMetaClass() = MetaClass(
    name = "CalculationDefinition",
    isAbstract = false,
    superclasses = listOf("ActionDefinition", "Function"),
    attributes = emptyList(),
    description = "A definition of a calculation"
)
