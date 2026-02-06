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
package org.openmbee.gearshift.kerml.generator

import org.openmbee.gearshift.generated.interfaces.*
import org.openmbee.gearshift.kerml.generator.base.KerMLGenerator
import kotlin.reflect.KClass

/**
 * Factory for obtaining the appropriate generator for a model element.
 *
 * Dispatches to specific generators based on element type, walking up
 * the type hierarchy to find the most specific registered generator.
 */
object GeneratorFactory {

    /**
     * Registry of generators keyed by their element type.
     */
    private val generators: Map<KClass<*>, KerMLGenerator<*>> = mapOf(
        // Classifiers
        Class::class to ClassGenerator(),
        DataType::class to DataTypeGenerator(),
        Structure::class to StructureGenerator(),
        Association::class to AssociationGenerator(),

        // Features
        Feature::class to FeatureGenerator(),

        // Namespaces
        Package::class to PackageGenerator(),

        // Imports
        Import::class to ImportGenerator(),
        NamespaceImport::class to ImportGenerator(),
        MembershipImport::class to ImportGenerator(),

        // Annotations
        Comment::class to CommentGenerator(),
    )

    /**
     * Generate KerML text for the given element.
     *
     * @param element The model element to generate
     * @param context The generation context
     * @return The KerML text representation
     * @throws IllegalArgumentException if no generator is found for the element type
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Element> generate(element: T, context: GenerationContext): String {
        val generator = findGenerator(element::class)
            ?: return generateFallback(element, context)

        return (generator as KerMLGenerator<T>).generate(element, context)
    }

    /**
     * Find the most specific generator for the given element class.
     */
    private fun findGenerator(klass: KClass<*>): KerMLGenerator<*>? {
        // Check exact match first
        generators[klass]?.let { return it }

        // Walk up the type hierarchy
        klass.supertypes.forEach { supertype ->
            val superKlass = supertype.classifier as? KClass<*> ?: return@forEach
            findGenerator(superKlass)?.let { return it }
        }

        return null
    }

    /**
     * Generate a fallback representation for elements without a specific generator.
     *
     * This produces a comment indicating the element type, which helps with debugging
     * and allows the generator to continue rather than failing completely.
     */
    private fun generateFallback(element: Element, context: GenerationContext): String {
        val typeName = element::class.simpleName ?: "Unknown"
        val elementName = element.declaredName ?: element.declaredShortName ?: "unnamed"
        return "${context.currentIndent()}/* TODO: Generator not implemented for $typeName '$elementName' */"
    }

    /**
     * Check if a generator is available for the given element type.
     */
    fun hasGenerator(klass: KClass<*>): Boolean {
        return findGenerator(klass) != null
    }

    /**
     * Register a custom generator for an element type.
     *
     * This allows extension of the factory with additional generators.
     */
    fun <T : Element> register(klass: KClass<T>, generator: KerMLGenerator<T>) {
        // Note: This would require making generators mutable
        // For now, the factory uses a fixed set of generators
        throw UnsupportedOperationException("Dynamic generator registration not yet supported")
    }
}
