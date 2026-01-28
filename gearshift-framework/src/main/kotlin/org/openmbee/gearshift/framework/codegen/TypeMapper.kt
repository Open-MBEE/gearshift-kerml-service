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
package org.openmbee.gearshift.framework.codegen

import org.openmbee.gearshift.framework.meta.MetaAssociationEnd
import org.openmbee.gearshift.framework.meta.MetaOperation
import org.openmbee.gearshift.framework.meta.MetaParameter
import org.openmbee.gearshift.framework.meta.MetaProperty

/**
 * Maps metamodel types to Kotlin types for code generation.
 */
object TypeMapper {

    /**
     * Primitive type mappings from metamodel type names to Kotlin types.
     */
    private val primitiveTypes = mapOf(
        "String" to "String",
        "Boolean" to "Boolean",
        "Int" to "Int",
        "Integer" to "Int",
        "Long" to "Long",
        "Double" to "Double",
        "Float" to "Float",
        "Real" to "Double",
        "Any" to "Any",
        // MOF types
        "UnlimitedNatural" to "Int", // -1 represents unlimited/unbounded
        // Enum types - map to String for now until enum generation is added
        "VisibilityKind" to "String",
        "FeatureDirectionKind" to "String",
        "PortionKind" to "String",
        "RequirementConstraintKind" to "String",
        "StateSubactionKind" to "String",
        "TransitionFeatureKind" to "String",
        "TriggerKind" to "String"
    )

    /**
     * Check if a type is a primitive type.
     */
    fun isPrimitive(type: String): Boolean = primitiveTypes.containsKey(type)

    /**
     * Map a metamodel type to a Kotlin type.
     *
     * @param metaType The metamodel type name
     * @param isMultiValued Whether this is a collection type
     * @param isNullable Whether the type should be nullable
     * @param isOrdered Whether the collection should maintain order (for multi-valued)
     * @param isUnique Whether the collection should contain unique values (for multi-valued)
     * @return The Kotlin type string
     */
    fun mapToKotlinType(
        metaType: String,
        isMultiValued: Boolean = false,
        isNullable: Boolean = true,
        isOrdered: Boolean = false,
        isUnique: Boolean = true
    ): String {
        val baseType = primitiveTypes[metaType] ?: metaType

        return if (isMultiValued) {
            val collectionType = when {
                isOrdered -> "List"
                isUnique -> "Set"
                else -> "List"
            }
            "$collectionType<$baseType>"
        } else {
            if (isNullable) "$baseType?" else baseType
        }
    }

    /**
     * Map a MetaProperty to its Kotlin type.
     */
    fun mapPropertyType(property: MetaProperty): String {
        return mapToKotlinType(
            metaType = property.type,
            isMultiValued = property.isMultiValued,
            isNullable = !property.isRequired && !property.isMultiValued,
            isOrdered = property.isOrdered,
            isUnique = property.isUnique
        )
    }

    /**
     * Map a MetaOperation's return type to its Kotlin type.
     *
     * Uses returnLowerBound and returnUpperBound:
     * - returnUpperBound == 1: single value
     * - returnUpperBound == -1 or > 1: multi-valued (List)
     * - returnLowerBound == 0: nullable (for single values)
     * - returnLowerBound >= 1: non-nullable
     */
    fun mapOperationReturnType(operation: MetaOperation): String {
        val returnType = operation.returnType ?: return "Unit"

        val isMultiValued = operation.returnUpperBound == -1 || operation.returnUpperBound > 1
        val isNullable = operation.returnLowerBound == 0 && !isMultiValued

        return mapToKotlinType(
            metaType = returnType,
            isMultiValued = isMultiValued,
            isNullable = isNullable,
            isOrdered = true,  // Operations return ordered lists by default
            isUnique = false   // No uniqueness constraint on operation returns
        )
    }

    /**
     * Map a MetaParameter to its Kotlin type.
     *
     * Uses lowerBound and upperBound:
     * - upperBound == 1: single value
     * - upperBound == -1 or > 1: multi-valued (List)
     * - lowerBound == 0: nullable (for single values)
     * - lowerBound >= 1: non-nullable
     */
    fun mapParameterType(parameter: MetaParameter): String {
        val isMultiValued = parameter.upperBound == -1 || parameter.upperBound > 1
        val isNullable = parameter.lowerBound == 0 && !isMultiValued

        return mapToKotlinType(
            metaType = parameter.type,
            isMultiValued = isMultiValued,
            isNullable = isNullable,
            isOrdered = true,  // Parameters are ordered lists by default
            isUnique = false   // No uniqueness constraint on parameters
        )
    }

    /**
     * Get the default value for a type.
     */
    fun getDefaultValue(type: String, isMultiValued: Boolean = false): String {
        return if (isMultiValued) {
            "emptyList()"
        } else {
            when (type) {
                "String" -> "null"
                "Boolean" -> "false"
                "Int", "Integer" -> "0"
                "Long" -> "0L"
                "Double", "Real" -> "0.0"
                "Float" -> "0.0f"
                else -> "null"
            }
        }
    }

    /**
     * Check if a type is a model element type (not primitive).
     */
    fun isModelElementType(type: String): Boolean = !isPrimitive(type)

    /**
     * Map a MetaAssociationEnd to its Kotlin type.
     *
     * Uses lowerBound and upperBound:
     * - upperBound == 1: single value
     * - upperBound == -1 or > 1: multi-valued (List or Set)
     * - lowerBound == 0: nullable (for single values)
     * - lowerBound >= 1: non-nullable
     *
     * @param useAliases If true, uses aliased type names for types that conflict with Kotlin stdlib
     */
    fun mapAssociationEndType(end: MetaAssociationEnd, useAliases: Boolean = false): String {
        val isMultiValued = end.upperBound == -1 || end.upperBound > 1
        val isNullable = end.lowerBound == 0 && !isMultiValued
        val baseType = if (useAliases) getAliasedTypeName(end.type) else end.type

        return mapToKotlinType(
            metaType = baseType,
            isMultiValued = isMultiValued,
            isNullable = isNullable,
            isOrdered = end.isOrdered,
            isUnique = end.isUnique
        )
    }

    /**
     * Types that conflict with Kotlin stdlib and need aliases in impl files.
     * Maps type name to its alias.
     */
    private val typeAliases = mapOf(
        "Annotation" to "KerMLAnnotation",
        "Function" to "KerMLFunction"
    )

    /**
     * Check if a type name conflicts with Kotlin stdlib.
     */
    fun isConflictingType(type: String): Boolean = type in typeAliases

    /**
     * Get the aliased type name for use in implementation files.
     * Returns the original type if no alias is needed.
     */
    fun getAliasedTypeName(type: String): String = typeAliases[type] ?: type

    /**
     * Get the fully qualified type name for use in implementation files.
     * Returns null if no qualification is needed.
     */
    fun getQualifiedTypeName(type: String, interfacePackage: String): String? {
        return if (type in typeAliases) {
            "$interfacePackage.$type"
        } else {
            null
        }
    }

    /**
     * Get the wrapper interface name for a type.
     */
    fun getInterfaceName(className: String): String = className

    /**
     * Get the implementation class name for a type.
     */
    fun getImplClassName(className: String): String = "${className}Impl"
}
