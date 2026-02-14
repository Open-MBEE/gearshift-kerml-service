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
package org.openmbee.gearshift.kerml.generator

import org.openmbee.gearshift.generated.interfaces.*
import org.openmbee.mdm.framework.runtime.MDMObject

/**
 * Helper for generating inline expression text (without indentation).
 *
 * Used by multiplicity bounds, value parts, and other contexts where
 * expressions must be emitted inline rather than as standalone elements.
 */
object InlineExpressionHelper {

    fun generateInline(expression: Expression, context: GenerationContext): String {
        return when (expression) {
            is LiteralInteger -> expression.value.toString()
            is LiteralRational -> expression.value.toString()
            is LiteralBoolean -> if (expression.value) "true" else "false"
            is LiteralString -> {
                val escaped = expression.value
                    .replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t")
                "\"$escaped\""
            }
            is LiteralInfinity -> "*"
            is NullExpression -> "null"
            is FeatureReferenceExpression -> context.resolveDisplayName(expression.referent)
            is FeatureChainExpression -> generateFeatureChainExpression(expression, context)
            is OperatorExpression -> generateOperatorExpression(expression, context)
            is InvocationExpression -> generateInvocationExpression(expression, context)
            else -> context.resolveDisplayName(expression)
        }
    }

    /**
     * Get expression arguments, preferring the parser-stored `_arguments` raw property
     * over the derived `argument` association end (which requires complex OCL evaluation).
     */
    @Suppress("UNCHECKED_CAST")
    private fun getArguments(expr: Expression): List<Expression> {
        val raw = (expr as? MDMObject)?.getProperty("_arguments") as? List<*>
        if (raw != null && raw.isNotEmpty()) {
            return raw.filterIsInstance<Expression>()
        }
        return if (expr is InstantiationExpression) expr.argument else emptyList()
    }

    private fun generateOperatorExpression(expr: OperatorExpression, context: GenerationContext): String {
        val args = getArguments(expr)
        val op = expr.operator

        return when {
            // Conditional: if a ? b else c
            op == "if" && args.size == 3 -> {
                val cond = generateInline(args[0], context)
                val thenExpr = generateInline(args[1], context)
                val elseExpr = generateInline(args[2], context)
                "if $cond ? $thenExpr else $elseExpr"
            }
            // Unary prefix: not a, -a
            args.size == 1 -> {
                val operand = generateInline(args[0], context)
                val needsParens = args[0] is OperatorExpression
                if (needsParens) "$op ($operand)" else "$op $operand"
            }
            // Binary infix: a + b, a and b, etc.
            args.size == 2 -> {
                val left = generateInline(args[0], context)
                val right = generateInline(args[1], context)
                val leftParens = needsParentheses(args[0], op, isRight = false)
                val rightParens = needsParentheses(args[1], op, isRight = true)
                val leftStr = if (leftParens) "($left)" else left
                val rightStr = if (rightParens) "($right)" else right
                "$leftStr $op $rightStr"
            }
            else -> op
        }
    }

    private fun generateFeatureChainExpression(expr: FeatureChainExpression, context: GenerationContext): String {
        val args = getArguments(expr)
        val targetName = (expr as? MDMObject)?.getProperty("_targetFeatureName") as? String

        return if (args.isNotEmpty() && targetName != null) {
            val source = generateInline(args[0], context)
            "$source.$targetName"
        } else if (targetName != null) {
            targetName
        } else {
            context.resolveDisplayName(expr)
        }
    }

    private fun generateInvocationExpression(expr: InvocationExpression, context: GenerationContext): String {
        val funcName = (expr as? MDMObject)?.getProperty("_functionName") as? String
            ?: try { context.resolveDisplayName(expr.instantiatedType) } catch (_: Exception) { "?" }
        val args = getArguments(expr)
        if (args.isEmpty()) return funcName

        // For arrow operations (collect, select, etc.), first arg is source
        val sourceArg = args.first()
        val remainingArgs = args.drop(1)
        val source = generateInline(sourceArg, context)

        return if (remainingArgs.isNotEmpty()) {
            val argStr = remainingArgs.joinToString(", ") { generateInline(it, context) }
            "$source->$funcName($argStr)"
        } else {
            "$source->$funcName()"
        }
    }

    /**
     * Determine if parentheses are needed around a sub-expression based on
     * operator precedence.
     */
    private fun needsParentheses(arg: Expression, parentOp: String, isRight: Boolean): Boolean {
        if (arg !is OperatorExpression) return false
        val childPrec = precedence(arg.operator)
        val parentPrec = precedence(parentOp)
        // Lower precedence needs parens; same precedence on right side needs parens for non-associative ops
        return childPrec < parentPrec || (childPrec == parentPrec && isRight)
    }

    private fun precedence(op: String): Int = when (op) {
        "or", "||" -> 1
        "xor" -> 2
        "and", "&&" -> 3
        "implies" -> 4
        "==", "!=", "===", "!==" -> 5
        "<", ">", "<=", ">=" -> 6
        "..", "..^", "^..^" -> 7
        "+", "-" -> 8
        "*", "/", "%" -> 9
        "**" -> 10
        "not", "-" -> 11  // unary
        else -> 0
    }
}
