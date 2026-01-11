package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML CaseUsage metaclass.
 * Specializes: CalculationUsage
 * A calculation usage representing a case.
 */
fun createCaseUsageMetaClass() = MetaClass(
    name = "CaseUsage",
    isAbstract = false,
    superclasses = listOf("CalculationUsage"),
    attributes = emptyList(),
    description = "A calculation usage representing a case"
)
