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
package org.openmbee.gearshift.kerml.eval.lib

import org.openmbee.gearshift.kerml.eval.FunctionRegistry

/**
 * Interface for a KerML kernel library function package.
 *
 * Each implementation corresponds to a KerML standard library `.kerml` file
 * (e.g., BaseFunctions, DataFunctions, etc.) and registers its functions
 * into the shared [FunctionRegistry].
 */
interface KernelLibraryFunction {

    /**
     * The KerML package name this library corresponds to (e.g., "BaseFunctions").
     */
    val packageName: String

    /**
     * Register all functions from this library into the registry.
     */
    fun register(registry: FunctionRegistry)
}
