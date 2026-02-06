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
package org.openmbee.gearshift.kerml

import io.kotest.core.spec.style.DescribeSpec
import org.openmbee.mdm.framework.runtime.MountRegistry

/**
 * Test support for KerML tests with shared library loading.
 *
 * The KerML Kernel Semantic Library is expensive to load (parsing multiple files).
 * This support class initializes the library once and shares it across all tests
 * via the mount system.
 *
 * Usage:
 * ```kotlin
 * class MyTest : KerMLTestSpec({
 *     describe("my tests") {
 *         it("should do something") {
 *             // freshModel() gives you a clean model with library mounted
 *             val factory = freshModel()
 *             factory.parseString("class MyClass;")
 *             // ...assertions...
 *         }
 *     }
 * })
 * ```
 *
 * The library is initialized lazily on first test run and stays mounted
 * for the entire test session. Each test gets a fresh model that shares
 * the mounted library content.
 */
abstract class KerMLTestSpec(body: KerMLTestSpec.() -> Unit) : DescribeSpec() {

    companion object {
        /**
         * Flag to track if library has been initialized.
         */
        @Volatile
        private var libraryInitialized = false

        /**
         * Initialize the full Kernel Semantic Library once.
         * Thread-safe via double-checked locking.
         */
        @Synchronized
        fun ensureLibraryInitialized() {
            if (!libraryInitialized) {
                val mount = KerMLModel.initializeKernelLibrary()
                if (mount != null) {
                    println("KerMLTestSpec: Library loaded with ${mount.engine.elementCount()} elements")
                } else {
                    System.err.println("KerMLTestSpec: WARNING - Library not available, tests may fail")
                }
                libraryInitialized = true
            }
        }
    }

    init {
        // Initialize library before any tests run
        ensureLibraryInitialized()

        // Run the test body
        body()
    }

    /**
     * Create a fresh KerMLModel with the Base library mounted.
     *
     * Each call returns a new, empty model that shares the mounted Base library.
     * Use this at the start of each test for isolation.
     */
    fun freshModel(): KerMLModel {
        return KerMLModel.createWithMounts()
    }
}

/**
 * Object providing static access to library initialization.
 *
 * Use this if you need to initialize the library outside of a test spec,
 * for example in a project-level configuration.
 */
object KerMLTestLibrary {

    /**
     * Initialize the Kernel Semantic Library for testing.
     *
     * Safe to call multiple times - subsequent calls are no-ops.
     *
     * @return true if library is available, false otherwise
     */
    fun initialize(): Boolean {
        KerMLTestSpec.ensureLibraryInitialized()
        return MountRegistry.isRegistered(KerMLModel.KERNEL_LIBRARY_MOUNT_ID)
    }

    /**
     * Check if the library has been initialized.
     */
    fun isInitialized(): Boolean {
        return MountRegistry.isRegistered(KerMLModel.KERNEL_LIBRARY_MOUNT_ID)
    }

    /**
     * Create a fresh model with the library mounted.
     *
     * Automatically initializes the library if needed.
     */
    fun createModel(): KerMLModel {
        initialize()
        return KerMLModel.createWithMounts()
    }
}
