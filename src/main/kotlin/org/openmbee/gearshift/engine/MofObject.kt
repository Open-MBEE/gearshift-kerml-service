package org.openmbee.gearshift.engine

import org.openmbee.gearshift.metamodel.MetaClass

/**
 * Runtime instance of a metamodel class.
 * Holds property values and provides access to its metaclass definition.
 */
class MofObject(
    val className: String,
    val metaClass: MetaClass
) {
    private val properties = mutableMapOf<String, Any?>()

    /**
     * Set a property value.
     */
    fun setProperty(name: String, value: Any?) {
        properties[name] = value
    }

    /**
     * Get a property value.
     */
    fun getProperty(name: String): Any? = properties[name]

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
        return "MofObject(className='$className', properties=$properties)"
    }
}
