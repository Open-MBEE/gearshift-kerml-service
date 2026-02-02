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
package org.openmbee.mdm.framework.constraints.ocl

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
