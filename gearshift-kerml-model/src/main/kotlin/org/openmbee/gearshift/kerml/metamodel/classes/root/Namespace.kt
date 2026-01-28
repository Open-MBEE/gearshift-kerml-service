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
package org.openmbee.gearshift.kerml.metamodel.classes.root

import org.openmbee.gearshift.framework.meta.BodyLanguage
import org.openmbee.gearshift.framework.meta.ConstraintType
import org.openmbee.gearshift.framework.meta.MetaClass
import org.openmbee.gearshift.framework.meta.MetaConstraint
import org.openmbee.gearshift.framework.meta.MetaOperation
import org.openmbee.gearshift.framework.meta.MetaParameter

/**
 * KerML Namespace metaclass.
 * Specializes: Element
 * An element that can contain other elements as members.
 *
 * Association ends (defined in NamespacesAssociations.kt):
 * - importedMembership : Membership [0..*] {derived}
 * - member : Element [0..*] {derived}
 * - membership : Membership [0..*] {derived, union}
 * - ownedImport : Import [0..*] {derived}
 * - ownedMember : Element [0..*] {derived}
 * - ownedMembership : Membership [0..*] {derived}
 */
fun createNamespaceMetaClass() = MetaClass(
    name = "Namespace",
    isAbstract = false,
    superclasses = listOf("Element"),
    attributes = emptyList(),
    constraints = listOf(
        MetaConstraint(
            name = "deriveNamespaceImportedMembership",
            type = ConstraintType.DERIVATION,
            expression = "importedMemberships(Set{})",
            description = "The importedMemberships of a Namespace are derived using the importedMemberships() operation"
        ),
        MetaConstraint(
            name = "deriveNamespaceMembers",
            type = ConstraintType.DERIVATION,
            expression = "membership.memberElement",
            description = "The members of a Namespace are the memberElements of all its memberships"
        ),
        MetaConstraint(
            name = "deriveNamespaceOwnedImport",
            type = ConstraintType.DERIVATION,
            expression = "ownedRelationship->selectByKind(Import)",
            description = "The ownedImports of a Namespace are all its ownedRelationships that are Imports"
        ),
        MetaConstraint(
            name = "deriveNamespaceOwnedMember",
            type = ConstraintType.DERIVATION,
            expression = "ownedMembership->selectByKind(OwningMembership).ownedMemberElement",
            description = "The ownedMembers of a Namespace are the ownedMemberElements of all its ownedMemberships that are OwningMemberships"
        ),
        MetaConstraint(
            name = "deriveNamespaceOwnedMembership",
            type = ConstraintType.DERIVATION,
            expression = "ownedRelationship->selectByKind(Membership)",
            description = "The ownedMemberships of a Namespace are all its ownedRelationships that are Memberships"
        ),
        MetaConstraint(
            name = "validateNamespaceDistinguishability",
            type = ConstraintType.VERIFICATION,
            expression = "membership->forAll(m1 | membership->forAll(m2 | m1 <> m2 implies m1.isDistinguishableFrom(m2)))",
            description = "All memberships of a Namespace must be distinguishable from each other"
        )
    ),
    operations = listOf(
        MetaOperation(
            name = "importedMemberships",
            returnType = "Membership",
            returnUpperBound = -1,
            parameters = listOf(
                MetaParameter(name = "excluded", type = "Namespace", lowerBound = 0, upperBound = -1)
            ),
            body = "ownedImport.importedMemberships(excluded->including(self))",
            bodyLanguage = BodyLanguage.OCL,
            description = """
                Derive the importedMemberships of this Namespace as the importedMembership of all
                ownedImports, excluding those Imports whose importOwningNamespace is in the excluded
                set, and excluding Memberships that have distinguishability collisions with each other
                or with any ownedMembership.
            """.trimIndent()
        ),
        MetaOperation(
            name = "membershipsOfVisibility",
            returnType = "Membership",
            returnUpperBound = -1,
            parameters = listOf(
                MetaParameter(name = "visibility", type = "VisibilityKind", lowerBound = 0),
                MetaParameter(name = "excluded", type = "Namespace", lowerBound = 0, upperBound = -1)
            ),
            body = """
                ownedMembership->
                    select(mem | visibility = null or mem.visibility = visibility)->
                    union(ownedImport->
                        select(imp | visibility = null or imp.visibility = visibility).
                        importedMemberships(excluded->including(self)))
            """.trimIndent(),
            bodyLanguage = BodyLanguage.OCL,
            description = """
                If visibility is not null, return the Memberships of this Namespace with the given
                visibility, including ownedMemberships with the given visibility and Memberships
                imported with the given visibility. If visibility is null, return all ownedMemberships
                and importedMemberships regardless of visibility. When computing importedMemberships,
                ignore this Namespace and any Namespaces in the given excluded set.
            """.trimIndent()
        ),
        MetaOperation(
            name = "namesOf",
            returnType = "String",
            returnUpperBound = -1,
            parameters = listOf(
                MetaParameter(name = "element", type = "Element")
            ),
            body = """
                let elementMemberships : Sequence(Membership) =
                    memberships->select(memberElement = element) in
                elementMemberships.memberShortName->
                    union(elementMemberships.memberName)->
                    asSet()
            """.trimIndent(),
            bodyLanguage = BodyLanguage.OCL,
            isQuery = true,
            description = "Return the names of the given element as it is known in this Namespace."
        ),
        MetaOperation(
            name = "qualificationOf",
            returnType = "String",
            parameters = listOf(
                MetaParameter(name = "qualifiedName", type = "String")
            ),
            body = MetaOperation.kotlinBody("""
                val qn = args["qualifiedName"] as? String ?: return@kotlinBody null
                // Parse the qualified name segments (handling both :: and single names)
                val segments = qn.split("::")
                if (segments.size <= 1) {
                    null  // Single segment has no qualification
                } else {
                    // Return all segments except the last, joined by ::
                    segments.dropLast(1).joinToString("::")
                }
            """),
            bodyLanguage = BodyLanguage.KOTLIN_DSL,
            isQuery = true,
            description = """
                Return a string with valid KerML syntax representing the qualification part of a
                given qualifiedName, that is, a qualified name with all the segment names of the
                given name except the last. If the given qualifiedName has only one segment, then
                return null.
            """.trimIndent()
        ),
        MetaOperation(
            name = "resolve",
            returnType = "Membership",
            parameters = listOf(
                MetaParameter(name = "qualifiedName", type = "String")
            ),
            body = """
                let qualification : String = qualificationOf(qualifiedName) in
                let name : String = unqualifiedNameOf(qualifiedName) in
                if qualification = null then resolveLocal(name)
                else if qualification = '' then self.resolveGlobal(name)
                else
                    let namespaceMembership : Membership = resolve(qualification) in
                    if namespaceMembership = null or
                       not namespaceMembership.memberElement.oclIsKindOf(Namespace)
                    then null
                    else
                        namespaceMembership.memberElement.oclAsType(Namespace).
                            resolveVisible(name)
                    endif
                endif endif
            """.trimIndent(),
            bodyLanguage = BodyLanguage.OCL,
            isQuery = true,
            description = """
                Resolve the given qualified name to the named Membership (if any), starting with
                this Namespace as the local scope. The qualified name string must conform to the
                concrete syntax of the KerML textual notation. According to the KerML name
                resolution rules every qualified name will resolve to either a single Membership,
                or to none.
            """.trimIndent()
        ),
        MetaOperation(
            name = "resolveGlobal",
            returnType = "Membership",
            parameters = listOf(
                MetaParameter(name = "qualifiedName", type = "String")
            ),
            body = MetaOperation.kotlinBody("""
                val qn = args["qualifiedName"] as? String ?: return@kotlinBody null
                // Delegate to engine's resolveGlobal which finds the root namespace
                // and resolves the qualified name from there
                engine.resolveGlobal(qn)
            """),
            bodyLanguage = BodyLanguage.KOTLIN_DSL,
            isQuery = true,
            description = """
                Resolve the given qualified name to the named Membership (if any) in the effective
                global Namespace that is the outermost naming scope. The qualified name string must
                conform to the concrete syntax of the KerML textual notation.
            """.trimIndent()
        ),
        MetaOperation(
            name = "resolveLocal",
            returnType = "Membership",
            parameters = listOf(
                MetaParameter(name = "name", type = "String")
            ),
            body = """
                if owningNamespace = null then self.resolveGlobal(name)
                else
                    let memberships : Membership = membership->
                        select(memberShortName = name or memberName = name) in
                    if memberships->notEmpty() then memberships->first()
                    else owningNamespace.resolveLocal(name)
                    endif
                endif
            """.trimIndent(),
            bodyLanguage = BodyLanguage.OCL,
            isQuery = true,
            description = """
                Resolve a simple name starting with this Namespace as the local scope, and continuing
                with containing outer scopes as necessary. However, if this Namespace is a root
                Namespace, then the resolution is done directly in global scope.
            """.trimIndent()
        ),
        MetaOperation(
            name = "resolveVisible",
            returnType = "Membership",
            parameters = listOf(
                MetaParameter(name = "name", type = "String")
            ),
            body = """
                let memberships : Sequence(Membership) =
                    visibleMemberships(Set{}, false, false)->
                    select(memberShortName = name or memberName = name) in
                if memberships->isEmpty() then null
                else memberships->first()
                endif
            """.trimIndent(),
            bodyLanguage = BodyLanguage.OCL,
            isQuery = true,
            description = "Resolve a simple name from the visibleMemberships of this Namespace."
        ),
        MetaOperation(
            name = "unqualifiedNameOf",
            returnType = "String",
            returnLowerBound = 1,
            returnUpperBound = 1,
            parameters = listOf(
                MetaParameter(name = "qualifiedName", type = "String")
            ),
            body = MetaOperation.kotlinBody("""
                val qn = args["qualifiedName"] as? String ?: return@kotlinBody ""
                // Get the last segment of the qualified name
                val segments = qn.split("::")
                val lastSegment = segments.last()

                // If it's an unrestricted name (surrounded by single quotes), unescape it
                if (lastSegment.startsWith("'") && lastSegment.endsWith("'") && lastSegment.length >= 2) {
                    // Remove surrounding quotes and unescape
                    lastSegment.substring(1, lastSegment.length - 1)
                        .replace("\\'", "'")
                        .replace("\\\\", "\\")
                } else {
                    lastSegment
                }
            """),
            bodyLanguage = BodyLanguage.KOTLIN_DSL,
            isQuery = true,
            description = """
                Return the simple name that is the last segment name of the given qualifiedName.
                If this segment name has the form of a KerML unrestricted name, then "unescape" it
                by removing the surrounding single quotes and replacing all escape sequences with
                the specified character.
            """.trimIndent()
        ),
        MetaOperation(
            name = "visibilityOf",
            returnType = "VisibilityKind",
            returnLowerBound = 1,
            returnUpperBound = 1,
            parameters = listOf(
                MetaParameter(name = "mem", type = "Membership")
            ),
            body = """
                if importedMembership->includes(mem) then
                    ownedImport->
                        select(importedMemberships(Set{})->includes(mem)).
                        first().visibility
                else if membership->includes(mem) then
                    mem.visibility
                else
                    VisibilityKind::private
                endif endif
            """.trimIndent(),
            bodyLanguage = BodyLanguage.OCL,
            isQuery = true,
            description = """
                Returns the visibility of mem relative to this Namespace. If mem is an
                importedMembership, this is the visibility of its Import. Otherwise it is
                the visibility of the Membership itself.
            """.trimIndent()
        ),
        MetaOperation(
            name = "visibleMemberships",
            returnType = "Membership",
            returnUpperBound = -1,
            parameters = listOf(
                MetaParameter(name = "excluded", type = "Namespace", lowerBound = 0, upperBound = -1),
                MetaParameter(name = "isRecursive", type = "Boolean"),
                MetaParameter(name = "includeAll", type = "Boolean")
            ),
            body = """
                let visibleMemberships : OrderedSet(Membership) =
                    if includeAll then membershipsOfVisibility(null, excluded)
                    else membershipsOfVisibility(VisibilityKind::public, excluded)
                    endif in
                if not isRecursive then visibleMemberships
                else visibleMemberships->union(ownedMember->
                    selectAsKind(Namespace).
                    select(includeAll or owningMembership.visibility = VisibilityKind::public)->
                    visibleMemberships(excluded->including(self), true, includeAll))
                endif
            """.trimIndent(),
            bodyLanguage = BodyLanguage.OCL,
            isQuery = true,
            description = """
                If includeAll = true, then return all the Memberships of this Namespace. Otherwise,
                return only the publicly visible Memberships of this Namespace, including
                ownedMemberships that have a visibility of public and Memberships imported with a
                visibility of public. If isRecursive = true, also recursively include all
                visibleMemberships of any public ownedNamespaces, or, if includeAll = true, all
                Memberships of all ownedNamespaces. When computing importedMemberships, ignore this
                Namespace and any Namespaces in the given excluded set.
            """.trimIndent()
        )
    ),
    description = "An element that can contain other elements as members"
)
