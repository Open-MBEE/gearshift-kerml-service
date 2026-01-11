package org.openmbee.gearshift.metamodel

import com.fasterxml.jackson.annotation.JsonValue

/**
 * Enumeration defining the aggregation kind for properties and associations.
 */
enum class AggregationKind(@JsonValue val value: String) {
    NONE("none"),
    SHARED("shared"),
    COMPOSITE("composite")
}
