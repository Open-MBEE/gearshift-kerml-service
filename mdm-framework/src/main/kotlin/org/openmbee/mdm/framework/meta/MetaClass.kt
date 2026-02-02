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
package org.openmbee.mdm.framework.meta

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
 *             lowerBound = 0
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
    val semanticBindings: List<SemanticBinding> = emptyList(),

    /**
     * If set, this class is an ownership intermediate.
     * The framework uses this to resolve the appropriate intermediate type
     * when establishing parent-child relationships.
     *
     * e.g., Membership declares:
     * - ownedElementEnd = "memberElement"
     * - ownerEnd = "membershipOwningNamespace"
     * - ownedElementType = "Element"
     * - ownerType = "Namespace"
     */
    @JsonProperty
    val ownershipBinding: OwnershipBinding? = null,

    @JsonProperty
    val isAbstract: Boolean = false,

    @JsonProperty
    val description: String? = null
) {
    /**
     * Whether this class acts as an ownership intermediate.
     */
    val isOwnershipIntermediate: Boolean
        get() = ownershipBinding != null
}
