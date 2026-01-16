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

import java.nio.file.Path

/**
 * Configuration for the metamodel code generator.
 */
data class CodeGenConfig(
    /**
     * Base output directory for generated code.
     */
    val outputDir: Path,

    /**
     * Package name for generated interfaces.
     */
    val interfacePackage: String = "org.openmbee.gearshift.generated.interfaces",

    /**
     * Package name for generated implementation classes.
     */
    val implPackage: String = "org.openmbee.gearshift.generated.impl",

    /**
     * Package name for utility classes (Wrappers, etc.).
     */
    val utilPackage: String = "org.openmbee.gearshift.generated",

    /**
     * Whether to generate abstract classes for abstract metaclasses.
     */
    val generateAbstractClasses: Boolean = true,

    /**
     * List of class names to include (empty = include all).
     */
    val includeClasses: Set<String> = emptySet(),

    /**
     * List of class names to exclude.
     */
    val excludeClasses: Set<String> = emptySet(),

    /**
     * Whether to generate KDoc comments from descriptions.
     */
    val generateDocs: Boolean = true,

    /**
     * File header comment (copyright, license, etc.).
     */
    val fileHeader: String = DEFAULT_FILE_HEADER
) {
    companion object {
        val DEFAULT_FILE_HEADER = """
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
        """.trimIndent()

        /**
         * Create a default configuration for the given output directory.
         */
        fun default(outputDir: Path) = CodeGenConfig(outputDir = outputDir)
    }

    /**
     * Get the output path for interface files.
     */
    val interfaceOutputDir: Path
        get() = outputDir.resolve(interfacePackage.replace('.', '/'))

    /**
     * Get the output path for implementation files.
     */
    val implOutputDir: Path
        get() = outputDir.resolve(implPackage.replace('.', '/'))

    /**
     * Get the output path for utility files.
     */
    val utilOutputDir: Path
        get() = outputDir.resolve(utilPackage.replace('.', '/'))

    /**
     * Check if a class should be generated based on include/exclude lists.
     */
    fun shouldGenerate(className: String): Boolean {
        if (excludeClasses.contains(className)) return false
        if (includeClasses.isNotEmpty() && !includeClasses.contains(className)) return false
        return true
    }
}
