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
import java.util.concurrent.ConcurrentHashMap

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

    /**
     * Register a MetaClass in the registry.
     */
    fun registerClass(metaClass: MetaClass) {
        classes[metaClass.name] = metaClass

        // Build subclass index
        metaClass.superclasses.forEach { superclass ->
            subclassIndex.getOrPut(superclass) { ConcurrentHashMap.newKeySet() }
                .add(metaClass.name)
        }

        logger.debug { "Registered MetaClass: ${metaClass.name}" }
    }

    /**
     * Register a MetaAssociation in the registry.
     */
    fun registerAssociation(association: MetaAssociation) {
        associations[association.name] = association
        logger.debug { "Registered MetaAssociation: ${association.name}" }
    }

    /**
     * Retrieve a MetaClass by name.
     */
    fun getClass(name: String): MetaClass? = classes[name]

    /**
     * Retrieve a MetaAssociation by name.
     */
    fun getAssociation(name: String): MetaAssociation? = associations[name]

    /**
     * Get all registered MetaClasses.
     */
    fun getAllClasses(): Collection<MetaClass> = classes.values

    /**
     * Get all registered MetaAssociations.
     */
    fun getAllAssociations(): Collection<MetaAssociation> = associations.values

    /**
     * Check if a class is a subclass of another (directly or transitively).
     */
    fun isSubclassOf(subclass: String, superclass: String): Boolean {
        if (subclass == superclass) return true

        val metaClass = classes[subclass] ?: return false

        return metaClass.superclasses.any { isSubclassOf(it, superclass) }
    }

    /**
     * Get all superclasses of a given class (transitively).
     */
    fun getAllSuperclasses(className: String): Set<String> {
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
        logger.debug { "Registry cleared" }
    }

    /**
     * Find all association ends that redefine the given property name.
     * E.g., if `subclassifier` redefines `specific`, calling this with "specific"
     * returns the association end for `subclassifier`.
     */
    fun findRedefiningEnds(propertyName: String): List<Pair<MetaAssociation, MetaAssociationEnd>> {
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
