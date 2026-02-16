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

import org.openmbee.mdm.framework.meta.MetaClass
import org.openmbee.mdm.framework.meta.ModelElement

/**
 * Runtime instance of a metamodel class - represents a node in the model graph.
 * Holds property values and provides access to its metaclass definition.
 *
 * In the graph model:
 * - MDMObject instances are nodes
 * - MDMLink instances are edges
 * - MetaClass defines the node type/schema
 */
open class MDMObject(
    val className: String,
    val metaClass: MetaClass
) : ModelElement {
    protected val properties = mutableMapOf<String, Any?>()

    /**
     * The ID of this object in the repository.
     * Set when the object is stored in a repository.
     */
    override var id: String? = null

    /**
     * Set a property value.
     */
    fun setProperty(name: String, value: Any?) {
        properties[name] = value
    }

    /**
     * Get a property value.
     */
    override fun getProperty(name: String): Any? = properties[name]

    /**
     * Check if a property has been set.
     */
    fun hasProperty(name: String): Boolean = properties.containsKey(name)

    /**
     * Get all property names that have been set.
     */
    fun getAllProperties(): Map<String, Any?> = properties.toMap()

    /**
     * Remove a property value.
     */
    fun removeProperty(name: String) {
        properties.remove(name)
    }

    override fun toString(): String {
        val safeProperties = properties.mapValues { (_, value) ->
            when (value) {
                is MDMObject -> "MDMObject(id=${value.id}, className=${value.className})"
                is Collection<*> -> value.map { item ->
                    if (item is MDMObject) "MDMObject(id=${item.id}, className=${item.className})" else item
                }

                else -> value
            }
        }
        return "MDMObject(id=$id, className='$className', properties=$safeProperties)"
    }
}
