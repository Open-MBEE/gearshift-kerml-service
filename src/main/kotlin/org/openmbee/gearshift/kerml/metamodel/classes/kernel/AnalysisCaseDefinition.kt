package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML AnalysisCaseDefinition metaclass.
 * Specializes: CaseDefinition
 * A definition of an analysis case.
 */
fun createAnalysisCaseDefinitionMetaClass() = MetaClass(
    name = "AnalysisCaseDefinition",
    isAbstract = false,
    superclasses = listOf("CaseDefinition"),
    attributes = emptyList(),
    description = "A definition of an analysis case"
)
