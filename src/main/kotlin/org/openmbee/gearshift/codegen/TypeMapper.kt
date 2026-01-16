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
package org.openmbee.gearshift.codegen

import org.openmbee.gearshift.metamodel.MetaProperty

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
        "Any" to "Any"
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
     * Get the wrapper interface name for a type.
     */
    fun getInterfaceName(className: String): String = className

    /**
     * Get the implementation class name for a type.
     */
    fun getImplClassName(className: String): String = "${className}Impl"
}
