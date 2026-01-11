package org.openmbee.gearshift.kerml.parser

import org.openmbee.gearshift.GearshiftEngine
import org.openmbee.gearshift.engine.MofObject

/**
 * Base visitor interface for KerML grammar elements.
 * Implements the Visitor pattern for constructing KerML element instances from parsed grammar.
 */
interface KerMLElementVisitor<T> {
    /**
     * Visit a grammar context node and return the constructed KerML element.
     *
     * @param ctx The ANTLR parser context to visit
     * @param engine The Gearshift engine for creating instances
     * @return The constructed element (typically a MofObject instance ID or the object itself)
     */
    fun visit(ctx: T, engine: GearshiftEngine): Any?
}

/**
 * Context for passing metadata during visitor traversal.
 */
data class VisitorContext(
    val engine: GearshiftEngine,
    val parentElement: String? = null,
    val metadata: MutableMap<String, Any> = mutableMapOf()
)

/**
 * Base abstract visitor providing common functionality for all KerML element visitors.
 */
abstract class BaseKerMLVisitor<T> : KerMLElementVisitor<T> {

    /**
     * Create an instance of a KerML element in the engine.
     */
    protected fun createInstance(
        engine: GearshiftEngine,
        metaclassName: String,
        elementId: String? = null
    ): Pair<String, MofObject> {
        return engine.createInstance(metaclassName, elementId)
    }

    /**
     * Set a property on a KerML element instance.
     */
    protected fun setProperty(
        engine: GearshiftEngine,
        instanceId: String,
        propertyName: String,
        value: Any?
    ) {
        engine.setProperty(instanceId, propertyName, value)
    }

    /**
     * Get a property from a KerML element instance.
     */
    protected fun getProperty(
        engine: GearshiftEngine,
        instanceId: String,
        propertyName: String
    ): Any? {
        return engine.getProperty(instanceId, propertyName)
    }
}
