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
 * An import that brings all visible members of a namespace into another namespace
 */
interface NamespaceImport : Import {

    var importedNamespace: Namespace

    /**
     * Returns at least the visibleMemberships of the importedNamespace. If isRecursive = true,
then Memberships are also recursively imported from any ownedMembers of the
importedNamespace that are themselves Namespaces.
     */
    override fun importedMemberships(excluded: List<Namespace>): List<Membership>
}

