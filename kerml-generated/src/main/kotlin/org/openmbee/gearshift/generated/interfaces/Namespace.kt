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
 * An element that can contain other elements as members
 */
interface Namespace : Element {

    val importedMembership: List<Membership>

    val member: List<Element>

    val membership: List<Membership>

    var ownedImport: List<Import>

    val ownedMember: List<Element>

    var ownedMembership: List<Membership>

    /**
     * Derive the importedMemberships of this Namespace as the importedMembership of all
ownedImports, excluding those Imports whose importOwningNamespace is in the excluded
set, and excluding Memberships that have distinguishability collisions with each other
or with any ownedMembership.
     */
    fun importedMemberships(excluded: List<Namespace>): List<Membership>

    /**
     * If visibility is not null, return the Memberships of this Namespace with the given
visibility, including ownedMemberships with the given visibility and Memberships
imported with the given visibility. If visibility is null, return all ownedMemberships
and importedMemberships regardless of visibility. When computing importedMemberships,
ignore this Namespace and any Namespaces in the given excluded set.
     */
    fun membershipsOfVisibility(visibility: String?, excluded: List<Namespace>): List<Membership>

    /**
     * Return the names of the given element as it is known in this Namespace.
     */
    fun namesOf(element: Element): List<String>

    /**
     * Return a string with valid KerML syntax representing the qualification part of a
given qualifiedName, that is, a qualified name with all the segment names of the
given name except the last. If the given qualifiedName has only one segment, then
return null.
     */
    fun qualificationOf(qualifiedName: String): String?

    /**
     * Resolve the given qualified name to the named Membership (if any), starting with
this Namespace as the local scope. The qualified name string must conform to the
concrete syntax of the KerML textual notation. According to the KerML name
resolution rules every qualified name will resolve to either a single Membership,
or to none.
     */
    fun resolve(qualifiedName: String): Membership?

    /**
     * Resolve the given qualified name to the named Membership (if any) in the effective
global Namespace that is the outermost naming scope. The qualified name string must
conform to the concrete syntax of the KerML textual notation.
     */
    fun resolveGlobal(qualifiedName: String): Membership?

    /**
     * Resolve a simple name starting with this Namespace as the local scope, and continuing
with containing outer scopes as necessary. However, if this Namespace is a root
Namespace, then the resolution is done directly in global scope.
     */
    fun resolveLocal(name: String): Membership?

    /**
     * Resolve a simple name from the visibleMemberships of this Namespace.
     */
    fun resolveVisible(name: String): Membership?

    /**
     * Return the simple name that is the last segment name of the given qualifiedName.
If this segment name has the form of a KerML unrestricted name, then "unescape" it
by removing the surrounding single quotes and replacing all escape sequences with
the specified character.
     */
    fun unqualifiedNameOf(qualifiedName: String): String

    /**
     * Returns the visibility of mem relative to this Namespace. If mem is an
importedMembership, this is the visibility of its Import. Otherwise it is
the visibility of the Membership itself.
     */
    fun visibilityOf(mem: Membership): String

    /**
     * If includeAll = true, then return all the Memberships of this Namespace. Otherwise,
return only the publicly visible Memberships of this Namespace, including
ownedMemberships that have a visibility of public and Memberships imported with a
visibility of public. If isRecursive = true, also recursively include all
visibleMemberships of any public ownedNamespaces, or, if includeAll = true, all
Memberships of all ownedNamespaces. When computing importedMemberships, ignore this
Namespace and any Namespaces in the given excluded set.
     */
    fun visibleMemberships(excluded: List<Namespace>, isRecursive: Boolean, includeAll: Boolean): List<Membership>
}

