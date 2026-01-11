package org.openmbee.gearshift.metamodel

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents an operation (method) in the metamodel.
 */
data class MetaOperation(
    @JsonProperty(required = true)
    val name: String,

    @JsonProperty
    val returnType: String? = null,

    @JsonProperty
    val parameters: List<MetaParameter> = emptyList(),

    @JsonProperty
    val body: String? = null,

    @JsonProperty
    val description: String? = null,

    @JsonProperty
    val isQuery: Boolean = false
)
