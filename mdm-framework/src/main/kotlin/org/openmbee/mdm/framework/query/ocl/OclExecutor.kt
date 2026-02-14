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
package org.openmbee.mdm.framework.query.ocl

import org.openmbee.mdm.framework.constraints.EngineAccessor
import org.openmbee.mdm.framework.runtime.MDMObject
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.round

/**
 * Wrapper for an MDMObject that's been cast via oclAsType().
 * When operations are invoked on this wrapper, they dispatch based on
 * the viewType instead of the object's actual runtime type.
 *
 * This enables patterns like `self.oclAsType(ParentClass).someOperation()`
 * to call the parent class's implementation of someOperation.
 */
data class OclAsTypeView(
    val obj: MDMObject,
    val viewType: String
)

/**
 * Executes OCL expressions against MDM model instances.
 *
 * This executor evaluates OCL AST nodes in the context of:
 * - A "self" object (the context instance)
 * - Variable bindings (for let, iterator variables, etc.)
 * - Access to the MDM engine for property/navigation/type queries
 *
 * @param engineAccessor Access to the MDM engine for property/association queries
 * @param contextObject The "self" object for this evaluation
 */
class OclExecutor(
    private val engineAccessor: EngineAccessor,
    private val contextObject: MDMObject,
    private val contextId: String
) : OclVisitor<Any?> {

    /** Variable bindings (self, let variables, iterator variables) */
    private val variables: MutableMap<String, Any?> = mutableMapOf(
        "self" to contextObject,
        "_it" to contextObject  // Default iterator variable equals self in non-iterator contexts
    )

    /**
     * Evaluate an OCL expression.
     */
    fun evaluate(expression: OclExpression): Any? = expression.accept(this)

    /**
     * Evaluate with additional variable bindings.
     */
    fun evaluateWith(expression: OclExpression, bindings: Map<String, Any?>): Any? {
        val savedBindings = variables.toMap()
        variables.putAll(bindings)
        try {
            return expression.accept(this)
        } finally {
            variables.clear()
            variables.putAll(savedBindings)
        }
    }

    // ===== Literals =====

    override fun visitNullLiteral(exp: NullLiteralExp): Any? = null
    override fun visitBooleanLiteral(exp: BooleanLiteralExp): Boolean = exp.value
    override fun visitIntegerLiteral(exp: IntegerLiteralExp): Long = exp.value
    override fun visitRealLiteral(exp: RealLiteralExp): Double = exp.value
    override fun visitStringLiteral(exp: StringLiteralExp): String = exp.value
    override fun visitUnlimitedNaturalLiteral(exp: UnlimitedNaturalLiteralExp): Long = Long.MAX_VALUE

    override fun visitCollectionLiteral(exp: CollectionLiteralExp): Collection<Any?> {
        val elements = exp.parts.map { it.accept(this) }
        return when (exp.kind) {
            CollectionKind.SET -> elements.toSet()
            CollectionKind.ORDERED_SET -> elements.toCollection(LinkedHashSet())
            CollectionKind.SEQUENCE -> elements
            CollectionKind.BAG -> elements
        }
    }

    // ===== Variables and Properties =====

    override fun visitVariable(exp: VariableExp): Any? {
        // Check for enum literal (Type::value syntax)
        if (exp.name.contains("::")) {
            return resolveEnumLiteral(exp.name)
        }

        // First check for explicit variable bindings
        if (variables.containsKey(exp.name)) {
            return variables[exp.name]
        }

        // Fall back to property access on self (OCL allows bare property names)
        val self = variables["self"] as? MDMObject
        if (self != null) {
            // Try direct property access
            val value = self.getProperty(exp.name)
            if (value != null) return value

            val selfId = findObjectId(self)
            if (selfId != null) {
                // Try via engine for derived properties
                try {
                    val derivedValue = engineAccessor.getProperty(selfId, exp.name)
                    if (derivedValue != null) return derivedValue
                } catch (_: Exception) {
                    // Property not found via engine
                }

                // Try navigation (association traversal) - returns single element or null for [0..1]
                try {
                    val linkedTargets = engineAccessor.getLinkedTargets(exp.name, selfId)
                    if (linkedTargets.isNotEmpty()) {
                        // For single-valued associations, return the single element
                        // For multi-valued, return the collection
                        return if (linkedTargets.size == 1) linkedTargets.first() else linkedTargets
                    }
                } catch (_: Exception) {
                    // Association doesn't exist, continue to return null
                }
            }
        }

        // Variable/property/navigation not found - return null rather than throwing
        // This allows null checks like "name <> null" to work
        return null
    }

    /**
     * Resolve an OCL enum literal (e.g., "VisibilityKind::private") to its value.
     * Returns the lowercase value name, which matches how KerML enums are stored via @JsonValue.
     */
    private fun resolveEnumLiteral(literal: String): String {
        val parts = literal.split("::")
        if (parts.size != 2) {
            throw OclEvaluationException("Invalid enum literal syntax: $literal")
        }
        // Strip leading underscore (KerML uses _in to escape reserved words) and lowercase
        return parts[1].removePrefix("_").lowercase()
    }

    override fun visitPropertyCall(exp: PropertyCallExp): Any? {
        val source = exp.source.accept(this)
        return when (source) {
            is OclAsTypeView -> {
                // Handle property access on a type-casted object
                // Use the view type for property lookup (derivation constraints, etc.)
                val sourceId = findObjectId(source.obj)
                if (sourceId != null) {
                    engineAccessor.getPropertyAs(sourceId, exp.propertyName, source.viewType)
                } else {
                    // Fallback to direct property access
                    source.obj.getProperty(exp.propertyName)
                }
            }

            is MDMObject -> {
                // First try direct property access
                val value = source.getProperty(exp.propertyName)
                if (value != null) return value

                // Then try via engine (handles derived properties)
                val sourceId = findObjectId(source)
                if (sourceId != null) {
                    engineAccessor.getProperty(sourceId, exp.propertyName)
                } else {
                    null
                }
            }

            is Map<*, *> -> source[exp.propertyName]

            // OCL implicit collect: collection.property means collection->collect(property)
            is Collection<*> -> {
                source.flatMap { element ->
                    when (element) {
                        is OclAsTypeView -> {
                            val objectId = findObjectId(element.obj)
                            val value = if (objectId != null) {
                                engineAccessor.getPropertyAs(objectId, exp.propertyName, element.viewType)
                            } else {
                                element.obj.getProperty(exp.propertyName)
                            }
                            when (value) {
                                is Collection<*> -> value.filterNotNull()
                                null -> emptyList()
                                else -> listOf(value)
                            }
                        }

                        is MDMObject -> {
                            var value = element.getProperty(exp.propertyName)
                            val objectId = findObjectId(element)
                            if (value == null && objectId != null) {
                                // Try derived property via engine
                                value = engineAccessor.getProperty(objectId, exp.propertyName)
                            }
                            if (value == null && objectId != null) {
                                // Try association navigation (including reverse navigation)
                                val linkedTargets = engineAccessor.getLinkedTargets(exp.propertyName, objectId)
                                if (linkedTargets.isNotEmpty()) {
                                    value = linkedTargets
                                }
                            }
                            when (value) {
                                is Collection<*> -> value.filterNotNull()
                                null -> emptyList()
                                else -> listOf(value)
                            }
                        }

                        is Map<*, *> -> {
                            val value = element[exp.propertyName]
                            if (value != null) listOf(value) else emptyList()
                        }

                        else -> emptyList<Any>()
                    }
                }
            }

            null -> null

            else -> throw OclEvaluationException(
                "Cannot access property '${exp.propertyName}' on ${source.javaClass.simpleName}"
            )
        }
    }

    override fun visitNavigationCall(exp: NavigationCallExp): Any? {
        val source = exp.source.accept(this)
        return when (source) {
            is OclAsTypeView -> {
                // Handle navigation on a type-casted object
                // Use the view type for association end lookup
                val sourceId = findObjectId(source.obj)
                    ?: throw OclEvaluationException("Cannot navigate from unregistered object")

                // For now, association navigation uses the same mechanism
                // Future: could add getLinkedTargetsAs for view-type-specific navigation
                engineAccessor.getLinkedTargets(exp.navigationName, sourceId)
            }

            is MDMObject -> {
                val sourceId = findObjectId(source)
                    ?: throw OclEvaluationException("Cannot navigate from unregistered object")

                // Try to find association by navigation name
                // The navigation name could be an association end name
                engineAccessor.getLinkedTargets(exp.navigationName, sourceId)
            }

            // OCL implicit collect: collection.navigation means collection->collect(navigation)
            is Collection<*> -> {
                source.flatMap { element ->
                    when (element) {
                        is OclAsTypeView -> {
                            val elementId = findObjectId(element.obj)
                            if (elementId != null) {
                                engineAccessor.getLinkedTargets(exp.navigationName, elementId)
                            } else {
                                emptyList()
                            }
                        }

                        is MDMObject -> {
                            val elementId = findObjectId(element)
                            if (elementId != null) {
                                engineAccessor.getLinkedTargets(exp.navigationName, elementId)
                            } else {
                                emptyList()
                            }
                        }

                        else -> emptyList<MDMObject>()
                    }
                }
            }

            else -> throw OclEvaluationException(
                "Cannot navigate from ${source?.javaClass?.simpleName ?: "null"}"
            )
        }
    }

    // ===== Operations =====

    override fun visitOperationCall(exp: OperationCallExp): Any? {
        // Handle Type.allInstances() — source is a VariableExp with the type name
        if (exp.operationName == "allInstances" && exp.source is VariableExp) {
            val typeName = (exp.source as VariableExp).name
            return engineAccessor.getElementsByClass(typeName)
        }

        val source = exp.source?.accept(this)
        val args = exp.arguments.map { it.accept(this) }

        return evaluateOperation(source, exp.operationName, args)
    }

    override fun visitArrowCall(exp: ArrowCallExp): Any? {
        val source = exp.source.accept(this)

        // Handle object-level operations before converting to collection
        when (exp.operationName) {
            "oclType" -> {
                // Return the type (class name) of the object
                return when (source) {
                    is MDMObject -> source.className
                    is Collection<*> -> source.firstOrNull()?.let {
                        if (it is MDMObject) it.className else it?.javaClass?.simpleName
                    }

                    null -> null
                    else -> source.javaClass.simpleName
                }
            }
        }

        val collection = asCollection(source)
        return evaluateCollectionOperation(collection, exp.operationName, exp.arguments)
    }

    // ===== Iterators =====

    override fun visitIterator(exp: IteratorExp): Any? {
        val source = exp.source.accept(this)
        val collection = asCollection(source)

        return when (exp.iteratorName) {
            "select" -> collection.filter { element ->
                evaluateIteratorBody(exp.iteratorVariable, element, exp.body) == true
            }

            "reject" -> collection.filter { element ->
                evaluateIteratorBody(exp.iteratorVariable, element, exp.body) != true
            }

            "collect" -> collection.map { element ->
                evaluateIteratorBody(exp.iteratorVariable, element, exp.body)
            }

            "forAll" -> collection.all { element ->
                evaluateIteratorBody(exp.iteratorVariable, element, exp.body) == true
            }

            "exists" -> collection.any { element ->
                evaluateIteratorBody(exp.iteratorVariable, element, exp.body) == true
            }

            "one" -> collection.count { element ->
                evaluateIteratorBody(exp.iteratorVariable, element, exp.body) == true
            } == 1

            "any" -> collection.firstOrNull { element ->
                evaluateIteratorBody(exp.iteratorVariable, element, exp.body) == true
            }

            "isUnique" -> {
                val results = collection.map { element ->
                    evaluateIteratorBody(exp.iteratorVariable, element, exp.body)
                }
                results.size == results.toSet().size
            }

            "sortedBy" -> {
                @Suppress("UNCHECKED_CAST")
                collection.sortedBy { element ->
                    evaluateIteratorBody(exp.iteratorVariable, element, exp.body) as? Comparable<Any>
                }
            }

            "closure" -> {
                // Transitive closure: collect all elements reachable by repeatedly
                // applying the body expression until no new elements are found
                computeClosure(collection, exp.iteratorVariable, exp.body)
            }

            "exists1" -> {
                // Exactly one element satisfies the condition
                collection.count { element ->
                    evaluateIteratorBody(exp.iteratorVariable, element, exp.body) == true
                } == 1
            }

            "selectByKind" -> {
                // Special case: selectByKind(Type) where Type is a type name
                val typeName = when (val arg = exp.body) {
                    is VariableExp -> arg.name
                    else -> throw OclEvaluationException("selectByKind requires a type name")
                }
                val mdmObjects = collection.filterIsInstance<MDMObject>()
                mdmObjects.filter { obj ->
                    isKindOf(obj, typeName)
                }
            }

            "selectAsKind" -> {
                // Like selectByKind but conceptually casts results to the type
                // For MDMObjects, the filtering is the same
                val typeName = when (val arg = exp.body) {
                    is VariableExp -> arg.name
                    else -> throw OclEvaluationException("selectAsKind requires a type name")
                }
                collection.filterIsInstance<MDMObject>().filter { obj ->
                    isKindOf(obj, typeName)
                }
            }

            "selectByType" -> {
                val typeName = when (val arg = exp.body) {
                    is VariableExp -> arg.name
                    else -> throw OclEvaluationException("selectByType requires a type name")
                }
                collection.filterIsInstance<MDMObject>().filter { obj ->
                    obj.className == typeName
                }
            }

            else -> throw OclEvaluationException("Unknown iterator: ${exp.iteratorName}")
        }
    }

    override fun visitIterate(exp: IterateExp): Any? {
        val source = exp.source.accept(this)
        val collection = asCollection(source)

        var accumulator = exp.accumulatorInit.accept(this)

        for (element in collection) {
            val savedBindings = variables.toMap()
            variables[exp.iteratorVariable] = element
            variables[exp.accumulatorVariable] = accumulator
            try {
                accumulator = exp.body.accept(this)
            } finally {
                variables.clear()
                variables.putAll(savedBindings)
            }
        }

        return accumulator
    }

    // ===== Control Flow =====

    override fun visitIf(exp: IfExp): Any? {
        val condition = exp.condition.accept(this)
        return if (condition == true) {
            exp.thenExpression.accept(this)
        } else {
            exp.elseExpression.accept(this)
        }
    }

    override fun visitLet(exp: LetExp): Any? {
        val value = exp.variableValue.accept(this)
        val savedBindings = variables.toMap()
        variables[exp.variableName] = value
        try {
            return exp.body.accept(this)
        } finally {
            variables.clear()
            variables.putAll(savedBindings)
        }
    }

    // ===== Type Operations =====

    override fun visitTypeOp(exp: TypeExp): Any? {
        val source = exp.source.accept(this)

        return when (exp.operationName) {
            "oclIsKindOf" -> {
                when (source) {
                    is MDMObject -> isKindOf(source, exp.typeName)
                    else -> false
                }
            }

            "oclIsTypeOf" -> {
                when (source) {
                    is MDMObject -> source.className == exp.typeName
                    else -> false
                }
            }

            "oclAsType" -> {
                // Type cast - wrap the object to track the view type for method dispatch.
                // This enables self.oclAsType(ParentClass).method() to call the parent's method.
                when (source) {
                    is MDMObject -> OclAsTypeView(source, exp.typeName)
                    is OclAsTypeView -> OclAsTypeView(source.obj, exp.typeName)
                    else -> source
                }
            }

            "selectByKind" -> {
                // Filter collection to only include elements that are instances of typeName (including subtypes)
                val collection = asCollection(source)
                collection.filter { element ->
                    when (element) {
                        is MDMObject -> isKindOf(element, exp.typeName)
                        else -> false
                    }
                }
            }

            "selectByType" -> {
                // Filter collection to only include elements that are exactly typeName
                val collection = asCollection(source)
                collection.filter { element ->
                    when (element) {
                        is MDMObject -> element.className == exp.typeName
                        else -> false
                    }
                }
            }

            else -> throw OclEvaluationException("Unknown type operation: ${exp.operationName}")
        }
    }

    override fun visitDynamicTypeOp(exp: DynamicTypeExp): Any? {
        val source = exp.source.accept(this)
        val typeValue = exp.typeExpression.accept(this)

        // The type expression should evaluate to a type name (String) or an MDMObject whose className is the type
        val typeName = when (typeValue) {
            is String -> typeValue
            is MDMObject -> typeValue.className
            else -> throw OclEvaluationException(
                "Dynamic type expression must evaluate to a type name, got: ${typeValue?.let { it::class.simpleName }}"
            )
        }

        return when (exp.operationName) {
            "oclIsKindOf" -> {
                when (source) {
                    is MDMObject -> isKindOf(source, typeName)
                    else -> false
                }
            }

            "oclIsTypeOf" -> {
                when (source) {
                    is MDMObject -> source.className == typeName
                    else -> false
                }
            }

            "oclAsType" -> {
                when (source) {
                    is MDMObject -> OclAsTypeView(source, typeName)
                    is OclAsTypeView -> OclAsTypeView(source.obj, typeName)
                    else -> source
                }
            }

            else -> throw OclEvaluationException("Unknown dynamic type operation: ${exp.operationName}")
        }
    }

    // ===== Binary/Unary Operations =====

    override fun visitInfix(exp: InfixExp): Any? {
        // Short-circuit evaluation for boolean operators
        if (exp.operator == "and") {
            val left = exp.left.accept(this)
            if (left != true) return false
            return exp.right.accept(this) == true
        }
        if (exp.operator == "or") {
            val left = exp.left.accept(this)
            if (left == true) return true
            return exp.right.accept(this) == true
        }
        if (exp.operator == "implies") {
            val left = exp.left.accept(this)
            if (left != true) return true  // false implies anything is true
            return exp.right.accept(this) == true
        }

        val left = exp.left.accept(this)
        val right = exp.right.accept(this)

        return when (exp.operator) {
            // Comparison
            "=" -> left == right
            "<>" -> left != right
            "<" -> compareValues(left, right) < 0
            ">" -> compareValues(left, right) > 0
            "<=" -> compareValues(left, right) <= 0
            ">=" -> compareValues(left, right) >= 0

            // Arithmetic
            "+" -> addValues(left, right)
            "-" -> subtractValues(left, right)
            "*" -> multiplyValues(left, right)
            "/" -> divideValues(left, right)
            "div" -> intDivide(left, right)
            "mod" -> modulo(left, right)

            // Boolean (non-short-circuit already handled above)
            "xor" -> (left == true) xor (right == true)

            // String
            "concat" -> (left?.toString() ?: "") + (right?.toString() ?: "")

            else -> throw OclEvaluationException("Unknown operator: ${exp.operator}")
        }
    }

    override fun visitPrefix(exp: PrefixExp): Any? {
        val operand = exp.operand.accept(this)

        return when (exp.operator) {
            "not" -> operand != true
            "-" -> when (operand) {
                is Long -> -operand
                is Int -> -operand
                is Double -> -operand
                is Float -> -operand
                else -> throw OclEvaluationException("Cannot negate: $operand")
            }

            else -> throw OclEvaluationException("Unknown prefix operator: ${exp.operator}")
        }
    }

    // ===== Helper Methods =====

    private fun evaluateIteratorBody(varName: String, element: Any?, body: OclExpression): Any? {
        val savedBindings = variables.toMap()
        variables[varName] = element
        try {
            return body.accept(this)
        } finally {
            variables.clear()
            variables.putAll(savedBindings)
        }
    }

    /**
     * Compute the transitive closure of a collection.
     *
     * Starting with the initial collection, repeatedly evaluates the body expression
     * for each element to get related elements, continuing until no new elements are found.
     */
    private fun computeClosure(
        initial: List<Any?>,
        iteratorVar: String,
        body: OclExpression
    ): Set<Any?> {
        val result = LinkedHashSet<Any?>()
        val workQueue = ArrayDeque<Any?>()

        for (element in initial) {
            if (element != null && element !in result) {
                result.add(element)
                workQueue.add(element)
            }
        }

        while (workQueue.isNotEmpty()) {
            val current = workQueue.removeFirst()
            val related = evaluateIteratorBody(iteratorVar, current, body)
            val relatedCollection = asCollection(related)

            for (newElement in relatedCollection) {
                if (newElement != null && newElement !in result) {
                    result.add(newElement)
                    workQueue.add(newElement)
                }
            }
        }

        return result
    }

    private fun asCollection(value: Any?): List<Any?> = when (value) {
        null -> emptyList()
        is Collection<*> -> value.toList()
        else -> listOf(value)
    }

    private fun findObjectId(obj: MDMObject): String? {
        // The object should have its ID set when stored
        return obj.id ?: contextId.takeIf { contextObject === obj }
    }

    private fun isKindOf(obj: MDMObject, typeName: String): Boolean {
        return obj.className == typeName ||
                engineAccessor.isSubclassOf(obj.className, typeName)
    }

    private fun buildNamedArgs(className: String, opName: String, args: List<Any?>): Map<String, Any?> {
        if (args.isEmpty()) return emptyMap()
        val paramNames = engineAccessor.getOperationParameterNames(className, opName)
        return if (paramNames.size >= args.size) {
            paramNames.zip(args).toMap()
        } else {
            // Fallback for operations without metadata: use positional naming
            args.mapIndexed { i, v -> "arg$i" to v }.toMap()
        }
    }

    private fun evaluateOperation(source: Any?, opName: String, args: List<Any?>): Any? {
        // Object operations
        when (opName) {
            "oclIsUndefined" -> return source == null
            "oclIsInvalid" -> return false
            "toString" -> return source?.toString() ?: "null"
            "oclType" -> return when (source) {
                is OclAsTypeView -> source.obj.className  // Return actual type, not view type
                is MDMObject -> source.className
                null -> null
                else -> source.javaClass.simpleName
            }

            "oclIsKindOf" -> {
                val typeName = args.firstOrNull()?.toString()
                    ?: throw OclEvaluationException("oclIsKindOf requires a type name argument")
                return when (source) {
                    is OclAsTypeView -> isKindOf(source.obj, typeName)
                    is MDMObject -> isKindOf(source, typeName)
                    null -> false
                    else -> false
                }
            }

            "oclIsTypeOf" -> {
                val typeName = args.firstOrNull()?.toString()
                    ?: throw OclEvaluationException("oclIsTypeOf requires a type name argument")
                return when (source) {
                    is OclAsTypeView -> source.obj.className == typeName  // Check actual type
                    is MDMObject -> source.className == typeName
                    null -> false
                    else -> source.javaClass.simpleName == typeName
                }
            }
        }

        // Handle OclAsTypeView for operation invocation - dispatch based on view type
        if (source is OclAsTypeView) {
            val targetId = findObjectId(source.obj)
            if (targetId != null) {
                val namedArgs = buildNamedArgs(source.viewType, opName, args)
                try {
                    return engineAccessor.invokeOperationAs(targetId, opName, source.viewType, namedArgs)
                } catch (e: Exception) {
                    throw OclEvaluationException(
                        "Error invoking operation '$opName' as ${source.viewType}: ${e.message}",
                        e
                    )
                }
            } else {
                throw OclEvaluationException("Cannot invoke operation '$opName': object has no ID (${source.obj.className})")
            }
        }

        // String operations
        if (source is String) {
            return when (opName) {
                "size" -> source.length.toLong()
                "concat" -> source + (args.firstOrNull()?.toString() ?: "")
                "substring" -> {
                    val lower = (args[0] as Number).toInt()
                    val upper = (args[1] as Number).toInt()
                    source.substring(lower - 1, upper)
                }

                "toInteger" -> source.toLong()
                "toReal" -> source.toDouble()
                "toUpperCase" -> source.uppercase()
                "toLowerCase" -> source.lowercase()
                "indexOf" -> {
                    val idx = source.indexOf(args[0].toString())
                    if (idx >= 0) (idx + 1).toLong() else 0L
                }

                "matches" -> {
                    val pattern = args[0].toString()
                    source.matches(Regex(pattern))
                }

                "isBasicName" -> {
                    // KerML basic name: starts with letter or underscore, followed by letters, digits, or underscores
                    source.matches(Regex("^[a-zA-Z_][a-zA-Z0-9_]*$"))
                }

                "asRestrictedName" -> {
                    // KerML restricted name: wrapped in single quotes
                    "'$source'"
                }

                else -> throw OclEvaluationException("Unknown string operation: $opName")
            }
        }

        // Number operations
        if (source is Number) {
            return when (opName) {
                "abs" -> when (source) {
                    is Long -> abs(source)
                    is Int -> abs(source)
                    is Double -> abs(source)
                    is Float -> abs(source)
                    else -> throw OclEvaluationException("Cannot abs: $source")
                }

                "floor" -> when (source) {
                    is Double -> floor(source).toLong()
                    is Float -> floor(source.toDouble()).toLong()
                    else -> source.toLong()
                }

                "round" -> when (source) {
                    is Double -> round(source).toLong()
                    is Float -> round(source).toLong()
                    else -> source.toLong()
                }

                "max" -> {
                    val other = args[0] as Number
                    maxOf(source.toDouble(), other.toDouble())
                }

                "min" -> {
                    val other = args[0] as Number
                    minOf(source.toDouble(), other.toDouble())
                }

                else -> throw OclEvaluationException("Unknown number operation: $opName")
            }
        }

        // OCL implicit collect: collection.operation(args) means collection->collect(e | e.operation(args))
        if (source is Collection<*>) {
            return source.flatMap { element ->
                val result = evaluateOperation(element, opName, args)
                when (result) {
                    is Collection<*> -> result.toList()
                    null -> emptyList()
                    else -> listOf(result)
                }
            }
        }

        // Try invoking user-defined operation on MDMObject
        val targetObject = when (source) {
            null -> contextObject  // Operation on self
            is MDMObject -> source
            else -> null
        }

        if (targetObject != null) {
            val targetId = findObjectId(targetObject)
            if (targetId != null) {
                val namedArgs = buildNamedArgs(targetObject.className, opName, args)
                try {
                    return engineAccessor.invokeOperation(targetId, opName, namedArgs)
                } catch (e: Exception) {
                    // Rethrow as OclEvaluationException with cause
                    throw OclEvaluationException("Error invoking operation '$opName': ${e.message}", e)
                }
            } else {
                throw OclEvaluationException("Cannot invoke operation '$opName': object has no ID (${targetObject.className})")
            }
        }

        throw OclEvaluationException("Cannot invoke operation '$opName' on ${source?.javaClass?.simpleName ?: "null"}")
    }

    private fun evaluateCollectionOperation(
        collection: List<Any?>,
        opName: String,
        argExpressions: List<OclExpression>
    ): Any? {
        val args = argExpressions.map { it.accept(this) }

        return when (opName) {
            // Query operations
            "size" -> collection.size.toLong()
            "isEmpty" -> collection.isEmpty()
            "notEmpty" -> collection.isNotEmpty()
            "includes" -> collection.contains(args[0])
            "excludes" -> !collection.contains(args[0])
            "count" -> collection.count { it == args[0] }.toLong()
            "includesAll" -> collection.containsAll(asCollection(args[0]))
            "excludesAll" -> collection.none { it in asCollection(args[0]) }

            // Element access
            "first" -> collection.firstOrNull()
            "last" -> collection.lastOrNull()
            "at" -> collection.getOrNull((args[0] as Number).toInt() - 1)
            "indexOf" -> {
                val idx = collection.indexOf(args[0])
                if (idx >= 0) (idx + 1).toLong() else 0L
            }

            // Set operations
            "union" -> OclStandardLibrary.union(collection, asCollection(args[0]))
            "intersection" -> OclStandardLibrary.intersection(collection, asCollection(args[0]))
            "-" -> OclStandardLibrary.minus(collection, asCollection(args[0]))
            "symmetricDifference" -> OclStandardLibrary.symmetricDifference(collection, asCollection(args[0]))
            "including" -> collection + args[0]
            "excluding" -> collection.filter { it != args[0] }

            // Sequence operations
            "append" -> collection + args[0]
            "prepend" -> listOf(args[0]) + collection
            "insertAt" -> {
                val mutable = collection.toMutableList()
                mutable.add((args[0] as Number).toInt() - 1, args[1])
                mutable
            }

            "subSequence" -> collection.subList(
                (args[0] as Number).toInt() - 1,
                (args[1] as Number).toInt()
            )

            "reverse" -> collection.reversed()

            // Conversion
            "asSet" -> collection.toSet()
            "asSequence" -> collection.toList()
            "asBag" -> collection.toList()
            "asOrderedSet" -> collection.toCollection(LinkedHashSet())

            // Flattening
            "flatten" -> OclStandardLibrary.flatten<Any?>(collection)

            // Aggregation
            "sum" -> OclStandardLibrary.sum(collection)

            else -> throw OclEvaluationException("Unknown collection operation: $opName")
        }
    }

    private fun compareValues(left: Any?, right: Any?): Int {
        if (left == null && right == null) return 0
        if (left == null) return -1
        if (right == null) return 1

        @Suppress("UNCHECKED_CAST")
        return when {
            left is Number && right is Number -> left.toDouble().compareTo(right.toDouble())
            left is String && right is String -> left.compareTo(right)
            left is Comparable<*> && right is Comparable<*> ->
                (left as Comparable<Any>).compareTo(right)

            else -> throw OclEvaluationException("Cannot compare: $left and $right")
        }
    }

    private fun addValues(left: Any?, right: Any?): Any? {
        return when {
            left is String || right is String -> (left?.toString() ?: "") + (right?.toString() ?: "")
            left is Double || right is Double ->
                (left as Number).toDouble() + (right as Number).toDouble()

            left is Number && right is Number -> left.toLong() + right.toLong()
            else -> throw OclEvaluationException("Cannot add: $left and $right")
        }
    }

    private fun subtractValues(left: Any?, right: Any?): Any? {
        return when {
            left is Double || right is Double ->
                (left as Number).toDouble() - (right as Number).toDouble()

            left is Number && right is Number -> left.toLong() - right.toLong()
            else -> throw OclEvaluationException("Cannot subtract: $left and $right")
        }
    }

    private fun multiplyValues(left: Any?, right: Any?): Any? {
        return when {
            left is Double || right is Double ->
                (left as Number).toDouble() * (right as Number).toDouble()

            left is Number && right is Number -> left.toLong() * right.toLong()
            else -> throw OclEvaluationException("Cannot multiply: $left and $right")
        }
    }

    private fun divideValues(left: Any?, right: Any?): Any? {
        return when {
            left is Number && right is Number ->
                left.toDouble() / right.toDouble()

            else -> throw OclEvaluationException("Cannot divide: $left and $right")
        }
    }

    private fun intDivide(left: Any?, right: Any?): Any? {
        return when {
            left is Number && right is Number ->
                left.toLong() / right.toLong()

            else -> throw OclEvaluationException("Cannot integer divide: $left and $right")
        }
    }

    private fun modulo(left: Any?, right: Any?): Any? {
        return when {
            left is Number && right is Number ->
                left.toLong() % right.toLong()

            else -> throw OclEvaluationException("Cannot modulo: $left and $right")
        }
    }
}

/**
 * Exception thrown during OCL evaluation.
 */
class OclEvaluationException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
