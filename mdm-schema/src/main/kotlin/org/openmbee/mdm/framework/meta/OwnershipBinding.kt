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

/**
 * Declares that a metaclass acts as an ownership intermediate.
 *
 * Ownership intermediates are classes that sit between an owner and the "real"
 * owned element. In KerML, Membership classes serve this role:
 *
 * ```
 * Namespace --[ownedMembership]--> Membership --[memberElement]--> Element
 * ```
 *
 * The binding only specifies the association end names. The actual type restrictions
 * (ownerType, ownedElementType) are derived from the association end declarations
 * in the metamodel. This ensures the binding always reflects the actual constraints.
 *
 * By declaring ownership bindings on intermediate classes, the framework can:
 * 1. Automatically determine the appropriate intermediate type when adding a child
 * 2. Find the most specific intermediate based on inheritance (e.g., FeatureMembership for Feature)
 * 3. Wire both ends of the ownership relationship correctly
 *
 * @property ownedElementEnd The association end pointing to the owned element (e.g., "ownedMemberElement")
 * @property ownerEnd The association end pointing to the owner (e.g., "membershipOwningNamespace")
 */
data class OwnershipBinding(
    /**
     * The association end on this intermediate that points to the owned element.
     * The type is derived from the association end's declared type.
     * e.g., "ownedMemberElement" on OwningMembership -> type is Element
     * e.g., "ownedMemberFeature" on FeatureMembership -> type is Feature
     */
    val ownedElementEnd: String,

    /**
     * The association end on this intermediate that points back to the owner.
     * The type is derived from the association end's declared type.
     * e.g., "membershipOwningNamespace" on Membership -> type is Namespace
     * e.g., "owningType" on FeatureMembership -> type is Type
     */
    val ownerEnd: String
)
