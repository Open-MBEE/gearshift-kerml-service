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
package org.openmbee.gearshift.framework.runtime

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.gearshift.framework.storage.ModelRepository

private val logger = KotlinLogging.logger {}

/**
 * Name resolution engine implementing KerML 8.2.3.5 Name Resolution specification.
 *
 * Resolves qualified names to Membership relationships and their member Elements.
 * Handles:
 * - Single segment names
 * - Multi-segment qualified names (A::B::C)
 * - Global scope qualifier ($::A::B)
 * - Redefinition context resolution
 */
class NameResolver(
    private val repository: ModelRepository,
    private val registry: MetamodelRegistry,
    private var mdmEngine: MDMEngine? = null
) {
    /**
     * Set the MDMEngine for property resolution.
     * This is called after MDMEngine is constructed to enable proper property/link resolution.
     */
    fun setEngine(engine: MDMEngine) {
        this.mdmEngine = engine
    }
    // Track names currently being resolved to prevent infinite loops
    private val resolutionStack = ThreadLocal.withInitial { mutableSetOf<String>() }

    /**
     * Result of name resolution containing the Membership and its member Element.
     */
    data class ResolutionResult(
        val membershipId: String,
        val membership: MDMObject,
        val memberElement: MDMObject
    )

    /**
     * Parse a qualified name into segments.
     * Examples:
     * - "A" -> QualifiedName(hasGlobalScope=false, segments=["A"])
     * - "A::B::C" -> QualifiedName(hasGlobalScope=false, segments=["A", "B", "C"])
     * - "$::A::B" -> QualifiedName(hasGlobalScope=true, segments=["A", "B"])
     */
    data class QualifiedName(
        val hasGlobalScope: Boolean,
        val segments: List<String>
    ) {
        val qualificationPart: QualifiedName?
            get() = if (segments.size > 1) {
                QualifiedName(hasGlobalScope, segments.dropLast(1))
            } else null

        val lastSegment: String
            get() = segments.last()

        companion object {
            fun parse(name: String): QualifiedName {
                val parts = name.split("::")
                val hasGlobal = parts.firstOrNull() == "$"
                val segments = if (hasGlobal) parts.drop(1) else parts
                return QualifiedName(hasGlobal, segments.filter { it.isNotBlank() })
            }
        }

        override fun toString(): String {
            val prefix = if (hasGlobalScope) "$::" else ""
            return prefix + segments.joinToString("::")
        }
    }

    /**
     * Resolve a qualified name relative to a local Namespace.
     * Implements KerML 8.2.3.5.1 basic name resolution process.
     *
     * @param qualifiedName The name to resolve (can be string or parsed QualifiedName)
     * @param localNamespaceId The ID of the Namespace to resolve relative to
     * @param isRedefinitionContext If true, use special redefinition resolution rules
     * @return ResolutionResult or null if name cannot be resolved
     */
    fun resolve(
        qualifiedName: String,
        localNamespaceId: String,
        isRedefinitionContext: Boolean = false
    ): ResolutionResult? {
        return resolve(QualifiedName.parse(qualifiedName), localNamespaceId, isRedefinitionContext)
    }

    fun resolve(
        qualifiedName: QualifiedName,
        localNamespaceId: String,
        isRedefinitionContext: Boolean = false
    ): ResolutionResult? {
        // Prevent infinite loops during circular resolution
        val key = "${qualifiedName}@${localNamespaceId}"
        if (!resolutionStack.get().add(key)) {
            logger.warn { "Circular name resolution detected: $key" }
            return null
        }

        try {
            return doResolve(qualifiedName, localNamespaceId, isRedefinitionContext)
        } finally {
            resolutionStack.get().remove(key)
        }
    }

    private fun doResolve(
        qualifiedName: QualifiedName,
        localNamespaceId: String,
        isRedefinitionContext: Boolean
    ): ResolutionResult? {
        val localNamespace = repository.get(localNamespaceId) ?: return null

        // Handle special redefinition context
        if (isRedefinitionContext) {
            return resolveInRedefinitionContext(qualifiedName, localNamespace)
        }

        // Basic resolution process (KerML 8.2.3.5.1)
        return when {
            // Case 1: Single segment with global scope qualifier
            qualifiedName.hasGlobalScope && qualifiedName.segments.size == 1 -> {
                val globalNamespace = findGlobalNamespace()
                fullResolution(qualifiedName.lastSegment, globalNamespace)
            }

            // Case 2: Single segment, no global scope
            qualifiedName.segments.size == 1 -> {
                if (isRootNamespace(localNamespace)) {
                    val globalNamespace = findGlobalNamespace()
                    fullResolution(qualifiedName.lastSegment, globalNamespace)
                } else {
                    fullResolution(qualifiedName.lastSegment, localNamespace)
                }
            }

            // Case 3: Multi-segment qualified name
            else -> {
                val qualPart = qualifiedName.qualificationPart!!
                val qualPartResult = resolve(qualPart, localNamespaceId)

                if (qualPartResult == null) {
                    logger.debug { "Failed to resolve qualification part: $qualPart" }
                    return null
                }

                // Qualification part must resolve to a Namespace
                if (!isNamespace(qualPartResult.memberElement)) {
                    logger.debug { "Qualification part did not resolve to Namespace: $qualPart" }
                    return null
                }

                // Resolve last segment relative to the namespace from qualification
                visibleResolution(qualifiedName.lastSegment, qualPartResult.memberElement)
            }
        }
    }

    /**
     * Full resolution: resolve name considering all memberships including imported.
     * Implements KerML 8.2.3.5.4.
     */
    private fun fullResolution(name: String, namespace: MDMObject): ResolutionResult? {
        // First try visible resolution
        visibleResolution(name, namespace)?.let { return it }

        // If visible resolution fails, try inherited memberships (for Types)
        if (isType(namespace)) {
            resolveInInheritedMemberships(name, namespace)?.let { return it }
        }

        return null
    }

    /**
     * Visible resolution: resolve name in visible memberships.
     * Implements KerML 8.2.3.5.3.
     *
     * Visible memberships = owned memberships + imported memberships
     */
    private fun visibleResolution(name: String, namespace: MDMObject): ResolutionResult? {
        // Search in owned memberships
        resolveInOwnedMemberships(name, namespace)?.let { return it }

        // Search in imported memberships
        resolveInImportedMemberships(name, namespace)?.let { return it }

        return null
    }

    /**
     * Resolve in redefinition context.
     * Per KerML 8.2.3.5.1: Try basic resolution with each general Type as local namespace.
     */
    private fun resolveInRedefinitionContext(
        qualifiedName: QualifiedName,
        owningType: MDMObject
    ): ResolutionResult? {
        val specializations = getOwnedSpecializations(owningType)

        for (spec in specializations) {
            val generalType = getGeneral(spec) ?: continue
            resolve(qualifiedName, getInstanceId(generalType) ?: continue)?.let {
                return it
            }
        }

        return null
    }

    /**
     * Resolve in owned memberships of a namespace.
     */
    private fun resolveInOwnedMemberships(name: String, namespace: MDMObject): ResolutionResult? {
        val memberships = getOwnedMemberships(namespace)

        for (membership in memberships) {
            val memberName = getMemberName(membership)
            if (memberName == name) {
                val memberElement = getMemberElement(membership)
                val membershipId = getInstanceId(membership)
                if (memberElement != null && membershipId != null) {
                    return ResolutionResult(membershipId, membership, memberElement)
                }
            }
        }

        return null
    }

    /**
     * Resolve in imported memberships.
     */
    private fun resolveInImportedMemberships(name: String, namespace: MDMObject): ResolutionResult? {
        val imports = getImports(namespace)

        for (import in imports) {
            val importedNamespace = getImportedNamespace(import) ?: continue
            visibleResolution(name, importedNamespace)?.let { return it }
        }

        return null
    }

    /**
     * Resolve in inherited memberships (for Types).
     */
    private fun resolveInInheritedMemberships(name: String, type: MDMObject): ResolutionResult? {
        val specializations = getOwnedSpecializations(type)
        val visited = mutableSetOf<String>()

        for (spec in specializations) {
            val general = getGeneral(spec) ?: continue
            val generalId = getInstanceId(general) ?: continue

            // Avoid circularity
            if (!visited.add(generalId)) continue

            // Search in general type
            fullResolution(name, general)?.let { return it }
        }

        return null
    }

    // ===== Helper methods to interact with model instances =====

    private fun isNamespace(obj: MDMObject): Boolean {
        val metaClass = obj.metaClass
        return metaClass.name == "Namespace" ||
                registry.isSubclassOf(metaClass.name, "Namespace")
    }

    private fun isType(obj: MDMObject): Boolean {
        val metaClass = obj.metaClass
        return metaClass.name == "Type" ||
                registry.isSubclassOf(metaClass.name, "Type")
    }

    private fun isRootNamespace(obj: MDMObject): Boolean {
        val owner = obj.getProperty("owner")
        return owner == null
    }

    private fun findGlobalNamespace(): MDMObject {
        // TODO: Implement proper global namespace lookup
        // For now, return first root namespace
        return repository.getAll().first { isRootNamespace(it) }
    }

    private fun getOwnedMemberships(namespace: MDMObject): List<MDMObject> {
        // Use MDMEngine.getProperty which handles association ends via links
        val engine = mdmEngine
        val rawValue = if (engine != null) {
            engine.getProperty(namespace, "ownedMembership")
        } else {
            namespace.getProperty("ownedMembership")
        }

        return when (rawValue) {
            is List<*> -> rawValue.mapNotNull { item ->
                when (item) {
                    is MDMObject -> item
                    is String -> repository.get(item)
                    else -> null
                }
            }
            else -> emptyList()
        }
    }

    private fun getImports(namespace: MDMObject): List<MDMObject> {
        val engine = mdmEngine
        val rawValue = if (engine != null) {
            engine.getProperty(namespace, "ownedImport")
        } else {
            namespace.getProperty("ownedImport")
        }

        return when (rawValue) {
            is List<*> -> rawValue.mapNotNull { item ->
                when (item) {
                    is MDMObject -> item
                    is String -> repository.get(item)
                    else -> null
                }
            }
            else -> emptyList()
        }
    }

    private fun getOwnedSpecializations(type: MDMObject): List<MDMObject> {
        val engine = mdmEngine
        val rawValue = if (engine != null) {
            engine.getProperty(type, "ownedSpecialization")
        } else {
            type.getProperty("ownedSpecialization")
        }

        return when (rawValue) {
            is List<*> -> rawValue.mapNotNull { item ->
                when (item) {
                    is MDMObject -> item
                    is String -> repository.get(item)
                    else -> null
                }
            }
            else -> emptyList()
        }
    }

    private fun getMemberName(membership: MDMObject): String? {
        val engine = mdmEngine
        val rawValue = if (engine != null) {
            engine.getProperty(membership, "memberName")
        } else {
            membership.getProperty("memberName")
        }
        return rawValue as? String
    }

    private fun getMemberElement(membership: MDMObject): MDMObject? {
        val engine = mdmEngine
        val rawValue = if (engine != null) {
            engine.getProperty(membership, "memberElement")
        } else {
            membership.getProperty("memberElement")
        }

        return when (rawValue) {
            is MDMObject -> rawValue
            is List<*> -> (rawValue.firstOrNull() as? MDMObject)
            is String -> repository.get(rawValue)
            else -> null
        }
    }

    private fun getGeneral(specialization: MDMObject): MDMObject? {
        val engine = mdmEngine
        val rawValue = if (engine != null) {
            engine.getProperty(specialization, "general")
        } else {
            specialization.getProperty("general")
        }

        return when (rawValue) {
            is MDMObject -> rawValue
            is List<*> -> (rawValue.firstOrNull() as? MDMObject)
            is String -> repository.get(rawValue)
            else -> null
        }
    }

    private fun getImportedNamespace(import: MDMObject): MDMObject? {
        val engine = mdmEngine
        val rawValue = if (engine != null) {
            engine.getProperty(import, "importedNamespace")
        } else {
            import.getProperty("importedNamespace")
        }

        return when (rawValue) {
            is MDMObject -> rawValue
            is List<*> -> (rawValue.firstOrNull() as? MDMObject)
            is String -> repository.get(rawValue)
            else -> null
        }
    }

    private fun getInstanceId(obj: MDMObject): String? {
        // Find instance ID by searching repository
        return repository.getAllIds().firstOrNull { repository.get(it) == obj }
    }
}
