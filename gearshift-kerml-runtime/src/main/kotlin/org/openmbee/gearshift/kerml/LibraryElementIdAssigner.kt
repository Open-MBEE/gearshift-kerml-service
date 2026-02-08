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
package org.openmbee.gearshift.kerml

import org.openmbee.gearshift.generated.interfaces.Element
import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.generated.interfaces.OwningMembership
import org.openmbee.gearshift.generated.interfaces.Relationship
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.util.UUID

/**
 * Assigns spec-compliant UUID v5 element IDs to library elements after parsing.
 *
 * Per KerML spec, standard library element IDs use a two-level UUID v5 scheme:
 * 1. Top-level library packages: UUIDv5(URL_NAMESPACE_UUID, "https://www.omg.org/spec/KerML/<packageName>")
 * 2. All elements inside: UUIDv5(topLevelPackageUUID, element.path())
 *
 * The path() operation returns:
 * - For named elements: their qualifiedName
 * - For Relationships without an owningRelationship: owningRelatedElement.path() + "/" + position
 * - For OwningMemberships with a named ownedMemberElement: ownedMemberElement.qualifiedName + "/owningMembership"
 * - For other elements: owningRelationship.path() + "/" + position in ownedRelatedElement
 */
object LibraryElementIdAssigner {

    /**
     * RFC 4122 URL namespace UUID (6ba7b811-...) used for top-level package IDs.
     * Note: This is the URL namespace, not the DNS namespace (6ba7b810-...).
     */
    val NAMESPACE_URL_UUID: UUID = UUID.fromString("6ba7b811-9dad-11d1-80b4-00c04fd430c8")

    /**
     * URL prefix for KerML library packages.
     */
    const val KERML_URL_PREFIX = "https://www.omg.org/spec/KerML/"

    /**
     * Assign spec-compliant UUID v5 IDs to all elements in a parsed root namespace.
     *
     * Finds top-level packages, computes their package UUID, then walks the containment
     * tree assigning IDs to all contained elements.
     *
     * @param rootNamespace The root namespace produced by parsing a library file
     */
    fun assignIds(rootNamespace: Namespace) {
        // The root namespace itself is an implicit container; it doesn't get a spec ID.
        // Process each top-level package (direct ownedMember of the root namespace).
        for (membership in rootNamespace.ownedMembership) {
            val memberElement = try {
                if (membership is OwningMembership) {
                    membership.ownedMemberElement
                } else {
                    membership.memberElement
                }
            } catch (_: Exception) { continue }

            if (memberElement is Namespace && memberElement.declaredName != null) {
                val packageName = memberElement.declaredName!!
                val packageUuid = generateUuidV5(NAMESPACE_URL_UUID, "$KERML_URL_PREFIX$packageName")

                // Assign the top-level package's elementId
                memberElement.elementId = packageUuid.toString()

                // Assign ID to the owning membership of this package
                val membershipPath = "$packageName/owningMembership"
                membership.elementId = generateUuidV5(packageUuid, membershipPath).toString()

                // Recursively assign IDs to all contained elements
                assignContainedElementIds(memberElement, packageUuid)
            }
        }
    }

    /**
     * Generate a UUID v5 (SHA-1 based) per RFC 4122.
     *
     * @param namespace The namespace UUID
     * @param name The name string to hash
     * @return A deterministic UUID v5
     */
    fun generateUuidV5(namespace: UUID, name: String): UUID {
        val md = MessageDigest.getInstance("SHA-1")
        md.update(uuidToBytes(namespace))
        md.update(name.toByteArray(Charsets.UTF_8))
        val hash = md.digest()

        // Set version to 5 (SHA-1 based)
        hash[6] = ((hash[6].toInt() and 0x0F) or 0x50).toByte()
        // Set variant to RFC 4122
        hash[8] = ((hash[8].toInt() and 0x3F) or 0x80).toByte()

        val buffer = ByteBuffer.wrap(hash)
        val mostSig = buffer.getLong()
        val leastSig = buffer.getLong()
        return UUID(mostSig, leastSig)
    }

    /**
     * Compute the path() for an element using the spec algorithm, implemented in Kotlin.
     *
     * This mirrors the OCL-defined path() operation but avoids depending on the OCL engine,
     * which may not be fully functional during post-parse ID assignment.
     *
     * Element::path():
     *   if qualifiedName <> null then qualifiedName
     *   else if owningRelationship <> null then
     *     owningRelationship.path() + '/' + indexOf(self in owningRelationship.ownedRelatedElement)
     *   else ''
     *
     * Relationship::path():
     *   if owningRelationship = null and owningRelatedElement <> null then
     *     owningRelatedElement.path() + '/' + indexOf(self in owningRelatedElement.ownedRelationship)
     *   else Element::path()
     *
     * OwningMembership::path():
     *   if ownedMemberElement.qualifiedName <> null then
     *     ownedMemberElement.qualifiedName + '/owningMembership'
     *   else Relationship::path()
     */
    fun computePath(element: Element): String {
        return when (element) {
            is OwningMembership -> computeOwningMembershipPath(element)
            is Relationship -> computeRelationshipPath(element)
            else -> computeElementPath(element)
        }
    }

    private fun computeOwningMembershipPath(om: OwningMembership): String {
        val memberElement = try { om.ownedMemberElement } catch (_: Exception) { null }
        if (memberElement != null) {
            val memberQN = computeQualifiedName(memberElement)
            if (memberQN != null) {
                return "$memberQN/owningMembership"
            }
        }
        return computeRelationshipPath(om)
    }

    private fun computeRelationshipPath(rel: Relationship): String {
        val owningRel = try { rel.owningRelationship } catch (_: Exception) { null }
        val owningRelElem = try { rel.owningRelatedElement } catch (_: Exception) { null }
        if (owningRel == null && owningRelElem != null) {
            val ownerPath = computePath(owningRelElem)
            val rels = try { owningRelElem.ownedRelationship } catch (_: Exception) { emptyList() }
            val index = rels.indexOf(rel) + 1 // 1-based
            return "$ownerPath/$index"
        }
        return computeElementPath(rel)
    }

    private fun computeElementPath(element: Element): String {
        val qn = computeQualifiedName(element)
        if (qn != null) return qn

        val owningRel = try { element.owningRelationship } catch (_: Exception) { null }
        if (owningRel != null) {
            val relPath = computePath(owningRel)
            val children = try { owningRel.ownedRelatedElement } catch (_: Exception) { emptyList() }
            val index = children.indexOf(element) + 1 // 1-based
            return "$relPath/$index"
        }
        return ""
    }

    /**
     * Compute the qualified name for an element without using the OCL engine.
     *
     * qualifiedName = owningNamespace.qualifiedName + '::' + escapedName
     * For top-level elements (owner of owningNamespace is null): just escapedName
     */
    private fun computeQualifiedName(element: Element): String? {
        val eName = element.declaredName ?: element.declaredShortName ?: return null
        val ns = element.owningNamespace ?: return null

        // If the owning namespace has no owner, this is a top-level element
        return if (ns.owner == null) {
            eName
        } else {
            val nsQN = computeQualifiedName(ns) ?: return null
            "$nsQN::$eName"
        }
    }

    /**
     * Recursively assign element IDs to all elements contained within a top-level package.
     * Uses safe property access since some relationship property getters may throw
     * when the underlying association end is not set (null cast to non-null type).
     */
    private fun assignContainedElementIds(element: Element, packageUuid: UUID) {
        val relationships = try { element.ownedRelationship } catch (_: Exception) { return }

        for (relationship in relationships) {
            // Assign ID to the relationship itself
            val relPath = computePath(relationship)
            relationship.elementId = generateUuidV5(packageUuid, relPath).toString()

            // Assign IDs to owned related elements (the children)
            val children = try { relationship.ownedRelatedElement } catch (_: Exception) { emptyList() }
            for (child in children) {
                val childPath = computePath(child)
                child.elementId = generateUuidV5(packageUuid, childPath).toString()

                // Recurse into the child
                assignContainedElementIds(child, packageUuid)
            }
        }
    }

    private fun uuidToBytes(uuid: UUID): ByteArray {
        val buffer = ByteBuffer.allocate(16)
        buffer.putLong(uuid.mostSignificantBits)
        buffer.putLong(uuid.leastSignificantBits)
        return buffer.array()
    }
}
