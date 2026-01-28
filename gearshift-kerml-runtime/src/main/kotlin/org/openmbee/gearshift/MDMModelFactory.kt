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
package org.openmbee.gearshift

import org.openmbee.gearshift.framework.runtime.MDMObject
import org.openmbee.gearshift.generated.Wrappers
import org.openmbee.gearshift.generated.interfaces.ModelElement
import org.openmbee.gearshift.generated.interfaces.Namespace
import java.time.Instant
import java.util.UUID

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
 * Base factory for creating and managing MDM models.
 *
 * Provides a model root concept and basic operations for creating/finding elements.
 * Domain-specific factories (like KerMLModelFactory) extend this to add their semantics.
 *
 * Project metadata is aligned with SysML v2 API Project schema for interoperability.
 * The project structure includes:
 * - id: Unique identifier (UUID format)
 * - name: Human-readable project name
 * - description: Optional description
 * - created: Creation timestamp
 */
abstract class MDMModelFactory(
    val engine: GearshiftEngine = GearshiftEngine(),
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
     * The root namespace that contains all model content.
     * All parsed packages, library content, and user content are added as members of this root.
     */
    lateinit var modelRoot: Namespace
        protected set

    /**
     * Initialize the model root. Called by subclasses after metamodel is loaded.
     * The model root's elementId is set to the project.id for easy identification.
     */
    protected fun initializeModelRoot() {
        // Create the root namespace with the project.id as its element ID
        val (_, obj) = engine.createInstance("Namespace", id = project.id)
        modelRoot = Wrappers.wrap(obj, engine) as Namespace
    }

    /**
     * Get a typed wrapper for an element by its ID.
     *
     * @param id The element ID
     * @return The typed wrapper or null if not found
     */
    fun getAsElement(id: String): ModelElement? {
        val obj = engine.getInstance(id) ?: return null
        return Wrappers.wrap(obj, engine)
    }

    /**
     * Create a new instance of a model element type by name.
     *
     * @param typeName The metaclass name
     * @return The created element wrapped
     */
    fun createByName(typeName: String): ModelElement {
        val (id, obj) = engine.createInstance(typeName)
        return Wrappers.wrap(obj, engine)
    }

    /**
     * Get all elements of a specific type by name.
     *
     * @param typeName The metaclass name
     * @return List of all elements matching the type
     */
    fun allOfTypeByName(typeName: String): List<ModelElement> {
        return engine.getInstancesByType(typeName).map { obj ->
            Wrappers.wrap(obj, engine)
        }
    }

    /**
     * Resolve a qualified name starting from the model root.
     *
     * @param qualifiedName The qualified name (e.g., "Package::Element")
     * @return The resolved MDMObject or null if not found
     */
    fun resolveQualifiedName(qualifiedName: String): MDMObject? {
        val modelRootId = (modelRoot as? org.openmbee.gearshift.generated.impl.BaseModelElementImpl)?.wrapped?.id
            ?: return null
        val result = engine.nameResolver.resolve(qualifiedName, modelRootId, false)
        return result?.membership
    }

    /**
     * Clear all model data and reset.
     */
    open fun reset() {
        engine.clearInstances()
        initializeModelRoot()
    }
}
