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
package org.openmbee.gearshift.kerml.exec

/**
 * Runtime context for behavior execution.
 *
 * Holds parameter bindings, local variables, and a reference to the parent
 * context for nested invocations (e.g., when a Step is typed by a Behavior
 * that is itself composed of Steps).
 *
 * Variable lookup follows lexical scoping — if a variable is not found in the
 * current context, the parent context is searched.
 */
class ExecutionContext(
    /** Parent context for nested invocations. null for top-level. */
    val parent: ExecutionContext? = null
) {
    /** Parameter and variable bindings in this scope. */
    private val bindings: MutableMap<String, Any?> = mutableMapOf()

    /**
     * Bind a parameter or variable in this context.
     */
    fun bind(name: String, value: Any?) {
        bindings[name] = value
    }

    /**
     * Look up a variable by name, searching parent scopes if not found locally.
     */
    fun resolve(name: String): Any? {
        if (bindings.containsKey(name)) {
            return bindings[name]
        }
        return parent?.resolve(name)
    }

    /**
     * Check if a variable is defined in this context or any parent.
     */
    fun isDefined(name: String): Boolean {
        if (bindings.containsKey(name)) return true
        return parent?.isDefined(name) ?: false
    }

    /**
     * Get all bindings in this context (not including parent).
     */
    fun getLocalBindings(): Map<String, Any?> = bindings.toMap()

    /**
     * Get all bindings visible from this context (including parent scopes).
     */
    fun getAllVisibleBindings(): Map<String, Any?> {
        val result = mutableMapOf<String, Any?>()
        // Start with parent bindings so local bindings override them
        parent?.getAllVisibleBindings()?.let { result.putAll(it) }
        result.putAll(bindings)
        return result
    }

    /**
     * Create a child context for a nested invocation.
     */
    fun createChild(): ExecutionContext {
        return ExecutionContext(parent = this)
    }

    /**
     * Create a child context with initial parameter bindings.
     */
    fun createChild(parameters: Map<String, Any?>): ExecutionContext {
        val child = ExecutionContext(parent = this)
        parameters.forEach { (name, value) -> child.bind(name, value) }
        return child
    }
}
