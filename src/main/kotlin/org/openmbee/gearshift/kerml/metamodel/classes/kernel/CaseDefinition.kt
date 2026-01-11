package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML CaseDefinition metaclass.
 * Specializes: CalculationDefinition
 * A definition of a case.
 */
fun createCaseDefinitionMetaClass() = MetaClass(
    name = "CaseDefinition",
    isAbstract = false,
    superclasses = listOf("CalculationDefinition"),
    attributes = emptyList(),
    description = "A definition of a case"
)
