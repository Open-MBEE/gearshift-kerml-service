package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaProperty

/**
 * KerML LiteralRational metaclass.
 * Specializes: LiteralExpression
 * A literal expression with a rational number value.
 */
fun createLiteralRationalMetaClass() = MetaClass(
    name = "LiteralRational",
    isAbstract = false,
    superclasses = listOf("LiteralExpression"),
    attributes = listOf(
        MetaProperty(
            name = "value",
            type = "String",
            multiplicity = "1",
            description = "The rational value as a string"
        )
    ),
    description = "A literal expression with a rational number value"
)
