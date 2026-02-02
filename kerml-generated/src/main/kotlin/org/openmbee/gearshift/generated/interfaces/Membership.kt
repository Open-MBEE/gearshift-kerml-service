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

package org.openmbee.gearshift.generated.interfaces

/**
 * A relationship that makes an element a member of a namespace
 */
interface Membership : Relationship {

    /**
     * The elementId of the memberElement
     */
    val memberElementId: String?

    /**
     * The name of the memberElement in the membershipOwningNamespace
     */
    var memberName: String?

    /**
     * The short name of the memberElement in the membershipOwningNamespace
     */
    var memberShortName: String?

    /**
     * The visibility of the member
     */
    var visibility: String

    var memberElement: Element

    val membershipNamespace: Set<Namespace>

    var membershipOwningNamespace: Namespace

    /**
     * Whether this Membership is distinguishable from a given other Membership.
By default, this is true if this Membership has no memberShortName or memberName;
or each of the memberShortName and memberName are different than both of those
of the other Membership; or neither of the metaclasses of the memberElement of this
Membership and the memberElement of the other Membership conform to the other.
     */
    fun isDistinguishableFrom(other: Membership): Boolean
}

