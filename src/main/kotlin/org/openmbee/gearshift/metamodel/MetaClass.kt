package org.openmbee.gearshift.metamodel

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents a class in the KerML metamodel.
 * This is the core metadata structure that defines a type.
 *
 * Example usage:
 * ```kotlin
 * val element = MetaClass(
 *     name = "Element",
 *     isAbstract = true,
 *     attributes = listOf(
 *         MetaProperty(
 *             name = "name",
 *             type = "String",
 *             multiplicity = "0..1"
 *         ),
 *         MetaProperty(
 *             name = "qualifiedName",
 *             type = "String",
 *             isDerived = true,
 *             isReadOnly = true
 *         )
 *     )
 * )
 * ```
 */
data class MetaClass(
    @JsonProperty(required = true)
    val name: String,

    @JsonProperty
    val superclasses: List<String> = emptyList(),

    @JsonProperty
    val attributes: List<MetaProperty> = emptyList(),

    @JsonProperty
    val constraints: List<MetaConstraint> = emptyList(),

    @JsonProperty
    val operations: List<MetaOperation> = emptyList(),

    @JsonProperty
    val isAbstract: Boolean = false,

    @JsonProperty
    val description: String? = null
)
