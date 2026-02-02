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
package org.openmbee.mdm.framework.runtime

import org.openmbee.mdm.framework.constraints.EngineAccessor
import org.openmbee.mdm.framework.constraints.ocl.OclExecutor
import org.openmbee.mdm.framework.constraints.ocl.OclParser
import java.io.File

/**
 * ExpressionEvaluator implementation for OCL expressions.
 *
 * Parses OCL text using OclParser and executes using OclExecutor.
 */
class OclExpressionEvaluator : ExpressionEvaluator {

    override val language = ExpressionLanguage.OCL

    override fun evaluate(
        expression: String,
        element: MDMObject,
        model: MDMEngine,
        args: Map<String, Any?>
    ): Any? {
        File("/tmp/ocl-debug.log").appendText("OclEvaluator: parsing '$expression' for ${element.className}\n")
        val ast = try {
            OclParser.parse(expression)
        } catch (e: Exception) {
            File("/tmp/ocl-debug.log").appendText("PARSE ERROR: ${e.message}\n")
            throw e
        }
        val accessor = MDMEngineAccessor(model)
        val executor = OclExecutor(accessor, element, element.id!!)
        return if (args.isEmpty()) {
            executor.evaluate(ast)
        } else {
            executor.evaluateWith(ast, args)
        }
    }
}

/**
 * Adapter that provides EngineAccessor interface over MDMEngine.
 */
private class MDMEngineAccessor(private val engine: MDMEngine) : EngineAccessor {

    override fun getInstance(id: String): MDMObject? = engine.getInstance(id)

    override fun getLinkedTargets(associationName: String, sourceId: String): List<MDMObject> =
        engine.getLinkedTargets(associationName, sourceId)

    override fun getLinkedSources(associationName: String, targetId: String): List<MDMObject> =
        engine.getLinkedSources(associationName, targetId)

    override fun getProperty(instanceId: String, propertyName: String): Any? =
        engine.getProperty(instanceId, propertyName)

    override fun isSubclassOf(subclass: String, superclass: String): Boolean =
        engine.schema.isSubclassOf(subclass, superclass)

    override fun invokeOperation(instanceId: String, operationName: String, arguments: Map<String, Any?>): Any? =
        engine.invokeOperation(instanceId, operationName, arguments)

    override fun resolveGlobal(qualifiedName: String): MDMObject? {
        // Parse qualified name into segments
        // KerML uses :: as separator (e.g., "Base::Anything")
        val segments = qualifiedName.split("::")
        if (segments.isEmpty()) return null

        File("/tmp/ocl-debug.log").appendText("resolveGlobal: '$qualifiedName' -> segments: $segments\n")

        // Get all root namespaces (includes mounted ones if using MountableEngine)
        val rootNamespaces = engine.getRootNamespaces()
        File("/tmp/ocl-debug.log").appendText("resolveGlobal: found ${rootNamespaces.size} root namespaces\n")

        // Search each root namespace for the qualified name
        for (rootNs in rootNamespaces) {
            val rootName = engine.getProperty(rootNs, "declaredName") as? String
                ?: engine.getProperty(rootNs, "name") as? String
            File("/tmp/ocl-debug.log").appendText("resolveGlobal: checking root namespace '$rootName'\n")

            // Check if first segment matches this root namespace
            if (rootName == segments[0]) {
                // Found matching root, resolve remaining segments
                val result = if (segments.size == 1) {
                    rootNs
                } else {
                    resolveInNamespace(rootNs, segments.drop(1))
                }
                if (result != null) {
                    File("/tmp/ocl-debug.log").appendText("resolveGlobal: FOUND ${result.className} id=${result.id}\n")
                    return result
                }
            }
        }

        File("/tmp/ocl-debug.log").appendText("resolveGlobal: NOT FOUND\n")
        return null
    }

    /**
     * Resolve remaining path segments within a namespace.
     */
    private fun resolveInNamespace(namespace: MDMObject, segments: List<String>): MDMObject? {
        if (segments.isEmpty()) return namespace

        val targetName = segments[0]

        // Get owned members of this namespace
        // Navigation via ownedMembership -> memberElement
        val ownedMemberships = engine.getProperty(namespace, "ownedMembership")
        val memberships = when (ownedMemberships) {
            is List<*> -> ownedMemberships.filterIsInstance<MDMObject>()
            is MDMObject -> listOf(ownedMemberships)
            else -> emptyList()
        }

        for (membership in memberships) {
            val memberElement = engine.getProperty(membership, "memberElement") as? MDMObject
            if (memberElement != null) {
                val memberName = engine.getProperty(memberElement, "declaredName") as? String
                    ?: engine.getProperty(memberElement, "name") as? String

                if (memberName == targetName) {
                    // Found matching member
                    return if (segments.size == 1) {
                        memberElement
                    } else {
                        // Continue resolving in this member
                        resolveInNamespace(memberElement, segments.drop(1))
                    }
                }
            }

            // Also check memberName on the membership itself
            val membershipName = engine.getProperty(membership, "memberName") as? String
            if (membershipName == targetName) {
                val memberElement2 = engine.getProperty(membership, "memberElement") as? MDMObject
                if (memberElement2 != null) {
                    return if (segments.size == 1) {
                        memberElement2
                    } else {
                        resolveInNamespace(memberElement2, segments.drop(1))
                    }
                }
            }
        }

        return null
    }
}
