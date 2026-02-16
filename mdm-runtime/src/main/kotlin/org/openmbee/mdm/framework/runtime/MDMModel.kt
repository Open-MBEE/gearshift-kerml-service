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

import java.time.Instant
import java.util.*

/**
 * Project metadata aligned with SysML v2 API Project schema.
 * See: https://www.omg.org/spec/SystemsModelingAPI/20230201/Project
 *
 * @property id The unique identifier (@id in API)
 * @property name The project name (required)
 * @property description Optional project description
 * @property created Timestamp when the project was created
 */
data class ProjectMetadata(
    val id: String,
    val name: String,
    val description: String? = null,
    val created: Instant = Instant.now()
) {
    companion object {
        const val TYPE = "Project"
    }
}

/**
 * Base class for managing MDM models.
 *
 * Provides a project container with metadata and basic operations for creating/finding elements.
 * Domain-specific models (like KerMLModel) extend this to add their semantics, typed accessors,
 * and parsing capabilities.
 *
 * Project metadata is aligned with SysML v2 API Project schema for interoperability.
 * The project structure includes:
 * - id: Unique identifier (UUID format)
 * - name: Human-readable project name
 * - description: Optional description
 * - created: Creation timestamp
 *
 * @param engine The MDM engine managing element storage and associations
 * @param rootClassName The metaclass name for the model root element (e.g., "Namespace")
 * @param projectId Optional project ID (UUID generated if not provided)
 * @param projectName Human-readable project name
 * @param projectDescription Optional project description
 */
open class MDMModel(
    val engine: MDMEngine,
    rootClassName: String,
    projectId: String? = null,
    projectName: String = "Untitled Project",
    projectDescription: String? = null
) {
    /**
     * The project metadata containing id, name, description, and created timestamp.
     * Aligned with SysML v2 API Project schema.
     */
    val project: ProjectMetadata = ProjectMetadata(
        id = projectId ?: UUID.randomUUID().toString(),
        name = projectName,
        description = projectDescription
    )

    /**
     * The unique identifier for this model/project.
     * Convenience accessor for project.id.
     */
    val projectId: String get() = project.id

    /**
     * The root element that contains all model content.
     * All parsed packages, library content, and user content are added as members of this root.
     * The root's elementId is set to the project.id for easy identification.
     */
    val modelRoot: MDMObject = engine.createElement(rootClassName).also {
        it.setProperty("elementId", project.id)
    }

    /**
     * Get an element by its ID.
     *
     * @param id The element ID
     * @return The element or null if not found
     */
    fun getElement(id: String): MDMObject? = engine.getElement(id)

    /**
     * Create a new instance of a model element type by name.
     *
     * @param typeName The metaclass name
     * @return The created element
     */
    fun createElement(typeName: String): MDMObject = engine.createElement(typeName)

    /**
     * Get all elements of a specific type by name (including subtypes).
     *
     * @param typeName The metaclass name
     * @return List of all elements matching the type
     */
    fun allOfType(typeName: String): List<MDMObject> = engine.getElementsByClass(typeName)

    /**
     * Get all elements in the model.
     *
     * @return List of all elements
     */
    fun allElements(): List<MDMObject> = engine.getAllElements()

    /**
     * Clear all model data and recreate the root.
     */
    open fun reset() {
        engine.clear()
        // Note: modelRoot is val, so we can't reassign. Subclasses may override reset() fully.
    }
}
