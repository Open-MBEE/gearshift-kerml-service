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

import org.openmbee.gearshift.generated.KerMLElementFactory
import org.openmbee.gearshift.generated.interfaces.Element
import org.openmbee.gearshift.generated.interfaces.ModelElement
import org.openmbee.gearshift.kerml.parser.visitors.base.ReferenceCollector
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MDMObject
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.util.UUID
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

/**
 * Context passed to visitors during parsing.
 * Contains the engine, parent element, namespace tracking, and reference collector.
 */
data class KermlParseContext(
    val engine: MDMEngine,
    val factory: KerMLElementFactory,
    val parent: ModelElement? = null,
    val parentQualifiedName: String = "",
    val namespaceStack: ArrayDeque<String> = ArrayDeque(),
    val referenceCollector: ReferenceCollector? = null,
    val memberVisibility: String = "public",
    val isLibraryContext: Boolean = false
) {
    companion object {
        /**
         * Namespace UUID for generating deterministic element IDs.
         * Uses the KerML namespace as a base for UUID v5 generation.
         */
        private val KERML_NAMESPACE_UUID = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8")

        /**
         * Generate a deterministic UUID v5 from a qualified name.
         * This ensures library elements always have the same elementId.
         */
        fun generateDeterministicId(qualifiedName: String): String {
            val md = MessageDigest.getInstance("SHA-1")
            md.update(uuidToBytes(KERML_NAMESPACE_UUID))
            md.update(qualifiedName.toByteArray(Charsets.UTF_8))
            val hash = md.digest()

            // Set version to 5 (SHA-1 based)
            hash[6] = ((hash[6].toInt() and 0x0F) or 0x50).toByte()
            // Set variant to RFC 4122
            hash[8] = ((hash[8].toInt() and 0x3F) or 0x80).toByte()

            val buffer = ByteBuffer.wrap(hash)
            val mostSig = buffer.getLong()
            val leastSig = buffer.getLong()
            return UUID(mostSig, leastSig).toString()
        }

        private fun uuidToBytes(uuid: UUID): ByteArray {
            val buffer = ByteBuffer.allocate(16)
            buffer.putLong(uuid.mostSignificantBits)
            buffer.putLong(uuid.leastSignificantBits)
            return buffer.array()
        }
    }

    /**
     * Create a child context with a new parent element.
     */
    fun withParent(newParent: ModelElement, name: String = ""): KermlParseContext {
        val newStack = ArrayDeque(namespaceStack)
        if (name.isNotEmpty()) {
            newStack.addLast(name)
        }
        val newQualifiedName = if (parentQualifiedName.isEmpty()) name
        else if (name.isEmpty()) parentQualifiedName
        else "$parentQualifiedName::$name"
        return copy(
            parent = newParent,
            parentQualifiedName = newQualifiedName,
            namespaceStack = newStack
        )
    }

    /**
     * Create a new instance using the secondary constructor which handles ownership automatically.
     * The secondary constructor takes (engine, parent, declaredName, etc.) and uses OwnershipResolver
     * to set up the correct intermediate membership.
     *
     * For library elements (when isLibraryContext is true), generates a deterministic elementId
     * based on the qualified name. This ensures library elements have stable IDs across sessions.
     *
     * @param declaredName Optional name for the element
     * @param declaredShortName Optional short name for the element
     */
    inline fun <reified T : ModelElement> create(
        declaredName: String? = null,
        declaredShortName: String? = null
    ): T {
        val typeName = T::class.simpleName ?: throw IllegalArgumentException("Cannot get simpleName for type")
        val implClassName = "org.openmbee.gearshift.generated.impl.${typeName}Impl"

        // Debug: Check if metaclass exists in schema
        val metaClass = engine.schema.getClass(typeName)
        if (metaClass == null) {
            throw IllegalStateException("MetaClass '$typeName' not found in schema. Available classes: ${engine.schema.getAllClasses().map { it.name }.sorted().take(10)}...")
        }

        @Suppress("UNCHECKED_CAST")
        val implClass = Class.forName(implClassName).kotlin as KClass<T>

        // Find the secondary constructor (the one that takes MDMEngine as first param)
        val constructor = implClass.constructors.find { ctor ->
            val params = ctor.parameters
            params.isNotEmpty() && params[0].type.classifier == MDMEngine::class
        } ?: throw IllegalArgumentException("No suitable constructor found for ${typeName}")

        // Generate elementId - deterministic for library elements, random for user elements
        val elementId = if (isLibraryContext && declaredName != null) {
            // For named library elements, use qualified name to generate deterministic ID
            val qualifiedName = if (parentQualifiedName.isEmpty()) declaredName
                               else "$parentQualifiedName::$declaredName"
            generateDeterministicId(qualifiedName)
        } else if (isLibraryContext) {
            // For unnamed library elements (relationships, etc.), include type and parent context
            val contextKey = "$parentQualifiedName#$typeName#${UUID.randomUUID()}"
            generateDeterministicId(contextKey)
        } else {
            // For user elements, use random UUID
            UUID.randomUUID().toString()
        }

        // Build argument map for callBy() which handles defaults
        val args = mutableMapOf<KParameter, Any?>()
        for (param in constructor.parameters) {
            when (param.name) {
                "engine" -> args[param] = engine
                "parent" -> args[param] = parent as? Element
                "declaredName" -> if (declaredName != null) args[param] = declaredName
                "declaredShortName" -> if (declaredShortName != null) args[param] = declaredShortName
                "elementId" -> args[param] = elementId
                // Let other params use their defaults
            }
        }

        try {
            val element = constructor.callBy(args) as MDMObject
            engine.registerElement(element)

            @Suppress("UNCHECKED_CAST")
            return element as T
        } catch (e: java.lang.reflect.InvocationTargetException) {
            val cause = e.cause
            throw IllegalStateException("Failed to construct $typeName: ${cause?.message}", cause)
        }
    }

    /**
     * Get the parent element ID for reference resolution context.
     */
    fun getParentElementId(): String? {
        return (parent as? Element)?.elementId
    }

    /**
     * Create a copy of this context with library context enabled.
     * Library elements get deterministic elementIds based on their qualified names.
     */
    fun asLibraryContext(): KermlParseContext {
        return copy(isLibraryContext = true)
    }
}
