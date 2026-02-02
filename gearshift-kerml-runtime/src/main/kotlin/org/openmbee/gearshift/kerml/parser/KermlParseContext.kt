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

import org.openmbee.gearshift.generated.KerMLElementFactory
import org.openmbee.gearshift.generated.interfaces.Element
import org.openmbee.gearshift.generated.interfaces.ModelElement
import org.openmbee.gearshift.kerml.parser.visitors.base.ReferenceCollector
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MDMObject
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

/**
 * Context passed to visitors during parsing.
 * Contains the engine, parent element, namespace tracking, and reference collector.
 */
data class KermlParseContext(
    val engine: MDMEngine,
    val factory: KerMLElementFactory,
    val parent: ModelElement? = null,
    val parentQualifiedName: String = "",
    val namespaceStack: ArrayDeque<String> = ArrayDeque(),
    val referenceCollector: ReferenceCollector? = null,
    val memberVisibility: String = "public"
) {
    /**
     * Create a child context with a new parent element.
     */
    fun withParent(newParent: ModelElement, name: String = ""): KermlParseContext {
        val newStack = ArrayDeque(namespaceStack)
        if (name.isNotEmpty()) {
            newStack.addLast(name)
        }
        val newQualifiedName = if (parentQualifiedName.isEmpty()) name
        else if (name.isEmpty()) parentQualifiedName
        else "$parentQualifiedName::$name"
        return copy(
            parent = newParent,
            parentQualifiedName = newQualifiedName,
            namespaceStack = newStack
        )
    }

    /**
     * Create a new instance using the secondary constructor which handles ownership automatically.
     * The secondary constructor takes (engine, parent, declaredName, etc.) and uses OwnershipResolver
     * to set up the correct intermediate membership.
     *
     * @param declaredName Optional name for the element
     * @param declaredShortName Optional short name for the element
     */
    inline fun <reified T : ModelElement> create(
        declaredName: String? = null,
        declaredShortName: String? = null
    ): T {
        val typeName = T::class.simpleName ?: throw IllegalArgumentException("Cannot get simpleName for type")
        val implClassName = "org.openmbee.gearshift.generated.impl.${typeName}Impl"

        // Debug: Check if metaclass exists in schema
        val metaClass = engine.schema.getClass(typeName)
        if (metaClass == null) {
            throw IllegalStateException("MetaClass '$typeName' not found in schema. Available classes: ${engine.schema.getAllClasses().map { it.name }.sorted().take(10)}...")
        }

        @Suppress("UNCHECKED_CAST")
        val implClass = Class.forName(implClassName).kotlin as KClass<T>

        // Find the secondary constructor (the one that takes MDMEngine as first param)
        val constructor = implClass.constructors.find { ctor ->
            val params = ctor.parameters
            params.isNotEmpty() && params[0].type.classifier == MDMEngine::class
        } ?: throw IllegalArgumentException("No suitable constructor found for ${typeName}")

        // Build argument map for callBy() which handles defaults
        val args = mutableMapOf<KParameter, Any?>()
        for (param in constructor.parameters) {
            when (param.name) {
                "engine" -> args[param] = engine
                "parent" -> args[param] = parent as? Element
                "declaredName" -> if (declaredName != null) args[param] = declaredName
                "declaredShortName" -> if (declaredShortName != null) args[param] = declaredShortName
                // Let other params use their defaults
            }
        }

        try {
            val element = constructor.callBy(args) as MDMObject
            engine.registerElement(element)

            @Suppress("UNCHECKED_CAST")
            return element as T
        } catch (e: java.lang.reflect.InvocationTargetException) {
            val cause = e.cause
            throw IllegalStateException("Failed to construct $typeName: ${cause?.message}", cause)
        }
    }

    /**
     * Get the parent element ID for reference resolution context.
     */
    fun getParentElementId(): String? {
        return (parent as? Element)?.elementId
    }
}
