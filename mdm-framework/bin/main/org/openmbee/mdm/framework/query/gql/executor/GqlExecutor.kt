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
package org.openmbee.mdm.framework.query.gql.executor

import org.openmbee.mdm.framework.query.gql.ast.*
import org.openmbee.mdm.framework.runtime.MDMEngine
import org.openmbee.mdm.framework.runtime.MDMObject

/**
 * Executes GQL queries against an MDMEngine.
 *
 * This executor evaluates GQL AST nodes against the model:
 * - MATCH clauses find elements matching graph patterns
 * - WHERE clauses filter bindings
 * - RETURN clauses project results into a BindingTable
 */
class GqlExecutor(private val engine: MDMEngine) {

    /**
     * Execute a GQL query and return the results as a BindingTable.
     */
    fun execute(query: GqlQuery): BindingTable {
        // Start with a single empty binding
        var bindings: List<MutableMap<String, Any?>> = listOf(mutableMapOf())

        // Process MATCH clauses - each extends bindings
        for (matchClause in query.matchClauses) {
            bindings = executeMatch(matchClause, bindings)
        }

        // Filter by WHERE clause
        if (query.whereClause != null) {
            bindings = bindings.filter { evaluateWhere(query.whereClause, it) }
        }

        // Project RETURN clause
        return projectReturn(query.returnClause, bindings)
    }

    // ===== MATCH Execution =====

    private fun executeMatch(
        clause: MatchClause,
        bindings: List<MutableMap<String, Any?>>
    ): List<MutableMap<String, Any?>> {
        var result = bindings

        for (pattern in clause.patterns) {
            result = executePathPattern(pattern, result)
        }

        // For optional match, if no results, keep original bindings with nulls for NEW variables
        if (clause.isOptional && result.isEmpty()) {
            return bindings.map { binding ->
                val newBinding = binding.toMutableMap()
                // Add null values for NEW pattern variables (not already bound)
                for (pattern in clause.patterns) {
                    addNullsForPattern(pattern, newBinding, binding.keys)
                }
                newBinding
            }
        }

        return result
    }

    private fun addNullsForPattern(
        pattern: PathPattern,
        binding: MutableMap<String, Any?>,
        existingKeys: Set<String>
    ) {
        // Only add null for NEW variables (not already bound)
        if (pattern.variable != null && pattern.variable !in existingKeys) {
            binding[pattern.variable] = null
        }
        for (element in pattern.elements) {
            when (element) {
                is NodePattern -> if (element.variable != null && element.variable !in existingKeys) {
                    binding[element.variable] = null
                }
                is EdgePattern -> if (element.variable != null && element.variable !in existingKeys) {
                    binding[element.variable] = null
                }
                is PathPattern -> addNullsForPattern(element, binding, existingKeys)
            }
        }
    }

    private fun executePathPattern(
        pattern: PathPattern,
        bindings: List<MutableMap<String, Any?>>
    ): List<MutableMap<String, Any?>> {
        val elements = pattern.elements
        if (elements.isEmpty()) return bindings

        var result = bindings

        // Process elements in sequence (alternating nodes and edges)
        var i = 0
        while (i < elements.size) {
            val element = elements[i]

            when (element) {
                is NodePattern -> {
                    result = matchNodePattern(element, result)
                }
                is EdgePattern -> {
                    // Edge must be between two nodes
                    if (i > 0 && i < elements.size - 1) {
                        val prevNode = elements[i - 1] as? NodePattern
                        val nextNode = elements[i + 1] as? NodePattern
                        if (prevNode != null && nextNode != null) {
                            result = matchEdgePattern(element, prevNode, nextNode, result)
                            i++ // Skip the next node, we've already handled it
                        }
                    }
                }
                is PathPattern -> {
                    result = executePathPattern(element, result)
                }
            }
            i++
        }

        // Bind path variable if specified
        if (pattern.variable != null) {
            result = result.map { binding ->
                val newBinding = binding.toMutableMap()
                // Path variable gets all the matched elements
                val pathElements = pattern.elements.mapNotNull { elem ->
                    when (elem) {
                        is NodePattern -> elem.variable?.let { binding[it] }
                        is EdgePattern -> elem.variable?.let { binding[it] }
                        else -> null
                    }
                }
                newBinding[pattern.variable] = pathElements
                newBinding
            }
        }

        return result
    }

    private fun matchNodePattern(
        pattern: NodePattern,
        bindings: List<MutableMap<String, Any?>>
    ): List<MutableMap<String, Any?>> {
        val variable = pattern.variable

        // If variable is already bound, verify it matches
        if (variable != null) {
            val alreadyBound = bindings.any { it.containsKey(variable) && it[variable] != null }
            if (alreadyBound) {
                return bindings.filter { binding ->
                    val existingValue = binding[variable]
                    if (existingValue is MDMObject) {
                        matchesLabels(existingValue, pattern.labels) &&
                            matchesProperties(existingValue, pattern.properties, binding)
                    } else {
                        false
                    }
                }
            }
        }

        // Find all matching elements
        val candidates = findMatchingNodes(pattern)

        // Extend bindings with matching nodes
        val result = mutableListOf<MutableMap<String, Any?>>()
        for (binding in bindings) {
            for (node in candidates) {
                if (matchesProperties(node, pattern.properties, binding)) {
                    val newBinding = binding.toMutableMap()
                    if (variable != null) {
                        newBinding[variable] = node
                    }
                    result.add(newBinding)
                }
            }
        }

        return result
    }

    private fun findMatchingNodes(pattern: NodePattern): List<MDMObject> {
        return if (pattern.labels.isEmpty()) {
            // No label filter - match all elements
            engine.getAllElements()
        } else {
            // Match elements of specified labels (labels are metaclass names)
            pattern.labels.flatMap { label ->
                engine.getElementsByClass(label)
            }.distinct()
        }
    }

    private fun matchesLabels(element: MDMObject, labels: List<String>): Boolean {
        if (labels.isEmpty()) return true
        return labels.any { label ->
            engine.isInstanceOf(element, label)
        }
    }

    private fun matchesProperties(
        element: MDMObject,
        properties: Map<String, GqlExpression>?,
        binding: Map<String, Any?>
    ): Boolean {
        if (properties.isNullOrEmpty()) return true

        for ((propName, expectedExpr) in properties) {
            val actualValue = engine.getProperty(element, propName)
            val contextBinding: Map<String, Any?> = if (element.id != null) {
                binding + ("__current" to element)
            } else {
                binding
            }
            val expectedValue = evaluateExpression(expectedExpr, contextBinding)

            if (!valuesEqual(actualValue, expectedValue)) {
                return false
            }
        }
        return true
    }

    private fun matchEdgePattern(
        edge: EdgePattern,
        sourceNode: NodePattern,
        targetNode: NodePattern,
        bindings: List<MutableMap<String, Any?>>
    ): List<MutableMap<String, Any?>> {
        val result = mutableListOf<MutableMap<String, Any?>>()

        for (binding in bindings) {
            val sourceVar = sourceNode.variable
            val sourceObj = if (sourceVar != null) binding[sourceVar] as? MDMObject else null

            if (sourceObj == null) continue

            // Find matching edges from source
            val targets = findMatchingEdgeTargets(sourceObj, edge, binding)

            for ((edgeBinding, target) in targets) {
                // Verify target matches target node pattern
                if (!matchesLabels(target, targetNode.labels)) continue
                if (!matchesProperties(target, targetNode.properties, binding)) continue

                val newBinding = binding.toMutableMap()
                newBinding.putAll(edgeBinding)

                // Bind target variable
                if (targetNode.variable != null) {
                    newBinding[targetNode.variable] = target
                }

                result.add(newBinding)
            }
        }

        return result
    }

    private fun findMatchingEdgeTargets(
        source: MDMObject,
        edge: EdgePattern,
        binding: Map<String, Any?>
    ): List<Pair<Map<String, Any?>, MDMObject>> {
        val results = mutableListOf<Pair<Map<String, Any?>, MDMObject>>()
        val sourceId = source.id ?: return results

        // Edge labels map to association names
        val associationNames = if (edge.labels.isEmpty()) {
            // No label filter - try all associations from this element
            engine.getLinks(sourceId).map { it.association.name }.distinct()
        } else {
            edge.labels
        }

        for (assocName in associationNames) {
            val targets = when (edge.direction) {
                EdgeDirection.RIGHT, EdgeDirection.UNDIRECTED_OR_RIGHT, EdgeDirection.LEFT_OR_RIGHT, EdgeDirection.ANY -> {
                    engine.getLinkedTargets(assocName, sourceId)
                }
                EdgeDirection.LEFT, EdgeDirection.LEFT_OR_UNDIRECTED -> {
                    engine.getLinkedSources(assocName, sourceId)
                }
                EdgeDirection.UNDIRECTED -> {
                    // Try both directions
                    engine.getLinkedTargets(assocName, sourceId) +
                        engine.getLinkedSources(assocName, sourceId)
                }
            }

            for (target in targets) {
                // Check edge properties if specified
                if (matchesEdgeProperties(edge.properties, binding)) {
                    val edgeBinding = mutableMapOf<String, Any?>()
                    if (edge.variable != null) {
                        // For edge variables, bind to a map with association info
                        edgeBinding[edge.variable] = mapOf(
                            "type" to assocName,
                            "source" to source,
                            "target" to target
                        )
                    }
                    results.add(edgeBinding to target)
                }
            }
        }

        // Handle variable-length paths
        if (edge.quantifier != null) {
            val additionalResults = expandVariableLengthPath(source, edge, binding, results)
            results.addAll(additionalResults)
        }

        return results
    }

    private fun matchesEdgeProperties(
        properties: Map<String, GqlExpression>?,
        binding: Map<String, Any?>
    ): Boolean {
        // Edge properties are not directly supported in MDM graph
        // For now, always return true
        return properties.isNullOrEmpty()
    }

    private fun expandVariableLengthPath(
        source: MDMObject,
        edge: EdgePattern,
        binding: Map<String, Any?>,
        initialResults: List<Pair<Map<String, Any?>, MDMObject>>
    ): List<Pair<Map<String, Any?>, MDMObject>> {
        val quantifier = edge.quantifier ?: return emptyList()

        val minHops = when (quantifier) {
            is FixedQuantifier -> quantifier.count
            is RangeQuantifier -> quantifier.min ?: 0
        }
        val maxHops = when (quantifier) {
            is FixedQuantifier -> quantifier.count
            is RangeQuantifier -> quantifier.max ?: 10 // Default max depth
        }

        if (minHops == 1 && maxHops == 1) {
            return emptyList() // Already handled by initial traversal
        }

        val results = mutableListOf<Pair<Map<String, Any?>, MDMObject>>()
        val visited = mutableSetOf<String>()
        visited.add(source.id ?: return emptyList())

        // BFS to find paths of length minHops..maxHops
        data class WorkItem(val node: MDMObject, val depth: Int, val path: List<MDMObject>)
        val queue = ArrayDeque<WorkItem>()
        queue.add(WorkItem(source, 0, listOf(source)))

        while (queue.isNotEmpty()) {
            val (current, depth, path) = queue.removeFirst()

            if (depth >= maxHops) continue

            val currentId = current.id ?: continue
            val edgeCopy = edge.copy(quantifier = null)
            val targets = findMatchingEdgeTargets(current, edgeCopy, binding)

            for ((edgeBinding, target) in targets) {
                val targetId = target.id ?: continue
                if (targetId in visited) continue

                visited.add(targetId)
                val newPath = path + target
                val newDepth = depth + 1

                if (newDepth >= minHops) {
                    val pathBinding = edgeBinding.toMutableMap()
                    if (edge.variable != null) {
                        pathBinding[edge.variable] = newPath
                    }
                    results.add(pathBinding to target)
                }

                if (newDepth < maxHops) {
                    queue.add(WorkItem(target, newDepth, newPath))
                }
            }
        }

        return results
    }

    // ===== WHERE Evaluation =====

    private fun evaluateWhere(clause: WhereClause, binding: Map<String, Any?>): Boolean {
        val result = evaluateExpression(clause.expression, binding)
        return result == true
    }

    private fun evaluateExpression(expr: GqlExpression, binding: Map<String, Any?>): Any? {
        return when (expr) {
            is VariableRef -> binding[expr.name]

            is PropertyAccess -> {
                val base = evaluateExpression(expr.base, binding)
                when (base) {
                    is MDMObject -> engine.getProperty(base, expr.property)
                    is Map<*, *> -> base[expr.property]
                    else -> null
                }
            }

            is Literal -> expr.value

            is BinaryOp -> evaluateBinaryOp(expr, binding)

            is UnaryOp -> evaluateUnaryOp(expr, binding)

            is FunctionCall -> evaluateFunction(expr, binding)

            is ListExpr -> expr.elements.map { evaluateExpression(it, binding) }

            is InExpr -> {
                val value = evaluateExpression(expr.value, binding)
                val list = evaluateExpression(expr.list, binding)
                val result = when (list) {
                    is Collection<*> -> list.contains(value)
                    is Array<*> -> list.contains(value)
                    else -> false
                }
                if (expr.negated) !result else result
            }

            is IsNullExpr -> {
                val value = evaluateExpression(expr.value, binding)
                val isNull = value == null
                if (expr.negated) !isNull else isNull
            }

            is CaseExpr -> evaluateCaseExpr(expr, binding)

            is LabelCheck -> {
                val obj = evaluateExpression(expr.variable, binding)
                val result = if (obj is MDMObject) {
                    engine.isInstanceOf(obj, expr.label)
                } else {
                    false
                }
                if (expr.negated) !result else result
            }

            is ExistsExpr -> {
                // Check if pattern would match anything
                val testBindings = executePathPattern(expr.pattern, listOf(binding.toMutableMap()))
                testBindings.isNotEmpty()
            }
        }
    }

    private fun evaluateBinaryOp(expr: BinaryOp, binding: Map<String, Any?>): Any? {
        // Short-circuit evaluation for AND/OR
        if (expr.operator == BinaryOperator.AND) {
            val left = evaluateExpression(expr.left, binding)
            if (left != true) return false
            return evaluateExpression(expr.right, binding) == true
        }
        if (expr.operator == BinaryOperator.OR) {
            val left = evaluateExpression(expr.left, binding)
            if (left == true) return true
            return evaluateExpression(expr.right, binding) == true
        }

        val left = evaluateExpression(expr.left, binding)
        val right = evaluateExpression(expr.right, binding)

        return when (expr.operator) {
            BinaryOperator.EQUALS -> valuesEqual(left, right)
            BinaryOperator.NOT_EQUALS -> !valuesEqual(left, right)
            BinaryOperator.LESS_THAN -> compare(left, right)?.let { it < 0 } ?: false
            BinaryOperator.LESS_THAN_OR_EQUALS -> compare(left, right)?.let { it <= 0 } ?: false
            BinaryOperator.GREATER_THAN -> compare(left, right)?.let { it > 0 } ?: false
            BinaryOperator.GREATER_THAN_OR_EQUALS -> compare(left, right)?.let { it >= 0 } ?: false
            BinaryOperator.AND -> left == true && right == true
            BinaryOperator.OR -> left == true || right == true
            BinaryOperator.XOR -> (left == true) xor (right == true)
            BinaryOperator.PLUS -> add(left, right)
            BinaryOperator.MINUS -> subtract(left, right)
            BinaryOperator.MULTIPLY -> multiply(left, right)
            BinaryOperator.DIVIDE -> divide(left, right)
            BinaryOperator.MODULO -> modulo(left, right)
            BinaryOperator.CONCATENATION -> "${left ?: ""}${right ?: ""}"
            BinaryOperator.STARTS_WITH -> left?.toString()?.startsWith(right?.toString() ?: "") ?: false
            BinaryOperator.ENDS_WITH -> left?.toString()?.endsWith(right?.toString() ?: "") ?: false
            BinaryOperator.CONTAINS -> left?.toString()?.contains(right?.toString() ?: "") ?: false
            BinaryOperator.LIKE -> matchesLike(left?.toString(), right?.toString())
        }
    }

    private fun evaluateUnaryOp(expr: UnaryOp, binding: Map<String, Any?>): Any? {
        val operand = evaluateExpression(expr.operand, binding)
        return when (expr.operator) {
            UnaryOperator.NOT -> operand != true
            UnaryOperator.NEGATE -> negate(operand)
            UnaryOperator.POSITIVE -> operand
        }
    }

    private fun evaluateFunction(expr: FunctionCall, binding: Map<String, Any?>): Any? {
        val args = expr.args.map { evaluateExpression(it, binding) }

        return when (expr.name.uppercase()) {
            "COUNT" -> {
                if (args.firstOrNull() == "*") {
                    1L // COUNT(*) in context of single row
                } else {
                    val arg = args.firstOrNull()
                    when (arg) {
                        null -> 0L
                        is Collection<*> -> arg.size.toLong()
                        else -> 1L
                    }
                }
            }
            "SUM" -> {
                val values = flattenToNumbers(args)
                values.fold(0.0) { acc, n -> acc + n.toDouble() }
            }
            "AVG" -> {
                val values = flattenToNumbers(args)
                if (values.isEmpty()) null else values.sumOf { it.toDouble() } / values.size
            }
            "MIN" -> {
                val values = flattenToNumbers(args)
                values.minOfOrNull { it.toDouble() }
            }
            "MAX" -> {
                val values = flattenToNumbers(args)
                values.maxOfOrNull { it.toDouble() }
            }
            "COLLECT" -> args.flatMap { arg ->
                when (arg) {
                    is Collection<*> -> arg.toList()
                    else -> listOf(arg)
                }
            }
            "SIZE", "LENGTH", "CHAR_LENGTH", "CHARACTER_LENGTH" -> {
                val arg = args.firstOrNull()
                when (arg) {
                    is String -> arg.length.toLong()
                    is Collection<*> -> arg.size.toLong()
                    is Array<*> -> arg.size.toLong()
                    else -> 0L
                }
            }
            "COALESCE" -> args.firstOrNull { it != null }
            "TOSTRING" -> args.firstOrNull()?.toString()
            "TOINTEGER", "TOINT" -> args.firstOrNull()?.toString()?.toLongOrNull()
            "TOFLOAT", "TODOUBLE" -> args.firstOrNull()?.toString()?.toDoubleOrNull()
            "TOBOOLEAN" -> args.firstOrNull()?.toString()?.lowercase()?.toBooleanStrictOrNull()
            "UPPER", "TOUPPER" -> args.firstOrNull()?.toString()?.uppercase()
            "LOWER", "TOLOWER" -> args.firstOrNull()?.toString()?.lowercase()
            "TRIM" -> args.firstOrNull()?.toString()?.trim()
            "LTRIM" -> args.firstOrNull()?.toString()?.trimStart()
            "RTRIM" -> args.firstOrNull()?.toString()?.trimEnd()
            "SUBSTRING" -> {
                val str = args.getOrNull(0)?.toString() ?: return null
                val start = (args.getOrNull(1) as? Number)?.toInt() ?: 0
                val length = args.getOrNull(2) as? Number
                if (length != null) {
                    str.substring(start.coerceIn(0, str.length), (start + length.toInt()).coerceIn(0, str.length))
                } else {
                    str.substring(start.coerceIn(0, str.length))
                }
            }
            "REPLACE" -> {
                val str = args.getOrNull(0)?.toString() ?: return null
                val search = args.getOrNull(1)?.toString() ?: return str
                val replace = args.getOrNull(2)?.toString() ?: ""
                str.replace(search, replace)
            }
            "SPLIT" -> {
                val str = args.getOrNull(0)?.toString() ?: return emptyList<String>()
                val delimiter = args.getOrNull(1)?.toString() ?: ","
                str.split(delimiter)
            }
            "HEAD" -> {
                when (val arg = args.firstOrNull()) {
                    is List<*> -> arg.firstOrNull()
                    is Collection<*> -> arg.firstOrNull()
                    else -> null
                }
            }
            "TAIL" -> {
                when (val arg = args.firstOrNull()) {
                    is List<*> -> if (arg.size > 1) arg.drop(1) else emptyList<Any?>()
                    else -> emptyList<Any?>()
                }
            }
            "LAST" -> {
                when (val arg = args.firstOrNull()) {
                    is List<*> -> arg.lastOrNull()
                    is Collection<*> -> arg.lastOrNull()
                    else -> null
                }
            }
            "REVERSE" -> {
                when (val arg = args.firstOrNull()) {
                    is List<*> -> arg.reversed()
                    is String -> arg.reversed()
                    else -> arg
                }
            }
            "KEYS" -> {
                when (val arg = args.firstOrNull()) {
                    is Map<*, *> -> arg.keys.toList()
                    is MDMObject -> arg.getAllProperties().keys.toList()
                    else -> emptyList<String>()
                }
            }
            "ID", "ELEMENT_ID" -> {
                when (val arg = args.firstOrNull()) {
                    is MDMObject -> arg.id
                    else -> null
                }
            }
            "TYPE" -> {
                when (val arg = args.firstOrNull()) {
                    is MDMObject -> arg.className
                    is Map<*, *> -> arg["type"] // For edge bindings
                    else -> null
                }
            }
            "LABELS" -> {
                when (val arg = args.firstOrNull()) {
                    is MDMObject -> listOf(arg.className)
                    else -> emptyList<String>()
                }
            }
            "EXISTS" -> {
                args.firstOrNull() != null
            }
            "ABS" -> {
                when (val arg = args.firstOrNull()) {
                    is Number -> kotlin.math.abs(arg.toDouble())
                    else -> null
                }
            }
            "CEIL" -> {
                when (val arg = args.firstOrNull()) {
                    is Number -> kotlin.math.ceil(arg.toDouble()).toLong()
                    else -> null
                }
            }
            "FLOOR" -> {
                when (val arg = args.firstOrNull()) {
                    is Number -> kotlin.math.floor(arg.toDouble()).toLong()
                    else -> null
                }
            }
            "ROUND" -> {
                when (val arg = args.firstOrNull()) {
                    is Number -> kotlin.math.round(arg.toDouble()).toLong()
                    else -> null
                }
            }
            else -> null // Unknown function
        }
    }

    private fun evaluateCaseExpr(expr: CaseExpr, binding: Map<String, Any?>): Any? {
        val operand = expr.operand?.let { evaluateExpression(it, binding) }

        for (whenClause in expr.whenClauses) {
            val condition = evaluateExpression(whenClause.condition, binding)
            val matches = if (operand != null) {
                valuesEqual(operand, condition)
            } else {
                condition == true
            }
            if (matches) {
                return evaluateExpression(whenClause.result, binding)
            }
        }

        return expr.elseExpr?.let { evaluateExpression(it, binding) }
    }

    // ===== RETURN Projection =====

    private fun projectReturn(
        clause: ReturnClause,
        bindings: List<MutableMap<String, Any?>>
    ): BindingTable {
        if (clause.returnAll) {
            // RETURN * - return all bound variables
            val columns = bindings.firstOrNull()?.keys?.toList() ?: emptyList()
            val rows = if (clause.distinct) {
                bindings.map { it.toMap() }.distinct()
            } else {
                bindings.map { it.toMap() }
            }
            return BindingTable(columns, rows)
        }

        if (clause.items.isEmpty()) {
            // No items - return empty or count
            return BindingTable.EMPTY
        }

        // Project specified items
        val columns = clause.items.map { item ->
            item.alias ?: expressionName(item.expression)
        }

        val rows = bindings.map { binding ->
            clause.items.associate { item ->
                val columnName = item.alias ?: expressionName(item.expression)
                val value = evaluateExpression(item.expression, binding)
                columnName to value
            }
        }

        val finalRows = if (clause.distinct) rows.distinct() else rows
        return BindingTable(columns, finalRows)
    }

    private fun expressionName(expr: GqlExpression): String {
        return when (expr) {
            is VariableRef -> expr.name
            is PropertyAccess -> "${expressionName(expr.base)}.${expr.property}"
            is FunctionCall -> "${expr.name}(...)"
            is Literal -> expr.value?.toString() ?: "null"
            else -> "expr"
        }
    }

    // ===== Helper Methods =====

    private fun valuesEqual(a: Any?, b: Any?): Boolean {
        if (a == null && b == null) return true
        if (a == null || b == null) return false
        if (a == b) return true

        // MDMObject comparison by ID
        if (a is MDMObject && b is MDMObject) {
            return a.id == b.id
        }

        // Numeric comparison
        if (a is Number && b is Number) {
            return a.toDouble() == b.toDouble()
        }

        // String comparison
        return a.toString() == b.toString()
    }

    @Suppress("UNCHECKED_CAST")
    private fun compare(a: Any?, b: Any?): Int? {
        if (a == null || b == null) return null

        return when {
            a is Number && b is Number -> a.toDouble().compareTo(b.toDouble())
            a is String && b is String -> a.compareTo(b)
            a is Comparable<*> && b is Comparable<*> -> {
                try {
                    (a as Comparable<Any>).compareTo(b)
                } catch (e: Exception) {
                    null
                }
            }
            else -> null
        }
    }

    private fun add(a: Any?, b: Any?): Any? {
        return when {
            a is Number && b is Number -> {
                if (a is Double || b is Double || a is Float || b is Float) {
                    a.toDouble() + b.toDouble()
                } else {
                    a.toLong() + b.toLong()
                }
            }
            a is String || b is String -> "${a ?: ""}${b ?: ""}"
            a is Collection<*> && b is Collection<*> -> a.toList() + b.toList()
            else -> null
        }
    }

    private fun subtract(a: Any?, b: Any?): Any? {
        if (a is Number && b is Number) {
            return if (a is Double || b is Double || a is Float || b is Float) {
                a.toDouble() - b.toDouble()
            } else {
                a.toLong() - b.toLong()
            }
        }
        return null
    }

    private fun multiply(a: Any?, b: Any?): Any? {
        if (a is Number && b is Number) {
            return if (a is Double || b is Double || a is Float || b is Float) {
                a.toDouble() * b.toDouble()
            } else {
                a.toLong() * b.toLong()
            }
        }
        return null
    }

    private fun divide(a: Any?, b: Any?): Any? {
        if (a is Number && b is Number) {
            val bVal = b.toDouble()
            if (bVal == 0.0) return null
            return a.toDouble() / bVal
        }
        return null
    }

    private fun modulo(a: Any?, b: Any?): Any? {
        if (a is Number && b is Number) {
            val bVal = b.toLong()
            if (bVal == 0L) return null
            return a.toLong() % bVal
        }
        return null
    }

    private fun negate(a: Any?): Any? {
        return when (a) {
            is Long -> -a
            is Int -> -a
            is Double -> -a
            is Float -> -a
            is Number -> -a.toDouble()
            else -> null
        }
    }

    private fun matchesLike(value: String?, pattern: String?): Boolean {
        if (value == null || pattern == null) return false
        // Convert SQL LIKE pattern to regex
        val regex = pattern
            .replace("%", ".*")
            .replace("_", ".")
            .replace("\\", "\\\\")
        return value.matches(Regex(regex, RegexOption.IGNORE_CASE))
    }

    private fun flattenToNumbers(args: List<Any?>): List<Number> {
        return args.flatMap { arg ->
            when (arg) {
                is Number -> listOf(arg)
                is Collection<*> -> arg.filterIsInstance<Number>()
                else -> emptyList()
            }
        }
    }
}
