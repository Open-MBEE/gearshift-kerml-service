package org.openmbee.gearshift.metamodel

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents a parameter for a MetaOperation.
 */
data class MetaParameter(
    @JsonProperty(required = true)
    val name: String,

    @JsonProperty(required = true)
    val type: String,

    @JsonProperty
    val defaultValue: String? = null
)
