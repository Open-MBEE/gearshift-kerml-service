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

import com.microsoft.z3.*

/**
 * Translates OCL AST expressions to Z3 SMT solver expressions.
 *
 * Implements [OclVisitor] to walk the OCL AST and produce Z3 [Expr] nodes.
 * Supports a subset of OCL suitable for constraint solving:
 *
 * **Supported:**
 * - Numeric: +, -, *, /, <, >, <=, >=, =, <>
 * - Boolean: and, or, not, implies, xor
 * - If-then-else
 * - Variables → Z3 symbolic constants
 * - Literals → Z3 constants
 * - Let expressions
 *
 * **Unsupported (throws [UnsupportedOperationException]):**
 * - Iterators (select, collect, forAll, exists, etc.)
 * - Navigation (property access, association traversal)
 * - String operations
 * - Type operations (oclIsKindOf, oclAsType)
 * - Model introspection
 *
 * @param ctx The Z3 Context to create expressions in
 * @param variables Map of variable names to their Z3 declarations
 */
class OclToZ3Translator(
    private val ctx: Context,
    private val variables: Map<String, Expr<*>>
) : OclVisitor<Expr<*>> {

    /** Stack for let-expression variable bindings. */
    private val letBindings = mutableMapOf<String, Expr<*>>()

    override fun visitNullLiteral(exp: NullLiteralExp): Expr<*> {
        // Represent null as integer 0 (Z3 doesn't have null)
        return ctx.mkInt(0)
    }

    override fun visitBooleanLiteral(exp: BooleanLiteralExp): Expr<*> {
        return ctx.mkBool(exp.value)
    }

    override fun visitIntegerLiteral(exp: IntegerLiteralExp): Expr<*> {
        return ctx.mkInt(exp.value)
    }

    override fun visitRealLiteral(exp: RealLiteralExp): Expr<*> {
        return ctx.mkReal(exp.value.toString())
    }

    override fun visitStringLiteral(exp: StringLiteralExp): Expr<*> {
        throw UnsupportedOperationException("String literals are not supported in Z3 translation")
    }

    override fun visitUnlimitedNaturalLiteral(exp: UnlimitedNaturalLiteralExp): Expr<*> {
        // Represent * as a very large integer
        return ctx.mkInt(Int.MAX_VALUE.toLong())
    }

    override fun visitCollectionLiteral(exp: CollectionLiteralExp): Expr<*> {
        throw UnsupportedOperationException("Collection literals are not supported in Z3 translation")
    }

    override fun visitVariable(exp: VariableExp): Expr<*> {
        // Check let bindings first
        letBindings[exp.name]?.let { return it }
        // Then check declared variables
        return variables[exp.name]
            ?: throw IllegalArgumentException("Undeclared variable: ${exp.name}")
    }

    override fun visitPropertyCall(exp: PropertyCallExp): Expr<*> {
        throw UnsupportedOperationException("Property access is not supported in Z3 translation")
    }

    override fun visitNavigationCall(exp: NavigationCallExp): Expr<*> {
        throw UnsupportedOperationException("Navigation is not supported in Z3 translation")
    }

    override fun visitOperationCall(exp: OperationCallExp): Expr<*> {
        throw UnsupportedOperationException("Operation calls are not supported in Z3 translation: ${exp.operationName}")
    }

    override fun visitArrowCall(exp: ArrowCallExp): Expr<*> {
        throw UnsupportedOperationException("Arrow calls are not supported in Z3 translation: ${exp.operationName}")
    }

    override fun visitIterator(exp: IteratorExp): Expr<*> {
        throw UnsupportedOperationException("Iterators are not supported in Z3 translation: ${exp.iteratorName}")
    }

    override fun visitIterate(exp: IterateExp): Expr<*> {
        throw UnsupportedOperationException("Iterate is not supported in Z3 translation")
    }

    @Suppress("UNCHECKED_CAST")
    override fun visitIf(exp: IfExp): Expr<*> {
        val condition = exp.condition.accept(this) as BoolExpr
        val thenExpr = exp.thenExpression.accept(this)
        val elseExpr = exp.elseExpression.accept(this)
        return ctx.mkITE(condition, thenExpr, elseExpr)
    }

    override fun visitLet(exp: LetExp): Expr<*> {
        val value = exp.variableValue.accept(this)
        letBindings[exp.variableName] = value
        try {
            return exp.body.accept(this)
        } finally {
            letBindings.remove(exp.variableName)
        }
    }

    override fun visitTypeOp(exp: TypeExp): Expr<*> {
        throw UnsupportedOperationException("Type operations are not supported in Z3 translation: ${exp.operationName}")
    }

    override fun visitDynamicTypeOp(exp: DynamicTypeExp): Expr<*> {
        throw UnsupportedOperationException("Dynamic type operations are not supported in Z3 translation")
    }

    @Suppress("UNCHECKED_CAST")
    override fun visitInfix(exp: InfixExp): Expr<*> {
        val left = exp.left.accept(this)
        val right = exp.right.accept(this)

        return when (exp.operator) {
            // Arithmetic
            "+" -> ctx.mkAdd(toArithExpr(left), toArithExpr(right))
            "-" -> ctx.mkSub(toArithExpr(left), toArithExpr(right))
            "*" -> ctx.mkMul(toArithExpr(left), toArithExpr(right))
            "/" -> ctx.mkDiv(toArithExpr(left), toArithExpr(right))

            // Comparison
            "<" -> ctx.mkLt(toArithExpr(left), toArithExpr(right))
            ">" -> ctx.mkGt(toArithExpr(left), toArithExpr(right))
            "<=" -> ctx.mkLe(toArithExpr(left), toArithExpr(right))
            ">=" -> ctx.mkGe(toArithExpr(left), toArithExpr(right))

            // Equality
            "=" -> ctx.mkEq(left, right)
            "==" -> ctx.mkEq(left, right)
            "<>" -> ctx.mkNot(ctx.mkEq(left, right))
            "!=" -> ctx.mkNot(ctx.mkEq(left, right))

            // Boolean
            "and" -> ctx.mkAnd(toBoolExpr(left), toBoolExpr(right))
            "or" -> ctx.mkOr(toBoolExpr(left), toBoolExpr(right))
            "xor" -> ctx.mkXor(toBoolExpr(left), toBoolExpr(right))
            "implies" -> ctx.mkImplies(toBoolExpr(left), toBoolExpr(right))

            else -> throw UnsupportedOperationException("Unsupported operator: ${exp.operator}")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun visitPrefix(exp: PrefixExp): Expr<*> {
        val operand = exp.operand.accept(this)

        return when (exp.operator) {
            "not" -> ctx.mkNot(toBoolExpr(operand))
            "-" -> ctx.mkUnaryMinus(toArithExpr(operand))
            else -> throw UnsupportedOperationException("Unsupported prefix operator: ${exp.operator}")
        }
    }

    // === Type coercion helpers ===

    @Suppress("UNCHECKED_CAST")
    private fun toArithExpr(expr: Expr<*>): ArithExpr<out ArithSort> {
        return when (expr) {
            is ArithExpr<*> -> expr as ArithExpr<out ArithSort>
            is IntExpr -> expr
            is RealExpr -> expr
            else -> throw IllegalArgumentException(
                "Expected arithmetic expression but got: ${expr.sort}"
            )
        }
    }

    private fun toBoolExpr(expr: Expr<*>): BoolExpr {
        return when (expr) {
            is BoolExpr -> expr
            else -> throw IllegalArgumentException(
                "Expected boolean expression but got: ${expr.sort}"
            )
        }
    }

    companion object {
        /**
         * Convenience method: parse an OCL expression and translate to Z3.
         *
         * @param oclText The OCL expression text
         * @param ctx The Z3 context
         * @param variables Variable declarations
         * @return The translated Z3 expression
         */
        fun translate(oclText: String, ctx: Context, variables: Map<String, Expr<*>>): Expr<*> {
            val ast = OclParser.parse(oclText)
            val translator = OclToZ3Translator(ctx, variables)
            return ast.accept(translator)
        }
    }
}
