package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML NullExpression metaclass.
 * Specializes: Expression
 * An expression representing a null value.
 */
fun createNullExpressionMetaClass() = MetaClass(
    name = "NullExpression",
    isAbstract = false,
    superclasses = listOf("Expression"),
    attributes = emptyList(),
    description = "An expression representing a null value"
)
