package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML SelectExpression metaclass.
 * Specializes: OperatorExpression
 * An expression that selects elements based on a condition.
 */
fun createSelectExpressionMetaClass() = MetaClass(
    name = "SelectExpression",
    isAbstract = false,
    superclasses = listOf("OperatorExpression"),
    attributes = emptyList(),
    description = "An expression that selects elements based on a condition"
)
