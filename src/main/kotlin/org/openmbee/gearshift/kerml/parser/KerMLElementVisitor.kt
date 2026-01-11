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
