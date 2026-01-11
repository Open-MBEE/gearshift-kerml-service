package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML Expression metaclass.
 * Specializes: Step
 * A step that represents an expression.
 */
fun createExpressionMetaClass() = MetaClass(
    name = "Expression",
    isAbstract = false,
    superclasses = listOf("Step"),
    attributes = emptyList(),
    description = "A step that represents an expression"
)
