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

import org.openmbee.gearshift.framework.constraints.EngineAccessor
import org.openmbee.gearshift.framework.constraints.ocl.OclExecutor
import org.openmbee.gearshift.framework.constraints.ocl.OclParser

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
        java.io.File("/tmp/ocl-debug.log").appendText("OclEvaluator: parsing '$expression' for ${element.className}\n")
        val ast = try {
            OclParser.parse(expression)
        } catch (e: Exception) {
            java.io.File("/tmp/ocl-debug.log").appendText("PARSE ERROR: ${e.message}\n")
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
        // TODO: Implement global name resolution
        // This needs to search root namespaces for the qualified name
        return null
    }
}
