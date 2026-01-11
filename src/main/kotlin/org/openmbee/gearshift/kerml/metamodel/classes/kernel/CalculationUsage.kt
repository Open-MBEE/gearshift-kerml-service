package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML CalculationUsage metaclass.
 * Specializes: ActionUsage, Expression
 * An action usage that is also an expression.
 */
fun createCalculationUsageMetaClass() = MetaClass(
    name = "CalculationUsage",
    isAbstract = false,
    superclasses = listOf("ActionUsage", "Expression"),
    attributes = emptyList(),
    description = "An action usage that is also an expression"
)
