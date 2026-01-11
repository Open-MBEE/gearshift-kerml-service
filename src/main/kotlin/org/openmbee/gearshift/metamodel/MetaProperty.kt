/*
 * Copyright 2026 Charles Galey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openmbee.gearshift.metamodel

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents a property (attribute or association end) in the metamodel.
 *
 * Example usage:
 * ```kotlin
 * val nameProperty = MetaProperty(
 *     name = "name",
 *     type = "String",
 *     multiplicity = "0..1"
 * )
 * ```
 */
data class MetaProperty(
    @JsonProperty(required = true)
    val name: String,

    @JsonProperty(required = true)
    val type: String,

    @JsonProperty
    val isUnion: Boolean = false,

    @JsonProperty
    val isUnique: Boolean = true,

    @JsonProperty
    val isDerived: Boolean = false,

    @JsonProperty
    val aggregation: AggregationKind = AggregationKind.NONE,

    @JsonProperty
    val isNavigable: Boolean = true,

    @JsonProperty
    val isOrdered: Boolean = false,

    @JsonProperty
    val redefines: List<String> = emptyList(),

    @JsonProperty
    val subsets: List<String> = emptyList(),

    @JsonProperty
    val derivationConstraint: String? = null,

    @JsonProperty
    val multiplicity: String = "1",

    @JsonProperty
    val isReadOnly: Boolean = false,

    @JsonProperty
    val defaultValue: String? = null
)
