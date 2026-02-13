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

import io.github.oshai.kotlinlogging.KotlinLogging
import org.openmbee.gearshift.kerml.eval.FunctionRegistry

private val logger = KotlinLogging.logger {}

/**
 * Orchestrator that loads all kernel library function packages into a [FunctionRegistry].
 *
 * Libraries are loaded in dependency order: base types first, then specializations.
 * This mirrors the pattern of `KerMLMetamodelLoader.initialize(registry)`.
 */
object KernelLibraryLoader {

    /**
     * All kernel library function packages, in dependency order.
     */
    private val libraries: List<KernelLibraryFunction> = listOf(
        BaseFunctionsLibrary(),
        DataFunctionsLibrary(),
        ControlFunctionsLibrary(),
        BooleanFunctionsLibrary(),
        StringFunctionsLibrary(),
        SequenceFunctionsLibrary(),
        NumericalFunctionsLibrary(),
        IntegerFunctionsLibrary(),
        RealFunctionsLibrary(),
        RationalFunctionsLibrary(),
        NaturalFunctionsLibrary(),
        CollectionFunctionsLibrary(),
        TrigFunctionsLibrary()
    )

    /**
     * Initialize the registry with all kernel library functions.
     *
     * @param registry The function registry to populate
     */
    fun initialize(registry: FunctionRegistry) {
        for (lib in libraries) {
            lib.register(registry)
            logger.debug { "Loaded kernel library: ${lib.packageName}" }
        }
        logger.info { "KernelLibraryLoader: ${registry.size} functions registered from ${libraries.size} libraries" }
    }
}
