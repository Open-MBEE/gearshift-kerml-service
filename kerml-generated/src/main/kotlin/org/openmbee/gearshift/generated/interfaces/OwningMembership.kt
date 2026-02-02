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
 * A membership where the namespace owns the member element
 */
interface OwningMembership : Membership {

    /**
     * The name of the ownedMemberElement
     */
    val ownedMemberName: String?

    /**
     * The shortName of the ownedMemberElement
     */
    val ownedMemberShortName: String?

    var ownedMemberElement: Element

    /**
     * If the ownedMemberElement of this OwningMembership has a non-null qualifiedName,
then return the string constructed by appending to that qualifiedName the string
"/owningMembership". Otherwise, return the path of the OwningMembership as
specified for a Relationship.
     */
    override fun path(): String
}

