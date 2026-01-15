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
package org.openmbee.gearshift.constraints.parsers.ocl

/**
 * OCL Abstract Syntax Tree node definitions.
 * Based on OCL 2.4 specification metamodel.
 */

/**
 * Base interface for all OCL AST nodes.
 */
sealed interface OclExpression {
    /**
     * Accept a visitor for traversal.
     */
    fun <T> accept(visitor: OclVisitor<T>): T
}

// ===== Literal Expressions =====

/**
 * Null literal.
 */
data object NullLiteralExp : OclExpression {
    override fun <T> accept(visitor: OclVisitor<T>): T = visitor.visitNullLiteral(this)
}

/**
 * Boolean literal (true/false).
 */
data class BooleanLiteralExp(val value: Boolean) : OclExpression {
    override fun <T> accept(visitor: OclVisitor<T>): T = visitor.visitBooleanLiteral(this)
}

/**
 * Integer literal.
 */
data class IntegerLiteralExp(val value: Long) : OclExpression {
    override fun <T> accept(visitor: OclVisitor<T>): T = visitor.visitIntegerLiteral(this)
}

/**
 * Real (floating point) literal.
 */
data class RealLiteralExp(val value: Double) : OclExpression {
    override fun <T> accept(visitor: OclVisitor<T>): T = visitor.visitRealLiteral(this)
}

/**
 * String literal.
 */
data class StringLiteralExp(val value: String) : OclExpression {
    override fun <T> accept(visitor: OclVisitor<T>): T = visitor.visitStringLiteral(this)
}

/**
 * Unlimited natural (*) literal.
 */
data object UnlimitedNaturalLiteralExp : OclExpression {
    override fun <T> accept(visitor: OclVisitor<T>): T = visitor.visitUnlimitedNaturalLiteral(this)
}

/**
 * Collection literal (Set, Bag, Sequence, OrderedSet).
 */
data class CollectionLiteralExp(
    val kind: CollectionKind,
    val parts: List<OclExpression>
) : OclExpression {
    override fun <T> accept(visitor: OclVisitor<T>): T = visitor.visitCollectionLiteral(this)
}

enum class CollectionKind {
    SET, BAG, SEQUENCE, ORDERED_SET
}

// ===== Variable and Property Access =====

/**
 * Variable reference (self, result, iterator variables, etc.).
 */
data class VariableExp(val name: String) : OclExpression {
    override fun <T> accept(visitor: OclVisitor<T>): T = visitor.visitVariable(this)
}

/**
 * Property access (object.property).
 */
data class PropertyCallExp(
    val source: OclExpression,
    val propertyName: String
) : OclExpression {
    override fun <T> accept(visitor: OclVisitor<T>): T = visitor.visitPropertyCall(this)
}

/**
 * Association end navigation (object.associationEnd).
 */
data class NavigationCallExp(
    val source: OclExpression,
    val navigationName: String
) : OclExpression {
    override fun <T> accept(visitor: OclVisitor<T>): T = visitor.visitNavigationCall(this)
}

// ===== Operations =====

/**
 * Operation call (object.operation(args) or object->operation(args)).
 */
data class OperationCallExp(
    val source: OclExpression?,
    val operationName: String,
    val arguments: List<OclExpression>
) : OclExpression {
    override fun <T> accept(visitor: OclVisitor<T>): T = visitor.visitOperationCall(this)
}

/**
 * Arrow operation call (collection->operation()).
 * Distinguishes between object operations (.) and collection operations (->).
 */
data class ArrowCallExp(
    val source: OclExpression,
    val operationName: String,
    val arguments: List<OclExpression>
) : OclExpression {
    override fun <T> accept(visitor: OclVisitor<T>): T = visitor.visitArrowCall(this)
}

// ===== Iterators =====

/**
 * Iterator expression (collection->select/reject/collect/forAll/exists/etc.).
 */
data class IteratorExp(
    val source: OclExpression,
    val iteratorName: String,
    val iteratorVariable: String,
    val body: OclExpression
) : OclExpression {
    override fun <T> accept(visitor: OclVisitor<T>): T = visitor.visitIterator(this)
}

/**
 * Iterate expression (the general iterator with accumulator).
 */
data class IterateExp(
    val source: OclExpression,
    val iteratorVariable: String,
    val accumulatorVariable: String,
    val accumulatorInit: OclExpression,
    val body: OclExpression
) : OclExpression {
    override fun <T> accept(visitor: OclVisitor<T>): T = visitor.visitIterate(this)
}

// ===== Control Flow =====

/**
 * If-then-else expression.
 */
data class IfExp(
    val condition: OclExpression,
    val thenExpression: OclExpression,
    val elseExpression: OclExpression
) : OclExpression {
    override fun <T> accept(visitor: OclVisitor<T>): T = visitor.visitIf(this)
}

/**
 * Let expression (let var = value in body).
 */
data class LetExp(
    val variableName: String,
    val variableValue: OclExpression,
    val body: OclExpression
) : OclExpression {
    override fun <T> accept(visitor: OclVisitor<T>): T = visitor.visitLet(this)
}

// ===== Type Operations =====

/**
 * Type check (oclIsKindOf, oclIsTypeOf).
 */
data class TypeExp(
    val source: OclExpression,
    val operationName: String,  // oclIsKindOf, oclIsTypeOf, oclAsType
    val typeName: String
) : OclExpression {
    override fun <T> accept(visitor: OclVisitor<T>): T = visitor.visitTypeOp(this)
}

// ===== Binary/Unary Operations =====

/**
 * Binary infix operation (a + b, a and b, a = b, etc.).
 */
data class InfixExp(
    val left: OclExpression,
    val operator: String,
    val right: OclExpression
) : OclExpression {
    override fun <T> accept(visitor: OclVisitor<T>): T = visitor.visitInfix(this)
}

/**
 * Unary prefix operation (not, -).
 */
data class PrefixExp(
    val operator: String,
    val operand: OclExpression
) : OclExpression {
    override fun <T> accept(visitor: OclVisitor<T>): T = visitor.visitPrefix(this)
}
