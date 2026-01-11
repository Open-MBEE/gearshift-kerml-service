package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaProperty

/**
 * KerML LiteralBoolean metaclass.
 * Specializes: LiteralExpression
 * A literal expression with a boolean value.
 */
fun createLiteralBooleanMetaClass() = MetaClass(
    name = "LiteralBoolean",
    isAbstract = false,
    superclasses = listOf("LiteralExpression"),
    attributes = listOf(
        MetaProperty(
            name = "value",
            type = "Boolean",
            multiplicity = "1",
            description = "The boolean value"
        )
    ),
    description = "A literal expression with a boolean value"
)
