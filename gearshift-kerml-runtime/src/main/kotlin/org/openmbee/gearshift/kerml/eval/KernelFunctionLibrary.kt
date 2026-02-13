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
package org.openmbee.gearshift.kerml.eval

import org.openmbee.gearshift.kerml.eval.lib.KernelLibraryLoader
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MDMObject

/**
 * Facade for the KerML kernel function library.
 *
 * Preserves the original public API while delegating to the modular
 * [FunctionRegistry] + [KernelLibraryLoader] infrastructure. All function
 * implementations are organized into per-package library classes under
 * `eval/lib/`.
 */
class KernelFunctionLibrary(engine: MDMEngine) {

    private val registry = FunctionRegistry(engine)

    init {
        KernelLibraryLoader.initialize(registry)
    }

    /**
     * Apply a kernel function by operator name.
     *
     * @param operator The KerML operator string (e.g., "+", "and", "if")
     * @param arguments Evaluated argument values
     * @return The result of applying the function, or null if the operator is unknown
     */
    fun apply(operator: String, arguments: List<Any?>): FunctionResult? = registry.apply(operator, arguments)

    /**
     * Check whether this library has a native implementation for the given operator.
     */
    fun hasOperator(operator: String): Boolean = registry.hasOperator(operator)

    // === Delegate literal creation to registry ===

    fun createLiteralBoolean(value: Boolean): MDMObject = registry.createLiteralBoolean(value)

    fun createLiteralInteger(value: Long): MDMObject = registry.createLiteralInteger(value)

    fun createLiteralRational(value: Double): MDMObject = registry.createLiteralRational(value)

    fun createLiteralString(value: String): MDMObject = registry.createLiteralString(value)

    // === Delegate extraction to registry ===

    fun extractNumber(value: Any?): Number? = registry.extractNumber(value)

    fun extractBoolean(value: Any?): Boolean? = registry.extractBoolean(value)

    fun extractString(value: Any?): String? = registry.extractString(value)
}
