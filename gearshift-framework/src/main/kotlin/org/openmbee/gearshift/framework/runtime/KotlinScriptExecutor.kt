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
package org.openmbee.gearshift.framework.runtime

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.gearshift.framework.constraints.EngineAccessor
import javax.script.ScriptEngineManager
import javax.script.SimpleBindings

private val logger = KotlinLogging.logger {}

/**
 * Executes Kotlin code from strings at runtime using JSR-223 scripting.
 * Provides a context with 'self' (the instance), 'args' (operation arguments),
 * and 'engine' (for accessing other objects/operations).
 */
class KotlinScriptExecutor(
    private val engineAccessor: EngineAccessor
) {
    private val scriptEngine by lazy {
        ScriptEngineManager().getEngineByExtension("kts")
            ?: throw IllegalStateException("Kotlin scripting engine not found. Ensure kotlin-scripting-jsr223 is on the classpath.")
    }

    /**
     * Execute a Kotlin script with the given context.
     *
     * @param script The Kotlin code to execute
     * @param self The instance the operation is being invoked on
     * @param args The operation arguments
     * @return The result of the script evaluation
     */
    fun execute(
        script: String,
        self: MDMObject,
        args: Map<String, Any?>
    ): Any? {
        // Use internal names to avoid clashing with script-level bindings
        val bindings = SimpleBindings().apply {
            put("__self__", self)
            put("__args__", args)
            put("__engine__", engineAccessor)
        }

        // Wrap the script to provide imports, typed variables, and a labeled lambda
        // The labeled lambda allows scripts to use `return@kotlinBody` for early returns
        val wrappedScript = """
            import org.openmbee.gearshift.framework.runtime.*
            import org.openmbee.gearshift.framework.constraints.EngineAccessor

            @Suppress("UNCHECKED_CAST")
            val self = bindings["__self__"] as MDMObject
            @Suppress("UNCHECKED_CAST")
            val args = bindings["__args__"] as Map<String, Any?>
            val engine = bindings["__engine__"] as EngineAccessor

            val kotlinBody: () -> Any? = kotlinBody@{
                $script
            }
            kotlinBody()
        """.trimIndent()

        return try {
            scriptEngine.eval(wrappedScript, bindings)
        } catch (e: Exception) {
            logger.error(e) { "Kotlin script execution failed" }
            throw e
        }
    }
}
