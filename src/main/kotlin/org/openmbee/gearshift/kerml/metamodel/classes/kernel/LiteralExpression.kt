package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML LiteralExpression metaclass.
 * Specializes: Expression
 * An expression that represents a literal value.
 */
fun createLiteralExpressionMetaClass() = MetaClass(
    name = "LiteralExpression",
    isAbstract = true,
    superclasses = listOf("Expression"),
    attributes = emptyList(),
    description = "An expression that represents a literal value"
)
