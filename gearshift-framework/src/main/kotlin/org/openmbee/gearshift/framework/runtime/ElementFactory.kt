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

import org.openmbee.gearshift.framework.meta.MetaClass

/**
 * Factory interface for creating MDMObject instances.
 *
 * This provides an IoC pattern for typed element creation. Implementations
 * can return domain-specific subclasses of MDMObject (like ElementImpl, NamespaceImpl)
 * rather than raw MDMObject instances.
 *
 * Key use cases:
 * 1. Creating new elements with proper typed implementations
 * 2. Deserializing elements from storage with correct types
 * 3. Allowing metamodel-specific implementations without coupling MDMEngine to them
 *
 * Example implementation for KerML:
 * ```kotlin
 * class KerMLElementFactory : ElementFactory {
 *     override fun createInstance(className: String, metaClass: MetaClass, engine: MDMEngine): MDMObject {
 *         return when (className) {
 *             "Element" -> throw IllegalArgumentException("Element is abstract")
 *             "Namespace" -> NamespaceImpl(className, metaClass, engine)
 *             "Package" -> PackageImpl(className, metaClass, engine)
 *             // ... other types
 *             else -> MDMObject(className, metaClass)
 *         }
 *     }
 * }
 * ```
 */
interface ElementFactory {

    /**
     * Create an instance of the specified class.
     *
     * @param className The name of the class to instantiate
     * @param metaClass The metaclass definition
     * @param engine The engine context (for implementations that need it)
     * @return A new MDMObject or subclass instance
     * @throws IllegalArgumentException if the class cannot be instantiated (e.g., abstract)
     */
    fun createInstance(className: String, metaClass: MetaClass, engine: MDMEngine): MDMObject

    /**
     * Check if this factory supports creating instances of the given class.
     *
     * @param className The class name to check
     * @return true if this factory can create instances of the class
     */
    fun supportsClass(className: String): Boolean = true
}

/**
 * Default factory that creates raw MDMObject instances.
 * Used when no specialized factory is registered.
 */
object DefaultElementFactory : ElementFactory {
    override fun createInstance(className: String, metaClass: MetaClass, engine: MDMEngine): MDMObject {
        if (metaClass.isAbstract) {
            throw IllegalArgumentException("Cannot instantiate abstract class: $className")
        }
        return MDMObject(className, metaClass)
    }
}

/**
 * Composite factory that delegates to registered factories.
 * Allows multiple factories to coexist (e.g., one for KerML, one for SysML).
 */
class CompositeElementFactory : ElementFactory {
    private val factories = mutableListOf<ElementFactory>()

    /**
     * Register a factory. Order matters - first factory that supports the class wins.
     */
    fun registerFactory(factory: ElementFactory) {
        factories.add(factory)
    }

    /**
     * Unregister a factory.
     */
    fun unregisterFactory(factory: ElementFactory) {
        factories.remove(factory)
    }

    override fun createInstance(className: String, metaClass: MetaClass, engine: MDMEngine): MDMObject {
        for (factory in factories) {
            if (factory.supportsClass(className)) {
                return factory.createInstance(className, metaClass, engine)
            }
        }
        // Fallback to default
        return DefaultElementFactory.createInstance(className, metaClass, engine)
    }

    override fun supportsClass(className: String): Boolean {
        return factories.any { it.supportsClass(className) }
    }
}
