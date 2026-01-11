package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML AnalysisCaseUsage metaclass.
 * Specializes: CaseUsage
 * A case usage representing an analysis case.
 */
fun createAnalysisCaseUsageMetaClass() = MetaClass(
    name = "AnalysisCaseUsage",
    isAbstract = false,
    superclasses = listOf("CaseUsage"),
    attributes = emptyList(),
    description = "A case usage representing an analysis case"
)
