package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML VerificationCaseUsage metaclass.
 * Specializes: CaseUsage
 * A case usage representing a verification case.
 */
fun createVerificationCaseUsageMetaClass() = MetaClass(
    name = "VerificationCaseUsage",
    isAbstract = false,
    superclasses = listOf("CaseUsage"),
    attributes = emptyList(),
    description = "A case usage representing a verification case"
)
