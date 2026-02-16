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
 * Configuration for how ownership/containment relationships work in a metamodel.
 *
 * This defines the BASE ownership pattern. The framework automatically finds
 * more specific intermediate types by analyzing the metamodel's inheritance
 * and subset chains.
 *
 * Example for KerML:
 * - Base pattern: Namespace → OwningMembership → Element
 * - When adding Feature to Type, framework finds FeatureMembership (subsets OwningMembership)
 * - When adding Expression to Function, finds ResultExpressionMembership
 *
 * The resolution algorithm:
 * 1. Given parent type P and child type C
 * 2. Find all subtypes of intermediateType that have:
 *    - An association end whose type is P or a supertype of P
 *    - An association end whose type is C or a supertype of C
 * 3. Choose the most specific intermediate (deepest in inheritance)
 *
 * ```json
 * {
 *   "ownerType": "Namespace",
 *   "ownedType": "Element",
 *   "intermediateType": "OwningMembership",
 *   "ownerToIntermediateEnd": "ownedMembership",
 *   "intermediateToOwnedEnd": "memberElement",
 *   "intermediateToOwnerEnd": "membershipOwningNamespace"
 * }
 * ```
 */
data class OwnershipConfig(
    /**
     * The base type that can own/contain other elements.
     * e.g., "Namespace" in KerML
     */
    @JsonProperty(required = true)
    val ownerType: String,

    /**
     * The base type that can be owned/contained.
     * e.g., "Element" in KerML
     */
    @JsonProperty(required = true)
    val ownedType: String,

    /**
     * The base intermediate element type for ownership.
     * e.g., "OwningMembership" in KerML.
     * Null if ownership is direct (no intermediate).
     *
     * The framework will automatically find more specific subtypes
     * based on the actual parent/child types being connected.
     */
    @JsonProperty
    val intermediateType: String? = null,

    /**
     * Base association end from owner to intermediate.
     * e.g., "ownedMembership" - Namespace owns OwningMembership elements
     *
     * When a more specific intermediate is used, the framework finds
     * the corresponding subset end (e.g., "ownedFeatureMembership").
     */
    @JsonProperty
    val ownerToIntermediateEnd: String? = null,

    /**
     * Base association end from intermediate to owned element.
     * e.g., "memberElement" - OwningMembership references the owned Element
     *
     * When a more specific intermediate is used, the framework finds
     * the corresponding redefining end (e.g., "ownedMemberFeature").
     */
    @JsonProperty
    val intermediateToOwnedEnd: String? = null,

    /**
     * Base association end from intermediate back to owner.
     * e.g., "membershipOwningNamespace" - OwningMembership references owning Namespace
     *
     * When a more specific intermediate is used, the framework finds
     * the corresponding redefining end.
     */
    @JsonProperty
    val intermediateToOwnerEnd: String? = null,

    /**
     * For direct ownership (no intermediate), the association end from owner to owned.
     * e.g., "ownedElement" if Element directly owns children.
     * Used when intermediateType is null.
     */
    @JsonProperty
    val directOwnershipEnd: String? = null
) {
    /**
     * Whether this ownership pattern uses an intermediate element.
     */
    val usesIntermediate: Boolean
        get() = intermediateType != null

    /**
     * Validate the configuration is consistent.
     */
    fun validate(): List<String> {
        val errors = mutableListOf<String>()

        if (usesIntermediate) {
            if (ownerToIntermediateEnd == null) {
                errors.add("ownerToIntermediateEnd is required when intermediateType is set")
            }
            if (intermediateToOwnedEnd == null) {
                errors.add("intermediateToOwnedEnd is required when intermediateType is set")
            }
            if (intermediateToOwnerEnd == null) {
                errors.add("intermediateToOwnerEnd is required when intermediateType is set")
            }
        } else {
            if (directOwnershipEnd == null) {
                errors.add("directOwnershipEnd is required when intermediateType is not set")
            }
        }

        return errors
    }
}
