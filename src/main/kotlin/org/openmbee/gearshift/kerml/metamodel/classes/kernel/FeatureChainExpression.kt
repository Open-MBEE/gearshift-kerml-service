package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML FeatureChainExpression metaclass.
 * Specializes: OperatorExpression
 * An expression that chains feature accesses.
 */
fun createFeatureChainExpressionMetaClass() = MetaClass(
    name = "FeatureChainExpression",
    isAbstract = false,
    superclasses = listOf("OperatorExpression"),
    attributes = emptyList(),
    description = "An expression that chains feature accesses"
)
