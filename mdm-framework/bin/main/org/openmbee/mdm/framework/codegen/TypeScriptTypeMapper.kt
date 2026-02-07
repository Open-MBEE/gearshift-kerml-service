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
package org.openmbee.mdm.framework.codegen

import org.openmbee.mdm.framework.meta.MetaAssociationEnd
import org.openmbee.mdm.framework.meta.MetaProperty

/**
 * Maps metamodel types to TypeScript types for code generation.
 *
 * Association ends (references to other metaclasses) are mapped to `ElementRef`
 * since the frontend works with ID-based references, not inline objects.
 */
object TypeScriptTypeMapper {

    /**
     * Primitive type mappings from metamodel type names to TypeScript types.
     */
    private val primitiveTypes = mapOf(
        "String" to "string",
        "Boolean" to "boolean",
        "Int" to "number",
        "Integer" to "number",
        "Long" to "number",
        "Double" to "number",
        "Float" to "number",
        "Real" to "number",
        "Any" to "unknown",
        "UnlimitedNatural" to "number",
        // Enum types - map to string for now until enum generation is added
        "VisibilityKind" to "string",
        "FeatureDirectionKind" to "string",
        "PortionKind" to "string",
        "RequirementConstraintKind" to "string",
        "StateSubactionKind" to "string",
        "TransitionFeatureKind" to "string",
        "TriggerKind" to "string"
    )

    /**
     * Check if a type is a primitive type.
     */
    fun isPrimitive(type: String): Boolean = primitiveTypes.containsKey(type)

    /**
     * Check if a type is a model element type (non-primitive).
     */
    fun isModelElementType(type: String): Boolean = !isPrimitive(type)

    /**
     * Map a metamodel type to a TypeScript type string.
     *
     * Non-primitive types are mapped to `ElementRef` since the frontend
     * works with ID-based references via the SysML v2 API.
     */
    fun mapType(
        metaType: String,
        isMultiValued: Boolean = false
    ): String {
        val baseType = if (isPrimitive(metaType)) {
            primitiveTypes[metaType]!!
        } else {
            "ElementRef"
        }

        return if (isMultiValued) {
            "$baseType[]"
        } else {
            baseType
        }
    }

    /**
     * Map a MetaProperty to its TypeScript type.
     */
    fun mapPropertyType(property: MetaProperty): String {
        return mapType(
            metaType = property.type,
            isMultiValued = property.isMultiValued
        )
    }

    /**
     * Map a MetaAssociationEnd to its TypeScript type.
     * Association ends always map to ElementRef (ID-based references).
     */
    fun mapAssociationEndType(end: MetaAssociationEnd): String {
        val isMultiValued = end.upperBound == -1 || end.upperBound > 1

        return mapType(
            metaType = end.type,
            isMultiValued = isMultiValued
        )
    }
}