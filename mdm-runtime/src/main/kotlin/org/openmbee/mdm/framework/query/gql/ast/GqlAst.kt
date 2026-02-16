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
package org.openmbee.mdm.framework.query.gql.ast

/**
 * GQL Abstract Syntax Tree node definitions.
 * Based on ISO GQL specification for graph pattern matching.
 */

// ===== Top-level Query =====

/**
 * Base interface for all GQL statements.
 */
sealed interface GqlStatement

/**
 * A complete GQL query with MATCH, optional WHERE, and RETURN clauses.
 */
data class GqlQuery(
    val matchClauses: List<MatchClause>,
    val whereClause: WhereClause?,
    val returnClause: ReturnClause
) : GqlStatement

// ===== MATCH Clause =====

/**
 * A MATCH clause containing one or more graph patterns.
 */
data class MatchClause(
    val patterns: List<PathPattern>,
    val isOptional: Boolean = false
)

// ===== Graph Patterns =====

/**
 * Base interface for graph pattern elements.
 */
sealed interface GraphPattern

/**
 * A node pattern: (variable:Label {props})
 */
data class NodePattern(
    val variable: String?,
    val labels: List<String>,
    val properties: Map<String, GqlExpression>?
) : GraphPattern

/**
 * An edge pattern: -[variable:Label]-> or <-[variable:Label]- etc.
 */
data class EdgePattern(
    val variable: String?,
    val labels: List<String>,
    val direction: EdgeDirection,
    val properties: Map<String, GqlExpression>?,
    val quantifier: PathQuantifier?
) : GraphPattern

/**
 * A complete path pattern: (n)-[e]->(m)
 * Elements alternate between NodePattern and EdgePattern.
 */
data class PathPattern(
    val elements: List<GraphPattern>,
    val variable: String? = null
) : GraphPattern

/**
 * Edge direction types from GQL specification.
 */
enum class EdgeDirection {
    /** Pointing left: <-[e]- */
    LEFT,
    /** Pointing right: -[e]-> */
    RIGHT,
    /** Undirected: ~[e]~ */
    UNDIRECTED,
    /** Left or undirected: <~[e]~ */
    LEFT_OR_UNDIRECTED,
    /** Undirected or right: ~[e]~> */
    UNDIRECTED_OR_RIGHT,
    /** Left or right: <-[e]-> */
    LEFT_OR_RIGHT,
    /** Any direction: -[e]- */
    ANY
}

// ===== Path Quantifiers =====

/**
 * Base interface for path quantifiers (variable-length paths).
 */
sealed interface PathQuantifier

/**
 * Fixed quantifier: {n}
 */
data class FixedQuantifier(val count: Int) : PathQuantifier

/**
 * Range quantifier: {min,max}, *, +
 */
data class RangeQuantifier(
    val min: Int?,
    val max: Int?
) : PathQuantifier {
    companion object {
        /** Zero or more: * */
        val STAR = RangeQuantifier(0, null)
        /** One or more: + */
        val PLUS = RangeQuantifier(1, null)
        /** Zero or one: ? */
        val OPTIONAL = RangeQuantifier(0, 1)
    }
}

// ===== WHERE Clause =====

/**
 * A WHERE clause for filtering results.
 */
data class WhereClause(val expression: GqlExpression)

// ===== RETURN Clause =====

/**
 * A RETURN clause specifying what to output.
 */
data class ReturnClause(
    val items: List<ReturnItem>,
    val distinct: Boolean = false,
    val returnAll: Boolean = false
)

/**
 * A single item in the RETURN clause.
 */
data class ReturnItem(
    val expression: GqlExpression,
    val alias: String?
)

// ===== Expressions =====

/**
 * Base interface for all GQL expressions.
 */
sealed interface GqlExpression

/**
 * Variable reference: n
 */
data class VariableRef(val name: String) : GqlExpression

/**
 * Property access: n.name
 */
data class PropertyAccess(
    val base: GqlExpression,
    val property: String
) : GqlExpression

/**
 * Literal value: 42, 'hello', true, null
 */
data class Literal(val value: Any?) : GqlExpression {
    companion object {
        val NULL = Literal(null)
        val TRUE = Literal(true)
        val FALSE = Literal(false)
    }
}

/**
 * Binary operation: a + b, a = b, a AND b
 */
data class BinaryOp(
    val left: GqlExpression,
    val operator: BinaryOperator,
    val right: GqlExpression
) : GqlExpression

/**
 * Unary operation: NOT x, -x
 */
data class UnaryOp(
    val operator: UnaryOperator,
    val operand: GqlExpression
) : GqlExpression

/**
 * Function call: count(*), sum(n.value)
 */
data class FunctionCall(
    val name: String,
    val args: List<GqlExpression>,
    val distinct: Boolean = false
) : GqlExpression

/**
 * List expression: [1, 2, 3]
 */
data class ListExpr(val elements: List<GqlExpression>) : GqlExpression

/**
 * IN expression: x IN [1, 2, 3]
 */
data class InExpr(
    val value: GqlExpression,
    val list: GqlExpression,
    val negated: Boolean = false
) : GqlExpression

/**
 * IS NULL expression: x IS NULL, x IS NOT NULL
 */
data class IsNullExpr(
    val value: GqlExpression,
    val negated: Boolean = false
) : GqlExpression

/**
 * CASE expression: CASE WHEN ... THEN ... ELSE ... END
 */
data class CaseExpr(
    val operand: GqlExpression?,
    val whenClauses: List<WhenClause>,
    val elseExpr: GqlExpression?
) : GqlExpression

/**
 * A WHEN clause in a CASE expression.
 */
data class WhenClause(
    val condition: GqlExpression,
    val result: GqlExpression
)

/**
 * Label check: n:Label or n IS Label
 */
data class LabelCheck(
    val variable: GqlExpression,
    val label: String,
    val negated: Boolean = false
) : GqlExpression

/**
 * EXISTS subquery predicate.
 */
data class ExistsExpr(
    val pattern: PathPattern
) : GqlExpression

// ===== Operators =====

/**
 * Binary operators.
 */
enum class BinaryOperator(val symbol: String) {
    // Comparison
    EQUALS("="),
    NOT_EQUALS("<>"),
    LESS_THAN("<"),
    LESS_THAN_OR_EQUALS("<="),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUALS(">="),

    // Logical
    AND("AND"),
    OR("OR"),
    XOR("XOR"),

    // Arithmetic
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    MODULO("%"),

    // String
    CONCATENATION("||"),

    // Pattern matching
    STARTS_WITH("STARTS WITH"),
    ENDS_WITH("ENDS WITH"),
    CONTAINS("CONTAINS"),
    LIKE("LIKE");

    companion object {
        fun fromSymbol(symbol: String): BinaryOperator? =
            entries.find { it.symbol.equals(symbol, ignoreCase = true) }
    }
}

/**
 * Unary operators.
 */
enum class UnaryOperator(val symbol: String) {
    NOT("NOT"),
    NEGATE("-"),
    POSITIVE("+");

    companion object {
        fun fromSymbol(symbol: String): UnaryOperator? =
            entries.find { it.symbol.equals(symbol, ignoreCase = true) }
    }
}
