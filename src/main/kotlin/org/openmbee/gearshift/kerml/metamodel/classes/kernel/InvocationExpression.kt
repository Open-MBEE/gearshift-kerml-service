package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML InvocationExpression metaclass.
 * Specializes: Expression
 * An expression that invokes a behavior or function.
 */
fun createInvocationExpressionMetaClass() = MetaClass(
    name = "InvocationExpression",
    isAbstract = false,
    superclasses = listOf("Expression"),
    attributes = emptyList(),
    description = "An expression that invokes a behavior or function"
)
