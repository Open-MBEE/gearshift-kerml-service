package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * KerML MetadataAccessExpression metaclass.
 * Specializes: Expression
 * An expression that accesses metadata.
 */
fun createMetadataAccessExpressionMetaClass() = MetaClass(
    name = "MetadataAccessExpression",
    isAbstract = false,
    superclasses = listOf("Expression"),
    attributes = emptyList(),
    description = "An expression that accesses metadata"
)
