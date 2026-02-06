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
import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.OwnershipBinding

private val logger = KotlinLogging.logger {}

/**
 * Resolved ownership pattern for a specific parent/child type combination.
 *
 * Contains the specific intermediate type and association ends to use.
 */
data class ResolvedOwnership(
    /**
     * The intermediate type to create (e.g., "FeatureMembership")
     */
    val intermediateType: String,

    /**
     * The binding from the intermediate class
     */
    val binding: OwnershipBinding,

    /**
     * The derived owner type from the association end
     */
    val ownerType: String,

    /**
     * The derived owned element type from the association end
     */
    val ownedElementType: String
)

/**
 * Resolves the most specific ownership intermediate for a given parent/child type combination.
 *
 * Uses the metamodel's inheritance hierarchy and OwnershipBinding declarations to find
 * the most specific intermediate type that can connect the parent and child.
 *
 * The type constraints (ownerType, ownedElementType) are derived from the association
 * end declarations, not specified manually in the binding.
 *
 * Algorithm:
 * 1. Find all classes with ownershipBinding set (ownership intermediates)
 * 2. Derive the type constraints from the association ends
 * 3. Filter to those compatible with parent/child types
 * 4. Return the most specific (deepest in inheritance hierarchy)
 *
 * Example resolution for KerML:
 * - Parent=Type, Child=Feature → FeatureMembership
 * - Parent=Function, Child=Expression → ResultExpressionMembership
 * - Parent=Namespace, Child=Package → OwningMembership (base case)
 */
class OwnershipResolver(private val registry: MetamodelRegistry) {

    /**
     * Cached binding info with derived types.
     */
    private data class BindingInfo(
        val metaClass: MetaClass,
        val binding: OwnershipBinding,
        val ownerType: String,
        val ownedElementType: String
    )

    /**
     * Cache of all ownership intermediate classes with derived types, built lazily.
     */
    private val intermediateBindings: List<BindingInfo> by lazy {
        registry.getAllClasses()
            .filter { it.isOwnershipIntermediate }
            .mapNotNull { metaClass ->
                val binding = metaClass.ownershipBinding ?: return@mapNotNull null
                val types = deriveTypes(metaClass.name, binding)
                if (types != null) {
                    BindingInfo(metaClass, binding, types.first, types.second)
                } else {
                    logger.warn { "Could not derive types for ownership binding on ${metaClass.name}" }
                    null
                }
            }
    }

    /**
     * Derive the owner and owned element types from the association ends.
     *
     * @return Pair of (ownerType, ownedElementType), or null if ends not found
     */
    private fun deriveTypes(intermediateClass: String, binding: OwnershipBinding): Pair<String, String>? {
        val ends = registry.getAllNavigableEndsForClass(intermediateClass)

        // Find the owner end and get its type
        val ownerEnd = ends.find { it.name == binding.ownerEnd }
        if (ownerEnd == null) {
            logger.debug { "Owner end '${binding.ownerEnd}' not found on $intermediateClass" }
            return null
        }

        // Find the owned element end and get its type
        val ownedEnd = ends.find { it.name == binding.ownedElementEnd }
        if (ownedEnd == null) {
            logger.debug { "Owned element end '${binding.ownedElementEnd}' not found on $intermediateClass" }
            return null
        }

        return ownerEnd.type to ownedEnd.type
    }

    /**
     * Resolve the most specific ownership intermediate for adding a child to a parent.
     *
     * @param parentType The metaclass name of the parent element
     * @param childType The metaclass name of the child element
     * @return The resolved ownership pattern, or null if no intermediate applies
     */
    fun resolve(parentType: String, childType: String): ResolvedOwnership? {
        // Find all compatible intermediates
        val compatibleIntermediates = intermediateBindings
            .filter { isCompatible(it, parentType, childType) }

        if (compatibleIntermediates.isEmpty()) {
            logger.debug { "No ownership intermediate found for $parentType → $childType" }
            return null
        }

        // Find the most specific one (deepest in inheritance for both owner and owned types)
        val mostSpecific = compatibleIntermediates
            .maxByOrNull { specificity(it, parentType, childType) }!!

        logger.trace {
            "Resolved ownership: $parentType → ${mostSpecific.metaClass.name} → $childType " +
                    "(owner end type: ${mostSpecific.ownerType}, owned end type: ${mostSpecific.ownedElementType})"
        }

        return ResolvedOwnership(
            intermediateType = mostSpecific.metaClass.name,
            binding = mostSpecific.binding,
            ownerType = mostSpecific.ownerType,
            ownedElementType = mostSpecific.ownedElementType
        )
    }

    /**
     * Check if an intermediate is compatible with the given parent/child types.
     *
     * An intermediate is compatible if:
     * - parentType is a subtype of the derived ownerType
     * - childType is a subtype of the derived ownedElementType
     */
    private fun isCompatible(info: BindingInfo, parentType: String, childType: String): Boolean {
        val ownerCompatible = registry.isSubclassOf(parentType, info.ownerType)
        val ownedCompatible = registry.isSubclassOf(childType, info.ownedElementType)

        return ownerCompatible && ownedCompatible
    }

    /**
     * Calculate the specificity of an intermediate for a given parent/child combination.
     * Higher value = more specific match.
     *
     * Specificity is based on how close the derived types are to the actual types.
     */
    private fun specificity(info: BindingInfo, parentType: String, childType: String): Int {
        // Sum of inheritance depths for both ends
        val ownerSpecificity = inheritanceDepth(info.ownerType, parentType)
        val ownedSpecificity = inheritanceDepth(info.ownedElementType, childType)

        return ownerSpecificity + ownedSpecificity
    }

    /**
     * Calculate how specific a binding type is for an actual type.
     * Higher value = binding type is closer to actual type in hierarchy.
     */
    private fun inheritanceDepth(bindingType: String, actualType: String): Int {
        if (bindingType == actualType) return Int.MAX_VALUE / 2  // Exact match

        // Count how many levels from actualType up to bindingType
        val superclasses = registry.getAllSuperclasses(actualType)
        return if (bindingType in superclasses) {
            // Closer binding type = higher specificity
            // (fewer levels between actual and binding = more specific)
            superclasses.size - superclasses.indexOf(bindingType)
        } else {
            0
        }
    }

    /**
     * Get all ownership intermediate types registered in the metamodel.
     */
    fun getAllIntermediates(): List<MetaClass> = intermediateBindings.map { it.metaClass }
}
