package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML VerificationCaseDefinition metaclass.
 * Specializes: CaseDefinition
 * A definition of a verification case.
 */
fun createVerificationCaseDefinitionMetaClass() = MetaClass(
    name = "VerificationCaseDefinition",
    isAbstract = false,
    superclasses = listOf("CaseDefinition"),
    attributes = emptyList(),
    description = "A definition of a verification case"
)
