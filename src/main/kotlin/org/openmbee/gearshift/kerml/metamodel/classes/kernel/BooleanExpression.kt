package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML BooleanExpression metaclass.
 * Specializes: Expression
 * An expression that evaluates to a boolean value.
 */
fun createBooleanExpressionMetaClass() = MetaClass(
    name = "BooleanExpression",
    isAbstract = false,
    superclasses = listOf("Expression"),
    attributes = emptyList(),
    description = "An expression that evaluates to a boolean value"
)
