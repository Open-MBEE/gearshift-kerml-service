package org.openmbee.gearshift.engine

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.gearshift.repository.ModelRepository

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
    private val registry: MetamodelRegistry
) {
    // Track names currently being resolved to prevent infinite loops
    private val resolutionStack = ThreadLocal.withInitial { mutableSetOf<String>() }

    /**
     * Result of name resolution containing the Membership and its member Element.
     */
    data class ResolutionResult(
        val membershipId: String,
        val membership: MofObject,
        val memberElement: MofObject
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
    private fun fullResolution(name: String, namespace: MofObject): ResolutionResult? {
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
    private fun visibleResolution(name: String, namespace: MofObject): ResolutionResult? {
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
        owningType: MofObject
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
    private fun resolveInOwnedMemberships(name: String, namespace: MofObject): ResolutionResult? {
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
    private fun resolveInImportedMemberships(name: String, namespace: MofObject): ResolutionResult? {
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
    private fun resolveInInheritedMemberships(name: String, type: MofObject): ResolutionResult? {
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

    private fun isNamespace(obj: MofObject): Boolean {
        val metaClass = obj.metaClass
        return metaClass.name == "Namespace" ||
               registry.isSubclassOf(metaClass.name, "Namespace")
    }

    private fun isType(obj: MofObject): Boolean {
        val metaClass = obj.metaClass
        return metaClass.name == "Type" ||
               registry.isSubclassOf(metaClass.name, "Type")
    }

    private fun isRootNamespace(obj: MofObject): Boolean {
        val owner = obj.getProperty("owner")
        return owner == null
    }

    private fun findGlobalNamespace(): MofObject {
        // TODO: Implement proper global namespace lookup
        // For now, return first root namespace
        return repository.getAll().first { isRootNamespace(it) }
    }

    private fun getOwnedMemberships(namespace: MofObject): List<MofObject> {
        val membershipIds = namespace.getProperty("ownedMembership") as? List<*>
            ?: return emptyList()
        return membershipIds.mapNotNull { repository.get(it.toString()) }
    }

    private fun getImports(namespace: MofObject): List<MofObject> {
        val importIds = namespace.getProperty("ownedImport") as? List<*>
            ?: return emptyList()
        return importIds.mapNotNull { repository.get(it.toString()) }
    }

    private fun getOwnedSpecializations(type: MofObject): List<MofObject> {
        val specIds = type.getProperty("ownedSpecialization") as? List<*>
            ?: return emptyList()
        return specIds.mapNotNull { repository.get(it.toString()) }
    }

    private fun getMemberName(membership: MofObject): String? {
        return membership.getProperty("memberName") as? String
    }

    private fun getMemberElement(membership: MofObject): MofObject? {
        val elementId = membership.getProperty("memberElement") as? String
            ?: return null
        return repository.get(elementId)
    }

    private fun getGeneral(specialization: MofObject): MofObject? {
        val generalId = specialization.getProperty("general") as? String
            ?: return null
        return repository.get(generalId)
    }

    private fun getImportedNamespace(import: MofObject): MofObject? {
        val namespaceId = import.getProperty("importedNamespace") as? String
            ?: return null
        return repository.get(namespaceId)
    }

    private fun getInstanceId(obj: MofObject): String? {
        // Find instance ID by searching repository
        return repository.getAllIds().firstOrNull { repository.get(it) == obj }
    }
}
