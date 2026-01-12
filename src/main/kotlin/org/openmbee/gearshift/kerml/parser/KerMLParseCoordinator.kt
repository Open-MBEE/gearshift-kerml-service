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
package org.openmbee.gearshift.kerml.parser

import org.openmbee.gearshift.GearshiftEngine

/**
 * Coordinates the parsing of KerML grammar elements into Gearshift engine instances.
 * This class serves as the main entry point for parsing KerML files.
 */
class KerMLParseCoordinator(
    private val engine: GearshiftEngine
) {
    /**
     * Tracks all parsed elements for reference resolution.
     */
    private val parsedElements = mutableMapOf<String, String>()  // qualified name -> instance ID

    /**
     * Tracks unresolved references to be resolved after parsing.
     */
    private val unresolvedReferences = mutableListOf<UnresolvedReference>()

    /**
     * Parse a root namespace (typically the entry point for a KerML file).
     *
     * @param rootContext The ANTLR root namespace context
     * @return The root namespace instance ID
     */
    fun parseRootNamespace(rootContext: Any): String? {
        // TODO: When ANTLR parser is generated, this will accept the actual parser context type
        // For now, this is a stub that demonstrates the pattern

        val visitor = KerMLVisitorFactory.NonFeatureElements.namespace
        val instanceId = visitor.visit(rootContext, engine) as? String

        instanceId?.let {
            registerParsedElement("", it)
        }

        return instanceId
    }

    /**
     * Parse a namespace element.
     *
     * @param context The ANTLR namespace context
     * @param parentQualifiedName The qualified name of the parent element
     * @return The namespace instance ID
     */
    fun parseNamespace(context: Any, parentQualifiedName: String = ""): String? {
        val visitor = KerMLVisitorFactory.NonFeatureElements.namespace
        val instanceId = visitor.visit(context, engine) as? String

        instanceId?.let { id ->
            val name = engine.getProperty(id, "name") as? String ?: ""
            val qualifiedName = if (parentQualifiedName.isNotEmpty()) {
                "$parentQualifiedName::$name"
            } else {
                name
            }
            registerParsedElement(qualifiedName, id)
        }

        return instanceId
    }

    /**
     * Parse any member element by delegating to the appropriate visitor.
     *
     * @param context The ANTLR context
     * @param elementType The type of element to parse
     * @param parentQualifiedName The qualified name of the parent
     * @return The element instance ID
     */
    fun parseMemberElement(
        context: Any,
        elementType: String,
        parentQualifiedName: String = ""
    ): String? {
        val visitor = KerMLVisitorFactory.getVisitor(elementType)
            ?: throw IllegalArgumentException("No visitor found for element type: $elementType")

        val instanceId = visitor.visit(context, engine) as? String

        instanceId?.let { id ->
            val name = engine.getProperty(id, "name") as? String ?: ""
            val qualifiedName = if (parentQualifiedName.isNotEmpty() && name.isNotEmpty()) {
                "$parentQualifiedName::$name"
            } else {
                name
            }
            if (qualifiedName.isNotEmpty()) {
                registerParsedElement(qualifiedName, id)
            }
        }

        return instanceId
    }

    /**
     * Register a parsed element for later reference resolution.
     */
    private fun registerParsedElement(qualifiedName: String, instanceId: String) {
        parsedElements[qualifiedName] = instanceId
    }

    /**
     * Record an unresolved reference to be resolved later.
     */
    fun recordUnresolvedReference(
        sourceInstanceId: String,
        propertyName: String,
        targetQualifiedName: String
    ) {
        unresolvedReferences.add(
            UnresolvedReference(
                sourceInstanceId = sourceInstanceId,
                propertyName = propertyName,
                targetQualifiedName = targetQualifiedName
            )
        )
    }

    /**
     * Resolve all unresolved references after parsing is complete.
     *
     * @return List of references that could not be resolved
     */
    fun resolveReferences(): List<UnresolvedReference> {
        val stillUnresolved = mutableListOf<UnresolvedReference>()

        for (ref in unresolvedReferences) {
            val targetInstanceId = parsedElements[ref.targetQualifiedName]

            if (targetInstanceId != null) {
                // Set the reference
                engine.setProperty(ref.sourceInstanceId, ref.propertyName, targetInstanceId)
            } else {
                // Could not resolve - add to unresolved list
                stillUnresolved.add(ref)
            }
        }

        return stillUnresolved
    }

    /**
     * Get statistics about the parsed model.
     */
    fun getParseStatistics(): ParseStatistics {
        return ParseStatistics(
            totalElements = parsedElements.size,
            unresolvedReferences = unresolvedReferences.size,
            elementsByType = engine.getStatistics().objects.typeDistribution
        )
    }

    /**
     * Clear all parsing state (useful for parsing a new file).
     */
    fun reset() {
        parsedElements.clear()
        unresolvedReferences.clear()
    }
}

/**
 * Represents an unresolved reference during parsing.
 */
data class UnresolvedReference(
    val sourceInstanceId: String,
    val propertyName: String,
    val targetQualifiedName: String
)

/**
 * Statistics about a parsing session.
 */
data class ParseStatistics(
    val totalElements: Int,
    val unresolvedReferences: Int,
    val elementsByType: Map<String, Int>
)
