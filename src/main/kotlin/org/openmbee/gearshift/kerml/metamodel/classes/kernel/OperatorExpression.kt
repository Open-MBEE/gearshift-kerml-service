package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaProperty

/**
 * KerML OperatorExpression metaclass.
 * Specializes: InvocationExpression
 * An expression that uses an operator.
 */
fun createOperatorExpressionMetaClass() = MetaClass(
    name = "OperatorExpression",
    isAbstract = false,
    superclasses = listOf("InvocationExpression"),
    attributes = listOf(
        MetaProperty(
            name = "operator",
            type = "String",
            multiplicity = "1",
            description = "The operator symbol"
        )
    ),
    description = "An expression that uses an operator"
)
