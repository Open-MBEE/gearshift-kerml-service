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
 * Application-level settings for the Gearshift KerML service.
 *
 * These defaults control behavior across the parser, generator, execution,
 * and API layers. Settings are designed to be overridable per-model or per-request.
 */
data class GearshiftSettings(

    // ── Naming ──────────────────────────────────────────────────────────

    /**
     * Automatically assign default names to unnamed features after parsing
     * or element creation. Names are derived from the feature's type using
     * the configured [nameStrategy].
     */
    val autoNameFeatures: Boolean = false,

    /**
     * Strategy for generating default declared names from type names.
     */
    val nameStrategy: NameStrategy = NameStrategy.CAMEL_CASE,

    /**
     * Whether to generate default short names (e.g., p1, p2) for unnamed
     * features. Short names appear as `<shortName>` in KerML syntax.
     */
    val autoShortNames: Boolean = true,

    // ── Semantic Processing ─────────────────────────────────────────────

    /**
     * Whether to process implied relationships (semantic bindings) after
     * parsing. When false, no implicit specializations, subsettings, or
     * type featurings are created. Useful for raw structural import or
     * testing without library dependencies.
     */
    val processImpliedRelationships: Boolean = true,

    /**
     * Whether to automatically mount the KerML kernel and base libraries
     * as implicit read-only mounts when creating a model. When false,
     * library types (Base::Anything, etc.) are not available for
     * resolution or semantic binding.
     */
    val autoMountLibraries: Boolean = true,

    // ── Behavior Execution ──────────────────────────────────────────────

    /**
     * Maximum number of execution steps before the behavior execution
     * engine terminates with MAX_STEPS_EXCEEDED. Protects against
     * infinite loops in cyclic behavior graphs.
     */
    val maxExecutionSteps: Int = 1000,

    // ── Writer / Generation ─────────────────────────────────────────────

    /**
     * Use symbolic syntax (`:>`, `:>>`, `:`) instead of keyword syntax
     * (`specializes`, `redefines`, `typed by`) in generated KerML text.
     */
    val preferSymbolicSyntax: Boolean = true,

    /**
     * Include implied relationships in generated KerML text output.
     * When false (default), relationships created by semantic inference
     * (e.g., implicit subsetting to Base::Anything) are omitted for
     * cleaner round-trip output.
     */
    val emitImpliedRelationships: Boolean = false,

    /**
     * Indentation string used in KerML text generation.
     */
    val writerIndent: String = "    ",

    // ── Parser ──────────────────────────────────────────────────────────

    /**
     * Use deterministic UUID v5 (based on qualified name) for all parsed
     * elements, not just library elements. When false (default), only
     * library elements get deterministic IDs; user elements get random
     * UUIDs. Enable for reproducible test fixtures or diff-friendly output.
     */
    val deterministicElementIds: Boolean = false
) {
    companion object {
        /**
         * Default settings — no auto-naming, suitable for strict round-trip fidelity.
         */
        val DEFAULT = GearshiftSettings()

        /**
         * Editor-friendly settings — auto-naming enabled, matching Cameo-style UX.
         */
        val EDITOR = GearshiftSettings(
            autoNameFeatures = true,
            autoShortNames = true
        )

        /**
         * Lightweight settings for unit testing — no libraries, no implied
         * relationships, deterministic IDs for reproducibility.
         */
        val TESTING = GearshiftSettings(
            autoMountLibraries = false,
            processImpliedRelationships = false,
            deterministicElementIds = true
        )
    }
}

/**
 * Strategy for generating default names from type names.
 */
enum class NameStrategy {
    /** Engine → engine, PowerSystem → powerSystem */
    CAMEL_CASE,

    /** Engine → engine, PowerSystem → power_system */
    SNAKE_CASE
}
