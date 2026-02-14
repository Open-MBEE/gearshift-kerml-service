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

import org.openmbee.gearshift.generated.interfaces.Feature
import org.openmbee.gearshift.generated.interfaces.Namespace
import org.openmbee.gearshift.settings.GearshiftSettings
import org.openmbee.gearshift.settings.NameStrategy

/**
 * Generates default names for newly created elements, similar to
 * Cameo's auto-naming behavior for SysML v1 properties and ports.
 *
 * Naming strategy:
 * - `declaredName`: derived from type name using the configured [org.openmbee.gearshift.settings.NameStrategy]
 * - `declaredShortName`: letter prefix + index (e.g., `p1`, `p2`, `e1`)
 *
 * When a name collision occurs in the owning namespace, an index suffix
 * is appended (`engine`, `engine2`, `engine3`).
 */
object DefaultNameGenerator {

    /**
     * Generate a default declared name from a type name using the given strategy.
     *
     * - [org.openmbee.gearshift.settings.NameStrategy.CAMEL_CASE]: "Engine" → "engine", "PowerSystem" → "powerSystem"
     * - [org.openmbee.gearshift.settings.NameStrategy.SNAKE_CASE]: "Engine" → "engine", "PowerSystem" → "power_system"
     */
    fun defaultNameFromType(typeName: String, strategy: NameStrategy = NameStrategy.CAMEL_CASE): String {
        if (typeName.isBlank()) return "element"
        return when (strategy) {
            NameStrategy.CAMEL_CASE -> typeName.replaceFirstChar { it.lowercase() }
            NameStrategy.SNAKE_CASE -> toSnakeCase(typeName)
        }
    }

    /**
     * Generate a unique name by appending an index suffix if needed.
     *
     * @param baseName The base name (e.g., "engine")
     * @param existingNames Names already in use in the scope
     * @return A unique name (e.g., "engine", "engine2", "engine3")
     */
    fun uniqueName(baseName: String, existingNames: Set<String>): String {
        if (baseName !in existingNames) return baseName
        var index = 2
        while ("$baseName$index" in existingNames) index++
        return "$baseName$index"
    }

    /**
     * Generate a default short name for a feature (e.g., "p1", "p2").
     *
     * Uses the first letter of the type name as prefix, or "e" for
     * untyped elements. Always includes an index suffix.
     *
     * @param typeName The type name, if known
     * @param existingShortNames Short names already in use in the scope
     * @return A unique short name like "p1", "e2"
     */
    fun defaultShortName(typeName: String?, existingShortNames: Set<String>): String {
        val prefix = typeName?.firstOrNull()?.lowercase() ?: "e"
        var index = 1
        while ("$prefix$index" in existingShortNames) index++
        return "$prefix$index"
    }

    /**
     * Collect the declared names currently used by members of a namespace.
     */
    fun existingNames(namespace: Namespace): Set<String> {
        return namespace.ownedMember.mapNotNull { it.declaredName }.toSet()
    }

    /**
     * Collect the declared short names currently used by members of a namespace.
     */
    fun existingShortNames(namespace: Namespace): Set<String> {
        return namespace.ownedMember.mapNotNull { it.declaredShortName }.toSet()
    }

    /**
     * Apply default names to a feature based on its typing and settings.
     *
     * Sets `declaredName` and optionally `declaredShortName` only if they
     * are not already set.
     *
     * @param feature The feature to name
     * @param owningNamespace The namespace that owns this feature
     * @param settings Controls naming strategy and short name generation
     */
    fun applyDefaults(
        feature: Feature,
        owningNamespace: Namespace,
        settings: GearshiftSettings = GearshiftSettings.DEFAULT
    ) {
        val typeName = feature.ownedTyping.firstOrNull()?.type?.declaredName

        if (feature.declaredName == null) {
            val baseName = if (typeName != null) {
                defaultNameFromType(typeName, settings.nameStrategy)
            } else {
                "feature"
            }
            feature.declaredName = uniqueName(baseName, existingNames(owningNamespace))
        }

        if (settings.autoShortNames && feature.declaredShortName == null) {
            feature.declaredShortName = defaultShortName(typeName, existingShortNames(owningNamespace))
        }
    }

    private fun toSnakeCase(name: String): String {
        return name.replace(Regex("([a-z])([A-Z])"), "$1_$2")
            .replace(Regex("([A-Z]+)([A-Z][a-z])"), "$1_$2")
            .lowercase()
    }
}
