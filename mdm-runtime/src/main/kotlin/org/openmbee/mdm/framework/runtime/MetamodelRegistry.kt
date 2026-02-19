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
package org.openmbee.mdm.framework.runtime

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.mdm.framework.meta.MetaAssociation
import org.openmbee.mdm.framework.meta.MetaAssociationEnd
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.OwnershipBinding
import org.openmbee.mdm.framework.model.createMDMBaseClass
import java.util.concurrent.ConcurrentHashMap

/**
 * Describes the role an association plays in the ownership chain.
 */
enum class OwnershipLinkRole {
    /** Link from owner to intermediate (e.g., Namespace → OwningMembership) */
    OWNER_TO_INTERMEDIATE,
    /** Link from intermediate to child (e.g., OwningMembership → Element) */
    INTERMEDIATE_TO_CHILD
}

/**
 * Pre-computed info about an association's role in ownership.
 */
data class OwnershipAssociationInfo(
    val intermediateClassName: String,
    val binding: OwnershipBinding,
    val role: OwnershipLinkRole
)

private val logger = KotlinLogging.logger {}

/**
 * Registry for managing metamodel definitions (MetaClasses and MetaAssociations).
 * Provides lookup, validation, and inheritance resolution capabilities.
 *
 * Ownership semantics are now declared on individual MetaClass definitions
 * via ownershipBinding, and resolved by OwnershipResolver.
 */
class MetamodelRegistry {
    private val classes = ConcurrentHashMap<String, MetaClass>()
    private val associations = ConcurrentHashMap<String, MetaAssociation>()
    private val subclassIndex = ConcurrentHashMap<String, MutableSet<String>>()

    // Pre-computed indexes — populated by buildIndexes(), safe because metamodel is immutable after startup
    private var superclassCache: Map<String, Set<String>> = emptyMap()
    private var isSubclassCache: Map<String, Map<String, Boolean>> = emptyMap()
    private var redefinesIndex: Map<String, List<Pair<MetaAssociation, MetaAssociationEnd>>> = emptyMap()
    private var subsetsIndex: Map<String, List<Pair<MetaAssociation, MetaAssociationEnd>>> = emptyMap()
    private var classAssocEndIndex: Map<String, List<Triple<MetaAssociation, MetaAssociationEnd, Boolean>>> = emptyMap()
    private var ownershipAssocIndex: Map<String, OwnershipAssociationInfo> = emptyMap()

    companion object {
        /** The default base class name that all classes inherit from if no superclass is specified */
        const val DEFAULT_BASE_CLASS = "MDMBaseClass"
    }

    /**
     * Register a MetaClass in the registry.
     *
     * If the class has no explicit superclasses and is not MDMBaseClass itself,
     * it will automatically inherit from MDMBaseClass to get default validation constraints.
     */
    fun registerClass(metaClass: MetaClass) {
        val effectiveClass = if (metaClass.superclasses.isEmpty() && metaClass.name != DEFAULT_BASE_CLASS) {
            // Ensure base class is registered before we reference it
            ensureBaseClassRegistered()
            // Add MDMBaseClass as default superclass
            metaClass.copy(superclasses = listOf(DEFAULT_BASE_CLASS))
        } else {
            metaClass
        }

        classes[effectiveClass.name] = effectiveClass

        // Build subclass index
        effectiveClass.superclasses.forEach { superclass ->
            subclassIndex.getOrPut(superclass) { ConcurrentHashMap.newKeySet() }
                .add(effectiveClass.name)
        }

        logger.debug { "Registered MetaClass: ${effectiveClass.name}" }
    }

    /**
     * Ensure the MDMBaseClass is registered.
     * This should be called before registering other classes to ensure they can inherit from it.
     * Calling this multiple times is safe - it only registers once.
     */
    fun ensureBaseClassRegistered() {
        if (!classes.containsKey(DEFAULT_BASE_CLASS)) {
            val baseClass = createMDMBaseClass()
            classes[baseClass.name] = baseClass
            logger.debug { "Registered default base class: ${baseClass.name}" }
        }
    }

    /**
     * Register a MetaAssociation in the registry.
     */
    fun registerAssociation(association: MetaAssociation) {
        associations[association.name] = association
        logger.debug { "Registered MetaAssociation: ${association.name}" }
    }

    /**
     * Pre-compute all lookup indexes from the current metamodel state.
     * Call once after all classes and associations are registered.
     * Dramatically speeds up isSubclassOf, getAllSuperclasses, findRedefiningEnds,
     * findSubsettingEnds, and findAssociationEnd lookups.
     */
    fun buildIndexes() {
        logger.info { "Building metamodel indexes..." }

        // 1. Superclass cache — transitive closure for every class
        val superCache = mutableMapOf<String, Set<String>>()
        for (className in classes.keys) {
            val result = mutableSetOf<String>()
            collectSuperclasses(className, result)
            superCache[className] = result
        }
        superclassCache = superCache

        // 2. isSubclass cache — for every (sub, super) pair
        val subCache = mutableMapOf<String, MutableMap<String, Boolean>>()
        for (className in classes.keys) {
            val supers = superCache[className] ?: emptySet()
            val innerMap = mutableMapOf<String, Boolean>()
            for (otherClass in classes.keys) {
                innerMap[otherClass] = (className == otherClass) || supers.contains(otherClass)
            }
            subCache[className] = innerMap
        }
        isSubclassCache = subCache

        // 3. Redefines index — propertyName → list of (association, redefining end)
        val redefIdx = mutableMapOf<String, MutableList<Pair<MetaAssociation, MetaAssociationEnd>>>()
        for (association in associations.values) {
            for (propName in association.sourceEnd.redefines) {
                redefIdx.getOrPut(propName) { mutableListOf() }.add(association to association.sourceEnd)
            }
            for (propName in association.targetEnd.redefines) {
                redefIdx.getOrPut(propName) { mutableListOf() }.add(association to association.targetEnd)
            }
        }
        redefinesIndex = redefIdx

        // 4. Subsets index — propertyName → list of (association, subsetting end)
        val subsetIdx = mutableMapOf<String, MutableList<Pair<MetaAssociation, MetaAssociationEnd>>>()
        for (association in associations.values) {
            for (propName in association.sourceEnd.subsets) {
                subsetIdx.getOrPut(propName) { mutableListOf() }.add(association to association.sourceEnd)
            }
            for (propName in association.targetEnd.subsets) {
                subsetIdx.getOrPut(propName) { mutableListOf() }.add(association to association.targetEnd)
            }
        }
        subsetsIndex = subsetIdx

        // 5. Per-class association end index — className → list of (association, end, isTargetEnd)
        val classAssocIdx = mutableMapOf<String, MutableList<Triple<MetaAssociation, MetaAssociationEnd, Boolean>>>()
        for (association in associations.values) {
            val sourceType = association.sourceEnd.type
            val targetType = association.targetEnd.type

            // For every class that is sourceType or a subclass of it, the targetEnd is applicable
            for (className in classes.keys) {
                val isSubOfSource = subCache[className]?.get(sourceType) ?: false
                if (isSubOfSource) {
                    classAssocIdx.getOrPut(className) { mutableListOf() }
                        .add(Triple(association, association.targetEnd, true))
                }
                val isSubOfTarget = subCache[className]?.get(targetType) ?: false
                if (isSubOfTarget) {
                    classAssocIdx.getOrPut(className) { mutableListOf() }
                        .add(Triple(association, association.sourceEnd, false))
                }
            }
        }
        classAssocEndIndex = classAssocIdx

        // 6. Ownership association index — maps association name → ownership role
        val ownerAssocIdx = mutableMapOf<String, OwnershipAssociationInfo>()
        for (metaClass in classes.values) {
            val binding = metaClass.ownershipBinding ?: continue
            for (assoc in associations.values) {
                // Skip derived associations — links are not stored under these names
                if (assoc.sourceEnd.isDerived && assoc.targetEnd.isDerived) continue

                // Check: intermediate is on TARGET side, sourceEnd.name matches ownerEnd
                // This means: source (owner) → target (intermediate)
                if (assoc.sourceEnd.name == binding.ownerEnd) {
                    val isCompatible = subCache[metaClass.name]?.get(assoc.targetEnd.type) ?: false
                    if (isCompatible) {
                        ownerAssocIdx[assoc.name] = OwnershipAssociationInfo(
                            metaClass.name, binding, OwnershipLinkRole.OWNER_TO_INTERMEDIATE
                        )
                    }
                }

                // Check: intermediate is on SOURCE side, targetEnd.name matches ownedElementEnd
                // This means: source (intermediate) → target (child)
                if (assoc.targetEnd.name == binding.ownedElementEnd) {
                    val isCompatible = subCache[metaClass.name]?.get(assoc.sourceEnd.type) ?: false
                    if (isCompatible) {
                        ownerAssocIdx[assoc.name] = OwnershipAssociationInfo(
                            metaClass.name, binding, OwnershipLinkRole.INTERMEDIATE_TO_CHILD
                        )
                    }
                }
            }
        }
        ownershipAssocIndex = ownerAssocIdx
        if (ownerAssocIdx.isNotEmpty()) {
            logger.debug { "Ownership association index: ${ownerAssocIdx.entries.joinToString { "${it.key} → ${it.value.role}" }}" }
        }

        logger.info { "Metamodel indexes built: ${classes.size} classes, ${associations.size} associations" }
    }

    /**
     * Get the pre-computed association ends for a class.
     * Returns null if indexes haven't been built yet.
     */
    fun getAssociationEndsForClass(className: String): List<Triple<MetaAssociation, MetaAssociationEnd, Boolean>>? {
        if (classAssocEndIndex.isEmpty()) return null
        return classAssocEndIndex[className]
    }

    /**
     * Check if an association plays a role in the ownership chain.
     * Returns ownership role info if found, null otherwise.
     */
    fun getOwnershipAssociationRole(associationName: String): OwnershipAssociationInfo? =
        ownershipAssocIndex[associationName]

    /**
     * Retrieve a MetaClass by name.
     */
    fun getClass(name: String): MetaClass? = classes[name]

    /**
     * Retrieve a MetaAssociation by name.
     */
    fun getAssociation(name: String): MetaAssociation? = associations[name]

    /**
     * Get all registered MetaClasses (excluding framework base classes).
     * This returns only user-defined classes, not internal framework classes like MDMBaseClass.
     */
    fun getAllClasses(): Collection<MetaClass> = classes.values.filter { it.name != DEFAULT_BASE_CLASS }

    /**
     * Get all registered MetaClasses including framework base classes.
     * Use this for internal operations that need the complete class hierarchy.
     */
    fun getAllClassesIncludingFramework(): Collection<MetaClass> = classes.values

    /**
     * Get all registered MetaAssociations.
     */
    fun getAllAssociations(): Collection<MetaAssociation> = associations.values

    /**
     * Check if a class is a subclass of another (directly or transitively).
     */
    fun isSubclassOf(subclass: String, superclass: String): Boolean {
        if (subclass == superclass) return true

        // Use pre-computed cache if available
        isSubclassCache[subclass]?.let { return it[superclass] ?: false }

        // Fallback to recursive lookup
        val metaClass = classes[subclass] ?: return false
        return metaClass.superclasses.any { isSubclassOf(it, superclass) }
    }

    /**
     * Get all superclasses of a given class (transitively).
     */
    fun getAllSuperclasses(className: String): Set<String> {
        // Use pre-computed cache if available
        superclassCache[className]?.let { return it }

        // Fallback to recursive lookup
        val result = mutableSetOf<String>()
        collectSuperclasses(className, result)
        return result
    }

    private fun collectSuperclasses(className: String, accumulator: MutableSet<String>) {
        val metaClass = classes[className] ?: return

        metaClass.superclasses.forEach { superclass ->
            if (accumulator.add(superclass)) {
                collectSuperclasses(superclass, accumulator)
            }
        }
    }

    /**
     * Get all direct subclasses of a given class.
     */
    fun getDirectSubclasses(className: String): Set<String> =
        subclassIndex[className] ?: emptySet()

    /**
     * Validate the entire metamodel for consistency.
     * Returns a list of validation errors, or empty list if valid.
     */
    fun validate(): List<String> {
        val errors = mutableListOf<String>()

        // Check for missing superclasses
        classes.values.forEach { metaClass ->
            metaClass.superclasses.forEach { superclass ->
                if (!classes.containsKey(superclass)) {
                    errors.add(
                        "MetaClass '${metaClass.name}' references unknown superclass: $superclass"
                    )
                }
            }

            // Check for circular inheritance
            if (hasCircularInheritance(metaClass.name, mutableSetOf())) {
                errors.add("MetaClass '${metaClass.name}' has circular inheritance")
            }
        }

        // Check associations reference valid classes
        associations.values.forEach { assoc ->
            if (!classes.containsKey(assoc.sourceEnd.type)) {
                errors.add(
                    "MetaAssociation '${assoc.name}' source references unknown class: ${assoc.sourceEnd.type}"
                )
            }
            if (!classes.containsKey(assoc.targetEnd.type)) {
                errors.add(
                    "MetaAssociation '${assoc.name}' target references unknown class: ${assoc.targetEnd.type}"
                )
            }
        }

        return errors
    }

    private fun hasCircularInheritance(className: String, visited: MutableSet<String>): Boolean {
        if (!visited.add(className)) {
            return true // Already visited - circular reference
        }

        val metaClass = classes[className] ?: return false

        return metaClass.superclasses.any { superclass ->
            hasCircularInheritance(superclass, visited.toMutableSet())
        }
    }

    /**
     * Get navigable association ends for a given class.
     *
     * Returns all association ends where:
     * - The sourceEnd.type == className and targetEnd.isNavigable, returns targetEnd
     * - The targetEnd.type == className and sourceEnd.isNavigable, returns sourceEnd
     *
     * @param className The class to get association ends for
     * @return List of pairs: (navigable end, owning class name for the end)
     */
    fun getNavigableEndsForClass(className: String): List<MetaAssociationEnd> {
        val result = mutableListOf<MetaAssociationEnd>()

        associations.values.forEach { association ->
            // If source is our class and target is navigable, include target end
            if (association.sourceEnd.type == className && association.targetEnd.isNavigable) {
                result.add(association.targetEnd)
            }
            // If target is our class and source is navigable, include source end
            if (association.targetEnd.type == className && association.sourceEnd.isNavigable) {
                result.add(association.sourceEnd)
            }
        }

        return result
    }

    /**
     * Get all navigable association ends for a class including inherited ones.
     *
     * @param className The class to get association ends for
     * @return List of navigable association ends (own + inherited)
     */
    fun getAllNavigableEndsForClass(className: String): List<MetaAssociationEnd> {
        val result = mutableListOf<MetaAssociationEnd>()
        val seenNames = mutableSetOf<String>()

        // Get own ends first
        getNavigableEndsForClass(className).forEach { end ->
            if (seenNames.add(end.name)) {
                result.add(end)
            }
        }

        // Get inherited ends from all superclasses
        getAllSuperclasses(className).forEach { superclass ->
            getNavigableEndsForClass(superclass).forEach { end ->
                if (seenNames.add(end.name)) {
                    result.add(end)
                }
            }
        }

        return result
    }

    /**
     * Clear all registered metamodel elements.
     */
    fun clear() {
        classes.clear()
        associations.clear()
        subclassIndex.clear()
        superclassCache = emptyMap()
        isSubclassCache = emptyMap()
        redefinesIndex = emptyMap()
        subsetsIndex = emptyMap()
        classAssocEndIndex = emptyMap()
        ownershipAssocIndex = emptyMap()
        logger.debug { "Registry cleared" }
    }

    /**
     * Find all association ends that redefine the given property name.
     * E.g., if `subclassifier` redefines `specific`, calling this with "specific"
     * returns the association end for `subclassifier`.
     */
    fun findRedefiningEnds(propertyName: String): List<Pair<MetaAssociation, MetaAssociationEnd>> {
        // Use pre-computed index if available
        redefinesIndex[propertyName]?.let { return it }
        if (redefinesIndex.isNotEmpty()) return emptyList()

        // Fallback to full scan
        val result = mutableListOf<Pair<MetaAssociation, MetaAssociationEnd>>()
        for (association in associations.values) {
            if (association.sourceEnd.redefines.contains(propertyName)) {
                result.add(association to association.sourceEnd)
            }
            if (association.targetEnd.redefines.contains(propertyName)) {
                result.add(association to association.targetEnd)
            }
        }
        return result
    }

    /**
     * Find all association ends that subset the given property name.
     * E.g., if `ownedMembership` subsets `ownedRelationship`, calling this with "ownedRelationship"
     * returns the association end for `ownedMembership`.
     */
    fun findSubsettingEnds(propertyName: String): List<Pair<MetaAssociation, MetaAssociationEnd>> {
        // Use pre-computed index if available
        subsetsIndex[propertyName]?.let { return it }
        if (subsetsIndex.isNotEmpty()) return emptyList()

        // Fallback to full scan
        val result = mutableListOf<Pair<MetaAssociation, MetaAssociationEnd>>()
        for (association in associations.values) {
            if (association.sourceEnd.subsets.contains(propertyName)) {
                result.add(association to association.sourceEnd)
            }
            if (association.targetEnd.subsets.contains(propertyName)) {
                result.add(association to association.targetEnd)
            }
        }
        return result
    }

    /**
     * Find an association by its source end name.
     * Used for reverse navigation in OCL - when navigating from target to source,
     * the property name used is the source end name.
     *
     * @param sourceEndName The name of the source end to find
     * @param targetClassName The class of the element being navigated FROM (the target end type)
     * @return The association and its source end, or null if not found
     */
    fun findAssociationBySourceEndName(
        sourceEndName: String,
        targetClassName: String
    ): Pair<MetaAssociation, MetaAssociationEnd>? {
        // Get all class names including superclasses for inheritance matching
        val targetClassHierarchy = setOf(targetClassName) + getAllSuperclasses(targetClassName)

        for (association in associations.values) {
            // Check if source end name matches and target end type is compatible
            if (association.sourceEnd.name == sourceEndName &&
                targetClassHierarchy.contains(association.targetEnd.type)
            ) {
                return association to association.sourceEnd
            }
        }
        return null
    }
}
