package org.openmbee.gearshift.kerml.metamodel

import com.fasterxml.jackson.annotation.JsonValue

/**
 * KerML FeatureDirectionKind enumeration.
 * Specifies the direction of a Feature (parameter direction).
 */
enum class FeatureDirectionKind(@JsonValue val value: String) {
    IN("in"),
    INOUT("inout"),
    OUT("out")
}
