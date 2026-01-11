package org.openmbee.gearshift.metamodel

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents an association between two metaclasses in the metamodel.
 */
data class MetaAssociation(
    @JsonProperty(required = true)
    val name: String,

    @JsonProperty(required = true)
    val sourceEnd: MetaAssociationEnd,

    @JsonProperty(required = true)
    val targetEnd: MetaAssociationEnd,

    @JsonProperty
    val isDerived: Boolean = false,

    @JsonProperty
    val description: String? = null
)
