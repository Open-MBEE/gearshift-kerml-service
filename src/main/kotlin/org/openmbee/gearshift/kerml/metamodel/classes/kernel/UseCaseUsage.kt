package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML UseCaseUsage metaclass.
 * Specializes: CaseUsage
 * A case usage representing a use case.
 */
fun createUseCaseUsageMetaClass() = MetaClass(
    name = "UseCaseUsage",
    isAbstract = false,
    superclasses = listOf("CaseUsage"),
    attributes = emptyList(),
    description = "A case usage representing a use case"
)
