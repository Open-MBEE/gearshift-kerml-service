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
package org.openmbee.gearshift.kerml.parser.visitors.base

import org.openmbee.gearshift.GearshiftEngine
import org.openmbee.gearshift.generated.Wrappers
import org.openmbee.gearshift.generated.interfaces.ModelElement
import org.openmbee.gearshift.kerml.antlr.KerMLParser

/**
 * Context passed to visitors during parsing.
 * Contains the engine, parent element, and namespace tracking for qualified name resolution.
 */
data class ParseContext(
    val engine: GearshiftEngine,
    val parent: ModelElement? = null,
    val parentQualifiedName: String = "",
    val namespaceStack: ArrayDeque<String> = ArrayDeque()
) {
    /**
     * Create a child context with a new parent element.
     */
    fun withParent(newParent: ModelElement, name: String = ""): ParseContext {
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
     * Create a typed wrapper from an MDMObject ID.
     */
    inline fun <reified T : ModelElement> wrap(id: String): T {
        val obj = engine.getInstance(id) ?: throw IllegalStateException("Instance not found: $id")
        return Wrappers.wrap(obj, engine) as T
    }

    /**
     * Create a new instance and return it as a typed wrapper.
     */
    inline fun <reified T : ModelElement> create(): T {
        val typeName = T::class.simpleName ?: throw IllegalArgumentException("Cannot determine type name")
        val (id, obj) = engine.createInstance(typeName)
        return Wrappers.wrap(obj, engine) as T
    }
}

/**
 * Base interface for typed KerML visitors.
 *
 * Each visitor handles a specific ANTLR parser context type (Ctx) and produces
 * a typed wrapper result (Result).
 *
 * @param Ctx The ANTLR parser context type this visitor handles
 * @param Result The typed wrapper type this visitor produces
 */
interface TypedKerMLVisitor<Ctx, Result : ModelElement> {

    /**
     * Visit the ANTLR context and produce a typed wrapper.
     *
     * @param ctx The ANTLR parser context
     * @param parseContext The parsing context with engine and parent info
     * @return The created element as a typed wrapper
     */
    fun visit(ctx: Ctx, parseContext: ParseContext): Result
}

/**
 * Abstract base class providing common functionality for typed visitors.
 *
 * @param Ctx The ANTLR parser context type this visitor handles
 * @param Result The typed wrapper type this visitor produces
 */
abstract class BaseTypedVisitor<Ctx, Result : ModelElement> : TypedKerMLVisitor<Ctx, Result> {

    /**
     * Parse the standard identification pattern (declaredShortName, declaredName).
     * Many KerML elements share this pattern.
     */
    protected fun parseIdentification(
        identification: KerMLParser.IdentificationContext?,
        element: org.openmbee.gearshift.generated.interfaces.Element
    ) {
        identification?.let { id ->
            id.declaredShortName?.let { shortName ->
                element.declaredShortName = shortName.text
            }
            id.declaredName?.let { name ->
                element.declaredName = name.text
            }
        }
    }

    /**
     * Extract a qualified name from a QualifiedNameContext.
     */
    protected fun extractQualifiedName(ctx: KerMLParser.QualifiedNameContext): String {
        val names = ctx.NAME().map { it.text }
        return if (ctx.DOLLAR() != null) {
            "\$::" + names.joinToString("::")
        } else {
            names.joinToString("::")
        }
    }

    /**
     * Compute the qualified name for an element given its parent qualified name.
     */
    protected fun computeQualifiedName(parentQualifiedName: String, declaredName: String?): String {
        return when {
            declaredName.isNullOrEmpty() -> parentQualifiedName
            parentQualifiedName.isEmpty() -> declaredName
            else -> "$parentQualifiedName::$declaredName"
        }
    }
}
