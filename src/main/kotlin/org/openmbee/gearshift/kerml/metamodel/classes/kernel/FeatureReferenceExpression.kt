package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML FeatureReferenceExpression metaclass.
 * Specializes: Expression
 * An expression that references a feature.
 */
fun createFeatureReferenceExpressionMetaClass() = MetaClass(
    name = "FeatureReferenceExpression",
    isAbstract = false,
    superclasses = listOf("Expression"),
    attributes = emptyList(),
    description = "An expression that references a feature"
)
