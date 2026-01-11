package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaProperty

/**
 * KerML LiteralInteger metaclass.
 * Specializes: LiteralExpression
 * A literal expression with an integer value.
 */
fun createLiteralIntegerMetaClass() = MetaClass(
    name = "LiteralInteger",
    isAbstract = false,
    superclasses = listOf("LiteralExpression"),
    attributes = listOf(
        MetaProperty(
            name = "value",
            type = "Integer",
            multiplicity = "1",
            description = "The integer value"
        )
    ),
    description = "A literal expression with an integer value"
)
