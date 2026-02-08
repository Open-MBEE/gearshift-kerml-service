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
import org.openmbee.gearshift.generated.interfaces.Function as KerMLFunction
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
        Association::class to AssociationGenerator(),
        AssociationStructure::class to AssociationStructureGenerator(),
        Class::class to ClassGenerator(),
        DataType::class to DataTypeGenerator(),
        Structure::class to StructureGenerator(),

        // Classifiers - Behavioral
        Behavior::class to BehaviorGenerator(),
        KerMLFunction::class to FunctionGenerator(),
        Interaction::class to InteractionGenerator(),
        Predicate::class to PredicateGenerator(),

        // Features
        Feature::class to FeatureGenerator(),
        Step::class to StepGenerator(),

        // Connectors
        BindingConnector::class to BindingConnectorGenerator(),
        Connector::class to ConnectorGenerator(),
        Flow::class to FlowGenerator(),
        Succession::class to SuccessionGenerator(),
        SuccessionFlow::class to SuccessionFlowGenerator(),

        // Expressions
        BooleanExpression::class to BooleanExpressionGenerator(),
        Expression::class to ExpressionContainerGenerator(),
        FeatureReferenceExpression::class to FeatureReferenceExpressionGenerator(),
        InvocationExpression::class to InvocationExpressionGenerator(),
        Invariant::class to InvariantGenerator(),
        NullExpression::class to NullExpressionGenerator(),
        OperatorExpression::class to OperatorExpressionGenerator(),

        // Literals
        LiteralBoolean::class to LiteralBooleanGenerator(),
        LiteralInfinity::class to LiteralInfinityGenerator(),
        LiteralInteger::class to LiteralIntegerGenerator(),
        LiteralRational::class to LiteralRationalGenerator(),
        LiteralString::class to LiteralStringGenerator(),

        // Namespaces
        LibraryPackage::class to LibraryPackageGenerator(),
        Package::class to PackageGenerator(),

        // Imports
        Import::class to ImportGenerator(),
        MembershipImport::class to ImportGenerator(),
        NamespaceImport::class to ImportGenerator(),

        // Annotations
        Comment::class to CommentGenerator(),
        Documentation::class to DocumentationGenerator(),
        MetadataFeature::class to MetadataFeatureGenerator(),
        TextualRepresentation::class to TextualRepresentationGenerator(),
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
     *
     * Uses BFS to ensure the most specific registered type is matched
     * before more general supertypes (e.g., Behavior before Class).
     */
    private fun findGenerator(klass: KClass<*>): KerMLGenerator<*>? {
        // Check exact match first
        generators[klass]?.let { return it }

        // BFS walk up the type hierarchy
        val queue = ArrayDeque<KClass<*>>()
        val visited = mutableSetOf<KClass<*>>()
        visited.add(klass)

        // Seed with direct supertypes
        klass.supertypes.forEach { supertype ->
            val superKlass = supertype.classifier as? KClass<*> ?: return@forEach
            if (visited.add(superKlass)) {
                queue.addLast(superKlass)
            }
        }

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            generators[current]?.let { return it }

            current.supertypes.forEach { supertype ->
                val superKlass = supertype.classifier as? KClass<*> ?: return@forEach
                if (visited.add(superKlass)) {
                    queue.addLast(superKlass)
                }
            }
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
