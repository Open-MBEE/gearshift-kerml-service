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

/**
 * Loads [GearshiftSettings] from system properties, allowing overrides
 * via Gradle `-D` flags or JVM system properties.
 *
 * Property prefix: `gearshift.`
 *
 * Example usage from Gradle:
 * ```
 * ./gradlew runDemoApi -Dgearshift.autoNameFeatures=true -Dgearshift.maxExecutionSteps=5000
 * ```
 *
 * Unset properties fall back to [GearshiftSettings.DEFAULT] values.
 */
object GearshiftSettingsLoader {

    private const val PREFIX = "gearshift."

    /**
     * Build a [GearshiftSettings] by reading system properties with the `gearshift.` prefix.
     * Any property not set in the environment keeps its default value.
     */
    fun fromSystemProperties(): GearshiftSettings {
        val defaults = GearshiftSettings.DEFAULT
        return GearshiftSettings(
            autoNameFeatures = boolProp("autoNameFeatures", defaults.autoNameFeatures),
            nameStrategy = enumProp("nameStrategy", defaults.nameStrategy),
            autoShortNames = boolProp("autoShortNames", defaults.autoShortNames),
            processImpliedRelationships = boolProp("processImpliedRelationships", defaults.processImpliedRelationships),
            autoMountLibraries = boolProp("autoMountLibraries", defaults.autoMountLibraries),
            maxExecutionSteps = intProp("maxExecutionSteps", defaults.maxExecutionSteps),
            preferSymbolicSyntax = boolProp("preferSymbolicSyntax", defaults.preferSymbolicSyntax),
            emitImpliedRelationships = boolProp("emitImpliedRelationships", defaults.emitImpliedRelationships),
            writerIndent = stringProp("writerIndent", defaults.writerIndent),
            deterministicElementIds = boolProp("deterministicElementIds", defaults.deterministicElementIds)
        )
    }

    private fun boolProp(name: String, default: Boolean): Boolean =
        System.getProperty("$PREFIX$name")?.toBooleanStrictOrNull() ?: default

    private fun intProp(name: String, default: Int): Int =
        System.getProperty("$PREFIX$name")?.toIntOrNull() ?: default

    private fun stringProp(name: String, default: String): String =
        System.getProperty("$PREFIX$name") ?: default

    private inline fun <reified T : Enum<T>> enumProp(name: String, default: T): T {
        val value = System.getProperty("$PREFIX$name") ?: return default
        return try {
            enumValueOf<T>(value.uppercase())
        } catch (_: IllegalArgumentException) {
            default
        }
    }
}
