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
package org.openmbee.gearshift.framework.constraints.ocl

/**
 * Visitor interface for OCL AST traversal.
 * Implementations can evaluate, transform, or analyze OCL expressions.
 *
 * @param T The return type of the visitor methods
 */
interface OclVisitor<T> {

    // ===== Literals =====

    fun visitNullLiteral(exp: NullLiteralExp): T
    fun visitBooleanLiteral(exp: BooleanLiteralExp): T
    fun visitIntegerLiteral(exp: IntegerLiteralExp): T
    fun visitRealLiteral(exp: RealLiteralExp): T
    fun visitStringLiteral(exp: StringLiteralExp): T
    fun visitUnlimitedNaturalLiteral(exp: UnlimitedNaturalLiteralExp): T
    fun visitCollectionLiteral(exp: CollectionLiteralExp): T

    // ===== Variables and Properties =====

    fun visitVariable(exp: VariableExp): T
    fun visitPropertyCall(exp: PropertyCallExp): T
    fun visitNavigationCall(exp: NavigationCallExp): T

    // ===== Operations =====

    fun visitOperationCall(exp: OperationCallExp): T
    fun visitArrowCall(exp: ArrowCallExp): T

    // ===== Iterators =====

    fun visitIterator(exp: IteratorExp): T
    fun visitIterate(exp: IterateExp): T

    // ===== Control Flow =====

    fun visitIf(exp: IfExp): T
    fun visitLet(exp: LetExp): T

    // ===== Type Operations =====

    fun visitTypeOp(exp: TypeExp): T

    // ===== Binary/Unary =====

    fun visitInfix(exp: InfixExp): T
    fun visitPrefix(exp: PrefixExp): T
}

/**
 * Base visitor with default implementations that throw.
 * Extend this to implement only the methods you need.
 */
abstract class OclBaseVisitor<T> : OclVisitor<T> {

    protected open fun defaultVisit(exp: OclExpression): T {
        throw UnsupportedOperationException("Visitor not implemented for ${exp::class.simpleName}")
    }

    override fun visitNullLiteral(exp: NullLiteralExp): T = defaultVisit(exp)
    override fun visitBooleanLiteral(exp: BooleanLiteralExp): T = defaultVisit(exp)
    override fun visitIntegerLiteral(exp: IntegerLiteralExp): T = defaultVisit(exp)
    override fun visitRealLiteral(exp: RealLiteralExp): T = defaultVisit(exp)
    override fun visitStringLiteral(exp: StringLiteralExp): T = defaultVisit(exp)
    override fun visitUnlimitedNaturalLiteral(exp: UnlimitedNaturalLiteralExp): T = defaultVisit(exp)
    override fun visitCollectionLiteral(exp: CollectionLiteralExp): T = defaultVisit(exp)

    override fun visitVariable(exp: VariableExp): T = defaultVisit(exp)
    override fun visitPropertyCall(exp: PropertyCallExp): T = defaultVisit(exp)
    override fun visitNavigationCall(exp: NavigationCallExp): T = defaultVisit(exp)

    override fun visitOperationCall(exp: OperationCallExp): T = defaultVisit(exp)
    override fun visitArrowCall(exp: ArrowCallExp): T = defaultVisit(exp)

    override fun visitIterator(exp: IteratorExp): T = defaultVisit(exp)
    override fun visitIterate(exp: IterateExp): T = defaultVisit(exp)

    override fun visitIf(exp: IfExp): T = defaultVisit(exp)
    override fun visitLet(exp: LetExp): T = defaultVisit(exp)

    override fun visitTypeOp(exp: TypeExp): T = defaultVisit(exp)

    override fun visitInfix(exp: InfixExp): T = defaultVisit(exp)
    override fun visitPrefix(exp: PrefixExp): T = defaultVisit(exp)
}

/**
 * Visitor that recursively visits children and returns Unit.
 * Useful for analysis passes that don't need to return values.
 */
abstract class OclRecursiveVisitor : OclVisitor<Unit> {

    override fun visitNullLiteral(exp: NullLiteralExp) {}
    override fun visitBooleanLiteral(exp: BooleanLiteralExp) {}
    override fun visitIntegerLiteral(exp: IntegerLiteralExp) {}
    override fun visitRealLiteral(exp: RealLiteralExp) {}
    override fun visitStringLiteral(exp: StringLiteralExp) {}
    override fun visitUnlimitedNaturalLiteral(exp: UnlimitedNaturalLiteralExp) {}

    override fun visitCollectionLiteral(exp: CollectionLiteralExp) {
        exp.parts.forEach { it.accept(this) }
    }

    override fun visitVariable(exp: VariableExp) {}

    override fun visitPropertyCall(exp: PropertyCallExp) {
        exp.source.accept(this)
    }

    override fun visitNavigationCall(exp: NavigationCallExp) {
        exp.source.accept(this)
    }

    override fun visitOperationCall(exp: OperationCallExp) {
        exp.source?.accept(this)
        exp.arguments.forEach { it.accept(this) }
    }

    override fun visitArrowCall(exp: ArrowCallExp) {
        exp.source.accept(this)
        exp.arguments.forEach { it.accept(this) }
    }

    override fun visitIterator(exp: IteratorExp) {
        exp.source.accept(this)
        exp.body.accept(this)
    }

    override fun visitIterate(exp: IterateExp) {
        exp.source.accept(this)
        exp.accumulatorInit.accept(this)
        exp.body.accept(this)
    }

    override fun visitIf(exp: IfExp) {
        exp.condition.accept(this)
        exp.thenExpression.accept(this)
        exp.elseExpression.accept(this)
    }

    override fun visitLet(exp: LetExp) {
        exp.variableValue.accept(this)
        exp.body.accept(this)
    }

    override fun visitTypeOp(exp: TypeExp) {
        exp.source.accept(this)
    }

    override fun visitInfix(exp: InfixExp) {
        exp.left.accept(this)
        exp.right.accept(this)
    }

    override fun visitPrefix(exp: PrefixExp) {
        exp.operand.accept(this)
    }
}
