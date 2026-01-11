package org.openmbee.gearshift.metamodel

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents a constraint in the metamodel.
 * Can be expressed using OCL or other constraint languages.
 */
data class MetaConstraint(
    @JsonProperty(required = true)
    val name: String,

    @JsonProperty
    val language: String = "OCL",

    @JsonProperty(required = true)
    val expression: String,

    @JsonProperty
    val description: String? = null
)
