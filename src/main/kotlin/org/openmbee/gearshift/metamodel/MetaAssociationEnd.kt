package org.openmbee.gearshift.metamodel

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents one end of an association in the metamodel.
 */
data class MetaAssociationEnd(
    @JsonProperty(required = true)
    val name: String,

    @JsonProperty(required = true)
    val type: String,

    @JsonProperty
    val multiplicity: String = "1",

    @JsonProperty
    val isNavigable: Boolean = true,

    @JsonProperty
    val aggregation: AggregationKind = AggregationKind.NONE,

    @JsonProperty
    val isOrdered: Boolean = false,

    @JsonProperty
    val isUnique: Boolean = true
)
