package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML CollectExpression metaclass.
 * Specializes: OperatorExpression
 * An expression that collects elements.
 */
fun createCollectExpressionMetaClass() = MetaClass(
    name = "CollectExpression",
    isAbstract = false,
    superclasses = listOf("OperatorExpression"),
    attributes = emptyList(),
    description = "An expression that collects elements"
)
