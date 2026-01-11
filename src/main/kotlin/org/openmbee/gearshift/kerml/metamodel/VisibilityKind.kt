package org.openmbee.gearshift.kerml.metamodel

import com.fasterxml.jackson.annotation.JsonValue

/**
 * KerML VisibilityKind enumeration.
 * Specifies the visibility of a Membership or Import.
 */
enum class VisibilityKind(@JsonValue val value: String) {
    PRIVATE("private"),
    PROTECTED("protected"),
    PUBLIC("public")
}
