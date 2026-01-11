package org.openmbee.gearshift.kerml.metamodel.classes.kernel

import org.openmbee.gearshift.metamodel.MetaClass
import org.openmbee.gearshift.metamodel.MetaProperty

/**
 * KerML LiteralString metaclass.
 * Specializes: LiteralExpression
 * A literal expression with a string value.
 */
fun createLiteralStringMetaClass() = MetaClass(
    name = "LiteralString",
    isAbstract = false,
    superclasses = listOf("LiteralExpression"),
    attributes = listOf(
        MetaProperty(
            name = "value",
            type = "String",
            multiplicity = "1",
            description = "The string value"
        )
    ),
    description = "A literal expression with a string value"
)
