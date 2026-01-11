package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML UseCaseDefinition metaclass.
 * Specializes: CaseDefinition
 * A definition of a use case.
 */
fun createUseCaseDefinitionMetaClass() = MetaClass(
    name = "UseCaseDefinition",
    isAbstract = false,
    superclasses = listOf("CaseDefinition"),
    attributes = emptyList(),
    description = "A definition of a use case"
)
